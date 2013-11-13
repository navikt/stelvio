package no.stelvio.presentation.jsf.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.binding.context.MessageContextUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * IbanValidator validates whether the specified parameter is a valid IBAN, and if invalid sets an error message on
 * FacesContext.
 * 
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class IbanValidator implements Validator {
	/** Invalid message key. */
	public static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.Iban.INVALID";

	/**
	 * Validates whether the specified parameter is a valid IBAN.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see no.stelvio.common.validation.IbanValidator
	 */
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value != null) {
			if (!no.stelvio.common.validation.IbanValidator.isValidIban(StringUtils.deleteWhitespace(value.toString().replace(
					".", "")))) {
				Object[] args = { value.toString() };
				throw new ValidatorException(MessageContextUtil.getFacesMessage(INVALID_MESSAGE_ID, args));
			}
		}
	}

}
