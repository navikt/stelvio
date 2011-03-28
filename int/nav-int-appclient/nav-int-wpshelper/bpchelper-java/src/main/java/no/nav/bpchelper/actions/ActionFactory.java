package no.nav.bpchelper.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import no.nav.appclient.util.PropertyMapper;
import no.nav.bpchelper.cmdoptions.ActionOptionValues;
import no.nav.bpchelper.cmdoptions.OptionOpts;

import org.apache.commons.cli.CommandLine;

public class ActionFactory {
	private static final DateFormat REPORT_FILENAME_DATEFORMAT = new SimpleDateFormat("yyyyddMMHHmmssSSS");

	public static Action getAction(CommandLine commandLine) {
		String actionValue = commandLine.getOptionValue(OptionOpts.ACTION).toUpperCase();
		AbstractAction action = ActionOptionValues.valueOf(actionValue).getAction();

		String configFilePath = commandLine.getOptionValue(OptionOpts.CONFIG_FILE);
		Properties properties = getProperties(configFilePath);
		action.setProperties(properties);

		action.setCriteria(CriteriaBuilder.build(commandLine));

		String actionName = action.getName();
		String reportFilename = commandLine.getOptionValue(OptionOpts.REPORT_FILENAME);
		String reportDirectory = commandLine.getOptionValue(OptionOpts.REPORT_DIR);
		File reportFile = getReportFile(actionName, reportFilename, reportDirectory);
		action.setReportFile(reportFile);
		
		String inputFilename = commandLine.getOptionValue(OptionOpts.INPUT_FILENAME);
		String inputDirectory = commandLine.getOptionValue(OptionOpts.INPUT_DIR);
		File inputFile = new File(inputDirectory, inputFilename);
		action.setInputFile(inputFile);
		
		if (commandLine.hasOption(OptionOpts.NO_STOP)) {
			action.setInteractiveMode(false);
		}
		
		return action;
	}
	
	private static Properties getProperties(String configFilePath) {
		// It is more or less okay to catch FileNotFound exceptions from
		// this method as we have give the user a log entry about
		// this and simultaneously abort the program before this
		// will occur.
		Properties connectProps;
		try {
			connectProps = new Properties();
			connectProps.load(new FileInputStream(configFilePath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		PropertyMapper mapper = new PropertyMapper();
		Properties properties = mapper.getMappedProperties(connectProps);
		return properties;
	}

	private static File getReportFile(String actionName, String reportFilename, String reportDirectory) {
		File reportFile;
		if (reportFilename == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(actionName);
			sb.append('_').append(REPORT_FILENAME_DATEFORMAT.format(System.currentTimeMillis()));
			sb.append(".csv");
			reportFilename = sb.toString();
		}
		if (reportDirectory != null) {
			reportFile = new File(reportDirectory, reportFilename);
		} else {
			reportFile = new File(reportFilename);
		}
		return reportFile;
	}
}
