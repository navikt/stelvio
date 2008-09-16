package no.nav.bpchelper.cmdoptions;

import java.text.SimpleDateFormat;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class OptionsBuilder {
	public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd.MM.yyyy:hhmm");
	
	public Options getOptions() {
		Options options = new Options();
		
		Option helpOption = OptionBuilder.withLongOpt(OptionOpts.HELP).create();
		helpOption.setDescription("print this message");
		options.addOption(helpOption);
		
		Option actionOption = OptionBuilder.withLongOpt(OptionOpts.ACTION).hasArg().create();
		actionOption.setDescription("action to perform (mandatory)");
		StringBuffer argName = new StringBuffer();
		for (ActionOptionValues actionOptionValues : ActionOptionValues.values()) {
			if (argName.length() > 0) {
				argName.append("|");
			}
			argName.append(actionOptionValues.name());
		}
		actionOption.setArgName(argName.toString());
		options.addOption(actionOption);
		
		Option startedBeforeOption = OptionBuilder.withLongOpt(OptionOpts.STARTED_BEFORE).hasArg().create();
		startedBeforeOption.setDescription("Filter by started before");
		startedBeforeOption.setArgName("timeStamp [" + TIMESTAMP_FORMAT.toPattern() + "]");
		options.addOption(startedBeforeOption);
		
		Option startedAfterOption = OptionBuilder.withLongOpt(OptionOpts.STARTED_AFTER).hasArg().create();
		startedAfterOption.setDescription("Filter by started after");
		startedAfterOption.setArgName("timeStamp [" + TIMESTAMP_FORMAT.toPattern() + "]");
		options.addOption(startedAfterOption);
		
		Option processTemplateNameOption = OptionBuilder.withLongOpt(OptionOpts.PROCESS_TEMPLATE_NAME).hasArg().create();
		processTemplateNameOption.setDescription("Filter by process template name");
		processTemplateNameOption.setArgName("processTemplateName");
		options.addOption(processTemplateNameOption);
		
		Option activityNameOption = OptionBuilder.withLongOpt(OptionOpts.ACTIVITY_NAME).hasArg().create();
		activityNameOption.setDescription("Filter by activity name");
		activityNameOption.setArgName("activityName");
		options.addOption(activityNameOption);
		
		return options;
	}
}
