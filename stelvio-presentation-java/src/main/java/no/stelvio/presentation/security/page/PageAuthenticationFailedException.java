package no.stelvio.presentation.security.page;

import no.stelvio.common.security.SecurityException;
import no.stelvio.presentation.security.page.definition.parse.support.JsfPage;

/**
 * Thrown when an attempt to authenticate the user fails.
 * 
 */
public class PageAuthenticationFailedException extends SecurityException {

	private static final long serialVersionUID = 1L;

	private JsfPage pageRequirements;

	/**
	 * Constructs a <code>PageAuthenticationFailedException</code> with message.
	 * 
	 * @param pageRequirements -
	 *            the page requirements.
	 * @param message -
	 *            the exception message.
	 */
	public PageAuthenticationFailedException(String message, JsfPage pageRequirements) {
		super(message);
		this.pageRequirements = pageRequirements;
	}

	/**
	 * Constructs a <code>PageAuthenticationFailedException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public PageAuthenticationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>PageAuthenticationFailedException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public PageAuthenticationFailedException(String message) {
		super(message);
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