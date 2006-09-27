package no.stelvio.integration.jms;

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyQueueSession implements QueueSession {
	
	private boolean failOnSenderCreate = false;
	private boolean failOnCreateTemp = false;
	private boolean failOnCreateOfReceiverWithMessageSelector = false;
	private boolean failOnCreateOfReceiverWithoutMessageSelector = false;
	private boolean failOnMessageCreate = false;
	
	private DummySender sender = new DummySender();
	private DummyReceiver receiver = new DummyReceiver();

	/* (non-Javadoc)
	     * @see javax.jms.QueueSession#createQueue(java.lang.String)
	     */
	public Queue createQueue(String arg0) throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.QueueSession#createReceiver(javax.jms.Queue)
	 */
	public QueueReceiver createReceiver(Queue arg0) throws JMSException {
		return createReceiverCommon(failOnCreateOfReceiverWithoutMessageSelector);
	}

	/* (non-Javadoc)
	 * @see javax.jms.QueueSession#createReceiver(javax.jms.Queue, java.lang.String)
	 */
	public QueueReceiver createReceiver(Queue arg0, String arg1) throws JMSException {
		return createReceiverCommon(failOnCreateOfReceiverWithMessageSelector);
	}

	private QueueReceiver createReceiverCommon(final boolean fail) throws JMSException {
		if (fail) {
			throw new JMSException("Test");
		}

		receiver.setExpectedReceiveCalls(sender.getMessagesSent());
		return receiver;
	}

	/* (non-Javadoc)
	     * @see javax.jms.QueueSession#createSender(javax.jms.Queue)
	     */
	public QueueSender createSender(Queue arg0) throws JMSException {
		if( failOnSenderCreate ) {
			throw new JMSException("test");
		}

		return sender;
	}

	/* (non-Javadoc)
	 * @see javax.jms.QueueSession#createBrowser(javax.jms.Queue)
	 */
	public QueueBrowser createBrowser(Queue arg0) throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.QueueSession#createBrowser(javax.jms.Queue, java.lang.String)
	 */
	public QueueBrowser createBrowser(Queue arg0, String arg1) throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.QueueSession#createTemporaryQueue()
	 */
	public TemporaryQueue createTemporaryQueue() throws JMSException {
		if( failOnCreateTemp ) {
			throw new JMSException("Test");
		}

		return new TemporaryQueue() {
			public void delete() {
				// dummy implementation
			}

			public String getQueueName() {
				return "queueName";
			}
		};
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#createBytesMessage()
	 */
	public BytesMessage createBytesMessage() throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#createMapMessage()
	 */
	public MapMessage createMapMessage() throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#createMessage()
	 */
	public Message createMessage() throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#createObjectMessage()
	 */
	public ObjectMessage createObjectMessage() throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#createObjectMessage(java.io.Serializable)
	 */
	public ObjectMessage createObjectMessage(Serializable arg0) throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#createStreamMessage()
	 */
	public StreamMessage createStreamMessage() throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#createTextMessage()
	 */
	public TextMessage createTextMessage() throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#createTextMessage(java.lang.String)
	 */
	public TextMessage createTextMessage(String arg0) throws JMSException {
		if( failOnMessageCreate ) {
			throw new JMSException("Error");
		}
		DummyTextMessage msg = new DummyTextMessage();
		msg.setText(arg0);
		return msg;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#getTransacted()
	 */
	public boolean getTransacted() throws JMSException {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#commit()
	 */
	public void commit() throws JMSException {

	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#rollback()
	 */
	public void rollback() throws JMSException {

	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#close()
	 */
	public void close() throws JMSException {

	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#recover()
	 */
	public void recover() throws JMSException {

	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#getMessageListener()
	 */
	public MessageListener getMessageListener() throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Session#setMessageListener(javax.jms.MessageListener)
	 */
	public void setMessageListener(MessageListener arg0) throws JMSException {
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
	}

	/**
	 * @return
	 */
	public boolean isFailOnSenderCreate() {
		return failOnSenderCreate;
	}

	/**
	 * @param b
	 */
	public void setFailOnSenderCreate(boolean b) {
		failOnSenderCreate = b;
	}

	/**
	 * @return
	 */
	public DummySender getSender() {
		return sender;
	}

	

	/**
	 * @param b
	 */
	public void setFailOnCreateTemp(boolean b) {
		failOnCreateTemp = b;
	}

	/**
	 * @param b
	 */
	public void setFailOnCreateOfReceiverWithMessageSelector(boolean b) {
		failOnCreateOfReceiverWithMessageSelector = b;
	}

	/**
	 * @param b
	 */
	public void setFailOnCreateOfReceiverWithoutMessageSelector(boolean b) {
		failOnCreateOfReceiverWithoutMessageSelector = b;
	}


	/**
	 * @return
	 */
	public DummyReceiver getReceiver() {
		return receiver;
	}

	/**
	 * @param b
	 */
	public void setFailOnMessageCreate(boolean b) {
		failOnMessageCreate = b;
	}

	public MessageConsumer createConsumer(Destination arg0) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public MessageConsumer createConsumer(Destination arg0, String arg1) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public MessageConsumer createConsumer(Destination arg0, String arg1, boolean arg2) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public TopicSubscriber createDurableSubscriber(Topic arg0, String arg1) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public TopicSubscriber createDurableSubscriber(Topic arg0, String arg1, String arg2, boolean arg3) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public MessageProducer createProducer(Destination arg0) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public TemporaryTopic createTemporaryTopic() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public Topic createTopic(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getAcknowledgeMode() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void unsubscribe(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		
	}

}
