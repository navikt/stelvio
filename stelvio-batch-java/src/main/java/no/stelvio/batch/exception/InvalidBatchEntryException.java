package no.stelvio.batch.exception;

/**
 * Thrown when the number of entries in the database for a batch name is invalid. Typically this means that the number of lines
 * returned when querying by batch name is more than 1 or is zero.
 * 
 *
 */
public class InvalidBatchEntryException extends BatchSystemException {

	private static final long serialVersionUID = -1091438174826992714L;

	/**
	 * Constructs an <code>InvalidBatchEntryException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public InvalidBatchEntryException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs an <code>InvalidBatchEntryException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public InvalidBatchEntryException(String message) {
		super(message);
	}
}