package no.stelvio.common.security.authorization.method;

import no.stelvio.common.security.SecurityException;

/**
 * Exception thrown if creating an <code>AfterInvocationProvider</code> from a configuration attribute fails.
 * 
 */
public class AfterInvocationProviderNotFoundException extends SecurityException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a <code>AfterInvocationProviderNotFoundException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public AfterInvocationProviderNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>AfterInvocationProviderNotFoundException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public AfterInvocationProviderNotFoundException(String message) {
		super(message);
	}
}