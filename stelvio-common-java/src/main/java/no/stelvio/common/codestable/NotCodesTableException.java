package no.stelvio.common.codestable;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Exception thrown when a codes table class does not extend the correct class.
 * 
 */
public class NotCodesTableException extends FunctionalUnrecoverableException {

	private static final long serialVersionUID = -1071348408365492500L;

	/**
	 * Constructs a <code>NotCodesTableException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public NotCodesTableException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>NotCodesTableException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public NotCodesTableException(String message) {
		super(message);
	}

}