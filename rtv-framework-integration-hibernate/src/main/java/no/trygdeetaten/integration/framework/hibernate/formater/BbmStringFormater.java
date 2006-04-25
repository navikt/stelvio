package no.trygdeetaten.integration.framework.hibernate.formater;

/**
 * This is a converter class for Strings from BBM.
 *
 * @author person17040a0d57d6, Accenture
 */
public class BbmStringFormater implements Formater {
	private boolean toUpper = false;

	/**
	 * This method changes a some special caracters needed in CICS. � = {, � = |, � = }
	 *
	 * @param input - The string to convert
	 * @return String - The converted String
	 */
	public String formatInput(Object input) {
		if (null == input) {
			return "";
		}
		String value = input.toString();
		if (toUpper) {
			value = value.toUpperCase();
		}

		value = value.replace('�', '#');
		value = value.replace('�', '#');
		value = value.replace('�', '@');
		value = value.replace('�', '@');
		value = value.replace('�', '$');
		value = value.replace('�', '$');

		return value;
	}

	/**
	 * This method changes a some special caracters needed in CICS. { = �, | = �, } = �
	 *
	 * @param output - The string to convert
	 * @return String - The converted String
	 */
	public Object formatOutput(String output) {
		if (null == output) {
			return output;
		}
		String change = output;
		change = change.replace('#', '�');
		change = change.replace('@', '�');
		change = change.replace('$', '�');
		if (toUpper) {
			return change.toUpperCase();
		}
		return change;
	}

	/**
	 * Setter for ToUpper
	 *
	 * @param b - boolean true if convert to uppercase
	 */
	public void setToUpper(boolean b) {
		toUpper = b;
	}

}



