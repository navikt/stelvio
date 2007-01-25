package no.stelvio.presentation.security.page.definition.parse;

import no.stelvio.presentation.security.page.definition.parse.support.JsfApplication;

/**
 * Interface for SecurityConfiguration sources which parses security definitions
 * for JSF pages and stores them in an internal <code>JsfApplication</code>
 * object.
 * 
 * @author persondab2f89862d3
 * @version $Id$
 */
public interface SecurityConfiguration {

	/**
	 * Gets the JsfApplication object that contains the security constraints for
	 * the application's JSF pages.
	 * 
	 * @return the JSF application's security constraints.
	 */
	JsfApplication getJsfApplication();

}
