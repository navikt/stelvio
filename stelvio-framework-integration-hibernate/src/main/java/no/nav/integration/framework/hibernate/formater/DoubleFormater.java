package no.nav.integration.framework.hibernate.formater;

import org.apache.commons.lang.StringUtils;

/**
 * Formating between Cobol (i.e. Java String) and Java Double values
 *
 * @author person5b7fd84b3197, Accenture
 * @version $Id: DoubleFormater.java 2617 2005-11-09 13:14:28Z psa2920 $
 */
public class DoubleFormater implements Formater {

	private int precision;

	private int length;

	private boolean signed = false;

	/**
	 * Format input. Converts double to String
	 *
	 * @param input - The double value to convert
	 * @return String - The string representation of the string
	 */
	public String formatInput(Object input) {
		if (null == input) {
			return "";
		}
		String value = ((Double) input).toString();
		int index = value.indexOf('.');
		int len = value.length();

		//	Pas with zero if dec to short
		int add = 1;
		if (value.charAt(0) == '-') {
			add = 2;
		}
		for (int i = len; i < (length + add); i++) {
			value = value + '0';
		}

		String dec = value.substring(index + 1, index + precision + 1);
		String cnt = value.substring((index - (length - precision) > 0 ? index - (length - precision) : 0), index);

		if (signed) {
			LongFormater formater = new LongFormater();

			if (value.charAt(0) == '-') {
				value = formater.formatInput(Long.valueOf("-" + cnt + dec));
			} else {
				value = formater.formatInput(Long.valueOf(cnt + dec));
			}

			len = value.length();
			for (int i = len; i < (precision + 1); i++) {
				value = '0' + value;
			}

		} else {
			value = cnt + dec;
		}

		return value;
	}

	/**
	 * Fomat output. Converts String to double
	 *
	 * @param output - The string to convert
	 * @return Object - The double value
	 */
	public Object formatOutput(String output) {
		if (StringUtils.isBlank(output)) {
			return (new Double(0));
		}
		String value = output.substring(0, length - precision) + "." + output.substring(length - precision);
		if (signed) {
			LongFormater formater = new LongFormater();

			Long val = (Long) formater.formatOutput(output.substring(length - precision));
			if (val.longValue() > 0) {
				value = output.substring(0, length - precision) + "." + val.toString();
			} else {
				value = "-" + output.substring(0, length - precision) + "." + Math.abs(val.longValue());
			}

		}
		return (new Double(value));
	}

	/**
	 * Gets the precision of the double balue
	 *
	 * @return int - precision
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * Sets the precision of the double value
	 *
	 * @param precision - precision number
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * Gets the length of the value
	 *
	 * @return int - The length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets the length
	 *
	 * @param i - The length
	 */
	public void setLength(int i) {
		length = i;
	}

	/**
	 * Setter for signed
	 *
	 * @param signed - True if signed
	 */
	public void setSigned(boolean signed) {
		this.signed = signed;
	}

}
