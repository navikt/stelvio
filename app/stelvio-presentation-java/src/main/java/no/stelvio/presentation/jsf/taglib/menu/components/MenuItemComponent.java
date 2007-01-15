package no.stelvio.presentation.jsf.taglib.menu.components;

import javax.faces.component.UIPanel;

/**
 * MenuItemComponent class is used to hold items in the menu and
 * each items properties like style class, text to display and 
 * which action to be invoced when the menu item is selected.
 * 
 * @author person4f9bc5bd17cc
 * @version $id$
 */
public class MenuItemComponent extends UIPanel {
	
	/**
	 * The standard component type for this component
	 */
	public static final String COMPONENT_TYPE = "no.nav.MenuItem";

	private String styleClass;
	private String inlineStyle;
	private String leadText;
	private String action;
	
	/**
	 * Contructor for MenuItemComponent. Sets the Renderer class of this
	 * component
	 *
	 */
	public MenuItemComponent() {
		setRendererType(COMPONENT_TYPE);
	}
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the leadText
	 */
	public String getLeadText() {
		return leadText;
	}

	/**
	 * @param leadText the leadText to set
	 */
	public void setLeadText(String leadText) {
		this.leadText = leadText;
	}

	/**
	 * @return the styleClass
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * @param styleClass the styleClass to set
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/**
	 * @return the inlineStyle
	 */
	public String getInlineStyle() {
		return inlineStyle;
	}

	/**
	 * @param inlineStyle the inlineStyle to set
	 */
	public void setInlineStyle(String inlineStyle) {
		this.inlineStyle = inlineStyle;
	}
}