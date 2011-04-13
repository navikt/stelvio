package no.stelvio.presentation.jsf.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * HtmlTableRendererBase to avoid a row to be rendered when the table is empty.
 * 
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class HtmlTableRendererBase extends org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase {

	/** {@inheritDoc} */
	public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
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

		afterBody(facesContext, (UIData) component);
	}

}
