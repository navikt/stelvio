package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import no.nav.appclient.util.Constants;

/**
 * Class to perform utility operations in connection with properties
 * and configuration
 * 
 * @author Andreas R�e
 */
public class PropertyUtil {
	
	/**
	 * Logger instance
	 */
	private static Logger LOGGER = Logger.getLogger(PropertyUtil.class.getName());
	
	public List validateProperties(Properties properties) {
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "validateProperties");
		
		List<String> result = new ArrayList<String>();

		// Validate property BootstrapHost.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(Constants.BootstrapHost))) {
			result.add("Property " + Constants.BootstrapHost + " is missing or empty");
		}
		
		// Validate property CONNECTOR_PORT.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(Constants.BootstrapPort))) {
			result.add("Property " + Constants.BootstrapPort + " is missing or empty");
		}		
		
		// Validate property CONNECTOR_TYPE.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(Constants.CONNECTOR_TYPE))) {
			result.add("Property " + Constants.CONNECTOR_TYPE + " is missing or empty");
		}		

		
		// Validate property CONNECTOR_SECURITY_ENABLED.
		// Criterias: present and 'true' or 'false' 
		if (StringUtils.isEmpty(properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED))) {
			result.add("Property " + Constants.CONNECTOR_SECURITY_ENABLED + " is missing or empty");
		} else if (!properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED).trim().equals("true")	
				&& !properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED).trim().equals("false")) {
			result.add("Property " + Constants.CONNECTOR_SECURITY_ENABLED + " must be 'true' or 'false'");
		}
		
		// Validate property USERNAME.
		// Criterias: present if CONNECTOR_SECURITY_ENABLED is true
		String userName = properties.getProperty(Constants.username);
		String securityEnabled = properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED);
		if (StringUtils.isEmpty(userName) && "true".equals(securityEnabled)) {
			result.add("Property " + Constants.username + " must be present if " + Constants.CONNECTOR_SECURITY_ENABLED + " is true");
		}	

		LOGGER.log(Level.FINE, Constants.METHOD_EXIT+ "validateProperties");
		return result;
	}
}
