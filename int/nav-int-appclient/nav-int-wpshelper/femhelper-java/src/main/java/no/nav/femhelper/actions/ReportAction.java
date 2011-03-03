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
		logger.log(Level.FINE, "Write CSV header part.");
		fileWriter.writeHeader();

		Collection<Event> events = new ArrayList<Event>();
		events = collectEvents(arguments, paging, totalevents, maxresultset);

		if (!events.isEmpty()) {
			if (!cl.hasOption(CommandOptions.noStop)) {
				String q = "Do you want to continue and write " + events.size() + " events (y/n)?";
				boolean result = askYesNo(q);
				if (!result) {
					return null;
				}
			}

			logFileWriter.log("Starting to report " + events.size() + " events");
			logger.log(Level.INFO, "Reporting of #" + events.size() + " events in progress...!");
			int count = 0;
			int countSuccessful = 0;
			for (Event event : events) {
				count++;
				if (count % maxresultset == 0) {
					logger.log(Level.INFO, "Reported " + (count) + " of " + events.size() + " events");
				}

				// Write the report

				// Using FailedEventImpl as a part of upgrading to WPS7 API
				FailedEventImpl failedEvent = new FailedEventImpl();
				failedEvent.setMsgId(event.getMessageID());
				String query = "";
				Object[] BOparams = new Object[] { failedEvent };
				String[] BOsignature = new String[] { "com.ibm.wbiserver.manualrecovery.FailedEvent" };

				if ((Constants.EVENT_TYPE_SCA).equals(event.getType())) {
					query = Queries.QUERY_EVENT_DETAIL_FOR_SCA;
				} else if ((Constants.EVENT_TYPE_BPC).equals(event.getType())) {
					query = Queries.QUERY_EVENT_DETAIL_FOR_BPC;
					failedEvent.setType(event.getType());
					failedEvent.setDeploymentTarget(event.getDeploymentTarget());
				} else if ((Constants.EVENT_TYPE_JMS).equals(event.getType())) {
					query = Queries.QUERY_EVENT_DETAIL_FOR_JMS;
					failedEvent.setDestinationModuleName(event.getDestinationModuleName());
					failedEvent.setType(event.getType());
				} else if ((Constants.EVENT_TYPE_MQ).equals(event.getType())) {
					query = Queries.QUERY_EVENT_DETAIL_FOR_MQ;
				}

				try {
					FailedEvent eventDetail = (FailedEvent) adminClient
							.invoke(failedEventManager, query, BOparams, BOsignature);
					fileWriter.writeCSVEvent(eventDetail, event, adminClient);
				} catch (MBeanException e) {
					if (e.getCause() != null && e.getCause() instanceof FailedEventDataException) {
						FailedEventDataException cause = (FailedEventDataException) e.getCause();
						logger.log(Level.WARNING, "Unable to fetch event with messageId '" + event.getMessageID()
								+ "', probably because it does not exist.", cause);
					} else {
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
		return events;
	}
}
