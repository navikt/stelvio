package no.stelvio.integration.framework.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyQueueConnectionFactory implements QueueConnectionFactory {
	
	private boolean failOnCreate = false;
	private DummyQueueConnection conn = new DummyQueueConnection();
	
	/* (non-Javadoc)
	 * @see javax.jms.QueueConnectionFactory#createQueueConnection()
	 */
	public QueueConnection createQueueConnection() throws JMSException {
		if( failOnCreate ) {
			throw new JMSException("TEST");
		}
		return conn;
	}

	/* (non-Javadoc)
	 * @see javax.jms.QueueConnectionFactory#createQueueConnection(java.lang.String, java.lang.String)
	 */
	public QueueConnection createQueueConnection(String arg0, String arg1) throws JMSException {
		return createQueueConnection();
	}

	/**
	 * @return
	 */
	public boolean isFailOnCreate() {
		return failOnCreate;
	}

	/**
	 * @param b
	 */
	public void setFailOnCreate(boolean b) {
		failOnCreate = b;
	}

	/**
	 * @return
	 */
	public DummyQueueConnection getConn() {
		return conn;
	}

	public Connection createConnection() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public Connection createConnection(String arg0, String arg1) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
