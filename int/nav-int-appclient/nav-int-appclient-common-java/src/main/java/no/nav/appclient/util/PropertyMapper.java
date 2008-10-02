package no.nav.appclient.util;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.ibm.websphere.management.AdminClient;

/**
 * <p>
 * Mapper class to ensure consistency of the <code>java.util.Properties</code> object passed to
 * <code>AdminClientFactory.createAdminClient().<p>
 * 
 * <p>Without this mapper the properties are read directly from the 
 * <code>.properties</code> file, and that may cause a runtime exception of 
 * <code>com.ibm.websphere.management.exception.InvalidAdminClientTypeException</code>
 * to be thrown if additional parameters are added of if the configuration 
 * is insufficent.</p>
 * 
 * @author Andreas Røe
 */
public class PropertyMapper {

	/**
	 * Logger instance
	 */
	private static Logger logger = Logger.getLogger(PropertyMapper.class.getName());

	/**
	 * <p>
	 * This method perfomes the actual mapping. No validation of the content and consistency of the configuration here. This is
	 * allready performed in <code>PropertyUtil.validateProperties()</code>. See class documentation for the purpose of this.
	 * </p>
	 * 
	 * @param src
	 *            source properties read directly from the configuration file
	 * @return a properly mapped set of properties
	 */
	public Properties getMappedProperties(Properties src) {
		logger.log(Level.FINE, Constants.METHOD_ENTER + "getMappedProperties");

		Properties result = new Properties();

		if (!StringUtils.isEmpty(src.getProperty(Constants.BootstrapHost))) {
			result.setProperty(AdminClient.CONNECTOR_HOST, src.getProperty(Constants.BootstrapHost));
		}

		if (!StringUtils.isEmpty(src.getProperty(Constants.BootstrapPort))) {
			result.setProperty(AdminClient.CONNECTOR_PORT, src.getProperty(Constants.BootstrapPort));
		}

		if (!StringUtils.isEmpty(src.getProperty(Constants.CONNECTOR_TYPE))) {
			result.setProperty(AdminClient.CONNECTOR_TYPE, src.getProperty(Constants.CONNECTOR_TYPE));
		}

		if (!StringUtils.isEmpty(src.getProperty(Constants.CONNECTOR_SECURITY_ENABLED))) {
			result.setProperty(AdminClient.CONNECTOR_SECURITY_ENABLED, src.getProperty(Constants.CONNECTOR_SECURITY_ENABLED));
		}

		if (!StringUtils.isEmpty(src.getProperty(Constants.username))) {
			result.setProperty(AdminClient.USERNAME, src.getProperty(Constants.username));
		}

		if (!StringUtils.isEmpty(src.getProperty(Constants.password))) {
			result.setProperty(AdminClient.PASSWORD, src.getProperty(Constants.password));
		}

		if (!"RMI".equals(src.getProperty(Constants.CONNECTOR_TYPE))) {
			if (!StringUtils.isEmpty(src.getProperty(Constants.SSL_KEYSTORE))) {
				result.setProperty("javax.net.ssl.keyStore", src.getProperty(Constants.SSL_KEYSTORE));
			}

			if (!StringUtils.isEmpty(src.getProperty(Constants.SSL_KEYSTORE_PASSWORD))) {
				result.setProperty("javax.net.ssl.keyStorePassword", src.getProperty(Constants.SSL_KEYSTORE_PASSWORD));
			}

			if (!StringUtils.isEmpty(src.getProperty(Constants.SSL_TRUSTSTORE))) {
				result.setProperty("javax.net.ssl.trustStore", src.getProperty(Constants.SSL_TRUSTSTORE));
			}

			if (!StringUtils.isEmpty(src.getProperty(Constants.SSL_TRUSTSTORE_PASSWORD))) {
				result.setProperty("javax.net.ssl.trustStorePassword", src.getProperty(Constants.SSL_TRUSTSTORE_PASSWORD));
			}
		}

		logger.log(Level.FINE, Constants.METHOD_EXIT + "getMappedProperties");
		return result;
	}
}
