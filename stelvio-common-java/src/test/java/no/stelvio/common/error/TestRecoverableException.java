package no.stelvio.common.error;

/**
 */
public class TestRecoverableException extends FunctionalRecoverableException {

	private static final long serialVersionUID = 2992621846206485339L;

	/**
	 * Constructs a <code>TestRecoverableException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public TestRecoverableException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>TestRecoverableException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public TestRecoverableException(String message) {
		super(message);
	}

}
