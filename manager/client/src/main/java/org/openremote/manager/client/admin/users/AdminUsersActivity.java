/*
 * Copyright 2016, OpenRemote Inc.
 *
 * See the CONTRIBUTORS.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openremote.manager.client.admin.users;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import org.openremote.manager.client.admin.AbstractAdminActivity;
import org.openremote.manager.client.admin.AdminView;
import org.openremote.manager.client.admin.TenantArrayMapper;
import org.openremote.manager.client.admin.UserArrayMapper;
import org.openremote.manager.client.admin.navigation.AdminNavigation;
import org.openremote.manager.client.event.bus.EventBus;
import org.openremote.manager.client.event.bus.EventRegistration;
import org.openremote.manager.client.i18n.ManagerMessages;
import org.openremote.manager.client.mvp.AppActivity;
import org.openremote.manager.client.service.RequestService;
import org.openremote.manager.shared.security.Tenant;
import org.openremote.manager.shared.security.TenantResource;
import org.openremote.manager.shared.security.User;
import org.openremote.manager.shared.security.UserResource;

import javax.inject.Inject;
import java.util.Collection;

import static org.openremote.manager.client.http.RequestExceptionHandler.handleRequestException;

public class AdminUsersActivity
    extends AbstractAdminActivity<AdminUsersPlace, AdminUsers>
    implements AdminUsers.Presenter {

    final protected EventBus eventBus;
    final protected ManagerMessages managerMessages;
    final protected PlaceController placeController;
    final protected RequestService requestService;
    final protected TenantResource tenantResource;
    final protected TenantArrayMapper tenantArrayMapper;
    final protected UserResource userResource;
    final protected UserArrayMapper userArrayMapper;

    protected String realm;

    @Inject
    public AdminUsersActivity(AdminView adminView,
                              AdminNavigation.Presenter adminNavigationPresenter,
                              AdminUsers view,
                              EventBus eventBus,
                              ManagerMessages managerMessages,
                              PlaceController placeController,
                              RequestService requestService,
                              TenantResource tenantResource,
                              TenantArrayMapper tenantArrayMapper,
                              UserResource userResource,
                              UserArrayMapper userArrayMapper) {
        super(adminView, adminNavigationPresenter, view);
        this.eventBus = eventBus;
        this.managerMessages = managerMessages;
        this.placeController = placeController;
        this.requestService = requestService;
        this.tenantResource = tenantResource;
        this.tenantArrayMapper = tenantArrayMapper;
        this.userResource = userResource;
        this.userArrayMapper = userArrayMapper;
    }

    @Override
    protected String[] getRequiredRoles() {
        return new String[]{"read:admin"};
    }

    @Override
    protected AppActivity<AdminUsersPlace> init(AdminUsersPlace place) {
        realm = place.getRealm();
        return super.init(place);
    }

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus, Collection<EventRegistration> registrations) {
        super.start(container, eventBus, registrations);

        adminContent.setPresenter(this);

        requestService.execute(
            tenantArrayMapper,
            tenantResource::getAll,
            200,
            tenants -> {
                adminContent.setTenants(tenants, realm);
            },
            ex -> handleRequestException(ex, eventBus, managerMessages)
        );

        if (realm != null) {
            requestService.execute(
                userArrayMapper,
                requestParams -> userResource.getAll(requestParams, realm),
                200,
                adminContent::setUsers,
                ex -> handleRequestException(ex, eventBus, managerMessages)
            );
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adminContent.setPresenter(null);
    }

    @Override
    public void onTenantSelected(String realm) {
        placeController.goTo(new AdminUsersPlace(realm));
    }

    @Override
    public void createUser() {
        placeController.goTo(new AdminUserPlace(realm));
    }

    @Override
    public void onUserSelected(User user) {
        placeController.goTo(new AdminUserPlace(realm, user.getId()));
    }
}
