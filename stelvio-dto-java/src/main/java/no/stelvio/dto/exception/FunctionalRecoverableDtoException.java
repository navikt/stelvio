package no.stelvio.dto.exception;

/**
 * Signals that a functional recoverable DTO exception has occurred. 
 * 
 */

public abstract class FunctionalRecoverableDtoException extends RecoverableDtoException {

	/**
	 * Constructs an <code>FunctionalRecoverableDtoException</code> with message and cause.
	 * 
	 * @param message - the exception message.
	 * @param cause - the throwable that caused the exception to be raised.
	 */	
	protected FunctionalRecoverableDtoException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs an <code>FunctionalRecoverableDtoException</code> with message.
	 * 
	 * @param message - the exception message.
	 */	
	protected FunctionalRecoverableDtoException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a FunctionalRecoverableDtoException.
	 */
	public FunctionalRecoverableDtoException() {
		super();
	}
}