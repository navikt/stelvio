package no.stelvio.common.security.authorization.method;

import no.stelvio.common.security.SecurityException;

/**
 * Exception thrown if creating an <code>AccessDecisionVoter</code> from a
 * configuration attribute fails.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AccessDecisionVoterNotFoundException extends SecurityException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param name
	 *            the class name of the AccessDecsionVoter.
	 * @param reason
	 *            the reason for this exception to be thrown.
	 */
	public AccessDecisionVoterNotFoundException(String name, String reason) {
		super(name, reason);
	}

	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param cause
	 *            the root cause for the exception
	 * @param name
	 *            the class name of the AccessDecsionVoter.
	 * @param reason
	 *            the reason for this exception to be thrown
	 */
	public AccessDecisionVoterNotFoundException(Throwable cause, String name,
			String reason) {
		super(cause, name, reason);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate() {
		return "Could not find configured AccessDecisionVoter {0}: {1}";
	}

}
