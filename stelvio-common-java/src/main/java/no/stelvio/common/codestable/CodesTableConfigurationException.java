package no.stelvio.common.codestable;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Exception thrown when there are problems configuring the codestable module.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class CodesTableConfigurationException extends FunctionalUnrecoverableException {

	private static final long serialVersionUID = -991305077539306879L;

	/**
	 * Constructs a <code>CodesTableConfigurationException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public CodesTableConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>CodesTableConfigurationException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public CodesTableConfigurationException(String message) {
		super(message);
	}

}