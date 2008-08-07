package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class to perform utility operations in connection with properties
 * and configuration
 * 
 * @author Andreas Røe
 */
public class PropertyUtil {
	
	// TODO AR Make this class with non-static methods, as that could might 
	// create unwanted errorsituations during the loading of this application
	public static List validateProperties(Properties properties) {
		List<String> result = new ArrayList<String>();

		// 1. MANDANTORY
		
		// Validate property CONNECTOR_HOST.
		// Criterias: present
		if ("".equals(properties.getProperty(Constants.CONNECTOR_HOST)) || properties.getProperty(Constants.CONNECTOR_HOST) == null) {
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
		if (("".equals(properties.getProperty(Constants.USERNAME)) || properties.getProperty(Constants.USERNAME) == null) && properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED).trim().equals("true")) {
			result.add("Property " + Constants.USERNAME + " must be present if " + Constants.CONNECTOR_SECURITY_ENABLED + " is true");
		}	

		// Validate property MAX_RESULT_SET.
		if ("".equals(properties.getProperty(Constants.MAX_RESULT_SET)) ||properties.getProperty(Constants.MAX_RESULT_SET)==null ) {
			result.add("Property " + Constants.MAX_RESULT_SET + " is missing or empty");
		}

		// Validate property MAX_RESULT_SET.
		if ("".equals(properties.getProperty(Constants.MAX_RESULT_SET_PAGING)) ||properties.getProperty(Constants.MAX_RESULT_SET_PAGING)==null ) {
			result.add("Property " + Constants.MAX_RESULT_SET_PAGING + " is missing or empty");
		} else if (!properties.getProperty(Constants.MAX_RESULT_SET_PAGING).trim().equals("true")	
				&& !properties.getProperty(Constants.MAX_RESULT_SET_PAGING).trim().equals("false")) {
			result.add("Property " + Constants.MAX_RESULT_SET_PAGING + " must be 'true' or 'false'");
		}
		
		// Validate property FEM_EDA_TYPE.
		if ("".equals(properties.getProperty(Constants.FEM_EDA_TYPE)) ||properties.getProperty(Constants.FEM_EDA_TYPE)==null ) {
			result.add("Property " + Constants.FEM_EDA_TYPE + " is missing or empty");
		}
		else
		{	
			// Validate options of property
			boolean foundIt=false;
			for (int i = 0; i < Constants.FEM_EDA_TYPE_OPTIONS.length; i++) {
		
				if (Constants.FEM_EDA_TYPE_OPTIONS[i].equals(properties.getProperty(Constants.FEM_EDA_TYPE))) 
				{
					foundIt = true;
					break;
				}
			}
			if (!foundIt)
			{
				result.add("Property " + Constants.FEM_EDA_TYPE + " value supplied is not a valid option.");
			}
		}	
		
		// Validate property FEM_EDA_TYPE_ACTION.
		if ("".equals(properties.getProperty(Constants.FEM_EDA_TYPE_ACTION)) ||properties.getProperty(Constants.FEM_EDA_TYPE_ACTION)==null ) {
			result.add("Property " + Constants.FEM_EDA_TYPE_ACTION + " is missing or empty");
		}
		else
		{	
			// Validate options of property
			boolean foundIt=false;
			for (int i = 0; i < Constants.FEM_EDA_TYPE_ACTION_OPTIONS.length; i++) {
		
				if (Constants.FEM_EDA_TYPE_ACTION_OPTIONS[i].equals(properties.getProperty(Constants.FEM_EDA_TYPE_ACTION))) 
				{
					foundIt = true;
					break;
				}
			}
			if (!foundIt)
			{
				result.add("Property " + Constants.FEM_EDA_TYPE_ACTION + " value supplied is not a valid option.");
			}
		}	
		
		// 2. BASED ON FEM_EDA_ACTION_TYPE
		
		// Validate property LOGFILE_PATH.
		// Criterias: present
		if ("".equals(properties.getProperty(Constants.LOGFILE_PATH)) || properties.getProperty(Constants.LOGFILE_PATH) == null ) {
			result.add("Property " + Constants.LOGFILE_PATH + " is missing or empty ");
		}

		
		// return the validator
		return result;
	}
}
