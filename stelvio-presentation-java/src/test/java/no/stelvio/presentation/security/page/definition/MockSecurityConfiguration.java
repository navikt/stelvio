package no.stelvio.presentation.security.page.definition;

import no.stelvio.presentation.security.page.definition.parse.SecurityConfiguration;
import no.stelvio.presentation.security.page.definition.parse.support.JsfApplication;

/**
 * MockSecurityConfiguration.
 * 
 *
 */
public class MockSecurityConfiguration implements SecurityConfiguration {

	private JsfApplication secureApp;

	/**
	 * Creates a new instance of MockSecurityConfiguration.
	 * 
	 * @param jsfApp
	 *            jsf app
	 */
	public MockSecurityConfiguration(JsfApplication jsfApp) {
		secureApp = jsfApp;
	}

	/**
	 * Get jsf app.
	 * 
	 * @return jsf app
	 */
	public JsfApplication getJsfApplication() {
		return secureApp;
	}

	/**
	 * Set up jsf app.
	 */
	public void setUpJSFApplication() {

	}
}
