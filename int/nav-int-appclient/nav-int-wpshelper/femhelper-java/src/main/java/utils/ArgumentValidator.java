package utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.femhelper.cmdoptions.CommandOptions;
import no.nav.femhelper.common.Constants;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;

/**
 * Class with responsibility for validating command line inputs. This might be
 * performed with Apache CLI as well, but that will just not list the erros in a
 * properly way.
 * 
 * @author Andreas Røe
 */
public class ArgumentValidator {
	private static Logger LOGGER = Logger.getLogger(ArgumentValidator.class.getName());

	public List validate(CommandLine cl) {
		List<String> result = new ArrayList<String>();

		// logFilePath
		validateLogPath(cl.getOptionValue(CommandOptions.reportDirectory));

		// maxResultSet
		String maxResultSet = cl.getOptionValue(CommandOptions.maxResultSet);
		if (!StringUtils.isEmpty(maxResultSet) && Integer.parseInt(maxResultSet) > 9999) {
			result.add("Property " + CommandOptions.maxResultSet + " is to large. Possible values is 1 >= 9999");
		}

		// maxResultSetPaging
		String maxResultSetPaging = cl.getOptionValue(CommandOptions.maxResultSetPaging);
		if (!StringUtils.isEmpty(maxResultSetPaging) && !maxResultSetPaging.equals(Boolean.TRUE.toString())
				&& !maxResultSetPaging.equals(Boolean.FALSE.toString())) {
			result.add("Property " + CommandOptions.maxResultSetPaging + " is set must be 'true' or 'false'");
		}

		// delimiter
		String delimiter = cl.getOptionValue(CommandOptions.delimiter);
		if (!StringUtils.isEmpty(delimiter) && delimiter.length() != 1) {
			result.add("Property " + CommandOptions.delimiter + " is set but consists of more than one character");
		}

		// delimiter
		String messageIdFile = cl.getOptionValue(CommandOptions.messageIdFile);
		if (!StringUtils.isEmpty(messageIdFile)) {
			File file = new File(messageIdFile);
			if (!file.canRead()) {
				result.add("Unable to read from file " + messageIdFile);
			}
		}

		// action
		// TODO AR Rewrite this to use toMap or something like that
		String action = cl.getOptionValue(CommandOptions.action);
		String actionValidationMessage = validateAction(action);
		if (!"".equals(actionValidationMessage)) {
			result.add(actionValidationMessage);
		}

		// timeFrame is validated againts the '-' separator,
		// and the date values on both sides are validated against the pattern.
		String timeFrame = cl.getOptionValue(CommandOptions.timeFrame);
		if (!StringUtils.isEmpty(timeFrame)) {

			// Validate that the String has one, and only one '-'
			// sign to separate the to- and from date.
			String times[] = StringUtils.split(timeFrame, "-");
			if (null == times || times.length != 2) {
				result.add("Property " + CommandOptions.timeFrame + " is not correct formatted. " + "The pattern is "
						+ Constants.TIME_FRAME_FORMAT);
			} else {

				// Validate the pattern
				try {
					SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FRAME_FORMAT);
					sdf.parse(times[0]);
					sdf.parse(times[1]);
				} catch (ParseException e) {
					result.add("Property " + CommandOptions.timeFrame + " is not correctly formatted. Expected pattern is "
							+ Constants.TIME_FRAME_FORMAT);
				}
			}
		}
		return result;
	}

	private String validateAction(String action) {
		String validationMessage = "";
		if (StringUtils.isEmpty(action)) {
			validationMessage = "Property " + CommandOptions.action + " is missing or empty";
		} else {
			if (!Constants.ACTIONS.contains(action)) {
				validationMessage = "Property " + CommandOptions.action + " value supplied is not a valid option.";
			}
		}
		return validationMessage;
	}

	private void validateLogPath(String logFilePath) {
		if (!StringUtils.isEmpty(logFilePath)) {
			LOGGER.log(Level.FINE, "Using '" + logFilePath + " as logfile location");
		} else {
			LOGGER.log(Level.WARNING, CommandOptions.reportDirectory + " is not declared. Using current folder.");
		}
	}
}
