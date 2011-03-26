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
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.portlet.filter.ActionFilter;

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
import no.nav.sibushelper.helper.DestinationNotFoundException;
import no.nav.sibushelper.helper.MEInfo;
import no.nav.sibushelper.helper.MQLinkInfo;
import no.nav.sibushelper.helper.MQLinkReceiverChannelInfo;
import no.nav.sibushelper.helper.MQLinkSenderChannelInfo;
import no.nav.sibushelper.helper.MediationPointInfo;
import no.nav.sibushelper.helper.MessageInfo;
import no.nav.sibushelper.helper.MessagingHelper;
import no.nav.sibushelper.helper.MessagingHelperImpl;
import no.nav.sibushelper.helper.MessagingOperationFailedException;
import no.nav.sibushelper.helper.QueuePointInfo;
import no.nav.sibushelper.helper.ServerConfigurationProperties;
import no.nav.sibushelper.helper.ServerInfo;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;

import com.ibm.websphere.management.exception.ConfigServiceException;
import com.ibm.websphere.management.exception.ConnectorException;
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
	private CommandLine commandLine = null;

	private String sibusAction;
	private String sibusComponent;
	private String argsibus = Constants.ARG_FILTER;
	private String argqueue = Constants.ARG_FILTER;
	private String argqueue1 = Constants.ARG_FILTER;
	private String argfilter = Constants.ARG_FILTER;

	private AdminHelper adminHelper;
	private String host;

	/**
	 * @param args
	 */
	public SIBUSHelper(String args[]) {
		overallConf = null;
		serverName = null;
		confFileName = null;
		arguments = null;
		serverConf = null;
		sibusAction = null;
		sibusComponent = null;
		host = null;
		adminHelper = null;
		commandLine = null;
		arguments = args;
	}

	@SuppressWarnings("unchecked")
	public int invokeHelper() {
		System.out.println();
		System.out.println(getSeparatorLine(112));
		System.out.println(" SIBUS Helper for WPS 7 (c) IBM Corp., 1997-2011");
		System.out.println(getSeparatorLine(112));

		CommandOptionsBuilder optionsBuilder = new CommandOptionsBuilder();
		Options options = optionsBuilder.getOptions();

		try {
			commandLine = new PosixParser().parse(options, arguments);

		} catch (ParseException parseEx) {
			System.out.println("Incorrect arguments (listed below) - application will terminate.");
			System.out.println(parseEx.getMessage());
			return -5;
		}

		if (commandLine.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(screenWidth);

			System.out.println("Usage:");
			System.out
					.println("launchClient <SIBUSHelper application> -CCpropfile=<configFile> [-CC<name>=<value>] [app args]");
			System.out
					.println("where -CC<name>=<value> are the client container (launchClient) name-value pair arguments app args are application client arguments.");
			System.out.println(StringUtils.EMPTY);
			System.out.println("Sample usage:");
			System.out
					.println("launchClient.sh <SIBUSHelper application> --configFile=/was_app/config/sibus/sibus.properties --action=REPORT --component=queue [select options]");
			System.out.println(StringUtils.EMPTY);
			System.out.println("Filter by API message id only supported.");
			formatter.printHelp("SIBUSHelper", options);
			return 0;
		}

		if (!commandLine.hasOption(CommandOptions.configFile)) {
			logger.log(Level.SEVERE, Constants.METHOD_ERROR + "Property file not spesified.");
			return -6;
		}

		String propertyFileName = commandLine.getOptionValue(CommandOptions.configFile);
		File propertyFile = new File(propertyFileName);
		if (!propertyFile.exists()) {
			logger.log(Level.SEVERE, Constants.METHOD_ERROR + "The property file does not exist ("
					+ commandLine.getOptionValue(CommandOptions.configFile) + ")");
			return -6;
		}

		ArgumentValidator argumentValidator = new ArgumentValidator();
		List validatedArguments = argumentValidator.validate(commandLine);
		if (!validatedArguments.isEmpty()) {
			for (Iterator argumentIter = validatedArguments.iterator(); argumentIter.hasNext();) {
				logger.log(Level.WARNING, (String) argumentIter.next());
			}
			logger.log(Level.SEVERE, "Exiting due to insufficient number of arguments. See details listed above");
			return -7;
		}

		// should not come an exception
		FileInputStream in;
		Properties connectProps = new Properties();
		try {
			in = new FileInputStream(propertyFile);
			connectProps.load(in);
			in.close();
		} catch (FileNotFoundException e1) {
		} catch (IOException e) {
		}

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
		sibusAction = commandLine.getOptionValue(CommandOptions.action);
		sibusComponent = commandLine.getOptionValue(CommandOptions.component);

		// get options for REPORT, SUBMIT, DELETE
		String[] option = commandLine.getArgs();
		if (option.length != 0) {
			// PATTERN SIBUS:QUEUE,QUEUE:FILTER
			StringTokenizer st = new StringTokenizer(option[0], Constants.ARG_DELIMITER);
			int i = 0;
			while (st.hasMoreTokens()) {

				if (i == 0) {
					argsibus = st.nextToken();
				}

				if (i == 1) {
					argqueue = st.nextToken();

					if (argqueue.indexOf(Constants.ARG_QUEUE_DEL) != -1) {
						StringTokenizer stqd = new StringTokenizer(argqueue, Constants.ARG_QUEUE_DEL);
						int l = 0;
						while (stqd.hasMoreTokens()) {
							if (l == 0) {
								argqueue = stqd.nextToken();
							}

							if (l == 1) {
								argqueue1 = stqd.nextToken();
							}

							l++;
						}
					}
				}

				if (i == 2) {
					argfilter = st.nextToken();
					break;
				}

				i++;
			}
		}

		try {
			boolean isAction = false;

			overallConf = new Configuration(propertyFile);
			serverConf = overallConf.getServer();
			if (serverConf != null) {
				logger.log(Level.INFO, "use configuration options: " + serverConf.toString());
			}

			host = serverConf.getMessagingHostName().equals("") ? serverConf.getServerHostName() : serverConf
					.getMessagingHostName();
			logger.log(Level.INFO, "ACTION: " + sibusAction + " COMPONENT: " + sibusComponent + " SIBUS: " + argsibus
					+ " QUEUE: " + argqueue + " FILTER: " + argfilter);

			// instance of admin helper
			adminHelper = new AdminHelper();

			// depending if the security is enabled
			if (serverConf.isSecurityEnabled()) {
				adminHelper.connect(serverConf.getServerHostName(), serverConf.getServerPort(), serverConf.getProtocol(),
						serverConf.getUserName(), serverConf.getPassword(), serverConf.getTrustStoreLocation(), serverConf
								.getTrustStorePassword(), serverConf.getKeyStoreLocation(), serverConf.getKeyStorePassword());
			} else {
				adminHelper.connect(serverConf.getServerHostName(), serverConf.getServerPort(), serverConf.getProtocol(), null,
						null, null, null, null, null);
			}

			// STATUS:SERVER
			if (sibusAction.equals(Constants.ACTION_STATUS) && sibusComponent.equals(Constants.HELPER_COMPONENT_SERVER)) {
				isAction = true;
				actionStatusServer();
			}

			// STATUS:SIBUS
			if (sibusAction.equals(Constants.ACTION_STATUS) && sibusComponent.equals(Constants.HELPER_COMPONENT_BUS)) {
				isAction = true;
				actionStatusSibus();
			}

			// STATUS:WMQLINK
			if (sibusAction.equals(Constants.ACTION_STATUS) && sibusComponent.equals(Constants.HELPER_COMPONENT_MQLINK)) {
				isAction = true;
				actionStatusWmqlink();
			}

			// STATUS:QUEUE
			if (sibusAction.equals(Constants.ACTION_STATUS) && sibusComponent.equals(Constants.HELPER_COMPONENT_QUEUE)) {
				isAction = true;
				if (argfilter.equals(Constants.ARG_FILTER)) {
					actionStatusQueue();
				} else {
					actionStatusQueueGreaterZero();
				}
			}

			// RESUBMIT:SE
			if (sibusAction.equals(Constants.ACTION_RESUBMIT) && sibusComponent.equals(Constants.HELPER_COMPONENT_QUEUE)
					&& argqueue.equals("SE")) {
				isAction = true;
				if (argsibus.equals(Constants.ARG_FILTER)) {
					logger.log(Level.SEVERE, "SIBUS Helper terminating due to missing argument SIBUS name!");
					return (-8);
				}
				actionResubmitSE();
			}

			// CLEAN:QUEUE
			if (sibusAction.equals(Constants.ACTION_DISCARD) && sibusComponent.equals(Constants.HELPER_COMPONENT_QUEUE)) {
				isAction = true;
				if (argsibus.equals(Constants.ARG_FILTER)) {
					logger.log(Level.SEVERE, "SIBUS Helper terminating due to missing argument SIBUS name!");
					return (-8);
				}
				if (argqueue.equals(Constants.ARG_FILTER)) {
					logger.log(Level.SEVERE, "SIBUS Helper terminating due to missing argument QUEUE name!");
					return (-8);
				}
				actionCleanQueue();
			}

			// MOVE:QUEUE
			if (sibusAction.equals(Constants.ACTION_MOVE) && sibusComponent.equals(Constants.HELPER_COMPONENT_QUEUE)) {
				isAction = true;
				if (argsibus.equals(Constants.ARG_FILTER)) {
					logger.log(Level.SEVERE, "SIBUS Helper terminating due to missing argument SIBUS name!");
					return (-8);
				}
				if (argqueue.equals(Constants.ARG_FILTER)) {
					logger.log(Level.SEVERE, "SIBUS Helper terminating due to missing argument SOURCE QUEUE name!");
					return (-8);
				}
				if (argqueue1.equals(Constants.ARG_FILTER)) {
					logger.log(Level.SEVERE, "SIBUS Helper terminating due to missing argument TARGET QUEUE name!");
					return (-8);
				}
				actionMove();
			}

			// REPORT:SE
			if (sibusAction.equals(Constants.ACTION_REPORT) && sibusComponent.equals(Constants.HELPER_COMPONENT_QUEUE)
					&& argqueue.equals("SE")) {
				isAction = true;
				actionReportSE();
			}

			// REPORT:QUEUE:[FILTER]
			if (sibusAction.equals(Constants.ACTION_REPORT) && sibusComponent.equals(Constants.HELPER_COMPONENT_QUEUE)
					&& !argqueue.equals("SE")) {
				isAction = true;
				if (argsibus.equals(Constants.ARG_FILTER) && !argqueue.equals(Constants.ARG_FILTER)) {
					logger.log(Level.SEVERE, "SIBUS Helper terminating due to missing argument SIBUS name!");
					return (-8);
				}
				actionReportQueue();
			}

			if (!isAction) {
				System.out.println();
				System.out.println(" None of the provided arguments triggered an action. Please validate command line option!");
			}

		} catch (Exception e) {
			System.out.println(Constants.METHOD_ERROR);
			e.printStackTrace();
			return -1;
		}

		System.out.println();
		System.out.println(getSeparatorLine(112));
		System.out.println(" SIBUS Helper - Done ");
		System.out.println(getSeparatorLine(112));

		return 0;
	}

	/**
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws NullPointerException
	 * @throws ConnectorException
	 * @throws ReflectionException
	 * @throws MBeanException
	 */
	private void actionStatusServer() throws MalformedObjectNameException, InstanceNotFoundException, NullPointerException,
			ConnectorException, ReflectionException, MBeanException {
		ServerInfo[] servers = adminHelper.getServersInfo();
		System.out.println();
		for (ServerInfo si : servers) {
			int fill = si.getServerName().length();
			System.out.println(getSeparatorLine(30) + " " + si.getServerName() + " " + getSeparatorLine(80 - fill));
			System.out.println("  PID(" + si.getServerPid() + ")");
			System.out.println("  VERSION(" + adminHelper.getServerProductDetails(si) + ")");
			System.out.println("  STATUS(" + "RUNNING" + ")");
			System.out.println();
		}
	}

	/**
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 * @throws ConfigServiceException
	 */
	private void actionStatusSibus() throws MalformedObjectNameException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException, ConfigServiceException {
		MEInfo mesInfo[] = adminHelper.getMessagingEngines();
		System.out.println();
		for (MEInfo element : mesInfo) {
			BusInfo busInfo = adminHelper.getBusInfo(element.getBus());
			int fill = busInfo.getName().length();
			System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
			System.out.println("  CLUSTER(" + element.getCluster() + ")");
			System.out.println("  NODE(" + element.getNode() + ")");
			System.out.println("  ENGINE(" + element.getName() + ")");
			System.out.println("  STATE(" + element.getState() + ")");
			System.out.println("  SECURE(" + busInfo.getSecure() + ")");
			System.out.println("  RELOADENABLED(" + busInfo.getConfigReload() + ")");
			System.out.println("  DESCRIPTION(" + busInfo.getDescription() + ")");
			System.out.println();
		}
	}

	/**
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 * @throws ConfigServiceException
	 */
	@SuppressWarnings("unchecked")
	private void actionStatusWmqlink() throws MalformedObjectNameException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException, ConfigServiceException {
		MEInfo mesInfo[] = adminHelper.getMessagingEngines();
		System.out.println();
		for (MEInfo element : mesInfo) {
			BusInfo busInfo = adminHelper.getBusInfo(element.getBus());
			int fill = busInfo.getName().length();

			MQLinkInfo[] mqlinks = adminHelper.getMQLinks(element);
			for (MQLinkInfo element0 : mqlinks) {
				if (element0 != null) {
					System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
					MQLinkInfo mqlink = element0;
					System.out.println("  WMQLINK(" + mqlink.getName() + ")");
					System.out.println("   QMGR(" + mqlink.getQueueManagerName() + ")");
					System.out.println("   STATUS(" + mqlink.getStatus() + ")");

					// sender channel
					Object objsend = adminHelper.getMQLinkChannelInfo(mqlink, "getCurrentStatus", true);
					if (objsend != null) {
						MQLinkSenderChannelInfo sender = new MQLinkSenderChannelInfo((SIBMQLinkSenderCurrentStatus) objsend);
						System.out.println("   SENDER CHANNEL STATUS(" + sender.getStatus() + ")");
						System.out.println("     IP(" + sender.getIpAddress() + ")");
						System.out.println("     IN DOUBT(" + sender.getInDoubt() + ")");
						System.out.println("     REMAININIG SHORT RETRY STARTS(" + sender.getRemainingShortRetryStarts() + ")");
						System.out.println("     CHANNEL START TIME("
								+ adminHelper
										.getTimestamp(sender.getChannelStartTimeMillis(), Constants.DEFAULT_DATE_FORMAT_TZ)
								+ ")");
						System.out.println("     LAST MESSAGE SEND("
								+ adminHelper.getTimestamp(sender.getLastMessageSendTimeMillis(),
										Constants.DEFAULT_DATE_FORMAT_TZ) + ")");
						System.out.println("     CURRENT SEQ NUMBER(" + sender.getCurrentSequenceNumber() + ")");
						System.out.println("     STOP REQUESTED(" + sender.getStopRequested() + ")");

					} else {
						System.out.println("   SENDER CHANNEL STATUS(" + "STOPPED" + ")");
					}

					// receiver channel
					Object objrecv = adminHelper.getMQLinkChannelInfo(mqlink, "getCurrentStatus", false);
					if (objrecv != null) {
						ArrayList<Object> list = (ArrayList<Object>) objrecv;
						if (!list.isEmpty()) {
							MQLinkReceiverChannelInfo receiver = new MQLinkReceiverChannelInfo(
									(SIBMQLinkReceiverCurrentStatus) list.get(0));
							System.out.println("   RECEIVER CHANNEL STATUS(" + receiver.getStatus() + ")");
							System.out.println("     IP(" + receiver.getIpAddress() + ")");
							System.out.println("     CHANNEL START TIME("
									+ adminHelper.getTimestamp(receiver.getChannelStartTimeMillis(),
											Constants.DEFAULT_DATE_FORMAT_TZ) + ")");
							System.out.println("     LAST MESSAGE SEND("
									+ adminHelper.getTimestamp(receiver.getLastMessageSendTimeMillis(),
											Constants.DEFAULT_DATE_FORMAT_TZ) + ")");
							System.out.println("     CURRENT SEQ NUMBER(" + receiver.getCurrentSequenceNumber() + ")");
							System.out.println("     STOP REQUESTED(" + receiver.getStopRequested() + ")");
						} else {
							System.out.println("   RECEIVER CHANNEL STATUS(" + "STOPPED" + ")");
						}
					} else {
						System.out.println("   RECEIVER CHANNEL STATUS(" + "STOPPED" + ")");
					}

				} // if mqlinks
			} // for mqlinks
		} // for buses
	}

	/**
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 * @throws ConfigServiceException
	 */
	private void actionStatusQueue() throws MalformedObjectNameException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException, ConfigServiceException {
		MEInfo mesInfo[] = adminHelper.getMessagingEngines();
		System.out.println();
		String queuename = null;
		for (MEInfo element : mesInfo) {
			BusInfo busInfo = adminHelper.getBusInfo(element.getBus());
			MEInfo meInfo = element;
			int fill = busInfo.getName().length();
			if (argqueue.equals("SE")) {
				queuename = Constants.SE_QUEUE + meInfo.getName();
			} else {
				queuename = argqueue;
			}

			// ALL
			if (argsibus.equalsIgnoreCase(Constants.ARG_FILTER)) {
				System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
				logger.log(Level.INFO, "Collecting data for SIBUS...Please wait can take time!");

				List<DestinationInfo> destList;
				if (argqueue.equals(Constants.ARG_FILTER)) {
					destList = adminHelper.getDestinations(busInfo.getName());
				} else {
					destList = adminHelper.getDestinations(busInfo.getName(), queuename);
				}

				if (destList.isEmpty() || destList == null) {
					System.out.println();
					System.out.println(" DESTINATION(" + "NONE" + ")");
				} else {
					Iterator iter = destList.iterator();
					while (iter.hasNext()) {
						DestinationInfo dest = (DestinationInfo) iter.next();
						System.out.println();
						System.out.println(" DESTINATION(" + dest.getDestinationName() + ")");
						System.out.println("  TYPE(" + dest.getType() + ")");
						System.out.println("  DESCRIPTION(" + dest.getDescription() + ")");
						System.out.println("  TARGET NAME(" + dest.getTargetName() + ")");
						System.out.println("  TARGET BUS(" + dest.getTargetBus() + ")");

						String qinfo[] = dest.getQueuePoints();
						for (String queue : qinfo) {
							QueuePointInfo qpi = adminHelper.getQueuePoints(meInfo.getName(), queue);
							System.out.println("  QUEUEPOINT(" + qpi.getId() + ")");
							System.out.println("   STATE(" + qpi.getState() + ")");
							System.out.println("   CURRENT DEPTH(" + qpi.getCurrentDepth() + ")");
							System.out.println("   HIGH MESSAGE THRESHOLD(" + qpi.getHighMessageThreshold() + ")");
							System.out.println("   SEND ALLOWED(" + qpi.getSendAllowed() + ")");

						}

						String minfo[] = dest.getMediationPoints();
						for (String medname : minfo) {
							MediationPointInfo mpi = adminHelper.getMediationPoints(meInfo.getName(), medname);
							System.out.println("  MEDIATIONPOINT(" + mpi.getId() + ")");
							System.out.println("   STATE(" + mpi.getState() + ")");
							System.out.println("   CURRENT DEPTH(" + mpi.getCurrentDepth() + ")");
							System.out.println("   HIGH MESSAGE THRESHOLD(" + mpi.getHighMessageThreshold() + ")");
							System.out.println("   SEND ALLOWED(" + mpi.getSendAllowed() + ")");
						}
					}
				}
				System.out.println();
			}
			// JUST THE SI BUS
			else if (busInfo.getName().equalsIgnoreCase(argsibus)) {
				System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
				logger.log(Level.INFO, "Collecting data for SIBUS...Please wait can take time!");

				List<DestinationInfo> destList;
				if (argqueue.equals(Constants.ARG_FILTER)) {
					destList = adminHelper.getDestinations(busInfo.getName());
				} else {
					destList = adminHelper.getDestinations(busInfo.getName(), queuename);
				}

				if (destList.isEmpty() || destList == null) {
					System.out.println();
					System.out.println(" DESTINATION(" + "NONE" + ")");
				} else {
					Iterator iter = destList.iterator();
					while (iter.hasNext()) {
						DestinationInfo dest = (DestinationInfo) iter.next();
						System.out.println();
						System.out.println(" DESTINATION(" + dest.getDestinationName() + ")");
						System.out.println("  TYPE(" + dest.getType() + ")");
						System.out.println("  DESCRIPTION(" + dest.getDescription() + ")");
						System.out.println("  TARGET NAME(" + dest.getTargetName() + ")");
						System.out.println("  TARGET BUS(" + dest.getTargetBus() + ")");

						String qinfo[] = dest.getQueuePoints();
						for (String queue : qinfo) {
							QueuePointInfo qpi = adminHelper.getQueuePoints(meInfo.getName(), queue);
							System.out.println("  QUEUEPOINT(" + qpi.getId() + ")");
							System.out.println("   STATE(" + qpi.getState() + ")");
							System.out.println("   CURRENT DEPTH(" + qpi.getCurrentDepth() + ")");
							System.out.println("   HIGH MESSAGE THRESHOLD(" + qpi.getHighMessageThreshold() + ")");
							System.out.println("   SEND ALLOWED(" + qpi.getSendAllowed() + ")");

						}

						String minfo[] = dest.getMediationPoints();
						for (String medname : minfo) {
							MediationPointInfo mpi = adminHelper.getMediationPoints(meInfo.getName(), medname);
							System.out.println("  MEDIATIONPOINT(" + mpi.getId() + ")");
							System.out.println("   STATE(" + mpi.getState() + ")");
							System.out.println("   CURRENT DEPTH(" + mpi.getCurrentDepth() + ")");
							System.out.println("   HIGH MESSAGE THRESHOLD(" + mpi.getHighMessageThreshold() + ")");
							System.out.println("   SEND ALLOWED(" + mpi.getSendAllowed() + ")");
						}
					}
				}
				System.out.println();
			}
		}
	}

	/**
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 * @throws ConfigServiceException
	 */
	private void actionStatusQueueGreaterZero() throws MalformedObjectNameException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException, ConfigServiceException {
		MEInfo mesInfo[] = adminHelper.getMessagingEngines();
		System.out.println();
		String queuename = null;
		boolean none = false;
		for (MEInfo element : mesInfo) {
			BusInfo busInfo = adminHelper.getBusInfo(element.getBus());
			MEInfo meInfo = element;
			int fill = busInfo.getName().length();
			if (argqueue.equals("SE")) {
				queuename = Constants.SE_QUEUE + meInfo.getName();
			} else {
				queuename = argqueue;
			}

			// ALL
			if (argsibus.equalsIgnoreCase(Constants.ARG_FILTER)) {
				System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
				logger.log(Level.INFO, "Collecting data for SIBUS...Please wait can take time!");

				List<DestinationInfo> destList;
				if (argqueue.equals(Constants.ARG_FILTER)) {
					destList = adminHelper.getDestinations(busInfo.getName());
				} else {
					destList = adminHelper.getDestinations(busInfo.getName(), queuename);
				}

				if (destList.isEmpty() || destList == null) {
					System.out.println();
					System.out.println("  DESTINATION(" + "NONE>0" + ")");
				} else {
					Iterator iter = destList.iterator();
					while (iter.hasNext()) {
						DestinationInfo dest = (DestinationInfo) iter.next();

						String qinfo[] = dest.getQueuePoints();
						for (String queue : qinfo) {
							QueuePointInfo qpi = adminHelper.getQueuePoints(meInfo.getName(), queue);

							Long qd = qpi.getCurrentDepth();

							if (qd != null && qd > 0) {
								none = true;
								System.out.println();
								System.out.println("  DESTINATION(" + dest.getDestinationName() + ")");
								System.out.println("   QUEUEPOINT(" + qpi.getId() + ")");
								System.out.println("   STATE(" + qpi.getState() + ")");
								System.out.println("   CURRENT DEPTH(" + qpi.getCurrentDepth() + ")");
								System.out.println("   HIGH MESSAGE THRESHOLD(" + qpi.getHighMessageThreshold() + ")");
								System.out.println("   SEND ALLOWED(" + qpi.getSendAllowed() + ")");
							}

						}

						String minfo[] = dest.getMediationPoints();
						for (String medname : minfo) {

							MediationPointInfo mpi = adminHelper.getMediationPoints(meInfo.getName(), medname);

							Long md = mpi.getCurrentDepth();

							if (md != null && md > 0) {
								none = true;
								System.out.println();
								System.out.println("  DESTINATION(" + dest.getDestinationName() + ")");
								System.out.println("   MEDIATIONPOINT(" + mpi.getId() + ")");
								System.out.println("   STATE(" + mpi.getState() + ")");
								System.out.println("   CURRENT DEPTH(" + mpi.getCurrentDepth() + ")");
								System.out.println("   HIGH MESSAGE THRESHOLD(" + mpi.getHighMessageThreshold() + ")");
								System.out.println("   SEND ALLOWED(" + mpi.getSendAllowed() + ")");
							}
						}
					}
					if (!none) {
						System.out.println();
						System.out.println("  DESTINATION(" + "NONE>0" + ")");
					}
				}
				System.out.println();
			}
			// JUST THE SI BUS
			else if (busInfo.getName().equalsIgnoreCase(argsibus)) {
				System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
				logger.log(Level.INFO, "Collecting data for SIBUS...Please wait can take time!");
				List<DestinationInfo> destList;
				if (argqueue.equals(Constants.ARG_FILTER)) {
					destList = adminHelper.getDestinations(busInfo.getName());
				} else {
					destList = adminHelper.getDestinations(busInfo.getName(), queuename);
				}

				if (destList.isEmpty() || destList == null) {
					System.out.println();
					System.out.println("  DESTINATION(" + "NONE>0" + ")");
				} else {
					Iterator iter = destList.iterator();
					while (iter.hasNext()) {
						DestinationInfo dest = (DestinationInfo) iter.next();

						String qinfo[] = dest.getQueuePoints();
						for (String queue : qinfo) {
							QueuePointInfo qpi = adminHelper.getQueuePoints(meInfo.getName(), queue);

							Long qd = qpi.getCurrentDepth();

							if (qd != null && qd > 0) {
								none = true;
								System.out.println();
								System.out.println("  DESTINATION(" + dest.getDestinationName() + ")");
								System.out.println("   QUEUEPOINT(" + qpi.getId() + ")");
								System.out.println("   STATE(" + qpi.getState() + ")");
								System.out.println("   CURRENT DEPTH(" + qpi.getCurrentDepth() + ")");
								System.out.println("   HIGH MESSAGE THRESHOLD(" + qpi.getHighMessageThreshold() + ")");
								System.out.println("   SEND ALLOWED(" + qpi.getSendAllowed() + ")");
							}

						}

						String minfo[] = dest.getMediationPoints();
						for (String medname : minfo) {

							MediationPointInfo mpi = adminHelper.getMediationPoints(meInfo.getName(), medname);

							Long md = mpi.getCurrentDepth();

							if (md != null && md > 0) {
								none = true;
								System.out.println();
								System.out.println("  DESTINATION(" + dest.getDestinationName() + ")");
								System.out.println("   MEDIATIONPOINT(" + mpi.getId() + ")");
								System.out.println("   STATE(" + mpi.getState() + ")");
								System.out.println("   CURRENT DEPTH(" + mpi.getCurrentDepth() + ")");
								System.out.println("   HIGH MESSAGE THRESHOLD(" + mpi.getHighMessageThreshold() + ")");
								System.out.println("   SEND ALLOWED(" + mpi.getSendAllowed() + ")");
							}
						}
					}

					if (!none) {
						System.out.println();
						System.out.println("  DESTINATION(" + "NONE>0" + ")");
					}
				}
				System.out.println();
			}
		}
	}

	/**
	 * @throws ConnectorException
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws MalformedObjectNameException
	 * @throws ConfigServiceException
	 * @throws DestinationNotFoundException
	 * @throws MessagingOperationFailedException
	 * 
	 */
	private long actionCleanQueue() throws MalformedObjectNameException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException, ConfigServiceException, MessagingOperationFailedException,
			DestinationNotFoundException {
		MEInfo mesInfo[] = adminHelper.getMessagingEngines();
		String queuename = null;
		for (MEInfo element0 : mesInfo) {
			BusInfo busInfo = adminHelper.getBusInfo(element0.getBus());
			MEInfo meInfo = element0;
			int fill = busInfo.getName().length();
			if (argqueue.equals("SE")) {
				queuename = Constants.SE_QUEUE + meInfo.getName();
			} else {
				queuename = argqueue;
			}

			if (busInfo.getName().equalsIgnoreCase(argsibus)) {
				System.out.println();
				System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
				MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf
						.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
				QueuePointInfo qpi = adminHelper.getQueuePoints(meInfo.getName(), queuename);
				long toDelete = qpi.getCurrentDepth();

				// all messages
				if (argfilter.equals(Constants.ARG_FILTER)) {
					String msgSelector = null;
					if (commandLine.hasOption(CommandOptions.problemDestination)) {
						String problemDestination = commandLine.getOptionValue(CommandOptions.problemDestination);
						msgSelector = "JMS_IBM_ExceptionProblemDestination = '" + problemDestination + "'";
						
						toDelete = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), queuename, msgSelector).size();
					}
					
					if (toDelete == 0) {
						System.out.println();
						System.out.println(" QUEUEPOINT(" + queuename + ")");
						System.out.println("  MESSAGE SELECTOR(" + (msgSelector == null ? "NONE" : msgSelector) + ")");
						System.out.println("  MESSAGE COUNT(" + toDelete + ")");
						System.out.println("  MESSAGE PURGED(" + 0 + ")");
						return 0;
					}

					// Check if the commandline has a --noStop option
					if (!commandLine.hasOption(CommandOptions.noStop) && toDelete > 0) {
						String q = "Do you want to continue and purge " + toDelete + " messages (y/n)?";
						boolean result = askYesNo(q);
						if (!result) {
							return 0;
						}
					}
					logger.log(Level.INFO, "Deleting data on queue " + argqueue + ". Please wait can take time!");
					long deleted = messagingHelper.clearQueue(busInfo.getName(), meInfo.getName(), queuename, msgSelector, toDelete);

					System.out.println();
					System.out.println(" QUEUEPOINT(" + queuename + ")");
					System.out.println("  MESSAGE SELECTOR(" + (msgSelector == null ? "NONE" : msgSelector) + ")");
					System.out.println("  MESSAGE COUNT(" + toDelete + ")");
					System.out.println("  MESSAGE PURGED(" + deleted + ")");
					return deleted;
				}
				// single message
				else {
					if (toDelete == 0) {
						System.out.println();
						System.out.println(" QUEUEPOINT(" + queuename + ")");
						System.out.println("  MESSAGE COUNT(" + toDelete + ")");
						System.out.println("  MESSAGE SELECTOR(ID:" + argfilter + ")");
						System.out.println("  MESSAGE FOUND(" + 0 + ")");
						System.out.println("  MESSAGE PURGED(" + 0 + ")");
						return 0;
					}

					MessageInfo element = messagingHelper.browseSingleMessage(busInfo.getName(), meInfo.getName(), queuename,
							argfilter);
					if (element == null) {
						System.out.println();
						System.out.println(" QUEUEPOINT(" + queuename + ")");
						System.out.println("  MESSAGE COUNT(" + toDelete + ")");
						System.out.println("  MESSAGE SELECTOR(ID:" + argfilter + ")");
						System.out.println("  MESSAGE FOUND(" + 0 + ")");
						System.out.println("  MESSAGE PURGED(" + 0 + ")");
						return 0;
					}

					// Check if the commandline has a --noStop option
					if (!commandLine.hasOption(CommandOptions.noStop) && toDelete > 0) {
						String q = "Do you want to continue and purge " + element.getApiMessageId() + " messages (y/n)?";
						boolean result = askYesNo(q);
						if (!result) {
							return 0;
						}
					}
					logger.log(Level.INFO, "Deleting data on queue " + argqueue + ". Please wait can take time!");
					String msgSelector = "JMSMessageID = 'ID:" + argfilter + "'";
					long deleted = messagingHelper.clearQueue(busInfo.getName(), meInfo.getName(), queuename, msgSelector,
							toDelete);

					System.out.println();
					System.out.println(" QUEUEPOINT(" + queuename + ")");
					System.out.println("  MESSAGE COUNT(" + toDelete + ")");
					System.out.println("  MESSAGE SELECTOR(" + element.getApiMessageId() + ")");
					System.out.println("  MESSAGE FOUND(" + deleted + ")");
					System.out.println("  MESSAGE PURGED(" + deleted + ")");
					return deleted;
				}
			}
		}
		return 0;
	}

	/**
	 * @throws IOException
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 * @throws ConfigServiceException
	 * @throws MessagingOperationFailedException
	 */
	private void actionResubmitSE() throws IOException, MalformedObjectNameException, InstanceNotFoundException,
			MBeanException, ReflectionException, ConnectorException, ConfigServiceException, MessagingOperationFailedException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date currentTime = GregorianCalendar.getInstance().getTime();
		String filename = Constants.FILE_PREFIX + "_" + sibusAction + "_" + sibusComponent.toUpperCase() + "_"
				+ getStrippedFileName(argqueue) + "_" + sdf.format(currentTime);
		String path = commandLine.getOptionValue(CommandOptions.reportDirectory);

		// Create file writer instances
		logger.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the messages.");
		MessageFileWriter fileWriter = new MessageFileWriter(path, filename + ".csv");

		MEInfo mesInfo[] = adminHelper.getMessagingEngines();
		for (MEInfo element0 : mesInfo) {
			BusInfo busInfo = adminHelper.getBusInfo(element0.getBus());
			MEInfo meInfo = element0;
			int fill = busInfo.getName().length();
			argqueue = Constants.SE_QUEUE + meInfo.getName();

			if (busInfo.getName().equalsIgnoreCase(argsibus)) {
				System.out.println();
				System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
				MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf
						.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
				QueuePointInfo qpi = adminHelper.getQueuePoints(meInfo.getName(), argqueue);
				long toMove = qpi.getCurrentDepth();

				// all messages
				if (argfilter.equals(Constants.ARG_FILTER)) {
					String msgSelector = null;
					if (commandLine.hasOption(CommandOptions.problemDestination)) {
						String problemDestination = commandLine.getOptionValue(CommandOptions.problemDestination);
						msgSelector = "JMS_IBM_ExceptionProblemDestination = '" + problemDestination + "'";
						
						toMove = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), argqueue, msgSelector).size();
					}
					
					if (toMove == 0) {
						System.out.println();
						System.out.println(" EXCEPTIONPOINT(" + argqueue + ")");
						System.out.println("  MESSAGE SELECTOR(" + (msgSelector == null ? "NONE" : msgSelector) + ")");
						System.out.println("  MESSAGE TO MOVE(" + toMove + ")");
						return;
					}

					// Check if the commandline has a --noStop option
					if (!commandLine.hasOption(CommandOptions.noStop) && toMove > 0) {
						String q = "Do you want to continue and resubmit " + toMove + " messages (y/n)?";
						boolean result = askYesNo(q);
						if (!result) {
							return;
						}
					}
					logger.log(Level.INFO, "Moving data from queue " + argqueue + ". Please wait can take time!");
					List<MessageInfo> list = messagingHelper.moveExceptionToDestination(busInfo.getName(), meInfo.getName(),
							argqueue, msgSelector, toMove);
					if (!list.isEmpty()) {
						logger.log(Level.FINE, "Writing messages to file...Please wait");
						fileWriter.writeSEHeader();
						Iterator iter = list.iterator();
						while (iter.hasNext()) {
							MessageInfo element = (MessageInfo) iter.next();
							fileWriter.writeCSVSEMessage(element, argqueue);
						}
					}

					System.out.println();
					System.out.println(" EXCEPTIONPOINT(" + argqueue + ")");
					System.out.println("  MESSAGE SELECTOR(" + (msgSelector == null ? "NONE" : msgSelector) + ")");
					System.out.println("  MESSAGE TO MOVE(" + toMove + ")");
					System.out.println("  MESSAGE MOVED(" + list.size() + ")");

				}
				// single message
				else {
					if (toMove == 0) {
						System.out.println();
						System.out.println(" EXCEPTIONPOINT(" + argqueue + ")");
						System.out.println("  MESSAGE TO MOVE(" + toMove + ")");
						return;
					}

					MessageInfo element = messagingHelper.browseSingleMessage(busInfo.getName(), meInfo.getName(), argqueue,
							argfilter);
					if (element == null) {
						System.out.println();
						System.out.println(" EXCEPTIONPOINT(" + argqueue + ")");
						System.out.println("  MESSAGE TO MOVE(" + 0 + ")");
						System.out.println("  MESSAGE SELECTOR(ID:" + argfilter + ")");
						System.out.println("  MESSAGE FOUND(" + 0 + ")");
						return;
					}

					// Check if the commandline has a --noStop option
					if (!commandLine.hasOption(CommandOptions.noStop) && toMove > 0) {
						String q = "Do you want to continue and resubmit " + element.getApiMessageId() + " messages (y/n)?";
						boolean result = askYesNo(q);
						if (!result) {
							return;
						}
					}

					logger.log(Level.INFO, "Moving data from queue " + argqueue + ". Please wait can take time!");
					String msgSelector = "JMSMessageID = 'ID:" + argfilter + "'";
					List<MessageInfo> list = messagingHelper.moveExceptionToDestination(busInfo.getName(), meInfo.getName(),
							argqueue, msgSelector, 1);

					if (!list.isEmpty()) {
						logger.log(Level.FINE, "Writing messages to file...Please wait");
						fileWriter.writeSEHeader();
						Iterator iter = list.iterator();
						while (iter.hasNext()) {
							MessageInfo meinfo = (MessageInfo) iter.next();
							fileWriter.writeCSVSEMessage(meinfo, argqueue);
						}
					}

					System.out.println();
					System.out.println(" EXCEPTIONPOINT(" + Constants.SE_QUEUE + meInfo.getName() + ")");
					System.out.println("  MESSAGE TO MOVE(" + 1 + ")");
					System.out.println("  MESSAGE SELECTOR(ID:" + argfilter + ")");
					System.out.println("  MESSAGE FOUND(" + list.size() + ")");
					System.out.println("  MESSAGE MOVED(" + list.size() + ")");
					System.out.println();
				}
			}
		}
		if (fileWriter != null) {
			fileWriter.close();
		}
		return;
	}

	/**
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 * @throws ConfigServiceException
	 * @throws MessagingOperationFailedException
	 * @throws DestinationNotFoundException
	 */
	private long actionMove() throws MalformedObjectNameException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException, ConfigServiceException, MessagingOperationFailedException,
			DestinationNotFoundException {
		MEInfo mesInfo[] = adminHelper.getMessagingEngines();
		String queuename = null;
		String queuename1 = null;
		for (MEInfo element0 : mesInfo) {
			BusInfo busInfo = adminHelper.getBusInfo(element0.getBus());
			MEInfo meInfo = element0;
			int fill = busInfo.getName().length();
			if (argqueue.equals("SE")) {
				queuename = Constants.SE_QUEUE + meInfo.getName();
			} else {
				queuename = argqueue;
			}

			if (argqueue1.equals("SE")) {
				queuename1 = Constants.SE_QUEUE + meInfo.getName();
			} else {
				queuename1 = argqueue1;
			}

			if (busInfo.getName().equalsIgnoreCase(argsibus)) {
				System.out.println();
				System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
				MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf
						.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
				QueuePointInfo qpi = adminHelper.getQueuePoints(meInfo.getName(), queuename);
				long toMove = qpi.getCurrentDepth();

				// all messages
				if (argfilter.equals(Constants.ARG_FILTER)) {
					String msgSelector = null;
					if (commandLine.hasOption(CommandOptions.problemDestination)) {
						String problemDestination = commandLine.getOptionValue(CommandOptions.problemDestination);
						msgSelector = "JMS_IBM_ExceptionProblemDestination = '" + problemDestination + "'";
						
						toMove = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), queuename, msgSelector).size();
					}
					
					if (toMove == 0) {
						System.out.println();
						System.out.println(" QUEUEPOINT SOURCE(" + queuename + ")");
						System.out.println("  MESSAGE SELECTOR(" + (msgSelector == null ? "NONE" : msgSelector) + ")");
						System.out.println("  MESSAGE TO MOVE(" + toMove + ")");
						System.out.println(" QUEUEPOINT TARGET(" + queuename1 + ")");
						System.out.println("  MESSAGE MOVED(" + toMove + ")");
						return 0;
					}

					// Check if the commandline has a --noStop option
					if (!commandLine.hasOption(CommandOptions.noStop) && toMove > 0) {
						String q = "Do you want to continue and move " + toMove + " messages (y/n)?";
						boolean result = askYesNo(q);
						if (!result) {
							return 0;
						}
					}
					logger.log(Level.INFO, "Moving data to queue " + argqueue1 + ". Please wait can take time!");
					long moved = messagingHelper.moveMessages(busInfo.getName(), meInfo.getName(), queuename, queuename1, msgSelector,
							toMove);
					System.out.println();
					System.out.println(" QUEUEPOINT SOURCE(" + queuename + ")");
					System.out.println("  MESSAGE SELECTOR(" + (msgSelector == null ? "NONE" : msgSelector) + ")");
					System.out.println("  MESSAGE TO MOVE(" + toMove + ")");
					System.out.println(" QUEUEPOINT TARGET(" + queuename1 + ")");
					System.out.println("  MESSAGE MOVED(" + moved + ")");
					return moved;
				}
				// single message
				else {
					if (toMove == 0) {
						System.out.println();
						System.out.println(" QUEUEPOINT SOURCE(" + queuename + ")");
						System.out.println("  MESSAGE TO MOVE(" + 0 + ")");
						System.out.println("  MESSAGE SELECTOR(ID:" + argfilter + ")");
						System.out.println("  MESSAGE FOUND(" + 0 + ")");
						System.out.println(" QUEUEPOINT TARGET(" + queuename1 + ")");
						System.out.println("  MESSAGE MOVED(" + 0 + ")");
						return 0;
					}

					MessageInfo element = messagingHelper.browseSingleMessage(busInfo.getName(), meInfo.getName(), queuename,
							argfilter);
					if (element == null) {
						System.out.println();
						System.out.println(" QUEUEPOINT SOURCE(" + queuename + ")");
						System.out.println("  MESSAGE TO MOVE(" + 0 + ")");
						System.out.println("  MESSAGE SELECTOR(ID:" + argfilter + ")");
						System.out.println("  MESSAGE FOUND(" + 0 + ")");
						System.out.println(" QUEUEPOINT TARGET(" + queuename1 + ")");
						System.out.println("  MESSAGE MOVED(" + 0 + ")");
						return 0;
					}

					// Check if the commandline has a --noStop option
					if (!commandLine.hasOption(CommandOptions.noStop) && toMove > 0) {
						String q = "Do you want to continue and move " + element.getApiMessageId() + " messages (y/n)?";
						boolean result = askYesNo(q);
						if (!result) {
							return 0;
						}
					}
					logger.log(Level.INFO, "Moving data to queue " + argqueue1 + ". Please wait can take time!");
					String msgSelector = "JMSMessageID = 'ID:" + argfilter + "'";
					long moved = messagingHelper.moveMessages(busInfo.getName(), meInfo.getName(), queuename, queuename1,
							msgSelector, 1);

					System.out.println();
					System.out.println(" QUEUEPOINT SOURCE(" + queuename + ")");
					System.out.println("  MESSAGE COUNT(" + toMove + ")");
					System.out.println("  MESSAGE SELECTOR(ID:" + argfilter + ")");
					System.out.println("  MESSAGE TO MOVE(" + 1 + ")");
					System.out.println(" QUEUEPOINT TARGET(" + queuename1 + ")");
					System.out.println("  MESSAGE MOVED(" + moved + ")");
					System.out.println();
					return moved;
				}
			}
		}
		return 0;
	}

	/**
	 * @throws IOException
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 * @throws ConfigServiceException
	 * @throws MessagingOperationFailedException
	 */
	private void actionReportSE() throws IOException, MalformedObjectNameException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException, ConfigServiceException, MessagingOperationFailedException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date currentTime = GregorianCalendar.getInstance().getTime();
		String filename = Constants.FILE_PREFIX + "_" + sibusAction + "_" + sibusComponent.toUpperCase() + "_"
				+ getStrippedFileName(argqueue) + "_" + sdf.format(currentTime);
		String path = commandLine.getOptionValue(CommandOptions.reportDirectory);
		boolean isHeader = false;

		// Create file writer instances
		logger.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the messages.");
		MessageFileWriter fileWriter = new MessageFileWriter(path, filename + ".csv");

		MEInfo mesInfo[] = adminHelper.getMessagingEngines();
		System.out.println();

		String msgSelector = null;
		if (commandLine.hasOption(CommandOptions.problemDestination)) {
			String problemDestination = commandLine.getOptionValue(CommandOptions.problemDestination);
			msgSelector = "JMS_IBM_ExceptionProblemDestination = '" + problemDestination + "'";
		}

		for (MEInfo element0 : mesInfo) {
			BusInfo busInfo = adminHelper.getBusInfo(element0.getBus());
			MEInfo meInfo = element0;
			int fill = busInfo.getName().length();

			// ALL SIBUSES
			if (argsibus.equalsIgnoreCase(Constants.ARG_FILTER)) {
				System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
				MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf
						.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
				logger.log(Level.INFO, "Collecting data for report...Please wait can take time!");
				List list = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), Constants.SE_QUEUE
						+ meInfo.getName(), msgSelector);

				if (!list.isEmpty()) {
					logger.log(Level.FINE, "Writing messages to file...Please wait");
					if (!isHeader) {
						fileWriter.writeHeader();
						isHeader = true;
					}
					Iterator iter = list.iterator();
					while (iter.hasNext()) {
						MessageInfo element = (MessageInfo) iter.next();
						fileWriter.writeCSVMessage(element, Constants.SE_QUEUE + meInfo.getName());
					}
				}
				System.out.println();
				System.out.println(" EXCEPTIONPOINT(" + Constants.SE_QUEUE + meInfo.getName() + ")");
				System.out.println("  MESSAGE SELECTOR(" + (msgSelector == null ? "NONE" : msgSelector) + ")");
				System.out.println("  MESSAGE COUNT(" + list.size() + ")");
				System.out.println("  BROWSED(" + "DONE" + ")");
				System.out.println();
			}
			// JUST FOR THE SIBUS
			else if (busInfo.getName().equalsIgnoreCase(argsibus)) {
				System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
				MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf
						.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());
				logger.log(Level.INFO, "Collecting data for report...Please wait can take time!");
				List list = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), Constants.SE_QUEUE
						+ meInfo.getName(), msgSelector);

				logger.log(Level.FINE, "Writing messages to file...Please wait");

				if (!isHeader) {
					fileWriter.writeHeader();
					isHeader = true;
				}
				Iterator iter = list.iterator();
				while (iter.hasNext()) {
					MessageInfo element = (MessageInfo) iter.next();
					fileWriter.writeCSVMessage(element, Constants.SE_QUEUE + meInfo.getName());
				}

				System.out.println();
				System.out.println(" EXCEPTIONPOINT(" + Constants.SE_QUEUE + meInfo.getName() + ")");
				System.out.println("  MESSAGE SELECTOR(" + (msgSelector == null ? "NONE" : msgSelector) + ")");
				System.out.println("  MESSAGE COUNT(" + list.size() + ")");
				System.out.println("  BROWSED(" + "DONE" + ")");
				System.out.println();
				System.out.println();
			}
		}
		if (fileWriter != null) {
			fileWriter.close();
		}
	}

	/**
	 * @throws IOException
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 * @throws ConfigServiceException
	 * @throws MessagingOperationFailedException
	 */
	private void actionReportQueue() throws IOException, MalformedObjectNameException, InstanceNotFoundException,
			MBeanException, ReflectionException, ConnectorException, ConfigServiceException, MessagingOperationFailedException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date currentTime = GregorianCalendar.getInstance().getTime();
		String filename = Constants.FILE_PREFIX + "_" + sibusAction + "_" + sibusComponent.toUpperCase() + "_"
				+ getStrippedFileName(argqueue) + "_" + sdf.format(currentTime);
		String path = commandLine.getOptionValue(CommandOptions.reportDirectory);

		// Create file writer instances
		logger.log(Level.FINE, "Opening file#" + filename + "on path#" + path + " for reporting the messages.");
		MessageFileWriter fileWriter = new MessageFileWriter(path, filename + ".csv");
		logger.log(Level.INFO, "Collecting and writing data for report...Please wait can take time!");
		fileWriter.writeHeader();


		MEInfo mesInfo[] = adminHelper.getMessagingEngines();
		for (MEInfo element0 : mesInfo) {
			BusInfo busInfo = adminHelper.getBusInfo(element0.getBus());
			MEInfo meInfo = element0;
			int fill = busInfo.getName().length();

			if (argsibus.equals(Constants.ARG_FILTER) || busInfo.getName().equalsIgnoreCase(argsibus)) {
				System.out.println(getSeparatorLine(30) + " " + busInfo.getName() + " " + getSeparatorLine(80 - fill));
				MessagingHelper messagingHelper = new MessagingHelperImpl(host, serverConf.getMessagingPort(), serverConf
						.getMessagingChainName(), overallConf.getServer().getUserName(), overallConf.getServer().getPassword());

				// Collect destinations
				List<DestinationInfo> destinations = new ArrayList<DestinationInfo>();
				if (argqueue.equals(Constants.ARG_FILTER)) {
					destinations = adminHelper.getDestinations(busInfo.getName());
				} else {
					destinations = adminHelper.getDestinations(busInfo.getName(), argqueue);
				}

				for (DestinationInfo destination : destinations) {
					logger.log(Level.WARNING, "Destination="+destination.getDestinationName());
					// more than one message
					if (argfilter.equals(Constants.ARG_FILTER) || argfilter.equals(Constants.ARG_FILTER_NOT_EMPTY)) {
						if (argfilter.equals(Constants.ARG_FILTER_NOT_EMPTY)) {
							String[] queuePoints = destination.getQueuePoints();
							int messageCount = 0;
							for (String queuePoint : queuePoints) {
								QueuePointInfo qpi = adminHelper.getQueuePoints(meInfo.getName(), queuePoint);
								messageCount += qpi.getCurrentDepth();
							}
							if (messageCount == 0) {
								continue;
							}
						}
						String msgSelector = null;
						if (commandLine.hasOption(CommandOptions.problemDestination)) {
							String problemDestination = commandLine.getOptionValue(CommandOptions.problemDestination);
							msgSelector = "JMS_IBM_ExceptionProblemDestination = '" + problemDestination + "'";
						}
						List list = messagingHelper.browseQueue(busInfo.getName(), meInfo.getName(), destination.getDestinationName(), msgSelector);

						Iterator iter = list.iterator();
						while (iter.hasNext()) {
							MessageInfo element = (MessageInfo) iter.next();
							fileWriter.writeCSVMessage(element, argqueue);
						}

						System.out.println();
						System.out.println(" DESTINATION(" + argqueue + ")");
						System.out.println("  MESSAGE SELECTOR(" + (msgSelector == null ? "NONE" : msgSelector) + ")");
						System.out.println("  MESSAGE COUNT(" + list.size() + ")");
						System.out.println("  BROWSED(" + "DONE" + ")");
						System.out.println();
					} else {
						MessageInfo element = messagingHelper.browseSingleMessage(busInfo.getName(), meInfo.getName(), argqueue,
								argfilter);

						if (element != null) {
							fileWriter.writeCSVMessage(element, argqueue);
							System.out.println(" DESTINATION(" + argqueue + ")");
							System.out.println("  MESSAGE FILTER(" + argfilter + ")");
							System.out.println("  MESSAGE COUNT(" + "1" + ")");
							System.out.println("  BROWSED(" + element.getSystemMessageId() + ")");
							System.out.println();
						} else {
							System.out.println(" DESTINATION(" + argqueue + ")");
							System.out.println("  MESSAGE FILTER(" + argfilter + ")");
							System.out.println("  MESSAGE COUNT(" + "0" + ")");
							System.out.println("  BROWSED(" + "NONE" + ")");
							System.out.println();
						}
					}
				}
			}
		}

		// Close writers. (The close() method handles if the writer allready
		// have been closed)
		if (null != filename) {
			fileWriter.close();
		}

	}

	/**
	 * @param width
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
	 * @param width
	 * @return
	 */
	private String getStrippedFileName(String s) {
		if (s != null) {
			String result = s.replaceAll("/", ".");
			result = result.replaceAll("\\\\", ".");
			return result;
		}
		return StringUtils.EMPTY;
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
