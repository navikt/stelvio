package no.stelvio.common.error;

/**
 */
public class TestUnrecoverableException extends SystemUnrecoverableException {

	private static final long serialVersionUID = -5513295489348621547L;

	/**
	 * Constructs a <code>TestUnrecoverableException</code> with message, error code and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param errorCode -
	 *            the error code for the exception.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public TestUnrecoverableException(String message, ErrorCode errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}

	/**
	 * Constructs a <code>TestUnrecoverableException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public TestUnrecoverableException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>TestUnrecoverableException</code> with message and error code.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param errorCode -
	 *            the error code for the exception.
	 */
	public TestUnrecoverableException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

	/**
	 * Constructs a <code>TestUnrecoverableException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public TestUnrecoverableException(String message) {
		super(message);
	}

}
