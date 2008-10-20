package no.nav.femhelper.cmdoptions;

import no.nav.femhelper.common.Constants;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;

/**
 * Utility class to build options to the <code>CommandLine</code> parser
 * 
 * @author Andreas Roe
 */
@SuppressWarnings("static-access")
public class CommandOptionsBuilder {
	/**
	 * Method that gather all options
	 * 
	 * @return all options
	 */
	public Options getOptions() {
		Options options = new Options();
		options.addOption(getHelpOption());
		options.addOption(getConfigFileOption());
		options.addOption(getLogFilePathOption());
		options.addOption(getActionOption());
		options.addOption(getMaxResultSetOption());
		options.addOption(getMaxResultSetPagingOption());
		options.addOption(getFailureMessageOption());
		options.addOption(getDataObjectOption());
		options.addOption(getNoStopOption());
		options.addOption(getTimeFrameOption());
		options.addOption(getSourceModuleOption());
		options.addOption(getSourceComponentOption());
		options.addOption(getDestinationModule());
		options.addOption(getDestinationComponent());
		return options;
	}

	/**
	 * Create command line option for 'sourceModule'
	 * 
	 * @return sourceModule option
	 */
	private Option getSourceModuleOption() {
		Option sourceModule = getGeneralOption(CommandOptions.sourceModule, "sm");
		sourceModule.setArgName("String");
		sourceModule.setDescription("Filter by Source Module");
		return sourceModule;
	}

	/**
	 * Create command line option for 'sourceComponent'
	 * 
	 * @return sourceComponent option
	 */
	private Option getSourceComponentOption() {
		Option sourceComponent = getGeneralOption(CommandOptions.sourceComponent, "sc");
		sourceComponent.setDescription("Filter by Source Component");
		return sourceComponent;
	}

	/**
	 * Create command line option for 'destinationModule'
	 * 
	 * @return destinationModule option
	 */
	private Option getDestinationModule() {
		Option destinationModule = getGeneralOption(CommandOptions.destinationModule, "dm");
		destinationModule.setDescription("Filter by Destination Module");
		return destinationModule;
	}

	/**
	 * Create command line option for 'destinationComponent'
	 * 
	 * @return destinationComponent option
	 */
	private Option getDestinationComponent() {
		Option destinationComponent = getGeneralOption(CommandOptions.destinationComponent, "dc");
		destinationComponent.setDescription("Filter by Destination Component");
		return destinationComponent;
	}

	/**
	 * Create command line option for 'failureMessage'
	 * 
	 * @return failureMessage option
	 */
	private Option getFailureMessageOption() {
		Option failureMessage = getGeneralOption(CommandOptions.failureMessage, "fm");
		failureMessage
				.setDescription("Wild card search in the failure message. Might be used to drill down a given exception etc.");
		return failureMessage;
	}

	/**
	 * Create command line option for 'dataObject'
	 * 
	 * @return dataObject option
	 */
	private Option getDataObjectOption() {
		Option dataObject = getGeneralOption(CommandOptions.dataObject, "do");
		return dataObject;
	}

	/**
	 * Create command line option for 'timeFrame'
	 * 
	 * @return timeFrame option
	 */
	private Option getTimeFrameOption() {
		Option timeFrame = getGeneralOption(CommandOptions.timeFrame, "tf");
		timeFrame.setArgName("timeframe");
		timeFrame.setRequired(false);
		timeFrame.setDescription("Filter by time. Pattern:" + Constants.TIME_FRAME_FORMAT + "-" + Constants.TIME_FRAME_FORMAT);
		return timeFrame;
	}

	/**
	 * Create command line option for 'maxResultSetPaging'. This option is
	 * requiered
	 * 
	 * @return the mandatory option maxResultSetPaging
	 * @TODO AR This could have a default value
	 */
	private Option getMaxResultSetPagingOption() {
		Option maxResultSetPaging = getGeneralOption(CommandOptions.maxResultSetPaging, "mrsp");
		maxResultSetPaging.setArgName("boolean");
		maxResultSetPaging.setDescription("Default is 'true'");
		return maxResultSetPaging;
	}

	/**
	 * Create command line option for 'maxResultSet'. This option is requiered
	 * 
	 * @return
	 * @TODO AR This could have a default value
	 */
	private Option getMaxResultSetOption() {
		Option maxResultSet = getGeneralOption(CommandOptions.maxResultSet, "mrs");
		maxResultSet.setArgName("integer");
		maxResultSet
				.setDescription("Default value is '1000'. Recommended to use range between 100 and 1000. This is a restriction due to JVM memory limitations");
		return maxResultSet;
	}

	/**
	 * Create command line option for 'action'. This option is requiered
	 * 
	 * @return
	 */
	private Option getActionOption() {
		Option action = getGeneralOption(CommandOptions.action, "a");
		action.setArgName(Constants.ACTIONS.toString());
		action.setDescription("Mandatory");
		return action;
	}

	/**
	 * Create command line option for 'logFilePath'. This option is requiered
	 * 
	 * @return
	 */
	private Option getLogFilePathOption() {
		Option logFilePath = getGeneralOption(CommandOptions.reportDirectory, "rd");
		logFilePath.setArgName("path");
		logFilePath.setDescription("Default is current Path for directory where the output file will be located.");
		return logFilePath;
	}

	/**
	 * Create command line option for 'configFile'. This option is requiered
	 * 
	 * @return
	 */
	private Option getConfigFileOption() {
		Option configFile = getGeneralOption(CommandOptions.configFile, "cf");
		configFile.setArgName("full path");
		configFile
				.setDescription("Mandatory. Full path to configuration file for system environment spesifications (hostname etc.).");
		return configFile;
	}

	/**
	 * Create command line option for 'noStop'. This paramater is used if
	 * actions not shal prompt before executing the action
	 * 
	 * @return
	 */
	private Option getNoStopOption() {
		Option noStop = OptionBuilder.withLongOpt(CommandOptions.noStop).create("ns");
		noStop.setDescription("Runs the action without prompting between collecting the msgids and the action");
		return noStop;
	}

	/**
	 * Create command line option for 'help'
	 * 
	 * @return
	 */
	private Option getHelpOption() {
		Option help = OptionBuilder.withLongOpt(CommandOptions.help).create("h");
		help.setDescription("This help index");
		return help;
	}

	/**
	 * Method that builds a often used <code>Option</code> with long desc,
	 * value separator and has arguments
	 * 
	 * @param optionName
	 * @return
	 */
	private Option getGeneralOption(String optionName, String shortOption) {
		Option option = OptionBuilder.withLongOpt(optionName).withValueSeparator().hasArg().create(
				StringUtils.isEmpty(shortOption) ? null : shortOption);
		return option;
	}
}
