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
package org.openremote.manager.client.assets.asset;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import org.openremote.manager.client.assets.AssetsDashboardPlace;
import org.openremote.manager.client.assets.browser.AssetBrowser;
import org.openremote.manager.client.assets.browser.AssetBrowsingActivity;
import org.openremote.manager.client.event.bus.EventBus;
import org.openremote.manager.client.event.bus.EventRegistration;
import org.openremote.manager.client.service.RequestService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.logging.Logger;

public class AssetActivity
    extends AssetBrowsingActivity<AssetView, AssetPlace>
    implements AssetView.Presenter {

    private static final Logger LOG = Logger.getLogger(AssetActivity.class.getName());

    final PlaceController placeController;
    final EventBus eventBus;
    final RequestService requestService;

    @Inject
    public AssetActivity(AssetView view,
                         AssetBrowser.Presenter assetBrowserPresenter,
                         PlaceController placeController,
                         EventBus eventBus,
                         RequestService requestService) {
        super(view, assetBrowserPresenter);
        this.placeController = placeController;
        this.eventBus = eventBus;
        this.requestService = requestService;
    }

    @Override
    public void start(AcceptsOneWidget container, EventBus eventBus, Collection<EventRegistration> registrations) {
        super.start(container, eventBus, registrations);
    }

    @Override
    protected void startCreateAsset() {
        asset = new Asset();
        asset.setDisplayName("My New Asset");
        onAssetReady();
    }

    @Override
    protected void onAssetReady() {
        view.setDisplayName(asset.getDisplayName());
        // TODO Write data
    }

    @Override
    protected void onAssetsDeselected() {
        placeController.goTo(new AssetsDashboardPlace());
    }

    @Override
    protected void onAssetSelectionChange(Asset newSelection) {
        placeController.goTo(new AssetPlace(newSelection.getId()));
    }

    @Override
    protected void onBeforeAssetLoad() {
        view.setFormBusy(true);
    }
}
