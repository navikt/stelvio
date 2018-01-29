package no.stelvio.dto.exception;

/**
 * Signals that a functional unrecoverable DTO exception has occurred. Exception that is Java SE 1.4 and WS-I compliant.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public abstract class UnrecoverableDtoException extends RuntimeException {

	private long errorId;

	private String message;

	/**
	 * Creates a new UnrecoverableDtoException.
	 */
	public UnrecoverableDtoException() {
	}

	/**
	 * Constructs an <code>UnrecoverableDtoException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	protected UnrecoverableDtoException(String message) {
		super(message);
		setMessage(message);
	}

	/**
	 * Constructs an <code>UnrecoverableDtoException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	protected UnrecoverableDtoException(String message, Throwable cause) {
		super(message, cause);
		setMessage(message);
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