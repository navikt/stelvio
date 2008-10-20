package no.nav.femhelper.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import no.nav.appclient.adapter.ServiceException;
import no.nav.appclient.util.Constants;
import no.nav.femhelper.common.Event;
import no.nav.femhelper.common.EventStatus;
import no.nav.femhelper.common.ProcessStatus;
import no.nav.femhelper.common.Queries;

import org.apache.commons.cli.CommandLine;

import com.ibm.wbiserver.manualrecovery.FailedEventExceptionReport;
import com.ibm.wbiserver.manualrecovery.exceptions.DiscardFailedException;
import com.ibm.websphere.management.exception.ConnectorException;

public class DeleteAction extends AbstractAction {
	private Set<Event> reportedEvents = new LinkedHashSet<Event>();

	public DeleteAction(Properties properties) {
		super(properties);
	}

	@Override
	Object processEvents(String path, String filename, Map<String, String> arguments, boolean paging, long totalevents,
			int maxresultset, CommandLine cl) throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {

		logger.log(Level.FINE, Constants.METHOD_ENTER + "processEvents");
		logger.log(Level.FINE, "Write discard header part.");

		fileWriter.writeShortHeader();

		// Collect events to stop processes and delete events
		ArrayList<Event> events = collectEvents(arguments, paging, totalevents, maxresultset);

		if (!events.isEmpty()) {
			// Check if the commandline has a --noStop option
			if (!cl.hasOption(Constants.noStop)) {
				String q = "Do you want to continue and delete " + events.size() + " events?";
				boolean result = askYesNo(q);
				if (!result) {
					return null;
				}
			}
			
			logger.log(Level.INFO, "Discarding #" + events.size() + " events...please wait!");
			int j = 1;
			ArrayList<Event> deleteChunk = new ArrayList<Event>();

			for (int i = 0; i < events.size(); i++) {
				Event event = events.get(i);
				deleteChunk.add(event);
				event.setEventStatus(EventStatus.MARKED);
				reportedEvents.add(event);

				// for each result set
				if (j == Constants.MAX_DELETE) {
					logger.log(Level.INFO, "Discard result set of events fra #" + ((i + 1) - Constants.MAX_DELETE) + " to #"
							+ (i + 1));
					deleteEvents(deleteChunk);
					// reset chunk iterator
					j = 1;
					deleteChunk.clear();
				}

				// The last chunk
				else if ((events.size() - i) < Constants.MAX_DELETE && events.size() == (i + 1)) {
					logger.log(Level.INFO, "Delete final result set of #" + deleteChunk.size() + " events");
					deleteEvents(deleteChunk);

					// reset chunk iterator
					j = 1;
					deleteChunk.clear();
				}
				j++;
			} // event for

			// Update the status map after all deleting are completed
			logger.log(Level.INFO, "Discarding of #" + events.size() + " events...done!");
			logger.log(Level.INFO, "Reporting status of discard events...please wait!");
			Iterator<Event> it = reportedEvents.iterator();
			while (it.hasNext()) {
				fileWriter.writeShortEvent(it.next());
			}
		} else {
			logger.log(Level.WARNING, "No events found to discard!");
		}

		logger.log(Level.FINE, Constants.METHOD_EXIT + "processEvents");
		return null;
	}

	private void deleteEvents(List<Event> events) throws InstanceNotFoundException, ReflectionException, ConnectorException {
		// Delete processes connected to this chunk of events
		// Collect event id for events where stopping the process not failing
		List<String> eventIds = new ArrayList<String>();
		for (Event event : events) {
			String correlationId = event.getCorrelationID();
			try {
				if (correlationId != null && correlationId.startsWith("_AI:")) {
					bfmConnection.deleteProcessInstanceByActivityId(correlationId);
					event.setProcessStatus(ProcessStatus.DELETED);
				} else {
					event.setProcessStatus(ProcessStatus.NOPROCESS);
				}
				eventIds.add(event.getMessageID());
			} catch (ServiceException se) {
				event.setProcessStatus(ProcessStatus.FAILED);
				event.setProcessFailureMessage(se.getMessage());
			}
		}

		try {
			// Map events to correct String array
			String opDelete = Queries.QUERY_DISCARD_FAILED_EVENTS;
			String[] sig = new String[] { "[Ljava.lang.String;" };
			String[] ids = new String[eventIds.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = events.get(i).getMessageID();
			}
			Object[] parameters = new Object[] { ids };

			// Delete events in this chunk
			adminClient.invoke(faildEventManager, opDelete, parameters, sig);

			// Update the reported events with event status deleted
			for (Event event : events) {
				event.setEventStatus(EventStatus.DELETED);
			}

		} catch (MBeanException e) {
			// REPORT not deleted events
			if (e.getTargetException() instanceof DiscardFailedException) {
				FailedEventExceptionReport[] re = ((DiscardFailedException) e.getTargetException())
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
					if (event.getEventStatus().equals(EventStatus.FAILED)) {
						event.setEventStatus(EventStatus.DELETED);
					}
				}
			}
		}
	}
}
