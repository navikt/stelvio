package no.nav.femhelper.actions;

import java.util.Properties;

import no.nav.femhelper.common.Constants;


public class ActionFactory {
	
	public static AbstractAction getAction (String actionType, Properties properties) {

		if (Constants.actionOptions[0].equalsIgnoreCase(actionType)) {
			AbstractAction action = new ReportAction(properties);
			return action; 
		} else if (Constants.actionOptions[1].equalsIgnoreCase(actionType)) {
			AbstractAction action = new DeleteAction(properties);
			return action;
		} else if (Constants.actionOptions[2].equalsIgnoreCase(actionType)) {
			AbstractAction action = new ResubmitAction(properties);
			return action;
		} else if (Constants.actionOptions[3].equalsIgnoreCase(actionType)) {
			AbstractAction action = new StatusAction(properties);
			return action;
		}
		
		// This will never occur. The input will be validated before this method
		// TODO AR Evaluate if it's functionaly correct to just return a ReportAction by default
		return null;
	}
}
