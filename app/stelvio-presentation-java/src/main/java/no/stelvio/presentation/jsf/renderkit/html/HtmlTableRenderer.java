package no.stelvio.presentation.jsf.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlDataTable;

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
				super.encodeChildren(facesContext, component);
			}
		} else {
			super.encodeChildren(facesContext, component);
		}
	}
}
