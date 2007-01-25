package no.stelvio.presentation.security.page;

import no.stelvio.common.security.SecurityException;
import no.stelvio.presentation.security.page.definition.parse.support.JsfPage;

/**
 * Thrown when an attempt to authenticate the user fails.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class PageAuthenticationFailedException extends SecurityException {

	private static final long serialVersionUID = 1L;

	private JsfPage pageRequirements;

	/**
	 * Constructor to set the viewId of the page which the user tried to access.
	 * 
	 * @param viewId
	 *            the viewId.
	 */
	public PageAuthenticationFailedException(String viewId) {
		super(viewId);
	}

	/**
	 * Constructor to set the viewId and the page requirements of the page which
	 * the user tried to access.
	 * 
	 * @param viewId
	 *            the viewId.
	 * @param page
	 *            the JsfPage object containing the security requirements of the
	 *            page.
	 */
	public PageAuthenticationFailedException(String viewId, JsfPage page) {
		super(viewId);
		pageRequirements = page;
	}

	/**
	 * Constructor to set the root cause if an exception caused the
	 * authentication to fail. Other parameters: the viewId and the page
	 * requirements of the page which the user tried to access.
	 * 
	 * @param t
	 *            root cause of the error.
	 * @param viewId
	 *            the viewId.
	 * @param page
	 *            the JsfPage object containing the security requirements of the
	 *            page.
	 */
	public PageAuthenticationFailedException(Throwable t, String viewId,
			JsfPage page) {
		super(t, viewId);
		pageRequirements = page;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate(final int numArgs) {
		return "Could not authenticate user when accessing page: {0}";
	}

	/**
	 * Gets the security requirements of the page as a string.
	 * 
	 * @return the page security requirements.
	 */
	public String getPageRequirements() {
		return this.pageRequirements != null ? this.pageRequirements.toString()
				: "No requirements found.";
	}

	/**
	 * Gets the JsfPage object representing the page and its security
	 * requirements.
	 * 
	 * @return the JsfPage object.
	 */
	public JsfPage getSecureJSFPage() {
		return this.pageRequirements;
	}

	/**
	 * Gets the message which should be shown to the enduser.
	 * 
	 * @return a user friendly message.
	 */
	public String getFriendlyMessage() {
		return "";
	}

}
