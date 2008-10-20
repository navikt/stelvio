package no.nav.appclient.util;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.ibm.websphere.management.AdminClient;

/**
 * <p>
 * Mapper class to ensure consistency of the <code>java.util.Properties</code>
 * object passed to <code>AdminClientFactory.createAdminClient().<p>
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
	 * <p>
	 * This method perfomes the actual mapping. No validation of the content and
	 * consistency of the configuration here. This is allready performed in
	 * <code>PropertyUtil.validateProperties()</code>. See class
	 * documentation for the purpose of this.
	 * </p>
	 * 
	 * @param src
	 *            source properties read directly from the configuration file
	 * @return a properly mapped set of properties
	 */
	public Properties getMappedProperties(Properties src) {
		Properties result = new Properties();

		if (!StringUtils.isEmpty(src.getProperty(ConfigPropertyNames.CONNECTOR_HOST))) {
			result.setProperty(AdminClient.CONNECTOR_HOST, src.getProperty(ConfigPropertyNames.CONNECTOR_HOST));
		}

		if (!StringUtils.isEmpty(src.getProperty(ConfigPropertyNames.CONNECTOR_PORT))) {
			result.setProperty(AdminClient.CONNECTOR_PORT, src.getProperty(ConfigPropertyNames.CONNECTOR_PORT));
		}

		if (!StringUtils.isEmpty(src.getProperty(ConfigPropertyNames.CONNECTOR_TYPE))) {
			result.setProperty(AdminClient.CONNECTOR_TYPE, src.getProperty(ConfigPropertyNames.CONNECTOR_TYPE));
		}

		if (!StringUtils.isEmpty(src.getProperty(ConfigPropertyNames.CONNECTOR_SECURITY_ENABLED))) {
			result.setProperty(AdminClient.CONNECTOR_SECURITY_ENABLED, src.getProperty(ConfigPropertyNames.CONNECTOR_SECURITY_ENABLED));
		}

		if (!StringUtils.isEmpty(src.getProperty(ConfigPropertyNames.username))) {
			result.setProperty(AdminClient.USERNAME, src.getProperty(ConfigPropertyNames.username));
		}

		if (!StringUtils.isEmpty(src.getProperty(ConfigPropertyNames.password))) {
			result.setProperty(AdminClient.PASSWORD, src.getProperty(ConfigPropertyNames.password));
		}

		if (!"RMI".equals(src.getProperty(ConfigPropertyNames.CONNECTOR_TYPE))) {
			if (!StringUtils.isEmpty(src.getProperty(ConfigPropertyNames.SSL_KEYSTORE))) {
				result.setProperty("javax.net.ssl.keyStore", src.getProperty(ConfigPropertyNames.SSL_KEYSTORE));
			}

			if (!StringUtils.isEmpty(src.getProperty(ConfigPropertyNames.SSL_KEYSTORE_PASSWORD))) {
				result.setProperty("javax.net.ssl.keyStorePassword", src.getProperty(ConfigPropertyNames.SSL_KEYSTORE_PASSWORD));
			}

			if (!StringUtils.isEmpty(src.getProperty(ConfigPropertyNames.SSL_TRUSTSTORE))) {
				result.setProperty("javax.net.ssl.trustStore", src.getProperty(ConfigPropertyNames.SSL_TRUSTSTORE));
			}

			if (!StringUtils.isEmpty(src.getProperty(ConfigPropertyNames.SSL_TRUSTSTORE_PASSWORD))) {
				result.setProperty("javax.net.ssl.trustStorePassword", src.getProperty(ConfigPropertyNames.SSL_TRUSTSTORE_PASSWORD));
			}
		}
		return result;
	}
}
