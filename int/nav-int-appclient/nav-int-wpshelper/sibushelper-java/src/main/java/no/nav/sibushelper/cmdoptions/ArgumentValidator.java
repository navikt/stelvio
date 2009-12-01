/**
 * 
 */
package no.nav.sibushelper.cmdoptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.sibushelper.SIBUSHelper;
import no.nav.sibushelper.common.Constants;
import no.nav.sibushelper.helper.Configuration;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class ArgumentValidator {

	private static Logger logger = Logger.getLogger(SIBUSHelper.class.getName());
	private String className = Configuration.class.getName();

	public List validate(CommandLine cl) {
		List<String> result = new ArrayList<String>();

		// logFilePath
		validateLogPath(cl.getOptionValue(CommandOptions.reportDirectory));

		/*
		 * maxResultSet String maxResultSet =
		 * cl.getOptionValue(CommandOptions.maxResultSet); if
		 * (!StringUtils.isEmpty(maxResultSet) && Integer.parseInt(maxResultSet) >
		 * 9999) { result.add("Property " + CommandOptions.maxResultSet + " is
		 * to large. Possible values is 1 >= 9999"); }
		 */

		// action
		String action = cl.getOptionValue(CommandOptions.action);
		String actionValidationMessage = validateAction(action);
		if (!"".equals(actionValidationMessage)) {
			result.add(actionValidationMessage);
		}

		// component
		String component = cl.getOptionValue(CommandOptions.component);
		String componentValidationMessage = validateComponent(component);
		if (!"".equals(componentValidationMessage)) {
			result.add(componentValidationMessage);
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

	/**
	 * @param action
	 * @return
	 */
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

	/**
	 * @param action
	 * @return
	 */
	private String validateComponent(String component) {
		String validationMessage = "";
		if (StringUtils.isEmpty(component)) {
			validationMessage = "Property " + CommandOptions.component + " is missing or empty";
		} else {
			if (!Constants.COMPONENTS.contains(component)) {
				validationMessage = "Property " + CommandOptions.component + " value supplied is not a valid option.";
			}
		}
		return validationMessage;
	}

	/**
	 * @param logFilePath
	 */
	private void validateLogPath(String logFilePath) {
		if (!StringUtils.isEmpty(logFilePath)) {
			logger.logp(Level.FINE, className, "validateLogPath", "Using '" + logFilePath + " as logfile location");
		} else {
			logger.logp(Level.WARNING, className, "validateLogPath", CommandOptions.reportDirectory
					+ " is not declared. Using current folder.");
		}
	}
}
