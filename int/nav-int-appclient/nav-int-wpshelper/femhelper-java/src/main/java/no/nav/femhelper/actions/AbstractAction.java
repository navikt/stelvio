package no.nav.femhelper.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import no.nav.appclient.adapter.BFMConnectionAdapter;
import no.nav.appclient.adapter.BusinessFlowManagerServiceAdapter;
import no.nav.appclient.adapter.ServiceException;
import no.nav.appclient.util.ConfigPropertyNames;
import no.nav.appclient.util.PasswordEncodeDelegate;
import no.nav.appclient.util.PropertyMapper;
import no.nav.femhelper.cmdoptions.CommandOptions;
import no.nav.femhelper.common.Constants;
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
	protected final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * AdminClient instance
	 */
	protected AdminClient adminClient;

	protected BusinessFlowManagerServiceAdapter bfmConnection;

	/**
	 * ObjectName object to represent the FEM Bean
	 */
	protected ObjectName failedEventManager;

	protected EventFileWriter fileWriter;

	protected LogFileWriter logFileWriter;

	/**
	 * Properties from config file
	 */
	private Properties properties;

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
	private void connect() throws ConnectorException, MalformedObjectNameException {
		// Map configuration to ensure additional parameters not are added to
		// the configuration file. We do not want those to be provided directly
		// to the AdminClientFactory.
		PropertyMapper mapper = new PropertyMapper();
		Properties mappedProperties = mapper.getMappedProperties(properties);

		// Decode password
		String encodedPass = mappedProperties.getProperty(ConfigPropertyNames.password);
		PasswordEncodeDelegate encode = new PasswordEncodeDelegate();
		String decodedPassword = encode.getDecryptedPassword(encodedPass);
		mappedProperties.setProperty(ConfigPropertyNames.password, decodedPassword);

		// Create Business Flow Manager instance
		BFMConnectionAdapter adapter = BFMConnectionAdapter.getInstance(mappedProperties);
		bfmConnection = adapter.getBusinessFlowManagerService();

		// Setup and test the connection with FailedEventManager MBean
		adminClient = AdminClientFactory.createAdminClient(mappedProperties);
		ObjectName queryName = new ObjectName("WebSphere:*,type=FailedEventManager");
		Set s = adminClient.queryNames(queryName, null);

		if (!s.isEmpty()) {
			failedEventManager = (ObjectName) s.iterator().next();
			logger.log(Level.FINE, "Connected to Failed Event Manager MBean.");
		} else {
			throw new ServiceException("Failed Event Manager MBean was not found");
		}
	}

	abstract Object processEvents(String path, String filename, Map<String, String> arguments, boolean paging,
			long totalevents, int maxresultset, CommandLine cl) throws IOException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException;

	public Object process(String path, String filename, Map<String, String> arguments, boolean paging, long totalevents,
			int maxresultset, CommandLine cl) throws MalformedObjectNameException, ConnectorException {
		// Log properties before creation of the AdminClient objects
		this.logProperties();

		this.connect();

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
		return result;
	}

	/**
	 * Private method that make use of the class member variable
	 * <code>properties</code> to debug log all input parameters (excluding
	 * the password)
	 */
	private void logProperties() {
		logger.log(Level.FINE, "Initializing admin client with the following properties:");
		logger.log(Level.FINE, "CONNECTOR_HOST: " + properties.getProperty(ConfigPropertyNames.BootstrapHost));
		logger.log(Level.FINE, "CONNECTOR_PORT: " + properties.getProperty(ConfigPropertyNames.BootstrapPort));
		logger.log(Level.FINE, "CONNECTOR_TYPE: " + properties.getProperty(ConfigPropertyNames.CONNECTOR_TYPE));
		logger.log(Level.FINE, "CONNECTOR_SECURITY_ENABLED: "
				+ properties.getProperty(ConfigPropertyNames.CONNECTOR_SECURITY_ENABLED));
		logger.log(Level.FINE, "USERNAME: " + properties.getProperty(ConfigPropertyNames.username));
		logger.log(Level.FINE, "PASSWORD: ****");
		if (!"".equals(properties.getProperty(ConfigPropertyNames.SSL_KEYSTORE))) {
			logger.log(Level.FINE, ConfigPropertyNames.SSL_KEYSTORE + ": "
					+ properties.getProperty(ConfigPropertyNames.SSL_KEYSTORE));
		}
		if (!"".equals(properties.getProperty(ConfigPropertyNames.SSL_TRUSTSTORE))) {
			logger.log(Level.FINE, ConfigPropertyNames.SSL_TRUSTSTORE + ": "
					+ properties.getProperty(ConfigPropertyNames.SSL_TRUSTSTORE));
		}
	}

	/**
	 * Private method for colleacting events TODO AR: Rewrite this method
	 * 
	 * @param criteria ->
	 *            filer events (session id) based on the criteria
	 * @param paging ->
	 *            should we run over all fem entries with paging
	 * @param totalevents ->
	 *            number of totalevents. Is now more used
	 * @param maxresultset ->
	 *            what are the result size
	 * @return a filtered list of failed events
	 */
	@SuppressWarnings("unchecked")
	protected Collection<Event> collectEvents(Map<String, String> agruments, boolean paging, long totalevents, int maxresultset)
			throws InstanceNotFoundException, MBeanException, ReflectionException, ConnectorException, IOException {
		logFileWriter.log("Starting to collect events");

		// Method level variables
		Map<String, Event> events = new LinkedHashMap<String, Event>();
		Date begin = null;
		Date end = new Date();
		List<FailedEvent> failedEventList;
		int pagecount = 0;
		
		// Set timefram agruments if applicable
		// The presence of times and the format are allready validated in
		// ArgumentValidator
		if (agruments.containsKey(CommandOptions.timeFrame)) {
			String timeFrame = agruments.get(CommandOptions.timeFrame);
			String times[] = StringUtils.split(timeFrame, "-");
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FRAME_FORMAT);
			try {
				begin = sdf.parse(times[0]);
				end = sdf.parse(times[1]);
			} catch (ParseException e) {
				throw new RuntimeException("Date values are not validated properly");
			}
		}

		do {
			++pagecount;
			logger.log(Level.INFO, "Collect events for page #" + pagecount);
			String femQuery = Queries.QUERY_EVENT_WITH_TIMEPERIOD;
			Object[] pagepar = new Object[] { begin, end, new Integer(maxresultset) };
			String[] pagesig = new String[] { "java.util.Date", "java.util.Date", "int" };
			failedEventList = (List<FailedEvent>) adminClient.invoke(failedEventManager, femQuery, pagepar, pagesig);
			for (FailedEvent failedEvent : failedEventList) {
				Event event = new Event(failedEvent.getMsgId(), failedEvent.getCorrelationId());
				events.put(event.getMessageID(), event);
			}

			if (!failedEventList.isEmpty()) {
				// Update end date for the next page
				FailedEvent lastevent = failedEventList.get(failedEventList.size() - 1);
				logger.log(Level.INFO, "Completed collecting events for page #" + pagecount);
				logger.log(Level.INFO, "The last event in this page is " + lastevent.getFailureDateTime());
				end = new Date(lastevent.getFailureDateTime().getTime());

				long firstInMillis = failedEventList.get(0).getFailureDateTime().getTime();
				long lastInMillis = lastevent.getFailureDateTime().getTime();
				if (firstInMillis == lastInMillis && failedEventList.size() == maxresultset) {
					logger.log(Level.SEVERE, "All events in this page is created on the same millis. "
							+ "This is unsupported. Try to increase pagesize, but keep memory usage in mind.");
					logger.log(Level.INFO, "Due to this situation the application is now exiting!");
					System.exit(0);
				}
			}
		} while (paging && failedEventList.size() == maxresultset);

		if (events.size() > 0) {
			logger.log(Level.INFO, "Collect events is done with result of event(s): #" + events.size());
		}

		logFileWriter.log("Collected " + events.size() + " events");
		return events.values();
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
		match = match && validate(event.getSourceModuleName(), arguments.get(CommandOptions.sourceModule)) ? true : false;
		match = match && validate(event.getSourceComponentName(), arguments.get(CommandOptions.sourceComponent)) ? true : false;
		match = match && validate(event.getDestinationModuleName(), arguments.get(CommandOptions.destinationModule)) ? true
				: false;
		match = match && validate(event.getDestinationComponentName(), arguments.get(CommandOptions.destinationComponent)) ? true
				: false;
		match = match && isMatchWildCard(event.getFailureMessage(), arguments.get(CommandOptions.failureMessage));

		String timeFrame = arguments.get(CommandOptions.timeFrame);
		if (match && !StringUtils.isEmpty(timeFrame)) {
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

	private boolean isMatchWildCard(String input, String regex) {
		if (!StringUtils.isEmpty(regex)) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(input);
			return m.find();
		} else {
			return true;
		}
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
	private boolean validate(String eventValue, String criteria) {
		if (StringUtils.isEmpty(criteria) || StringUtils.contains(eventValue, criteria)) {
			return true;
		}
		return false;
	}

	protected boolean askYesNo(String question) {
		String answer = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (!"y".equalsIgnoreCase(answer) && !"n".equalsIgnoreCase(answer)) {
				if (answer != null) {
					System.out.println(answer + " is not a valid answer");
				}
				System.out.print(question + " ");
				answer = in.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return "y".equalsIgnoreCase(answer);
	}
}
