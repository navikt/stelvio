package no.stelvio.integration.framework.jms;

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
 * 
 * @author person356941106810, Accenture
 */
public class DummyQueueConnection implements QueueConnection {
	
	private boolean failOnSessionCreate = false;
	private boolean failOnClose = false;
	private DummyQueueSession session = new DummyQueueSession();
	private boolean failOnStart = false;
	
	/* (non-Javadoc)
	 * @see javax.jms.QueueConnection#createQueueSession(boolean, int)
	 */
	public QueueSession createQueueSession(boolean arg0, int arg1) throws JMSException {
		if( failOnSessionCreate ) {
			throw new JMSException("Test");
		}
		return session;
	}

	/* (non-Javadoc)
	 * @see javax.jms.QueueConnection#createConnectionConsumer(javax.jms.Queue, java.lang.String, javax.jms.ServerSessionPool, int)
	 */
	public ConnectionConsumer createConnectionConsumer(Queue arg0, String arg1, ServerSessionPool arg2, int arg3)
		throws JMSException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Connection#getClientID()
	 */
	public String getClientID() throws JMSException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Connection#setClientID(java.lang.String)
	 */
	public void setClientID(String arg0) throws JMSException {
		

	}

	/* (non-Javadoc)
	 * @see javax.jms.Connection#getMetaData()
	 */
	public ConnectionMetaData getMetaData() throws JMSException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Connection#getExceptionListener()
	 */
	public ExceptionListener getExceptionListener() throws JMSException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.jms.Connection#setExceptionListener(javax.jms.ExceptionListener)
	 */
	public void setExceptionListener(ExceptionListener arg0) throws JMSException {
		

	}

	/* (non-Javadoc)
	 * @see javax.jms.Connection#start()
	 */
	public void start() throws JMSException {
		if( failOnStart ) {
			throw new JMSException("Test");
		}

	}

	/* (non-Javadoc)
	 * @see javax.jms.Connection#stop()
	 */
	public void stop() throws JMSException {
		

	}

	/* (non-Javadoc)
	 * @see javax.jms.Connection#close()
	 */
	public void close() throws JMSException {
		if( failOnClose ) {
			throw new JMSException("Test");
		}
	}

	/**
	 * @return
	 */
	public boolean isFailOnSessionCreate() {
		return failOnSessionCreate;
	}

	/**
	 * @param b
	 */
	public void setFailOnSessionCreate(boolean b) {
		failOnSessionCreate = b;
	}

	/**
	 * @return
	 */
	public boolean isFailOnClose() {
		return failOnClose;
	}

	/**
	 * @param b
	 */
	public void setFailOnClose(boolean b) {
		failOnClose = b;
	}

	/**
	 * @return
	 */
	public DummyQueueSession getSession() {
		return session;
	}

	/**
	 * @param b
	 */
	public void setFailOnStart(boolean b) {
		failOnStart = b;
	}

	public ConnectionConsumer createConnectionConsumer(Destination arg0, String arg1, ServerSessionPool arg2, int arg3) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public ConnectionConsumer createDurableConnectionConsumer(Topic arg0, String arg1, String arg2, ServerSessionPool arg3, int arg4) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public Session createSession(boolean arg0, int arg1) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
