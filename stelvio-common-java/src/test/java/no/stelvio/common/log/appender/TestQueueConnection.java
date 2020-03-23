package no.stelvio.common.log.appender;

import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

/**
 * QueueConnection implementation for unit testing.
 * 
 * @version $Id: TestQueueConnection.java 2190 2005-04-06 07:44:39Z psa2920 $
 */
public class TestQueueConnection implements QueueConnection {

	private QueueSession session = null;
	private boolean errorOnCreate = false;
	private boolean errorOnStart = false;

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueConnection#createQueueSession(boolean, int)
	 */
	public QueueSession createQueueSession(boolean arg0, int arg1) throws JMSException {
		if (errorOnCreate) {
			throw new JMSException("TestException");
		}
		return session;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueConnection#createConnectionConsumer(javax.jms.Queue,
	 *      java.lang.String, javax.jms.ServerSessionPool, int)
	 */
	public ConnectionConsumer createConnectionConsumer(Queue arg0, String arg1, ServerSessionPool arg2, int arg3) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Connection#getClientID()
	 */
	public String getClientID() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Connection#setClientID(java.lang.String)
	 */
	public void setClientID(String arg0) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Connection#getMetaData()
	 */
	public ConnectionMetaData getMetaData() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Connection#getExceptionListener()
	 */
	public ExceptionListener getExceptionListener() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Connection#setExceptionListener(javax.jms.ExceptionListener)
	 */
	public void setExceptionListener(ExceptionListener arg0) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Connection#start()
	 */
	public void start() throws JMSException {
		if (errorOnStart) {
			throw new JMSException("TestException");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Connection#stop()
	 */
	public void stop() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.Connection#close()
	 */
	public void close() {
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
	 * Returns the QueueSession.
	 * 
	 * @return the session.
	 */
	public QueueSession getSession() {
		return session;
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
	 * Sets the QueueSession.
	 * 
	 * @param session
	 *            the session.
	 */
	public void setSession(QueueSession session) {
		this.session = session;
	}

	/**
	 * Error on start?
	 * 
	 * @return true for error on start, false otherwise.
	 */
	public boolean isErrorOnStart() {
		return errorOnStart;
	}

	/**
	 * Set error on start.
	 * 
	 * @param b
	 *            true for error on start, false otherwise.
	 */
	public void setErrorOnStart(boolean b) {
		errorOnStart = b;
	}

	/**
	 * {@inheritDoc}
	 */
	public ConnectionConsumer createConnectionConsumer(Destination arg0, String arg1, ServerSessionPool arg2, int arg3) {
		return null;
	}

    @Override
    public ConnectionConsumer createSharedConnectionConsumer(Topic topic, String s, String s1, ServerSessionPool serverSessionPool, int i) {
        return null;
    }

	/**
	 * {@inheritDoc}
	 */
	public ConnectionConsumer createDurableConnectionConsumer(Topic arg0, String arg1, String arg2, ServerSessionPool arg3,
			int arg4) {
		return null;
	}

    @Override
    public ConnectionConsumer createSharedDurableConnectionConsumer(Topic topic, String s, String s1, ServerSessionPool serverSessionPool, int i) {
        return null;
    }

	/**
	 * {@inheritDoc}
	 */
	public Session createSession(boolean arg0, int arg1) {
        return null;
    }

    @Override
    public Session createSession(int i) {
        return null;
    }

    @Override
    public Session createSession() {
        return null;
	}
}
