package no.nav.bpchelper.utils;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.ibm.websphere.management.AdminClient;

/**
 * <p>
 * Mapper class to ensure consistency of the <code>java.util.Properties</code>
 * object passed to <code>BFMConnectorAdapter</code>
 * <p>
 * 
 * <p>
 * Without this mapper the properties are read directly from the
 * <code>.properties</code> file, and that may cause unknown error situations
 * 
 * <p>
 * For maintenance it is important to remember that the FEM Helper tool also
 * make use of a property file on the same format
 * </p>
 * 
 * <p>
 * NB! Since it is prefered to not perform functional validation of this
 * properties is their values used transparent here
 * </p>
 * 
 * @author Andreas Røe
 */
public class PropertyMapper {

	public Properties getMappedProperties(Properties src) {
		Properties result = new Properties();

		StringBuilder url = new StringBuilder();
		url.append(src.getProperty(ConnectorProperties.CONNECTOR_HOST));
		url.append(":");
		url.append(src.getProperty(ConnectorProperties.CONNECTOR_PORT));

		result.setProperty("java.naming.provider.url", url.toString());
		result.setProperty("providerURL", url.toString());

		if (!StringUtils.isEmpty(src.getProperty(ConnectorProperties.CONNECTOR_TYPE))) {
			result.setProperty(AdminClient.CONNECTOR_TYPE, src.getProperty(ConnectorProperties.CONNECTOR_TYPE));
		}

		if (!StringUtils.isEmpty(src.getProperty(ConnectorProperties.CONNECTOR_SECURITY_ENABLED))) {
			result.setProperty(AdminClient.CONNECTOR_SECURITY_ENABLED, src
					.getProperty(ConnectorProperties.CONNECTOR_SECURITY_ENABLED));
		}

		if (!StringUtils.isEmpty(src.getProperty(ConnectorProperties.USERNAME))) {
			result.setProperty(ConnectorProperties.USERNAME, src.getProperty(ConnectorProperties.USERNAME));
		}

		if (!StringUtils.isEmpty(src.getProperty(ConnectorProperties.PASSWORD))) {
			result.setProperty(ConnectorProperties.PASSWORD, src.getProperty(ConnectorProperties.PASSWORD));
		}

		return result;
	}
}
