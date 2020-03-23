package no.stelvio.dto.exception;

/**
 * Signals that a functional unrecoverable DTO exception has occurred.
 * 
 */
public abstract class FunctionalUnrecoverableDtoException extends UnrecoverableDtoException {

	/**
	 * Constructs a <code>FunctionalUnrecoverableDtoException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	protected FunctionalUnrecoverableDtoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>FunctionalUnrecoverableDtoException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	protected FunctionalUnrecoverableDtoException(String message) {
		super(message);
	}

}
