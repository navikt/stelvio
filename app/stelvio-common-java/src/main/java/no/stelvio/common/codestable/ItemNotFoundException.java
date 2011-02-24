package no.stelvio.common.codestable;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Exception thrown when <code>CodesTable</code> or a <code>CodesTablePeriodic</code> doesn't hold a requested item.
 * 
 * @author personf8e9850ed756
 */
public class ItemNotFoundException extends FunctionalUnrecoverableException {

	private static final long serialVersionUID = -1206901328468603479L;

	/**
	 * Constructs a <code>ItemNotFoundException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public ItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>ItemNotFoundException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public ItemNotFoundException(String message) {
		super(message);
	}

}