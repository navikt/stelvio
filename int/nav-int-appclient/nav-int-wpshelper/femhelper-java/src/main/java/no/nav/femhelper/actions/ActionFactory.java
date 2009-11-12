package no.nav.femhelper.actions;

import java.util.Properties;

import no.nav.femhelper.common.Constants;


public class ActionFactory {
	
	public static AbstractAction getAction (String actionType, Properties properties) {

		if (Constants.ACTION_REPORT.equalsIgnoreCase(actionType)) {
			return new ReportAction(properties);
		} else if (Constants.ACTION_DISCARD.equalsIgnoreCase(actionType)) {
			return new DeleteAction(properties);
		} else if (Constants.ACTION_RESUBMIT.equalsIgnoreCase(actionType)) {
			return new ResubmitAction(properties);
		} else if (Constants.ACTION_STATUS.equalsIgnoreCase(actionType)) {
			return new StatusAction(properties);
		} else {
			throw new RuntimeException("ActionType '" + actionType + "' is not supported");
		}
	}
}
