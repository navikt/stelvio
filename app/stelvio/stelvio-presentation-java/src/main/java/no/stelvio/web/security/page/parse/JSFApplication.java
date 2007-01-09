package no.stelvio.web.security.page.parse;

import java.util.HashMap;

/**
 * Object to hold all JSF page security definitions for the application.
 * 
 * @author persondab2f89862d3
 * @version $Id$
 */
public class JSFApplication {

	private HashMap<String, JSFPage> allJsfPages = new HashMap<String, JSFPage>();

	private SSLConfig sslConfig;

	public JSFApplication() {
	}

	/**
	 * Sets all the JSF pages of the application with security definitions.
	 * 
	 * @param jsfPages
	 *            the JSF pages with the page name as key.
	 */
	public void setAllJsfPages(HashMap<String, JSFPage> jsfPages) {
		this.allJsfPages = jsfPages;
	}

	/**
	 * Returns all secured JSF pages of the application.
	 * 
	 * @return the secured JSF pages.
	 */
	public HashMap<String, JSFPage> getAllJsfPages() {
		return this.allJsfPages;
	}

	/**
	 * Sets the object that contains the SSL configuration for the overall
	 * application: https port, http port and keep-ssl-mode
	 * 
	 * @param sslConfig
	 *            the overall SSL configuration information.
	 */
	public void addSslConfig(SSLConfig sslConfig) {
		this.sslConfig = sslConfig;
	}

	/**
	 * Gets the <code>SSLConfig</code> object. If the object is
	 * <code>null</code> it will be created with default settings.
	 * 
	 * @return the SSLConfig object with overall SSL settings.
	 */
	public SSLConfig getSslConfig() {
		if (this.sslConfig == null) {
			// return default setting
			this.sslConfig = new SSLConfig();
		}
		return this.sslConfig;
	}

	/**
	 * Adds a secured JSF page.
	 * 
	 * @param jsfPage
	 *            the JSF page to add.
	 */
	public void addJsfPage(JSFPage jsfPage) {
		// add new page to list of application pages
		this.allJsfPages.put(jsfPage.getPageName(), jsfPage);
	}

	/**
	 * Gets the HTTP port specified in the <code>SSLConfig</code> object.
	 * 
	 * @return the HTTP port as a string
	 */
	public String getHttpPort() {
		return getSslConfig().getHttpPort();
	}

	/**
	 * Gets the HTTPS port specified in the <code>SSLConfig</code> object.
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
	 * Find security settings for a specific JSF page
	 * 
	 * @param viewId -
	 *            the ViewId value obtained in the JSF view handler
	 * @return JSFPage object containing the page settings and J2EE roles
	 */
	public JSFPage findJsfPageDefinition(String viewId) {
		return this.allJsfPages.get(viewId);
	}
}
