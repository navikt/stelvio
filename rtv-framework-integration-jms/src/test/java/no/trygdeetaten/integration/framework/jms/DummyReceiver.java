package no.trygdeetaten.integration.framework.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import junit.framework.AssertionFailedError;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyReceiver implements QueueReceiver {

	private boolean errorOnReceive = false;
	private boolean returnNull = false;
	private boolean errorOnReceiveNoWait = false;
	private int actualReceiveCalls;
	private int expectedReceiveCalls;

	/* (non-Javadoc)
	     * @see javax.jms.QueueReceiver#getQueue()
	     */
	public Queue getQueue() throws JMSException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.MessageConsumer#getMessageSelector()
	 */
	public String getMessageSelector() throws JMSException {

		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.MessageConsumer#getMessageListener()
	 */
	public MessageListener getMessageListener() throws JMSException {

		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.MessageConsumer#setMessageListener(javax.jms.MessageListener)
	 */
	public void setMessageListener(MessageListener arg0) throws JMSException {


	}

	/* (non-Javadoc)
	 * @see javax.jms.MessageConsumer#receive()
	 */
	public Message receive() throws JMSException {
		return receiveCommon(errorOnReceive);
	}

    public Message receive(long arg0) throws JMSException {
		return receive();
	}

	public Message receiveNoWait() throws JMSException {
		return receiveCommon(errorOnReceiveNoWait);
	}

	private Message receiveCommon(final boolean throwException) throws JMSException {
		actualReceiveCalls++;

		if (actualReceiveCalls > expectedReceiveCalls) {
			throw new AssertionFailedError("Too many receive calls");
		}

		if (throwException) {
			throw new JMSException("Test");
		}

		if (returnNull) {
			return null;
		}

		return new DummyTextMessage();
	}

	/* (non-Javadoc)
	     * @see javax.jms.MessageConsumer#close()
	     */
	public void close() throws JMSException {
	}

	/**
	 * @param b
	 */
	public void setErrorOnReceive(boolean b) {
		errorOnReceive = b;
	}

	public void setErrorOnReceiveNoWait(final boolean errorOnReceiveNoWait) {
		this.errorOnReceiveNoWait = errorOnReceiveNoWait;
	}

	/**
	 * @param b
	 */
	public void setReturnNull(boolean b) {
		returnNull = b;
	}

	/**
	 * Sets the expected number of calls to the receive* methods.
	 *
	 * @param expectedReceiveCalls the expected number of calls to the receive* methods.
	 */
	public void setExpectedReceiveCalls(final int expectedReceiveCalls) {
		this.expectedReceiveCalls = expectedReceiveCalls;
	}
}
