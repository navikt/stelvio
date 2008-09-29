package no.nav.bpchelper.cmdoptions;

import java.text.SimpleDateFormat;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class OptionsBuilder {
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd.MM.yyyy:hhmm");

    public Options getOptions() {
	Options options = new Options();

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
	options.addOption(reportDirectoryOption);
	
	Option reportFilenameOption = OptionBuilder.withLongOpt(OptionOpts.REPORT_FILENAME).hasArg().create("rf");
	reportFilenameOption.setDescription("report filename");
	options.addOption(reportFilenameOption);

	Option startedBeforeOption = OptionBuilder.withLongOpt(OptionOpts.FILTER_STARTED_BEFORE).hasArg().create("Fsb");
	startedBeforeOption.setDescription("filter by started before");
	startedBeforeOption.setArgName("timeStamp [" + TIMESTAMP_FORMAT.toPattern() + "]");
	options.addOption(startedBeforeOption);

	Option startedAfterOption = OptionBuilder.withLongOpt(OptionOpts.FILTER_STARTED_AFTER).hasArg().create("Fsa");
	startedAfterOption.setDescription("filter by started after");
	startedAfterOption.setArgName("timeStamp [" + TIMESTAMP_FORMAT.toPattern() + "]");
	options.addOption(startedAfterOption);

	Option processTemplateNameOption = OptionBuilder.withLongOpt(OptionOpts.FILTER_PROCESS_TEMPLATE_NAME).hasArg().create("Fptn");
	processTemplateNameOption.setDescription("filter by process template name");
	processTemplateNameOption.setArgName("processTemplateName");
	options.addOption(processTemplateNameOption);

	Option activityNameOption = OptionBuilder.withLongOpt(OptionOpts.FILTER_ACTIVITY_NAME).hasArg().create("Fan");
	activityNameOption.setDescription("filter by activity name");
	activityNameOption.setArgName("activityName");
	options.addOption(activityNameOption);

	Option configFile = OptionBuilder.withLongOpt(OptionOpts.CONFIG_FILE).hasArg().create("cf");
	configFile.setDescription("config file with env info (mandatory)");
	configFile.setArgName("configFile");
	options.addOption(configFile);
	
	
	return options;
    }
}
