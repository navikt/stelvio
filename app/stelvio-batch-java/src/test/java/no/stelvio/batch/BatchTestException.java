package no.stelvio.batch;

import no.stelvio.batch.exception.BatchSystemException;

/**
 * Batch test exception.
 * 
 * @author MA
 *
 */
public class BatchTestException extends BatchSystemException {

	private static final long serialVersionUID = -4344647108241437387L;
	
	/**
	 * Constructs a <code>BatchTestException</code> with message and cause.
	 * 
	 * @param message - the exception message.
	 * @param cause - the throwable that caused the exception to be raised.
	 */
	public BatchTestException(String message, Throwable cause) {
		super(message, cause);
	}	

	/**
	 * Constructs a <code>BatchTestException</code> with message.
	 * 
	 * @param message - the exception message.
	 */	
	public BatchTestException(String message) {
		super(message);
	}
	
}
