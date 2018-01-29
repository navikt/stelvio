package no.stelvio.presentation.jsf.model;

import javax.faces.model.SelectItem;

/**
 * Extended {@link SelectItem} component, with style class added
 * 
 * @author persone38597605f58
 *
 */
public class SelectItemExtended extends SelectItem {

	private static final long serialVersionUID = 6208338635270445780L;

	// FIELDS
	private String styleClass;

	// CONSTRUCTORS
	/**
	 * Default empty constructor
	 */
	public SelectItemExtended() {
	}

	/**
	 * Parameterized constructor
	 *  
	 * @param value the value of the select item
	 * @param label the label of the select item
	 * @param styleClass the style class of the select item
	 */
	public SelectItemExtended(Object value, String label, String styleClass) {
		super(value, label);
		this.styleClass = styleClass;
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param value the value of the select item
	 * @param label the label of the select item
	 * @param description the description of the select item
	 * @param styleClass the style class of the select item
	 */
	public SelectItemExtended(Object value, String label, String description, String styleClass) {
		super(value, label, description);
		this.styleClass = styleClass;
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param value the value of the select item
	 * @param label the label of the select item
	 * @param description the description of the select item
	 * @param disabled true if disabled, false otherwise
	 * @param styleClass the style class of the select item
	 */
	public SelectItemExtended(Object value, String label, String description, boolean disabled, String styleClass) {
		super(value, label, description, disabled);
		this.styleClass = styleClass;
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
}