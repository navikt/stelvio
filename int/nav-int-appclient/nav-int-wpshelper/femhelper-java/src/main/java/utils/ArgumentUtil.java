package utils;

import java.util.HashMap;
import java.util.Map;

import no.nav.femhelper.cmdoptions.CommandOptions;

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

		String sourceModule = cl.getOptionValue(CommandOptions.sourceModule);
		if (!StringUtils.isEmpty(sourceModule)) {
			result.put(CommandOptions.sourceModule, sourceModule);
		}

		String sourceComponent = cl.getOptionValue(CommandOptions.sourceComponent);
		if (!StringUtils.isEmpty(sourceComponent)) {
			result.put(CommandOptions.sourceComponent, sourceComponent);
		}

		String destModule = cl.getOptionValue(CommandOptions.destinationModule);
		if (!StringUtils.isEmpty(destModule)) {
			result.put(CommandOptions.destinationModule, destModule);
		}

		String destComponent = cl.getOptionValue(CommandOptions.destinationComponent);
		if (!StringUtils.isEmpty(destComponent)) {
			result.put(CommandOptions.destinationComponent, destComponent);
		}

		String failureMessage = cl.getOptionValue(CommandOptions.failureMessage);
		if (!StringUtils.isEmpty(CommandOptions.failureMessage)) {
			result.put(CommandOptions.failureMessage, failureMessage);
		}

		String dataObject = cl.getOptionValue(CommandOptions.dataObject);
		if (!StringUtils.isEmpty(dataObject)) {
			result.put(CommandOptions.dataObject, dataObject);
		}

		String timeFrame = cl.getOptionValue(CommandOptions.timeFrame);
		if (!StringUtils.isEmpty(timeFrame)) {
			result.put(CommandOptions.timeFrame, timeFrame);
		}

		String maxResultSet = cl.getOptionValue(CommandOptions.maxResultSet);
		// Default value 1000
		if (StringUtils.isEmpty(maxResultSet)) {
			result.put(CommandOptions.maxResultSet, "1000");
		} else {
			result.put(CommandOptions.maxResultSet, maxResultSet);
		}

		String maxResultSetPaging = cl.getOptionValue(CommandOptions.maxResultSetPaging);
		if (StringUtils.isEmpty(maxResultSetPaging)) {
			result.put(CommandOptions.maxResultSetPaging, "true");
		} else {
			result.put(CommandOptions.maxResultSetPaging, maxResultSetPaging);
		}

		String reportDirectory = cl.getOptionValue(CommandOptions.reportDirectory);
		if (StringUtils.isEmpty(reportDirectory)) {
			result.put(CommandOptions.reportDirectory, ".");
		} else {
			result.put(CommandOptions.reportDirectory, reportDirectory);
		}

		return result;
	}
}
