package no.stelvio.batch.exception;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Superclass for exceptions thrown from a batch when a batch can't be processed due to functional failures or shortcomings.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class BatchFunctionalException extends FunctionalUnrecoverableException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -3851866973894371832L;

	/**
	 * Constructs a <code>BatchFunctionalException</code> with message and cause.
	 * 
	 * @param message
	 *            the exception message.
	 * @param cause
	 *            the throwable that caused the exception to be raised.
	 */
	public BatchFunctionalException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>BatchFunctionalException</code> with message.
	 * 
	 * @param message
	 *            the exception message.
	 */
	public BatchFunctionalException(String message) {
		super(message);
	}

}