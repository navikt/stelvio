package no.nav.femhelper.actions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import no.nav.femhelper.common.Constants;
import no.nav.femhelper.common.Queries;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;

import com.ibm.wbiserver.manualrecovery.FailedEvent;
import com.ibm.wbiserver.manualrecovery.FailedEventWithParameters;
import com.ibm.websphere.management.exception.ConnectorException;

/**
 * @deprecated The parameter -timeFrame will be used in collectEvents instead 
 */
public class TimeFrameAction extends AbstractAction {

	/**
	 * Logger instance
	 */
	private Logger logger = Logger.getLogger(ReportAction.class.getName());
	
	public TimeFrameAction(Properties properties) {
		super(properties);
	}

	@Override
	Object processEvents(String path, String filename, Map arguments,
			boolean paging, long totalevents, int maxresultset, CommandLine cl)
			throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		
		logger.log(Level.FINE, Constants.METHOD_ENTER + "processEvents");
		
		logger.log(Level.FINE, "Write discard header part.");
		fileWriter.writeHeader();
		
		// Parse the date objects. The values are allready validated
		// agains the pattern defined in Constants.TIME_FRAME_FORMAT
		String input = cl.getOptionValue(Constants.timeFrame);
		String dates[] = StringUtils.split(input, "-");
		Date fromDate = null;
		Date toDate = null;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FRAME_FORMAT);
			fromDate = sdf.parse(dates[0]);
			toDate = sdf.parse(dates[1]);
		} catch (ParseException e) {
			// Will never occur here. 
			// TODO AR: Consider to repackage this kind of exceptions
		}
		
		String timeQuery = Queries.QUERY_EVENT_WITH_TIMEPERIOD;
		Object[] pagepar = new Object[] { fromDate, toDate, new Integer(maxresultset)};
		String[] pagesig = new String[] { "java.util.Date", "java.util.Date", "int"};
		
		List events = (List) adminClient.invoke(faildEventManager, timeQuery, pagepar, pagesig);
		Iterator it = events.iterator();
		
		while (it.hasNext()) {
			logger.log(Level.INFO,"Reporting events...please wait!");
			
			FailedEvent failedEvent = (FailedEvent) it.next();
			String messageID[] = new String[]{failedEvent.getMsgId()};
			
			// Write the report
			String query = Queries.QUERY_EVENT_WITH_PARAMETERS;
			String[] BOsignature = new String[] {"java.lang.String"};
			
			// Write the report
			FailedEventWithParameters p = (FailedEventWithParameters) adminClient.invoke(faildEventManager, query, messageID, BOsignature);
			fileWriter.writeCSVEvent(p, adminClient, Constants.DEFAULT_DATE_FORMAT_MILLS);
		}
		
		logger.log(Level.INFO,"Reporting of #" + events.size() + " events...done!");
		logger.log(Level.FINE, Constants.METHOD_EXIT + "processEvents");

		return null;
	}

}
