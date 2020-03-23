package no.stelvio.common.validation;

import org.apache.commons.lang3.StringUtils;

/**
 * Class used to validate BBAN. (Basic Bank Account Number: used by financial institutions in individual countries as part of a
 * National Account Numbering Scheme which uniquely identifies an account of a customer at a financial institution.)
 * 
 *
 */
public class BbanValidator {

	/**
	 * Validates a Norwegian BBAN.
	 * 
	 * @param norwegianBban
	 *            a Norwegian Bank account number, either human readable (with spaces) or computer readable (witout spaces)
	 * @return <code>true</code> if input is a valid Norwegian BBAN, otherwise <code>false</code>
	 */
	public static boolean isValidNorwegianBban(String norwegianBban) {
		return isValidModulus11WithOneControlDigit(StringUtils.deleteWhitespace(norwegianBban));
	}

	/**
	 * Validation of Strings with modulus 11.
	 * 
	 * @param inputString
	 *            String to validate, max length 25.
	 * @return boolean true if valid modulus, false if invalid modulus
	 */
	private static boolean isValidModulus11WithOneControlDigit(String inputString) {
		int inputLengthWithoutChecksum = inputString.length() - 1;
		String checkDigit = calculateModulus11(inputString.substring(0, inputLengthWithoutChecksum));
		String lastChar = inputString.substring(inputLengthWithoutChecksum, inputString.length());
		return lastChar.equals(checkDigit);
	}

	/**
	 * Calculates modulus 11.
	 * 
	 * @param inputString
	 *            the input string to be processed
	 * @return the calculated result, modulus 11
	 */
	private static String calculateModulus11(String inputString) {
		int inputLength = inputString.length();
		int[] weight = { 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7, 2, 3, 4, 5, 6, 7 };
		int totalSum = 0;
		int index2 = 0;

		for (int index1 = inputLength - 1; index1 >= 0; index1--) {
			int inputInt = inputString.charAt(index1) - '0';
			totalSum = totalSum + (inputInt * weight[index2]);
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
}
