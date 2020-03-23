package no.stelvio.presentation.security.eai;

import no.stelvio.common.security.SecurityException;

/**
 * Thrown to indicate that an authentication failure has occurred.
 * 
 * @see SecurityException
 */
public class AuthenticationFailureException extends SecurityException {

	private static final long serialVersionUID = 9115065066648985291L;

	/**
	 * Constructs a <code>AuthenticationFailureException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public AuthenticationFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>AuthenticationFailureException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public AuthenticationFailureException(String message) {
		super(message);
	}
}