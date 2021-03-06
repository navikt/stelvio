package no.stelvio.presentation.jsf.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.binding.context.MessageContextUtil;

/**
 * DoubleRangeValidator validates whether the specified parameter is a valid number range If invalid sets an error message on
 * FacesContext. DoubleRangeValidator is a modified copy of javax.faces.validator.DoubleRangeValidator
 * 
 * @version $Id$
 */
public class DoubleRangeValidator extends AbstractFieldNameValidator {

	/** Not in range message key. */
	public static final String NOT_IN_RANGE_MESSAGE_ID = "no.stelvio.presentation.validator.NumberRange.NOT_IN_RANGE";

	/** Minimum message key. */
	public static final String MINIMUM_MESSAGE_ID = "no.stelvio.presentation.validator.NumberRange.MINIMUM";

	/** Maximum message key. */
	public static final String MAXIMUM_MESSAGE_ID = "no.stelvio.presentation.validator.NumberRange.MAXIMUM";

	/** Number range message key. */
	public static final String TYPE_MESSAGE_ID = "no.stelvio.presentation.validator.NumberRange.TYPE";

	private Double minimum = null;

	private Double maximum = null;

	/**
	 * Creates a new instance of DoubleRangeValidator.
	 */
	public DoubleRangeValidator() {
	}

	/**
	 * Sets double range maximum.
	 * 
	 * @param maximum
	 *            Maximum value.
	 */
	public DoubleRangeValidator(Double maximum) {
		this.maximum = maximum;
	}

	/**
	 * Sets double range.
	 * 
	 * @param maximum
	 *            Maximum value.
	 * @param minimum
	 *            Minimum value.
	 */
	public DoubleRangeValidator(Double maximum, Double minimum) {
		this.maximum = maximum;
		this.minimum = minimum;
	}

	/**
	 * Validates whether the specified parameter is a number between minimum and maximum values.
	 * 
	 * {@inheritDoc}
	 */
	protected void validateField(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
		if (value == null) {
			return;
		}

		Double dvalue = parseDoubleValue(uiComponent, value);
		if (minimum != null && maximum != null) {
			if (dvalue < minimum || dvalue > maximum) {
				Object[] args = { getFieldName(), minimum, maximum };
				throw new ValidatorException(MessageContextUtil.getFacesMessage(NOT_IN_RANGE_MESSAGE_ID, args));
			}
		} else if (minimum != null) {
			if (dvalue < minimum) {
				Object[] args = { getFieldName(), minimum };
				throw new ValidatorException(MessageContextUtil.getFacesMessage(MINIMUM_MESSAGE_ID, args));
			}
		} else if (maximum != null) {
			if (dvalue > maximum) {
				Object[] args = { getFieldName(), maximum };
				throw new ValidatorException(MessageContextUtil.getFacesMessage(MAXIMUM_MESSAGE_ID, args));
			}
		}
	}

	/**
	 * Parses the object value to a Date object.
	 * 
	 * @param uiComponent
	 *            the UIComponent
	 * @param value
	 *            the value to parse
	 * @return the parsed Date
	 * @throws ValidatorException
	 *             Throws ValidatorException
	 */
	private Double parseDoubleValue(UIComponent uiComponent, Object value) throws ValidatorException {
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		try {
			return Double.parseDouble(value.toString());
		} catch (NumberFormatException e) {
			Object[] args = { uiComponent.getId() };
			throw new ValidatorException(MessageContextUtil.getFacesMessage(TYPE_MESSAGE_ID, args));
		}
	}

	/**
	 * Get maximum.
	 *  
	 * @return the maximum
	 */
	public Double getMaximum() {
		return maximum != null ? maximum : Double.MAX_VALUE;
	}

	/**
	 * Set maximum.
	 * 
	 * @param maximum
	 *            the maximum to set
	 */
	public void setMaximum(Double maximum) {
		this.maximum = maximum;
	}

	/**
	 * Get minimum.
	 * 
	 * @return the minimum
	 */
	public Double getMinimum() {
		return minimum != null ? minimum : Double.MIN_VALUE;
	}

	/**
	 * Set minimum.
	 * 
	 * @param minimum
	 *            the minimum to set
	 */
	public void setMinimum(Double minimum) {
		this.minimum = minimum;
	}

	// RESTORE/SAVE STATE

	/**
	 * {@inheritDoc}
	 */
	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = maximum;
		values[2] = minimum;
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		maximum = (Double) values[1];
		minimum = (Double) values[2];
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Autogenerated by Eclipse.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maximum == null) ? 0 : maximum.hashCode());
		result = prime * result + ((minimum == null) ? 0 : minimum.hashCode());
		return result;
	}

	// MISC
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof DoubleRangeValidator)) {
			return false;
		} else {
			final DoubleRangeValidator doubleRangeValidator = (DoubleRangeValidator) o;

			if ((maximum != null ? !maximum.equals(doubleRangeValidator.maximum) : doubleRangeValidator.maximum != null)
					|| (minimum != null ? !minimum.equals(doubleRangeValidator.minimum) : doubleRangeValidator.minimum != null)) {
				return false;
			} else {
				return true;
			}
		}
	}
}
