package no.stelvio.integration.framework.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummySender implements QueueSender {

	private boolean errorOnSend = false;
	private int messagesSent;

	public Queue getQueue() throws JMSException {
		return null;
	}

	public void send(Message arg0) throws JMSException {
		if( errorOnSend ) {
			throw new JMSException("True");
		}

		messagesSent++;
	}

	public void send(Message arg0, int arg1, int arg2, long arg3) throws JMSException {
		send(null);
	}

	public void send(Queue arg0, Message arg1) throws JMSException {
		send(null);
	}

	public void send(Queue arg0, Message arg1, int arg2, int arg3, long arg4) throws JMSException {
		send(null);
	}

	public void setDisableMessageID(boolean arg0) throws JMSException {
	}

	public boolean getDisableMessageID() throws JMSException {
		return false;
	}

	public void setDisableMessageTimestamp(boolean arg0) throws JMSException {
	}

	public boolean getDisableMessageTimestamp() throws JMSException {
		return false;
	}

	public void setDeliveryMode(int arg0) throws JMSException {
	}

	public int getDeliveryMode() throws JMSException {
		return 0;
	}

	public void setPriority(int arg0) throws JMSException {
	}

	public int getPriority() throws JMSException {
		return 0;
	}

	public void setTimeToLive(long arg0) throws JMSException {
	}

	public long getTimeToLive() throws JMSException {
		return 0;
	}

	public void close() throws JMSException {
	}

	/**
	 * @return whether we should error out when sending.
	 */
	public boolean isErrorOnSend() {
		return errorOnSend;
	}

	/**
	 * @param b
	 */
	public void setErrorOnSend(boolean b) {
		errorOnSend = b;
	}

	/**
	 * Returns how many messages are sent.
	 *
	 * @return how many messages are sent.
	 */
	public int getMessagesSent() {
		return messagesSent;
	}
}
