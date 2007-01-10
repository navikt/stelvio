package no.stelvio.presentation.security.page;

import no.stelvio.common.security.SecurityException;

/**
 * Thrown when an attempt to redirect from http to https and vice versa fails.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class PageProtocolSwitchFailedException extends SecurityException {

	private static final long serialVersionUID = 1L;

	private boolean pageRequiresSSL;

	/**
	 * Constructor to set the viewId of the page, whether or not SSL is required
	 * and the port of the redirect.
	 * 
	 * @param viewId
	 *            the viewId.
	 * @param pageRequiresSSL
	 *            whether or not the page requires SSL.
	 * @param port
	 *            the port used in the redirect.
	 */
	public PageProtocolSwitchFailedException(String viewId,
			boolean pageRequiresSSL, String port) {
		super(viewId, port);
		this.pageRequiresSSL = pageRequiresSSL;
	}

	/**
	 * Constructor to set the viewId of the page, whether or not SSL is required
	 * and the port of the redirect.
	 * 
	 * @param cause
	 *            the root cause for the error.
	 * @param viewId
	 *            the viewId.
	 * @param pageRequiresSSL
	 *            whether or not the page requires SSL.
	 * @param port
	 *            the port used in the redirect.
	 */
	public PageProtocolSwitchFailedException(Throwable cause, String viewId,
			boolean pageRequiresSSL, String port) {
		super(cause, viewId, port);
		this.pageRequiresSSL = pageRequiresSSL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate(final int numArgs) {
		String toHttpsMsg = "Redirection to secure channel for page '{0}' with HTTPS port '{1}' failed.";
		String toHttpMsg = "Redirection to normal channel for page '{0}' with HTTP port '{1}' failed.";
		return this.pageRequiresSSL ? toHttpsMsg : toHttpMsg;
	}

}
