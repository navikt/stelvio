package no.nav.femhelper.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import no.nav.femhelper.common.Constants;
import no.nav.femhelper.common.Queries;
import no.nav.femhelper.filewriters.EventFileWriter;
import no.nav.femhelper.filewriters.LogFileWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;

import utils.PropertyMapper;

import com.ibm.wbiserver.manualrecovery.FailedEvent;
import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.AdminClientFactory;
import com.ibm.websphere.management.exception.ConnectorException;

/**
 * Super class for all Action classes. This class shall be inherited for all 
 * functional areas, such as finding all failed events, deleting events, or 
 * just getting the current depth of FEM
 * 
 * @author Andreas Roe
 */
public abstract class AbstractAction {
	
	/**
	 * Logger instance
	 */
	private Logger LOGGER = Logger.getLogger(AbstractAction.class.getName());
	
	/**
	 * Properties from config file
	 */
	protected Properties properties;

	/**
	 * Properties from command line options
	 */
	protected CommandLine cl;
	
	/**
	 * AdminClient instance
	 */
	protected AdminClient adminClient;
	
	/**
	 * ObjectName object to represent the FEM Bean
	 */
	protected ObjectName failEventManager;
	
	protected EventFileWriter fileWriter;
	
	protected LogFileWriter logFileWriter;
		
	private boolean connected;
	
	/**
	 * Default contructor
	 * @param must be an instance
	 * TODO AR This should not be necessary
	 */
	public AbstractAction(Properties properties) {
		this.properties = properties;
	}
	
	/**
	 * <p>Connect to FEM</p>
	 * @return true or false if connect was working
	 */
	private boolean connect () throws ConnectorException, MalformedObjectNameException, NullPointerException {
		
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "connect");
		
		// Map configuration to ensure additional parameters not are added to 
		// the configuration file. We do not want those to be provided directly
		// to the AdminClientFactory.
		PropertyMapper mapper = new PropertyMapper();
		Properties mappedProperties = mapper.getMappedProperties(properties);

		adminClient = AdminClientFactory.createAdminClient(mappedProperties);
	 	
	 	// Testing connection with query to FailedEventManager
		ObjectName queryName = new ObjectName("WebSphere:*,type=FailedEventManager");
	 	Set s = adminClient.queryNames(queryName, null);
	 	boolean result = false;
	 	if (!s.isEmpty()) {
			failEventManager = (ObjectName) s.iterator().next();
			LOGGER.log(Level.FINE, "Connected to Failed Event Manager MBean.");
			result = true;
		} else {
			LOGGER.log(Level.WARNING, "Failed Event Manager MBean was not found");
			result = false;
		}

	 	LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "connect");
	 	return result;
	}
	
	private void disconnect() {
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "disconnect");
		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "disconnect");
	}
	
	abstract Object processEvents(String path, String filename,
			String criteria, boolean paging, long totalevents,
			int maxresultset, CommandLine cl) throws IOException,
			InstanceNotFoundException, MBeanException, ReflectionException,
			ConnectorException;
	
	public Object process(String path, String filename, String criteria,
			boolean paging, long totalevents, int maxresultset, CommandLine cl)
			throws MalformedObjectNameException, ConnectorException,
			NullPointerException {
		
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "collectEvents");
		
		// Log properties before creation of the AdminClient objects
		this.logProperties();
		
		this.connected = this.connect();
		
		// Create file writer instances
		LOGGER.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the events.");
		
		Object result = null;
		try {
			fileWriter = new EventFileWriter(path, filename + ".csv");
			logFileWriter = new LogFileWriter(path, filename + ".log");
			
			result = this.processEvents(path, filename, criteria, paging, totalevents, maxresultset, cl);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "IOException:StackTrace:");			
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "InstanceNotFoundException:StackTrace:");
			e.printStackTrace();
		} catch (MBeanException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "MBeanException:StackTrace:");
			e.printStackTrace();
		} catch (ReflectionException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ReflectionException:StackTrace:");
			e.printStackTrace();
		} catch (ConnectorException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ConnectorException:StackTrace:");
			e.printStackTrace();
		}
		
		// Close writers. (The close() method handles if the writer allready have been closed)
		if (null != filename) {
			fileWriter.close();
		}
		
		if (null != logFileWriter) {
			logFileWriter.close();
		}
		
		this.disconnect();
		
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "collectEvents");
		
		return result;
	}
	
	/**
	 * Private method that make use of the class member variable
	 * <code>properties</code> to debug log all input parameters
	 * (excluding the password) 
	 */
	private void logProperties() {
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "logProperties");
		
		LOGGER.log(Level.FINE, "Initializing admin client with the following properties:");
		LOGGER.log(Level.FINE, "CONNECTOR_HOST: " + properties.getProperty(Constants.CONNECTOR_HOST));
		LOGGER.log(Level.FINE, "CONNECTOR_PORT: " + properties.getProperty(Constants.CONNECTOR_PORT));
		LOGGER.log(Level.FINE, "CONNECTOR_TYPE: " + properties.getProperty(Constants.CONNECTOR_TYPE));
		LOGGER.log(Level.FINE, "CONNECTOR_SECURITY_ENABLED: " + properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED));
		LOGGER.log(Level.FINE, "USERNAME: " + properties.getProperty(Constants.USERNAME));
		LOGGER.log(Level.FINE, "PASSWORD: ****");
		if (!"".equals(properties.getProperty(Constants.SSL_KEYSTORE))) {
			LOGGER.log(Level.FINE, Constants.SSL_KEYSTORE + ": " + properties.getProperty(Constants.SSL_KEYSTORE));
		}
		if (!"".equals(properties.getProperty(Constants.SSL_TRUSTSTORE))) {
			LOGGER.log(Level.FINE, Constants.SSL_TRUSTSTORE + ": " + properties.getProperty(Constants.SSL_TRUSTSTORE));
		}
		
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "logProperties");
	}
	
	/**
	 * Private method for colleacting events
	 * TODO AR: Rewrite this method
	 * @param criteria -> filer events (session id) based on the criteria
	 * @param paging -> should we run over all fem entries with paging
	 * @param totalevents -> number of totalevents
	 * @param maxresultset -> what are the result size
	 * @return a filtered list of failed events
	 */
	protected ArrayList <String> collectEvents (String criteria, boolean paging, long totalevents, int maxresultset) 
		throws InstanceNotFoundException, MBeanException, ReflectionException, ConnectorException, IOException{

		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "collectEvents");
		
		logFileWriter.log("Starting to collect events");
		
		// Method level variables
		int iEvents=1;
		ArrayList <String> events = new ArrayList<String>();
		String lastEventId = null;
		Date lastEventDate = null;
		
		// Get all events from fem based on maxresultset
		String femQuery = Queries.QUERY_ALL_EVENTS;
		SimpleDateFormat sdfm = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_MILLS);
		Calendar cal = GregorianCalendar.getInstance();
		lastEventDate = cal.getTime();
	
		Object[] params = new Object[] { new Integer(maxresultset) }; 
		String[] signature = new String[] { "int" };
		List failedEventList = (List) adminClient.invoke(failEventManager, femQuery, params, signature);
		Iterator it = failedEventList.iterator();
	
		// first result set
		LOGGER.log(Level.INFO,"Collect events is working for first result set...please wait!");
		
		while (it.hasNext()) {
			
			FailedEvent failedEvent = (FailedEvent) it.next();
			lastEventDate = failedEvent.getFailureDateTime();
			lastEventId = failedEvent.getMsgId();
		
			// if criteria == null all event id's put into ArrayList
			if (StringUtils.equalsIgnoreCase(Constants.messageTypeOptions[0], criteria)) {
				events.add(lastEventId);	
			} else if (StringUtils.contains(failedEvent.getSessionId().toLowerCase(), criteria.toLowerCase())) {
				// only with criteria -> what kind of interface
				events.add(lastEventId);	
			}
			// increment the eventcounter
			iEvents++;
		}
		// clean up list for re-use
		failedEventList.clear();
		
		int pagecount = 1;
		int current = 1;

		// if paging enabled
		while (paging && iEvents < totalevents) {
			LOGGER.log(Level.INFO, "Paging is activated: collected LastEventId: " + lastEventId + " LastEventDate: " + sdfm.format(lastEventDate) + " Events collected: #" + events.size());

			femQuery = Queries.QUERY_EVENT_WITH_TIMEPERIOD;
			
			// FEM API deliver last current to oldest 
			Date end = new Date(lastEventDate.getTime() + Constants.ONE_MILLI);

			// midnight + Constants.MONTH_RANGE
			cal.setTime(lastEventDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date begin = new Date(cal.getTimeInMillis() - (Constants.MONTH_RANGE*Constants.ONE_WEEK));

			LOGGER.log(Level.INFO, "New collection start from EventDate: " + sdfm.format(begin) + " to EventDate: " + sdfm.format(end) + " with max. result set of #" + maxresultset);
			
			Object[] pagepar = new Object[] { begin, end, new Integer(maxresultset)};
			String[] pagesig = new String[] { "java.util.Date", "java.util.Date", "int"};
			failedEventList = (List) adminClient.invoke(failEventManager, femQuery, pagepar, pagesig);
			Iterator pgit = failedEventList.iterator(); 

			LOGGER.log(Level.INFO,"Collect events is working for result set #" + pagecount + "...please wait!");
			
			while (pgit.hasNext()) {
				FailedEvent failedEvent = (FailedEvent) pgit.next();
				lastEventDate = failedEvent.getFailureDateTime();
				String currEventId = failedEvent.getMsgId();

				// check id from last loop and skip if we have a double entry because on milli can include the last id (better than don't select events)
				if (!lastEventId.equals(currEventId)) {

					// if criteria == null all event id's put into ArrayList
					if (criteria == null || "ALL".equals(criteria.toUpperCase())) {
						events.add(currEventId);	
					} else {
						// only with criteria -> what kind of interface
						if (StringUtils.contains(failedEvent.getSessionId(), criteria))	{
							events.add(currEventId);
						} else if (StringUtils.contains(failedEvent.getSourceModuleName(), criteria)) {
							events.add(currEventId);
						} else if (StringUtils.contains(failedEvent.getDestinationModuleName(), criteria)) {
							events.add(currEventId);
						}
					}
				}
				else {	
				  LOGGER.log(Level.FINE,"Collect events detect on result set #" + pagecount + " that the event with the id " + currEventId + " is allready collected!");
				}
				current++;
				iEvents++;
				lastEventId = currEventId;
			}

			// reset variables for next loop
			failedEventList.clear();
			pagecount++;
			current=1;
		}
		
		if (events.size() > 0) {
			LOGGER.log(Level.INFO, "Collect events is done with result of event(s): #" + events.size());			
		}
		
		logFileWriter.log("Collected " + events.size() + " events");
		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "collectEvents");
		return events;		
	}
	
	// Getters and setters

	public boolean isConnected() {
		return connected;
	}

	
	
}
