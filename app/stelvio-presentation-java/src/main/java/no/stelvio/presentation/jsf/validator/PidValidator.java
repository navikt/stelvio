package no.stelvio.presentation.jsf.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import no.stelvio.domain.person.Pid;
import no.stelvio.presentation.binding.context.MessageContextUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * PidValidator validates whether the specified parameter is a valid Personal Identification number (fodselsnummer), and if
 * invalid; sets an error message on FacesContext.
 * 
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class PidValidator extends AbstractFieldNameValidator {
	/** Invalid message Id. */
	public static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.Pid.INVALID";

	/**
	 * Validates whether the specified parameter is a valid Pid.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see Pid
	 */
	public void validateField(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value != null) {
			if (!Pid.isValidPid(StringUtils.deleteWhitespace(value.toString().replace(".", "")))) {
				Object[] args = { getFieldName() };
				throw new ValidatorException(MessageContextUtil.getFacesMessage(INVALID_MESSAGE_ID, args));
			}
		}
	}
}
