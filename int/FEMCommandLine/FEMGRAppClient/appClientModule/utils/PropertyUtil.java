package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import no.nav.femhelper.common.Constants;

/**
 * Class to perform utility operations in connection with properties
 * and configuration
 * 
 * @author Andreas Røe
 */
public class PropertyUtil {
	
	public List validateProperties(Properties properties) {
		List<String> result = new ArrayList<String>();

		// Validate property CONNECTOR_HOST.
		// Criterias: present
		if (StringUtils.isEmpty(properties.getProperty(Constants.CONNECTOR_HOST))) {
			result.add("Property " + Constants.CONNECTOR_HOST + " is missing or empty");
		}
		
		// Validate property CONNECTOR_PORT.
		// Criterias: present
		if ("".equals(properties.getProperty(Constants.CONNECTOR_PORT)) || properties.getProperty(Constants.CONNECTOR_PORT) == null) {
			result.add("Property " + Constants.CONNECTOR_PORT + " is missing or empty");
		}		
		
		// Validate property CONNECTOR_TYPE.
		// Criterias: present
		if ("".equals(properties.getProperty(Constants.CONNECTOR_TYPE)) || properties.getProperty(Constants.CONNECTOR_TYPE) == null) {
			result.add("Property " + Constants.CONNECTOR_TYPE + " is missing or empty");
		}		

		
		// Validate property CONNECTOR_SECURITY_ENABLED.
		// Criterias: present and 'true' or 'false' 
		if ("".equals(properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED)) || properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED) == null ) {
			result.add("Property " + Constants.CONNECTOR_SECURITY_ENABLED + " is missing or empty");
		} else if (!properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED).trim().equals("true")	
				&& !properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED).trim().equals("false")) {
			result.add("Property " + Constants.CONNECTOR_SECURITY_ENABLED + " must be 'true' or 'false'");
		}
		
		// Validate property USERNAME.
		// Criterias: present if CONNECTOR_SECURITY_ENABLED is true
		String userName = properties.getProperty(Constants.USERNAME);
		String securityEnabled = properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED);
		if (StringUtils.isEmpty(userName) && "true".equals(securityEnabled)) {
			result.add("Property " + Constants.USERNAME + " must be present if " + Constants.CONNECTOR_SECURITY_ENABLED + " is true");
		}	

		return result;
	}
}
