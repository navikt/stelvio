package no.nav.bpchelper.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import no.nav.bpchelper.cmdoptions.ActionOptionValues;
import no.nav.bpchelper.cmdoptions.OptionOpts;
import no.nav.bpchelper.utils.PropertyMapper;

import org.apache.commons.cli.CommandLine;

public class ActionFactory {
	private static final DateFormat REPORT_FILENAME_DATEFORMAT = new SimpleDateFormat("yyyyddMMHHmmssSSS");

	public static Action getAction(CommandLine commandLine) {
		// It is more or less okay to catch FileNotFound exceptions from
		// this method as we have give the user a log entry about
		// this and simultainisly abort the program before this
		// will occur.
		Properties connectProps;
		try {
			String configFilePath = commandLine.getOptionValue(OptionOpts.CONFIG_FILE);
			connectProps = new Properties();
			connectProps.load(new FileInputStream(configFilePath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		PropertyMapper mapper = new PropertyMapper();
		Properties properties = mapper.getMappedProperties(connectProps);
		
		String actionValue = commandLine.getOptionValue(OptionOpts.ACTION);
		AbstractAction action = ActionOptionValues.valueOf(actionValue).getAction(properties);

		action.setCriteria(CriteriaBuilder.build(commandLine));

		File reportFile;
		String reportFilename = commandLine.getOptionValue(OptionOpts.REPORT_FILENAME);
		if (reportFilename == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(action.getName());
			sb.append('_').append(REPORT_FILENAME_DATEFORMAT.format(System.currentTimeMillis()));
			sb.append(".csv");
			reportFilename = sb.toString();
		}
		String reportDirectory = commandLine.getOptionValue(OptionOpts.REPORT_DIR);
		if (reportDirectory != null) {
			reportFile = new File(reportDirectory, reportFilename);
		} else {
			reportFile = new File(reportFilename);
		}
		action.setReportFile(reportFile);
		return action;
	}
}
