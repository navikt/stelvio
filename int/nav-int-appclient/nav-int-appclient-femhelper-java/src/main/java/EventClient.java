import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

import no.nav.appclient.util.Constants;
import no.nav.appclient.util.PropertyMapper;
import no.nav.femhelper.common.Queries;
import no.nav.femhelper.filewriters.EventFileWriter;


import com.ibm.wbiserver.manualrecovery.FailedEvent;
import com.ibm.wbiserver.manualrecovery.FailedEventExceptionReport;
import com.ibm.wbiserver.manualrecovery.FailedEventWithParameters;
import com.ibm.wbiserver.manualrecovery.exceptions.DiscardFailedException;
import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.AdminClientFactory;
import com.ibm.websphere.management.exception.ConnectorException;

/**
 * @author Andreas Røe
 * @author persona2c5e3b49756 Schnell
 * @deprecated Action classes should be used instead
 */
public class EventClient {

	// properties from config file
	private Properties properties;
	
	//adminClient instance
	private AdminClient adminClient=null;
	ObjectName failEventManager = null;
	
	private static Logger LOGGER = Logger.getLogger(EventClient.class.getName());


	/**
	 * <p>Default constructor which takes a set of connection
	 * settings as the one and only parameter. Instance of the 
	 * AdminClient object is created first when the 
	 * <code>writeEvents()</code> method is invoked</p> 
	 * 
	 * <p>This constructor does also validate the configuration</p>
	 * 
	 * @param properties properties with connection settings
	 */
	public EventClient(Properties properties) {
		super();
		adminClient = null;
		failEventManager=null;	
		this.properties = properties;
		logProperties();
	}

	/**
	 * <p>Connect to FEM</p>
	 * @return true or false if connect was working
	 * @deprecated Action classes should be used instead
	 */
	public boolean connect() 
	{
		// Map configuration to ensure additional parameters not are added to 
		// the configuration file. We do not want those to be provided directly
		// to the AdminClientFactory.
		PropertyMapper mapper = new PropertyMapper();
		Properties mappedProperties = mapper.getMappedProperties(properties);
	 	try {
			adminClient = AdminClientFactory.createAdminClient(mappedProperties);
		 	ObjectName queryName = new ObjectName("WebSphere:*,type=FailedEventManager");
		 	Set s = adminClient.queryNames(queryName, null);
		 	if (!s.isEmpty()) {
				failEventManager = (ObjectName) s.iterator().next();
				LOGGER.log(Level.FINE, "Connected to Failed Event Manager MBean.");
			 	return true;
			} else {
				LOGGER.log(Level.WARNING, "Failed Event Manager MBean was not found");
				return false;
			}

	 	} catch (ConnectorException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ConnectorException:StackTrace:");
			e.printStackTrace();
		 	return false;
		} catch (MalformedObjectNameException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "MalformedObjectNameException:StackTrace:");
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "NullPointerException:StackTrace:");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return number of total failed events
	 * @deprecated Action classes should be used instead 
	 */
	public long getTotalFailedEvents()  {
		try {
			// Printing the total number of failed events
			Long lnr = Long.decode("0");
			String countQuery = Queries.QUERY_COUNT_EVENTS;
			lnr = (Long) adminClient.invoke(failEventManager, countQuery, null, null);
			LOGGER.log(Level.INFO, "Current total number of events: #" + lnr);
			return lnr; 
		} catch (InstanceNotFoundException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "InstanceNotFoundException:StackTrace:");
			e.printStackTrace();
			return -1;
		} catch (MBeanException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "MBeanException:StackTrace:");
			e.printStackTrace();
			return -1;
		} catch (ReflectionException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ReflectionException:StackTrace:");
			e.printStackTrace();
			return -1;
		} catch (ConnectorException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ConnectorException:StackTrace:");
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Get all events with or without paging
	 * @param path
	 * @param filename
	 * @param paging
	 * @param totalevents
	 * @param maxresultset
	 * @deprecated Action classes should be used instead
	 */
	public void reportEvents(String path, String filename, String criteria, boolean paging, long totalevents, int maxresultset ) {

		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "EventClient.reportEvents");
		EventFileWriter fileWriter=null;
		try 
		{
			LOGGER.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the events.");
			fileWriter = new EventFileWriter(path, filename);
			LOGGER.log(Level.FINE, "Write CSV header part.");
			fileWriter.writeHeader();
		
			ArrayList <String> events = new ArrayList<String>();
			events = collectEvents(criteria, paging, totalevents, maxresultset);
			
			if (events.size() > 0) {
				LOGGER.log(Level.INFO,"Reporting events...please wait!");
				Iterator itreport = events.iterator();
				//report
				while (itreport.hasNext()) {
					// report the result
					String femQuery = Queries.QUERY_EVENT_WITH_PARAMETERS;
					Object[] BOparams = new Object[] { new String((String)itreport.next()) };
					String[] BOsignature = new String[] { "java.lang.String" };
					FailedEventWithParameters failedEventWithParameters = (FailedEventWithParameters) adminClient.invoke(failEventManager, femQuery, BOparams, BOsignature);
					fileWriter.writeCSVEvent(failedEventWithParameters, adminClient, Constants.DEFAULT_DATE_FORMAT_MILLS);
					
					// test after 1 take to much time
					//break;
				}
				LOGGER.log(Level.INFO,"Reporting of #" + events.size() + " events...done!");
			}
			else
			{
				LOGGER.log(Level.WARNING, "No events found to report!");
			}

			fileWriter.close();

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "IOException:StackTrace:");			
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "InstanceNotFoundException:StackTrace:");
			e.printStackTrace();
			fileWriter.close();
		} catch (MBeanException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "MBeanException:StackTrace:");
			e.printStackTrace();
			fileWriter.close();
		} catch (ReflectionException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ReflectionException:StackTrace:");
			e.printStackTrace();
			fileWriter.close();
		} catch (ConnectorException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ConnectorException:StackTrace:");
			e.printStackTrace();
			fileWriter.close();
		}
		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "EventClient.reportEvents");
 	}
	
	
	/**
	 * Discard events from FEM
	 * @param path
	 * @param filename
	 * @param criteria
	 * @param paging
	 * @param totalevents
	 * @param maxresultset
	 * @deprecated Action classes should be used instead
	 */
	public void discardEvents(String path, String filename, String criteria, boolean paging, long totalevents, int maxresultset) {
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "EventClient.reportEvents");
		EventFileWriter fileWriter=null;
		try 
		{
			LOGGER.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the events.");
			fileWriter = new EventFileWriter(path, filename);
			LOGGER.log(Level.FINE, "Write discard header part.");
			fileWriter.writeShortHeader();
		
			ArrayList <String> events = new ArrayList<String>();
			// collect events before delete
			events = collectEvents(criteria, paging, totalevents, maxresultset);
			
			// for test purpose to simulate failure from FEM
			/*
			try {
				LOGGER.log(Level.WARNING, "SLLLLLLLLLLLLLLEEEEEEEPING!!!!");
				Thread.sleep(30000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			*/
			
			if (events.size() > 0) {
				LOGGER.log(Level.INFO,"Discarding #" + events.size() + " events...please wait!");
				int j = 1;
				ArrayList <String> deleteChunk = new ArrayList<String>();
				HashMap<String, String> reportEvents = new HashMap<String, String>();
				for (int i = 0; i < events.size(); i++) {
					deleteChunk.add(events.get(i));
					reportEvents.put(events.get(i), new String("MARKED~ ~ "));

					// for each result set
					if (j==Constants.MAX_DELETE)
					{
						LOGGER.log(Level.INFO, "Discard result set of events fra #" + ((i+1)-Constants.MAX_DELETE) + " to #" + (i+1));
						String passIn[] = new String[deleteChunk.size()];
					 	for (int d = 0; d < passIn.length; d++) {
							passIn[d] = deleteChunk.get(d);
						}
						String opDelete = Queries.QUERY_DISCARD_FAILED_EVENTS;
						Object[] para = new Object[] {passIn};  
					 	String[] sig = new String[] {"[Ljava.lang.String;"};
					 	try {
							adminClient.invoke(failEventManager, opDelete, para, sig);
							// no exception update hashtable for reporting
							for (int d = 0; d < passIn.length; d++) {
								if (reportEvents.containsKey(passIn[d])) {
									reportEvents.put(passIn[d], "DELETED~ ~ ");
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
										reportEvents.put(passIn[d], "DELETED~ ~ ");
									}
								}	
							}
						}
						// reset chunk iterator
						j=1;
						deleteChunk.clear();
					}
					// the last chunk
					else if ((events.size()-i) < Constants.MAX_DELETE && events.size()==(i+1)) {
						
						if (events.size() < Constants.MAX_DELETE)
							LOGGER.log(Level.INFO, "Delete result set of #" + (i+1) + " events" );
						else
							LOGGER.log(Level.INFO, "Delete final result set of #" + deleteChunk.size() + " events" );

						String passIn[] = new String[deleteChunk.size()];
					 	for (int d = 0; d < passIn.length; d++) {
							passIn[d] = deleteChunk.get(d);
						}
						String opDelete = Queries.QUERY_DISCARD_FAILED_EVENTS;
						Object[] para = new Object[] {passIn};  
					 	String[] sig = new String[] {"[Ljava.lang.String;"};
					 	try {
							adminClient.invoke(failEventManager, opDelete, para, sig);
							// no exception update hashtable for reporting
							for (int d = 0; d < passIn.length; d++) {
								if (reportEvents.containsKey(passIn[d])) {
									reportEvents.put(passIn[d], "DELETE~ ~ ");
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
										reportEvents.put(passIn[d], "DELETED~ ~ ");
									}
								}	
							}
						}
						// reset chunk iterator
						j=1;
						deleteChunk.clear();
					}
					j++;
				} // event for
				
				LOGGER.log(Level.INFO,"Discarding of #" + events.size() + " events...done!");
				LOGGER.log(Level.INFO,"Reporting status of discard events...please wait!");
				Iterator it = reportEvents.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					String[] values = (reportEvents.get(key)).split("~");
					String status = values[0];
					String fdate = values[1];
					String fmsg = values[2];
					fileWriter.writeShortEvent(key, status, fdate, fmsg);
				}
			}
			else
			{
				LOGGER.log(Level.WARNING, "No events found to discard!");
			}


			
			fileWriter.close();

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "IOException:StackTrace:");			
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "InstanceNotFoundException:StackTrace:");
			e.printStackTrace();
			fileWriter.close();
		} catch (ReflectionException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ReflectionException:StackTrace:");
			e.printStackTrace();
			fileWriter.close();
		} catch (ConnectorException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ConnectorException:StackTrace:");
			e.printStackTrace();
			fileWriter.close();
		}
		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "EventClient.reportEvents");
	
	}
		
	/**
	 * Rebubmit events from FEM
	 * @param path
	 * @param filename
	 * @param criteria
	 * @param paging
	 * @param totalevents
	 * @param maxresultset
	 * @deprecated Action classes should be used instead
	 */
	public void resubmitEvents(String path, String filename, String criteria, boolean paging, long totalevents, int maxresultset) {
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "EventClient.reportEvents");
		EventFileWriter fileWriter=null;
		try 
		{
			LOGGER.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the events.");
			fileWriter = new EventFileWriter(path, filename);
			LOGGER.log(Level.FINE, "Write discard header part.");
			fileWriter.writeShortHeader();
		
			ArrayList <String> events = collectEvents(criteria, paging, totalevents, maxresultset);
			
			// for test purpose to simulate failure from FEM
			/*
			try {
				LOGGER.log(Level.WARNING, "SLLLLLLLLLLLLLLEEEEEEEPING!!!!");
				Thread.sleep(30000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			*/
			
			if (!events.isEmpty()) {
				LOGGER.log(Level.INFO,"Discarding #" + events.size() + " events...please wait!");
				int j = 1;
				ArrayList <String> deleteChunk = new ArrayList<String>();
				HashMap<String, String> reportEvents = new HashMap<String, String>();
				for (int i = 0; i < events.size(); i++) {
					deleteChunk.add(events.get(i));
					reportEvents.put(events.get(i), new String("MARKED~ ~ "));

					// for each result set
					if (j==Constants.MAX_DELETE)
					{
						LOGGER.log(Level.INFO, "Discard result set of events fra #" + ((i+1)-Constants.MAX_DELETE) + " to #" + (i+1));
						String passIn[] = new String[deleteChunk.size()];
					 	for (int d = 0; d < passIn.length; d++) {
							passIn[d] = deleteChunk.get(d);
						}
						String opDelete = Queries.QUERY_RESUBMIT_FAILED_EVENTS;
						Object[] para = new Object[] {passIn};  
					 	String[] sig = new String[] {"[Ljava.lang.String;"};
					 	try {
							adminClient.invoke(failEventManager, opDelete, para, sig);
							// no exception update hashtable for reporting
							for (int d = 0; d < passIn.length; d++) {
								if (reportEvents.containsKey(passIn[d])) {
									reportEvents.put(passIn[d], "DELETED~ ~ ");
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
										reportEvents.put(passIn[d], "DELETED~ ~ ");
									}
								}	
							}
						}
						// reset chunk iterator
						j=1;
						deleteChunk.clear();
					}
					// the last chunk
					else if ((events.size()-i) < Constants.MAX_DELETE && events.size()==(i+1)) {
						
						if (events.size() < Constants.MAX_DELETE)
							LOGGER.log(Level.INFO, "Delete result set of #" + (i+1) + " events" );
						else
							LOGGER.log(Level.INFO, "Delete final result set of #" + deleteChunk.size() + " events" );

						String passIn[] = new String[deleteChunk.size()];
					 	for (int d = 0; d < passIn.length; d++) {
							passIn[d] = deleteChunk.get(d);
						}
						String opDelete = Queries.QUERY_DISCARD_FAILED_EVENTS;
						Object[] para = new Object[] {passIn};  
					 	String[] sig = new String[] {"[Ljava.lang.String;"};
					 	try {
							adminClient.invoke(failEventManager, opDelete, para, sig);
							// no exception update hashtable for reporting
							for (int d = 0; d < passIn.length; d++) {
								if (reportEvents.containsKey(passIn[d])) {
									reportEvents.put(passIn[d], "DELETE~ ~ ");
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
										reportEvents.put(passIn[d], "DELETED~ ~ ");
									}
								}	
							}
						}
						// reset chunk iterator
						j=1;
						deleteChunk.clear();
					}
					j++;
				} // event for
				
				LOGGER.log(Level.INFO,"Discarding of #" + events.size() + " events...done!");
				LOGGER.log(Level.INFO,"Reporting status of discard events...please wait!");
				Iterator it = reportEvents.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					String[] values = (reportEvents.get(key)).split("~");
					String status = values[0];
					String fdate = values[1];
					String fmsg = values[2];
					fileWriter.writeShortEvent(key, status, fdate, fmsg);
				}
			}
			else
			{
				LOGGER.log(Level.WARNING, "No events found to discard!");
			}


			
			fileWriter.close();

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "IOException:StackTrace:");			
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "InstanceNotFoundException:StackTrace:");
			e.printStackTrace();
			fileWriter.close();
		} catch (ReflectionException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ReflectionException:StackTrace:");
			e.printStackTrace();
			fileWriter.close();
		} catch (ConnectorException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ConnectorException:StackTrace:");
			e.printStackTrace();
			fileWriter.close();
		}
		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "EventClient.reportEvents");
	
	}

	
		
	/**
	 * Private method that make use of the class member variable
	 * <code>properties</code> to debug log all input parameters
	 * (excluding the password) 
	 * @deprecated Action classes should be used instead
	 */
	private void logProperties() {
		LOGGER.log(Level.FINE, "Initializing admin client with the following properties:");
		LOGGER.log(Level.FINE, "CONNECTOR_HOST: " + properties.getProperty(Constants.BootstrapHost));
		LOGGER.log(Level.FINE, "CONNECTOR_PORT: " + properties.getProperty(Constants.BootstrapPort));
		LOGGER.log(Level.FINE, "CONNECTOR_TYPE: " + properties.getProperty(Constants.CONNECTOR_TYPE));
		LOGGER.log(Level.FINE, "CONNECTOR_SECURITY_ENABLED: " + properties.getProperty(Constants.CONNECTOR_SECURITY_ENABLED));
		LOGGER.log(Level.FINE, "USERNAME: " + properties.getProperty(Constants.username));
		LOGGER.log(Level.FINE, "PASSWORD: ****");
		if (!"".equals(properties.getProperty(Constants.SSL_KEYSTORE))) {
			LOGGER.log(Level.FINE, Constants.SSL_KEYSTORE + ": " + properties.getProperty(Constants.SSL_KEYSTORE));
		}
		if (!"".equals(properties.getProperty(Constants.SSL_TRUSTSTORE))) {
			LOGGER.log(Level.FINE, Constants.SSL_TRUSTSTORE + ": " + properties.getProperty(Constants.SSL_TRUSTSTORE));
		}
	}

	/**
	 * Private method for colleacting events
	 * @param criteria -> filer events (session id) based on the criteria
	 * @param paging -> should we run over all fem entries with paging
	 * @param totalevents -> number of totalevents
	 * @param maxresultset -> what are the result size
	 * @return
	 * @deprecated Action classes should be used instead
	 */
	private ArrayList <String> collectEvents (String criteria, boolean paging, long totalevents, int maxresultset) {

		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "EventClient.collectEvents");
		
		// method variables
		int iEvents=1;
		ArrayList <String> events = new ArrayList<String>();
		String lastEventId = null;
		Date lastEventDate = null;
		
		try 
		{
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
				if (criteria == null) {
					events.add(lastEventId);	
				} else 
				{
					// only with criteria -> what kind of interface
					if (failedEvent.getSessionId().toLowerCase().indexOf(criteria.toLowerCase()) != -1)	{
						events.add(lastEventId);	
					}
				}
				// count the events
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
					if (!lastEventId.equals(currEventId))
					{

						// if criteria == null all event id's put into ArrayList
						if (criteria == null) {
							events.add(currEventId);	
						} else 
						{
							// only with criteria -> what kind of interface
							if (failedEvent.getSessionId().toLowerCase().indexOf(criteria.toLowerCase()) != -1)	{
								events.add(currEventId);	
							}
						}
					}
					else
					{	
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
		} catch (InstanceNotFoundException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "InstanceNotFoundException:StackTrace:");
			e.printStackTrace();
			return null;
		} catch (MBeanException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "MBeanException:StackTrace:");
			e.printStackTrace();
			return null;
		} catch (ReflectionException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ReflectionException:StackTrace:");
			e.printStackTrace();
			return null;			
		} catch (ConnectorException e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "ConnectorException:StackTrace:");
			e.printStackTrace();
			return null;
		}
		if (events.size() > 0) {
			LOGGER.log(Level.INFO, "Collect events is done with result of event(s): #" + events.size());			
		}
		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "EventClient.collectEvents");
		return events;		
	}
}
