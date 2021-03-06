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
package org.openremote.manager.client.admin.agent;

import org.openremote.manager.client.admin.AdminContent;
import org.openremote.manager.shared.connector.Connector;

public interface AdminAgent extends AdminContent {

    interface Presenter {

        void create();

        void update();

        void delete();

        void cancel();

        void onConnectorSelected(Connector connector);
    }

    void setPresenter(Presenter presenter);

    void setFormBusy(boolean busy);

    void addFormMessageError(String message);

    void addFormMessageSuccess(String message);

    void clearFormMessagesError();

    void clearFormMessagesSuccess();

    void setConnectors(Connector[] connectors);

    void setAssignedConnector(Connector connector);

    void setName(String name);

    String getName();

    void setNameError(boolean error);

    void setDescription(String description);

    String getDescription();

    void setDescriptionError(boolean error);

    void setAgentEnabled(Boolean enabled);

    boolean getAgentEnabled();

    void setAgentEnabledError(boolean error);

    void enableCreate(boolean enable);

    void enableUpdate(boolean enable);

    void enableDelete(boolean enable);
}
