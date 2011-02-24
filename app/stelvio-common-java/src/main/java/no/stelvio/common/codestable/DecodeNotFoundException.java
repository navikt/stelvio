package no.stelvio.common.codestable;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Exception thrown when <code>CodesTable</code> or a <code>CodesTablePeriodic</code> doesn't hold a requested
 * <code>decode</code>.
 * 
 * @author personf8e9850ed756
 */
public class DecodeNotFoundException extends FunctionalUnrecoverableException {

	private static final long serialVersionUID = 8665509491381533830L;

	/**
	 * Constructs a <code>DecodeNotFoundException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public DecodeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>DecodeNotFoundException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public DecodeNotFoundException(String message) {
		super(message);
	}

}