/**
 * 
 */
package no.nav.sibushelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.appclient.util.PasswordEncodeDelegate;
import no.nav.sibushelper.cmdoptions.ArgumentValidator;
import no.nav.sibushelper.cmdoptions.CommandOptions;
import no.nav.sibushelper.cmdoptions.CommandOptionsBuilder;
import no.nav.sibushelper.cmdoptions.PropertyUtil;
import no.nav.sibushelper.common.Constants;
import no.nav.sibushelper.filewriters.MessageFileWriter;
import no.nav.sibushelper.helper.AdminHelper;
import no.nav.sibushelper.helper.BusInfo;
import no.nav.sibushelper.helper.Configuration;
import no.nav.sibushelper.helper.DestinationInfo;
import no.nav.sibushelper.helper.MEInfo;
import no.nav.sibushelper.helper.MQLinkInfo;
import no.nav.sibushelper.helper.MQLinkReceiverChannelInfo;
import no.nav.sibushelper.helper.MQLinkSenderChannelInfo;
import no.nav.sibushelper.helper.MessageInfo;
import no.nav.sibushelper.helper.MessagingHelper;
import no.nav.sibushelper.helper.MessagingHelperImpl;
import no.nav.sibushelper.helper.QueuePointInfo;
import no.nav.sibushelper.helper.ServerConfigurationProperties;
import no.nav.sibushelper.helper.ServerInfo;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;

import com.ibm.icu.util.StringTokenizer;
import com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus;
import com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus;

/**
 * @author persona2c5e3b49756 Schnell
 *
 */
public class SIBUSHelper {

	private static Logger logger = Logger.getLogger(SIBUSHelper.class.getName());
	private static final int screenWidth = 500;
	
	protected String arguments[];
	protected String confFileName;
    protected String serverName;
    
    private Configuration overallConf;
    private ServerConfigurationProperties serverConf;
	
		
	/**
	 * @param args
	 */
	public SIBUSHelper(String args[]) {
		overallConf = null;
		serverName = null;
		confFileName = null;
		arguments = null;
		serverConf = null;
		this.arguments = args;
	}

	@SuppressWarnings("unchecked")
	public int invokeHelper()
	{
		System.out.println(getSeparatorLine(100));
		System.out.println("SIBUS Helper for WPS 6.1");
		System.out.println(getSeparatorLine(100));

		CommandOptionsBuilder optionsBuilder = new CommandOptionsBuilder();
		Options options = optionsBuilder.getOptions();
		CommandLine cl = null;

		try {
			cl = new PosixParser().parse(options, arguments);

		} catch (ParseException parseEx) {
			System.out.println("Incorrect arguments (listed below) - application will terminate.");
			System.out.println(parseEx.getMessage());
			return -5;
		}

		if (cl.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(screenWidth);

			System.out.println("Usage:");
			System.out.println("launchClient <SIBUSHelper application> -CCpropfile=<configFile> [-CC<name>=<value>] [app args]");
			System.out.println("where -CC<name>=<value> are the client container (launchClient) name-value pair arguments app args are application client arguments.");
			System.out.println(StringUtils.EMPTY);
			System.out.println("Sample usage:");
			System.out.println("launchClient.sh <SIBUSHelper application> --configFile=/was_app/config/sibus/sibus.properties --action=REPORT");
			System.out.println(StringUtils.EMPTY);
			System.out.println("All filter by attributes might in certain circumstances be empty. "	+ "If this occurs you might make use of the --sessionIdWildCard parameter");
			formatter.printHelp("SIBUSHelper", options);
			return 0;
		}

		if (!cl.hasOption(CommandOptions.configFile)) {
			logger.log(Level.WARNING, Constants.METHOD_ERROR + "Property file not spesified.");
			return -6;
		}

		String propertyFileName = cl.getOptionValue(CommandOptions.configFile);
		File propertyFile = new File(propertyFileName);
		if (!propertyFile.exists()) {
			logger.log(Level.WARNING, Constants.METHOD_ERROR + "The property file does not exist ("
					+ cl.getOptionValue(CommandOptions.configFile) + ")");
			return -6;
		}

		ArgumentValidator argumentValidator = new ArgumentValidator();
		List validatedArguments = argumentValidator.validate(cl);
		if (!validatedArguments.isEmpty()) {
			for (Iterator argumentIter = validatedArguments.iterator(); argumentIter.hasNext();) {
				logger.log(Level.WARNING, (String) argumentIter.next());
			}
			logger.log(Level.WARNING, "Exiting due to insufficient number of arguments. See details listed above");
			return -7;
		}
		
		// Do XOR encryption of the password in present in the propfile
		PasswordEncodeDelegate encoder = new PasswordEncodeDelegate();
		encoder.encodePassword(propertyFile);

		// It is more or less okay to throw these exceptions from
		// this method as we have give the user a log entry about
		// this and simultainisly abort the program before this
		// will occur.
		FileInputStream in;
		Properties connectProps = new Properties();
		try {
			in = new FileInputStream(propertyFile);
			connectProps.load(in);
			in.close();
		} catch (FileNotFoundException e1) {} 
		  catch (IOException e) {}
		
		
		// Log warnings from property validation if any violations and exit
		PropertyUtil propertyUtil = new PropertyUtil();
		List validatedProperties = propertyUtil.validateProperties(connectProps);
		if (!validatedProperties.isEmpty()) {
			for (Iterator propertyIter = validatedProperties.iterator(); propertyIter.hasNext();) {
				logger.log(Level.WARNING, (String) propertyIter.next());
			}
			logger.log(Level.SEVERE, "SIBUS Helper terminating due to missing or invalid parameters in configuration file");
			System.exit(-8);
		}


		// get the command line options more sorted
		String sibusAction = cl.getOptionValue(CommandOptions.action);
        String sibusComponent = cl.getOptionValue(CommandOptions.component);

        // get options for REPORT, SUBMIT, DELETE
        String[] option = cl.getArgs();
        String argsibus ="";
        String argqueue ="";
        String argfilter = Constants.ARG_FILTER;
        if (option.length != 0)
        {
        	// PATTERN SIBUS:QUEUE:FILTER
        	StringTokenizer st = new StringTokenizer(option[0], Constants.ARG_DELIMITER);
        	int i=0;
        	while (st.hasMoreTokens()) {
				
        		if(i==0)
        			argsibus = st.nextToken();
        		
        		if (i==1)
        			argqueue = st.nextToken();

        		if (i==2)
        		{	
        			argfilter = st.nextToken();
        			break;
        		}	

        		i++;
			}
		}

		
		/*
		 * ACTIONS:REPORT,STATUS,RESUBMIT,DELETE
		 */
        try
        {
        	overallConf = new Configuration(propertyFile);
            serverConf = overallConf.getServer();
            if (serverConf != null)
            	logger.log(Level.INFO, "use configuration options: " + serverConf.toString());
            	
        	String host = serverConf.getMessagingHostName().equals("") ? serverConf.getServerHostName() : serverConf.getMessagingHostName();
            logger.log(Level.WARNING, "ACTION: " + sibusAction +  " COMPONENT: " + sibusComponent + " SIBUS: " +  argsibus + " QUEUE: " + argqueue + " FILTER: " + argfilter);

            //instance of admin helper
            AdminHelper adminHelper = new AdminHelper();

            //depending if the security is enabled
            if(serverConf.isSecurityEnabled())
                adminHelper.connect(serverConf.getServerHostName(), serverConf.getServerPort(), serverConf.getProtocol(), serverConf.getUserName(), serverConf.getPassword(), serverConf.getTrustStoreLocation(), serverConf.getTrustStorePassword(), serverConf.getKeyStoreLocation(), serverConf.getKeyStorePassword());
            else
                adminHelper.connect(serverConf.getServerHostName(), serverConf.getServerPort(), serverConf.getProtocol(), null, null, null, null, null, null);

            // STATUS:SERVER
            if (sibusAction.equals(Constants.ACTION_STATUS) && sibusComponent.equals(Constants.HELPER_COMPONENT_SERVER))
            {	
                ServerInfo[] servers = adminHelper.getServersInfo();
                System.out.println();
                for(int x = 0; x < servers.length; x++)
                {
                	ServerInfo si = servers[x];
                	int fill = si.getServerName().length();
                	System.out.println(getSeparatorLine(30) + " " + si.getServerName() + " " + getSeparatorLine(80-fill));
                	System.out.println("  PID(" + si.getServerPid() + ")");
                	System.out.println("  VERSION(" + adminHelper.getServerProductDetails(si) + ")");
                	System.out.println("  STATUS(" + "RUNNING" + ")");
                	System.out.println();
                }
            }

            // STATUS:BUS
            if (sibusAction.equals(Constants.ACTION_STATUS) && sibusComponent.equals(Constants.HELPER_COMPONENT_BUS))
            {	
            	MEInfo mesInfo[] = adminHelper.getMessagingEngines();
                System.out.println();
                for(int x = 0; x < mesInfo.length; x++)
                {
                    BusInfo busInfo = adminHelper.getBusInfo(mesInfo[x].getBus());
                	int fill = busInfo.getName().length();
                	System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80-fill));
                    System.out.println("  CLUSTER(" +mesInfo[x].getCluster() + ")");
                    System.out.println("  NODE(" +mesInfo[x].getNode() + ")");
                    System.out.println("  ENGINE(" +mesInfo[x].getName() + ")");
                    System.out.println("  STATE(" +mesInfo[x].getState() + ")");
                    System.out.println("  SECURE(" + busInfo.getSecure() + ")");
                    System.out.println("  RELOADENABLED(" + busInfo.getConfigReload() + ")");
                	System.out.println("  DESCRIPTION(" + busInfo.getDescription() + ")");
                    System.out.println();
                }
            }

            //STATUS:WMQLINK
            if (sibusAction.equals(Constants.ACTION_STATUS) && sibusComponent.equals(Constants.HELPER_COMPONENT_MQLINK))
            {	
            	MEInfo mesInfo[] = adminHelper.getMessagingEngines();
                System.out.println();
                for(int x = 0; x < mesInfo.length; x++)
                {
                    BusInfo busInfo = adminHelper.getBusInfo(mesInfo[x].getBus());
                	int fill = busInfo.getName().length();

                    MQLinkInfo[] mqlinks = adminHelper.getMQLinks(mesInfo[x]);
                    for(int y = 0; y < mqlinks.length; y++)
                    {
                    	if (mqlinks[y] != null)
                    	{
                        	System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80-fill));
                    		MQLinkInfo mqlink = mqlinks[y];
                    		System.out.println("  WMQLINK(" + mqlink.getName() + ")");
                    		System.out.println("   QMGR(" + mqlink.getQueueManagerName() + ")");
                    		System.out.println("   STATUS(" + mqlink.getStatus() + ")");
                    		
                        	// sender channel
                    		Object objsend = adminHelper.getMQLinkChannelInfo(mqlink, "getCurrentStatus", true);
                    		if (objsend != null)
                    		{
                    			MQLinkSenderChannelInfo sender = new MQLinkSenderChannelInfo((SIBMQLinkSenderCurrentStatus) objsend);
                    			System.out.println("   SENDER CHANNEL STATUS(" + sender.getStatus() + ")");
                    			System.out.println("     IP(" + sender.getIpAddress() + ")");
                    			System.out.println("     IN DOUBT(" + sender.getInDoubt() + ")");
                    			System.out.println("     REMAININIG SHORT RETRY STARTS(" + sender.getRemainingShortRetryStarts() + ")");
                    			System.out.println("     CHANNEL START TIME(" + adminHelper.getTimestamp(sender.getChannelStartTimeMillis(), Constants.DEFAULT_DATE_FORMAT_TZ) + ")");
                    			System.out.println("     LAST MESSAGE SEND(" + adminHelper.getTimestamp(sender.getLastMessageSendTimeMillis(), Constants.DEFAULT_DATE_FORMAT_TZ) + ")");
                    			System.out.println("     CURRENT SEQ NUMBER(" + sender.getCurrentSequenceNumber() + ")");
                    			System.out.println("     STOP REQUESTED(" + sender.getStopRequested() + ")");
                    			
                    		}
                    		else
                    		{
                    			System.out.println("   SENDER CHANNEL STATUS(" + "STOPPED" + ")");                    			
                    		}
                    		
                        	// receiver channel
                    		Object objrecv = adminHelper.getMQLinkChannelInfo(mqlink, "getCurrentStatus", false);
                        	if (objrecv!= null)
                        	{
                        		ArrayList<Object> list = (ArrayList<Object>) objrecv;
                        		if (!list.isEmpty())
                        		{	
                        			MQLinkReceiverChannelInfo receiver = new MQLinkReceiverChannelInfo((SIBMQLinkReceiverCurrentStatus)list.get(0));
                        			System.out.println("   RECEIVER CHANNEL STATUS(" +  receiver.getStatus() + ")");
                        			System.out.println("     IP(" + receiver.getIpAddress() + ")");
                        			System.out.println("     CHANNEL START TIME(" + adminHelper.getTimestamp(receiver.getChannelStartTimeMillis(), Constants.DEFAULT_DATE_FORMAT_TZ) + ")");
                        			System.out.println("     LAST MESSAGE SEND(" + adminHelper.getTimestamp(receiver.getLastMessageSendTimeMillis(), Constants.DEFAULT_DATE_FORMAT_TZ) + ")");
                        			System.out.println("     CURRENT SEQ NUMBER(" + receiver.getCurrentSequenceNumber() + ")");
                        			System.out.println("     STOP REQUESTED(" + receiver.getStopRequested() + ")");
                        		}
                        		else
                        		{
                        			System.out.println("   RECEIVER CHANNEL STATUS(" + "STOPPED" + ")");                        	
                    			}
                    		} 
                    		else
                    		{
                    			System.out.println("   RECEIVER CHANNEL STATUS(" + "STOPPED" + ")");
                		}                    		
                    		
                    	} //if mqlinks
                   	} // for mqlinks
                } // for buses
            } //status wmqlink
            
            //STATUS:QUEUE (ALL)
            if (sibusAction.equals(Constants.ACTION_STATUS) && sibusComponent.equals(Constants.HELPER_COMPONENT_QUEUE))
            {	
            	MEInfo mesInfo[] = adminHelper.getMessagingEngines();
                System.out.println();
                for(int x = 0; x < mesInfo.length; x++)
                {
                    BusInfo busInfo = adminHelper.getBusInfo(mesInfo[x].getBus());
                    MEInfo meInfo = mesInfo[x];
                	int fill = busInfo.getName().length();
                	
                   	
                	//ALL
                	if (argsibus.equalsIgnoreCase(Constants.ARG_FILTER))
                	{
                    	System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80-fill));
                    	logger.log(Level.INFO, "Collecting data for SIBUS...Please wait can take time!");
                    	
                        List destlist = adminHelper.getDestinations(busInfo.getName());
                        
                        if ( destlist.isEmpty())
                        {
                        	System.out.println(" DESTINATION(" + "NONE" + ")");
                        }
                        else
                        {
                            Iterator iter = destlist.iterator();
                            while (iter.hasNext()) {
                            	DestinationInfo dest = (DestinationInfo) iter.next();
        						//System.out.println(que.toString());
        	                	System.out.println(" DESTINATION(" + dest.getDestinationName() + ")");
        	                    System.out.println("  TYPE(" + dest.getType() + ")");
        	                    System.out.println("  DESCRIPTION(" + dest.getDescription() + ")");
        	                    System.out.println("  TARGET NAME(" + dest.getTargetName() + ")");
        	                    System.out.println("  TARGET BUS(" + dest.getTargetBus() + ")");
        	                    
        	                    String qinfo[] = dest.getQueuePoints();
        	                    for(int q = 0; q < qinfo.length; q++)
        	                    {
        	                    	String queue = qinfo[q];
        	                    	QueuePointInfo qpi = adminHelper.getQueuePoints(meInfo.getName(), queue);
        	                    	System.out.println("  QUEUEPOINT(" + qpi.getId() + ")");
        	                    	System.out.println("   STATE(" + qpi.getState() + ")");
        	                    	System.out.println("   CURRENT DEPTH(" + qpi.getCurrentDepth() + ")");
            	                    System.out.println("   HIGH MESSAGE THRESHOLD(" + qpi.getHighMessageThreshold() + ")");
            	                    System.out.println("   SEND ALLOWED(" + qpi.getSendAllowed() + ")");

        	                    }
        	                    System.out.println();
                            }
                        }
                        System.out.println(); 
                	}
                    // JUST THE SI BUS
                    else if (busInfo.getName().equalsIgnoreCase(argsibus))
                    {
                    	System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80-fill));
                    	logger.log(Level.INFO, "Collecting data for SIBUS...Please wait can take time!");
                    	
                        List destlist = adminHelper.getDestinations(busInfo.getName());
                        
                        if ( destlist.isEmpty())
                        {
                        	System.out.println(" DESTINATION(" + "NONE" + ")");
                        }
                        else
                        {
                            Iterator iter = destlist.iterator();
                            while (iter.hasNext()) {
                            	DestinationInfo dest = (DestinationInfo) iter.next();
        						//System.out.println(que.toString());
        	                	System.out.println(" DESTINATION(" + dest.getDestinationName() + ")");
        	                    System.out.println("  TYPE(" + dest.getType() + ")");
        	                    System.out.println("  DESCRIPTION(" + dest.getDescription() + ")");
        	                    System.out.println("  TARGET NAME(" + dest.getTargetName() + ")");
        	                    System.out.println("  TARGET BUS(" + dest.getTargetBus() + ")");

        	                    
        	                    String qinfo[] = dest.getQueuePoints();
        	                    for(int q = 0; q < qinfo.length; q++)
        	                    {
        	                    	String queue = qinfo[q];
        	                    	QueuePointInfo qpi = adminHelper.getQueuePoints(meInfo.getName(), queue);
        	                    	System.out.println("  QUEUEPOINT(" + qpi.getId() + ")");
        	                    	System.out.println("   STATE(" + qpi.getState() + ")");
        	                    	System.out.println("   CURRENT DEPTH(" + qpi.getCurrentDepth() + ")");
            	                    System.out.println("   HIGH MESSAGE THRESHOLD(" + qpi.getHighMessageThreshold() + ")");
            	                    System.out.println("   SEND ALLOWED(" + qpi.getSendAllowed() + ")");

        	                    }
        	                    System.out.println();
                            }
                        }
                        System.out.println(); 
                    }
                }
            }

            //REPORT:DESTINATION
            if (sibusAction.equals(Constants.ACTION_REPORT) && sibusComponent.equals(Constants.HELPER_COMPONENT_DESTINATION) && !argqueue.equals("SE"))
            {	
    			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
    			Date currentTime = GregorianCalendar.getInstance().getTime();
    			String filename = Constants.FILE_PREFIX + "_" + sibusAction + "_" + sibusComponent.toUpperCase()+ "_" + sdf.format(currentTime);
    			String path = cl.getOptionValue(CommandOptions.reportDirectory);

    			// Create file writer instances
        		logger.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the messages.");
       			MessageFileWriter fileWriter = new MessageFileWriter(path, filename + ".csv");

       			MEInfo mesInfo[] = adminHelper.getMessagingEngines();
       			
       			System.out.println();
       			
       			for(int x = 0; x < mesInfo.length; x++)
                {
                    BusInfo busInfo = adminHelper.getBusInfo(mesInfo[x].getBus());
                    MEInfo meInfo = mesInfo[x];
                	int fill = busInfo.getName().length();

                	
                	//ALL
                	if (argsibus.equalsIgnoreCase(Constants.ARG_FILTER))
                	{
                		
                		System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80-fill));
                		MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
                		List list = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), Constants.SE_QUEUE+meInfo.getName());

                		if (!list.isEmpty())
                		{	
                			logger.log(Level.FINE, "Writing messages to file...Please wait");
                			fileWriter.writeHeader();
                    		Iterator iter = list.iterator();
                    		while (iter.hasNext()) {
    							MessageInfo element = (MessageInfo) iter.next();
    							fileWriter.writeCSVMessage(element, Constants.SE_QUEUE+meInfo.getName());
    						}
                		}	
                		
                		System.out.println(" EXCEPTIONPOINT(" + Constants.SE_QUEUE+meInfo.getName() + ")");
                		System.out.println("  MESSAGE COUNT(" +  list.size() + ")");
                		System.out.println("  BROWSED(" +  "DONE" + ")");
                		System.out.println();
                	}
                	//JUST FOR THE SIBUS
                	else if (busInfo.getName().equalsIgnoreCase(argsibus))
                	{
                		System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80-fill));
                		MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
                		List list = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), Constants.SE_QUEUE+meInfo.getName());

                		logger.log(Level.FINE, "Writing messages to file...Please wait");
                		fileWriter.writeHeader();
                		Iterator iter = list.iterator();
                		while (iter.hasNext()) {
							MessageInfo element = (MessageInfo) iter.next();
							fileWriter.writeCSVMessage(element, Constants.SE_QUEUE+meInfo.getName());
						}
                		
                		System.out.println(" EXCEPTIONPOINT(" + Constants.SE_QUEUE+meInfo.getName() + ")");
                		System.out.println("  MESSAGE COUNT(" +  list.size() + ")");
                		System.out.println("  BROWSED(" +  "DONE" + ")");
                		System.out.println();
                	}
                }  
                
                if (fileWriter != null)
                	fileWriter.close();
            }

            //REPORT:SE
            if (sibusAction.equals(Constants.ACTION_REPORT) && sibusComponent.equals(Constants.HELPER_COMPONENT_QUEUE) && argqueue.equals("SE"))
            {	
    			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
    			Date currentTime = GregorianCalendar.getInstance().getTime();
    			String filename = Constants.FILE_PREFIX + "_" + sibusAction + "_" + sibusComponent.toUpperCase()+ "_" + argqueue + "_" + sdf.format(currentTime);
    			String path = cl.getOptionValue(CommandOptions.reportDirectory);

    			// Create file writer instances
        		logger.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the messages.");
       			MessageFileWriter fileWriter = new MessageFileWriter(path, filename + ".csv");

       			MEInfo mesInfo[] = adminHelper.getMessagingEngines();
       			
       			System.out.println();
       			
       			for(int x = 0; x < mesInfo.length; x++)
                {
                    BusInfo busInfo = adminHelper.getBusInfo(mesInfo[x].getBus());
                    MEInfo meInfo = mesInfo[x];
                	int fill = busInfo.getName().length();

                	
                	//ALL
                	if (argsibus.equalsIgnoreCase(Constants.ARG_FILTER))
                	{
                		
                		System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80-fill));
                		MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
                		List list = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), Constants.SE_QUEUE+meInfo.getName());

                		if (!list.isEmpty())
                		{	
                			logger.log(Level.FINE, "Writing messages to file...Please wait");
                			fileWriter.writeHeader();
                    		Iterator iter = list.iterator();
                    		while (iter.hasNext()) {
    							MessageInfo element = (MessageInfo) iter.next();
    							fileWriter.writeCSVMessage(element, Constants.SE_QUEUE+meInfo.getName());
    						}
                		}	
                		
                		System.out.println(" EXCEPTIONPOINT(" + Constants.SE_QUEUE+meInfo.getName() + ")");
                		System.out.println("  MESSAGE COUNT(" +  list.size() + ")");
                		System.out.println("  BROWSED(" +  "DONE" + ")");
                		System.out.println();
                	}
                	//JUST FOR THE SIBUS
                	else if (busInfo.getName().equalsIgnoreCase(argsibus))
                	{
                		System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80-fill));
                		MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
                		List list = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), Constants.SE_QUEUE+meInfo.getName());

                		logger.log(Level.FINE, "Writing messages to file...Please wait");
                		fileWriter.writeHeader();
                		Iterator iter = list.iterator();
                		while (iter.hasNext()) {
							MessageInfo element = (MessageInfo) iter.next();
							fileWriter.writeCSVMessage(element, Constants.SE_QUEUE+meInfo.getName());
						}
                		
                		System.out.println(" EXCEPTIONPOINT(" + Constants.SE_QUEUE+meInfo.getName() + ")");
                		System.out.println("  MESSAGE COUNT(" +  list.size() + ")");
                		System.out.println("  BROWSED(" +  "DONE" + ")");
                		System.out.println();
                	}
                }  
                
                if (fileWriter != null)
                	fileWriter.close();
            }
            
            //REPORT: JUST FOR A QUEUE
            if (sibusAction.equals(Constants.ACTION_REPORT) && sibusComponent.equals(Constants.HELPER_COMPONENT_QUEUE) && !argqueue.equals("SE"))
            {	
            	
    			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
    			Date currentTime = GregorianCalendar.getInstance().getTime();
    			String filename = Constants.FILE_PREFIX + "_" + sibusAction + "_" + sibusComponent.toUpperCase() + "_" + sdf.format(currentTime);
    			String path = cl.getOptionValue(CommandOptions.reportDirectory);

    			// Create file writer instances
        		logger.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the messages.");
       			MessageFileWriter fileWriter = new MessageFileWriter(path, filename + ".csv");

            	MEInfo mesInfo[] = adminHelper.getMessagingEngines();
                for(int x = 0; x < mesInfo.length; x++)
                {
                    BusInfo busInfo = adminHelper.getBusInfo(mesInfo[x].getBus());
                    MEInfo meInfo = mesInfo[x];
                	int fill = busInfo.getName().length();
                	
                	if (busInfo.getName().equalsIgnoreCase(argsibus))
                	{	
                		System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80-fill));
                		MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
                		
                		//all messages
                		if (argfilter.equals(Constants.ARG_FILTER))
                		{
                    		List list = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), argqueue);

                    		logger.log(Level.FINE, "Writing messages to file...Please wait");
                    		fileWriter.writeHeader();
                    		Iterator iter = list.iterator();
                    		while (iter.hasNext()) {
    							MessageInfo element = (MessageInfo) iter.next();
    							fileWriter.writeCSVMessage(element, argqueue);
    						}

                    		System.out.println(" QUEUEPOINT(" + argqueue + ")");
                    		System.out.println("  MESSAGE COUNT(" +  list.size() + ")");
                    		System.out.println("  BROWSED(" +  "DONE" + ")");
                    		System.out.println();
                		}
                		else
                		{
                    		MessageInfo element = messagingHelper.browseSingleMessage(busInfo.getName(), meInfo.getName(), argqueue, argfilter);
                    		logger.log(Level.FINE, "Writing messages to file...Please wait");
                    		
                    		fileWriter.writeHeader();
                    		
                    		if (element != null)
                    		{
       							fileWriter.writeCSVMessage(element, argqueue);
                        		System.out.println(" QUEUEPOINT(" + argqueue + ")");
                        		System.out.println("  MESSAGE FILTER(" +  argfilter + ")");
                        		System.out.println("  MESSAGE COUNT(" +  "1" + ")");
                        		System.out.println("  BROWSED(" +  element.getSystemMessageId() + ")");
                        		System.out.println();
                    		}
                    		else
                    		{
                        		System.out.println(" QUEUEPOINT(" + argqueue + ")");
                        		System.out.println("  MESSAGE FILTER(" +  argfilter + ")");
                        		System.out.println("  MESSAGE COUNT(" +  "0" + ")");
                        		System.out.println("  BROWSED(" +  "NONE" + ")");
                        		System.out.println();
                    		}
                		}
                	}	
                }
                
        		// Close writers. (The close() method handles if the writer allready have been closed)
        		if (null != filename) {
        			fileWriter.close();
        		}
            }
            
            //CLEAN:QUEUE
            if (sibusAction.equals(Constants.ACTION_DISCARD) && sibusComponent.equals(Constants.HELPER_COMPONENT_QUEUE))
            {	
            	MEInfo mesInfo[] = adminHelper.getMessagingEngines();
                for(int x = 0; x < mesInfo.length; x++)
                {
                    BusInfo busInfo = adminHelper.getBusInfo(mesInfo[x].getBus());
                    MEInfo meInfo = mesInfo[x];
                	int fill = busInfo.getName().length();
                	System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80-fill));
                	
                	//TODO -> bus name, queue 
                	if (busInfo.getName().equalsIgnoreCase("SCA.APPLICATION.T6Cell.Bus"))
                     {	

                         MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
                		 int toDelete = messagingHelper.queueMessages(busInfo.getName(), meInfo.getName(), "LARS"); 
                		 // Check if the commandline has a --noStop option
             			if (!cl.hasOption(CommandOptions.noStop)) {
             				String q = "Do you want to continue and purge " + toDelete + " messages (y/n)?";
             				boolean result = askYesNo(q);
             				if (!result) {
             					return 0;
             				}
             				int deleted = messagingHelper.clearQueue(busInfo.getName(), meInfo.getName(), "LARS");
                        	System.out.println(" QUEUEPOINT(" + "LARS" + ")");
                            System.out.println("  MESSAGE COUNT(" +  toDelete + ")");
                            System.out.println("  MESSAGE PURGED(" +  deleted + ")");
                            System.out.println();                    
             			}
                     }	
                }
            }
            
        }
        catch(Exception e)
        {
            System.out.println(Constants.METHOD_ERROR);
            e.printStackTrace();
            return -1;
        }
        return 0;
	}
	
	/**
	 * 	@param width
	 * @return
	 */
	private static String getSeparatorLine(int width) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width; i++) {
			sb.append("=");
		}
		return sb.toString();
	}	

	/**
	 * @param question
	 * @return
	 */
	private boolean askYesNo(String question) {
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
