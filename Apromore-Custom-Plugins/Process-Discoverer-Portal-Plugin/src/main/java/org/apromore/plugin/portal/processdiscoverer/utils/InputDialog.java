/*-
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2018 - 2021 Apromore Pty Ltd.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

package org.apromore.plugin.portal.processdiscoverer.utils;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apromore.commons.item.Constants;
import org.apromore.commons.item.ItemNameUtils;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class InputDialog {
    /**
     * Bruce added 21.05.2019
     * Display an input dialog
     * @param title: title of the dialog
     * @param message: the message regarding the input to enter
     * @param initialValue: initial value for the input
     * @param valuePattern: the expression pattern to check validity of the input
     * @param allowedValues: message about valid values allowed  
     * @throws IOException 
     * @returnValueHander: callback event listener, notified with onOK (containing return value as string) and onCancel event
     */
    public static void showInputDialog(String title, String message, String initialValue, 
                                EventListener<Event> returnValueHander) throws IOException {
        Window win = (Window) Executions.createComponents(getPageDefination("static/processdiscoverer/zul/inputDialog.zul"), null, null);
        Window dialog = (Window) win.getFellow("inputDialog");
        dialog.setTitle(title);
        Label labelMessage = (Label)dialog.getFellow("labelMessage"); 
        Textbox txtValue = (Textbox)dialog.getFellow("txtValue");
        Label labelError = (Label)dialog.getFellow("labelError"); 
        labelMessage.setValue(message);
        txtValue.setValue(initialValue);
        labelError.setValue("");
        
        dialog.doModal();
        
        ((Button)dialog.getFellow("btnCancel")).addEventListener("onClick", new EventListener<Event>() {
             @Override
             public void onEvent(Event event) throws Exception {
                 dialog.detach();
                 returnValueHander.onEvent( new Event("onCancel"));
             }
         });
         
        ((Button)dialog.getFellow("btnOK")).addEventListener("onClick", new EventListener<Event>() {
             @Override
             public void onEvent(Event event) throws Exception {
                 okHandler(txtValue, labelError, dialog, returnValueHander);
             }
        });

        win.addEventListener("onOK", new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                okHandler(txtValue, labelError, dialog, returnValueHander);
            }
        });
        
    }

    public static void okHandler(Textbox txtValue, Label labelError, Window dialog, EventListener<Event> returnValueHander) throws Exception {
        String allowedValues = Constants.VALID_NAME_MESSAGE;

        if (txtValue.getValue().trim().isEmpty()) {
            labelError.setValue("Please enter a value!");
        }
        else if (!ItemNameUtils.hasValidName(txtValue.getValue())) {
            labelError.setValue("The entered value is not valid! Allowed characters: " + allowedValues);
        }
        else {
            dialog.detach();
            returnValueHander.onEvent( new Event("onOK", null, txtValue.getValue()));
        }

    }
    
    private static PageDefinition getPageDefination(String uri) throws IOException {
		Execution current = Executions.getCurrent();
		PageDefinition pageDefinition=current.getPageDefinitionDirectly(new InputStreamReader(
				InputDialog.class.getClassLoader().getResourceAsStream(uri)), "zul");
		return pageDefinition;
	}
}
