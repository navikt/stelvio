package no.stelvio.common.codestable;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Exception thrown when a <code>CodesTable</code> or <code>CodesTablePeriodic</code> cannot be retrieved.
 * 
 */
public class CodesTableNotFoundException extends FunctionalUnrecoverableException {

	private static final long serialVersionUID = -6427780265990487437L;

	/**
	 * Constructs a <code>CodesTableNotFoundException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public CodesTableNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>CodesTableNotFoundException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public CodesTableNotFoundException(String message) {
		super(message);
	}

}