package no.stelvio.presentation.jsf.model;

import javax.faces.model.SelectItem;

/**
 * SelectItemExtended.
 * 
 * @author PC
 *
 */
public class SelectItemExtended extends SelectItem {

	private static final long serialVersionUID = 6208338635270445780L;

	// FIELDS
	private String styleClass;

	/**
	 * Creates a new instance of SelectItemExtended.
	 */
	public SelectItemExtended() {
	}

	/**
	 * Creates a new instance of SelectItemExtended.
	 *
	 * @param value value
	 * @param label label
	 * @param styleClass style class
	 */
	public SelectItemExtended(Object value, String label, String styleClass) {
		super(value, label);
		this.styleClass = styleClass;
	}

	/**
	 * Creates a new instance of SelectItemExtended.
	 *
	 * @param value value
	 * @param label label
	 * @param description description
	 * @param styleClass style class
	 */
	public SelectItemExtended(Object value, String label, String description, String styleClass) {
		super(value, label, description);
		this.styleClass = styleClass;
	}

	/**
	 * Creates a new instance of SelectItemExtended.
	 *
	 * @param value value 
	 * @param label label
	 * @param description description
	 * @param disabled diabled
	 * @param styleClass style class
	 */
	public SelectItemExtended(Object value, String label, String description, boolean disabled, String styleClass) {
		super(value, label, description, disabled);
		this.styleClass = styleClass;
	}

	/**
	 * Get style class.
	 *  
	 * @return style class
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * Set style class.
	 * 
	 * @param styleClass style class
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
}
