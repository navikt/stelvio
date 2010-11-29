package no.stelvio.common.security.authorization.method;

import no.stelvio.common.security.SecurityException;

/**
 * Exception thrown if creating an <code>AccessDecisionVoter</code> from a configuration attribute fails.
 * 
 * @author persondab2f89862d3, Accenture
 */
public class AccessDecisionVoterNotFoundException extends SecurityException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a <code>AccessDecisionVoterNotFoundException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public AccessDecisionVoterNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>AccessDecisionVoterNotFoundException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public AccessDecisionVoterNotFoundException(String message) {
		super(message);
	}
}