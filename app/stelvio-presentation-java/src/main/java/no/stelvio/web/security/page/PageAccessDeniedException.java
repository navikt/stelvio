package no.stelvio.web.security.page;

import no.stelvio.common.security.SecurityException;
import no.stelvio.web.security.page.parse.JSFPage;

/**
 * Thrown when a user is not authorized to view a page.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class PageAccessDeniedException extends SecurityException {

	private static final long serialVersionUID = 1L;

	private JSFPage pageRequirements;

	/**
	 * Constructor to set the viewId of the page which the user tried to access.
	 * 
	 * @param viewId
	 *            the viewId.
	 */
	public PageAccessDeniedException(String viewId) {
		super(viewId);
	}

	/**
	 * Constructor to set the viewId and the page requirements of the page.
	 * 
	 * @param viewId
	 *            the viewId.
	 * @param page
	 *            the JSFPage object containing the security requirements of the
	 *            page.
	 */
	public PageAccessDeniedException(String viewId, JSFPage page) {
		super(viewId);
		pageRequirements = page;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate(final int numArgs) {
		return "User is not authorized to view this page: {0}";
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
	 * Gets the JSFPage object representing the page and its security
	 * requirements.
	 * 
	 * @return the JSFPage object.
	 */
	public JSFPage getSecureJSFPage() {
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
