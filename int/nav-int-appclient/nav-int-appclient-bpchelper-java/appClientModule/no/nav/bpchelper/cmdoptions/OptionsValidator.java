package no.nav.bpchelper.cmdoptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

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

		return validationErrors;
	}
}
