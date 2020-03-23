package no.stelvio.presentation.jsf.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.binding.context.MessageContextUtil;

/**
 * TextValidator validates whether the specified parameter consists of only text If invalid sets an error message on
 * FacesContext.
 * 
 * @version $Id$
 */
public class TextValidator extends AbstractFieldNameValidator {
	/** Text type  message key. */
	public static final String TYPE_MESSAGE_ID = "no.stelvio.presentation.validator.Text.TYPE";

	/**
	 * Creates a new instance of TextValidator.
	 */
	public TextValidator() {
	}

	/**
	 * Validates whether the specified parameter is a string of only letters. Accepts whitspaces, a-z, æ, æ, å, A-Z, Æ, Ø, Å
	 * 
	 * {@inheritDoc}
	 */
	protected void validateField(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
		if (value == null || !(value instanceof String)) {
			Object[] args = { uiComponent.getId() };
			throw new ValidatorException(MessageContextUtil.getFacesMessage(TYPE_MESSAGE_ID, args));
		}
		Pattern pattern = Pattern.compile("[\\sa-zæøåA-ZÆØÅ]+");
		Matcher matcher = pattern.matcher((String) value);
		if (!matcher.matches()) {
			Object[] args = { uiComponent.getId() };
			throw new ValidatorException(MessageContextUtil.getFacesMessage(TYPE_MESSAGE_ID, args));
		}
	}
}