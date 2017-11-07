package no.stelvio.common.error.message;

/**
 * Builds message from exception's argument(s).
 *
 * @author personf8e9850ed756
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
