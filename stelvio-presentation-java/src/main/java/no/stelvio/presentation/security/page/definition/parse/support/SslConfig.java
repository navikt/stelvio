package no.stelvio.presentation.security.page.definition.parse.support;

/**
 * Object to hold the SSL port and SSL definitions defined in the security
 * configuration file.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class SslConfig {

	private String httpPort = "80";

	private String httpsPort = "443";

	private String keepSsl = "false";

	/**
	 * Sets the http port to use when switching from https to http.
	 * 
	 * @param httpPort
	 *            the http port
	 */
	public void setHttpPort(String httpPort) {
		this.httpPort = httpPort;
	}

	/**
	 * Gets the http port to use when switching from https.
	 * 
	 * @return the http port
	 */
	public String getHttpPort() {
		return this.httpPort;
	}

	/**
	 * Sets the https port to use when switching from http to secure channel.
	 * 
	 * @param httpsPort
	 *            the https port
	 */
	public void setHttpsPort(String httpsPort) {
		this.httpsPort = httpsPort;
	}

	/**
	 * Gets the https port to use when switching from http to secure channel.
	 * 
	 * @return the https port
	 */
	public String getHttpsPort() {
		return this.httpsPort;
	}

	/**
	 * Once a page is accessed in SSL mode, developers may decide that from here
	 * on all other pages must be accessed with SSL as well.
	 * 
	 * @param keepSslMode -
	 *            default value is false. Set to true if you want to continue
	 *            with SSL. If set to false SSL mode is looked up in the page
	 *            definition and SSL might be switched back to normal http.
	 */
	public void setKeepSsl(String keepSslMode) {
		this.keepSsl = keepSslMode;
	}

	/**
	 * Checks if SSL mode should be kept even when accessing pages not requiring
	 * SSL.
	 * 
	 * @return <code>true</code> if SSL mode should be kept after enabled,
	 *         <code>false</code> otherwise.
	 */
	public boolean isKeepSslMode() {
		return this.keepSsl.equalsIgnoreCase("TRUE");
	}
}
