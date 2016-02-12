package org.openremote.manager.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface AssetsView extends IsWidget {

    interface Presenter {
        void goTo(Place place);

        void getHelloText();
    }

    void setPresenter(Presenter presenter);
}