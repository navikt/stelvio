package no.nav.femhelper.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import no.nav.femhelper.common.Constants;
import no.nav.femhelper.common.Queries;
import no.nav.femhelper.filewriters.EventFileWriter;

import org.apache.commons.cli.CommandLine;

import com.ibm.wbiserver.manualrecovery.FailedEventExceptionReport;
import com.ibm.wbiserver.manualrecovery.exceptions.DiscardFailedException;
import com.ibm.websphere.management.exception.ConnectorException;

public class ResubmitAction extends AbstractAction {

	public ResubmitAction(Properties properties) {
		super(properties);
	}

	/**
	 * Logger instance
	 */
	private Logger LOGGER = Logger.getLogger(DeleteAction.class.getName());
	
	@Override
	Object processEvents(String path, String filename, String criteria,
			boolean paging, long totalevents, int maxresultset, CommandLine cl)
			throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		
		EventFileWriter fileWriter=null;
		LOGGER.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the events.");
		fileWriter = new EventFileWriter(path, filename);
		LOGGER.log(Level.FINE, "Write discard header part.");
		fileWriter.writeDiscardHeader();
	
		ArrayList <String> events = collectEvents(criteria, paging, totalevents, maxresultset);
		
		// for test purpose to simulate failure from FEM
		/*
		try {
			LOGGER.log(Level.WARNING, "SLLLLLLLLLLLLLLEEEEEEEPING!!!!");
			Thread.sleep(30000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		*/
		
		if (!events.isEmpty()) {
			LOGGER.log(Level.INFO,"Discarding #" + events.size() + " events...please wait!");
			int j = 1;
			ArrayList <String> deleteChunk = new ArrayList<String>();
			HashMap<String, String> reportEvents = new HashMap<String, String>();
			for (int i = 0; i < events.size(); i++) {
				deleteChunk.add(events.get(i));
				reportEvents.put(events.get(i), new String("MARKED~ ~ "));

				// for each result set
				if (j==Constants.MAX_DELETE)
				{
					LOGGER.log(Level.INFO, "Discard result set of events fra #" + ((i+1)-Constants.MAX_DELETE) + " to #" + (i+1));
					String passIn[] = new String[deleteChunk.size()];
				 	for (int d = 0; d < passIn.length; d++) {
						passIn[d] = deleteChunk.get(d);
					}
					String opDelete = Queries.QUERY_RESUBMIT_FAILED_EVENTS;
					Object[] para = new Object[] {passIn};  
				 	String[] sig = new String[] {"[Ljava.lang.String;"};
				 	try {
						adminClient.invoke(failEventManager, opDelete, para, sig);
						// no exception update hashtable for reporting
						for (int d = 0; d < passIn.length; d++) {
							if (reportEvents.containsKey(passIn[d])) {
								reportEvents.put(passIn[d], "DELETED~ ~ ");
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
							for (int d = 0; d < passIn.length; d++) {
								if (reportEvents.get(passIn[d]).indexOf("FAILURE")==-1) {
									reportEvents.put(passIn[d], "DELETED~ ~ ");
								}
							}	
						}
					}
					// reset chunk iterator
					j=1;
					deleteChunk.clear();
				}
				// the last chunk
				else if ((events.size()-i) < Constants.MAX_DELETE && events.size()==(i+1)) {
					
					if (events.size() < Constants.MAX_DELETE)
						LOGGER.log(Level.INFO, "Delete result set of #" + (i+1) + " events" );
					else
						LOGGER.log(Level.INFO, "Delete final result set of #" + deleteChunk.size() + " events" );

					String passIn[] = new String[deleteChunk.size()];
				 	for (int d = 0; d < passIn.length; d++) {
						passIn[d] = deleteChunk.get(d);
					}
					String opDelete = Queries.QUERY_DISCARD_FAILED_EVENTS;
					Object[] para = new Object[] {passIn};  
				 	String[] sig = new String[] {"[Ljava.lang.String;"};
				 	try {
						adminClient.invoke(failEventManager, opDelete, para, sig);
						// no exception update hashtable for reporting
						for (int d = 0; d < passIn.length; d++) {
							if (reportEvents.containsKey(passIn[d])) {
								reportEvents.put(passIn[d], "DELETE~ ~ ");
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
							for (int d = 0; d < passIn.length; d++) {
								if (reportEvents.get(passIn[d]).indexOf("FAILURE")==-1) {
									reportEvents.put(passIn[d], "DELETED~ ~ ");
								}
							}	
						}
					}
					// reset chunk iterator
					j=1;
					deleteChunk.clear();
				}
				j++;
			} // event for
			
			LOGGER.log(Level.INFO,"Discarding of #" + events.size() + " events...done!");
			LOGGER.log(Level.INFO,"Reporting status of discard events...please wait!");
			Iterator it = reportEvents.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String[] values = (reportEvents.get(key)).split("~");
				String status = values[0];
				String fdate = values[1];
				String fmsg = values[2];
				fileWriter.writeDISCARDEvent(key, status, fdate, fmsg);
			}
		}
		else
		{
			LOGGER.log(Level.WARNING, "No events found to discard!");
		}


		
		fileWriter.close();

		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "EventClient.reportEvents");
		return null;

	}

}
