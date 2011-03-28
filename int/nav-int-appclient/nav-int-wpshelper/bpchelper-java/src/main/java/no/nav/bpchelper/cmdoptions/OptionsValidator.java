package no.nav.bpchelper.cmdoptions;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;

public class OptionsValidator {
	public static Collection<String> validate(CommandLine commandLine) {
		Collection<String> validationErrors = new ArrayList<String>();

		// Would love to set action-option to required, but then help-option
		// does not work anymore.
		if (commandLine.hasOption(OptionOpts.ACTION)) {
			String actionValue = commandLine.getOptionValue(OptionOpts.ACTION);
			try {
				ActionOptionValues.valueOf(actionValue);
			} catch (IllegalArgumentException e) {
				validationErrors.add("Illegal argument for option:" + OptionOpts.ACTION + " <" + actionValue + ">");
			}
		} else {
			validationErrors.add("Missing required option:" + OptionOpts.ACTION);
		}

		// Would love to set configFile-option to required, but then help-option
		// does not work anymore.
		if (commandLine.hasOption(OptionOpts.CONFIG_FILE)) {
			String configFilePath = commandLine.getOptionValue(OptionOpts.CONFIG_FILE);
			File propertyFile = new File(configFilePath);
			if (!propertyFile.exists()) {
				validationErrors.add("Illegal argument for option:" + OptionOpts.CONFIG_FILE + " (" + configFilePath
						+ ") does not exist");
			}
		} else {
			validationErrors.add("Missing required option:" + OptionOpts.CONFIG_FILE);
		}

		if (commandLine.hasOption(OptionOpts.REPORT_DIR)) {
			String reportDirectoryPath = commandLine.getOptionValue(OptionOpts.REPORT_DIR);
			File reportDirectory = new File(reportDirectoryPath);
			if (!reportDirectory.exists()) {
				validationErrors.add("Illegal argument for option:" + OptionOpts.REPORT_DIR + " (" + reportDirectoryPath
						+ ") does not exist");
			}
			if (!reportDirectory.isDirectory()) {
				validationErrors.add("Illegal argument for option:" + OptionOpts.REPORT_DIR + " (" + reportDirectoryPath
						+ ") is not a directory");
			}
		}

		if (commandLine.hasOption(OptionOpts.REPORT_FILENAME)) {
			String reportFilenameString = commandLine.getOptionValue(OptionOpts.REPORT_FILENAME);
			File reportFile;
			if (commandLine.hasOption(OptionOpts.REPORT_DIR)) {
				reportFile = new File(commandLine.getOptionValue(OptionOpts.REPORT_DIR), reportFilenameString);
			} else {
				reportFile = new File(reportFilenameString);
			}
			if (reportFile.exists()) {
				validationErrors.add("Illegal argument for option:" + OptionOpts.REPORT_FILENAME + " ("
						+ reportFile.getAbsolutePath() + ") already exists");
			}
		}
		
		if (commandLine.hasOption(OptionOpts.INPUT_DIR)) {
			String inputDirectoryPath = commandLine.getOptionValue(OptionOpts.INPUT_DIR);
			File inputDirectory = new File(inputDirectoryPath);
			if (!inputDirectory.exists()) {
				validationErrors.add("Illegal argument for option:" + OptionOpts.INPUT_DIR + " (" + inputDirectoryPath
						+ ") does not exist");
			}
			if (!inputDirectory.isDirectory()) {
				validationErrors.add("Illegal argument for option:" + OptionOpts.INPUT_DIR + " (" + inputDirectoryPath
						+ ") is not a directory");
			}
		}
		
		if (commandLine.hasOption(OptionOpts.INPUT_FILENAME)) {
			String inputFilenameString = commandLine.getOptionValue(OptionOpts.INPUT_FILENAME);
			File inputFile;
			if (commandLine.hasOption(OptionOpts.INPUT_DIR)) {
				inputFile = new File(commandLine.getOptionValue(OptionOpts.INPUT_DIR), inputFilenameString);
			} else {
				inputFile = new File(inputFilenameString);
			}
			if (!inputFile.exists()) {
				validationErrors.add("Illegal argument for option:" + OptionOpts.INPUT_FILENAME + " ("
						+ inputFilenameString + ") does not exist");
			}
		}
		
		if (commandLine.hasOption(OptionOpts.FILTER_PROCESS_STARTED_TIME_FRAME)) {
			String startedTimeFrameFilterArg = commandLine.getOptionValue(OptionOpts.FILTER_PROCESS_STARTED_TIME_FRAME);
			StringTokenizer st = new StringTokenizer(startedTimeFrameFilterArg, "-");
			if (st.countTokens() != 2) {
				validationErrors.add("Illegal argument for option:" + OptionOpts.FILTER_PROCESS_STARTED_TIME_FRAME);
			} else {
				while (st.hasMoreTokens()) {
					try {
						OptionsBuilder.TIMESTAMP_FORMAT.parse(st.nextToken());
					} catch (ParseException e) {
						validationErrors.add("Illegal argument for option:" + OptionOpts.FILTER_PROCESS_STARTED_TIME_FRAME);
						break;
					}					
				}
			}
		}

		if (commandLine.hasOption(OptionOpts.FILTER_PROCESS_CUSTOM_PROPERTY)) {
			String[] customPropertiesFilterArgs = commandLine.getOptionValues(OptionOpts.FILTER_PROCESS_CUSTOM_PROPERTY);
			if (customPropertiesFilterArgs.length % 2 != 0) {
				validationErrors.add("Illegal argument for option:" + OptionOpts.FILTER_PROCESS_CUSTOM_PROPERTY);
			}
		}

		return validationErrors;
	}
}
