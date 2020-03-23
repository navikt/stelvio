package no.stelvio.common.log.appender;

import javax.jms.Connection;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

/**
 * QueueConnectionFactory implementation for unit testing.
 * 
 * @author person356941106810, Accenture
 * @version $Id: TestQueueConnectionFactory.java 2191 2005-04-06 07:45:10Z
 *          psa2920 $
 */
public class TestQueueConnectionFactory implements QueueConnectionFactory {

	private QueueConnection conn = null;
	private boolean errorOnCreate = false;

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueConnectionFactory#createQueueConnection()
	 */
	public QueueConnection createQueueConnection() throws JMSException {
		if (errorOnCreate) {
			throw new JMSException("TEST ERROR");
		}
		return conn;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.jms.QueueConnectionFactory#createQueueConnection(java.lang.String,
	 *      java.lang.String)
	 */
	public QueueConnection createQueueConnection(String arg0, String arg1) throws JMSException {
		if (errorOnCreate) {
			throw new JMSException("TEST ERROR");
		}
		return conn;
	}

	/**
	 * Returns the QueueConnection.
	 * 
	 * @return the connection.
	 */
	public QueueConnection getConn() {
		return conn;
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
	 * Sets the QueueConnection.
	 * 
	 * @param connection
	 *            the connection.
	 */
	public void setConn(QueueConnection connection) {
		conn = connection;
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
	 * {@inheritDoc}
	 */
	public Connection createConnection() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Connection createConnection(String arg0, String arg1) {
        return null;
    }

    @Override
    public JMSContext createContext() {
        return null;
    }

    @Override
    public JMSContext createContext(String s, String s1) {
        return null;
    }

    @Override
    public JMSContext createContext(String s, String s1, int i) {
        return null;
    }

    @Override
    public JMSContext createContext(int i) {
        return null;
	}
}