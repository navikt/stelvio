package utils;

import java.util.HashMap;
import java.util.Map;

import no.nav.femhelper.common.Constants;

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

		String sourceModule = cl.getOptionValue(Constants.sourceModule);
		if (!StringUtils.isEmpty(sourceModule)) {
			result.put(Constants.sourceModule, sourceModule);
		}

		String sourceComponent = cl.getOptionValue(Constants.sourceComponent);
		if (!StringUtils.isEmpty(sourceComponent)) {
			result.put(Constants.sourceComponent, sourceComponent);
		}

		String destModule = cl.getOptionValue(Constants.destinationModule);
		if (!StringUtils.isEmpty(destModule)) {
			result.put(Constants.destinationModule, destModule);
		}

		String destComponent = cl.getOptionValue(Constants.destinationComponent);
		if (!StringUtils.isEmpty(destComponent)) {
			result.put(Constants.destinationComponent, destComponent);
		}

		String failureMessage = cl.getOptionValue(Constants.failureMessage);
		if (!StringUtils.isEmpty(Constants.failureMessage)) {
			result.put(Constants.failureMessage, failureMessage);
		}

		String dataObject = cl.getOptionValue(Constants.dataObject);
		if (!StringUtils.isEmpty(dataObject)) {
			result.put(Constants.dataObject, dataObject);
		}

		String timeFrame = cl.getOptionValue(Constants.timeFrame);
		if (!StringUtils.isEmpty(timeFrame)) {
			result.put(Constants.timeFrame, timeFrame);
		}

		String maxResultSet = cl.getOptionValue(Constants.maxResultSet);
		// Default value 1000
		if (StringUtils.isEmpty(maxResultSet)) {
			result.put(Constants.maxResultSet, "1000");
		} else {
			result.put(Constants.maxResultSet, maxResultSet);
		}

		String maxResultSetPaging = cl.getOptionValue(Constants.maxResultSetPaging);
		if (StringUtils.isEmpty(maxResultSetPaging)) {
			result.put(Constants.maxResultSetPaging, "true");
		} else {
			result.put(Constants.maxResultSetPaging, maxResultSetPaging);
		}

		String reportDirectory = cl.getOptionValue(Constants.reportDirectory);
		if (StringUtils.isEmpty(reportDirectory)) {
			result.put(Constants.reportDirectory, ".");
		} else {
			result.put(Constants.reportDirectory, reportDirectory);
		}

		return result;
	}
}
