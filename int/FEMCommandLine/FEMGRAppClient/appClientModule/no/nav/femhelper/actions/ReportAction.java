package no.nav.femhelper.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.apache.commons.cli.CommandLine;

import no.nav.femhelper.common.Constants;
import no.nav.femhelper.common.Queries;
import no.nav.femhelper.filewriters.EventFileWriter;

import com.ibm.wbiserver.manualrecovery.FailedEventWithParameters;
import com.ibm.websphere.management.exception.ConnectorException;

public class ReportAction extends AbstractAction {

	public ReportAction(Properties properties) {
		super(properties);
	}

	/**
	 * Logger instance
	 */
	private Logger LOGGER = Logger.getLogger(ReportAction.class.getName());
	
	@Override
	Object processEvents(String path, String filename, String criteria, boolean paging, long totalevents, int maxresultset, CommandLine cl) throws IOException, InstanceNotFoundException, MBeanException, ReflectionException, ConnectorException {
		LOGGER.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the events.");
		fileWriter = new EventFileWriter(path, filename);
		
		LOGGER.log(Level.FINE, "Write CSV header part.");
		fileWriter.writeHeader();
	
		ArrayList <String> events = new ArrayList<String>();
		events = collectEvents(criteria, paging, totalevents, maxresultset);
		
		for (int i = 0; i < events.size(); i++) {
		LOGGER.log(Level.INFO,"Reporting events (" + (i+1) + " of " + events.size() + "). Please wait!");
			
			// Write the report
			String femQuery = Queries.QUERY_EVENT_WITH_PARAMETERS;
			Object[] BOparams = new Object[] { new String((String) events.get(i)) };
			String[] BOsignature = new String[] { "java.lang.String" };
			FailedEventWithParameters failedEventWithParameters = (FailedEventWithParameters) adminClient.invoke(failEventManager, femQuery, BOparams, BOsignature);
			fileWriter.writeCSVEvent(failedEventWithParameters, adminClient, Constants.DEFAULT_DATE_FORMAT_MILLS);
		}
		
		LOGGER.log(Level.INFO,"Reporting of #" + events.size() + " events...done!");
		
		fileWriter.close();
		return events;
	}

}
