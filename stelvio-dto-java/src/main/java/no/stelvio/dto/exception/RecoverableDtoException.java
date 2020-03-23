package no.stelvio.dto.exception;

/**
 * Signals that a recoverable DTO exception has occurred. Exception that is Java SE 1.4 and WS-I compliant.
 * 
 *
 */
public abstract class RecoverableDtoException extends Exception {

	private long errorId;

	private String message;

	/**
	 * Constructs an <code>RecoverableDtoException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	protected RecoverableDtoException(String message, Throwable cause) {
		super(message, cause);
		setMessage(message);
	}

	/**
	 * Constructs an <code>RecoverableDtoException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	protected RecoverableDtoException(String message) {
		super(message);
		setMessage(message);
	}

	/**
	 * Creates a new RecoverableDtoException.
	 */

	public RecoverableDtoException() {
		super();
	}

	/**
	 * Gets the errorId.
	 * 
	 * @return errorId the errorId
	 */
	public long getErrorId() {
		return errorId;
	}

	/**
	 * Sets the errorId.
	 * 
	 * @param errorId
	 *            the errorId
	 */
	public void setErrorId(long errorId) {
		this.errorId = errorId;
	}

	/**
	 * Gets the message.
	 * 
	 * @return message the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the message
	 */
	public final void setMessage(String message) {
		this.message = message;
	}
}
