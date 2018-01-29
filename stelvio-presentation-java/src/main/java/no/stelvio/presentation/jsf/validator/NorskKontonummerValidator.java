package no.stelvio.presentation.jsf.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.binding.context.MessageContextUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * NorskKontonummerValidator validates whether the specified parameter is a valid norsk kontonummer, and if invalid sets an
 * error message on FacesContext.
 * 
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class NorskKontonummerValidator implements Validator {
	/** NorskKontonummer unvalid message key. */
	public static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.NorskKontonummer.INVALID";

	/**
	 * Validates whether the specified parameter is a valid norsk kontonummer.
	 * 
	 * {@inheritDoc}
	 */
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value != null) {
			if (!isNorskKontonummer(value)) {
				Object[] args = { value.toString() };
				throw new ValidatorException(MessageContextUtil.getFacesMessage(INVALID_MESSAGE_ID, args));
			}
		}
	}

	/**
	 * Returns true if the kNr is a valid norsk kontonummer.
	 * 
	 * @param value
	 *            a Object representation of a norsk kontonummer the String should be trimmed, and all separators removed
	 * @return true if the kNr is a valid norsk kontonummer
	 */
	private boolean isNorskKontonummer(Object value) {
		String kNr = StringUtils.deleteWhitespace(value.toString().replace(".", ""));
		if (kNr.length() == 11) {
			try {
				/* Checks that the String is really a number. */
				Long.parseLong(kNr);
			} catch (NumberFormatException e) {
				return false;
			}

			/* Checks if it is a valid modulus 11 didgit. */
			return isValidModulus11WithOneControlDigit(kNr);
		}
		return false;
	}

	/**
	 * Validation of Strings with modulus 11.
	 * 
	 * @param inputString
	 *            String to validate, max length 25.
	 * @return boolean true if valid modulus, false if invalid modulus
	 */
	private boolean isValidModulus11WithOneControlDigit(String inputString) {
		int inputLengthWithoutChecksum = inputString.length() - 1;
		String checkDigit = calculateModulus11(inputString.substring(0, inputLengthWithoutChecksum));
		String lastChar = inputString.substring(inputLengthWithoutChecksum, inputString.length());
		return lastChar.equals(checkDigit);
	}

	/**
	 * Calculates modulus 11.
	 * 
	 * @param inputString
	 *            the input String
	 * @return modulus11 of the input String
	 */
	private String calculateModulus11(String inputString) {
		int inputLength = inputString.length();
		int[] weight = { 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7 };
		int totalSum = 0;
		int index2 = 0;

		for (int index1 = inputLength - 1; index1 >= 0; index1--) {
			totalSum = totalSum + (getInt(inputString.charAt(index1)) * weight[index2]);
			index2++;
		}

		int rest = totalSum % 11;

		if (rest == 0) {
			return String.valueOf(rest);
		} else if (rest > 1) {
			return String.valueOf(11 - rest);
		} else { // (rest == 1)
			return "-";
		}
	}

	/**
	 * Gets the int value of a character i.e. the parsed value.
	 * 
	 * @param c
	 *            a character
	 * @return the int value
	 */
	private int getInt(char c) {
		return Integer.parseInt(String.valueOf(c));
	}

}
