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
package org.openremote.manager.client.widget;

import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.ui.ComplexPanel;

public class FormLabel extends ComplexPanel {

    protected LabelElement labelElement;
    protected SpanElement requiredElement;
    protected boolean required;

    public FormLabel() {
        setElement(Document.get().createElement(DivElement.TAG));
        getElement().addClassName("larger or-FormLabel theme-FormLabel");

        labelElement = (LabelElement) Document.get().createElement(LabelElement.TAG);
        labelElement.setHtmlFor(Document.get().createUniqueId());
        getElement().appendChild(labelElement);

        requiredElement= (SpanElement) Document.get().createElement(SpanElement.TAG);
        requiredElement.addClassName("required");
        requiredElement.setInnerText("*");
        requiredElement.getStyle().setVisibility(Style.Visibility.HIDDEN);
        getElement().appendChild(requiredElement);
    }

    public String getText() {
        return labelElement.getInnerText();
    }

    public void setText(String text) {
        labelElement.setInnerText(text);
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
        requiredElement.getStyle().setVisibility(required ? Style.Visibility.VISIBLE : Style.Visibility.HIDDEN);
    }

    public String getFormFieldId() {
        return labelElement.getHtmlFor();
    }
}
