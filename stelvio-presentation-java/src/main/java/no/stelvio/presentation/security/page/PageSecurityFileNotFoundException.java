package no.stelvio.presentation.security.page;

import no.stelvio.common.security.SecurityException;

/**
 * Thrown when the configuration file containing the security definitions cannot be found.
 * 
 */
public class PageSecurityFileNotFoundException extends SecurityException {

	private static final long serialVersionUID = -4832255871071020404L;

	/**
	 * Constructs a <code>PageSecurityFileNotFoundException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public PageSecurityFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>PageSecurityFileNotFoundException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public PageSecurityFileNotFoundException(String message) {
		super(message);
	}

}