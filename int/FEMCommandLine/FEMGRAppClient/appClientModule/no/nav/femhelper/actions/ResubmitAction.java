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
	private Logger LOGGER = Logger.getLogger(ResubmitAction.class.getName());
	
	@Override
	Object processEvents(String path, String filename, String criteria,
			boolean paging, long totalevents, int maxresultset, CommandLine cl)
			throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		
		LOGGER.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the events.");
		fileWriter = new EventFileWriter(path, filename);
		
		LOGGER.log(Level.FINE, "Write header part.");
		fileWriter.writeDiscardHeader();
	
		ArrayList <String> events = collectEvents(criteria, paging, totalevents, maxresultset);
		String opResubmit = Queries.QUERY_RESUBMIT_FAILED_EVENTS;
		
		
		if (!events.isEmpty()) {
			LOGGER.log(Level.INFO,"Resubmiting #" + events.size() + " events...please wait!");
			int j = 1;
			ArrayList <String> chunk = new ArrayList<String>();
			HashMap<String, String> reportEvents = new HashMap<String, String>();
			for (int i = 0; i < events.size(); i++) {
				chunk.add(events.get(i));
				reportEvents.put(events.get(i), new String("MARKED~ ~ "));

				// for each result set
				if (j==Constants.MAX_DELETE)
				{
					LOGGER.log(Level.INFO, "Resubmited result set of events from #" + ((i+1)-Constants.MAX_DELETE) + " to #" + (i+1));
					String passIn[] = new String[chunk.size()];
				 	for (int d = 0; d < passIn.length; d++) {
						passIn[d] = chunk.get(d);
					}
					Object[] para = new Object[] {passIn};  
				 	String[] sig = new String[] {"[Ljava.lang.String;"};
				 	try {
						adminClient.invoke(failEventManager, opResubmit, para, sig);
						// no exception update hashtable for reporting
						for (int d = 0; d < passIn.length; d++) {
							if (reportEvents.containsKey(passIn[d])) {
								reportEvents.put(passIn[d], "RESUBMITED~ ~ ");
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
									reportEvents.put(passIn[d], "RESUBMITED~ ~ ");
								}
							}	
						}
					}
					// reset chunk iterator
					j=1;
					chunk.clear();
				}
				// the last chunk
				else if ((events.size()-i) < Constants.MAX_DELETE && events.size()==(i+1)) {
					
					if (events.size() < Constants.MAX_DELETE)
						LOGGER.log(Level.INFO, "Resubmit result set of #" + (i+1) + " events" );
					else
						LOGGER.log(Level.INFO, "Resubmit final result set of #" + chunk.size() + " events" );

					String passIn[] = new String[chunk.size()];
				 	for (int d = 0; d < passIn.length; d++) {
						passIn[d] = chunk.get(d);
					}
					Object[] para = new Object[] {passIn};  
				 	String[] sig = new String[] {"[Ljava.lang.String;"};
				 	try {
						adminClient.invoke(failEventManager, opResubmit, para, sig);
						// no exception update hashtable for reporting
						for (int d = 0; d < passIn.length; d++) {
							if (reportEvents.containsKey(passIn[d])) {
								reportEvents.put(passIn[d], "RESUBMIT~ ~ ");
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
									reportEvents.put(passIn[d], "RESUBMITED~ ~ ");
								}
							}	
						}
					}
					// reset chunk iterator
					j=1;
					chunk.clear();
				}
				j++;
			} // event for
			
			LOGGER.log(Level.INFO,"Resubmit of #" + events.size() + " events...done!");
			LOGGER.log(Level.INFO,"Reporting status of events...please wait!");
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
			LOGGER.log(Level.WARNING, "No events found to resubmit!");
		}


		
		fileWriter.close();

		return null;

	}

}
