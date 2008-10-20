package no.nav.femhelper.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
			for (int startIndex = 0; startIndex < events.size();) {
				int endIndex = Math.min(startIndex + Constants.MAX_DELETE, events.size());
				List<Event> chunk = events.subList(startIndex, endIndex);
				for (Event event : chunk) {
					event.setEventStatus(EventStatus.MARKED);
				}
				logger.log(Level.INFO, "Discard result set of events fra #" + (startIndex + 1) + " to #" + endIndex);
				deleteEvents(chunk);
				startIndex = endIndex;
			}

			// Update the status map after all deleting are completed
			logger.log(Level.INFO, "Discarding of #" + events.size() + " events...done!");
			logger.log(Level.INFO, "Reporting status of discard events...please wait!");
			for (Event event : events) {
				fileWriter.writeShortEvent(event);
			}
		} else {
			logger.log(Level.WARNING, "No events found to discard!");
		}

		logger.log(Level.FINE, Constants.METHOD_EXIT + "processEvents");
		return null;
	}

	private void deleteEvents(List<Event> events) throws InstanceNotFoundException, ReflectionException, ConnectorException,
			MBeanException {
		// Create a local collection that will allow me to remove elements
		List<Event> eventsToDelete = new ArrayList<Event>(events);
		// Delete processes connected to this chunk of events
		// Collect event id for events where stopping the process not failing
		Iterator<Event> eventsToDeleteIterator = eventsToDelete.iterator();
		while (eventsToDeleteIterator.hasNext()) {
			Event event = eventsToDeleteIterator.next();
			String correlationId = event.getCorrelationID();
			try {
				if (correlationId != null && correlationId.startsWith("_AI:")) {
					bfmConnection.deleteProcessInstanceByActivityId(correlationId);
					event.setProcessStatus(ProcessStatus.DELETED);
				} else {
					event.setProcessStatus(ProcessStatus.NOPROCESS);
				}
			} catch (ServiceException se) {
				event.setProcessStatus(ProcessStatus.FAILED);
				event.setProcessFailureMessage(se.getMessage());
				// Remove event from events to delete
				eventsToDeleteIterator.remove();
			}
		}

		try {
			// Map events to correct String array
			String opDelete = Queries.QUERY_DISCARD_FAILED_EVENTS;
			String[] sig = new String[] { "[Ljava.lang.String;" };
			String[] ids = new String[eventsToDelete.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = eventsToDelete.get(i).getMessageID();
			}
			Object[] parameters = new Object[] { ids };

			// Delete events in this chunk
			adminClient.invoke(faildEventManager, opDelete, parameters, sig);

			// Update the reported events with event status deleted
			for (Event event : eventsToDelete) {
				event.setEventStatus(EventStatus.DELETED);
			}
		} catch (MBeanException e) {
			if (e.getTargetException() instanceof DiscardFailedException) {
				FailedEventExceptionReport[] re = ((DiscardFailedException) e.getTargetException())
						.getFailedEventExceptionReports();

				Map<String, FailedEventExceptionReport> reportMap = new LinkedHashMap<String, FailedEventExceptionReport>(
						re.length);
				for (FailedEventExceptionReport report : re) {
					reportMap.put(report.getMsgId(), report);
				}

				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_MILLS);

				// Update with failure status and set failureinformation to the
				// failed events
				for (Event event : eventsToDelete) {
					if (reportMap.containsKey(event.getMessageID())) {
						FailedEventExceptionReport report = reportMap.get(event.getMessageID());
						event.setEventStatus(EventStatus.FAILED);
						event.setEventFailureDate(sdf.format(report.getExceptionTime()));
						event.setEventFailureMessage(report.getExceptionDetail());
					} else {
						event.setEventStatus(EventStatus.DELETED);
					}
				}
			} else {
				throw e;
			}
		}
	}
}
