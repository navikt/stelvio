package no.stelvio.common.log.appender;

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
 * QueueSession implementation for unit testing.
 * 
 * @version $Id: TestQueueSession.java 2189 2005-04-06 07:37:28Z psa2920 $
 */
public class TestQueueSession implements QueueSession {

	private boolean errorOnCreate = false;
	private QueueSender sender = null;

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSession#createQueue(java.lang.String)
	 */
	public Queue createQueue(String arg0) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSession#createReceiver(javax.jms.Queue)
	 */
	public QueueReceiver createReceiver(Queue arg0) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSession#createReceiver(javax.jms.Queue,
	 *      java.lang.String)
	 */
	public QueueReceiver createReceiver(Queue arg0, String arg1) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSession#createSender(javax.jms.Queue)
	 */
	public QueueSender createSender(Queue arg0) throws JMSException {
		if (errorOnCreate) {
			throw new JMSException("Test error");
		}
		return sender;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSession#createBrowser(javax.jms.Queue)
	 */
	public QueueBrowser createBrowser(Queue arg0) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSession#createBrowser(javax.jms.Queue,
	 *      java.lang.String)
	 */
	public QueueBrowser createBrowser(Queue arg0, String arg1) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSession#createTemporaryQueue()
	 */
	public TemporaryQueue createTemporaryQueue() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#createBytesMessage()
	 */
	public BytesMessage createBytesMessage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#createMapMessage()
	 */
	public MapMessage createMapMessage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#createMessage()
	 */
	public Message createMessage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#createObjectMessage()
	 */
	public ObjectMessage createObjectMessage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#createObjectMessage(java.io.Serializable)
	 */
	public ObjectMessage createObjectMessage(Serializable arg0) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#createStreamMessage()
	 */
	public StreamMessage createStreamMessage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#createTextMessage()
	 */
	public TextMessage createTextMessage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#createTextMessage(java.lang.String)
	 */
	public TextMessage createTextMessage(String arg0) {
		return new TestTextMessage(arg0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#getTransacted()
	 */
	public boolean getTransacted() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#commit()
	 */
	public void commit() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#rollback()
	 */
	public void rollback() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#close()
	 */
	public void close() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#recover()
	 */
	public void recover() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#getMessageListener()
	 */
	public MessageListener getMessageListener() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Session#setMessageListener(javax.jms.MessageListener)
	 */
	public void setMessageListener(MessageListener arg0) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
	}

	/**
	 * Error on create?
	 * 
	 * @return true for error on create, false otherwise.
	 */
	public boolean isErrorOnCreate() {
		return errorOnCreate;
	}

	/**
	 * Gets the sender.
	 * 
	 * @return the sender.
	 */
	public QueueSender getSender() {
		return sender;
	}

	/**
	 * Set error on create.
	 * 
	 * @param b
	 *            true for error on create, false otherwise.
	 */
	public void setErrorOnCreate(boolean b) {
		errorOnCreate = b;
	}

	/**
	 * Sets the sender.
	 * 
	 * @param sender
	 *            the sender.
	 */
	public void setSender(QueueSender sender) {
		this.sender = sender;
	}

	/**
	 * {@inheritDoc}
	 */
	public MessageConsumer createConsumer(Destination arg0) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public MessageConsumer createConsumer(Destination arg0, String arg1) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public MessageConsumer createConsumer(Destination arg0, String arg1, boolean arg2) {
		return null;
	}

    @Override
    public MessageConsumer createSharedConsumer(Topic topic, String s) {
        return null;
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic, String s, String s1) {
        return null;
    }

	/**
	 * {@inheritDoc}
	 */
	public TopicSubscriber createDurableSubscriber(Topic arg0, String arg1) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public TopicSubscriber createDurableSubscriber(Topic arg0, String arg1, String arg2, boolean arg3) {
        return null;
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String s) {
        return null;
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String s, String s1, boolean b) {
        return null;
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String s) {
        return null;
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String s, String s1) {
        return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public MessageProducer createProducer(Destination arg0) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public TemporaryTopic createTemporaryTopic() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Topic createTopic(String arg0) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getAcknowledgeMode() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public void unsubscribe(String arg0) {

	}

}
