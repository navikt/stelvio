package no.stelvio.presentation.security.page.parse;

/**
 * Interface for SecurityConfiguration sources which parses security definitions
 * for JSF pages and stores them in an internal <code>JSFApplication</code>
 * object.
 * 
 * @author persondab2f89862d3
 * @version $Id$
 */
public interface SecurityConfiguration {

	/**
	 * Gets the JSFApplication object that contains the security constraints for
	 * the application's JSF pages.
	 * 
	 * @return the JSF application's security constraints.
	 */
	JSFApplication getJsfApplication();

}
