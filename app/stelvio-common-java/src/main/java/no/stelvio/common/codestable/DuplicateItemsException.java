package no.stelvio.common.codestable;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Exception thrown when a <code>List</code> with <code>CodesTableItem</code>s or <code>CodesTablePeriodicItem</code>s
 * used for creating a <code>CodesTable</code> or <code>CodesTablePeriodic</code> respectively has duplicate entries.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class DuplicateItemsException extends FunctionalUnrecoverableException {

	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = 8768050496767133340L;

	/**
	 * Constructs a <code>DuplicateItemsException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public DuplicateItemsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>DuplicateItemsException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public DuplicateItemsException(String message) {
		super(message);
	}

}