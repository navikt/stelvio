package no.stelvio.presentation.security.page;

import no.stelvio.common.security.SecurityException;

/**
 * Thrown when an attempt to redirect from http to https and vice versa fails.
 * 
 * @author persondab2f89862d3, Accenture
 */
public class PageProtocolSwitchFailedException extends SecurityException {

	private static final long serialVersionUID = 3329332131466791355L;

	/**
	 * Constructs a <code>PageProtocolSwitchFailedException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public PageProtocolSwitchFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>PageProtocolSwitchFailedException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public PageProtocolSwitchFailedException(String message) {
		super(message);
	}
}