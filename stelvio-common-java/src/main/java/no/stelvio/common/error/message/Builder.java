package no.stelvio.common.error.message;

/**
 * Builds message from exception's argument(s).
 *
 */
public interface Builder {
	/**
	 * Builds error messages.
	 *
	 * @param arguments exception's arguments
	 * @return the message.
	 */	
	String build(Object... arguments);
}
