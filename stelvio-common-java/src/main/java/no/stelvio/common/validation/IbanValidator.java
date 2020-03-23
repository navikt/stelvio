package no.stelvio.common.validation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * Class used to validate IBANs.
 * 
 *
 */
public class IbanValidator {

	/**
	 * Creates a new instance of IbanValidator.
	 * 
	 */
	protected IbanValidator() {
	}

	// Map used to convert characters into integers
	private static final Map<Character, Integer> CHARTOINTMAP;

	static {
		Map<Character, Integer> tempCharToIntMap = new HashMap<Character, Integer>(25);
		tempCharToIntMap.put('A', 10);
		tempCharToIntMap.put('B', 11);
		tempCharToIntMap.put('C', 12);
		tempCharToIntMap.put('D', 13);
		tempCharToIntMap.put('E', 14);
		tempCharToIntMap.put('F', 15);
		tempCharToIntMap.put('G', 16);
		tempCharToIntMap.put('H', 17);
		tempCharToIntMap.put('I', 18);
		tempCharToIntMap.put('J', 19);
		tempCharToIntMap.put('K', 20);
		tempCharToIntMap.put('L', 21);
		tempCharToIntMap.put('M', 22);
		tempCharToIntMap.put('N', 23);
		tempCharToIntMap.put('O', 24);
		tempCharToIntMap.put('P', 25);
		tempCharToIntMap.put('Q', 26);
		tempCharToIntMap.put('R', 27);
		tempCharToIntMap.put('S', 28);
		tempCharToIntMap.put('T', 29);
		tempCharToIntMap.put('U', 30);
		tempCharToIntMap.put('V', 31);
		tempCharToIntMap.put('W', 32);
		tempCharToIntMap.put('X', 33);
		tempCharToIntMap.put('Y', 34);
		tempCharToIntMap.put('Y', 35);
		// Set 'final' charToIntMap to a read-only map
		CHARTOINTMAP = Collections.unmodifiableMap(tempCharToIntMap);
	}

	/**
	 * Validates whether the specified parameter is a valid IBAN.
	 * 
	 * @param iban
	 *            with (human readable) or without (computer readable) spaces
	 * @return <code>true</code> if input is a valid IBAN, otherwise <code>false</code>
	 */
	public static boolean isValidIban(String iban) {
		String modifiedIban = modifyIban(StringUtils.deleteWhitespace(iban));
		String numericIban = ibanToNumericString(modifiedIban);
		return mod97NumericString(numericIban) == 1;
	}

	/**
	 * Moves the first four characters of an IBAN without spaces to the end of the IBAN.
	 * 
	 * @param noSpaceIban
	 *            Iban witout spaces
	 * @return original string with the first four characters moved to the end
	 */
	private static String modifyIban(String noSpaceIban) {
		return noSpaceIban.substring(4) + noSpaceIban.substring(0, 4);
	}

	/**
	 * Converts a alphanumeric string into a numeric string according to the charToIntMap.
	 * 
	 * @param modifiedIban
	 *            a modified IBAN
	 * @return string of numeric characters
	 */
	private static String ibanToNumericString(String modifiedIban) {
		StringBuffer sb = new StringBuffer(modifiedIban.length());
		for (char character : modifiedIban.toUpperCase().toCharArray()) {
			if (StringUtils.isAlpha(Character.toString(character))) {
				sb.append(CHARTOINTMAP.get(character)); // Append numeric
			} else {
				sb.append(character);
			}
		}
		return sb.toString();
	}

	/**
	 * Performs a mod97 operation on a numeric string in accordance with ISO7064. Handles all string lengths as the algorithm
	 * devides the string into substrings that can be parsed into integers.¨ Once
	 * 
	 * @param numericString
	 *            the numeric string to be processed
	 * @return inputString as a number % 97
	 */
	private static int mod97NumericString(String numericString) {
		if (numericString.length() < 10) {
			return Integer.parseInt(numericString) % 97;
		} else {
			int toCharIndex = 9;
			int fromCharIndex = 0;
			int mod97 = 0;
			String numericStringPart = numericString.substring(0, toCharIndex);

			while (true) {
				mod97 = Integer.parseInt(numericStringPart) % 97;

				if (numericString.length() == toCharIndex) {
					// Break loop after final mod calculation
					break;
				}

				fromCharIndex = toCharIndex;
				// If mod97+toCharIndex is greater than the length of the numeric string, use length of numeric string as
				// toCharIndex
				toCharIndex = ((toCharIndex + (mod97 < 10 ? 8 : 7)) > numericString.length()) ? numericString.length()
						: toCharIndex + ((mod97 < 10) ? 8 : 7);
				numericStringPart = mod97 + numericString.substring(fromCharIndex, toCharIndex);
			}

			return mod97;
		}
	}

}
