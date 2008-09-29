import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import no.nav.bpchelper.actions.Action;
import no.nav.bpchelper.actions.ActionFactory;
import no.nav.bpchelper.cmdoptions.ActionOptionValues;
import no.nav.bpchelper.cmdoptions.OptionOpts;
import no.nav.bpchelper.cmdoptions.OptionsBuilder;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Main {
	private static final int WIDTH = 200;

	private static final String CMD_LINE_SYNTAX = "launchClient <BPCHelper application> [args]";

	private static final Options OPTIONS = new OptionsBuilder().getOptions();

	public static void main(String[] args) {
		Collection<String> argsCollection = new ArrayList<String>(Arrays.asList(args));
		for (Iterator<String> it = argsCollection.iterator(); it.hasNext();) {
			if (it.next().startsWith("-CC")) {
				it.remove();
			}
		}

		int returnCode = new Main().run(argsCollection.toArray(new String[argsCollection.size()]));
		System.exit(returnCode);
	}

	private int run(String[] args) {
		CommandLine commandLine;
		try {
			commandLine = new PosixParser().parse(OPTIONS, args);
		} catch (ParseException e) {
			printHelp(e.getMessage());
			return ReturnCodes.ERROR;
		}

		if (commandLine.hasOption(OptionOpts.HELP)) {
			printHelp(null);
			return ReturnCodes.OK;
		}

		// Would love to set action-option to required, but then help-option
		// does not work anymore.
		if (commandLine.hasOption(OptionOpts.ACTION)) {
			String actionValue = commandLine.getOptionValue(OptionOpts.ACTION);
			try {
				ActionOptionValues.valueOf(actionValue);
			} catch (IllegalArgumentException e) {
				printHelp("Illegal argument for option:" + OptionOpts.ACTION + " <" + actionValue + ">");
				return ReturnCodes.ERROR;
			}
		} else {
			printHelp("Missing required option:" + OptionOpts.ACTION);
			return ReturnCodes.ERROR;
		}

		// Would love to set configFile-option to required, but then help-option
		// does not work anymore.
		if (commandLine.hasOption(OptionOpts.CONFIG_FILE)) {
			String configFilePath = commandLine.getOptionValue(OptionOpts.CONFIG_FILE);
			File propertyFile = new File(configFilePath);
			if (!propertyFile.exists()) {
				printHelp("Illegal argument for option:" + OptionOpts.CONFIG_FILE + " (" + configFilePath + ") does not exist");
				return ReturnCodes.ERROR;
			}
		} else {
			printHelp("Missing required option:" + OptionOpts.CONFIG_FILE);
			return ReturnCodes.ERROR;
		}

		if (commandLine.hasOption(OptionOpts.REPORT_DIR)) {
			String reportDirectoryPath = commandLine.getOptionValue(OptionOpts.REPORT_DIR);
			File reportDirectory = new File(reportDirectoryPath);
			if (!reportDirectory.exists()) {
				printHelp("Illegal argument for option:" + OptionOpts.REPORT_DIR + " (" + reportDirectoryPath
						+ ") does not exist");
				return ReturnCodes.ERROR;
			}
			if (!reportDirectory.isDirectory()) {
				printHelp("Illegal argument for option:" + OptionOpts.REPORT_DIR + " (" + reportDirectoryPath
						+ ") is not a directory");
				return ReturnCodes.ERROR;
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
				printHelp("Illegal argument for option:" + OptionOpts.REPORT_FILENAME + " (" + reportFile.getAbsolutePath()
						+ ") already exists");
				return ReturnCodes.ERROR;
			}
		}

		Action action = ActionFactory.getAction(commandLine);
		action.process();

		return ReturnCodes.OK;
	}

	private void printHelp(String footer) {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(WIDTH, CMD_LINE_SYNTAX, null, OPTIONS, footer);
	}
}