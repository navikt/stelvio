package no.nav.femhelper.actions;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import no.nav.femhelper.common.Queries;

import org.apache.commons.cli.CommandLine;

import com.ibm.websphere.management.exception.ConnectorException;

public class StatusAction extends AbstractAction {
	public StatusAction(Properties properties) {
		super(properties);
	}

	@Override
	Object processEvents(String path, String filename, Map argument, boolean paging, long totalevents, int maxresultset,
			CommandLine cl) throws IOException, InstanceNotFoundException, MBeanException, ReflectionException,
			ConnectorException {
		String countQuery = Queries.QUERY_COUNT_EVENTS;
		Long lnr = (Long) adminClient.invoke(faildEventManager, countQuery, null, null);
		logger.log(Level.INFO, "Current total number of events: #" + lnr);
		return lnr;
	}
}
