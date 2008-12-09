package no.nav.sibushelper.cmdoptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import no.nav.sibushelper.common.Constants;

import org.apache.commons.lang.StringUtils;

/**
 * Class to perform utility operations in connection with properties and
 * configuration
 * 
 * @author persona2c5e3b49756 Schnell
 */
public class PropertyUtil {
	public List validateProperties(Properties properties) {
		List<String> result = new ArrayList<String>();

		// Validate property PROP_SERVER_HOST_NAME.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(Constants.PROP_SERVER_HOST_NAME))) {
			result.add("Property " + Constants.PROP_SERVER_HOST_NAME + " is missing or empty");
		}

		// Validate property PROP_SERVER_PROTOCOL.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(Constants.PROP_SERVER_PROTOCOL))) {
			result.add("Property " + Constants.PROP_SERVER_PROTOCOL + " is missing or empty");
		}

		// Validate property PROP_SERVER_PORT.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(Constants.PROP_SERVER_PORT))) {
			result.add("Property " + Constants.PROP_SERVER_PORT + " is missing or empty");
		}
		
		
		// Validate property CONNECTOR_SECURITY_ENABLED.
		// Criterias: present and 'true' or 'false'
		if (StringUtils.isEmpty(properties.getProperty(Constants.PROP_SECURITY_ENABLED))) {
			result.add("Property " + Constants.PROP_SECURITY_ENABLED + " is missing or empty");
		} else if (!properties.getProperty(Constants.PROP_SECURITY_ENABLED).trim().equals("true")
				&& !properties.getProperty(Constants.PROP_SECURITY_ENABLED).trim().equals("false")) {
			result.add("Property " + Constants.PROP_SECURITY_ENABLED + " must be 'true' or 'false'");
		}

		String securityEnabled = properties.getProperty(Constants.PROP_SECURITY_ENABLED);

		// Validate property PROP_USER_NAME.
		// Criterias: present if CONNECTOR_SECURITY_ENABLED is true
		String userName = properties.getProperty(Constants.PROP_USER_NAME);
		if (StringUtils.isEmpty(userName) && "true".equals(securityEnabled)) {
			result.add("Property " + Constants.PROP_USER_NAME + " must be present if " + Constants.PROP_SECURITY_ENABLED
					+ " is true");
		}
	
		// Validate property PROP_PASSWORD.
		// Criterias: present if CONNECTOR_SECURITY_ENABLED is true
		String userPwd = properties.getProperty(Constants.PROP_PASSWORD);
		if (StringUtils.isEmpty(userPwd) && "true".equals(securityEnabled)) {
			result.add("Property " + Constants.PROP_USER_NAME + " must be present if " + Constants.PROP_SECURITY_ENABLED
					+ " is true");
		}

		// Validate property meEngineHost.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(Constants.PROP_MSGING_HOST))) {
			result.add("Property " + Constants.PROP_MSGING_HOST + " is missing or empty");
		}

		// Validate property meEnginePort.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(Constants.PROP_MSGING_PORT))) {
			result.add("Property " + Constants.PROP_MSGING_PORT + " is missing or empty");
		}
		
		// Validate property meEngineChain.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(Constants.PROP_MSGING_CHAIN))) {
			result.add("Property " + Constants.PROP_MSGING_CHAIN + " is missing or empty");
		}

		return result;
	}
}
