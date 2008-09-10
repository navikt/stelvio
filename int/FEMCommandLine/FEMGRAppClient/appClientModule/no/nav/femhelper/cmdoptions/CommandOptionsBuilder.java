package no.nav.femhelper.cmdoptions;

import no.nav.femhelper.common.Constants;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.ArrayUtils;

public class CommandOptionsBuilder {
	
	// Default constructor
	public CommandOptionsBuilder() {
		
	}
	
	public Options getOptions() {
		
		// Create command line option for 'help'
		Option help = OptionBuilder.withLongOpt(Constants.help).create();
		help.setDescription("This help index");
		
		// Create command line option for 'configFile'
		// This option is requiered
		Option configFile = OptionBuilder.withLongOpt(Constants.configFile).withValueSeparator().hasArg().create();
		configFile.setArgName("full path");
		configFile.setDescription("Mandatory. Full path to configuration file for system environment spesifications (hostname etc.).");
		
		// Create command line option for 'logFilePath'
		// This option is requiered
		Option logFilePath = OptionBuilder.withLongOpt(Constants.logFilePath).withValueSeparator().hasArg().create();
		logFilePath.setArgName("path");
		logFilePath.setDescription("Path for directory where the output file will be located.");
		
		// Create command line option for 'logFileName'
		// This option is requiered
		Option logFileName = OptionBuilder.withLongOpt(Constants.logFileName).withValueSeparator().hasArg().create();
		logFileName.setArgName("filename");
		logFileName.setDescription("Name of output log file. Default value is xxx");
		
		// Create command line option for 'messageType'
		// This option is requiered
		Option messageType = OptionBuilder.withLongOpt(Constants.messageType).withValueSeparator().hasArg().create();
		messageType.setArgName(ArrayUtils.toString(Constants.messageTypeOptions));
		messageType.setDescription("Mandatory");
		
		// Create command line option for 'action'
		// This option is requiered
		Option action = OptionBuilder.withLongOpt(Constants.action).withValueSeparator().hasArg().create();
		action.setArgName(ArrayUtils.toString(Constants.actionOptions));
		action.setDescription("Mandatory");
		
//		 Create command line option for 'maxResultSet'
		// This option is requiered
		Option maxResultSet = OptionBuilder.withLongOpt(Constants.maxResultSet).withValueSeparator().hasArg().create();
		maxResultSet.setArgName("integer");
		maxResultSet.setDescription("Recommended to use range between 100 and 1000. This is a restriction due to JVM memory limitations");
		
		// Create command line option for 'maxResultSetPaging'
		// This option is requiered
		Option maxResultSetPaging = OptionBuilder.withLongOpt(Constants.maxResultSetPaging).withValueSeparator().hasArg().create();
		maxResultSetPaging.setArgName("boolean");
		maxResultSetPaging.setDescription("Mandatory");
		
		// Create command line option for 'timeFrame'
		Option timeFrame = OptionBuilder.withLongOpt(Constants.timeFrame).withValueSeparator().hasArgs().create();
		timeFrame.setArgName("timeframe");
		timeFrame.setRequired(false);
		timeFrame.setDescription("Pattern:" + Constants.TIME_FRAME_FORMAT);
		
		Options options = new Options();
		options.addOption(help);
		options.addOption(configFile);
		options.addOption(logFilePath);
		options.addOption(logFileName);
		options.addOption(messageType);
		options.addOption(action);
		options.addOption(maxResultSet);
		options.addOption(maxResultSetPaging);
		options.addOption(timeFrame);
		return options;
	}
	
}
