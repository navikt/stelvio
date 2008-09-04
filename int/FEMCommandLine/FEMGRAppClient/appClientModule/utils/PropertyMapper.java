package utils;

import java.util.Properties;

import no.nav.femhelper.common.Constants;

import com.ibm.websphere.management.AdminClient;

/**
 * <p>Mapper class to ensure consistency of the <code>java.util.Properties</code>
 * object passed to <code>AdminClientFactory.createAdminClient().<p>
 * 
 * <p>Without this mapper the properties are read directly from the 
 * <code>.properties</code> file, and that may cause a runtime exception of 
 * <code>com.ibm.websphere.management.exception.InvalidAdminClientTypeException</code>
 * to be thrown if additional parameters are added of if the configuration 
 * is insufficent.</p>
 * 
 * @author Andreas R�e
 */
public class PropertyMapper {
	
	/**
	 * Default constructor
	 */
	public PropertyMapper() {
		
	}
	
	/**
	 * <p>This method perfomes the actual mapping. No validation of the content
	 * and consistency of the configuration here. This is allready performed in
	 * <code>PropertyUtil.validateProperties()</code>. See class documentation
	 * for the purpose of this.</p>
	 * 
	 * @param src source properties read directly from the configuration file
	 * @return a properly mapped set of properties
	 */
	public Properties getMappedProperties(Properties src) {
		Properties result = new Properties();

		result.setProperty(AdminClient.CONNECTOR_HOST, src.getProperty(Constants.CONNECTOR_HOST));
		result.setProperty(AdminClient.CONNECTOR_PORT, src.getProperty(Constants.CONNECTOR_PORT));
		result.setProperty(AdminClient.CONNECTOR_TYPE, src.getProperty(Constants.CONNECTOR_TYPE));
		result.setProperty(AdminClient.CONNECTOR_SECURITY_ENABLED, src.getProperty(Constants.CONNECTOR_SECURITY_ENABLED));
		result.setProperty(AdminClient.USERNAME, src.getProperty(Constants.USERNAME));
		result.setProperty(AdminClient.PASSWORD, src.getProperty(Constants.PASSWORD));
		
		if (!"RMI".equals(src.getProperty(Constants.CONNECTOR_TYPE))) 
		{
				if (!"".equals(src.getProperty(Constants.SSL_KEYSTORE))) {
					result.setProperty("javax.net.ssl.keyStore", src.getProperty(Constants.SSL_KEYSTORE));
				}
		
				if (!"".equals(src.getProperty(Constants.SSL_KEYSTORE_PASSWORD))) {
					result.setProperty("javax.net.ssl.keyStorePassword", src.getProperty(Constants.SSL_KEYSTORE_PASSWORD));
				}
		
				if (!"".equals(src.getProperty(Constants.SSL_TRUSTSTORE))) {
					result.setProperty("javax.net.ssl.trustStore", src.getProperty(Constants.SSL_TRUSTSTORE));
				}
		
				if (!"".equals(src.getProperty(Constants.SSL_TRUSTSTORE_PASSWORD))) {
					result.setProperty("javax.net.ssl.trustStorePassword", src.getProperty(Constants.SSL_TRUSTSTORE_PASSWORD));
				}
		}
	
		return result;
	}
}
