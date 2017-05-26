package no.stelvio.presentation.jsf.renderkit.html;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * HtmlTableRenderer to avoid a row to be rendered when the table is empty.
 * 
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class HtmlTableRenderer extends org.apache.myfaces.renderkit.html.ext.HtmlTableRenderer {

	/** {@inheritDoc} */
	public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
		if (component instanceof HtmlDataTable) {
			HtmlDataTable htmlDataTable = (HtmlDataTable) component;
			if (htmlDataTable.isRenderedIfEmpty() || htmlDataTable.getRowCount() > 0) {
				encodeChildrenOverride(facesContext, component);
			}
		} else {
			encodeChildrenOverride(facesContext, component);
		}
	}

	/**
	 * Render the TBODY section of the html table. See also method encodeInnerHtml.
	 * 
	 * @see javax.faces.render.Renderer#encodeChildren(FacesContext, UIComponent)
	 */
	protected void encodeChildrenOverride(FacesContext facesContext, UIComponent component) throws IOException {
		RendererUtils.checkParamValidity(facesContext, component, UIData.class);

		ResponseWriter writer = facesContext.getResponseWriter();

		beforeBody(facesContext, (UIData) component);

		HtmlRendererUtils.writePrettyLineSeparator(facesContext);

		if (!(component instanceof HtmlDataTable) || ((HtmlDataTable) component).getRowCount() > 0) {
			writer.startElement(HTML.TBODY_ELEM, component);
			writer.writeAttribute(HTML.ID_ATTR, component.getClientId(facesContext) + ":tbody_element", null);

			encodeInnerHtml(facesContext, component);

			writer.endElement(HTML.TBODY_ELEM);
		}
		
		// render clientbehavior (for <f:ajax> support)
		if (component instanceof ClientBehaviorHolder)
        {
			Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();
            HtmlRendererUtils.renderBehaviorizedEventHandlers(facesContext, writer, component, behaviors);
            HtmlRendererUtils.renderBehaviorizedFieldEventHandlers(facesContext, writer, component, behaviors);
        }	

		afterBody(facesContext, (UIData) component);
	}
}
