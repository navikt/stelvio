/**
 * 
 */
package no.nav.sibushelper.helper;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.QueueConnection;

import no.nav.sibushelper.SIBUSHelper;
import no.nav.sibushelper.common.Constants;

import com.ibm.websphere.sib.Reliability;
import com.ibm.websphere.sib.SIApiConstants;
import com.ibm.websphere.sib.SIDestinationAddress;
import com.ibm.websphere.sib.SIDestinationAddressFactory;
import com.ibm.websphere.sib.api.jms.JmsFactoryFactory;
import com.ibm.websphere.sib.api.jms.JmsQueueConnectionFactory;
import com.ibm.websphere.sib.exception.SIException;
import com.ibm.websphere.sib.exception.SINotPossibleInCurrentConfigurationException;
import com.ibm.ws.sib.mfp.JmsBodyType;
import com.ibm.ws.sib.mfp.JsJmsBytesMessage;
import com.ibm.ws.sib.mfp.JsJmsMapMessage;
import com.ibm.ws.sib.mfp.JsJmsMessage;
import com.ibm.ws.sib.mfp.JsJmsTextMessage;
import com.ibm.wsspi.sib.core.BrowserSession;
import com.ibm.wsspi.sib.core.ConsumerSession;
import com.ibm.wsspi.sib.core.SIBusMessage;
import com.ibm.wsspi.sib.core.SICoreConnection;
import com.ibm.wsspi.sib.core.SICoreConnectionFactory;
import com.ibm.wsspi.sib.core.SelectionCriteria;
import com.ibm.wsspi.sib.core.SelectionCriteriaFactory;
import com.ibm.wsspi.sib.core.SelectorDomain;
import com.ibm.wsspi.sib.core.selector.FactoryType;
import com.ibm.wsspi.sib.core.selector.SICoreConnectionFactorySelector;
import commonj.sdo.DataObject;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class MessagingHelperImpl implements MessagingHelper {

	private static Logger logger = Logger.getLogger(SIBUSHelper.class.getName());
	private String className = MessagingHelperImpl.class.getName();
	
    private String hostName;
    private int port;
    private String chainName;
    private String userName;
    private String password;
    
    private String keyStoreLocation;
    private String trustStoreLocation;
    private String keyStorePassword;
    private String trustStorePassword;
    
    private static Object connectionLock = new Object();
    
    /**
	 * @param hostName
	 * @param port
	 * @param chainName
	 * @param userName
	 * @param password
	 */
    public MessagingHelperImpl(String hostName, int port, String chainName, String userName, String password)
    {
        this.hostName = "localhost";
        this.port = 7276;
        this.chainName = "BootstrapBasicMessaging";
        this.userName = null;
        this.password = null;

        this.hostName = hostName;
        this.port = port;
        this.chainName = chainName;
        this.userName = userName;
        this.password = password;
    }

    /**
	 * @param hostName
	 * @param port
	 * @param chainName
	 * @param userName
	 * @param password
	 * @param trustStore
	 * @param trustStorePassword
	 * @param keyStore
	 * @param keyStorePassword
	 */
    public MessagingHelperImpl(String hostName, int port, String chainName, String userName, String password, String trustStore, String trustStorePassword, String keyStore, String keyStorePassword)
    {
        this.hostName = "localhost";
        this.port = 7276;
        this.chainName = "BootstrapBasicMessaging";
        this.userName = null;
        this.password = null;
        this.keyStoreLocation = "";
        this.trustStoreLocation = "";
        this.keyStorePassword = "";
        this.trustStorePassword = "";
      
        this.hostName = hostName;
        this.port = port;
        this.chainName = chainName;
        this.userName = userName;
        this.password = password;
        this.trustStoreLocation = trustStore;
        this.trustStorePassword = trustStorePassword;
        this.keyStoreLocation = keyStore;
        this.keyStorePassword = keyStorePassword;
    }

    /**
	 * @param meName
	 * @param busName
	 * @return
	 * @throws SIException
	 */
    private SICoreConnection getConnectionToME(String meName, String busName) throws SIException
    {
    	logger.logp(Level.FINE, className, "getConnectionToME", (new Object[] {meName, busName}).toString());
    	Hashtable<String, String> props = new Hashtable<String, String>();
    	props.put("busName", busName);
    	props.put("providerEndpoints", hostName + ":" + port + ":" + chainName);
    	if(meName != null)
    	{
    		props.put("targetType", "ME");
    		props.put("targetGroup", meName);
    		props.put("targetSignificance", "Required");
    	}
    	SICoreConnectionFactory factory = SICoreConnectionFactorySelector.getSICoreConnectionFactory(FactoryType.TRM_CONNECTION);
		logger.logp(Level.FINE, className, "getConnectionToME", "Connecting with:", props);
		SICoreConnection conn = factory.createConnection(userName, password, props);
		logger.logp(Level.FINE, className, "getConnectionToME", conn.toString());
		return conn;
    }

    /**
	 * @param meName
	 * @param busName
	 * @return
	 * @throws MessagingServiceNotAvailableException
	 */
    @SuppressWarnings("unused")
	private QueueConnection getJMSConnectionToME(String meName, String busName) throws MessagingServiceNotAvailableException
    {
    	QueueConnection conn = null;
    	try
    	{
    		JmsQueueConnectionFactory fac = JmsFactoryFactory.getInstance().createQueueConnectionFactory();
    		fac.setBusName(busName);
    		fac.setProviderEndpoints(hostName + ":" + port + ":" + chainName);
    		fac.setReadAhead("AlwaysOn");
    		if(meName != null)
    		{
    			fac.setTargetType("ME");
    			fac.setTarget(meName);
    			fac.setTargetSignificance("Required");
    		}
    		if(chainName.equals("BootstrapSecureMessaging"))
    			fac.setTargetTransportChain("InboundSecureMessaging");
        
    		synchronized(connectionLock)
    		{
    			if(trustStoreLocation != null || keyStoreLocation != null)
    			{
    				System.setProperty("com.ibm.ssl.enableSignerExchangePrompt", "true");
    				System.setProperty("com.ibm.ssl.keyStore", keyStoreLocation);
    				System.setProperty("com.ibm.ssl.keyStorePassword", keyStorePassword);
    				System.setProperty("com.ibm.ssl.keyStoreFileBased", "true");
    				if(keyStoreLocation.endsWith(".p12") || keyStoreLocation.endsWith(".P12"))
    					System.setProperty("com.ibm.ssl.keyStoreType", "PKCS12");
    				else
    					System.setProperty("com.ibm.ssl.keyStoreType", "JKS");
    				System.setProperty("com.ibm.ssl.trustStore", trustStoreLocation);
    				System.setProperty("com.ibm.ssl.trustStorePassword", trustStorePassword);
    				System.setProperty("com.ibm.ssl.trustStoreFileBased", "true");
    				if(trustStoreLocation.endsWith(".p12") || trustStoreLocation.endsWith(".P12"))
    					System.setProperty("com.ibm.ssl.trustStoreType", "PKCS12");
    				else
    					System.setProperty("com.ibm.ssl.trustStoreType", "JKS");
    			}
    			conn = fac.createQueueConnection(userName, password);
    			Properties systemProps = System.getProperties();
    			systemProps.remove("com.ibm.ssl.enableSignerExchangePrompt");
    			systemProps.remove("com.ibm.ssl.keyStore");
    			systemProps.remove("com.ibm.ssl.keyStorePassword");
    			systemProps.remove("com.ibm.ssl.keyStoreFileBased");
    			systemProps.remove("com.ibm.ssl.keyStoreType");
    			systemProps.remove("com.ibm.ssl.trustStore");
    			systemProps.remove("com.ibm.ssl.trustStorePassword");
    			systemProps.remove("com.ibm.ssl.trustStoreFileBased");
    			systemProps.remove("com.ibm.ssl.trustStoreType");
        	}
    	}
    	catch(JMSException e)
    	{
    		throw new MessagingServiceNotAvailableException(e);
    	}
    	return conn;
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.nav.sibushelper.helper.MessagingHelper#browseSingleMessage(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public MessageInfo browseSingleMessage(String busName, String meName, String queueName, String msgId) throws MessagingOperationFailedException
	{
		SICoreConnection conn=null;
		BrowserSession browserSession=null;
		MessageInfo singlemesg = new MessageInfo(); 
		String msgSelector = "JMSMessageID = 'ID:" + msgId + "'";
		try {

	    	   logger.logp(Level.FINE, className, "browseSingleMessage()", "Connect to " + busName+":"+meName);
	    	   conn = getConnectionToME(meName,busName);
	    	   logger.logp(Level.FINE, className, "browseSingleMessage", "Connect to " + busName+":"+meName+ " done.");
	    	   SIDestinationAddressFactory addrFact = SIDestinationAddressFactory.getInstance();
	           com.ibm.websphere.sib.SIDestinationAddress addr = addrFact.createSIDestinationAddress(queueName, false);
	           logger.logp(Level.FINE, className, "browseSingleMessage()", "Start single browse queue " + queueName);
	           SelectionCriteriaFactory selcFact =  SelectionCriteriaFactory.getInstance();
	           SelectionCriteria seCrit = selcFact.createSelectionCriteria(null, msgSelector, SelectorDomain.JMS);
	           browserSession = conn.createBrowserSession(addr, null, seCrit, null);
        	   SIBusMessage msg = browserSession.next();
        	   
        	   if(msg == null) 
        	   {
        		   logger.logp(Level.INFO, className, "browseSingleMessage()", "NO MESSAGE FOUND for " + msgId);
        		   return null;
        	   }
        		   

        	   if (msg instanceof JsJmsMessage) 
        	   {
        		   JsJmsMessage jmsmsg = (JsJmsMessage) msg;
	        		   
        		   // basics
        		   singlemesg.setApiMessageId(jmsmsg.getApiMessageId());
        		   singlemesg.setSystemMessageId(jmsmsg.getSystemMessageId());
        		   singlemesg.setCorrelationId(jmsmsg.getCorrelationId());
        		   singlemesg.setApiUserId(jmsmsg.getApiUserId());
        		   singlemesg.setSysUserId(jmsmsg.getSecurityUserid());
        		   
        		   // type
        		   singlemesg.setBusName(busName);
        		   if (jmsmsg.getJsMessageType() != null)
        			   singlemesg.setMessageType(jmsmsg.getJsMessageType().toString());
        		   JmsBodyType bdtype = jmsmsg.getBodyType();
        		   if (bdtype != null)
        			   singlemesg.setMessageBodyType(bdtype.toString());
	        		   
        		   // exception
        		   singlemesg.setExceptionMessage(jmsmsg.getExceptionMessage());
        		   singlemesg.setExceptionReason(jmsmsg.getExceptionReason());
        		   singlemesg.setExceptionTimestamp(jmsmsg.getExceptionTimestamp());
        		   
        		   // length + redelivery + reliability
        		   singlemesg.setApproximateLength(jmsmsg.getApproximateLength());
        		   singlemesg.setRedeliveredCount(jmsmsg.getRedeliveredCount());
        		   if (jmsmsg.getReliability() != null)
        			   singlemesg.setReliability(jmsmsg.getReliability().toString());
	        		   
        		   // timing
        		   singlemesg.setCurrentMEArrivalTimestamp(jmsmsg.getCurrentMEArrivalTimestamp());
        		   singlemesg.setCurrentMessageWaitTimestamp(jmsmsg.getMessageWaitTime());
        		   singlemesg.setCurrentTimestamp(jmsmsg.getTimestamp());

        		   // routing path for re-send
        		   singlemesg.setProblemDestination(jmsmsg.getExceptionProblemDestination());
        		   if (jmsmsg.getExceptionInserts() != null)
	               {
	            	   String[] detail = jmsmsg.getExceptionInserts();
	            	   // queue and busname is delivered on the last elements
	            	   int posDest=0;
	            	   int posBus=1;

	            	   if (detail.length > 2) {
	            		   posBus = detail.length;
	            		   posDest = detail.length-1;
	            	   }
	            	   for(int y = 0; y < detail.length; y++)
	            	   {
	            		   
	            		   if (posDest == y) {
	            		   		// queue point
	            		   		singlemesg.setOrigDestination(detail[y]);
	            		   }
	            		   if (posBus == y) {
	            			   	// bus
	            		   		singlemesg.setOrigDestinationBus(detail[y]);
	            		   }
		            	}
		             }
	        		   
        		   /*
					 * TBD List frp = jmsmsg.getForwardRoutingPath(); if (frp !=
					 * null && !frp.isEmpty()) {
					 * System.out.println("getForwardRoutingPath:" +
					 * frp.toString()); }
					 */

        		   // content
        		   StringBuffer data = new StringBuffer();
        		   formatMessage(data, jmsmsg, "");
        		   singlemesg.setMsgStringBuffer(data);
        	   }
        	   else 
        	   {
        		   singlemesg.setSystemMessageId(msg.getSystemMessageId());
        		   singlemesg.setBusName(busName);
	        	   logger.logp(Level.SEVERE, className, "browseSingleMessage()", "Does not support this kind of message: " + msg.getSystemMessageId());
        	   }
        	   logger.logp(Level.INFO, className, "browseSingleMessage()", singlemesg.getApiMessageId());
	           browserSession.close();
		       conn.close();
       } catch (SIException e) {
			try 
			{
				if (browserSession!= null)	browserSession.close();
				if (conn!= null) conn.close();
					
			} catch (Exception e1) {}
			logger.logp(Level.SEVERE, className, "browseQueue()", Constants.METHOD_ERROR + e.getMessage());
			e.printStackTrace();
			throw new MessagingOperationFailedException(e);
       }	
   	   logger.logp(Level.FINE, className, "browseQueue()", "End browse queue " + queueName + " return message " + singlemesg.getSystemMessageId());
   	   return singlemesg;			
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see no.nav.sibushelper.helper.MessagingHelper#browseQueue(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public List<MessageInfo> browseQueue(String busName, String meName, String queueName) throws MessagingOperationFailedException
	{
	       SICoreConnection conn=null;
	       BrowserSession browserSession=null;
	       List<MessageInfo> browsedMsg = new ArrayList<MessageInfo>();
	       
	       try {
	    	   logger.logp(Level.FINE, className, "browseQueue()", "Connect to " + busName+":"+meName);
	    	   conn = getConnectionToME(meName,busName);
	    	   logger.logp(Level.FINE, className, "browseQueue()", "Connect to " + busName+":"+meName+ " done.");
	    	   SIDestinationAddressFactory addrFact = SIDestinationAddressFactory.getInstance();
	           com.ibm.websphere.sib.SIDestinationAddress addr = addrFact.createSIDestinationAddress(queueName, false);
	           logger.logp(Level.FINE, className, "browseQueue()", "Start browse queue " + queueName);
	           browserSession = conn.createBrowserSession(addr, null, null, null);
	           int numberOfMessagesBrowsed=0;
	           do
	           {
	        	   SIBusMessage msg = browserSession.next();

	        	   if(msg == null) break;
	        	   MessageInfo msginfo = new MessageInfo();
	        	   
	        	   if (msg instanceof JsJmsMessage) {
	        		   JsJmsMessage jmsmsg = (JsJmsMessage) msg;
	        		   
	        		   // basics
	        		   msginfo.setApiMessageId(jmsmsg.getApiMessageId());
	        		   msginfo.setSystemMessageId(jmsmsg.getSystemMessageId());
	        		   msginfo.setCorrelationId(jmsmsg.getCorrelationId());
	        		   msginfo.setApiUserId(jmsmsg.getApiUserId());
	        		   msginfo.setSysUserId(jmsmsg.getSecurityUserid());
	        		   
	        		   // type
	        		   msginfo.setBusName(busName);
	        		   if (jmsmsg.getJsMessageType() != null)
	        			   msginfo.setMessageType(jmsmsg.getJsMessageType().toString());
	        		   JmsBodyType bdtype = jmsmsg.getBodyType();
	        		   if (bdtype != null)
	        			 msginfo.setMessageBodyType(bdtype.toString());
	        		   
	        		   // exception
	        		   msginfo.setExceptionMessage(jmsmsg.getExceptionMessage());
	        		   msginfo.setExceptionReason(jmsmsg.getExceptionReason());
	        		   msginfo.setExceptionTimestamp(jmsmsg.getExceptionTimestamp());
	        		   
	        		   // length + redelivery + reliability
	        		   msginfo.setApproximateLength(jmsmsg.getApproximateLength());
	        		   msginfo.setRedeliveredCount(jmsmsg.getRedeliveredCount());
	        		   if (jmsmsg.getReliability() != null)
	        			   msginfo.setReliability(jmsmsg.getReliability().toString());
	        		   
	        		   // timing
	        		   msginfo.setCurrentMEArrivalTimestamp(jmsmsg.getCurrentMEArrivalTimestamp());
	        		   msginfo.setCurrentMessageWaitTimestamp(jmsmsg.getMessageWaitTime());
	        		   msginfo.setCurrentTimestamp(jmsmsg.getTimestamp());

	        		   // routing path for re-send
	        		   msginfo.setProblemDestination(jmsmsg.getExceptionProblemDestination());
	        		   if (jmsmsg.getExceptionInserts() != null)
		               {
		            	   String[] detail = jmsmsg.getExceptionInserts();
		            	   // queue and busname is delivered on the last elements
		            	   int posDest=0;
		            	   int posBus=1;

		            	   if (detail.length > 2) {
		            		   posBus = detail.length;
		            		   posDest = detail.length-1;
		            	   }
		            	   for(int y = 0; y < detail.length; y++)
		            	   {
		            		   
		            		   if (posDest == y) {
		            		   		// queue point
		            			   msginfo.setOrigDestination(detail[y]);
		            		   }
		            		   if (posBus == y) {
		            			   	// bus
		            			   msginfo.setOrigDestinationBus(detail[y]);
		            		   }
			            	}
			             }
	        		   
	        		   /*
						 * TBD List frp = jmsmsg.getForwardRoutingPath(); if
						 * (frp != null && !frp.isEmpty()) {
						 * System.out.println("getForwardRoutingPath:" +
						 * frp.toString()); }
						 */

	        		   // content
	        		   StringBuffer data = new StringBuffer();
	        		   formatMessage(data, jmsmsg, "");
	        		   msginfo.setMsgStringBuffer(data);
	        	   }
	        	   else {
	        		   msginfo.setSystemMessageId(msg.getSystemMessageId());
	        		   msginfo.setBusName(busName);
	        		   logger.logp(Level.SEVERE, className, "browseQueue()", "Does not support this kind of message: " + msg.getSystemMessageId());
	        	   }

	        	   browsedMsg.add(numberOfMessagesBrowsed, msginfo);
	        	   numberOfMessagesBrowsed++;
		           logger.logp(Level.FINE, className, "browseQueue()", "#" + browsedMsg);
		           logger.logp(Level.FINE, className, "browseQueue()", msginfo.getApiMessageId());
	        	   
              } while(true);
	           browserSession.close();
		       conn.close();
	       } catch (SIException e) {
				try 
				{
					if (browserSession!= null)	browserSession.close();
					if (conn!= null) conn.close();
					
				} catch (Exception e1) {}
				logger.logp(Level.SEVERE, className, "browseQueue()", Constants.METHOD_ERROR + e.getMessage());
				e.printStackTrace();
				throw new MessagingOperationFailedException(e);
	       }	
	       if (browsedMsg!= null && !browsedMsg.isEmpty())
	    	   logger.logp(Level.FINE, className, "browseQueue()", "End browse queue " + queueName + " return messages #" + browsedMsg.size());
	       else
	    	   logger.logp(Level.FINE, className, "browseQueue()", "End browse queue " + queueName + " return: no message in queue");
	       return browsedMsg;
	}

	
	/**
	 * @param busName
	 * @param meName
	 * @param queueName
	 * @return
	 * @throws MessagingOperationFailedException
	 */
	public int queueMessages(String busName, String meName, String queueName) throws MessagingOperationFailedException
	{
	       SICoreConnection conn=null;
	       BrowserSession browserSession=null;
	       int numberOfMessagesBrowsed=0;
	       try {
	    	   logger.logp(Level.FINE, className, "queueMessages()", "Connect to " + busName+":"+meName);
	    	   conn = getConnectionToME(meName,busName);
	    	   logger.logp(Level.FINE, className, "queueMessages()", "Connect to " + busName+":"+meName+ " done.");
	    	   SIDestinationAddressFactory addrFact = SIDestinationAddressFactory.getInstance();
	           com.ibm.websphere.sib.SIDestinationAddress addr = addrFact.createSIDestinationAddress(queueName, false);
	           logger.logp(Level.FINE, className, "queueMessages()", "Start browse queue " + queueName);
	           browserSession = conn.createBrowserSession(addr, null, null, null);
	           do
	           {
	        	   SIBusMessage msg = browserSession.next();
	        	   if(msg == null) break;
	        	   numberOfMessagesBrowsed++;
		           logger.logp(Level.FINE, className, "queueMessages()", "#" + numberOfMessagesBrowsed);
	        	   
              } while(true);
	           browserSession.close();
		       conn.close();
	       } catch (SIException e) {
				try 
				{
					if (browserSession!= null)	browserSession.close();
					if (conn!= null) conn.close();
					
				} catch (Exception e1) {}
				logger.logp(Level.SEVERE, className, "queueMessages()", Constants.METHOD_ERROR + e.getMessage());
				e.printStackTrace();
				throw new MessagingOperationFailedException(e);
	       }	
    	   logger.logp(Level.FINE, className, "queueMessages()", "End browse queue " + queueName + " return queueMessages #" + numberOfMessagesBrowsed);
	       return numberOfMessagesBrowsed;
	}
	
	/* (non-Javadoc)
	 * @see no.nav.sibushelper.helper.MessagingHelper#clearQueue(java.lang.String, java.lang.String, java.lang.String)
	 */
	public int clearQueue(String busName, String meName, String queueName) throws MessagingOperationFailedException, DestinationNotFoundException 
	{
		int ret=0;
		try
        {
            ret = _clearQueue(busName, meName, queueName);
        }
        catch(SIException e)
        {
        	if(e instanceof SINotPossibleInCurrentConfigurationException)
        		throw new DestinationNotFoundException(e);
            else
            	throw new MessagingOperationFailedException(e);
        }
        return ret;
	}

	public int moveToQueue(String s, String s1, String s2)
			throws MessagingOperationFailedException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void putTestMessage(String s, String s1, String s2, String s3)
			throws MessagingOperationFailedException {
		// TODO Auto-generated method stub

	}

	/**
	 * formatJMSMessage
	 * 
	 * @param buf
	 * @param msg
	 * @param indent
	 */
	private void formatMessage(StringBuffer buf, JsJmsMessage jsmsg, String indent) {
	    String msgfmt = jsmsg.getFormat();
	    String newLine = '\n' + indent;
	    DataObject msgRoot;
	    try {
	        if (msgfmt.equals(SIApiConstants.JMS_FORMAT_TEXT)) {
	            buf.append("\nJMS TextMessage:" + newLine);
	            JsJmsTextMessage txt = (JsJmsTextMessage) jsmsg;	            
	            String msgText = txt.getText();
	            buf.append(msgText.replaceAll("\n", newLine));
	        } else if (msgfmt.equals(SIApiConstants.JMS_FORMAT_BYTES)) {
	            buf.append("\nJMS BytesMessage: ");
	            JsJmsBytesMessage byt = (JsJmsBytesMessage) jsmsg;
	            appendBytes(buf, byt.getBytes(), indent);
	        } else if (msgfmt.equals(SIApiConstants.JMS_FORMAT_OBJECT)) {
	            buf.append("\nJMS ObjectMessage: (");
		    	msgRoot = jsmsg.makeInboundSdoMessage().getDataGraph().getRootObject();
	            appendObjectMessage(buf, indent, msgRoot, newLine);
	        } else if (msgfmt.equals(SIApiConstants.JMS_FORMAT_STREAM)) {
	            buf.append("\nJMS StreamMessage:");
		    	msgRoot = jsmsg.makeInboundSdoMessage().getDataGraph().getRootObject();
	            appendStreamMessage(buf, indent, msgRoot, newLine);
	        } else if (msgfmt.equals(SIApiConstants.JMS_FORMAT_BYTES)) {
	        	buf.append("\nJMS MapMessage:");
	        	JsJmsMapMessage map = (JsJmsMapMessage) jsmsg;
	        	if (map.getUserFriendlyBytes() != null)
	        		buf.append(map);
	        }
	    } catch (Exception e) {
	    	logger.logp(Level.SEVERE, className, "formatMessage", "MediationHelper: " + e.getMessage());
	        buf.append("Cannot format message (" + jsmsg.getApiMessageId());
	        buf.append(") body using format " + msgfmt + " due to " + e);
	    }
	}
	
	/**
	 * @param buf
	 * @param indent
	 * @param msgRoot
	 * @param newLine
	 */
	private void appendStreamMessage(StringBuffer buf, String indent, DataObject msgRoot, String newLine) 
	{
		 List streamList = msgRoot.getList("data/value");
		 Object streamItem;
		 if (streamList != null && !streamList.isEmpty()) {
		     for (int i = 1; i <= streamList.size(); i++) {
		         streamItem = msgRoot.get("data/value[" + i + "]");
		         buf.append(newLine);
		         buf.append("Stream item " + i + " : (");
		         if (streamItem instanceof byte[]) {
		             buf.append("byte[])");
		             appendBytes(buf, (byte[]) streamItem, "    " + indent);
		         } else {
		             buf.append(streamItem.getClass().getName());
		             buf.append(") " + streamItem.toString());
		         }
		     }
		 } else {
		     buf.append("[]");
		 }
	}

	/**
	 * @param buf
	 * @param indent
	 * @param msgRoot
	 * @param newLine
	 */
	private void appendObjectMessage(StringBuffer buf, String indent, DataObject msgRoot, String newLine) {
	 Object obj = null;
	 try {
		 obj = getObject(msgRoot, "data/value");
	     buf.append(obj.getClass().getName());
	     String objString = obj.toString();
	     if (objString.indexOf('\n')>0){
	         buf.append(")\n");
	         objString = indent + objString.replaceAll("\n", newLine);
	         buf.append(objString);
	     } else {
	         buf.append(") " + objString);    
	     }
	 } catch (Exception e) {
		 logger.logp(Level.FINE, className, "appendObjectMessage", "MessageHelperError: " + e.getMessage() + " due to " + e.getCause());
	     buf.append(e.getMessage() + " due to " + e.getCause());
	 }
	}

	/**
	 * @param msgRoot
	 * @param string
	 * @return
	 * @throws MessagingOperationFailedException
	 */
	private Object getObject(DataObject msgRoot, String string) throws MessagingOperationFailedException {
	    Object result = null;
	    byte[] msgBodyBytes = msgRoot.getBytes("data/value");
	    try {
	        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(msgBodyBytes));
	        result = in.readObject();
	        in.close();
	    } catch (Throwable e) {
	        // Error de-serialising object
	    	logger.logp(Level.SEVERE, className, "getObject", "MessageHelperError: SerializeObjectError: " + e.getMessage());
	        throw new MessagingOperationFailedException(e);
	    }
	    return result;
	}

	/**
	 * @param buf
	 * @param bytes
	 * @param indent
	 */
	private void appendBytes(StringBuffer buf, byte[] bytes, String indent) {
	    int length = bytes.length;
	    String newLine = '\n' + indent;      
	    for (int lineStart = 0; lineStart<length; lineStart += 16) {
	        int lineEnd = Math.min(lineStart+16, length);
	        StringBuffer hex=new StringBuffer();
	        StringBuffer ascii=new StringBuffer();
	        for (int i=lineStart; i<lineEnd; i++) {
	          int b = bytes[i];
	          b=(b+256)%256;
	          int c1=b/16;
	          int c2=b%16;
	          hex.append((char)(c1<10 ? '0'+c1 : 'a'+c1-10));
	          hex.append((char)(c2<10 ? '0'+c2 : 'a'+c2-10));
	          if (i%2 == 1) hex.append(' ');
	          if ((b>=0x20 && b<=0x7e)) ascii.append((char)b);
	          else ascii.append('.');
	        }
	        int pad=16-(lineEnd-lineStart);
	        int spaces=(pad*5 + pad%2)/2;
	        spaces+=3;
	        for (int i=0; i<spaces; i++) hex.append(' ');       
	        String offset="0000"+Integer.toHexString(lineStart);
	        offset=offset.substring(offset.length()-4);
	        buf.append(newLine);
	        buf.append(offset);
	        buf.append("  ");
	        buf.append(hex.toString());
	        buf.append(ascii.toString());
	      }
	}

    /**
     * @param busName
     * @param meName
     * @param queueName
     * @return
     * @throws MessagingOperationFailedException
     */
    private int _clearQueue(String busName, String meName, String queueName) throws SIException
    {
    	SICoreConnection conn=null;
    	ConsumerSession consumerSession=null;
    	int numberOfMessagesCleared=0;
    	logger.logp(Level.FINE, className, "_clearQueue", "{ busName=" + busName + ", meName=" + meName + ", queueName=" + queueName + "}");
    	try {
			conn = getConnectionToME(meName, busName);
			numberOfMessagesCleared = 0;
			SIDestinationAddressFactory addrFact = SIDestinationAddressFactory.getInstance();
			SIDestinationAddress addr = addrFact.createSIDestinationAddress(queueName, false);
			consumerSession = conn.createConsumerSession(addr, null, null, null, true, false, Reliability.ASSURED_PERSISTENT, false, null);
			consumerSession.start(false);
			do
			{
				SIBusMessage msg = consumerSession.receiveWithWait(null, 1000L);
				if(msg == null)
					break;
				numberOfMessagesCleared++;
			} while(true);
			consumerSession.close();
			conn.close();
		} catch (SIException e) {
			try 
			{
				if (consumerSession!= null)	consumerSession.close();
				if (conn!= null) conn.close();
					
			} catch (Exception e1){}
			logger.logp(Level.SEVERE, className, "_clearQueue()", Constants.METHOD_ERROR + e.getMessage());
			e.printStackTrace();
			throw new SIException(e){
				private static final long serialVersionUID = -1602574733872920210L;};
			}
    	logger.logp(Level.FINE, className, "_clearQueue()", new Integer(numberOfMessagesCleared).toString());
    	return numberOfMessagesCleared;
    }
	
}
