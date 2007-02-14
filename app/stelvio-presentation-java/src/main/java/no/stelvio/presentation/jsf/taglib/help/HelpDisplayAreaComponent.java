package no.stelvio.presentation.jsf.taglib.help;

import java.io.IOException;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.div.Div;

/**
 * Component for the display area of the help text functionality. When a user clicks on a
 * HelpIconComponent in the page, this component is used to display the help text to 
 * the user. 
 * <br>
 * The component is a subclass of HtmlPanelGrid, and all attributes from the HtmlPanelGrid
 * component can be applied to this component as well.
 * <br>
 * This component has two optional attributes:
 * <ul>
 * <li>headingText: use this attribute to supply a heading to the display area</li>
 * <li>defaultText: use this attribute to supply a default text to be displayed in the
 * area when the page is rendered and no help icon has been clicked.</li>
 * </ul>
 * 
 * @author person6045563b8dec, Accenture
 * @version $Id$
 *
 */
public class HelpDisplayAreaComponent extends  HtmlPanelGrid {

	private static final String FACET_HEADER = "header";
	private static final String HELP_DISPLAY_AREA_ID = "no_stelvio_HelpDisplayAreaComponent_ID";

	private String headingText = null;
	private String defaultText = null;
	

	HtmlOutputText headerTextComponent = new HtmlOutputText();
	HtmlOutputText helpTextComponent = new HtmlOutputText();

	/**
	 * Default constructor, add the children of this component. 
	 * The component is a panelgrid consisting of a header text
	 * and a area for displaying the help text.
	 *
	 */
	public HelpDisplayAreaComponent() {
		createChildren();
	}
	
	/**
	 * Encodes this component. Sets the header and default text
	 * 
	 * @param context the current Faces context instance 
	 * @see javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context.FacesContext)
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		headerTextComponent.setValue(headingText);
		helpTextComponent.setValue(defaultText);
		super.encodeBegin(context);
	}
	
	/**
	 * Creates the children of this component. 
	 */
	@SuppressWarnings("unchecked")
	private void createChildren() {
		headerTextComponent = new HtmlOutputText();
		helpTextComponent = new HtmlOutputText();
		Div div = new Div();
		div.setId(HELP_DISPLAY_AREA_ID);
		
		this.getFacets().put(FACET_HEADER, headerTextComponent);
		div.getChildren().add(helpTextComponent);
		this.getChildren().add(div);
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

	/**
	 * @return the defaultText
	 */
	public String getDefaultText() {
		return defaultText;
	}

	/**
	 * @param defaultText the defaultText to set
	 */
	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}
	
}
