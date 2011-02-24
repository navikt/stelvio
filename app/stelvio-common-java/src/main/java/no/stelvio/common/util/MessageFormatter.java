package no.stelvio.common.util;

/**
 * Interface for formatting objects into text messages.
 * 
 * @author person356941106810, Accenture
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
