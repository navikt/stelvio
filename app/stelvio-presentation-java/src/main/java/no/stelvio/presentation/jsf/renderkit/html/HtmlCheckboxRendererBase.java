/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package no.stelvio.presentation.jsf.renderkit.html;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * @author personc7e15cb5f7a6 (latest modification by $Author: lu4242 $)
 * @author person2f6eb49b5744
 * @version $Revision: 684924 $ $Date: 2008-08-11 15:59:26 -0500 (Mon, 11 Aug 2008) $
 */
public class HtmlCheckboxRendererBase extends org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlCheckboxRendererBase {
	/**
	 * Renders the input item
	 * 
	 * @return the 'id' value of the rendered element
	 */
	protected String renderCheckbox(FacesContext facesContext, UIComponent uiComponent, String value, boolean disabled,
			boolean checked, boolean renderId, Integer itemNum) throws IOException {
		String clientId = uiComponent.getClientId(facesContext);

		String itemId = (itemNum == null) ? null : clientId + NamingContainer.SEPARATOR_CHAR + itemNum;

		ResponseWriter writer = facesContext.getResponseWriter();

		writer.startElement(HTML.INPUT_ELEM, uiComponent);

		if (itemId != null) {
			writer.writeAttribute(HTML.ID_ATTR, itemId, null);
		}
		writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_CHECKBOX, null);
		writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

		if (renderId) {
			HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);
		}

		if (checked) {
			writer.writeAttribute(HTML.CHECKED_ATTR, org.apache.myfaces.shared_tomahawk.renderkit.html.HTML.CHECKED_ATTR, null);
		}

		if (disabled) {
			writer.writeAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR, null);
		}

		if ((value != null) && (value.length() > 0)) {
			writer.writeAttribute(HTML.VALUE_ATTR, value, null);
		}

		if (uiComponent instanceof UISelectBoolean) {
			HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
		} else {
			HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
		}
		if (isDisabled(facesContext, uiComponent)) {
			writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
		}
		
		// render clientbehavior (for <f:ajax> support)
		if (uiComponent instanceof ClientBehaviorHolder)
        {
			Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) uiComponent).getClientBehaviors();
            HtmlRendererUtils.renderBehaviorizedEventHandlers(facesContext, writer, uiComponent, behaviors);
            HtmlRendererUtils.renderBehaviorizedFieldEventHandlers(facesContext, writer, uiComponent, behaviors);
        }	


		writer.endElement(HTML.INPUT_ELEM);

		return itemId;
	}
}