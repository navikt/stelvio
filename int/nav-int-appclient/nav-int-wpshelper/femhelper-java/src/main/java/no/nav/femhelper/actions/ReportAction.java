package no.nav.femhelper.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import no.nav.femhelper.cmdoptions.CommandOptions;
import no.nav.femhelper.common.Event;
import no.nav.femhelper.common.Queries;
import no.nav.femhelper.common.Constants;

import org.apache.commons.cli.CommandLine;

import com.ibm.wbiserver.manualrecovery.FailedEvent;
import com.ibm.wbiserver.manualrecovery.FailedEventImpl;
import com.ibm.wbiserver.manualrecovery.exceptions.FailedEventDataException;
import com.ibm.websphere.management.exception.ConnectorException;

public class ReportAction extends AbstractAction {
	public ReportAction(Properties properties) {
		super(properties);
	}

	@Override
	Object processEvents(String path, String filename, Map<String, String> arguments, boolean paging, long totalevents,
			int maxresultset, CommandLine cl) throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		logger.log(Level.FINE, "ENTERING processEvents - Write CSV header part.");
		fileWriter.writeHeader();

		Collection<Event> events = new ArrayList<Event>();
		logger.log(Level.FINE, "COLLECTING events");
		events = collectEvents(arguments, paging, totalevents, maxresultset);

		if (!events.isEmpty()) {
			logger.log(Level.FINE, "Events found");
			if (!cl.hasOption(CommandOptions.noStop)) {
				String q = "Do you want to continue and write " + events.size() + " events (y/n)?";
				boolean result = askYesNo(q);
				if (!result) {
					logger.log(Level.FINE, "EXIT processEvents - User didnt want to continue");
					return null;
				}
			}

			logFileWriter.log("Starting to report " + events.size() + " events");
			logger.log(Level.INFO, "Reporting of #" + events.size() + " events in progress...!");
			int count = 0;
			int countSuccessful = 0;
			logger.log(Level.FINE, "LOOPING EVENTS:");
			for (Event event : events) {
				count++;
				if (count % maxresultset == 0) {
					logger.log(Level.INFO, "Reported " + (count) + " of " + events.size() + " events");
				}

				// Write the report

				// Using FailedEventImpl as a part of upgrading to WPS7 API
				logger.log(Level.FINE, "Creating an instance of FailedEventImpl");
				FailedEventImpl failedEvent = new FailedEventImpl();
				logger.log(Level.FINE, "Sets messageId");
				failedEvent.setMsgId(event.getMessageID());
				
				logger.log(Level.FINE, "Building query");
				String query = "";
				Object[] BOparams = new Object[] { failedEvent };
				String[] BOsignature = new String[] { "com.ibm.wbiserver.manualrecovery.FailedEvent" };

				if ((Constants.EVENT_TYPE_SCA).equals(event.getType())) {
					logger.log(Level.FINE, "Event type of SCA");
					query = Queries.QUERY_EVENT_DETAIL_FOR_SCA;
				} else if ((Constants.EVENT_TYPE_BPC).equals(event.getType())) {
					logger.log(Level.FINE, "Event type of BPC");
					query = Queries.QUERY_EVENT_DETAIL_FOR_BPC;
					failedEvent.setType(event.getType());
					failedEvent.setDeploymentTarget(event.getDeploymentTarget());
				} else if ((Constants.EVENT_TYPE_JMS).equals(event.getType())) {
					logger.log(Level.FINE, "Event type of JMS");
					query = Queries.QUERY_EVENT_DETAIL_FOR_JMS;
					failedEvent.setDestinationModuleName(event.getDestinationModuleName());
					failedEvent.setType(event.getType());
				}

				try {
					// writing an information message in cases of MQ events. 
					// MQ events are currently not used, or supported by FEM Helper
					if ((Constants.EVENT_TYPE_MQ).equals(event.getType())) {
						logger.log(Level.FINE, "Event type of MQ - writeEventLineWithoutDetails");
						fileWriter.writeEventLineWithoutDetails(Constants.MQ_EVENT_INFORMATION_MESSAGE);
					}
					
					logger.log(Level.FINE, "GETTING EVENT DETAILS - adminClient.invoke");
					FailedEvent eventDetail = (FailedEvent) adminClient
							.invoke(failedEventManager, query, BOparams, BOsignature);
					logger.log(Level.FINE, "WRITING EVENT DETAILS");
					fileWriter.writeCSVEvent(eventDetail, event, adminClient);
				} catch (MBeanException e) {
					if (e.getCause() != null && e.getCause() instanceof FailedEventDataException) {
						FailedEventDataException cause = (FailedEventDataException) e.getCause();
						logger.log(Level.WARNING, "Unable to fetch event with messageId '" + event.getMessageID()
								+ "', probably because it does not exist.", cause);
					} else {
						logger.log(Level.FINE, "GOT MBeanException: " + e.getMessage());
						logger.log(Level.FINE, "EXIT processEvents through rethrow MBeanException");
						throw e;
					}
				}

				countSuccessful++;

			}

			logFileWriter.log("Reported " + countSuccessful + " events");
			logger.log(Level.INFO, "Reporting of #" + countSuccessful + " events...done!");
		} else {
			logger.log(Level.WARNING, "No events found to report!");
		}
		logger.log(Level.FINE, "EXIT processEvents");
		return events;
	}
}
