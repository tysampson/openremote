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
package org.openremote.manager.client.app;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Provider;
import org.openremote.manager.client.assets.browser.AssetBrowser;
import org.openremote.manager.client.assets.browser.AssetBrowserPresenter;
import org.openremote.manager.client.event.GoToPlaceEvent;
import org.openremote.manager.client.event.bus.EventBus;
import org.openremote.manager.client.toast.Toast;
import org.openremote.manager.client.toast.Toasts;
import org.openremote.manager.shared.Constants;
import org.openremote.manager.shared.event.ui.ShowFailureEvent;
import org.openremote.manager.shared.event.ui.ShowInfoEvent;

import javax.inject.Inject;

public class AppControllerImpl implements AppController, AppView.Presenter {

    private final AppView appView;
    private final AssetBrowser.Presenter assetBrowserPresenter;
    private final PlaceController placeController;
    private final PlaceHistoryHandler placeHistoryHandler;

    @Inject
    public AppControllerImpl(PlaceController placeController,
                             Provider<HeaderPresenter> headerPresenterProvider,
                             Provider<FooterPresenter> footerPresenterProvider,
                             AssetBrowser.Presenter assetBrowserPresenter,
                             PlaceHistoryHandler placeHistoryHandler,
                             EventBus eventBus,
                             AppView appView,
                             Toasts toasts,
                             AppInitializer appInitializer) { // AppInitializer is needed so that activities are mapped to views

        this.appView = appView;
        this.assetBrowserPresenter = assetBrowserPresenter;
        this.placeController = placeController;
        this.placeHistoryHandler = placeHistoryHandler;

        // Configure layout as not using activity mapper (it's static)
        HeaderPresenter headerPresenter = headerPresenterProvider.get();
        appView.getHeaderPanel().setWidget(headerPresenter.getView());
        FooterPresenter footerPresenter = footerPresenterProvider.get();
        appView.getFooterPanel().setWidget(footerPresenter.getView());

        appView.getSidebarPanel().setWidget(assetBrowserPresenter.getView());

        eventBus.register(
            GoToPlaceEvent.class,
            event -> appView.updateLayout(event.getPlace())
        );

        eventBus.register(
            ShowInfoEvent.class,
            event -> toasts.showToast(
                new Toast(Toast.Type.INFO, event.getText(), Toast.INFO_DEFAULT_MAX_AGE)
            )
        );

        eventBus.register(
            ShowFailureEvent.class,
            event -> {
                Toast.Type type = event.getDurationMillis() == ShowFailureEvent.DURABLE
                    ? Toast.Type.DURABLE_FAILURE : Toast.Type.FAILURE;
                toasts.showToast(
                    new Toast(type, event.getText(), event.getDurationMillis())
                );
            }
        );
    }

    @Override
    public AppView getView() {
        return appView;
    }

    @Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }

    @Override
    public void start() {
        Window.setTitle(Constants.MANAGE_NAME);
        RootPanel.get().add(appView);
        appView.setPresenter(this);
        placeHistoryHandler.handleCurrentHistory();
    }
}
