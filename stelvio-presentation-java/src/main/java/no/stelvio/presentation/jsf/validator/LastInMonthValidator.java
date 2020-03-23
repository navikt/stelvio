package no.stelvio.presentation.jsf.validator;

import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import no.stelvio.domain.person.Pid;
import no.stelvio.presentation.binding.context.MessageContextUtil;

/**
 * PidValidator validates whether the date is the last day of month, and if invalid; sets an error message on FacesContext.
 * 
 * @version $Id$
 */
public class LastInMonthValidator extends AbstractFieldNameValidator {
	/** LastInMonth invalid message key. */
	public static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.LastInMonth.INVALID";

	/** Date message key. */
	public static final String TYPE_MESSAGE_ID = "no.stelvio.presentation.validator.Date.TYPE";

	/**
	 * Validates whether the specified parameter is the last day of month.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see Pid
	 */
	public void validateField(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value != null) {
			Date date = parseDateValue(component, value);
			if (!isDateLastInMonth(date)) {
				Object[] args = { getFieldName() };
				throw new ValidatorException(MessageContextUtil.getFacesMessage(INVALID_MESSAGE_ID, args));
			}
		}
	}

	/**
	 * Checks whether the date is the last day of month.
	 * 
	 * @param date
	 *            the Date
	 * @return true if the date is the last day of month
	 */
	private boolean isDateLastInMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int actualLastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (actualLastDayOfMonth == dayOfMonth) {
			return true;
		}

		return false;
	}

	/**
	 * Parses the object value to a Date object.
	 * 
	 * @param uiComponent
	 *            the UIComponent
	 * @param value
	 *            the value to parse
	 * @return the parsed Date
	 */
	private Date parseDateValue(UIComponent uiComponent, Object value) {
		if (value instanceof Date) {
			return (Date) value;
		}
		Object[] args = { uiComponent.getId() };
		throw new ValidatorException(MessageContextUtil.getFacesMessage(TYPE_MESSAGE_ID, args));
	}

}
