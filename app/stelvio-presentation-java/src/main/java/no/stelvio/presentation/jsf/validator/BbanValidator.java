/**
 * 
 */
package no.stelvio.presentation.jsf.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.binding.context.MessageContextUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * BbanValidator validates whether the specified parameter is a valid Norwegian bank account number. If invalid sets an error
 * message on FacesContext.
 * 
 * This validator provides the following predefined, overridable error messages:
 * <ul>
 * <li><code>no.stelvio.presentation.jsf.validator.BbanValidator.INVALID</code> - This message is displayed when an illegal
 * value is entered. The value entered must follow the formatting rules for Norwegian bank account numbers.</li>
 * </ul>
 * 
 * The error messages displayed by this validator is overridable. It is possible to use placeholders in the error messages for
 * parameters supplied by the validator.
 * 
 * @see no.stelvio.common.validation.BbanValidator
 * 
 * @author persone38597605f58 (Capgemini)
 * @author person6045563b8dec (Accenture)
 * @version $Id$
 */
public class BbanValidator implements Validator {
	/** NorskKontonummer unvalid message key. */
	public static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.Bban.INVALID";

	/**
	 * Validates whether the specified parameter is a valid norsk kontonummer.
	 * 
	 * {@inheritDoc}}
	 * @see no.stelvio.common.validation.BbanValidator {@inheritDoc}
	 */
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value != null) {
			String valueTrimmed = StringUtils.deleteWhitespace(value.toString().replace(".", ""));
			if (!no.stelvio.common.validation.BbanValidator.isValidNorwegianBban(valueTrimmed)) {
				Object[] args = { value.toString() };
				throw new ValidatorException(MessageContextUtil.getFacesMessage(INVALID_MESSAGE_ID, args));
			}
		}
	}

}
