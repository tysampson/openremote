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
package org.openremote.manager.client.user;

import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import org.openremote.manager.client.ManagerHistoryMapper;
import org.openremote.manager.client.event.UserChangeEvent;
import org.openremote.manager.client.event.bus.EventBus;
import org.openremote.manager.client.i18n.ManagerMessages;
import org.openremote.manager.client.service.SecurityService;
import org.openremote.manager.shared.event.ui.ShowFailureEvent;

import java.util.logging.Logger;

public class UserControlsPresenter implements UserControls.Presenter {

    private static final Logger LOG = Logger.getLogger(UserControlsPresenter.class.getName());

    final protected UserControls view;
    final protected SecurityService securityService;
    final protected PlaceController placeController;
    final protected ManagerHistoryMapper managerHistoryMapper;

    @Inject
    public UserControlsPresenter(UserControls view,
                                 SecurityService securityService,
                                 PlaceController placeController,
                                 ManagerHistoryMapper managerHistoryMapper,
                                 ManagerMessages managerMessages,
                                 EventBus eventBus) {
        this.view = view;
        this.securityService = securityService;
        this.placeController = placeController;
        this.managerHistoryMapper = managerHistoryMapper;

        view.setPresenter(this);

        updateView();

        eventBus.register(UserChangeEvent.class, event -> {
            if (event.getUsername() == null) {
                eventBus.dispatch(new ShowFailureEvent(managerMessages.sessionTimedOut()));
            } else {
                updateView();
            }
        });
    }

    @Override
    public UserControls getView() {
        return view;
    }

    @Override
    public void doLogout() {
        securityService.logout();
    }

    protected void updateView() {
        view.setUserDetails(
            securityService.getParsedToken().getPreferredUsername(),
            securityService.getParsedToken().getName(),
            managerHistoryMapper.getToken(new UserAccountPlace()),
            securityService.hasResourceRole("manage-account", "account")
        );
    }

}
