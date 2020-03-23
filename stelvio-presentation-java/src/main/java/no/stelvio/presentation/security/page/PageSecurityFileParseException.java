package no.stelvio.presentation.security.page;

import no.stelvio.common.security.SecurityException;

/**
 * Thrown if the parsing of the security definitions from the configuration file fails.
 * 
 */
public class PageSecurityFileParseException extends SecurityException {

	private static final long serialVersionUID = 6047326360448953923L;

	/**
	 * Constructs a <code>PageSecurityFileParseException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public PageSecurityFileParseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>PageSecurityFileParseException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public PageSecurityFileParseException(String message) {
		super(message);
	}

}