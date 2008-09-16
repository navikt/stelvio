package no.nav.femhelper.cmdoptions;

import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.femhelper.common.Constants;
import no.nav.femhelper.filewriters.AbstractFileWriter;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.ArrayUtils;

/**
 * Utility class to build options to the <code>CommandLine</code> parser
 * @author Andreas Roe
 */
public class CommandOptionsBuilder {
	
	/**
	 * Logger instance
	 */
	private Logger logger = Logger.getLogger(AbstractFileWriter.class.getName());
	
	/**
	 * Method that gather all options
	 * @return all options
	 */
	public Options getOptions() {	
		Options options = new Options();
		options.addOption(getHelpOption());
		options.addOption(getConfigFileOption());
		options.addOption(getLogFilePathOption());
		options.addOption(getLogFileNameOption());
		options.addOption(getActionOption());
		options.addOption(getMaxResultSetOption());
		options.addOption(getMaxResultSetPagingOption());
		options.addOption(getTimeFrameOption());
		options.addOption(getSourceModuleOption());
		options.addOption(getSourceComponentOption());
		options.addOption(getDestinationModule());
		options.addOption(getDestinationComponent());
		options.addOption(getFailureMessageOption());
		options.addOption(getDataObjectOption());
		
		logger.log(Level.FINE, Constants.METHOD_EXIT + "getOptions");
		return options;
	}

	/**
	 * Create command line option for 'sourceModule'
	 * @return sourceModule option
	 */
	private Option getSourceModuleOption() {
		Option sourceModule = getGeneralOption(Constants.sourceModule);
		sourceModule.setArgName("String");
		sourceModule.setDescription("This attribute might in certain circumstances be empty. If this occurs you might make use of the --sessionIdWildCard parameter");
		return sourceModule;
	}
	
	/**
	 * Create command line option for 'sourceComponent'
	 * @return sourceComponent option
	 */
	private Option getSourceComponentOption() {
		Option sourceComponent = getGeneralOption(Constants.sourceComponent);
		sourceComponent.setDescription("This attribute might in certain circumstances be empty. If this occurs you might make use of the --sessionIdWildCard parameter");
		return sourceComponent;
	}
	
	/**
	 * Create command line option for 'destinationModule'
	 * @return destinationModule option
	 */
	private Option getDestinationModule() {
		Option destinationModule = getGeneralOption(Constants.destinationModule);
		destinationModule.setDescription("This attribute might in certain circumstances be empty. If this occurs you might make use of the --sessionIdWildCard parameter");
		return destinationModule;
	}
	
	/**
	 * Create command line option for 'destinationComponent'
	 * @return destinationComponent option
	 */
	private Option getDestinationComponent() {
		Option destinationComponent = getGeneralOption(Constants.destinationComponent);
		destinationComponent.setDescription("This attribute might in certain circumstances be empty. If this occurs you might make use of the --sessionIdWildCard parameter");
		return destinationComponent;
	}
	
	/**
	 * Create command line option for 'failureMessage'
	 * @return failureMessage option
	 */
	private Option getFailureMessageOption() {
		Option failureMessage = getGeneralOption(Constants.failureMessage);
		failureMessage.setDescription("Wild card search in the failure message. Might be used to drill down a given exception etc.");
		return failureMessage;
	}
	
	/**
	 * Create command line option for 'dataObject'
	 * @return dataObject option
	 */
	private Option getDataObjectOption() {
		Option dataObject = getGeneralOption(Constants.dataObject);
		return dataObject;
	}
	
	/**
	 * Create command line option for 'timeFrame'
	 * @return timeFrame option
	 */
	private Option getTimeFrameOption() {
		Option timeFrame = getGeneralOption(Constants.timeFrame);
		timeFrame.setArgName("timeframe");
		timeFrame.setRequired(false);
		timeFrame.setDescription("Pattern:" + Constants.TIME_FRAME_FORMAT);
		return timeFrame;
	}

	/**
	 * Create command line option for 'maxResultSetPaging'.
	 * This option is requiered
	 * @return the mandatory option maxResultSetPaging
	 * @TODO AR This could have a default value
	 */
	private Option getMaxResultSetPagingOption() {
		Option maxResultSetPaging = getGeneralOption(Constants.maxResultSetPaging);
		maxResultSetPaging.setArgName("boolean");
		maxResultSetPaging.setDescription("Mandatory");
		return maxResultSetPaging;
	}

	/**
	 * Create command line option for 'maxResultSet'.
	 * This option is requiered
	 * @return
	 * @TODO AR This could have a default value
	 */
	private Option getMaxResultSetOption() {
		Option maxResultSet = getGeneralOption(Constants.maxResultSet);
		maxResultSet.setArgName("integer");
		maxResultSet.setDescription("Recommended to use range between 100 and 1000. This is a restriction due to JVM memory limitations");
		return maxResultSet;
	}

	/**
	 * Create command line option for 'action'.
	 * This option is requiered
	 * @return
	 */
	private Option getActionOption() {
		Option action = getGeneralOption(Constants.action);
		action.setArgName(ArrayUtils.toString(Constants.actionOptions));
		action.setDescription("Mandatory");
		return action;
	}
	
	/**
	 * Create command line option for 'logFileName'.
	 * This option is requiered
	 * @return
	 */
	private Option getLogFileNameOption() {
		Option logFileName = getGeneralOption(Constants.logFileName);
		logFileName.setArgName("filename");
		logFileName.setDescription("Name of output log file. Default value is xxx");
		return logFileName;
	}

	/**
	 * Create command line option for 'logFilePath'.
	 * This option is requiered
	 * @return
	 */
	private Option getLogFilePathOption() {
		Option logFilePath = getGeneralOption(Constants.logFilePath);
		logFilePath.setArgName("path");
		logFilePath.setDescription("Path for directory where the output file will be located.");
		return logFilePath;
	}

	/**
	 * Create command line option for 'configFile'.
	 * This option is requiered
	 * @return
	 */
	private Option getConfigFileOption() {
		Option configFile = getGeneralOption(Constants.configFile);		
		configFile.setArgName("full path");
		configFile.setDescription("Mandatory. Full path to configuration file for system environment spesifications (hostname etc.).");
		return configFile;
	}
	
	/**
	 * Create command line option for 'help'
	 * @return
	 */
	private Option getHelpOption() {
		Option help = OptionBuilder.withLongOpt(Constants.help).create();
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
	private Option getGeneralOption(String optionName) {
		Option option = OptionBuilder.withLongOpt(optionName)
									 .withValueSeparator()
									 .hasArg()
									 .create();
		return option;
	}
}
