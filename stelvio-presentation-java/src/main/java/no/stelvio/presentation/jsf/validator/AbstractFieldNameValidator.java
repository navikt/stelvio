package no.stelvio.presentation.jsf.validator;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * AbstractFieldNameValidator abstract class implementing mandatory fields on validators with field names.
 * 
 * @version $Id$
 */
public abstract class AbstractFieldNameValidator implements Validator, StateHolder {

	private boolean newTransientValue = false;

	/** The fieldName is used to get a nice field name on the error message. Default will be the component id. */
	private String fieldName = null;

	/**
	 * {@inheritDoc}
	 */
	public final void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (fieldName == null) {
			fieldName = component.getId();
		}

		validateField(context, component, value);
	}

	/**
	 * Template pattern to force the use of validateField instead of validate.
	 * 
	 * @param context
	 *            the FacesContext
	 * @param component
	 *            the component
	 * @param value
	 *            the value to be validated
	 * @throws ValidatorException
	 *             Validator exception
	 */
	protected abstract void validateField(FacesContext context, UIComponent component, Object value) throws ValidatorException;

	/**
	 * {@inheritDoc}
	 */
	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = fieldName;
		values[1] = newTransientValue;
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		fieldName = (String) values[0];
		newTransientValue = (Boolean) values[1];
	}

	/**
	 * Get field name.
	 * 
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Set field name.
	 * 
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Is transient.
	 * 
	 * @return the newTransientValue
	 */
	public boolean isTransient() {
		return newTransientValue;
	}

	/**
	 * Set transient.
	 * 
	 * @param newTransientValue
	 *            the newTransientValue to set
	 */
	public void setTransient(boolean newTransientValue) {
		this.newTransientValue = newTransientValue;
	}

}
