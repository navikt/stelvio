package no.nav.femhelper.actions;

import java.io.IOException;
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
	private Logger LOGGER = Logger.getLogger(ReportAction.class.getName());
	
	public StatusAction(Properties properties) {
		super(properties);
	}

	@Override
	Object processEvents(String path, String filename, String criteria,
			boolean paging, long totalevents, int maxresultset, CommandLine cl)
			throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "processEvents");
		Long lnr = new Long(-1);
		String countQuery = Queries.QUERY_COUNT_EVENTS;
		lnr = (Long) adminClient.invoke(failEventManager, countQuery, null, null);
		LOGGER.log(Level.INFO, "Current total number of events: #" + lnr);
		
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "processEvents");
		return lnr; 
	}

}
