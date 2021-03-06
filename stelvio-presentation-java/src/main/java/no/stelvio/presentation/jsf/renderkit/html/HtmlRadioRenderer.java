/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package no.stelvio.presentation.jsf.renderkit.html;

import java.io.IOException;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.custom.radio.HtmlRadio;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;


/**
 * Added because the need of extended another RndererBase ({@link HtmlRadioRendererBase}) than the default one.
 * 
 * @JSFRenderer
 *   renderKitId = "HTML_BASIC"
 *   family = "org.apache.myfaces.Radio"
 *   type = "org.apache.myfaces.Radio"
 *    
 * @JSFRenderer
 *   renderKitId = "HTML_BASIC"
 *   family = "javax.faces.SelectOne"
 *   type = "org.apache.myfaces.Radio"
 * 
 * @version $Revision: 685654 $ $Date: 2008-08-13 14:57:50 -0500 (Wed, 13 Aug 2008) $
 */
public class HtmlRadioRenderer
        extends HtmlRadioRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlRadioRenderer.class);

    private static final String LAYOUT_SPREAD = "spread";

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");

        if (component instanceof HtmlRadio)
        {
            renderRadio(context, (HtmlRadio)component);
        }
        else if (HtmlRendererUtils.isDisplayValueOnly(component))
        {
            HtmlRendererUtils.renderDisplayValueOnlyForSelects(context, component);
        }
        else if (component instanceof UISelectOne)
        {
            String layout = getLayout(component);
            if (layout != null && layout.equals(LAYOUT_SPREAD))
            {
                return; //radio inputs are rendered by spread radio components
            }
            else
            {
                super.encodeEnd(context, component);
            }
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }

    @SuppressWarnings("unchecked")
	protected void renderRadio(FacesContext facesContext, HtmlRadio radio) throws IOException
    {
        String forAttr = radio.getFor();
        if (forAttr == null)
        {
            throw new IllegalStateException("mandatory attribute 'for'");
        }
        int index = radio.getIndex();
        if (index < 0)
        {
            throw new IllegalStateException("positive index must be given");
        }

        UIComponent uiComponent = radio.findComponent(forAttr);
        if (uiComponent == null)
        {
            throw new IllegalStateException("Could not find component '" + forAttr + "' (calling findComponent on component '" + radio.getClientId(facesContext) + "')");
        }
        if (!(uiComponent instanceof UISelectOne))
        {
            throw new IllegalStateException("UISelectOne expected");
        }

        UISelectOne uiSelectOne = (UISelectOne)uiComponent;
        Converter converter;
        List selectItemList = RendererUtils.getSelectItemList(uiSelectOne);
        if (index >= selectItemList.size())
        {
            throw new IndexOutOfBoundsException("index " + index + " >= " + selectItemList.size());
        }

        try
        {
            converter = RendererUtils.findUIOutputConverter(facesContext, uiSelectOne);
        }
        catch (FacesException e)
        {
            converter = null;
        }

        Object currentValue = RendererUtils.getObjectValue(uiSelectOne);
        currentValue
            = RendererUtils.getConvertedStringValue(facesContext, uiSelectOne,
                                                    converter, currentValue);
        SelectItem selectItem = (SelectItem)selectItemList.get(index);
        String itemStrValue
            = RendererUtils.getConvertedStringValue(facesContext, uiSelectOne,
                                                    converter,
                                                    selectItem.getValue());

        ResponseWriter writer = facesContext.getResponseWriter();

        //Render the radio component
        String itemId = renderRadio(facesContext,
                uiSelectOne,
                itemStrValue,
                selectItem.isDisabled(),
                itemStrValue.equals(currentValue),
                false,
                index);        
        
        //Render the
        // label element after the input
        boolean componentDisabled = isDisabled(facesContext, uiSelectOne);
        boolean itemDisabled = selectItem.isDisabled();
        boolean disabled = (componentDisabled || itemDisabled);

        HtmlRendererUtils.renderLabel(writer, uiSelectOne, itemId, selectItem, disabled);
    }


    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        if (!UserRoleUtils.isEnabledOnUserRole(uiComponent))
        {
            return true;
        }
        else
        {
            return super.isDisabled(facesContext, uiComponent);
        }
    }
}