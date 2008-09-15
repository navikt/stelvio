package no.nav.bpchelper.cmdoptions;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionsBuilder {
	public Options getOptions() {
		Options options = new Options();
		
		Option helpOption = new Option(OptionOpts.HELP, "print this message");
		helpOption.setLongOpt("help");
		options.addOption(helpOption);
		
		Option actionOption = new Option(OptionOpts.ACTION, true,"action to perform (mandatory)");
		actionOption.setLongOpt("action");
		StringBuffer argName = new StringBuffer();
		for (ActionOptionValues actionOptionValues : ActionOptionValues.values()) {
			if (argName.length() > 0) {
				argName.append("|");
			}
			argName.append(actionOptionValues.name());
		}
		actionOption.setArgName(argName.toString());
		options.addOption(actionOption);
		
		Option filterOption = new Option(OptionOpts.FILTER, true, "filter to apply when searching for processes");
		filterOption.setLongOpt("filter");
		options.addOption(filterOption);
		
		return options;
	}
}
