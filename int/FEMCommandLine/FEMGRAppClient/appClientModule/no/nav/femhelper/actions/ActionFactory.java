package no.nav.femhelper.actions;

import java.util.Properties;

import no.nav.femhelper.common.Constants;


public class ActionFactory {
	
	public static AbstractAction getAction (String actionType, Properties properties) {

		AbstractAction action= null;
		
		if (Constants.actionOptions[0].equalsIgnoreCase(actionType)) {
			action = new ReportAction(properties);
		} else if (Constants.actionOptions[1].equalsIgnoreCase(actionType)) {
			action = new DeleteAction(properties);
		} else if (Constants.actionOptions[2].equalsIgnoreCase(actionType)) {
			action = new ResubmitAction(properties);
		} else if (Constants.actionOptions[3].equalsIgnoreCase(actionType)) {
			action = new StatusAction(properties);
		} else if (Constants.actionOptions[4].equalsIgnoreCase(actionType)) {
			action = new TimeFrameAction(properties);
		}

		// Since the input allready is validated to be a valid option 
		// based on these constants
		return action;
	}
}
