package no.stelvio.presentation.jsf.taglib.menu.components;

import javax.faces.component.UIPanel;

/**
 * The MenuComponent contains one or several MenuItem as its children. 
 * This class holds properties for the Menu component, like style class
 * to be used on the elements of the menu.
 * 
 * @author person4f9bc5bd17cc, Accenture
 * @version $id$
 */
public class MenuComponent extends UIPanel {
	
	/**
	 * The standard component type for this component.
	 */
	public static final String COMPONENT_TYPE = "no.nav.Menu";
	
	private String rootStyleClass;
	private String childStyleClass;
	private String rootInlineStyle;
	private String childInlineStyle;
	
	/**
	 * Default constructor for this class, sets the Renderer type
	 * of this component
	 *
	 */
	public MenuComponent() {
		setRendererType(COMPONENT_TYPE);
	}

	/**
	 * @return the childStyleClass
	 */
	public String getChildStyleClass() {
		return childStyleClass;
	}

	/**
	 * @param childStyleClass the childStyleClass to set
	 */
	public void setChildStyleClass(String childStyleClass) {
		if (rootStyleClass != null && !rootStyleClass.equals("")) {
			this.childStyleClass = childStyleClass;
		}
	}

	/**
	 * @return the rootStyleClass
	 */
	public String getRootStyleClass() {
		return rootStyleClass;
	}

	/**
	 * @param rootStyleClass the rootStyleClass to set
	 */
	public void setRootStyleClass(String rootStyleClass) {
		if (rootStyleClass != null && !rootStyleClass.equals("")) {
			this.rootStyleClass = rootStyleClass;
		}
	}

	/**
	 * @return the childInlineStyle
	 */
	public String getChildInlineStyle() {
		return childInlineStyle;
	}

	/**
	 * @param childInlineStyle the childInlineStyle to set
	 */
	public void setChildInlineStyle(String childInlineStyle) {
		if (childInlineStyle != null && !childInlineStyle.equals("")) {
			this.childInlineStyle = childInlineStyle;
		}
	}

	/**
	 * @return the rootInlineStyle
	 */
	public String getRootInlineStyle() {
		return rootInlineStyle;
	}

	/**
	 * @param rootInlineStyle the rootInlineStyle to set
	 */
	public void setRootInlineStyle(String rootInlineStyle) {
		if (rootInlineStyle != null && !rootInlineStyle.equals("")) {
			this.rootInlineStyle = rootInlineStyle;
		}
	}
}