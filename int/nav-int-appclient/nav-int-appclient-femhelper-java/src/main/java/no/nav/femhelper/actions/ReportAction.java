package no.nav.femhelper.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import no.nav.appclient.util.Constants;
import no.nav.femhelper.common.Event;
import no.nav.femhelper.common.Queries;

import org.apache.commons.cli.CommandLine;

import com.ibm.wbiserver.manualrecovery.FailedEventWithParameters;
import com.ibm.websphere.management.exception.ConnectorException;

public class ReportAction extends AbstractAction {

	public ReportAction(Properties properties) {
		super(properties);
	}

	/**
	 * Logger instance
	 */
	private Logger logger = Logger.getLogger(ReportAction.class.getName());
	
	@Override
	Object processEvents(String path, String filename, Map arguments,
			boolean paging, long totalevents, int maxresultset, CommandLine cl)
			throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		
		logger.log(Level.FINE, Constants.METHOD_ENTER + "processEvents");
		
		logger.log(Level.FINE, "Write CSV header part.");
		fileWriter.writeHeader();
	
		ArrayList <Event> events = new ArrayList<Event>();
		events = collectEvents(arguments, paging, totalevents, maxresultset);
		
		if (!cl.hasOption(Constants.noStop)) {
			String q = "Do you want to continue and write " + totalevents + " events?";
			boolean result = askYesNo(q);
			if (!result) {
				return null;
			}
		}
		
		logFileWriter.log("Starting to report " + events.size() + " events");
		logger.log(Level.INFO,"Reporting of #" + events.size() + " events in progress...!");
		for (int i = 0; i < events.size(); i++) {
			logger.log(Level.FINE,"Reporting events (" + (i+1) + " of " + events.size() + "). Please wait!");
			Event event = events.get(i);
			
			// Write the report
			String eventWithParameter = Queries.QUERY_EVENT_WITH_PARAMETERS;
			Object[] BOparams = new Object[] { new String((String) event.getMessageID()) };
			String[] BOsignature = new String[] { "java.lang.String" };
			FailedEventWithParameters failedEventWithParameters = (FailedEventWithParameters) adminClient
					.invoke(faildEventManager, eventWithParameter, BOparams, BOsignature);
			fileWriter.writeCSVEvent(failedEventWithParameters, event
					.getCorrelationID(), adminClient, Constants.DEFAULT_DATE_FORMAT_MILLS);
		}
		
		logFileWriter.log("Reported " + events.size() + " events");
		logger.log(Level.INFO,"Reporting of #" + events.size() + " events...done!");
		logger.log(Level.FINE, Constants.METHOD_EXIT + "processEvents");
		
		return events;
	}

}
