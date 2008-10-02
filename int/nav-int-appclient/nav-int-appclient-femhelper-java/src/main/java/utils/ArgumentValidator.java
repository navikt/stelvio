package utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.appclient.util.Constants;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;

/**
 * Class with responsibility for validating command line inputs.
 * This might be performed with Apache CLI as well, but that
 * will just not list the erros in a properly way.
 * 
 * @author Andreas Røe
 */
public class ArgumentValidator {
	
	private static Logger LOGGER = Logger.getLogger(ArgumentValidator.class.getName());
	
	public List validate(CommandLine cl) {
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "validate");
		
		List<String> result = new ArrayList<String>();
		
		// logFilePath
		validateLogPath(cl.getOptionValue(Constants.logFilePath));
		
		// maxResultSet
		if (StringUtils.isEmpty(cl.getOptionValue(Constants.maxResultSet))) {
			result.add("Property " + Constants.maxResultSet + " is missing or empty");
		} else if (Integer.parseInt(cl.getOptionValue(Constants.maxResultSet)) > 9999) {
			result.add("Property " + Constants.maxResultSet + " is to large. Possible values is 1 >= 9999");
		}
		
		// maxResultSetPaging
		String maxResultSetPaging = cl.getOptionValue(Constants.maxResultSetPaging);
		if (StringUtils.isEmpty(maxResultSetPaging)) {
			result.add("Property " + Constants.maxResultSetPaging + " is missing or empty");
		} else if (!maxResultSetPaging.equals(Boolean.TRUE.toString()) && !maxResultSetPaging.equals(Boolean.FALSE.toString())) {
			result.add("Property " + Constants.maxResultSetPaging + " must be 'true' or 'false'");
		}

		// action
		// TODO AR Rewrite this to use toMap or something like that
		String action = cl.getOptionValue(Constants.action);
		String actionValidationMessage = validateAction(action);
		if (!"".equals(actionValidationMessage)) {
			result.add(actionValidationMessage);
		}
				
		// timeFrame is validated againts the '-' separator, 
		// and the date values on both sides are validated against the pattern.
		String timeFrame = cl.getOptionValue(Constants.timeFrame);
		if (Constants.ACTION_TIMEFRAME.equals(action) && StringUtils.isEmpty(timeFrame)) {
			result.add("Property " + Constants.timeFrame + " is empty");
		} else if (Constants.ACTION_TIMEFRAME.equals(action)) {

			// Validate that the String has one, and only one '-'
			// sign to separate the to- and from date.
			String times[] = StringUtils.split(timeFrame, "-");
			if (null == times || times.length != 2) {
				result.add("Property " + Constants.timeFrame + " is not correct formatted. " + "The pattern is "
						+ Constants.TIME_FRAME_FORMAT);
			} else {

				// Validate the pattern
				try {
					SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FRAME_FORMAT);
					sdf.parse(times[0]);
					sdf.parse(times[1]);
				} catch (ParseException e) {
					result.add("Property " + Constants.timeFrame + " is not correctly formatted. Expected pattern is "
							+ Constants.TIME_FRAME_FORMAT);
				}
			}
		}		
		return result;
	}

	private String validateAction(String action) {
		String validationMessage = "";
		if (StringUtils.isEmpty(action)) {
			validationMessage = "Property " + Constants.action + " is missing or empty";
		} else {	
			if (!Constants.ACTIONS.contains(action)) {
				validationMessage = "Property " + Constants.action + " value supplied is not a valid option.";
			} 	
		}		
		return validationMessage;
	}

	private void validateLogPath(String logFilePath) {
		if (!StringUtils.isEmpty(logFilePath)) {
			LOGGER.log(Level.FINE, "Using '" + logFilePath + " as logfile location");
		} else {
			String tempFolderProperty = "java.io.tmpdir";
			String tempFolder = System.getProperty(tempFolderProperty); 
			LOGGER.log(Level.WARNING, Constants.logFilePath + " is not declared. Using " + tempFolder + ".");
		}
	}
}
