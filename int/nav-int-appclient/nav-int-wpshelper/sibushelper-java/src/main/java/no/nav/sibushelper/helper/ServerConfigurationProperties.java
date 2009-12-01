/**
 * 
 */
package no.nav.sibushelper.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.sibushelper.SIBUSHelper;
import no.nav.sibushelper.common.Constants;

/**
 * @author wpsadmin
 * 
 */
public class ServerConfigurationProperties implements Comparable {

	private static Logger logger = Logger.getLogger(SIBUSHelper.class.getName());
	private String className = ServerConfigurationProperties.class.getName();

	private String serverName;
	private String serverHostName;
	private int port;
	private String meEngineHostName;
	private int meEnginePort;
	private String meEngineChainName;
	private String meEngineUserName;
	private String meEnginePassword;
	private boolean meEngineUseAlternateUserId;
	private String protocol;
	private String userName;
	private String password;
	private boolean securityEnabled;
	private String keyStoreLocation;
	private String trustStoreLocation;
	private String keyStorePassword;
	private String trustStorePassword;
	private boolean createNewSSLStores;

	/**
	 * 
	 */
	public ServerConfigurationProperties() {
		serverName = "";
		serverHostName = "localhost";
		port = 8880;
		meEngineHostName = "";
		meEnginePort = 7276;
		meEngineChainName = "BootstrapBasicMessaging";
		meEngineUserName = "";
		meEnginePassword = "";
		meEngineUseAlternateUserId = false;
		protocol = Constants.SOAP_PROTOCOL;
		userName = "";
		password = "";
		securityEnabled = false;
		keyStoreLocation = "";
		trustStoreLocation = "";
		keyStorePassword = "";
		trustStorePassword = "";
		createNewSSLStores = false;
	}

	/**
	 * @return
	 */
	public int getMessagingPort() {
		logger.logp(Level.FINE, className, "getMessagingPort", new Integer(meEnginePort).toString());
		return meEnginePort;
	}

	/**
	 * @return
	 */
	public String getServerHostName() {
		logger.logp(Level.FINE, className, "getServerHostName", serverHostName);
		return serverHostName;
	}

	/**
	 * @return
	 */
	public int getServerPort() {
		logger.logp(Level.FINE, className, "getServerPort", new Integer(port).toString());
		return port;
	}

	/**
	 * @param jsPort
	 */
	public void setMessagingPort(int jsPort) {
		logger.logp(Level.FINE, className, "setMessagingPort", new Integer(jsPort).toString());
		meEnginePort = jsPort;
	}

	/**
	 * @param serverHostName
	 */
	public void setServerHostName(String serverHostName) {
		logger.logp(Level.FINE, className, "setServerHostName", serverHostName);
		this.serverHostName = serverHostName;
	}

	/**
	 * @param port
	 */
	public void setServerPort(int port) {
		logger.logp(Level.FINE, className, "setServerPort", new Integer(port).toString());
		this.port = port;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		logger.logp(Level.FINE, className, "getPassword", "******");
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		logger.logp(Level.FINE, className, "setPassword", "******");
		this.password = password;
	}

	/**
	 * @return
	 */
	public String getProtocol() {
		logger.logp(Level.FINE, className, "getProtocol", protocol);
		return protocol;
	}

	/**
	 * @param protocol
	 */
	public void setProtocol(String protocol) {
		logger.logp(Level.FINE, className, "setProtocol", protocol);
		this.protocol = protocol;
	}

	/**
	 * @return
	 */
	public String getUserName() {
		logger.logp(Level.FINE, className, "getUserName", userName);
		return userName;
	}

	/**
	 * @param userName
	 */
	public void setUserName(String userName) {
		logger.logp(Level.FINE, className, "setUserName", userName);
		this.userName = userName;
	}

	/**
	 * @return
	 */
	public boolean isSecurityEnabled() {
		logger.logp(Level.FINE, className, "isSecurityEnabled", new Boolean(securityEnabled).toString());
		return securityEnabled;
	}

	/**
	 * @param enabled
	 */
	public void setSecurityEnabled(boolean enabled) {
		logger.logp(Level.FINE, className, "setSecurityEnabled", new Boolean(enabled).toString());
		securityEnabled = enabled;
	}

	/**
	 * @return
	 */
	public String getServerName() {
		logger.logp(Level.FINE, className, "getServerName", serverName);
		return serverName;
	}

	/**
	 * @param serverName
	 */
	public void setServerName(String serverName) {
		logger.logp(Level.FINE, className, "setServerName", serverName);
		this.serverName = serverName;
	}

	/**
	 * @return
	 */
	public String getMessagingHostName() {
		logger.logp(Level.FINE, className, "getMessagingHostName", meEngineHostName);
		return meEngineHostName;
	}

	/**
	 * @param meEngineHostName
	 */
	public void setMessagingHostName(String meEngineHostName) {
		logger.logp(Level.FINE, className, "setMessagingHostName", meEngineHostName);
		this.meEngineHostName = meEngineHostName;
	}

	/**
	 * @return
	 */
	public String getMessagingChainName() {
		logger.logp(Level.FINE, className, "getMessagingChainName", meEngineChainName);
		return meEngineChainName;
	}

	/**
	 * @param meEngineChainName
	 */
	public void setMessagingChainName(String meEngineChainName) {
		logger.logp(Level.FINE, className, "setMessagingChainName", meEngineChainName);
		this.meEngineChainName = meEngineChainName;
	}

	/**
	 * @return
	 */
	public String getMessagingPassword() {
		logger.logp(Level.FINE, className, "getMessagingPassword", meEnginePassword);
		return meEnginePassword;
	}

	/**
	 * @param meEnginePassword
	 */
	public void setMessagingPassword(String meEnginePassword) {
		logger.logp(Level.FINE, className, "setMessagingPassword", meEnginePassword);
		this.meEnginePassword = meEnginePassword;
	}

	/**
	 * @return
	 */
	public String getMessagingUserName() {
		logger.logp(Level.FINE, className, "getMessagingUserName", meEngineUserName);
		return meEngineUserName;
	}

	/**
	 * @param meEngineUserName
	 */
	public void setMessagingUserName(String meEngineUserName) {
		logger.logp(Level.FINE, className, "setMessagingUserName", meEngineUserName);
		this.meEngineUserName = meEngineUserName;
	}

	/**
	 * @param useAlternate
	 */
	public void setMessagingUseAlternateUserId(boolean useAlternate) {
		logger.logp(Level.FINE, className, "setMessagingUseAlternateUserId", new Boolean(useAlternate).toString());
		meEngineUseAlternateUserId = useAlternate;
	}

	/**
	 * @return
	 */
	public boolean isMessagingUseAlernateUserId() {
		logger.logp(Level.FINE, className, "isMessagingUseAlernateUserId", new Boolean(meEngineUseAlternateUserId).toString());
		return meEngineUseAlternateUserId;
	}

	/**
	 * @return
	 */
	public String getKeyStoreLocation() {
		logger.logp(Level.FINE, className, "getKeyStoreLocation", keyStoreLocation);
		return keyStoreLocation;
	}

	/**
	 * @param keyStoreLocation
	 */
	public void setKeyStoreLocation(String keyStoreLocation) {
		logger.logp(Level.FINE, className, "setKeyStoreLocation", keyStoreLocation);
		this.keyStoreLocation = keyStoreLocation;
	}

	/**
	 * @return
	 */
	public String getKeyStorePassword() {
		logger.logp(Level.FINE, className, "getKeyStorePassword", keyStorePassword);
		return keyStorePassword;
	}

	/**
	 * @param keyStorePassword
	 */
	public void setKeyStorePassword(String keyStorePassword) {
		logger.logp(Level.FINE, className, "setKeyStorePassword", keyStorePassword);
		this.keyStorePassword = keyStorePassword;
	}

	/**
	 * @return
	 */
	public String getTrustStoreLocation() {
		logger.logp(Level.FINE, className, "getTrustStoreLocation", trustStoreLocation);
		return trustStoreLocation;
	}

	/**
	 * @param trustStoreLocation
	 */
	public void setTrustStoreLocation(String trustStoreLocation) {
		logger.logp(Level.FINE, className, "setTrustStoreLocation", trustStoreLocation);
		this.trustStoreLocation = trustStoreLocation;
	}

	/**
	 * @return
	 */
	public String getTrustStorePassword() {
		logger.logp(Level.FINE, className, "getTrustStorePassword", "******");
		return trustStorePassword;
	}

	/**
	 * @param trustStorePassword
	 */
	public void setTrustStorePassword(String trustStorePassword) {
		logger.logp(Level.FINE, className, "setTrustStorePassword", "******");
		this.trustStorePassword = trustStorePassword;
	}

	/**
	 * @return
	 */
	public boolean isCreateNewSSLStores() {
		return createNewSSLStores;
	}

	/**
	 * @param createNewSSLStores
	 */
	public void setCreateNewSSLStores(boolean createNewSSLStores) {
		this.createNewSSLStores = createNewSSLStores;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(Object other) {
		int result = -1;
		if (other instanceof no.nav.sibushelper.helper.ServerConfigurationProperties) {
			ServerConfigurationProperties otherConf = (no.nav.sibushelper.helper.ServerConfigurationProperties) other;
			String n1 = serverName.equals("") ? serverHostName : serverName;
			String n2 = otherConf.getServerName().equals("") ? otherConf.getServerHostName() : otherConf.getServerName();
			result = n1.compareTo(n2);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "ServerConfigurationProperties@" + Integer.toHexString(System.identityHashCode(this)) + ": {" + "serverName="
				+ serverName + ", " + "serverHostName=" + serverHostName + ", " + "port=" + port + ", " + "protocol="
				+ protocol + ", " + "securityEnabled=" + securityEnabled + ", " + "userName=" + userName + ", "
				+ "meEngineHostName=" + meEngineHostName + ", " + "meEnginePort=" + meEnginePort + ", " + "meEngineChainName="
				+ meEngineChainName + ", " + "meEngineUseAlternateUserId=" + meEngineUseAlternateUserId + ", "
				+ "meEngineUserId=" + meEngineUserName + ", " + "trustStoreLocation=" + trustStoreLocation + ", "
				+ "keyStoreLocation=" + keyStoreLocation + ", " + "createNewSSLStores=" + createNewSSLStores + " }";
	}

}
