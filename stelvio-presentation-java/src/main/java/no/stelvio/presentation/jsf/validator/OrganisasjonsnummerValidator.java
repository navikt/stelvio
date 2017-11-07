package no.stelvio.presentation.jsf.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.binding.context.MessageContextUtil;

/**
 * OrganisasjonsnummerValidator validates whether the specified parameter is a valid organization number, and if invalid sets an
 * error message on FacesContext.
 * 
 * @author person37c6059e407e (Capgmeini)
 * @version $Id$
 * 
 */
public class OrganisasjonsnummerValidator extends AbstractFieldNameValidator {
	/** Organisasjonsnummer unvalid message key. */
	public static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.Organisasjonsnummer.INVALID";
	/** Organisasjonsnummer length message key. */
	public static final String LENGTH_MESSAGE_ID = "no.stelvio.presentation.validator.Organisasjonsnummer.LENGTH";
	/** Number of length. */
	public static final Integer LENGTH_OF_NUMBER = 9;

	/**
	 * Validates whether the specified parameter is digit and the length is 9.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void validateField(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String num = null;

		if (value != null) {
			Object[] args = { value };
			try {
				num = String.valueOf(value);
				if (num.length() == LENGTH_OF_NUMBER) {
					Integer.parseInt(num);
				} else {
					throw new ValidatorException(MessageContextUtil.getFacesMessage(LENGTH_MESSAGE_ID, args));
				}
			} catch (Exception e) {
				throw new ValidatorException(MessageContextUtil.getFacesMessage(INVALID_MESSAGE_ID, args));
			}
		}
	}
}
