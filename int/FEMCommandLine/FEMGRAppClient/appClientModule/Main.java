import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Constants;
import utils.PropertyUtil;


/**
 *  
 * @author persona2c5e3b49756 Schnell
 * @author Andreas Røe
 */
public class Main {

	private static Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args) throws IOException {

		String sPath = "/tmp"; 
		String sFilename = "default_events.txt";
		long lTotalEvents;
		int lPagesize; 
		boolean bPaging=false;
		
		LOGGER.log(Level.INFO, "FEMGRAppClient for WPS 6.1 - Version 1.0.1");
		
		if (args.length <= 0)
		{	
		 LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "The property file is not spesified, or does not exist");
		 return;
		}
				
		String propertyPath = args[0];
		File propertyFile = new File(propertyPath);
		if (!propertyFile.exists()) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "The property file is not spesified, or does not exist");
			return;
		}
		
		// It is more or less okay to throw these exceptions from 
		// this method as we have give the user a log entry about
		// this and simultainisly abort the program before this
		// will occur.
		Properties connectProps = new Properties();
		connectProps.load(new FileInputStream(propertyPath));
		
		// Log warnings from property validation if any violations and exit
		List validatedProperties = PropertyUtil.validateProperties(connectProps);
		if (!validatedProperties.isEmpty()) {
			for (int i = 0; i < validatedProperties.size(); i++) {
				LOGGER.log(Level.WARNING, (String) validatedProperties.get(i));
			}
			LOGGER.log(Level.SEVERE, "FEMGRAppClient will now end due to issues with missing or invalid parameter in the configuration file - logged above!");
			return;
		}

		// We doesn't accept tolarge result sets due to Java Dmgr Heap Size
		String sPagesize=connectProps.getProperty(Constants.MAX_RESULT_SET);
		if (sPagesize.length()> 5 ) {
			LOGGER.log(Level.SEVERE, "FEMGRAppClient will now end because the MAX_RESULT_SET parameter is to large (1 >= 9999).");
			return;
		}
		
		try {
			LOGGER.log(Level.FINE, "Get instance of EventClient.");
			EventClient eventClient = new EventClient(connectProps);
			boolean isConnected = eventClient.connect();
			
			if (!isConnected) {
				LOGGER.log(Level.SEVERE, "FEMGRAppClient will now end due to issues with connecting to Failed Event Manager - logged above!");
				return;
			}

			// get the total number of events to match with the page size to avoid hughe amount of retrieving
			lTotalEvents = eventClient.getTotalFailedEvents();
			if (lTotalEvents == -1 ) {
				LOGGER.log(Level.SEVERE, "FEMGRAppClient will now end due to issues with getting the total failed events count of Failed Event Manager - logged above!");
				return;
			} else if (lTotalEvents == 0)
			{
				LOGGER.log(Level.SEVERE, "FEMGRAppClient will now end due to that no events exists at Failed Event Manager.");
				return;
			}

			// set to max. fem entries if pagsizse is greater
			lPagesize = Integer.valueOf((connectProps.getProperty(Constants.MAX_RESULT_SET)));
			if (lPagesize > lTotalEvents)
			{
				lPagesize = Integer.valueOf((Long.toString(lTotalEvents)));
				LOGGER.log(Level.INFO, "Set working result set to total number of events (" + lPagesize + ") because MAX_RESULT_SET > TotalEvents.");
			}
			
			// paging?  
			bPaging = Boolean.parseBoolean(connectProps.getProperty(Constants.MAX_RESULT_SET_PAGING).toLowerCase());
			
			// implement FEM_EDA_TYPE and FEM_EDA_TYPE_ACTION
			String sEdaType = connectProps.getProperty(Constants.FEM_EDA_TYPE);
			String sEdaTypeAction = connectProps.getProperty(Constants.FEM_EDA_TYPE_ACTION);
			LOGGER.log(Level.INFO, "Running scenario " + sEdaTypeAction + " with filter option " + sEdaType + " and paging option is set to " + bPaging);

			//REPORT
			if (sEdaTypeAction.equalsIgnoreCase(Constants.FEM_EDA_TYPE_ACTION_OPTIONS[0]))
			{
				// build output file
				sPath = connectProps.getProperty(Constants.LOGFILE_PATH);
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
				Calendar cal = GregorianCalendar.getInstance();
				Date lastEventDate = cal.getTime();
				sFilename = Constants.FILE_PREFIX + "_" + sEdaTypeAction + "_" + sEdaType  +"_"+ sdf.format(lastEventDate) + ".csv";
				
				//ALL
				if (sEdaType.equalsIgnoreCase(Constants.FEM_EDA_TYPE_OPTIONS[0]))
				{
					eventClient.reportEvents(sPath, sFilename, null, bPaging, lTotalEvents, lPagesize);	
				}
				//BASED on FILTER
				else
				{
					eventClient.reportEvents(sPath, sFilename, sEdaType, bPaging, lTotalEvents, lPagesize);
				}
			}
			
			// DISCARD
			else if (sEdaTypeAction.equalsIgnoreCase(Constants.FEM_EDA_TYPE_ACTION_OPTIONS[1]))
			{
				// build output file
				sPath = connectProps.getProperty(Constants.LOGFILE_PATH);
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
				Calendar cal = GregorianCalendar.getInstance();
				Date lastEventDate = cal.getTime();
				sFilename = Constants.FILE_PREFIX + "_" + sEdaTypeAction + "_" + sEdaType  +"_"+ sdf.format(lastEventDate) + ".csv";
				
				//ALL
				if (sEdaType.equalsIgnoreCase(Constants.FEM_EDA_TYPE_OPTIONS[0]))
				{
					eventClient.discardEvents(sPath, sFilename, null, bPaging, lTotalEvents, lPagesize);						
				}
				//BASED on FILTER
				else
				{
					//test
					//eventClient.discardEvents(sPath, sFilename, "mod-test-CEI-FEM", bPaging, lTotalEvents, lPagesize);
					eventClient.discardEvents(sPath, sFilename, sEdaType, bPaging, lTotalEvents, lPagesize);
				}
			}
			
			// RESUBMIT
			else if (sEdaTypeAction.equalsIgnoreCase(Constants.FEM_EDA_TYPE_ACTION_OPTIONS[2])) {
				// build output file
				sPath = connectProps.getProperty(Constants.LOGFILE_PATH);
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
				Calendar cal = GregorianCalendar.getInstance();
				Date lastEventDate = cal.getTime();
				sFilename = Constants.FILE_PREFIX + "_" + sEdaTypeAction + "_" + sEdaType  +"_"+ sdf.format(lastEventDate) + ".csv";
				
				//ALL
				if (sEdaType.equalsIgnoreCase(Constants.FEM_EDA_TYPE_OPTIONS[0]))
				{
					eventClient.resubmitEvents(sPath, sFilename, null, bPaging, lTotalEvents, lPagesize);						
				}
				//BASED on FILTER
				else
				{
					eventClient.resubmitEvents(sPath, sFilename, sEdaType, bPaging, lTotalEvents, lPagesize);
				}
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "Exception:StackTrace:");
			e.printStackTrace();
		}
		LOGGER.log(Level.INFO, "FEMGRAppClient done...Thx for using it!");
	}
}