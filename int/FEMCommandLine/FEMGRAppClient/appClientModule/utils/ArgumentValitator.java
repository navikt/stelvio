package utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.femhelper.common.Constants;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;

/**
 * Class with responsibility for validating command line inputs.
 * This might be performed with Apache CLI as well, but that
 * will just not list the erros in a properly way.
 * 
 * @author Andreas Røe
 */
public class ArgumentValitator {
	
	private static Logger LOGGER = Logger.getLogger(ArgumentValitator.class.getName());
	
	public List validate(CommandLine cl) {
		List <String> result = new ArrayList<String>();
		
		// logFilePath
		String logFilePath = cl.getOptionValue(Constants.logFilePath);
		if (!StringUtils.isEmpty(logFilePath)) {
			LOGGER.log(Level.FINE, "Using '" + logFilePath + " as logfile location");
		} else {
			String tempFolderProperty = "java.io.tmpdir";
			String tempFolder = System.getProperty(tempFolderProperty); 
			LOGGER.log(Level.WARNING, Constants.logFilePath + " is not declared. " + tempFolder + " will be used");
		}
		
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

		// messageType
		// TODO AR Rewrite this to use toMap or something like that
		String messageType = cl.getOptionValue(Constants.messageType);
		if (StringUtils.isEmpty(messageType)) {
			result.add("Property " + Constants.messageType + " is missing or empty");
			
		} else {
			boolean found = false;
			for (int i = 0; i < Constants.messageTypeOptions.length; i++) {
				if (Constants.messageTypeOptions[i].equals(messageType)) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				result.add("Property " + Constants.messageType + " value supplied is not a valid option.");
			}
		}
		
		
		// action
		// TODO AR Rewrite this to use toMap or something like that
		String action = cl.getOptionValue(Constants.action);
		if (StringUtils.isEmpty(action)) {
			result.add("Property " + Constants.action + " is missing or empty");
		} else {
			
			boolean found = false;
			for (int i = 0; i < Constants.actionOptions.length; i++) {
				if (Constants.actionOptions[i].equals(action)) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				result.add("Property " + Constants.action + " value supplied is not a valid option.");
			}
		}
		
		// timeFrame is validated againts the '-' separator, 
		// and the date values on both sides are validated against
		// the ssmm.MMyyyy pattern.
		String timeFrame = cl.getOptionValue(Constants.timeFrame);
		if (StringUtils.isEmpty(timeFrame)) {
			result.add("Property " + Constants.timeFrame + " is empty");
		} else {
			
				// Validate that the String has one, and only one '-'
				// sign to separate the to- and from date.
				String times[] = StringUtils.split(timeFrame, "-");
				if (null == times || times.length != 2) {
					result.add("Property " + Constants.timeFrame + " is not correct formatted. " +
							"The pattern is " + Constants.TIME_FRAME_FORMAT);
				}
				
				// Validate the pattern
				try {
					SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FRAME_FORMAT);
					sdf.parse(times[0]);
					sdf.parse(times[1]);
				} catch (ParseException e) {
					result.add("Property " + Constants.timeFrame + " is not correct formatted. " +
							"The pattern is " + Constants.TIME_FRAME_FORMAT);
				}
		}
		
		return result;
	}
}
