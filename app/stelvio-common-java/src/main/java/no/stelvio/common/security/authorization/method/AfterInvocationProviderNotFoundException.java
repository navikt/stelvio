package no.stelvio.common.security.authorization.method;

import no.stelvio.common.security.SecurityException;

/**
 * Exception thrown if creating an <code>AfterInvocationProvider</code> from a
 * configuration attribute fails.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AfterInvocationProviderNotFoundException extends SecurityException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param name
	 *            the class name of the AfterInvocationProvider.
	 * @param reason
	 *            the reason for this exception to be thrown.
	 */
	public AfterInvocationProviderNotFoundException(String name, String reason) {
		super(name, reason);
	}

	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param cause
	 *            the root cause for the exception
	 * @param name
	 *            the class name of the AfterInvocationProvider.
	 * @param reason
	 *            the reason for this exception to be thrown
	 */
	public AfterInvocationProviderNotFoundException(Throwable cause,
			String name, String reason) {
		super(cause, name, reason);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate(final int numArgs) {
		return "Could not find configured AfterInvocationProvider {0}: {1}";
	}

}
