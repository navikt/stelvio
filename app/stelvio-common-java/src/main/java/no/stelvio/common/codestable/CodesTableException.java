package no.stelvio.common.codestable;

import no.stelvio.common.error.FunctionalRecoverableException;

/**
 * Super class for exceptions thrown when handling codes tables.
 * 
 * @author personf8e9850ed756
 */
abstract class CodesTableException extends FunctionalRecoverableException {

	/**
	 * Constructs a <code>CodesTableException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public CodesTableException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>CodesTableException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public CodesTableException(String message) {
		super(message);
	}

}