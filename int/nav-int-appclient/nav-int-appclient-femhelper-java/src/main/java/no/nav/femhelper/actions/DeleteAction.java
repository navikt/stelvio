package no.nav.femhelper.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import no.nav.appclient.util.Constants;
import no.nav.femhelper.common.Queries;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;

import com.ibm.wbiserver.manualrecovery.FailedEventExceptionReport;
import com.ibm.wbiserver.manualrecovery.FailedEventWithParameters;
import com.ibm.wbiserver.manualrecovery.exceptions.DiscardFailedException;
import com.ibm.websphere.management.exception.ConnectorException;

public class DeleteAction extends AbstractAction {

	public DeleteAction(Properties properties) {
		super(properties);
	}

	/**
	 * Logger instance
	 */
	private Logger logger = Logger.getLogger(DeleteAction.class.getName());
	
	HashMap<String, String> reportEvents = new HashMap<String, String>();
	
	@Override
	Object processEvents(String path, String filename, Map arguments,
			boolean paging, long totalevents, int maxresultset, CommandLine cl)
			throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		
		logger.log(Level.FINE, Constants.METHOD_ENTER + "processEvents");
		logger.log(Level.FINE, "Write discard header part.");
		
		fileWriter.writeShortHeader();
	
		// Collect events to stop processes and delete events
		ArrayList <String> events = collectEvents(arguments, paging, totalevents, maxresultset);
		
		// Stop all process that are correlated to any of the events collected
		stopProcesses(events);
		
		
		if (!cl.hasOption(Constants.noStop)) {
			String q = "Do you want to continue and delete " + events.size() + " events?";
			boolean result = askYesNo(q);
			if (!result) {
				return null;
			}
		}
		
		if (events.size() > 0) {
			logger.log(Level.INFO,"Discarding #" + events.size() + " events...please wait!");
			int j = 1;
			ArrayList <String> deleteChunk = new ArrayList<String>();
			
			for (int i = 0; i < events.size(); i++) {
				deleteChunk.add(events.get(i));
				reportEvents.put(events.get(i), new String("MARKED~ ~ "));

				// for each result set
				if (j==Constants.MAX_DELETE) {
					logger.log(Level.INFO, "Discard result set of events fra #" + ((i+1)-Constants.MAX_DELETE) + " to #" + (i+1));
					String passIn[] = new String[deleteChunk.size()];
				 	for (int d = 0; d < passIn.length; d++) {
						passIn[d] = deleteChunk.get(d);
					}
				 	deleteEvents(passIn);
					// reset chunk iterator
					j=1;
					deleteChunk.clear();
				} else if ((events.size()-i) < Constants.MAX_DELETE && events.size()==(i+1)) {
					// the last chunk
					
					if (events.size() < Constants.MAX_DELETE) {
						logger.log(Level.INFO, "Delete result set of #" + (i+1) + " events" );
					} else {
						logger.log(Level.INFO, "Delete final result set of #" + deleteChunk.size() + " events" );
					}

					String passIn[] = new String[deleteChunk.size()];
				 	for (int d = 0; d < passIn.length; d++) {
						passIn[d] = deleteChunk.get(d);
					}
				 	deleteEvents(passIn);
					
					// reset chunk iterator
					j=1;
					deleteChunk.clear();
				}
				j++;
			} // event for
			
			// Update the status map after all deleting are completed
			logger.log(Level.INFO,"Discarding of #" + events.size() + " events...done!");
			logger.log(Level.INFO,"Reporting status of discard events...please wait!");
			Iterator it = reportEvents.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String[] values = (reportEvents.get(key)).split("~");
				String status = values[0];
				String fdate = values[1];
				String fmsg = values[2];
				fileWriter.writeShortEvent(key, status, fdate, fmsg);
			}
		}
		else {
			logger.log(Level.WARNING, "No events found to discard!");
		}

		logger.log(Level.FINE, Constants.METHOD_EXIT + "processEvents");
		return null;
	}
	
	private void deleteEvents(String events[]) throws InstanceNotFoundException, ReflectionException, ConnectorException {
		String opDelete = Queries.QUERY_DISCARD_FAILED_EVENTS;
		String[] sig = new String[] {"[Ljava.lang.String;"};
		Object[] para = new Object[] {events};
		
		try {
			adminClient.invoke(faildEventManager, opDelete, para, sig);
			// no exception update hashtable for reporting
			for (int d = 0; d < events.length; d++) {
				if (reportEvents.containsKey(events[d])) {
					reportEvents.put(events[d], "DELETED~ ~ ");
				}
			}
		} catch (MBeanException e) {
			//REPORT not deleted events
			if (e.getTargetException() instanceof DiscardFailedException) {
				FailedEventExceptionReport[] exRep = ((DiscardFailedException) e.getTargetException()).getFailedEventExceptionReports();
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_MILLS);
				for (int r = 0; r < exRep.length; r++) {
					if (reportEvents.containsKey(exRep[r].getMsgId())) {
						reportEvents.put(exRep[r].getMsgId(), "FAILURE" + "~" + sdf.format(exRep[r].getExceptionTime()) + "~" +  exRep[r].getExceptionDetail());
					}	
				}
				//UPDATE ALL the rest to DELETED
				for (int d = 0; d < events.length; d++) {
					if (reportEvents.get(events[d]).indexOf("FAILURE")==-1) {
						reportEvents.put(events[d], "DELETED~ ~ ");
					}
				}	
			}
		}
	}
	
	private void stopProcesses(List <String> eventIds) throws InstanceNotFoundException, MBeanException, ReflectionException, ConnectorException {
		String query = Queries.QUERY_EVENT_WITH_PARAMETERS;
		
		// Temporarily variable to keep process ids (PIIDs) to stop afterwards
		Set <String> processIds = new LinkedHashSet<String>();
		
		for (int i = 0; i < eventIds.size(); i++) {
			Object[] BOparams = new Object[] { new String((String) eventIds.get(i)) };
			String[] BOsignature = new String[] { "java.lang.String" };
			FailedEventWithParameters event = (FailedEventWithParameters) adminClient.invoke(faildEventManager, query, BOparams, BOsignature);
			if (!StringUtils.isEmpty(event.getCorrelationId())) {
				processIds.add(event.getCorrelationId());
				logger.fine("Adding process " + event.getCorrelationId() + " to the list of processes to stop");
			} else {
				logger.fine("Event " + event.getMsgId() + " don't hava a correlation id. No process is handled as a matter of deleting this event");
			}
			
		}
		
		logger.fine("The failed events are in total correlated to " + processIds.size() + " processes");
		logger.fine("Forcing " + processIds.size() + " to terminate");
		
		for (String piid : processIds) {
			bfmConnection.forceTerminate(piid);
		}
		 
	}
}
