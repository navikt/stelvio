package no.stelvio.common.codestable;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Exception thrown to indicate that a codes table has no items.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class CodesTableEmptyException extends FunctionalUnrecoverableException {

	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = -3499927279266886444L;

	/**
	 * Constructs a <code>CodesTableEmptyException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public CodesTableEmptyException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>CodesTableEmptyException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public CodesTableEmptyException(String message) {
		super(message);
	}

}