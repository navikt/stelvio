package no.nav.femhelper.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import no.nav.femhelper.cmdoptions.CommandOptions;
import no.nav.femhelper.common.Constants;
import no.nav.femhelper.common.Event;
import no.nav.femhelper.common.EventStatus;
import no.nav.femhelper.common.Queries;

import org.apache.commons.cli.CommandLine;

import com.ibm.wbiserver.manualrecovery.FailedEventExceptionReport;
import com.ibm.wbiserver.manualrecovery.exceptions.ResubmissionFailedException;
import com.ibm.websphere.management.exception.ConnectorException;

public class ResubmitAction extends AbstractAction {
	public ResubmitAction(Properties properties) {
		super(properties);
	}

	@Override
	Object processEvents(String path, String filename, Map<String, String> arguments, boolean paging, long totalevents,
			int maxresultset, CommandLine cl) throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		logger.log(Level.FINE, "Write header part.");
		fileWriter.writeShortHeader();

		logFileWriter.log("Starting to collect events");
		Collection <Event> events = collectEvents(arguments, paging, totalevents, maxresultset);
		logFileWriter.log("Collected " + events.size() + " events");

		if (!events.isEmpty()) {
			if (!cl.hasOption(CommandOptions.noStop)) {
				String q = "Do you want to continue and resubmit " + events.size() + " events (y/n)?";
				boolean result = askYesNo(q);
				if (!result) {
					return null;
				}
			}

			List <Event> eventList = new ArrayList<Event> (events);
			
			logger.log(Level.INFO, "Resubmiting #" + events.size() + " events...please wait!");
			logFileWriter.log("Staring to resubmit " + events.size() + " events");
			for (int startIndex = 0; startIndex < events.size();) {
				int endIndex = Math.min(startIndex + Constants.MAX_DELETE, events.size());
				List<Event> chunk = eventList.subList(startIndex, endIndex);
				for (Event event : chunk) {
					event.setEventStatus(EventStatus.MARKED);
				}
				logger.log(Level.INFO, "Resubmitted result set of events from #" + (startIndex + 1) + " to #" + endIndex);
				resubmitEvents(chunk);
				startIndex = endIndex;
			}

			logger.log(Level.INFO, "Resubmit of #" + events.size() + " events...done!");
			logger.log(Level.INFO, "Reporting status of events...please wait!");
			logFileWriter.log("Completed resubmit of " + events.size() + " events");
			for (Event event : events) {
				fileWriter.writeShortEvent(event);
			}
		} else {
			logger.log(Level.WARNING, "No events found to resubmit!");
		}
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
			adminClient.invoke(failedEventManager, opResubmit, para, sig);

			// Update the reported events with event status resubmitted
			for (Event event : events) {
				event.setEventStatus(EventStatus.RESUBMITED);
			}
		} catch (MBeanException e) {
			if (e.getTargetException() instanceof ResubmissionFailedException) {
				FailedEventExceptionReport[] re = ((ResubmissionFailedException) e.getTargetException())
						.getFailedEventExceptionReports();

				Map<String, FailedEventExceptionReport> reportMap = new LinkedHashMap<String, FailedEventExceptionReport>(
						re.length);
				for (FailedEventExceptionReport report : re) {
					reportMap.put(report.getMsgId(), report);
				}

				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_MILLS);

				// Update with failure status and set failureinformation to the
				// failed events
				for (Event event : events) {
					if (reportMap.containsKey(event.getMessageID())) {
						FailedEventExceptionReport report = reportMap.get(event.getMessageID());
						event.setEventStatus(EventStatus.FAILED);
						event.setEventFailureDate(sdf.format(report.getExceptionTime()));
						event.setEventFailureMessage(report.getExceptionDetail());
					} else {
						event.setEventStatus(EventStatus.RESUBMITED);
					}
				}
			} else {
				throw e;
			}
		}
	}
}
