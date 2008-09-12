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
		options.addOption(actionOption);
		
		return options;
	}
}
