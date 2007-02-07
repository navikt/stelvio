/**
 * 
 */
package no.stelvio.presentation.jsf.taglib.help;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.webapp.FacetTag;

import org.apache.myfaces.shared_impl.renderkit.html.HTML;

/**
 * @author utvikler
 *
 */
public class HelpDisplayAreaComponent extends HtmlPanelGrid {

	public static final String HELP_DISPLAY_AREA_ID = "no.stelvio.HelpDisplayAreaComponent.ID";
	private String headingText = null;
	
	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponentBase#encodeEnd(javax.faces.context.FacesContext)
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		createChildren(context);
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement(HTML.DIV_ELEM, this);
		writer.writeAttribute("id", HELP_DISPLAY_AREA_ID, null);
		super.encodeBegin(context);
	}

	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponentBase#encodeEnd(javax.faces.context.FacesContext)
	 */
	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		super.encodeEnd(context);
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement(HTML.DIV_ELEM);
	}
	/**
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	private void createChildren(FacesContext context) {
		this.setId("help");
		
//		UIComponent header = this.getFacet("header");
		
//		List<UIComponent> children = this.getChildren();
		HtmlOutputText headerText = new HtmlOutputText();
		HtmlOutputText helpText = new HtmlOutputText();
//		UIColumn column = new UIColumn();
		headerText.setValue(this.getHeadingText());
//		column.setHeader(headerText);
		helpText.setValue("");
		helpText.setId("hjelpeteksten");
//		column.getChildren().add(headerText);
//		column.getChildren().add(helpText);
//		children.add(column);

		this.getFacets().put("header", headerText);
		this.getChildren().add(helpText);

	}

	/**
	 * @return the headingText
	 */
	public String getHeadingText() {
		return headingText;
	}

	/**
	 * @param headingText the headingText to set
	 */
	public void setHeadingText(String headingText) {
		this.headingText = headingText;
	}
	
}
