package no.stelvio.common.security;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * Thrown to indicate that an unrecoverable security exception has occured. <p/> Applications will typically not handle recovery
 * from these exceptions.
 * 
 * @author persondab2f89862d3, Accenture
 */
public abstract class SecurityException extends SystemUnrecoverableException {

	/**
	 * Constructs a <code>SecurityException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public SecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>SecurityException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public SecurityException(String message) {
		super(message);
	}

}