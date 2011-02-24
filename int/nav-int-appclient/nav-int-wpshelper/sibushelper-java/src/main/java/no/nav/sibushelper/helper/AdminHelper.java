/**
 * 
 */
package no.nav.sibushelper.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import no.nav.sibushelper.SIBUSHelper;
import no.nav.sibushelper.common.Constants;

import com.ibm.websphere.command.CommandException;
import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.AdminClientFactory;
import com.ibm.websphere.management.Session;
import com.ibm.websphere.management.cmdframework.AdminCommand;
import com.ibm.websphere.management.cmdframework.CommandMgr;
import com.ibm.websphere.management.cmdframework.CommandMgrInitException;
import com.ibm.websphere.management.cmdframework.CommandNotFoundException;
import com.ibm.websphere.management.cmdframework.CommandResult;
import com.ibm.websphere.management.configservice.ConfigService;
import com.ibm.websphere.management.configservice.ConfigServiceHelper;
import com.ibm.websphere.management.configservice.ConfigServiceProxy;
import com.ibm.websphere.management.exception.ConfigServiceException;
import com.ibm.websphere.management.exception.ConnectorException;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class AdminHelper {

	private static Logger logger = Logger.getLogger(SIBUSHelper.class.getName());
	private String className = AdminHelper.class.getName();

	private AdminClient ac;
	private ConfigService configService;
	private ServerInfo serverInfoCache[];

	/**
	 * 
	 */
	public AdminHelper() {
		ac = null;
		configService = null;
		serverInfoCache = null;
	}

	/**
	 * @param hostName
	 * @param port
	 * @param protocol
	 * @param userName
	 * @param password
	 * @param trustStoreLocation
	 * @param trustStorePassword
	 * @param keyStoreLocation
	 * @param keyStorePassword
	 * @throws ConnectorException
	 */
	public void connect(String hostName, int port, String protocol, String userName, String password,
			String trustStoreLocation, String trustStorePassword, String keyStoreLocation, String keyStorePassword)
			throws ConnectorException {
		logger.logp(Level.FINE, className, "connect", "Init");

		if (ac == null) {
			Properties props = new Properties();
			props.put(AdminClient.CONNECTOR_HOST, hostName);
			props.put(AdminClient.CONNECTOR_PORT, String.valueOf(port));

			if (protocol.equals(Constants.SOAP_PROTOCOL)) {
				props.put(AdminClient.CONNECTOR_TYPE, "SOAP");
				props.put(AdminClient.CONNECTOR_SOAP_REQUEST_TIMEOUT, "360");
			} else if (protocol.equals(Constants.RMI_PROTOCOL)) {
				props.put(AdminClient.CONNECTOR_TYPE, "RMI");
			}

			if (userName != null && !userName.equals("")) {
				props.put(AdminClient.USERNAME, userName);
				props.put(AdminClient.PASSWORD, password);
				props.put(AdminClient.CONNECTOR_SECURITY_ENABLED, "true");

				// RMI we don't need the file store
				if (!protocol.equals(Constants.RMI_PROTOCOL)) {
					props.put("com.ibm.ssl.enableSignerExchangePrompt", "true");
					props.put("com.ibm.ssl.keyStoreFileBased", "true");
					props.put("com.ibm.ssl.trustStoreFileBased", "true");
					props.put("com.ibm.ssl.keyStore", keyStoreLocation);
					props.put("javax.net.ssl.keyStore", keyStoreLocation);
					props.put("com.ibm.ssl.keyStorePassword", keyStorePassword);
					props.put("javax.net.ssl.keyStorePassword", keyStorePassword);
					if (keyStoreLocation.endsWith(".p12") || keyStoreLocation.endsWith(".P12")) {
						props.put("com.ibm.ssl.keyStoreType", "PKCS12");
						props.put("javax.net.ssl.keyStoreType", "PKCS12");
					} else {
						props.put("com.ibm.ssl.keyStoreType", "JKS");
						props.put("javax.net.ssl.keyStoreType", "JKS");
					}
					props.put("com.ibm.ssl.trustStore", trustStoreLocation);
					props.put("javax.net.ssl.trustStore", trustStoreLocation);
					props.put("com.ibm.ssl.trustStorePassword", trustStorePassword);
					props.put("javax.net.ssl.trustStorePassword", trustStorePassword);
					if (trustStoreLocation.endsWith(".p12") || trustStoreLocation.endsWith(".P12")) {
						props.put("com.ibm.ssl.trustStoreType", "PKCS12");
						props.put("javax.net.ssl.trustStoreType", "PKCS12");
					} else {
						props.put("com.ibm.ssl.trustStoreType", "JKS");
						props.put("javax.net.ssl.trustStoreType", "JKS");
					}
				}

			}

			logger.logp(Level.FINE, className, "connect", "Using properties: " + props);
			ac = AdminClientFactory.createAdminClient(props);

			try {
				configService = new ConfigServiceProxy(ac);
				logger.logp(Level.FINE, className, "connect", "Got hold of config service");
			} catch (InstanceNotFoundException e) {
				logger.logp(Level.FINE, className, "connect", "Unable to get hold of config service", e);
				String message = "Unable to connect to the remote configuration service.\r\n\r\nThis is most likely because this node is a managed node but you are not connected through the D-Manager. As such, many functions may not be available.\r\nYou can avoid seeing this error message by connecting directly to the Dmgr for this cell.";
				System.err.println(message);
			}
		}
		logger.logp(Level.FINE, className, "connect", "Exit Ok");
	}

	/**
	 * @param hostName
	 * @param port
	 * @throws ConnectorException
	 */
	public void connect(String hostName, int port) throws ConnectorException {
		connect(hostName, port, Constants.RMI_PROTOCOL, null, null, null, null, null, null);
	}

	/**
	 * @param cmd
	 * @return
	 * @throws CommandNotFoundException
	 * @throws CommandException
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 */
	private Object executeCommand(AdminCommand cmd) throws CommandNotFoundException, CommandException, ConfigServiceException,
			ConnectorException {
		logger.logp(Level.FINE, className, "executeCommand", cmd.toString());
		Session session;
		Object retObject;
		session = getConfigSession();
		retObject = null;
		retObject = executeCommand(cmd, session);
		logger.logp(Level.FINE, className, "executeCommand", retObject.toString());
		return retObject;
	}

	/**
	 * @param cmd
	 * @param session
	 * @return
	 * @throws CommandNotFoundException
	 * @throws CommandException
	 * @throws ConfigServiceException
	 */
	private Object executeCommand(AdminCommand cmd, Session session) throws CommandNotFoundException, CommandException,
			ConfigServiceException {
		logger.logp(Level.FINE, className, "executeCommand", (new Object[] { cmd, session }).toString());
		CommandResult result = null;
		if (configService == null) {
			logger.logp(Level.SEVERE, className, "executeCommand", "ConfigService is null");
			throw new ConfigServiceException("SIBUSHelper configuration service is not available!");
		}
		cmd.setConfigSession(session);
		cmd.execute();
		result = cmd.getCommandResult();
		logger.logp(Level.FINE, className, "executeCommand", "Result=" + result);
		if (!result.isSuccessful()) {
			System.err.println("Command failed. Message: " + result.getMessages());
			System.err.println("Exception: " + result.getException());
			throw new CommandException("The command failed: " + result.getException());
		}
		Object retObject = result.getResult();
		logger.logp(Level.FINE, className, "executeCommand", retObject.toString());
		return retObject;
	}

	/**
	 * @return
	 */
	private synchronized Session getConfigSession() {
		logger.logp(Level.FINE, className, "getConfigSession", "Init");
		Session configSession = new Session("SIBBUSHelper", false);
		logger.logp(Level.FINE, className, "getConfigSession", configSession.toString());
		return configSession;
	}

	/**
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public MEInfo[] getMessagingEngines() throws MalformedObjectNameException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {

		logger.logp(Level.FINE, className, "getMessagingEngines", "Init");
		ObjectName sibMes = new ObjectName("WebSphere:type=SIBMessagingEngine,*");
		Set sibMesSet = ac.queryNames(sibMes, null);
		MEInfo mes[] = new MEInfo[sibMesSet.size()];
		int x = 0;
		for (Iterator i = sibMesSet.iterator(); i.hasNext();) {
			ObjectName meObj = (ObjectName) i.next();
			String haGroup = ac.invoke(meObj, "getHAGroupName", null, null).toString();
			String state = (String) ac.invoke(meObj, "state", null, null);
			boolean started = ((Boolean) ac.invoke(meObj, "isStarted", null, null)).booleanValue();
			mes[x] = new MEInfo(meObj.getKeyProperty("name"), haGroup, state, started, meObj.getKeyProperty("process"), meObj
					.getKeyProperty("node"), meObj.getKeyProperty("cell"), meObj);
			x++;
		}
		Arrays.sort(mes);
		logger.logp(Level.FINE, className, "getMessagingEngines", "Exit");
		return mes;
	}

	/**
	 * @param meName
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public List getQueuePoints(String meName) throws MalformedObjectNameException, InstanceNotFoundException,
			ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "getQueuePoints", meName);
		ObjectName queuePoints = new ObjectName("WebSphere:type=SIBQueuePoint,SIBMessagingEngine=" + meName + ",*");
		Set queueNameSet = ac.queryNames(queuePoints, null);
		ArrayList<QueuePointInfo> queueList = new ArrayList<QueuePointInfo>();
		Long depth;
		String state;
		String id;
		Long highMessageThreshold;
		Boolean sendAllowed;
		String name;
		for (Iterator i = queueNameSet.iterator(); i.hasNext(); queueList.add(new QueuePointInfo(name, depth, state,
				highMessageThreshold, sendAllowed, id))) {
			ObjectName obj = (ObjectName) i.next();
			logger.logp(Level.FINE, className, "getQueuePoints", "Found Queue M-Bean:", obj);
			AttributeList attribList = ac.getAttributes(obj, new String[] { "depth", "state", "id", "highMessageThreshold",
					"sendAllowed" });
			depth = null;
			state = "";
			id = "";
			highMessageThreshold = null;
			sendAllowed = null;
			name = obj.getKeyProperty("name");
			for (Iterator j = attribList.iterator(); j.hasNext();) {
				Attribute a = (Attribute) j.next();
				if (a.getName().equals("depth")) {
					depth = (Long) a.getValue();
				} else if (a.getName().equals("state")) {
					state = (String) a.getValue();
				} else if (a.getName().equals("id")) {
					id = (String) a.getValue();
				} else if (a.getName().equals("highMessageThreshold")) {
					highMessageThreshold = (Long) a.getValue();
				} else if (a.getName().equals("sendAllowed")) {
					sendAllowed = (Boolean) a.getValue();
				}
			}
		}

		logger.logp(Level.FINE, className, "getQueuePoints", new Integer(queueList.size()).toString());
		return queueList;
	}

	/**
	 * @param meName
	 * @param queuePoint
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public QueuePointInfo getQueuePoints(String meName, String queuename) throws MalformedObjectNameException,
			InstanceNotFoundException, ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "getQueuePoints", meName);
		ObjectName queuePoints = new ObjectName("WebSphere:name=" + queuename + ",type=SIBQueuePoint" + ",SIBMessagingEngine="
				+ meName + ",*");
		Set queueNameSet = ac.queryNames(queuePoints, null);
		QueuePointInfo queuePoint = new QueuePointInfo();
		Long depth;
		String state;
		String id;
		Long highMessageThreshold;
		Boolean sendAllowed;
		String name;
		Iterator i = queueNameSet.iterator();
		while (i.hasNext()) {
			ObjectName obj = (ObjectName) i.next();
			logger.logp(Level.FINE, className, "getQueuePoints", "Found Queue M-Bean:", obj);
			AttributeList attribList = ac.getAttributes(obj, new String[] { "depth", "state", "id", "highMessageThreshold",
					"sendAllowed" });
			depth = null;
			state = "";
			id = "";
			highMessageThreshold = null;
			sendAllowed = null;
			name = obj.getKeyProperty("name");
			for (Iterator j = attribList.iterator(); j.hasNext();) {
				Attribute a = (Attribute) j.next();
				if (a.getName().equals("depth")) {
					depth = (Long) a.getValue();
				} else if (a.getName().equals("state")) {
					state = (String) a.getValue();
				} else if (a.getName().equals("id")) {
					id = (String) a.getValue();
				} else if (a.getName().equals("highMessageThreshold")) {
					highMessageThreshold = (Long) a.getValue();
				} else if (a.getName().equals("sendAllowed")) {
					sendAllowed = (Boolean) a.getValue();
				}
			}

			queuePoint.setId(id);
			queuePoint.setName(name);
			queuePoint.setState(state);
			queuePoint.setCurrentDepth(depth);
			queuePoint.setHighMessageThreshold(highMessageThreshold);
			queuePoint.setSendAllowed(sendAllowed);

		}
		logger.logp(Level.FINE, className, "getQueuePoints", "done");
		return queuePoint;
	}

	/**
	 * @param busName
	 * @return
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 * @throws MalformedObjectNameException
	 */
	public List<DestinationInfo> getDestinations(String busName) throws ConfigServiceException, ConnectorException,
			MalformedObjectNameException {
		Set queuePointsSet;
		Set mediationPointsSet;
		Set publicationPointsSet;
		ArrayList<DestinationInfo> destList;
		Session session;
		logger.logp(Level.FINE, className, "getDestinations", busName);
		destList = new ArrayList<DestinationInfo>();
		session = getConfigSession();
		ObjectName scopeArray[] = configService.resolve(session, "SIBus=" + busName);
		if (scopeArray.length != 1) {
			throw new ConfigServiceException("Bus " + busName + " not found in the configuration");
		}

		ObjectName queuePointsObjs = new ObjectName("WebSphere:type=SIBQueuePoint,*");
		queuePointsSet = ac.queryNames(queuePointsObjs, null);
		ObjectName mediationPointsObjs = new ObjectName("WebSphere:type=SIBMediationPoint,*");
		mediationPointsSet = ac.queryNames(mediationPointsObjs, null);
		ObjectName publicationPointsObjs = new ObjectName("WebSphere:type=SIBPublicationPoint,*");
		publicationPointsSet = ac.queryNames(publicationPointsObjs, null);

		ObjectName pattern = ConfigServiceHelper.createObjectName(null, "SIBAbstractDestination");
		ObjectName destConfig[] = configService.queryConfigObjects(session, scopeArray[0], pattern, null);

		for (ObjectName element : destConfig) {
			String type = element.getKeyProperty("_Websphere_Config_Data_Type");
			AttributeList attribList = configService.getAttributes(session, element, new String[] { "identifier",
					"description", "uuid" }, false);
			String destName = (String) ((Attribute) attribList.get(0)).getValue();
			String description = (String) ((Attribute) attribList.get(1)).getValue();
			String destUuid = (String) ((Attribute) attribList.get(2)).getValue();
			String targetIdentifier = null;
			String targetBus = null;

			if (type.equals("SIBDestinationAlias")) {
				AttributeList attribList2 = configService.getAttributes(session, element, new String[] {
						"targetIdentifier", "targetBus" }, false);
				targetIdentifier = (String) ((Attribute) attribList2.get(0)).getValue();
				targetBus = (String) ((Attribute) attribList2.get(1)).getValue();
			}

			DestinationInfo destInfo = new DestinationInfo(destName, type, description, destUuid, targetIdentifier, targetBus);
			for (Iterator i = queuePointsSet.iterator(); i.hasNext();) {
				ObjectName objName = (ObjectName) i.next();
				if (objName.getKeyProperty("name").equals(destName) && objName.getKeyProperty("SIBus").equals(busName)) {
					// destInfo.addQueuePoint(objName.getKeyProperty("name") +
					// "@" + objName.getKeyProperty("SIBMessagingEngine"));
					destInfo.addQueuePoint(objName.getKeyProperty("name"));
				}
			}

			for (Iterator i = mediationPointsSet.iterator(); i.hasNext();) {
				ObjectName objName = (ObjectName) i.next();
				if (objName.getKeyProperty("name").equals(destName) && objName.getKeyProperty("SIBus").equals(busName)) {
					// destInfo.addMediationPoint(objName.getKeyProperty("name")
					// + "@" + objName.getKeyProperty("SIBMessagingEngine"));
					destInfo.addMediationPoint(objName.getKeyProperty("name"));
				}
			}

			for (Iterator i = publicationPointsSet.iterator(); i.hasNext();) {
				ObjectName objName = (ObjectName) i.next();
				if (objName.getKeyProperty("name").equals(destName) && objName.getKeyProperty("SIBus").equals(busName)) {
					// destInfo.addPublicationPoint(objName.getKeyProperty("name")
					// + "@" + objName.getKeyProperty("SIBMessagingEngine"));
					destInfo.addPublicationPoint(objName.getKeyProperty("name"));
				}
			}

			destList.add(destInfo);
		}

		logger.logp(Level.FINE, className, "getDestinations", new Integer(destList.size()).toString());
		return destList;
	}

	/**
	 * @param busName
	 * @return
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 * @throws MalformedObjectNameException
	 */
	public List<DestinationInfo> getDestinations(String busName, String queuename) throws ConfigServiceException,
			ConnectorException, MalformedObjectNameException {
		Set queuePointsSet;
		Set mediationPointsSet;
		Set publicationPointsSet;
		ArrayList<DestinationInfo> destList;
		Session session;
		boolean stopIterate = false;
		logger.logp(Level.FINE, className, "getDestinations", busName);
		destList = new ArrayList<DestinationInfo>();
		session = getConfigSession();
		ObjectName scopeArray[] = configService.resolve(session, "SIBus=" + busName);
		if (scopeArray.length != 1) {
			throw new ConfigServiceException("Bus " + busName + " not found in the configuration");
		}

		ObjectName queuePointsObjs = new ObjectName("WebSphere:name=" + queuename + ",type=SIBQueuePoint,*");
		queuePointsSet = ac.queryNames(queuePointsObjs, null);
		ObjectName mediationPointsObjs = new ObjectName("WebSphere:type=SIBMediationPoint,*");
		mediationPointsSet = ac.queryNames(mediationPointsObjs, null);
		ObjectName publicationPointsObjs = new ObjectName("WebSphere:type=SIBPublicationPoint,*");
		publicationPointsSet = ac.queryNames(publicationPointsObjs, null);

		// use of quename doesn't work DisplayName therfore we have to get all
		// queues
		ObjectName pattern = ConfigServiceHelper.createObjectName(null, "SIBAbstractDestination", null);
		// ObjectName destConfig[] = configService.queryConfigObjects(session,
		// scopeArray[0], pattern, null);

		// QueryExp query = Query.eq(Query.attr("identifier"),
		// Query.value(queuename));
		// ObjectName destConfig[] = configService.queryConfigObjects(session,
		// scopeArray[0], pattern, query);
		ObjectName destConfig[] = configService.queryConfigObjects(session, scopeArray[0], pattern, null);

		for (ObjectName element : destConfig) {
			String type = element.getKeyProperty("_Websphere_Config_Data_Type");
			AttributeList attribList = configService.getAttributes(session, element, new String[] { "identifier",
					"description", "uuid" }, false);
			String destName = (String) ((Attribute) attribList.get(0)).getValue();
			String description = (String) ((Attribute) attribList.get(1)).getValue();
			String destUuid = (String) ((Attribute) attribList.get(2)).getValue();
			String targetIdentifier = null;
			String targetBus = null;

			if (destName.equalsIgnoreCase(queuename)) {
				stopIterate = true;
			}

			if (type.equals("SIBDestinationAlias")) {
				AttributeList attribList2 = configService.getAttributes(session, element, new String[] {
						"targetIdentifier", "targetBus" }, false);
				targetIdentifier = (String) ((Attribute) attribList2.get(0)).getValue();
				targetBus = (String) ((Attribute) attribList2.get(1)).getValue();
			}

			DestinationInfo destInfo = new DestinationInfo(destName, type, description, destUuid, targetIdentifier, targetBus);
			for (Iterator i = queuePointsSet.iterator(); i.hasNext();) {
				ObjectName objName = (ObjectName) i.next();
				if (objName.getKeyProperty("name").equals(destName) && objName.getKeyProperty("SIBus").equals(busName)) {
					// destInfo.addQueuePoint(objName.getKeyProperty("name") +
					// "@" + objName.getKeyProperty("SIBMessagingEngine"));
					destInfo.addQueuePoint(objName.getKeyProperty("name"));
				}
			}

			for (Iterator i = mediationPointsSet.iterator(); i.hasNext();) {
				ObjectName objName = (ObjectName) i.next();
				if (objName.getKeyProperty("name").equals(destName) && objName.getKeyProperty("SIBus").equals(busName)) {
					// destInfo.addMediationPoint(objName.getKeyProperty("name")
					// + "@" + objName.getKeyProperty("SIBMessagingEngine"));
					destInfo.addMediationPoint(objName.getKeyProperty("name"));
				}
			}

			for (Iterator i = publicationPointsSet.iterator(); i.hasNext();) {
				ObjectName objName = (ObjectName) i.next();
				if (objName.getKeyProperty("name").equals(destName) && objName.getKeyProperty("SIBus").equals(busName)) {
					// destInfo.addPublicationPoint(objName.getKeyProperty("name")
					// + "@" + objName.getKeyProperty("SIBMessagingEngine"));
					destInfo.addPublicationPoint(objName.getKeyProperty("name"));
				}
			}

			if (stopIterate) {
				destList.add(destInfo);
				break;
			}
		}

		logger.logp(Level.FINE, className, "getDestinations", new Integer(destList.size()).toString());
		return destList;
	}

	/**
	 * @param meName
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public List getMediationPoints(String meName) throws MalformedObjectNameException, InstanceNotFoundException,
			MBeanException, ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "getMediationPoints", meName);
		ObjectName medPoints = new ObjectName("WebSphere:type=SIBMediationPoint,SIBMessagingEngine=" + meName + ",*");
		Set medPointNameSet = ac.queryNames(medPoints, null);
		ArrayList<MediationPointInfo> medPointList = new ArrayList<MediationPointInfo>();
		ObjectName obj;
		String state;
		String id;
		Long highMessageThreshold;
		Boolean sendAllowed;
		Long currentDepth;
		String reason;
		String name;
		for (Iterator i = medPointNameSet.iterator(); i.hasNext(); medPointList.add(new MediationPointInfo(name, state,
				currentDepth, highMessageThreshold, sendAllowed, id, reason, obj))) {
			obj = (ObjectName) i.next();
			logger.logp(Level.FINE, className, "getMediationPoints", "Found Mediation M-Bean:", obj);
			AttributeList attribList = ac.getAttributes(obj, new String[] { "currentState", "id", "highMessageThreshold",
					"sendAllowed", "depth" });
			state = (String) ((Attribute) attribList.get(0)).getValue();
			id = (String) ((Attribute) attribList.get(1)).getValue();
			highMessageThreshold = (Long) ((Attribute) attribList.get(2)).getValue();
			sendAllowed = (Boolean) ((Attribute) attribList.get(3)).getValue();
			currentDepth = null;
			if (attribList.size() == 5) {
				currentDepth = (Long) ((Attribute) attribList.get(4)).getValue();
			}
			reason = (String) ac.invoke(obj, "getReason", null, null);
			name = obj.getKeyProperty("name");
		}
		logger.logp(Level.FINE, className, "getMediationPoints", new Integer(medPointList.size()).toString());
		return medPointList;
	}

	/**
	 * @param meName
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public MediationPointInfo getMediationPoints(String meName, String medname) throws MalformedObjectNameException,
			InstanceNotFoundException, MBeanException, ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "getMediationPoints", meName);
		ObjectName medPoints = new ObjectName("WebSphere:name=" + medname + ",type=SIBMediationPoint,SIBMessagingEngine="
				+ meName + ",*");
		Set medPointNameSet = ac.queryNames(medPoints, null);
		MediationPointInfo medPointInfo = new MediationPointInfo();
		ObjectName obj;
		String state;
		String id;
		Long highMessageThreshold;
		Boolean sendAllowed;
		Long currentDepth;
		String reason;
		String name;
		Iterator i = medPointNameSet.iterator();
		while (i.hasNext()) {
			obj = (ObjectName) i.next();
			logger.logp(Level.FINE, className, "getMediationPoints", "Found Mediation M-Bean:", obj);
			AttributeList attribList = ac.getAttributes(obj, new String[] { "currentState", "id", "highMessageThreshold",
					"sendAllowed", "depth" });
			state = (String) ((Attribute) attribList.get(0)).getValue();
			id = (String) ((Attribute) attribList.get(1)).getValue();
			highMessageThreshold = (Long) ((Attribute) attribList.get(2)).getValue();
			sendAllowed = (Boolean) ((Attribute) attribList.get(3)).getValue();
			currentDepth = null;
			if (attribList.size() == 5) {
				currentDepth = (Long) ((Attribute) attribList.get(4)).getValue();
			}
			reason = (String) ac.invoke(obj, "getReason", null, null);
			name = obj.getKeyProperty("name");

			medPointInfo.setId(id);
			medPointInfo.setHighMessageThreshold(highMessageThreshold);
			medPointInfo.setReason(reason);
			medPointInfo.setName(name);
			medPointInfo.setCurrentDepth(currentDepth);
			medPointInfo.setState(state);
			medPointInfo.setSendAllowed(sendAllowed);
		}
		logger.logp(Level.FINE, className, "getMediationPoints", "Done");
		return medPointInfo;
	}

	/**
	 * @param meName
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public List getPublicationPoints(String meName) throws MalformedObjectNameException, InstanceNotFoundException,
			ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "getPublicationPoints", meName);
		ObjectName pubPoints = new ObjectName("WebSphere:type=SIBPublicationPoint,SIBMessagingEngine=" + meName + ",*");
		Set pubNameSet = ac.queryNames(pubPoints, null);
		ArrayList<PublicationPointInfo> pubList = new ArrayList<PublicationPointInfo>();
		String id;
		Long highMessageThreshold;
		Boolean sendAllowed;
		String name;
		for (Iterator i = pubNameSet.iterator(); i.hasNext(); pubList.add(new PublicationPointInfo(name, highMessageThreshold,
				sendAllowed, id))) {
			ObjectName obj = (ObjectName) i.next();
			logger.logp(Level.FINE, className, "Found Publication M-Bean:", obj.toString());
			AttributeList attribList = ac.getAttributes(obj, new String[] { "id", "highMessageThreshold", "sendAllowed" });
			id = (String) ((Attribute) attribList.get(0)).getValue();
			highMessageThreshold = (Long) ((Attribute) attribList.get(1)).getValue();
			sendAllowed = (Boolean) ((Attribute) attribList.get(2)).getValue();
			name = obj.getKeyProperty("name");
		}

		logger.logp(Level.FINE, className, "getPublicationPoints", new Integer(pubList.size()).toString());
		return pubList;
	}

	/**
	 * @param meName
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public MEInfo getMEInfo(String meName) throws MalformedObjectNameException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "getMEInfo", meName);
		ObjectName sibMain = new ObjectName("WebSphere:type=SIBMessagingEngine,name=" + meName + ",*");
		Set meSet = ac.queryNames(sibMain, null);
		Iterator i = meSet.iterator();
		ObjectName meObj = (ObjectName) i.next();
		String haGroup = ac.invoke(meObj, "getHAGroupName", null, null).toString();
		String state = (String) ac.invoke(meObj, "state", null, null);
		boolean started = ((Boolean) ac.invoke(meObj, "isStarted", null, null)).booleanValue();
		MEInfo me = new MEInfo(meName, haGroup, state, started, meObj.getKeyProperty("process"), meObj.getKeyProperty("node"),
				meObj.getKeyProperty("cell"), meObj);
		logger.logp(Level.FINE, className, "getMEInfo", me.toString());
		return me;
	}

	/**
	 * @param busName
	 * @return
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 */
	public BusInfo getBusInfo(String busName) throws ConfigServiceException, ConnectorException {
		logger.logp(Level.FINE, className, "getBusInfo", busName);
		Session session = null;
		BusInfo busInfo = null;
		session = getConfigSession();
		ObjectName bus = configService.resolve(session, "SIBus=" + busName)[0];
		AttributeList attribList = configService.getAttributes(session, bus, new String[] { "name", "description",
				"configurationReloadEnabled", "secure" }, false);
		String name = (String) ((Attribute) attribList.get(0)).getValue();
		String description = (String) ((Attribute) attribList.get(1)).getValue();
		Boolean configReload = (Boolean) ((Attribute) attribList.get(2)).getValue();
		Boolean secure = (Boolean) ((Attribute) attribList.get(3)).getValue();
		busInfo = new BusInfo(name, description, secure, configReload);
		logger.logp(Level.FINE, className, "getBusInfo", busInfo.toString());
		return busInfo;
	}

	/**
	 * @param memberInfo
	 * @param queueName
	 * @throws CommandException
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 * @throws com.ibm.websphere.management.cmdframework.CommandException
	 * @throws CommandMgrInitException
	 */
	public void createQueue(BusMemberInfo memberInfo, String queueName) throws CommandException, ConfigServiceException,
			ConnectorException, CommandMgrInitException, com.ibm.websphere.management.cmdframework.CommandException {
		logger.logp(Level.FINE, className, "createQueue", (new Object[] { memberInfo, queueName }).toString());
		createDestination(memberInfo, memberInfo.getBusName(), queueName, "Queue", null, null);
		logger.logp(Level.FINE, className, "createQueue", "done");
	}

	/**
	 * @param busName
	 * @param aliasName
	 * @param targetIdentifier
	 * @param targetBus
	 * @throws CommandException
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 * @throws com.ibm.websphere.management.cmdframework.CommandException
	 * @throws CommandMgrInitException
	 */
	public void createAliasDestination(String busName, String aliasName, String targetIdentifier, String targetBus)
			throws CommandException, ConfigServiceException, ConnectorException, CommandMgrInitException,
			com.ibm.websphere.management.cmdframework.CommandException {
		logger.logp(Level.FINE, className, "createAliasDestination", (new Object[] { busName, aliasName, targetIdentifier,
				targetBus }).toString());
		createDestination(null, busName, aliasName, "Alias", targetIdentifier, targetBus);
		logger.logp(Level.FINE, className, "createAliasDestination", "done");
	}

	/**
	 * @param memberInfo
	 * @param busName
	 * @param destName
	 * @param destType
	 * @param targetIdentifier
	 * @param targetBus
	 * @throws CommandException
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 * @throws com.ibm.websphere.management.cmdframework.CommandException
	 * @throws CommandMgrInitException
	 */
	private void createDestination(BusMemberInfo memberInfo, String busName, String destName, String destType,
			String targetIdentifier, String targetBus) throws CommandException, ConfigServiceException, ConnectorException,
			CommandMgrInitException, com.ibm.websphere.management.cmdframework.CommandException {
		logger.logp(Level.FINE, className, "createDestination", (new Object[] { memberInfo, busName, destName, destType,
				targetIdentifier, targetBus }).toString());
		AdminCommand cmd = getCommandManager().createCommand("createSIBDestination");
		if (memberInfo != null) {
			if (memberInfo.isClustered()) {
				cmd.setParameter("cluster", memberInfo.getClusterName());
			} else {
				cmd.setParameter("node", memberInfo.getNodeName());
				cmd.setParameter("server", memberInfo.getServerName());
			}
		}
		if (targetIdentifier != null && targetBus != null) {
			cmd.setParameter("aliasBus", busName);
			cmd.setParameter("targetName", targetIdentifier);
			cmd.setParameter("targetBus", targetBus);
		}
		cmd.setParameter("bus", busName);
		cmd.setParameter("type", destType);
		cmd.setParameter("name", destName);
		cmd.setParameter("description", "Created by Service Integration Bus Explorer");
		executeCommand(cmd);
		logger.logp(Level.FINE, className, "createDestination", "done");
	}

	/**
	 * @param busName
	 * @param destName
	 * @throws CommandException
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 * @throws CommandNotFoundException
	 * @throws com.ibm.websphere.management.cmdframework.CommandException
	 */
	public void deleteDestination(String busName, String destName) throws CommandException, ConfigServiceException,
			ConnectorException, CommandNotFoundException, com.ibm.websphere.management.cmdframework.CommandException {
		logger.logp(Level.FINE, className, "deleteDestination", (new Object[] { busName, destName }).toString());
		AdminCommand cmd = getCommandManager().createCommand("deleteSIBDestination");
		cmd.setParameter("bus", busName);
		cmd.setParameter("name", destName);
		executeCommand(cmd);
		logger.logp(Level.FINE, className, "deleteDestination", "done");
	}

	/**
	 * use AdminTask to get the destination info
	 * 
	 * @param busName
	 * @param destName
	 * @return
	 * @throws CommandException
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 * @throws com.ibm.websphere.management.cmdframework.CommandException
	 * @throws CommandMgrInitException
	 */
	public Hashtable showSIBDestination(String busName, String destName) throws CommandException, ConfigServiceException,
			ConnectorException, CommandMgrInitException, com.ibm.websphere.management.cmdframework.CommandException {
		logger.logp(Level.FINE, className, "showSIBDestination", (new Object[] { busName, destName }).toString());
		AdminCommand cmd = getCommandManager().createCommand("showSIBDestination");
		cmd.setParameter("bus", busName);
		cmd.setParameter("name", destName);
		Hashtable res = (Hashtable) executeCommand(cmd);
		logger.logp(Level.FINE, className, "showSIBDestination", res.toString());
		return res;
	}

	/**
	 * use AdminTask to get the sibus info
	 * 
	 * @param busName
	 * @return
	 * @throws CommandException
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 * @throws CommandMgrInitException
	 * @throws com.ibm.websphere.management.cmdframework.CommandException
	 */
	public Hashtable showSIBus(String busName) throws CommandException, ConfigServiceException, ConnectorException,
			CommandMgrInitException, com.ibm.websphere.management.cmdframework.CommandException {
		logger.logp(Level.FINE, className, "showSIBus", busName);
		AdminCommand cmd = getCommandManager().createCommand("showSIBus");
		cmd.setParameter("bus", busName);
		Hashtable res = (Hashtable) executeCommand(cmd);
		logger.logp(Level.FINE, className, "showSIBus", res.toString());
		return res;
	}

	/**
	 * @param objName
	 * @param methodName
	 * @param params
	 * @param signature
	 * @return
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	private Object invokeMethod(ObjectName objName, String methodName, Object params[], String signature[])
			throws InstanceNotFoundException, MBeanException, ReflectionException, ConnectorException {
		logger
				.logp(Level.FINE, className, "invokeMethod", (new Object[] { objName, methodName, params, signature })
						.toString());
		Object returnValue = null;
		Set mbeanSet = ac.queryNames(objName, null);
		if (mbeanSet.size() != 0) {
			returnValue = ac.invoke((ObjectName) mbeanSet.iterator().next(), methodName, params, signature);
		}
		if (returnValue != null) {
			logger.logp(Level.FINE, className, "invokeMethod", returnValue.toString());
		} else {
			logger.logp(Level.FINE, className, "invokeMethod", "returnValue=empty");
		}
		return returnValue;
	}

	/**
	 * @param server
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public void stopServer(ServerInfo server) throws InstanceNotFoundException, MBeanException, ReflectionException,
			ConnectorException {
		logger.logp(Level.FINE, className, "stopServer", server.toString());
		ac.invoke(server.getMBean(), "stop", null, null);
		logger.logp(Level.FINE, className, "stopServer", "done");
	}

	/**
	 * @param server
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public void restartServer(ServerInfo server) throws InstanceNotFoundException, MBeanException, ReflectionException,
			ConnectorException {
		logger.logp(Level.FINE, className, "restartServer", server.toString());
		ac.invoke(server.getMBean(), "restart", null, null);
		logger.logp(Level.FINE, className, "restartServer", "done");
	}

	/**
	 * @param server
	 * @return
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public String getServerProductDetails(ServerInfo server) throws InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "getServerProductVersions", server.toString());
		String versionInfos[] = (String[]) ac.invoke(server.getMBean(), "getVersionsForAllProducts", null, null);
		StringBuffer ret = null;
		for (String element : versionInfos) {
			String productNameTag = "<product name=\"";
			int pos1 = element.indexOf(productNameTag);
			if (pos1 != -1) {
				int pos2 = element.indexOf("\">", pos1 + 1);
				if (pos2 != -1) {
					if (ret == null) {
						ret = new StringBuffer();
					} else {
						ret.append(", ");
					}
					ret.append(element.substring(pos1 + productNameTag.length(), pos2));
					String levelTag = "level=\"";
					int pos3 = element.indexOf(levelTag);
					if (pos3 != -1) {
						int pos4 = element.indexOf("\"", pos3 + levelTag.length() + 1);
						if (pos4 != -1) {
							ret.append(" [");
							ret.append(element.substring(pos3 + levelTag.length(), pos4));
							ret.append("]");
						}
					}
				}
			}
		}
		logger.logp(Level.FINE, className, "getServerProductVersions", ret.toString());
		return ret.toString();
	}

	/**
	 * @param server
	 * @throws MalformedObjectNameException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public void dumpThreads(ServerInfo server) throws MalformedObjectNameException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		logger.logp(Level.WARNING, className, "dumpThreads", server.toString());
		ObjectName jvmObj = new ObjectName("WebSphere:type=JVM,process=" + server.getServerName() + ",node="
				+ server.getNodeName() + ",cell=" + server.getCellName() + ",*");
		Set jvmObjs = ac.queryNames(jvmObj, null);
		ObjectName jvm = (ObjectName) jvmObjs.iterator().next();
		ac.invoke(jvm, "dumpThreads", null, null);
		logger.logp(Level.WARNING, className, "dumpThreads", "done");
	}

	/**
	 * @param server
	 * @return
	 */
	public String getTraceString(ServerInfo server) {
		logger.logp(Level.FINE, className, "getTraceString", server.toString());
		String traceSpec = null;
		try {
			ObjectName traceObj = new ObjectName("WebSphere:type=TraceService,process=" + server.getServerName() + ",node="
					+ server.getNodeName() + ",cell=" + server.getCellName() + ",*");
			Set traceObjs = ac.queryNames(traceObj, null);
			ObjectName trace = (ObjectName) traceObjs.iterator().next();
			traceSpec = (String) ac.getAttribute(trace, "traceSpecification");
		} catch (Exception e) {
			logger.logp(Level.SEVERE, className, "getTraceString", "Unable to get trace String", e);
		}
		logger.logp(Level.FINE, className, "getTraceString", traceSpec.toString());
		return traceSpec;
	}

	/**
	 * @param server
	 * @param traceSpec
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 * @throws InvalidAttributeValueException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 * @throws MalformedObjectNameException
	 */
	public void setTraceString(ServerInfo server, String traceSpec) throws InstanceNotFoundException,
			AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException,
			ConnectorException, MalformedObjectNameException {
		logger.logp(Level.FINE, className, "setTraceString", (new Object[] { server, traceSpec }).toString());
		ObjectName traceObj = new ObjectName("WebSphere:type=TraceService,process=" + server.getServerName() + ",node="
				+ server.getNodeName() + ",cell=" + server.getCellName() + ",*");
		Set traceObjs = ac.queryNames(traceObj, null);
		ObjectName trace = (ObjectName) traceObjs.iterator().next();
		Attribute att = new Attribute("traceSpecification", traceSpec);
		ac.setAttribute(trace, att);
		logger.logp(Level.FINE, className, "setTraceString", "done");
	}

	/**
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws ConnectorException
	 * @throws InstanceNotFoundException
	 * @throws ReflectionException
	 */
	public ServerInfo[] getServersInfo() throws MalformedObjectNameException, NullPointerException, ConnectorException,
			InstanceNotFoundException, ReflectionException {
		logger.logp(Level.FINE, className, "getServersInfo", "Init");
		if (serverInfoCache == null) {
			serverInfoCache = _getServersInfo();
		}
		logger.logp(Level.FINE, className, "getServersInfo", serverInfoCache.toString());
		return serverInfoCache;
	}

	/**
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws ConnectorException
	 * @throws InstanceNotFoundException
	 * @throws ReflectionException
	 */
	private ServerInfo[] _getServersInfo() throws MalformedObjectNameException, NullPointerException, ConnectorException,
			InstanceNotFoundException, ReflectionException {
		ObjectName serversObj = new ObjectName("WebSphere:type=Server,*");
		Set serversObjs = ac.queryNames(serversObj, null);
		ServerInfo servers[] = new ServerInfo[serversObjs.size()];
		int x = 0;
		for (Iterator i = serversObjs.iterator(); i.hasNext();) {
			ObjectName server = (ObjectName) i.next();
			AttributeList list = ac.getAttributes(server, new String[] { "name", "pid", "cellName", "nodeName",
					"platformVersion", "processType" });
			servers[x] = new ServerInfo((String) ((Attribute) list.get(0)).getValue(), (String) ((Attribute) list.get(1))
					.getValue(), (String) ((Attribute) list.get(2)).getValue(), (String) ((Attribute) list.get(3)).getValue(),
					(String) ((Attribute) list.get(4)).getValue(), (String) ((Attribute) list.get(5)).getValue(), server);
			x++;
		}
		Arrays.sort(servers);
		logger.logp(Level.FINE, className, "_getServersInfo", servers.toString());
		return servers;
	}

	/**
	 * @param linkName
	 * @param channelName
	 * @param queueManagerName
	 * @param meInfo
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 */
	public void createMQClientLink(String linkName, String channelName, String queueManagerName, MEInfo meInfo)
			throws ConfigServiceException, ConnectorException {
		logger.logp(Level.FINE, className, "createMQClientLink", (new Object[] { linkName, channelName, queueManagerName,
				meInfo }).toString());
		Session session = getConfigSession();
		ObjectName scope = configService.resolve(session, "SIBMessagingEngine=" + meInfo.getName())[0];
		AttributeList attributes = new AttributeList();
		ConfigServiceHelper.setAttributeValue(attributes, "channelName", channelName);
		ConfigServiceHelper.setAttributeValue(attributes, "name", linkName);
		ConfigServiceHelper.setAttributeValue(attributes, "qmName", queueManagerName);
		ObjectName mqClientLink = configService.createConfigData(session, scope, "mqClientLink", "SIBMQClientLink", attributes);
		logger.logp(Level.FINE, className, "Created MQ Client Link: ", mqClientLink.toString());
		configService.save(session, true);
		logger.logp(Level.FINE, className, "createMQClientLink", "done");
	}

	/**
	 * @param mqClientLinkName
	 * @throws ConfigServiceException
	 * @throws ConnectorException
	 */
	public void deleteMQClientLink(String mqClientLinkName) throws ConfigServiceException, ConnectorException {
		logger.logp(Level.FINE, className, "deleteMQClientLink", mqClientLinkName.toString());
		Session session = getConfigSession();
		ObjectName obj = configService.resolve(session, "SIBMQClientLink=" + mqClientLinkName)[0];
		configService.deleteConfigData(session, obj);
		configService.save(session, true);
		logger.logp(Level.FINE, className, "deleteMQClientLink", "done");
	}

	/**
	 * @param meInfo
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws ConfigServiceException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public MQClientLinkInfo[] getMQClientLinks(MEInfo meInfo) throws MalformedObjectNameException, ConfigServiceException,
			InstanceNotFoundException, MBeanException, ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "getMQClientLinks", meInfo.toString());
		Session session = getConfigSession();
		ObjectName scope = configService.resolve(session, "SIBMessagingEngine=" + meInfo.getName())[0];
		ObjectName pattern = ConfigServiceHelper.createObjectName(null, "SIBMQClientLink");
		ObjectName mqClientLinkConfig[] = configService.queryConfigObjects(session, scope, pattern, null);
		MQClientLinkInfo links[] = new MQClientLinkInfo[mqClientLinkConfig.length];
		for (int x = 0; x < mqClientLinkConfig.length; x++) {
			ObjectName name = new ObjectName("WebSphere:mbeanIdentifier="
					+ mqClientLinkConfig[x].getKeyProperty("_Websphere_Config_Data_Id").replace('|', '/') + ",*");
			Set names = ac.queryNames(name, null);
			AttributeList attribList = configService.getAttributes(session, mqClientLinkConfig[x], new String[] {
					"channelName", "defaultQM" }, false);
			String channelName = (String) ((Attribute) attribList.get(0)).getValue();
			boolean defaultQM = ((Boolean) ((Attribute) attribList.get(1)).getValue()).booleanValue();
			if (names.size() != 0) {
				ObjectName mqClientLinkMBean = (ObjectName) names.iterator().next();
				links[x] = new MQClientLinkInfo(mqClientLinkMBean.getKeyProperty("name"), (String) ac.invoke(mqClientLinkMBean,
						"getOverallStatus", null, null), (String) ac.invoke(mqClientLinkMBean, "getQueueManagerName", null,
						null), channelName, defaultQM, mqClientLinkMBean);
			}
		}
		configService.discard(session);
		logger.logp(Level.FINE, className, "getMQClientLinks", links.toString());
		return links;
	}

	/**
	 * @param meInfo
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws ConfigServiceException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public MQLinkInfo[] getMQLinks(MEInfo meInfo) throws MalformedObjectNameException, ConfigServiceException,
			InstanceNotFoundException, MBeanException, ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "getMQLinks", meInfo.toString());
		Session session = getConfigSession();
		ObjectName scope = configService.resolve(session, "SIBMessagingEngine=" + meInfo.getName())[0];
		ObjectName pattern = ConfigServiceHelper.createObjectName(null, "SIBMQLink");
		ObjectName mqLinkConfig[] = configService.queryConfigObjects(session, scope, pattern, null);
		MQLinkInfo links[] = new MQLinkInfo[mqLinkConfig.length];
		for (int x = 0; x < mqLinkConfig.length; x++) {
			AttributeList attribList = configService.getAttributes(session, mqLinkConfig[x], new String[] { "qmName" }, false);
			String qmName = (String) ((Attribute) attribList.get(0)).getValue();
			ObjectName name = new ObjectName("WebSphere:mbeanIdentifier="
					+ mqLinkConfig[x].getKeyProperty("_Websphere_Config_Data_Id").replace('|', '/') + ",*");
			Set names = ac.queryNames(name, null);
			if (names.size() != 0) {
				ObjectName mqLinkMBean = (ObjectName) names.iterator().next();
				links[x] = new MQLinkInfo(meInfo, mqLinkMBean.getKeyProperty("name"), (String) ac.invoke(mqLinkMBean,
						"getOverallStatus", null, null), qmName, mqLinkMBean);
			}
		}
		configService.discard(session);
		logger.logp(Level.FINE, className, "getMQLinks", links.toString());
		return links;
	}

	/**
	 * @param mqLinkInfo
	 * @param methodName
	 * @param senderChannel
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws ConfigServiceException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public Object getMQLinkChannelInfo(MQLinkInfo mqLinkInfo, String methodName, boolean senderChannel)
			throws MalformedObjectNameException, ConfigServiceException, InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		Object r = invokeMQLinkChannelMethod(mqLinkInfo, methodName, senderChannel, null, null);
		return r;
	}

	/**
	 * @param mqLinkInfo
	 * @param methodName
	 * @param senderChannel
	 * @param args
	 * @param params
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws ConfigServiceException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	private Object invokeMQLinkChannelMethod(MQLinkInfo mqLinkInfo, String methodName, boolean senderChannel, Object args[],
			String params[]) throws MalformedObjectNameException, ConfigServiceException, InstanceNotFoundException,
			MBeanException, ReflectionException, ConnectorException {
		ObjectName channelMBean = null;
		if (senderChannel) {
			channelMBean = mqLinkInfo.getSenderChannelMBean();
		} else {
			channelMBean = mqLinkInfo.getReceiverChannelMBean();
		}

		if (channelMBean == null) {
			Session session = getConfigSession();
			ObjectName scope = configService.resolve(session, "SIBMQLink=" + mqLinkInfo.getName())[0];
			ObjectName pattern = null;
			if (senderChannel) {
				pattern = ConfigServiceHelper.createObjectName(null, "SIBMQLinkSenderChannel");
			} else {
				pattern = ConfigServiceHelper.createObjectName(null, "SIBMQLinkReceiverChannel");
			}
			ObjectName mqLinkChannelConfig[] = configService.queryConfigObjects(session, scope, pattern, null);
			ObjectName name = new ObjectName("WebSphere:mbeanIdentifier="
					+ mqLinkChannelConfig[0].getKeyProperty("_Websphere_Config_Data_Id").replace('|', '/') + ",*");
			Set names = ac.queryNames(name, null);
			if (names.size() != 0) {
				channelMBean = (ObjectName) names.iterator().next();
			}
			configService.discard(session);
		}
		if (senderChannel) {
			mqLinkInfo.setSenderChannelMBean(channelMBean);
		} else {
			mqLinkInfo.setReceiverChannelMBean(channelMBean);
		}
		Object returnData = null;
		if (channelMBean != null) {
			returnData = invokeMethod(channelMBean, methodName, args, params);
		}
		return returnData;
	}

	/**
	 * @param linkInfo
	 * @return
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public List getMQClientLinkConnections(MQClientLinkInfo linkInfo) throws InstanceNotFoundException, MBeanException,
			ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "getMQClientLinkConnections", linkInfo.toString());
		List r = (List) invokeMethod(linkInfo.getMBean(), "getConnectionStatus", null, null);
		logger.logp(Level.FINE, className, "getMQClientLinkConnections", r.toString());
		return r;
	}

	/**
	 * @param link
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public void startMQClientLink(MQClientLinkInfo link) throws InstanceNotFoundException, MBeanException, ReflectionException,
			ConnectorException {
		logger.logp(Level.FINE, className, "startMQClientLink", link.toString());
		invokeMethod(link.getMBean(), "startLink", null, null);
		logger.logp(Level.FINE, className, "startMQClientLink", "done");
	}

	/**
	 * @param link
	 * @param stopMode
	 * @param targetState
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws ConnectorException
	 */
	public void stopMQClientLink(MQClientLinkInfo link, String stopMode, String targetState) throws InstanceNotFoundException,
			MBeanException, ReflectionException, ConnectorException {
		logger.logp(Level.FINE, className, "stopMQClientLink", (new Object[] { link, stopMode, targetState }).toString());
		invokeMethod(link.getMBean(), "stopLink", new Object[] { stopMode, targetState }, new String[] { "java.lang.String",
				"java.lang.String" });
		logger.logp(Level.FINE, className, "topMQClientLink", "done");
	}

	/**
	 * @return
	 */
	public boolean isConfigServiceAvailable() {
		logger.logp(Level.FINE, className, "isConfigServiceAvailable", "Init");
		boolean isConfigServiceAvailable = configService != null;
		logger.logp(Level.FINE, className, "isConfigServiceAvailable", new Boolean(isConfigServiceAvailable).toString());
		return isConfigServiceAvailable;
	}

	/**
	 * @return
	 * @throws CommandMgrInitException
	 */
	private CommandMgr getCommandManager() throws CommandMgrInitException {
		logger.logp(Level.FINE, className, "getCommandManager", "Init");
		CommandMgr cmdMgr = CommandMgr.getCommandMgr(ac);
		logger.logp(Level.FINE, className, "getCommandManager", cmdMgr.toString());
		return cmdMgr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "AdminHelper@" + Integer.toHexString(System.identityHashCode(this)) + "{ adminClient=" + ac + ", configService="
				+ configService + "}";
	}

	/**
	 * @param time
	 * @return
	 */
	public String getTimestamp(Long time, String dateFormat) {
		if (time == null) {
			return "";
		} else {
			Date date = new Date(time.longValue());
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
			formatter.setTimeZone(TimeZone.getDefault());
			return formatter.format(date);
		}
	}

}
