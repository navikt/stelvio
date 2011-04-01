package no.nav.bpchelper.cmdoptions;

import java.text.SimpleDateFormat;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class OptionsBuilder {
	public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd.MM.yyyy:hhmm");

	@SuppressWarnings("static-access")
	public Options getOptions() {
		Options options = new Options();

		Option configFile = OptionBuilder.withLongOpt(OptionOpts.CONFIG_FILE).hasArg().create("cf");
		configFile.setDescription("config file with env info (mandatory)");
		configFile.setArgName("configFile");
		options.addOption(configFile);

		Option helpOption = OptionBuilder.withLongOpt(OptionOpts.HELP).create("h");
		helpOption.setDescription("print usage information");
		options.addOption(helpOption);

		Option actionOption = OptionBuilder.withLongOpt(OptionOpts.ACTION).hasArg().create("a");
		actionOption.setDescription("action to perform (mandatory)");
		StringBuilder argName = new StringBuilder();
		for (ActionOptionValues actionOptionValues : ActionOptionValues.values()) {
			if (argName.length() > 0) {
				argName.append("|");
			}
			argName.append(actionOptionValues.name());
		}
		actionOption.setArgName(argName.toString());
		options.addOption(actionOption);

		Option reportDirectoryOption = OptionBuilder.withLongOpt(OptionOpts.REPORT_DIR).hasArg().create("rd");
		reportDirectoryOption.setDescription("directory to put report");
		reportDirectoryOption.setArgName("path");
		options.addOption(reportDirectoryOption);

		Option reportFilenameOption = OptionBuilder.withLongOpt(OptionOpts.REPORT_FILENAME).hasArg().create("rf");
		reportFilenameOption.setDescription("report filename");
		reportFilenameOption.setArgName("filename");
		options.addOption(reportFilenameOption);
		
		Option inputFilenameOption = OptionBuilder.withLongOpt(OptionOpts.INPUT_FILENAME).hasArg().create("if");
		inputFilenameOption.setDescription("input filename (mandatory with action SAMORDNING");
		inputFilenameOption.setArgName("input filename");
		options.addOption(inputFilenameOption);
		
		Option noStopOption = OptionBuilder.withLongOpt(OptionOpts.NO_STOP).create("ns");
		noStopOption.setDescription("Runs the action without prompting after collecting process instance ids");
		options.addOption(noStopOption);

		Option processStartedTimeFrameOption = OptionBuilder.withLongOpt(OptionOpts.FILTER_PROCESS_STARTED_TIME_FRAME).hasArg()
				.create("tf");
		processStartedTimeFrameOption.setDescription("filter by process started time frame");
		String timestampPattern = TIMESTAMP_FORMAT.toPattern();
		processStartedTimeFrameOption.setArgName("time frame [" + timestampPattern + "-" + timestampPattern + "]");
		options.addOption(processStartedTimeFrameOption);

		Option processTemplateNameOption = OptionBuilder.withLongOpt(OptionOpts.FILTER_PROCESS_TEMPLATE_NAME).hasArg().create(
				"ptn");
		processTemplateNameOption.setDescription("filter by process template name");
		processTemplateNameOption.setArgName("processTemplateName");
		options.addOption(processTemplateNameOption);

		Option processCustomPropertyOption = OptionBuilder.withLongOpt(OptionOpts.FILTER_PROCESS_CUSTOM_PROPERTY).hasArgs()
				.withValueSeparator().create("pcp");
		processCustomPropertyOption.setDescription("filter by process custom properties");
		processCustomPropertyOption.setArgName("custom property name=custom property value");
		options.addOption(processCustomPropertyOption);

		Option activityNameOption = OptionBuilder.withLongOpt(OptionOpts.FILTER_ACTIVITY_NAME).hasArg().create("an");
		activityNameOption.setDescription("filter by activity name");
		activityNameOption.setArgName("activityName");
		options.addOption(activityNameOption);

		options.addOption(new Option("as", "allStates", false, "filter matching all process and activity states (default: failed/terminated/stopped)"));

		return options;
	}
}
