package no.trygdeetaten.common.framework.ejb;

/**
 * RemoteServiceDescription represents a description of a remote service.
 * 
 * @author person7553f5959484
 * @version $Revision: 40 $ $Author: psa2920 $ $Date: 2004-04-26 16:24:19 +0200 (Mon, 26 Apr 2004) $
 */
public class RemoteServiceDescription {

	// instance variables
	private String jndiName = null;
	private String providerUrl = null;
	private String initialContextFactory = null;
	private String urlPkgPrefixes = null;
	private String securityPrincipal = null;
	private String securityCredentials = null;
	private boolean cacheable = true;

	/**
	 * Get the class name of the remote context factory to be used 
	 * for looking up and accessing the remote service.
	 * 
	 * @return the initial context factory class name.
	 */
	public String getInitialContextFactory() {
		return initialContextFactory;
	}

	/**
	 * Get the jndi name to be used for looking up the remote service.
	 * 
	 * @return the jndi name.
	 */
	public String getJndiName() {
		return jndiName;
	}

	/**
	 * Get the URL to the provider where the remote service is registered.
	 * 
	 * @return the URL.
	 */
	public String getProviderUrl() {
		return providerUrl;
	}

	/**
	 * Get the security credentials to be used for accessing the remote service.
	 * 
	 * @return the security credentials.
	 */
	public String getSecurityCredentials() {
		return securityCredentials;
	}

	/**
	 * Get the security principal to be used for accessing the remote service.
	 * 
	 * @return the security principal.
	 */
	public String getSecurityPrincipal() {
		return securityPrincipal;
	}

	/**
	 * Get the package prefixes to be used for for accessing the remote service.
	 * 
	 * @return the package prefixes.
	 */
	public String getUrlPkgPrefixes() {
		return urlPkgPrefixes;
	}

	/**
	 * Set the class name of the remote context factory to be used 
	 * for looking up and accessing the remote service.
	 * 
	 * @param initialContextFactory the initial context factory class name.
	 */
	public void setInitialContextFactory(String initialContextFactory) {
		this.initialContextFactory = initialContextFactory;
	}

	/**
	 * Set the jndi name to be used for looking up the remote service.
	 * 
	 * @param jndiName the jndi name.
	 */
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	/**
	 * Set the URL to the provider where the remote service is registered.
	 * 
	 * @param providerUrl the URL.
	 */
	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

	/**
	 * Set the security credentials to be used for accessing the remote service.
	 * 
	 * @param securityCredentials the security credentials.
	 */
	public void setSecurityCredentials(String securityCredentials) {
		this.securityCredentials = securityCredentials;
	}

	/**
	 * Set the security principal to be used for accessing the remote service.
	 * 
	 * @param securityPrincipal the security principal.
	 */
	public void setSecurityPrincipal(String securityPrincipal) {
		this.securityPrincipal = securityPrincipal;
	}

	/**
	 * Set the package prefixes to be used for for accessing the remote service.
	 * 
	 * @param urlPkgPrefixes the package prefixes.
	 */
	public void setUrlPkgPrefixes(String urlPkgPrefixes) {
		this.urlPkgPrefixes = urlPkgPrefixes;
	}

	/**
	 * Verify if the remote service should be cached locally.
	 * 
	 * @return true if the remote service is cacheable, false otherwise.
	 */
	public boolean isCacheable() {
		return cacheable;
	}

	/**
	 * Deside if the remote service is should be cached locally.
	 * 
	 * @param cacheable true if the remote service is cacheable, false otherwise.
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

}