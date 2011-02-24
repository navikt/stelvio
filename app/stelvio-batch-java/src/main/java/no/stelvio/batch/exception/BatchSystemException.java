package no.stelvio.batch.exception;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * Superclass for exceptions thrown from a batch when a batch can't be processed due to system failures or shortcomings.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public abstract class BatchSystemException extends SystemUnrecoverableException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an <code>BatchSystemException</code> with message and cause.
	 * 
	 * @param message
	 *            the exception message.
	 * @param cause
	 *            the throwable that caused the exception to be raised.
	 */
	public BatchSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs an <code>BatchSystemException</code> with message.
	 * 
	 * @param message
	 *            the exception message.
	 */
	public BatchSystemException(String message) {
		super(message);
	}

}
