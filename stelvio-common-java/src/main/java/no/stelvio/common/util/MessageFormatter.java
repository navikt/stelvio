package no.stelvio.common.util;

/**
 * Interface for formatting objects into text messages.
 * 
 */
public interface MessageFormatter {
	
	/**
	 * Formats an array of parameters into a string.
	 * 
	 * @param params the array of object to format
	 * @return the formatter text string
	 */
	String formatMessage(Object[] params); 
}
