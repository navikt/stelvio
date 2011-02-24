package no.stelvio.presentation.jsf.validator;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import no.stelvio.domain.person.Pid;
import no.stelvio.presentation.binding.context.MessageContextUtil;

/**
 * FirstInMonthValidator validates whether the date is the first day of month, and if invalid; sets an error message on
 * FacesContext.
 * 
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class FirstInMonthValidator extends AbstractFieldNameValidator {

	/** Invalid message key. */
	public static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.FirstInMonth.INVALID";

	/** Date message key. */
	public static final String TYPE_MESSAGE_ID = "no.stelvio.presentation.validator.Date.TYPE";

	/**
	 * Validates whether the specified parameter is the first day of month.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see Pid
	 */
	public void validateField(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value != null) {
			Date date = parseDateValue(component, value);
			if (!isDateFirstInMonth(date)) {
				Object[] args = { getFieldName() };
				String msg = MessageContextUtil.getMessage(INVALID_MESSAGE_ID, args);
				FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
				throw new ValidatorException(facesMessage);
			}
		}
	}

	/**
	 * Checks whether the date is the first day of month.
	 * 
	 * @param date
	 *            the Date
	 * @return true if the date is the first day of month
	 */
	private boolean isDateFirstInMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		return 1 == dayOfMonth;
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
		String msg = MessageContextUtil.getMessage(TYPE_MESSAGE_ID, args);
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
		throw new ValidatorException(facesMessage);
	}

}
