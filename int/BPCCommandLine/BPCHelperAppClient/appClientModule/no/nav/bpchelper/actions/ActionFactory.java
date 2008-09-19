package no.nav.bpchelper.actions;

import no.nav.bpchelper.cmdoptions.ActionOptionValues;
import no.nav.bpchelper.cmdoptions.OptionOpts;

import org.apache.commons.cli.CommandLine;

public class ActionFactory {
    public static Action getAction(CommandLine commandLine) {
	String actionValue = commandLine.getOptionValue(OptionOpts.ACTION);
	AbstractAction action = ActionOptionValues.valueOf(actionValue).getAction();
	action.setCriteria(CriteriaBuilder.build(commandLine));
	return action;
    }
}
