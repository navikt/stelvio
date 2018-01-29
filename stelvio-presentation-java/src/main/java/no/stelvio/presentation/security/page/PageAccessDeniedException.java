package no.stelvio.presentation.security.page;

import no.stelvio.common.security.SecurityException;
import no.stelvio.presentation.security.page.definition.parse.support.JsfPage;

/**
 * Thrown when a user is not authorized to view a page.
 * 
 * @author persondab2f89862d3, Accenture
 */
public class PageAccessDeniedException extends SecurityException {

	private static final long serialVersionUID = 847820923875850L;

	private JsfPage pageRequirements;

	/**
	 * Constructs a <code>PageAccessDeniedException</code> with page requirements and message.
	 * 
	 * @param pageRequirements -
	 *            the page requirements.
	 * @param message -
	 *            the exception message.
	 */
	public PageAccessDeniedException(JsfPage pageRequirements, String message) {
		super(message);
		this.pageRequirements = pageRequirements;
	}

	/**
	 * Constructs a <code>PageAccessDeniedException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public PageAccessDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>PageAccessDeniedException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public PageAccessDeniedException(String message) {
		super(message);
	}

	/**
	 * Constructs a <code>PageAccessDeniedException</code> with viewId and page.
	 * 
	 * @param viewId -
	 *            the exception message.
	 * @param pageRquirements -
	 *            the page requirements.
	 */
	public PageAccessDeniedException(String viewId, JsfPage pageRquirements) {
		super(viewId);
		pageRequirements = pageRquirements;
	}

	/**
	 * Gets the security requirements of the page as a string.
	 * 
	 * @return the page security requirements.
	 */
	public String getPageRequirements() {
		return this.pageRequirements != null ? this.pageRequirements.toString() : "No requirements found.";
	}

	/**
	 * Gets the JsfPage object representing the page and its security requirements.
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