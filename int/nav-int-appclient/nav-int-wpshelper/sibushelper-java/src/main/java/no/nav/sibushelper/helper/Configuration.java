package no.nav.sibushelper.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.sibushelper.SIBUSHelper;
import no.nav.sibushelper.common.Constants;

import org.apache.commons.lang.StringUtils;

import com.ibm.websphere.crypto.PasswordUtil;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class Configuration {

	private static Logger logger = Logger.getLogger(SIBUSHelper.class.getName());
	private String className = Configuration.class.getName();

	private java.util.Properties confFile;
	private File confFileName;
	private boolean doReSyncOnObjectCreate;
	private boolean useJMS;
	private java.util.ArrayList<ServerConfigurationProperties> servers;
	private boolean viewSystemObjects;
	private boolean viewTemporaryObjects;
	private boolean autoRefresh;

	/**
	 * @param confFileName
	 */
	public Configuration(File confFileName) {
		logger.logp(Level.FINE, className, "Configuration()", "Init");
		confFile = null;
		this.confFileName = null;
		doReSyncOnObjectCreate = false;
		useJMS = false;
		servers = new ArrayList<ServerConfigurationProperties>();
		viewSystemObjects = false;
		viewTemporaryObjects = false;
		autoRefresh = false;
		this.confFileName = confFileName;
		loadConfiguration();
		logger.logp(Level.FINE, className, "Configuration()", "Exit");

	}

	/**
	 * @return
	 */
	public boolean getViewSystemObjects() {
		logger.logp(Level.FINE, className, "getViewSystemObjects", new Boolean(viewSystemObjects).toString());
		return viewSystemObjects;
	}

	/**
	 * @param viewSystemObjects
	 */
	public void setViewSystemObjects(boolean viewSystemObjects) {
		logger.logp(Level.FINE, className, "setViewSystemObjects", new Boolean(viewSystemObjects).toString());
		this.viewSystemObjects = viewSystemObjects;
	}

	/**
	 * @return
	 */
	public boolean getAutoRefresh() {
		logger.logp(Level.FINE, className, "getAutoRefresh", new Boolean(autoRefresh).toString());
		return autoRefresh;
	}

	/**
	 * @param autoRefresh
	 */
	public void setAutoRefresh(boolean autoRefresh) {
		logger.logp(Level.FINE, className, "setAutoRefresh", new Boolean(autoRefresh).toString());
		this.autoRefresh = autoRefresh;
	}

	/**
	 * @return
	 */
	public boolean getViewTemporaryObjects() {
		logger.logp(Level.FINE, className, "getViewTemporaryObjects", new Boolean(viewTemporaryObjects).toString());
		return viewTemporaryObjects;
	}

	/**
	 * @param viewTemporaryObjects
	 */
	public void setViewTemporaryObjects(boolean viewTemporaryObjects) {
		logger.logp(Level.FINE, className, "setViewTemporaryObjects", new Boolean(viewTemporaryObjects).toString());
		this.viewTemporaryObjects = viewTemporaryObjects;
	}

	/**
	 * @return
	 */
	public boolean performFullReSyncOnObjectCreate() {
		logger.logp(Level.FINE, className, "performFullReSyncOnObjectCreate", new Boolean(doReSyncOnObjectCreate).toString());
		return doReSyncOnObjectCreate;
	}

	/**
	 * @return
	 */
	public boolean useJMS() {
		logger.logp(Level.FINE, className, "useJMS", new Boolean(useJMS).toString());
		return useJMS;
	}

	/**
	 * We support only one entry (dmgr, server)
	 * 
	 * @return
	 */
	public ServerConfigurationProperties getServer() {
		if (!servers.isEmpty()) {
			return servers.get(0);
		} else {
			ServerConfigurationProperties dummy = new ServerConfigurationProperties();
			return dummy;
		}
	}

	/**
	 * 
	 */
	private void loadConfiguration() {
		confFile = new Properties();
		try {
			confFile.load(new FileInputStream(confFileName));
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, "loadConfiguration", "Unable to load configuration" + e);
		}

		// LS, if this not set we not validate
		if (confFile.getProperty(Constants.PROP_VIEW_SYSTEM) != null) {
			viewSystemObjects = confFile.getProperty(Constants.PROP_VIEW_SYSTEM).equals("true");
		}
		if (confFile.getProperty(Constants.PROP_AUTO_REFRESH) != null) {
			autoRefresh = confFile.getProperty(Constants.PROP_AUTO_REFRESH).equals("true");
		}
		if (confFile.getProperty(Constants.PROP_VIEW_TEMP) != null) {
			viewTemporaryObjects = confFile.getProperty(Constants.PROP_VIEW_TEMP).equals("true");
		}
		if (confFile.getProperty(Constants.PROP_RESYNC_ON_CREATE) != null) {
			doReSyncOnObjectCreate = confFile.getProperty(Constants.PROP_RESYNC_ON_CREATE).equals("true");
		}
		if (confFile.getProperty(Constants.PROP_USEJMS) != null) {
			useJMS = confFile.getProperty(Constants.PROP_USEJMS).equals("true");
		}

		readServerConfiguration("");
	}

	/**
	 * @param serverId
	 */
	private void readServerConfiguration(String serverId) {
		if (confFile.getProperty(Constants.PROP_SERVER_HOST_NAME) == null) {
			logger.logp(Level.SEVERE, className, "readServerConfiguration", "No servers exist!");
		} else {
			ServerConfigurationProperties serverProps = new ServerConfigurationProperties();

			String serverName = confFile.getProperty(Constants.PROP_SERVER_NAME);
			if (!"".equals(serverName) && serverName != null) {
				serverProps.setServerName(serverName);
			} else {
				serverName = "SIBUSHelperServer";
			}

			String serverHostName = confFile.getProperty(Constants.PROP_SERVER_HOST_NAME);
			if (!"".equals(serverHostName) && serverHostName != null) {
				serverProps.setServerHostName(serverHostName);
			}

			String soapPort = confFile.getProperty(Constants.PROP_SERVER_PORT);
			if (!"".equals(soapPort) && soapPort != null) {
				serverProps.setServerPort(Integer.parseInt(soapPort));
			}

			String meEngineHost = confFile.getProperty(Constants.PROP_MSGING_HOST);
			if (!"".equals(meEngineHost) && meEngineHost != null) {
				serverProps.setMessagingHostName(meEngineHost);
			}

			String meEnginePort = confFile.getProperty(Constants.PROP_MSGING_PORT);
			if (!"".equals(meEnginePort) && meEnginePort != null) {
				serverProps.setMessagingPort(Integer.parseInt(meEnginePort));
			}

			String meEngineChain = confFile.getProperty(Constants.PROP_MSGING_CHAIN);
			if (!"".equals(meEngineChain) && meEngineChain != null) {
				serverProps.setMessagingChainName(meEngineChain);
			}

			String meEngineUserName = confFile.getProperty(Constants.PROP_MSGING_USER_NAME);
			if (!"".equals(meEngineUserName) && meEngineUserName != null) {
				serverProps.setMessagingUserName(meEngineUserName);
			}

			String protocol = confFile.getProperty(Constants.PROP_SERVER_PROTOCOL);
			if (!"".equals(protocol) && protocol != null) {
				serverProps.setProtocol(protocol);
			}

			String userName = confFile.getProperty(Constants.PROP_USER_NAME);
			if (!"".equals(userName) && userName != null) {
				serverProps.setUserName(userName);
			}

			String useAlternateMsgingCred = confFile.getProperty(Constants.PROP_MSGING_ALTERNATE_CRED);
			if ((!"".equals(useAlternateMsgingCred) || useAlternateMsgingCred != null) && "true".equals(useAlternateMsgingCred)) {
				serverProps.setMessagingUseAlternateUserId(useAlternateMsgingCred.equals("true"));
			} else {
				serverProps.setMessagingUseAlternateUserId(false);
			}

			String trustLocation = confFile.getProperty(Constants.PROP_TRUST_LOCATION);
			if (!"".equals(trustLocation) && trustLocation != null) {
				serverProps.setTrustStoreLocation(trustLocation);
			}

			String keyLocation = confFile.getProperty(Constants.PROP_KEY_LOCATION);
			if (!"".equals(keyLocation) && keyLocation != null) {
				serverProps.setKeyStoreLocation(keyLocation);
			}

			String securityEnabled = confFile.getProperty(Constants.PROP_SECURITY_ENABLED);
			if ((!"".equals(securityEnabled) && securityEnabled != null) && "true".equals(securityEnabled)) {
				serverProps.setSecurityEnabled(securityEnabled.equals("true"));
			} else {
				serverProps.setSecurityEnabled(false);
			}

			String createNewSSLStores = confFile.getProperty(Constants.PROP_CREATE_NEW_SSL_STORES);
			if ((!"".equals(createNewSSLStores) && createNewSSLStores != null) && "true".equals(createNewSSLStores)) {
				serverProps.setCreateNewSSLStores(createNewSSLStores.equals("true"));
			} else {
				serverProps.setCreateNewSSLStores(false);
			}

			String meEnginePassword = confFile.getProperty(Constants.PROP_MSGING_PASSWORD);
			try {
				if (!"".equals(meEnginePassword) && meEnginePassword != null) {
					if (meEnginePassword.startsWith("{xor}")) {
						serverProps.setMessagingPassword(PasswordUtil.decode(meEnginePassword));
					} else {
						encodePassword(confFileName, Constants.PROP_MSGING_PASSWORD);
						serverProps.setMessagingPassword(meEnginePassword);
					}
				}
			} catch (Exception e) {
				logger.logp(Level.SEVERE, className, "readServerConfiguration", "Password decoding failed for "
						+ Constants.PROP_MSGING_PASSWORD, e);
				serverProps.setMessagingPassword(meEnginePassword);
			}

			String password = confFile.getProperty(Constants.PROP_PASSWORD);
			try {
				if (!"".equals(password) && password != null) {
					if (password.startsWith("{xor}")) {
						serverProps.setPassword(PasswordUtil.decode(password));
					} else {
						encodePassword(confFileName, Constants.PROP_PASSWORD);
						serverProps.setPassword(password);
					}

				}
			} catch (Exception e) {
				logger.logp(Level.SEVERE, className, "readServerConfiguration", "Password decoding failed for "
						+ Constants.PROP_PASSWORD, e);
				serverProps.setPassword(password);
			}

			String trustPassword = confFile.getProperty(Constants.PROP_TRUST_PASSWORD);
			try {
				if (!"".equals(trustPassword) && trustPassword != null) {
					if (trustPassword.startsWith("{xor}")) {
						serverProps.setTrustStorePassword(PasswordUtil.decode(trustPassword));
					} else {
						encodePassword(confFileName, Constants.PROP_TRUST_PASSWORD);
						serverProps.setTrustStorePassword(trustPassword);
					}
				}
			} catch (Exception e) {
				logger.logp(Level.SEVERE, className, "readServerConfiguration", "Password decoding failed for "
						+ Constants.PROP_TRUST_PASSWORD, e);
				serverProps.setTrustStorePassword(trustPassword);
			}

			String keyPassword = confFile.getProperty(Constants.PROP_KEY_PASSWORD);
			try {
				if (!"".equals(keyPassword) && keyPassword != null) {
					if (keyPassword.startsWith("{xor}")) {
						serverProps.setKeyStorePassword(PasswordUtil.decode(keyPassword));
					} else {
						encodePassword(confFileName, Constants.PROP_KEY_PASSWORD);
						serverProps.setKeyStorePassword(trustPassword);
					}
				}
			} catch (Exception e) {
				logger.logp(Level.SEVERE, className, "readServerConfiguration", "Password decoding failed for "
						+ Constants.PROP_KEY_PASSWORD, e);
				serverProps.setKeyStorePassword(keyPassword);
			}
			servers.add(serverProps);
		}
	}

	/**
	 * This method manipulate the property file with an xor encoded password
	 * 
	 * @param propertiesFile
	 */
	private void encodePassword(File propertiesFile, String propPwd) {
		try {
			FileInputStream in = new FileInputStream(propertiesFile);
			Properties properties = new Properties();
			properties.load(in);
			in.close();
			// Check is password is defined
			if (!StringUtils.isEmpty(properties.getProperty(propPwd))) {
				// Check if password already is encrypted
				String password = properties.getProperty(propPwd);
				if (!password.startsWith("{xor}")) {
					// Write encoded password back to the property file
					String encodedPassword = PasswordUtil.encode(password);
					properties.setProperty(propPwd, encodedPassword);
					FileOutputStream out = new FileOutputStream(propertiesFile);
					properties.store(out, "Encoded properties " + propPwd);
					out.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
