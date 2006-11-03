package no.stelvio.common.util;

import no.stelvio.common.error.ErrorCode;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.service.ServiceFailedException;

/**
 * This class provides som static method for string modification.
 * The class is intended to support handeling of CICS records.
 * 
 * @author person5b7fd84b3197, Accenture
 * @version $Revision: 2648 $ $Author: ena2920 $ $Date: 2005-11-26 15:57:51 +0100 (Sat, 26 Nov 2005) $
 */
public final class StringHelper {

	/** String representation of double. 
	 *  Value is: <code>double</code> */
	public static final String DOUBLE = "double";

	/** String representation of float. 
	 *  Value is: <code>float</code> */
	public static final String FLOAT = "float";

	/** String representation of int. 
	 *  Value is: <code>int</code> */
	public static final String INT = "int";

	/** String representation of integer. 
	 *  Value is: <code>integer</code> */
	public static final String INTEGER = "integer";

	/** String representation of long. 
	 *  Value is: <code>long</code> */
	public static final String LONG = "long";

	/** String representation of short. 
	 *  Value is: <code>short</code> */
	public static final String SHORT = "short";

	/**
	 * String representing of empty String.
	 * Value is <code>""</code>
	 */
	public static final String EMPTY_STRING = "";

	/**
	 * Empty constructor.
	 *
	 */
	private StringHelper() {
	}
	/**
	 * This is a utility method to get a property from a string.
	 * 
	 * @param length - Length of the property to get
	 * @param offset - Where to start reading the property
	 * @param record - The string to find the property in
	 * @return String - The property as a string
	 */
	public static String getProperty(int length, int offset, String record) {

		if (null == record || (length + offset) > record.length()) {
			throw new SystemException(
                    new Object[] {
					"StringHelper.getProperty",
					"Input record is null or will couse StringIndexOutOfBoundsException" });
		}

		return record.substring(offset, offset + length);
	}

	/**
	 * This method is a string appender for properties. 
	 * The value added will be appended on the end of the buffer and padded to
	 * the length. If the type is a number of any kind the value will be padded
	 * with zero's and for other properties it will be padded with space.
	 * Numbers are right padded and other properties are left padded.
	 * 
	 * @param length - The length of the value
	 * @param value - The value
	 * @param buff - The buffer to append the value to
	 * @param type - The type eg. string
	 * @throws ServiceFailedException - If value.toString is longer than length 
	 */
	public static void appendProperty(int length, Object value, StringBuffer buff, String type) throws ServiceFailedException {
		boolean number = false;
		String val = EMPTY_STRING;
		if (LONG.equals(type)
			|| INT.equals(type)
			|| DOUBLE.equals(type)
			|| INTEGER.equals(type)
			|| SHORT.equals(type)
			|| FLOAT.equals(type)) {
			number = true;
		}
		if (null != value) {
			val = value.toString();
		}

		int strlen = val.length();
		// The input data is bigger than the length of the coulmn
		if (strlen > length) {
			throw new ServiceFailedException(
				ErrorCode.UNSPECIFIED_ERROR,
				new IllegalArgumentException("Teksten '" + value + "' er lengre enn " + length + " tegn."),
				new Object[] { "StringHelper.appendProperty", "Teksten '" + value + "' er lengre enn " + length + " tegn." });
		}
		if (number) {
			for (int i = 0; i < (length - strlen); i++) {
				buff.append('0');
			}
		}
		buff.append(val);

		if (!number) {
			for (int i = strlen; i < length; i++) {
				buff.append(' ');
			}
		}

	}

	/**
	 * This method removes trailing spaces form a given string.
	 * 
	 * @param str - String to trim 
	 * @return String - The trimed value
	 */
	public static String removeEndingSpaces(String str) {
		int size = str.length();
		for (int i = size - 1; 0 <= i; i--) {
			if (str.charAt(i) != ' ') {
				return str.substring(0, i + 1);
			}
		}
		return EMPTY_STRING;
	}

	/**
	 * This method inserts leading zero's on a given string.
	 * 
	 * @param str - String to insert/pad
	 * @param lenght - Max. length of string
	 * @return String - The padded value
	 */
	public static String insertLeadingZeros(String str, int length) {
		if(str.length() == length){
			return str;
		}
		
		StringBuffer padStr = new StringBuffer(length);
		for(int i = 0; i < length; i++){
			padStr.insert(i, '0');
		}
		
		padStr.replace(length-str.length(), length, str);
		return padStr.toString();		
	}
}
