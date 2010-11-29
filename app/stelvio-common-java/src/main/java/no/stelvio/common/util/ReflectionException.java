package no.stelvio.common.util;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Exception thrown when working with reflection fails.
 * 
 * @author personf8e9850ed756
 */
public class ReflectionException extends FunctionalUnrecoverableException {

	private static final long serialVersionUID = 9177864208453169887L;

	/**
	 * Constructs a <code>ReflectionException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public ReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>ReflectionException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public ReflectionException(String message) {
		super(message);
	}

}