package no.stelvio.dto.exception;

/**
 * Signals that a system unrecoverable DTO exception has occurred. 
 * 
 *
 */
public abstract class SystemUnrecoverableDtoException extends UnrecoverableDtoException {

	
	private String errorCode;
	
	/**
	 * Constructs a <code>SystemUnrecoverableDtoException</code> with message and cause.
	 * 
	 * @param message - the exception message.
	 * @param cause - the throwable that caused the exception to be raised.
	 */	
	protected SystemUnrecoverableDtoException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a <code>SystemUnrecoverableDtoException</code> with message.
	 * 
	 * @param message - the exception message.
	 */	
	protected SystemUnrecoverableDtoException(String message) {
		super(message);
	}

	/**
	 * Gets the errorCode used by operations.
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Sets the errorCode used by operations.
	 * @param errorCode the error code
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
