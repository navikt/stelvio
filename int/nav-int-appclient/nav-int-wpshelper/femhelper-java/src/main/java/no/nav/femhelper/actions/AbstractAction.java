package no.nav.femhelper.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import no.nav.appclient.adapter.BFMConnectionAdapter;
import no.nav.appclient.adapter.BusinessFlowManagerServiceAdapter;
import no.nav.appclient.util.Constants;
import no.nav.appclient.util.PasswordEncodeDelegate;
import no.nav.appclient.util.PropertyMapper;
import no.nav.femhelper.common.Event;
import no.nav.femhelper.common.Queries;
import no.nav.femhelper.filewriters.EventFileWriter;
import no.nav.femhelper.filewriters.LogFileWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;

import com.ibm.wbiserver.manualrecovery.FailedEvent;
import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.AdminClientFactory;
import com.ibm.websphere.management.exception.ConnectorException;

/**
 * Super class for all Action classes. This class shall be inherited for all
 * functional areas, such as finding all failed events, deleting events, or just
 * getting the current depth of FEM
 * 
 * @author Andreas Roe
 */
public abstract class AbstractAction {

	/**
	 * Logger instance
	 */
	private Logger logger = Logger.getLogger(AbstractAction.class.getName());

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

	protected BusinessFlowManagerServiceAdapter bfmConnection;

	/**
	 * ObjectName object to represent the FEM Bean
	 */
	protected ObjectName faildEventManager;

	protected EventFileWriter fileWriter;

	protected LogFileWriter logFileWriter;

	private boolean connected;

	/**
	 * Default contructor
	 * 
	 * @param must
	 *            be an instance TODO AR This should not be necessary
	 */
	public AbstractAction(Properties properties) {
		this.properties = properties;
	}

	/**
	 * <p>
	 * Connect to FEM
	 * </p>
	 * 
	 * @return true or false if connect was working
	 */
	private boolean connect() throws ConnectorException, MalformedObjectNameException { // NullPointerException
		// {

		logger.log(Level.FINE, Constants.METHOD_ENTER + "connect");
		boolean result = false;

		// Map configuration to ensure additional parameters not are added to
		// the configuration file. We do not want those to be provided directly
		// to the AdminClientFactory.
		PropertyMapper mapper = new PropertyMapper();
		Properties mappedProperties = mapper.getMappedProperties(properties);

		// Decode password
		String encodedPass = mappedProperties.getProperty(Constants.password);
		PasswordEncodeDelegate encode = new PasswordEncodeDelegate();
		String decodedPassword = encode.getDecryptedPassword(encodedPass);
		mappedProperties.setProperty(Constants.password, decodedPassword);

		// Create Business Flow Manager instance
		try {
			BFMConnectionAdapter adapter = BFMConnectionAdapter.getInstance(mappedProperties);
			bfmConnection = adapter.getBusinessFlowManagerService();
		} catch (RuntimeException re) {
			result = false;
			logger.log(Level.SEVERE, "Could not connect to the BFM", re);
		}

		// Setup and test the connection with FailedEventManager MBean
		adminClient = AdminClientFactory.createAdminClient(mappedProperties);
		ObjectName queryName = new ObjectName("WebSphere:*,type=FailedEventManager");
		Set s = adminClient.queryNames(queryName, null);

		if (!s.isEmpty()) {
			faildEventManager = (ObjectName) s.iterator().next();
			logger.log(Level.FINE, "Connected to Failed Event Manager MBean.");
			result = true;
		} else {
			logger.log(Level.SEVERE, "Failed Event Manager MBean was not found");
			result = false;
		}

		logger.log(Level.FINE, Constants.METHOD_EXIT + "connect");
		return result;
	}

	// TODO: SEB: Hva gjør denne her?
	private void disconnect() {
		logger.log(Level.FINE, Constants.METHOD_ENTER + "disconnect");
		logger.log(Level.FINE, Constants.METHOD_EXIT + "disconnect");
	}

	abstract Object processEvents(String path, String filename, Map<String, String> arguments, boolean paging,
			long totalevents, int maxresultset, CommandLine cl) throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException;

	public Object process(String path, String filename, Map<String, String> arguments, boolean paging, long totalevents,
			int maxresultset, CommandLine cl) throws MalformedObjectNameException, ConnectorException, NullPointerException {

		logger.log(Level.FINE, Constants.METHOD_ENTER + "collectEvents");

		// Log properties before creation of the AdminClient objects
		this.logProperties();

		this.connected = this.connect();

		// Create file writer instances
		logger.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the events.");

		Object result = null;
		try {
			fileWriter = new EventFileWriter(path, filename + ".csv");
			logFileWriter = new LogFileWriter(path, filename + ".log");

			result = this.processEvents(path, filename, arguments, paging, totalevents, maxresultset, cl);
		} catch (IOException e) {
			logger.log(Level.SEVERE, Constants.METHOD_ERROR + "IOException:StackTrace:");
		} catch (InstanceNotFoundException e) {
			logger.log(Level.SEVERE, Constants.METHOD_ERROR + "InstanceNotFoundException:StackTrace:");
		} catch (MBeanException e) {
			logger.log(Level.SEVERE, Constants.METHOD_ERROR + "MBeanException:StackTrace:");
		} catch (ReflectionException e) {
			logger.log(Level.SEVERE, Constants.METHOD_ERROR + "ReflectionException:StackTrace:");
		} catch (ConnectorException e) {
			logger.log(Level.SEVERE, Constants.METHOD_ERROR + "ConnectorException:StackTrace:");
		}

		// Close writers. (The close() method handles if the writer allready
		// have been closed)
		if (null != filename) {
			fileWriter.close();
		}

		if (null != logFileWriter) {
			logFileWriter.close();
		}

		this.disconnect();

		logger.log(Level.FINE, Constants.METHOD_ENTER + "collectEvents");

		return result;
	}

	/**
	 * Private method that make use of the class member variable
	 * <code>properties</code> to debug log all input parameters (excluding
	 * the password)
	 */
	private void logProperties() {
		logger.log(Level.FINE, Constants.METHOD_ENTER + "logProperties");

		logger.log(Level.FINE, "Initializing admin client with the following properties:");
		logger.log(Level.FINE, "CONNECTOR_HOST: " + properties.getProperty(Constants.BootstrapHost));
		logger.log(Level.FINE, "CONNECTOR_PORT: " + properties.getProperty(Constants.BootstrapPort));
		logger.log(Level.FINE, "CONNECTOR_TYPE: " + properties.getProperty(Constants.CONNECTOR_TYPE));
		logger.log(Level.FINE, "CONNECTOR_SECURITY_ENABLED: " + properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED));
		logger.log(Level.FINE, "USERNAME: " + properties.getProperty(Constants.username));
		logger.log(Level.FINE, "PASSWORD: ****");
		if (!"".equals(properties.getProperty(Constants.SSL_KEYSTORE))) {
			logger.log(Level.FINE, Constants.SSL_KEYSTORE + ": " + properties.getProperty(Constants.SSL_KEYSTORE));
		}
		if (!"".equals(properties.getProperty(Constants.SSL_TRUSTSTORE))) {
			logger.log(Level.FINE, Constants.SSL_TRUSTSTORE + ": " + properties.getProperty(Constants.SSL_TRUSTSTORE));
		}

		logger.log(Level.FINE, Constants.METHOD_ENTER + "logProperties");
	}

	/**
	 * Private method for colleacting events TODO AR: Rewrite this method
	 * 
	 * @param criteria ->
	 *            filer events (session id) based on the criteria
	 * @param paging ->
	 *            should we run over all fem entries with paging
	 * @param totalevents ->
	 *            number of totalevents
	 * @param maxresultset ->
	 *            what are the result size
	 * @return a filtered list of failed events
	 */
	protected ArrayList<Event> collectEvents(Map<String, String> agruments, boolean paging, long totalevents, int maxresultset)
			throws InstanceNotFoundException, MBeanException, ReflectionException, ConnectorException, IOException {

		logger.log(Level.FINE, Constants.METHOD_ENTER + "collectEvents");

		logFileWriter.log("Starting to collect events");

		// Method level variables
		ArrayList<Event> events = new ArrayList<Event>();
		int iEvents = 1;
		String lastEventId = null;
		Date lastEventDate = null;

		SimpleDateFormat sdfm = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_MILLS);
		Calendar cal = GregorianCalendar.getInstance();
		lastEventDate = cal.getTime();

		// Get all events from fem based on maxresultset
		Object[] params = new Object[] { new Integer(maxresultset) };
		String[] signature = new String[] { "int" };
		String femQuery = Queries.QUERY_ALL_EVENTS;
		List failedEventList = (List) adminClient.invoke(faildEventManager, femQuery, params, signature);

		// first result set
		logger.log(Level.INFO, "Collect events is working for first result set...please wait!");

		for (int i = 0; i < failedEventList.size(); i++) {
			FailedEvent failedEvent = (FailedEvent) failedEventList.get(i);
			lastEventDate = failedEvent.getFailureDateTime();
			lastEventId = failedEvent.getMsgId();

			if (isEventApplicable(failedEvent, agruments)) {
				Event event = new Event(failedEvent.getMsgId(), failedEvent.getCorrelationId());
				events.add(event);
			}

			iEvents++;
		}

		// clean up list for re-use
		failedEventList.clear();

		int pagecount = 1;
		int current = 1;

		while (paging && iEvents < totalevents) {
			logger.log(Level.INFO, "Paging is activated: collected LastEventId: " + lastEventId + " LastEventDate: "
					+ sdfm.format(lastEventDate) + " Events collected: #" + events.size());

			femQuery = Queries.QUERY_EVENT_WITH_TIMEPERIOD;

			// FEM API deliver last current to oldest
			Date end = new Date(lastEventDate.getTime() + Constants.ONE_MILLI);

			// midnight + Constants.MONTH_RANGE
			cal.setTime(lastEventDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date begin = new Date(cal.getTimeInMillis() - (Constants.MONTH_RANGE * Constants.ONE_WEEK));

			logger.log(Level.INFO, "New collection start from EventDate: " + sdfm.format(begin) + " to EventDate: "
					+ sdfm.format(end) + " with max. result set of #" + maxresultset);

			Object[] pagepar = new Object[] { begin, end, new Integer(maxresultset) };
			String[] pagesig = new String[] { "java.util.Date", "java.util.Date", "int" };
			failedEventList = (List) adminClient.invoke(faildEventManager, femQuery, pagepar, pagesig);
			Iterator pgit = failedEventList.iterator();

			logger.log(Level.INFO, "Collect events is working for result set #" + pagecount + "...please wait!");

			while (pgit.hasNext()) {
				FailedEvent failedEvent = (FailedEvent) pgit.next();
				lastEventDate = failedEvent.getFailureDateTime();
				String currEventId = failedEvent.getMsgId();

				// check id from last loop and skip if we have a double entry
				// because on milli can include the last id (better
				// than don't select events)
				if (!lastEventId.equals(currEventId) && isEventApplicable(failedEvent, agruments)) {
					Event event = new Event(failedEvent.getMsgId(), failedEvent.getCorrelationId());
					events.add(event);
				} else {
					logger.log(Level.FINE, "Collect events detect on result set #" + pagecount + " that the event with the id "
							+ currEventId + " is allready collected!");
				}
				current++;
				iEvents++;
				lastEventId = currEventId;
			}

			// reset variables for next loop
			failedEventList.clear();
			pagecount++;
			current = 1;
		}

		if (events.size() > 0) {
			logger.log(Level.INFO, "Collect events is done with result of event(s): #" + events.size());
		}

		logFileWriter.log("Collected " + events.size() + " events");
		logger.log(Level.FINE, Constants.METHOD_EXIT + "collectEvents");
		return events;
	}

	// Getters and setters

	public boolean isConnected() {
		return connected;
	}

	/**
	 * Method that evaluate if a single event matches one or more search
	 * criterias
	 * 
	 * @param event
	 *            to evaluate
	 * @param arguments
	 *            to evaluate against. This is collected from the
	 *            <code>CommandLing </code> object
	 * @return
	 */
	private boolean isEventApplicable(FailedEvent event, Map<String, String> arguments) {

		// Default true. If no criterias are listed the 'ALL events' apply.
		boolean match = true;

		// Make use of the generic validate method for arguments that validate
		// attributes of datatype
		match = match && validate(event.getSourceModuleName(), arguments.get(Constants.sourceModule)) ? true : false;
		match = match && validate(event.getSourceComponentName(), arguments.get(Constants.sourceComponent)) ? true : false;
		match = match && validate(event.getDestinationModuleName(), arguments.get(Constants.destinationModule)) ? true : false;
		match = match && validate(event.getDestinationComponentName(), arguments.get(Constants.destinationComponent)) ? true
				: false;
		match = match && validate(event.getFailureMessage(), arguments.get(Constants.failureMessage)) ? true : false;

		String timeFrame = arguments.get(Constants.timeFrame);
		if (!StringUtils.isEmpty(timeFrame)) {

			// The time / date format is already validated, and can be parsed
			// without
			// handling ParseException
			String dates[] = StringUtils.split(timeFrame, "-");
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FRAME_FORMAT);
				Date fromDate = sdf.parse(dates[0]);
				Date toDate = sdf.parse(dates[1]);
				Date failureDate = event.getFailureDateTime();

				match = match && null != failureDate && failureDate.after(fromDate) && failureDate.before(toDate) ? true
						: false;

			} catch (ParseException e) {
				// Will never occur here.
			}
		}

		return match;
	}

	/**
	 * Generic method to validate a attribute on the <code>FailedEvent</code>
	 * event against values in the argument list.
	 * 
	 * The rules are the following
	 * <li>TRUE If the criteria is empty</li>
	 * <li>TRUE If the criteria is a part of the value</li>
	 * <li>FALSE If the event's value is empty while the criteria is not</li>
	 * <li>FALSE If the criteria is not a part of the value</li>
	 * 
	 * @param eventValue
	 * @param criteria
	 * @return
	 */
	protected boolean validate(String eventValue, String criteria) {
		if (StringUtils.isEmpty(criteria) || StringUtils.contains(eventValue, criteria)) {
			return true;
		}
		return false;
	}

	protected boolean askYesNo(String question) {
		String answer = "";

		try {
			System.out.println(question + "(y/n)");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			answer = in.readLine();
			while (!answer.equals("y") && !answer.equals("n")) {
				System.out.println(answer + " is not a valid option");
				System.out.println(question + "(y/n)");
				answer = in.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return "y".equals(answer) || "yes".equals(answer) ? true : false;
	}

}
