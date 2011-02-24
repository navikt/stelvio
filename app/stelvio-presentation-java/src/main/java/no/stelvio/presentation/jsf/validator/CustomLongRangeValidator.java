package no.stelvio.presentation.jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.myfaces.shared_tomahawk.util.MessageUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * CustomLongRangeValidator.
 * 
 * @author person096a015479bb, capgemini
 */
public class CustomLongRangeValidator extends LongRangeValidator implements Validator, StateHolder {

	private String NOT_IN_RANGE_MESSAGE_ID_CUSTOM = "javax.faces.validator.custom.NOT_IN_RANGE";
	private String arg0;
	private String arg1;
	private String arg2;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {

		Object[] args = { getArg0(), getArg1(), getArg2(), value, getMinimum(), getMaximum(), uiComponent.getId() };

		try {
			super.validate(facesContext, uiComponent, value);
		} catch (ValidatorException ex) {
			FacesMessage msg = MessageUtils.getMessage(LocaleContextHolder.getLocale(),
					LongRangeValidator.NOT_IN_RANGE_MESSAGE_ID, args);
			// if the error is NOT IN RANGE we replace the text with what arg0 is.
			if (ex.getMessage().indexOf(msg.getSummary()) != -1) {
				throw new ValidatorException(MessageUtils.getMessage(LocaleContextHolder.getLocale(),
						NOT_IN_RANGE_MESSAGE_ID_CUSTOM, args));
			} else {
				throw ex;
			}
		}
	}

	/**
	 * Save state.
	 * 
	 * @param context
	 *            context
	 * @return state
	 */
	public Object saveState(FacesContext context) {
		Object[] values = new Object[6];
		values[0] = arg0;
		values[1] = arg1;
		values[2] = arg2;
		values[3] = getMaximum();
		values[4] = getMinimum();
		values[5] = NOT_IN_RANGE_MESSAGE_ID_CUSTOM;

		return values;
	}

	/**
	 * Restore state.
	 * 
	 * @param context
	 *            context
	 * @param state
	 *            state
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		arg0 = (String) values[0];
		arg1 = (String) values[1];
		arg2 = (String) values[2];
		setMaximum((Long) values[3]);
		setMinimum((Long) values[4]);
		NOT_IN_RANGE_MESSAGE_ID_CUSTOM = (String) values[5];

	}

	/**
	 * Set message not in range.
	 * 
	 * @param messageNotInRange
	 *            message not in range
	 */
	public void setMessageNotInRange(String messageNotInRange) {
		if (messageNotInRange != null) {
			NOT_IN_RANGE_MESSAGE_ID_CUSTOM = messageNotInRange;
		}
	}

	/**
	 * Get message not in range.
	 * 
	 * @return message not in range
	 */
	public String getMessageNotInRange() {
		return NOT_IN_RANGE_MESSAGE_ID_CUSTOM;
	}

	/**
	 * Get arg0.
	 * 
	 * @return arg0
	 */
	public String getArg0() {
		return arg0;
	}

	/**
	 * Set arg0.
	 * 
	 * @param arg0
	 *            arg0
	 */
	public void setArg0(String arg0) {
		this.arg0 = arg0;
	}

	/**
	 * Get arg1.
	 * 
	 * @return arg1
	 */
	public String getArg1() {
		return arg1;
	}

	/**
	 * Set arg1.
	 * 
	 * @param arg1
	 *            arg1
	 */
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}

	/**
	 * Get arg2.
	 * 
	 * @return arg2
	 */
	public String getArg2() {
		return arg2;
	}

	/**
	 * Set arg2.
	 * 
	 * @param arg2
	 *            arg2
	 */
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}

}
