package no.nav.sibushelper.cmdoptions;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;

/**
 * This class abstract the handling of getting commandline arguments that is a
 * part of the search (like SourceModule, DestinationComponent etc. The result
 * from methods in this class is typically used as input to
 * <code>AbstractActions.collectEvents(..)</code>.
 * 
 * @author Andreas Roe
 */
public class ArgumentUtil {
	public static Map<String, String> getArguments(CommandLine cl) {
		Map<String, String> result = new HashMap<String, String>();

		String failureMessage = cl.getOptionValue(CommandOptions.failureMessage);
		if (!StringUtils.isEmpty(CommandOptions.failureMessage)) {
			result.put(CommandOptions.failureMessage, failureMessage);
		}

		String timeFrame = cl.getOptionValue(CommandOptions.timeFrame);
		if (!StringUtils.isEmpty(timeFrame)) {
			result.put(CommandOptions.timeFrame, timeFrame);
		}

		String maxResultSet = cl.getOptionValue(CommandOptions.maxResultSet);
		// Default value 100
		if (StringUtils.isEmpty(maxResultSet)) {
			result.put(CommandOptions.maxResultSet, "100");
		} else {
			result.put(CommandOptions.maxResultSet, maxResultSet);
		}

		String reportDirectory = cl.getOptionValue(CommandOptions.reportDirectory);
		if (StringUtils.isEmpty(reportDirectory)) {
			result.put(CommandOptions.reportDirectory, ".");
		} else {
			result.put(CommandOptions.reportDirectory, reportDirectory);
		}

		String optionComponent = cl.getOptionValue(CommandOptions.component);
		if (!StringUtils.isEmpty(optionComponent)) {
			result.put(CommandOptions.component, optionComponent);
		}

		return result;
	}
}
