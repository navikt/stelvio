package no.stelvio.domain.person;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Exception thrown as a result of Pid validation failure.
 * 
 *
 * @see Pid
 */
public class PidValidationException extends FunctionalUnrecoverableException {

	private static final long serialVersionUID = 6146570031382768191L;

	/**
	 * Constructs a <code>PidValidationException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public PidValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>PidValidationException</code> with pid.
	 * 
	 * @param pid -
	 *            the pid that did not pass validation.
	 * 
	 */
	public PidValidationException(String pid) {
		super("Pid validation failed, " + pid + " is not a valid personal identification number");
	}

}