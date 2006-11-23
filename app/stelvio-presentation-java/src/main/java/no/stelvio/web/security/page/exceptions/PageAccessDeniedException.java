package no.stelvio.web.security.page.exceptions;

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
	 * @param viewId the viewId.
	 */
	public PageAccessDeniedException(String viewId){
		super(viewId);
	}
	/**
	 * Constructor to set the viewId and the page requirements of the page 
	 * which the user tried to access. 
	 * @param viewId the viewId.
	 * @param page the JSFPage object containing the security requirements of the page.
	 */
	public PageAccessDeniedException(String viewId, JSFPage page){
		super(viewId);
		pageRequirements = page;
	}
	
	@Override
	protected String messageTemplate() {
		return "User is not authorized to view this page: {0}";
	}
	public String getPageRequirements(){
		return this.pageRequirements != null ? this.pageRequirements.toString() : "No requirements found.";
	}
	public JSFPage getSecureJSFPage(){
		return this.pageRequirements;
	}
	
	public String getFriendlyMessage(){
		return "";
	}
}
