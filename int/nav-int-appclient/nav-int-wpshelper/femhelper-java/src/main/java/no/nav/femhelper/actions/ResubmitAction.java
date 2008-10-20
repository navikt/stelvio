package no.nav.femhelper.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import no.nav.appclient.util.Constants;
import no.nav.femhelper.common.Event;
import no.nav.femhelper.common.EventStatus;
import no.nav.femhelper.common.Queries;

import org.apache.commons.cli.CommandLine;

import com.ibm.wbiserver.manualrecovery.FailedEventExceptionReport;
import com.ibm.wbiserver.manualrecovery.exceptions.ResubmissionFailedException;
import com.ibm.websphere.management.exception.ConnectorException;

public class ResubmitAction extends AbstractAction {
	private Set<Event> reportedEvents = new LinkedHashSet<Event>();

	public ResubmitAction(Properties properties) {
		super(properties);
	}

	@Override
	Object processEvents(String path, String filename, Map<String, String> arguments, boolean paging, long totalevents,
			int maxresultset, CommandLine cl) throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		logger.log(Level.FINE, Constants.METHOD_ENTER + "processEvents");

		logger.log(Level.FINE, "Write header part.");
		fileWriter.writeShortHeader();

		logFileWriter.log("Starting to collect events");
		ArrayList<Event> events = collectEvents(arguments, paging, totalevents, maxresultset);
		logFileWriter.log("Collected " + events.size() + " events");

		if (!events.isEmpty()) {
			if (!cl.hasOption(Constants.noStop)) {
				String q = "Do you want to continue and resubmit " + events.size() + " events?";
				boolean result = askYesNo(q);
				if (!result) {
					return null;
				}
			}

			logger.log(Level.INFO, "Resubmiting #" + events.size() + " events...please wait!");
			logFileWriter.log("Staring to resubmit " + events.size() + " events");
			int j = 1;
			ArrayList<Event> chunk = new ArrayList<Event>();
			for (int i = 0; i < events.size(); i++) {
				Event event = events.get(i);
				event.setEventStatus(EventStatus.MARKED);
				chunk.add(event);
				reportedEvents.add(event);

				// for each result set
				if (j == Constants.MAX_DELETE) {
					logger.log(Level.INFO, "Resubmited result set of events from #" + ((i + 1) - Constants.MAX_DELETE)
							+ " to #" + (i + 1));
					resubmitEvents(chunk);
					j = 1;
					chunk.clear();
				}
				// the last chunk
				else if ((events.size() - i) < Constants.MAX_DELETE && events.size() == (i + 1)) {

					logger.log(Level.INFO, "Resubmit final result set of #" + chunk.size() + " events");
					resubmitEvents(chunk);
					j = 1;
					chunk.clear();
				}
				j++;
			} // event for

			logger.log(Level.INFO, "Resubmit of #" + events.size() + " events...done!");
			logger.log(Level.INFO, "Reporting status of events...please wait!");
			logFileWriter.log("Completed resubmit of " + events.size() + " events");
			for (Event event : events) {
				fileWriter.writeShortEvent(event);
			}
		} else {
			logger.log(Level.WARNING, "No events found to resubmit!");
		}

		logger.log(Level.FINE, Constants.METHOD_EXIT + "processEvents");
		return null;
	}

	private void resubmitEvents(List<Event> events) throws InstanceNotFoundException, ReflectionException, ConnectorException,
			MBeanException {
		try {
			// Map events to correct String array
			String opResubmit = Queries.QUERY_RESUBMIT_FAILED_EVENTS;
			String[] sig = new String[] { "[Ljava.lang.String;" };
			String[] ids = new String[events.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = events.get(i).getMessageID();
			}
			Object[] para = new Object[] { ids };

			// Resubmit events in this chunk
			adminClient.invoke(faildEventManager, opResubmit, para, sig);

			// Update the reported events with event status deleted
			for (Event event : events) {
				event.setEventStatus(EventStatus.RESUBMITED);
			}

		} catch (MBeanException e) {
			// REPORT not deleted events
			if (e.getTargetException() instanceof ResubmissionFailedException) {
				FailedEventExceptionReport[] re = ((ResubmissionFailedException) e.getTargetException())
						.getFailedEventExceptionReports();
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_MILLS);

				// Update with failure status and set failureinformation to the
				// failed events
				for (FailedEventExceptionReport report : re) {
					if (reportedEvents.contains(report.getMsgId())) {
						for (Event event : reportedEvents) {
							if (event.getMessageID().equals(report.getMsgId())) {
								event.setEventStatus(EventStatus.FAILED);
								event.setEventFailureDate(sdf.format(report.getExceptionTime()));
								event.setEventFailureMessage(report.getExceptionDetail());
							}
						}
					}
				}

				// Update the reported events with event status deleted
				for (Event event : events) {
					if (!event.getEventStatus().equals(EventStatus.FAILED)) {
						event.setEventStatus(EventStatus.RESUBMITED);
					}
				}
			} else {
				throw e;
			}
		}
	}
}
