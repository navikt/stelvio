package no.nav.femhelper.actions;

import java.io.IOException;
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

import com.ibm.websphere.management.exception.ConnectorException;

public class StatusAction extends AbstractAction {

	/**
	 * Logger instance
	 */
	private Logger logger = Logger.getLogger(ReportAction.class.getName());
	
	public StatusAction(Properties properties) {
		super(properties);
	}

	@Override
	Object processEvents(String path, String filename, Map argument,
			boolean paging, long totalevents, int maxresultset, CommandLine cl)
			throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		
		logger.log(Level.FINE, Constants.METHOD_ENTER + "processEvents");
		Long lnr = new Long(-1);
		String countQuery = Queries.QUERY_COUNT_EVENTS;
		lnr = (Long) adminClient.invoke(faildEventManager, countQuery, null, null);
		logger.log(Level.INFO, "Current total number of events: #" + lnr);
		
		logger.log(Level.FINE, Constants.METHOD_ENTER + "processEvents");
		return lnr; 
	}

}
