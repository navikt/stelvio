package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import no.nav.appclient.util.ConfigPropertyNames;

import org.apache.commons.lang.StringUtils;

/**
 * Class to perform utility operations in connection with properties and
 * configuration
 * 
 * @author Andreas Røe
 */
public class PropertyUtil {
	public List validateProperties(Properties properties) {
		List<String> result = new ArrayList<String>();

		// Validate property BootstrapHost.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(ConfigPropertyNames.BootstrapHost))) {
			result.add("Property " + ConfigPropertyNames.BootstrapHost + " is missing or empty");
		}

		// Validate property CONNECTOR_PORT.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(ConfigPropertyNames.BootstrapPort))) {
			result.add("Property " + ConfigPropertyNames.BootstrapPort + " is missing or empty");
		}

		// Validate property CONNECTOR_TYPE.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(ConfigPropertyNames.CONNECTOR_TYPE))) {
			result.add("Property " + ConfigPropertyNames.CONNECTOR_TYPE + " is missing or empty");
		}

		// Validate property CONNECTOR_SECURITY_ENABLED.
		// Criterias: present and 'true' or 'false'
		if (StringUtils.isEmpty(properties.getProperty(ConfigPropertyNames.CONNECTOR_SECURITY_ENABLED))) {
			result.add("Property " + ConfigPropertyNames.CONNECTOR_SECURITY_ENABLED + " is missing or empty");
		} else if (!properties.getProperty(ConfigPropertyNames.CONNECTOR_SECURITY_ENABLED).trim().equals("true")
				&& !properties.getProperty(ConfigPropertyNames.CONNECTOR_SECURITY_ENABLED).trim().equals("false")) {
			result.add("Property " + ConfigPropertyNames.CONNECTOR_SECURITY_ENABLED + " must be 'true' or 'false'");
		}

		// Validate property USERNAME.
		// Criterias: present if CONNECTOR_SECURITY_ENABLED is true
		String userName = properties.getProperty(ConfigPropertyNames.username);
		String securityEnabled = properties.getProperty(ConfigPropertyNames.CONNECTOR_SECURITY_ENABLED);
		if (StringUtils.isEmpty(userName) && "true".equals(securityEnabled)) {
			result.add("Property " + ConfigPropertyNames.username + " must be present if " + ConfigPropertyNames.CONNECTOR_SECURITY_ENABLED
					+ " is true");
		}
		return result;
	}
}
