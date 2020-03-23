package no.stelvio.presentation.security.page.definition.parse.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Object to hold all JSF page security definitions for the application.
 * 
 * @version $Id$
 */
public class JsfApplication {

	private Map<String, JsfPage> allJsfPages = new HashMap<>();

	private SslConfig sslConfig;

	/**
	 * Creates a new instance of JsfApplication.
	 */
	public JsfApplication() {
	}

	/**
	 * Sets all the JSF pages of the application with security definitions.
	 * 
	 * @param jsfPages
	 *            the JSF pages with the page name as key.
	 */
	public void setAllJsfPages(Map<String, JsfPage> jsfPages) {
		this.allJsfPages = jsfPages;
	}

	/**
	 * Returns all secured JSF pages of the application.
	 * 
	 * @return the secured JSF pages.
	 */
	public Map<String, JsfPage> getAllJsfPages() {
		return this.allJsfPages;
	}

	/**
	 * Sets the object that contains the SSL configuration for the overall
	 * application: https port, http port and keep-ssl-mode.
	 * 
	 * @param sslConfig
	 *            the overall SSL configuration information.
	 */
	public void addSslConfig(SslConfig sslConfig) {
		this.sslConfig = sslConfig;
	}

	/**
	 * Gets the <code>SslConfig</code> object. If the object is
	 * <code>null</code> it will be created with default settings.
	 * 
	 * @return the SslConfig object with overall SSL settings.
	 */
	public SslConfig getSslConfig() {
		if (this.sslConfig == null) {
			// return default setting
			this.sslConfig = new SslConfig();
		}
		return this.sslConfig;
	}

	/**
	 * Adds a secured JSF page.
	 * 
	 * @param jsfPage
	 *            the JSF page to add.
	 */
	public void addJsfPage(JsfPage jsfPage) {
		// add new page to list of application pages
		this.allJsfPages.put(jsfPage.getPageName(), jsfPage);
	}

	/**
	 * Gets the HTTP port specified in the <code>SslConfig</code> object.
	 * 
	 * @return the HTTP port as a string
	 */
	public String getHttpPort() {
		return getSslConfig().getHttpPort();
	}

	/**
	 * Gets the HTTPS port specified in the <code>SslConfig</code> object.
	 * 
	 * @return the HTTPS port as a string
	 */
	public String getHttpsPort() {
		return getSslConfig().getHttpsPort();
	}

	/**
	 * Checks if SSL mode should be kept even when accessing pages not requiring
	 * SSL.
	 * 
	 * @return <code>true</code> if SSL mode should be kept after enabled,
	 *         <code>false</code> otherwise.
	 */
	public boolean isKeepSSL() {
		return getSslConfig().isKeepSslMode();
	}

	/**
	 * Find security settings for a specific JSF page.
	 * 
	 * @param viewId -
	 *            the ViewId value obtained in the JSF view handler
	 * @return JsfPage object containing the page settings and J2EE roles
	 */
	public JsfPage findJsfPageDefinition(String viewId) {
		return this.allJsfPages.get(viewId);
	}
}
