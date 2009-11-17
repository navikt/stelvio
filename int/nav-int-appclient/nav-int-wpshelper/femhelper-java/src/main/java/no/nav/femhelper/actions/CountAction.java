package no.nav.femhelper.actions;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import no.nav.femhelper.common.Event;

import org.apache.commons.cli.CommandLine;

import com.ibm.websphere.management.exception.ConnectorException;

public class CountAction extends AbstractAction {

	public CountAction(Properties properties) {
		super(properties);
	}

	@Override
	Object processEvents(String path, String filename,
			Map<String, String> arguments, boolean paging, long totalevents,
			int maxresultset, CommandLine cl) throws IOException,
			InstanceNotFoundException, MBeanException, ReflectionException,
			ConnectorException {
		Collection  <Event> events = collectEvents(arguments, paging, totalevents, maxresultset);
		System.out.println("Filter matched " +  events.size() + " events");
		return null;
	}

}
