package no.nav.femhelper.actions;

import java.util.Properties;

import no.nav.appclient.util.Constants;


public class ActionFactory {
	
	public static AbstractAction getAction (String actionType, Properties properties) {

		AbstractAction action= null;
		
		if (Constants.ACTION_REPORT.equalsIgnoreCase(actionType)) {
			action = new ReportAction(properties);
		} else if (Constants.ACTION_DISCARD.equalsIgnoreCase(actionType)) {
			action = new DeleteAction(properties);
		} else if (Constants.ACTION_RESUBMIT.equalsIgnoreCase(actionType)) {
			action = new ResubmitAction(properties);
		} else if (Constants.ACTION_STATUS.equalsIgnoreCase(actionType)) {
			action = new StatusAction(properties);
		} 

		// Since the input allready is validated to be a valid option 
		// based on these constants
		return action;
	}
}
