package no.stelvio.batch.exception;

/**
 * Exception thrown when a specified Batch is null.
 * 
 * @author person983601e0e117 (Accenture)
 *
 */
public class NullBatchException extends BatchSystemException {

	private static final long serialVersionUID = -3364616443275434225L;

	/**
	 * Constructs a <code>NullBatchException</code> with message and cause.
	 * 
	 * @param message - the exception message.
	 * @param cause - the throwable that caused the exception to be raised.
	 */
	public NullBatchException(String message, Throwable cause) {
		super(message, cause);
	}	

	/**
	 * Constructs a <code>NullBatchException</code> with message.
	 * 
	 * @param message - the exception message.
	 */	
	public NullBatchException(String message) {
		super(message);
	}	
}
