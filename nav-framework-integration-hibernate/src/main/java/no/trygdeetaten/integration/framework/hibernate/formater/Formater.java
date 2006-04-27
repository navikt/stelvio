package no.trygdeetaten.integration.framework.hibernate.formater;

/**
 * This interface is to be used to implement formater for converting
 * properties from cics commarea and into java types.
 * 
 * This interface must be implemented for each type of formating nessesary.
 * 
 * @author person5b7fd84b3197, Accenture
 * @version $Id: Formater.java 2623 2005-11-11 11:49:01Z psa2920 $
 */
public interface Formater {
	
	/**
	 * This method converts a input of any type to a string.
	 * 
	 * @param input - Value to format
	 * @return String - The formated value
	 */
	String formatInput(Object input);
	
	/**
	 * This method formats the output from cics commarea.
	 * @param output - Value to format
	 * @return Object - The formated value
	 */
	Object formatOutput(String output);

}
