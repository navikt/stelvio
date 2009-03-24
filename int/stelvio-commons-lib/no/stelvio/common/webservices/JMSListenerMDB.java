package no.stelvio.common.webservices;

/* Author: IBM (decompiled from com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB)
 * Modified by: person73874c7d71f8
 * Temporary fix to support transactional one-way soap/jms-message handling.
 * 
 */
import com.ibm.ejs.ras.Tr;
import com.ibm.ejs.ras.TraceComponent;
import com.ibm.mq.jms.MQDestination;
import com.ibm.ws.ffdc.FFDCFilter;
import com.ibm.ws.webservices.engine.EngineConfiguration;
import com.ibm.ws.webservices.engine.MessageContext;
import com.ibm.ws.webservices.engine.SimpleTargetedChain;
import com.ibm.ws.webservices.engine.WebServicesFault;
import com.ibm.ws.webservices.engine.configuration.EngineConfigurationFactoryFinder;
import com.ibm.ws.webservices.engine.resources.Messages;
import com.ibm.ws.webservices.engine.server.ServerEngine;
import com.ibm.ws.webservices.engine.transport.TransactionSuspender;
import com.ibm.ws.webservices.engine.transport.http.HTTPConstants;
import com.ibm.ws.webservices.engine.transport.http.HTTPSender;
import com.ibm.ws.webservices.engine.transport.jms.JMSSender;
import com.ibm.ws.webservices.engine.utils.JavaUtils;
import com.ibm.ws.webservices.engine.utils.QNameTable;
import com.ibm.ws.webservices.trace.MessageTrace;
import com.ibm.ws.webservices.trace.UserExceptionTrace;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenContext;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.soap.SOAPException;

public class JMSListenerMDB extends com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB
{

    public JMSListenerMDB()
    {
        fMessageDrivenCtx = null;
        serverEngine = null;
        initialContext = null;
        replyQCF = null;
    }

    public MessageDrivenContext getMessageDrivenContext()
    {
        return fMessageDrivenCtx;
    }

    public void setMessageDrivenContext(MessageDrivenContext messagedrivencontext)
    {
        fMessageDrivenCtx = messagedrivencontext;
    }

    private InitialContext getInitialContext()
        throws NamingException
    {
        if(_tc.isEntryEnabled())
            Tr.entry(_tc, "getInitialContext");
        try
        {
            if(initialContext == null)
                initialContext = new InitialContext();
        }
        catch(NamingException namingexception)
        {
            FFDCFilter.processException(namingexception, "com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB.getInitialContext", "154", this);
            if(_tc.isDebugEnabled())
                Tr.debug(_tc, "Failed to create initial context!");
            throw namingexception;
        }
      finally{
        if(_tc.isEntryEnabled())
            Tr.exit(_tc, "getInitialContext");
    	}
        return initialContext;
    }

    private synchronized QueueConnectionFactory getReplyQCF()
        throws Exception
    {
        try
        {
            if(_tc.isEntryEnabled())
                Tr.entry(_tc, "getReplyQCF");
            if(replyQCF == null)
            {
                InitialContext initialcontext = getInitialContext();
                String s = "java:comp/env/jms/WebServicesReplyQCF";
                if(_tc.isDebugEnabled())
                    Tr.debug(_tc, (new StringBuilder("About to look up QCF: ")).append(s).toString());
                replyQCF = (QueueConnectionFactory)initialcontext.lookup(s);
                if(_tc.isDebugEnabled())
                    Tr.debug(_tc, "Found the QCF!");
            }
        }
        catch(Exception exception)
        {
            FFDCFilter.processException(exception, "com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB.getReplyQCF", "205", this);
            if(_tc.isDebugEnabled())
                Tr.debug(_tc, (new StringBuilder("Failed to locate my reply QCF, exception = ")).append(exception.toString()).toString());
            throw exception;
        }finally{
      
        if(_tc.isEntryEnabled())
            Tr.exit(_tc, "getReplyQCF");
        }
        return replyQCF;
    }

    public void ejbCreate()
    {
        if(_tc.isEntryEnabled())
            Tr.entry(_tc, "ejbCreate");
        try
        {
            HashMap hashmap = new HashMap();
            serverEngine = ServerEngine.getServer(null, hashmap);
            if(serverEngine == null)
                throw new WebServicesFault(Messages.getMessage("JMS.ServerEngine"));
        }
        catch(WebServicesFault webservicesfault)
        {
            FFDCFilter.processException(webservicesfault, "com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB.ejbCreate", "248", this);
            Tr.error(_tc, "JMS.Fault01", webservicesfault.toString());
            throw new EJBException(webservicesfault.toString());
        }
       finally{
        if(_tc.isEntryEnabled())
            Tr.exit(_tc, "ejbCreate");
       }
        return;
    }

    public void onMessage(Message message)
    {
        if(_tc.isEntryEnabled())
            Tr.entry(_tc, "onMessage");
        if(_tc.isDebugEnabled())
            Tr.debug(_tc, (new StringBuilder("Received JMS Message: ")).append(message.toString()).toString());
        MessageContext messagecontext;
        Queue replyQueue;
        String jmsMessageID;
        boolean isByteMessage;
        boolean runInSeparateTransaction;
        TransactionSuspender transactionsuspender;
        messagecontext = new MessageContext(serverEngine);
        Object obj = null;
        Object obj1 = null;
        replyQueue = null;
        jmsMessageID = null;
        isByteMessage = false;
        runInSeparateTransaction = true;
        transactionsuspender = null;
        try
        {
            String enableTransString = message.getStringProperty("enableTransaction");
            runInSeparateTransaction = JavaUtils.isFalse(enableTransString);
        }
        catch(Throwable throwable) { }
        if(runInSeparateTransaction)
            transactionsuspender = new TransactionSuspender();
        if(runInSeparateTransaction)
            transactionsuspender.suspendTransaction();
        try {
        
        	try
	        {
	            jmsMessageID = message.getJMSMessageID();
	            replyQueue = (Queue)message.getJMSReplyTo();
	            String targetServiceName = message.getStringProperty("targetService");
	            if(targetServiceName == null || targetServiceName.length() == 0)
	            {
	                Tr.error(_tc, "JMS.NoTargetService", "targetService");
	                throw new WebServicesFault("JMS", Messages.getMessage("JMS.NoTargetService", "targetService"), null, null);
	            }
	            messagecontext.setTargetPort(targetServiceName);
	            if(_tc.isDebugEnabled())
	                Tr.debug(_tc, (new StringBuilder("Set target port on message context to: ")).append(targetServiceName).toString());
	            String endpointURLName = message.getStringProperty("endpointURL");
	            if(endpointURLName != null && endpointURLName.length() > 0)
	            {
	                messagecontext.setProperty("inbound.url", endpointURLName);
	                if(_tc.isDebugEnabled())
	                    Tr.debug(_tc, (new StringBuilder("Set inbound.url property on MessageContext to: ")).append(endpointURLName).toString());
	            }
	            String contentTypeString = message.getStringProperty("contentType");
	            contentTypeString = contentTypeString == null ? null : contentTypeString.trim();
	            byte abyte0[] = (byte[])null;
	            com.ibm.ws.webservices.engine.Message message1;
	            if(message instanceof BytesMessage)
	            {
	                if(_tc.isDebugEnabled())
	                    Tr.debug(_tc, (new StringBuilder("Got BytesMessage, contentType=")).append(contentTypeString).toString());
	                BytesMessage bytesmessage = (BytesMessage)message;
	                isByteMessage = true;
	                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
	                byte abyte1[] = new byte[4096];
	                int i = 1;
	                do
	                {
	                    if(i <= 0)
	                        break;
	                    i = bytesmessage.readBytes(abyte1);
	                    if(i > 0)
	                        bytearrayoutputstream.write(abyte1, 0, i);
	                } while(true);
	                if(_tc.isDebugEnabled())
	                    Tr.debug(_tc, (new StringBuilder("Extracted the following SOAP message from the JMS request message:\n")).append(bytearrayoutputstream.toString()).toString());
	                abyte0 = bytearrayoutputstream.toByteArray();
	                ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
	                message1 = new com.ibm.ws.webservices.engine.Message(bytearrayinputstream, contentTypeString, null);
	            } else
	            if(message instanceof TextMessage)
	            {
	                if(_tc.isDebugEnabled())
	                    Tr.debug(_tc, (new StringBuilder("Got TextMessage, contentType=")).append(contentTypeString).toString());
	                String messageTextString = ((TextMessage)message).getText();
	                if(_tc.isDebugEnabled())
	                    Tr.debug(_tc, (new StringBuilder("Extracted the following SOAP text message from the JMS request message:\n")).append(messageTextString).toString());
	                message1 = new com.ibm.ws.webservices.engine.Message(messageTextString, contentTypeString, null);
	                if(MessageTrace.isTraceEnabled())
	                    abyte0 = messageTextString.getBytes();
	            } else
	            {
	                Tr.error(_tc, "JMS.BadMsgType");
	                throw new WebServicesFault("JMS", Messages.getMessage("JMS.BadMsgType"), null, null);
	            }
	            messagecontext.setRequestMessage(message1);
	            messagecontext.setTransportName("jms");
	            messagecontext.setProperty("transport.jms.requestMessage", message);
	            handleSoapAction(message, messagecontext);
	            if(MessageTrace.isTraceEnabled() && abyte0 != null)
	                MessageTrace.log(MessageTrace.INBOUND_JMS_REQUEST, contentTypeString, abyte0);
	            if(_tc.isDebugEnabled())
	                Tr.debug(_tc, "Calling ServerEngine.invoke()...");
	            serverEngine.invoke(messagecontext);
	            if(_tc.isDebugEnabled())
	                Tr.debug(_tc, "Returned from ServerEngine.invoke()...");
	        }
	        catch(Exception exception)
	        {
	            boolean isUserException = false;
	            if(UserExceptionTrace.isUserDefinedException(exception))
	            {
	                UserExceptionTrace.log(exception);
	                isUserException = true;
	            }
	            WebServicesFault webservicesfault = WebServicesFault.makeFault(exception);
	            if(!isUserException)
	            {
	                FFDCFilter.processException(webservicesfault, "com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB.onMessage", "485", this);
	                Tr.error(_tc, "JMS.JMSError03", webservicesfault.toString());
	            }
	            if(replyQueue != null || messagecontext.getProperty("com.ibm.ws.wsaddressing.RedirectRequired") != null)
	            {
	                com.ibm.ws.webservices.engine.Message message2 = new com.ibm.ws.webservices.engine.Message(webservicesfault);
	                messagecontext.setResponseMessage(message2);
	            }
	            if(replyQueue == null && !isUserException)
	                throw new RuntimeException(webservicesfault.getMessage());            
	        }
	        finally{
	        if(runInSeparateTransaction)
	            transactionsuspender.resumeTransaction();
	        }
	      
	        if(messagecontext.getProperty("com.ibm.ws.wsaddressing.RedirectRequired") != null) {
	        String targetEnpointAddress = messagecontext.getTargetEndpointAddress();
	        boolean isJMSTarget = false;
	        boolean isHttpTarget = false;
	        boolean debugEnabled = _tc.isDebugEnabled();
	        if(targetEnpointAddress != null && targetEnpointAddress.toLowerCase().startsWith("jms"))
	        {
	            isJMSTarget = true;
	            if(debugEnabled)
	                Tr.debug(_tc, "Redirect directive to JMS found.");
	        }
	        if(targetEnpointAddress != null && targetEnpointAddress.toLowerCase().startsWith("http"))
	        {
	            isHttpTarget = true;
	            if(debugEnabled)
	                Tr.debug(_tc, "Redirect directive to http/https found.");
	        }
	        if(messagecontext.isOneWay())
	        {
	            Tr.error(_tc, Messages.getMessage("servlet.unexpected.oneway.response00"));
	            if(debugEnabled)
	                Tr.debug(_tc, "No Response Sent");
	        } else
	        if(messagecontext.getProperty("com.ibm.ws.wsaddressing.NoneURI") != null)
	        {
	            if(debugEnabled)
	                Tr.debug(_tc, "Discard Response : WSAddressing NoneURI specified.");
	            if(debugEnabled)
	                Tr.debug(_tc, "No Response Sent");
	        } else
	        if(isJMSTarget)
	        {
	            EngineConfiguration engineconfiguration = EngineConfigurationFactoryFinder.newFactory("NONE").getClientEngineConfig();
	            if(engineconfiguration != null)
	            {
	                SimpleTargetedChain simpletargetedchain = (SimpleTargetedChain)engineconfiguration.getTransport(QNameTable.createQName(null, "jms"));
	                if(simpletargetedchain != null)
	                {
	                    JMSSender jmssender = (JMSSender)simpletargetedchain.getPivotHandler();
	                    MessageContext.setCurrentThreadsContext(messagecontext);
	                    if(jmssender != null)
	                    {
	                        java.net.URL url = messagecontext.getTargetEndpointAddressAsURL();
	                        if(debugEnabled)
	                            Tr.debug(_tc, (new StringBuilder("Attempting to redirect to JMS to ")).append(url).toString());
	                        jmssender.invoke(messagecontext);
	                    }
	                }
	            }
	        } else
	        if(isHttpTarget)
	            try
	            {
	                EngineConfiguration engineconfiguration1 = EngineConfigurationFactoryFinder.newFactory("NONE").getClientEngineConfig();
	                if(engineconfiguration1 != null)
	                {
	                    SimpleTargetedChain simpletargetedchain1 = (SimpleTargetedChain)engineconfiguration1.getTransport(QNameTable.createQName(null, "http"));
	                    HTTPSender httpsender = (HTTPSender)simpletargetedchain1.getPivotHandler();
	                    MessageContext.setCurrentThreadsContext(messagecontext);
	                    messagecontext.setProperty(HTTPConstants.MC_HTTP_OUTBOUND_ASYNC_RESPONSE, "yes".intern());
	                    httpsender.invoke(messagecontext);
	                }
	            }
	            catch(Exception exception2)
	            {
	                FFDCFilter.processException(exception2, "com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB.onMessage", "1:584:1.38", this);
	                throw new SOAPException(exception2);
	            }
	
	        if(_tc.isEntryEnabled())
	            Tr.exit(_tc, "onMessage");
	        return;
		}
	        com.ibm.ws.webservices.engine.Message message3;
	        if(replyQueue == null)
	        {
	            if(messagecontext.getResponseMessage()!=null && messagecontext.getResponseMessage().getSOAPBody().hasFault())
	            {
	                System.out.println((new StringBuilder("One-way call returned with soapfault. Will be sent redirected to error destination (usually System.Exception-queue on system bus) :")).append(messagecontext.getResponseMessage().getSOAPPartAsString()).toString());
	                throw new RuntimeException((new StringBuilder("Fault message returned one one-way call:")).append(messagecontext.getResponseMessage().getSOAPPartAsString()).toString());
	            }            
	        }else{
		        message3 = messagecontext.getResponseMessage();
		        if(message3 != null) {
			        QueueConnection queueconnection;
			        QueueConnectionFactory queueconnectionfactory = getReplyQCF();
			        queueconnection = queueconnectionfactory.createQueueConnection();
			        if(_tc.isDebugEnabled())
			            Tr.debug(_tc, "Created the queue connection.");
			        try
			        {
			            QueueSession queuesession = queueconnection.createQueueSession(false, 1);
			            if(_tc.isDebugEnabled())
			                Tr.debug(_tc, "Created the queue session.");
			            if(message.getJMSDestination() == null)
			                try
			                {
			                    if(replyQueue instanceof MQDestination)
			                        ((MQDestination)replyQueue).setTargetClient(1);
			                }
			                catch(NoClassDefFoundError noclassdeffounderror) { }
			            QueueSender queuesender = queuesession.createSender(replyQueue);
			            if(_tc.isDebugEnabled())
			                Tr.debug(_tc, "Created the queue sender.");
			            Object returnByteMessage = null;
			            String s7 = message3.getContentType(messagecontext.getSOAPConstants());
			            if(isByteMessage || message3.countAttachments() > 0)
			            {
			                returnByteMessage = queuesession.createBytesMessage();
			                ByteArrayOutputStream bytearrayoutputstream1 = new ByteArrayOutputStream();
			                message3.writeTo(bytearrayoutputstream1, s7);
			                message3.removeAllAttachments();
			                if(_tc.isDebugEnabled())
			                    Tr.debug(_tc, (new StringBuilder("About to write the following reply message into the JMS response message:\n")).append(bytearrayoutputstream1.toString()).toString());
			                ((BytesMessage)returnByteMessage).writeBytes(bytearrayoutputstream1.toByteArray());
			                if(MessageTrace.isTraceEnabled())
			                    MessageTrace.log(MessageTrace.OUTBOUND_JMS_RESPONSE, s7, bytearrayoutputstream1.toByteArray());
			            } else
			            {
			                returnByteMessage = queuesession.createTextMessage();
			                String s8 = message3.getSOAPPartAsString();
			                if(_tc.isDebugEnabled())
			                    Tr.debug(_tc, (new StringBuilder("About to write the following reply message into the JMS response message:\n")).append(s8).toString());
			                ((TextMessage)returnByteMessage).setText(s8);
			                if(MessageTrace.isTraceEnabled())
			                    MessageTrace.log(MessageTrace.OUTBOUND_JMS_RESPONSE, s7, s8);
			            }
			            ((Message)returnByteMessage).setStringProperty("contentType", s7);
			            ((Message)returnByteMessage).setStringProperty("transportVersion", "1");
			            HashMap hashmap = null;
			            hashmap = (HashMap)messagecontext.getProperty("com.ibm.websphere.webservices.responseTransportProperties");
			            if(hashmap != null)
			                setResponseHandlerTransportProperties((Message)returnByteMessage, hashmap);
			            ((Message)returnByteMessage).setJMSCorrelationID(jmsMessageID);
			            if(_tc.isDebugEnabled())
			                Tr.debug(_tc, (new StringBuilder("About to send the following reply message:\n")).append(returnByteMessage.toString()).toString());
			            long l = message.getJMSExpiration() - message.getJMSTimestamp();
			            if(l < 0L)
			                l = 0L;
			            queuesender.send((Message)returnByteMessage, message.getJMSDeliveryMode(), message.getJMSPriority(), l);
			            if(_tc.isDebugEnabled())
			                Tr.debug(_tc, (new StringBuilder("Sent the reply message with a time-to-live of ")).append(l).toString());
			            if(_tc.isDebugEnabled())
			                Tr.debug(_tc, "Sent the reply message.");
			        }
			        catch(Exception exception1)
			        {
			            FFDCFilter.processException(exception1, "com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB.onMessage", "756", this);
			            WebServicesFault webservicesfault2 = WebServicesFault.makeFault(exception1);
			            Tr.error(_tc, "JMS.unexpected", webservicesfault2.toString());
			            throw webservicesfault2;
			        }
			        finally{
				        queueconnection.close();
				        if(_tc.isDebugEnabled()){
				            Tr.debug(_tc, "Closed the queue connection.");
				        }
			        }
		        }
	        }
	    }catch(Exception outer){
       
	        FFDCFilter.processException(outer, "com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB.onMessage", "756", this);
	        WebServicesFault webservicesfault2 = WebServicesFault.makeFault(outer);
	        Tr.error(_tc, "JMS.unexpected", webservicesfault2.toString());
	        throw new RuntimeException(webservicesfault2);
		    }
	    finally{	   
	        if(_tc.isEntryEnabled())
	            Tr.exit(_tc, "onMessage");
	    }
        return;
    }

    private void handleSoapAction(Message message, MessageContext messagecontext)
        throws JMSException
    {
        String s = message.getStringProperty("soapAction");
        if(_tc.isDebugEnabled())
            Tr.debug(_tc, (new StringBuilder("Retrieve soapAction property from JMS message: ")).append(s == null ? "<null>" : s).toString());
        if(s == null || s.length() == 0)
        {
            messagecontext.setUseSOAPAction(false);
            messagecontext.setSOAPActionURI(null);
            if(_tc.isDebugEnabled())
                Tr.debug(_tc, "Disabling soapAction on MessageContext...");
        } else
        {
            messagecontext.setUseSOAPAction(true);
            if(s.startsWith("\"") && s.endsWith("\""))
                s = s.substring(1, s.length() - 1);
            messagecontext.setSOAPActionURI(s);
            if(_tc.isDebugEnabled())
                Tr.debug(_tc, (new StringBuilder("Setting soapAction on MessageContext to: ")).append(s).toString());
        }
    }

    public void ejbRemove()
    {
    }

    private void setResponseHandlerTransportProperties(Message message, HashMap hashmap)
        throws WebServicesFault
    {
        try
        {
            if(hashmap != null)
            {
                Iterator iterator = hashmap.entrySet().iterator();
                do
                {
                    if(!iterator.hasNext())
                        break;
                    java.util.Map.Entry entry = (java.util.Map.Entry)(java.util.Map.Entry)iterator.next();
                    if(entry.getKey() == null || !(entry.getKey() instanceof String))
                    {
                        Tr.warning(_tc, "invalid.transportheader.key");
                    } else
                    {
                        String s = ((String)entry.getKey()).trim();
                        if(!JavaUtils.hasValue(s))
                            Tr.warning(_tc, "invalid.transportheader.key");
                        else
                        if(entry.getValue() == null || !(entry.getValue() instanceof String))
                        {
                            Tr.warning(_tc, "invalid.transportheader.value", ((Object) (new Object[] {
                                entry.getValue()
                            })));
                        } else
                        {
                            String s1 = ((String)entry.getValue()).trim();
                            if(!JavaUtils.hasValue(s1))
                                Tr.warning(_tc, "invalid.transportheader.value", ((Object) (new Object[] {
                                    s1
                                })));
                            else
                            if(!message.propertyExists(s))
                                message.setStringProperty(s, s1);
                        }
                    }
                } while(true);
            }
        }
        catch(JMSException jmsexception)
        {
            FFDCFilter.processException(jmsexception, "com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB.setResponseHandlerTransportProperties", "886", this);
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append(Messages.getMessage("JMS.JMSError01", jmsexception.toString()));
            for(Exception exception = jmsexception.getLinkedException(); exception != null;)
            {
                stringbuffer.append("\n");
                stringbuffer.append(Messages.getMessage("JMS.JMSError02", exception.toString()));
                if(exception instanceof JMSException)
                    exception = ((JMSException)exception).getLinkedException();
                else
                    exception = null;
            }

            Tr.error(_tc, stringbuffer.toString());
            if(_tc.isDebugEnabled())
                Tr.debug(_tc, stringbuffer.toString());
            throw new WebServicesFault("JMS", stringbuffer.toString(), null, null);
        }
    }

    private static final TraceComponent _tc = Tr.register(com.ibm.ws.webservices.engine.transport.jms.JMSListenerMDB.class, "WebServices", "com.ibm.ws.webservices.engine.resources.engineMessages");
    private MessageDrivenContext fMessageDrivenCtx;
    private ServerEngine serverEngine;
    private InitialContext initialContext;
    private QueueConnectionFactory replyQCF;

}