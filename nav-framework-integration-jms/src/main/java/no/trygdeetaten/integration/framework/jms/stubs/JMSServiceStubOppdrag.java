package no.trygdeetaten.integration.framework.jms.stubs;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;
import no.trygdeetaten.integration.framework.jms.formatter.DynamicXMLMessageFormatter;
import no.trygdeetaten.integration.framework.service.IntegrationService;


/**
 * Service stub for JMS kall mot Oppdrag.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: JMSServiceStubOppdrag.java 2805 2006-03-01 11:53:30Z skb2930 $
 */
public class JMSServiceStubOppdrag extends IntegrationService {
	private DynamicXMLMessageFormatter formatter = null;
	private SessionStub session = null;
	/** Oppdrag log */
	private Log oppdragLog = LogFactory.getLog("OPPDRAG_LOG");

	/**
	 * Init metode for stub
	 */
	public final void init() {
		formatter = new DynamicXMLMessageFormatter();
		formatter.setKeyField("mal");
		formatter.setKeyBean("bean");

		Map xmlFileMappings = new HashMap();
		xmlFileMappings.put("OpprettOppdrag", "jms/oppdrag/mapping-oppdrag.xml");
		xmlFileMappings.put("Avstemming", "jms/oppdrag/mapping-oppdrag-avstemming.xml");

		formatter.setXmlFileMappings(xmlFileMappings);
		session = new SessionStub();
		formatter.init();
	}

	/**
	 * {@inheritDoc}
	 */
	protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
		Message msg = formatter.formatMessage(session, request);

		try {
			TextMessage textMessage = (TextMessage) msg;

			if (oppdragLog.isInfoEnabled()) {
				oppdragLog.info(textMessage.getText());
			}
		} catch (JMSException e) {
			throw new SystemException(FrameworkError.UNSPECIFIED_ERROR, new String[] {"Stub oppdrag", "JMS"});
		}
		
		return new ServiceResponse();
	}

	/**
	 * Private class for message stubbing
	 *
	 * @author persone5d69f3729a8, Accenture
	 * @version $Id: JMSServiceStubOppdrag.java 2805 2006-03-01 11:53:30Z skb2930 $
	 */
	private static class MessageStub implements TextMessage {
		private boolean failOnReplyTo = false;
		private String text = null;
		private String corrId = null;
		private Map header = new HashMap();
		
		/* (non-Javadoc)
		 * @see javax.jms.TextMessage#setText(java.lang.String)
		 */
		public void setText(String arg0) throws JMSException {
			text = arg0;		
		}

		/* (non-Javadoc)
		 * @see javax.jms.TextMessage#getText()
		 */
		public String getText() throws JMSException {
			return text;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSMessageID()
		 */
		public String getJMSMessageID() throws JMSException {
		
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSMessageID(java.lang.String)
		 */
		public void setJMSMessageID(String arg0) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSTimestamp()
		 */
		public long getJMSTimestamp() throws JMSException {
		
			return 0;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSTimestamp(long)
		 */
		public void setJMSTimestamp(long arg0) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSCorrelationIDAsBytes()
		 */
		public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
		
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSCorrelationIDAsBytes(byte[])
		 */
		public void setJMSCorrelationIDAsBytes(byte[] arg0) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSCorrelationID(java.lang.String)
		 */
		public void setJMSCorrelationID(String arg0) throws JMSException {
			corrId = arg0;

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSCorrelationID()
		 */
		public String getJMSCorrelationID() throws JMSException {
		
			return corrId;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSReplyTo()
		 */
		public Destination getJMSReplyTo() throws JMSException {
		
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSReplyTo(javax.jms.Destination)
		 */
		public void setJMSReplyTo(Destination arg0) throws JMSException {
			if( failOnReplyTo ) {
				throw new JMSException("Test");
			}

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSDestination()
		 */
		public Destination getJMSDestination() throws JMSException {
		
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSDestination(javax.jms.Destination)
		 */
		public void setJMSDestination(Destination arg0) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSDeliveryMode()
		 */
		public int getJMSDeliveryMode() throws JMSException {
		
			return 0;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSDeliveryMode(int)
		 */
		public void setJMSDeliveryMode(int arg0) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSRedelivered()
		 */
		public boolean getJMSRedelivered() throws JMSException {
		
			return false;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSRedelivered(boolean)
		 */
		public void setJMSRedelivered(boolean arg0) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSType()
		 */
		public String getJMSType() throws JMSException {
		
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSType(java.lang.String)
		 */
		public void setJMSType(String arg0) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSExpiration()
		 */
		public long getJMSExpiration() throws JMSException {
		
			return 0;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSExpiration(long)
		 */
		public void setJMSExpiration(long arg0) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getJMSPriority()
		 */
		public int getJMSPriority() throws JMSException {
		
			return 0;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setJMSPriority(int)
		 */
		public void setJMSPriority(int arg0) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#clearProperties()
		 */
		public void clearProperties() throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#propertyExists(java.lang.String)
		 */
		public boolean propertyExists(String arg0) throws JMSException {
		
			return false;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getBooleanProperty(java.lang.String)
		 */
		public boolean getBooleanProperty(String arg0) throws JMSException {
		
			return false;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getByteProperty(java.lang.String)
		 */
		public byte getByteProperty(String arg0) throws JMSException {
		
			return 0;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getShortProperty(java.lang.String)
		 */
		public short getShortProperty(String arg0) throws JMSException {
		
			return 0;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getIntProperty(java.lang.String)
		 */
		public int getIntProperty(String arg0) throws JMSException {
		
			return 0;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getLongProperty(java.lang.String)
		 */
		public long getLongProperty(String arg0) throws JMSException {
		
			return 0;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getFloatProperty(java.lang.String)
		 */
		public float getFloatProperty(String arg0) throws JMSException {
		
			return 0;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getDoubleProperty(java.lang.String)
		 */
		public double getDoubleProperty(String arg0) throws JMSException {
		
			return 0;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getStringProperty(java.lang.String)
		 */
		public String getStringProperty(String arg0) throws JMSException {
		
			return (String)header.get(arg0);
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getObjectProperty(java.lang.String)
		 */
		public Object getObjectProperty(String arg0) throws JMSException {
		
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#getPropertyNames()
		 */
		public Enumeration getPropertyNames() throws JMSException {
		
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setBooleanProperty(java.lang.String, boolean)
		 */
		public void setBooleanProperty(String arg0, boolean arg1) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setByteProperty(java.lang.String, byte)
		 */
		public void setByteProperty(String arg0, byte arg1) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setShortProperty(java.lang.String, short)
		 */
		public void setShortProperty(String arg0, short arg1) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setIntProperty(java.lang.String, int)
		 */
		public void setIntProperty(String arg0, int arg1) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setLongProperty(java.lang.String, long)
		 */
		public void setLongProperty(String arg0, long arg1) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setFloatProperty(java.lang.String, float)
		 */
		public void setFloatProperty(String arg0, float arg1) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setDoubleProperty(java.lang.String, double)
		 */
		public void setDoubleProperty(String arg0, double arg1) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setStringProperty(java.lang.String, java.lang.String)
		 */
		public void setStringProperty(String arg0, String arg1) throws JMSException {
			header.put( arg0, arg1);
		
		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#setObjectProperty(java.lang.String, java.lang.Object)
		 */
		public void setObjectProperty(String arg0, Object arg1) throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#acknowledge()
		 */
		public void acknowledge() throws JMSException {
		

		}

		/* (non-Javadoc)
		 * @see javax.jms.Message#clearBody()
		 */
		public void clearBody() throws JMSException {
		

		}

		/**
		 * @param b
		 */
		public void setFailOnReplyTo(boolean b) {
			failOnReplyTo = b;
		}

	}

	private static class SessionStub implements QueueSession {

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.QueueSession#createQueue(java.lang.String)
		 */
		public Queue createQueue(String arg0) throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.QueueSession#createReceiver(javax.jms.Queue)
		 */
		public QueueReceiver createReceiver(Queue arg0) throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.QueueSession#createReceiver(javax.jms.Queue, java.lang.String)
		 */
		public QueueReceiver createReceiver(Queue arg0, String arg1) throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.QueueSession#createSender(javax.jms.Queue)
		 */
		public QueueSender createSender(Queue arg0) throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.QueueSession#createBrowser(javax.jms.Queue)
		 */
		public QueueBrowser createBrowser(Queue arg0) throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.QueueSession#createBrowser(javax.jms.Queue, java.lang.String)
		 */
		public QueueBrowser createBrowser(Queue arg0, String arg1) throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.QueueSession#createTemporaryQueue()
		 */
		public TemporaryQueue createTemporaryQueue() throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#createBytesMessage()
		 */
		public BytesMessage createBytesMessage() throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#createMapMessage()
		 */
		public MapMessage createMapMessage() throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#createMessage()
		 */
		public Message createMessage() throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#createObjectMessage()
		 */
		public ObjectMessage createObjectMessage() throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#createObjectMessage(java.io.Serializable)
		 */
		public ObjectMessage createObjectMessage(Serializable arg0) throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#createStreamMessage()
		 */
		public StreamMessage createStreamMessage() throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#createTextMessage()
		 */
		public TextMessage createTextMessage() throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#createTextMessage(java.lang.String)
		 */
		public TextMessage createTextMessage(String arg0) throws JMSException {
			MessageStub msg = new MessageStub();
			msg.setText(arg0);
			return msg;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#getTransacted()
		 */
		public boolean getTransacted() throws JMSException {
			return false;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#commit()
		 */
		public void commit() throws JMSException {
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#rollback()
		 */
		public void rollback() throws JMSException {
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#close()
		 */
		public void close() throws JMSException {			
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#recover()
		 */
		public void recover() throws JMSException {
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#getMessageListener()
		 */
		public MessageListener getMessageListener() throws JMSException {
			return null;
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#setMessageListener(javax.jms.MessageListener)
		 */
		public void setMessageListener(MessageListener arg0) throws JMSException {
		}

		/** 
		 * {@inheritDoc}
		 * @see javax.jms.Session#run()
		 */
		public void run() {
		}
	}
}
