package no.nav.integration.framework.hibernate.formater;

/**
 * This is a converter class for Strings in CICS.
 * 
 * @author person5b7fd84b3197, Accenture
 * @version $Id: CicsStringFormater.java 2623 2005-11-11 11:49:01Z psa2920 $
 */
public class CicsStringFormater implements Formater {
	
	private boolean toUpper = false;

	/**
	 * This method changes a some special caracters needed in CICS.
	 * Æ = {, Ø = |, Å = } 
	 * @param input - The string to convert
	 * @return String - The converted String
	 */
	public String formatInput(Object input) {
		if (null == input) {
			return "";
		}
		String value = input.toString();
		if(toUpper) {
			value = value.toUpperCase();
		}
		
		value = value.replace('æ', '{');
		value = value.replace('Æ', '{');
		value = value.replace('ø', '¦');
		value = value.replace('Ø', '¦');
		value = value.replace('å', '}');
		value = value.replace('Å', '}');

		return value;
	}

	/**
	 * This method changes a some special caracters needed in CICS.
	 * { = Æ, | = Ø, } = Å
	 * @param output - The string to convert
	 * @return String - The converted String
	 */
	public Object formatOutput(String output) {
		if (null == output) {
			return output;
		}
		String change = output;
		change = change.replace('{', 'Æ');
		change = change.replace('¦', 'Ø');
		change = change.replace('}', 'Å');
		if(toUpper) {
			return change.toUpperCase();
		}
		return change;
	}

	/**
	 * Setter for ToUpper
	 * @param b - boolean true if convert to uppercase
	 */
	public void setToUpper(boolean b) {
		toUpper = b;
	}

}
