package no.stelvio.common.log.appender;

import javax.jms.CompletionListener;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

/**
 * QueueSender implementation for unit testing.
 * 
 * @author person356941106810, Accenture
 * @version $Id: TestQueueSender.java 2189 2005-04-06 07:37:28Z psa2920 $
 */
public class TestQueueSender implements QueueSender {

	private boolean errorOnOperation = false;
	private Message msg = null;

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSender#getQueue()
	 */
	public Queue getQueue() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSender#send(javax.jms.Message)
	 */
	public void send(Message arg0) throws JMSException {
		msg = arg0;
		if (errorOnOperation) {
			throw new JMSException("Test error");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSender#send(javax.jms.Message, int, int, long)
	 */
	public void send(Message arg0, int arg1, int arg2, long arg3) throws JMSException {
		msg = arg0;
		if (errorOnOperation) {
			throw new JMSException("Test error");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSender#send(javax.jms.Queue, javax.jms.Message)
	 */
	public void send(Queue arg0, Message arg1) throws JMSException {
		msg = arg1;
		if (errorOnOperation) {
			throw new JMSException("Test error");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueSender#send(javax.jms.Queue, javax.jms.Message, int,
	 *      int, long)
	 */
	public void send(Queue arg0, Message arg1, int arg2, int arg3, long arg4) throws JMSException {
		msg = arg1;
		if (errorOnOperation) {
			throw new JMSException("Test error");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#setDisableMessageID(boolean)
	 */
	public void setDisableMessageID(boolean arg0) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#getDisableMessageID()
	 */
	public boolean getDisableMessageID() throws JMSException {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#setDisableMessageTimestamp(boolean)
	 */
	public void setDisableMessageTimestamp(boolean arg0) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#getDisableMessageTimestamp()
	 */
	public boolean getDisableMessageTimestamp() throws JMSException {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#setDeliveryMode(int)
	 */
	public void setDeliveryMode(int arg0) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#getDeliveryMode()
	 */
	public int getDeliveryMode() throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#setPriority(int)
	 */
	public void setPriority(int arg0) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#getPriority()
	 */
	public int getPriority() throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#setTimeToLive(long)
	 */
	public void setTimeToLive(long arg0) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#getTimeToLive()
	 */
	public long getTimeToLive() throws JMSException {
		return 0;
	}

    @Override
    public void setDeliveryDelay(long l) throws JMSException {

    }

    @Override
    public long getDeliveryDelay() throws JMSException {
        return 0;
    }

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.MessageProducer#close()
	 */
	public void close() throws JMSException {
	}

	/**
	 * Error on operation?
	 * 
	 * @return true for error on operation, false otherwise.
	 */
	public boolean isErrorOnOperation() {
		return errorOnOperation;
	}

	/**
	 * Set error on operation.
	 * 
	 * @param b
	 *            true for error on operation, false otherwise.
	 */
	public void setErrorOnOperation(boolean b) {
		errorOnOperation = b;
	}

	/**
	 * Returs the message.
	 * 
	 * @return the message.
	 */
	public Message getMsg() {
		return msg;
	}

	/**
	 * {@inheritDoc}
	 */
	public Destination getDestination() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void send(Destination arg0, Message arg1) throws JMSException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void send(Destination arg0, Message arg1, int arg2, int arg3, long arg4) throws JMSException {
	}

    @Override
    public void send(Message message, CompletionListener completionListener) throws JMSException {

    }

    @Override
    public void send(Message message, int i, int i1, long l, CompletionListener completionListener) throws JMSException {

    }

    @Override
    public void send(Destination destination, Message message, CompletionListener completionListener) throws JMSException {

    }

    @Override
    public void send(Destination destination, Message message, int i, int i1, long l, CompletionListener completionListener) throws JMSException {

    }
}
