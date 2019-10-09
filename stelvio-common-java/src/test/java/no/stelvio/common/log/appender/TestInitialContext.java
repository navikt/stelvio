package no.stelvio.common.log.appender;

import java.util.Hashtable;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * Context implementation for unit testing.
 * 
 * @author person356941106810, Accenture
 * @version $Id: TestInitialContext.java 2192 2005-04-06 07:53:20Z psa2920 $
 */
public class TestInitialContext implements Context {

	private QueueConnectionFactory qcf = null;
	private Queue q = null;
	private boolean errorOnClose = false;
	private boolean errorOnLookup = false;

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#lookup(javax.naming.Name)
	 */
	public Object lookup(Name name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#lookup(java.lang.String)
	 */
	public Object lookup(String name) throws NamingException {
		if (errorOnLookup) {
			throw new NamingException("TestException");
		}
		if (name == null) {
			throw new NamingException("Null name");
		}
		if (name.indexOf("qcf") != -1) {
			return qcf;
		} else if (name.indexOf("queue") != -1) {
			return q;
		}
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
	 */
	public void bind(Name name, Object obj) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
	 */
	public void bind(String name, Object obj) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
	 */
	public void rebind(Name name, Object obj) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
	 */
	public void rebind(String name, Object obj) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#unbind(javax.naming.Name)
	 */
	public void unbind(Name name) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#unbind(java.lang.String)
	 */
	public void unbind(String name) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
	 */
	public void rename(Name oldName, Name newName) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
	 */
	public void rename(String oldName, String newName) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#list(javax.naming.Name)
	 */
	public NamingEnumeration<NameClassPair> list(Name name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#list(java.lang.String)
	 */
	public NamingEnumeration<NameClassPair> list(String name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#listBindings(javax.naming.Name)
	 */
	public NamingEnumeration<Binding> listBindings(Name name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#listBindings(java.lang.String)
	 */
	public NamingEnumeration<Binding> listBindings(String name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
	 */
	public void destroySubcontext(Name name) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#destroySubcontext(java.lang.String)
	 */
	public void destroySubcontext(String name) {
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#createSubcontext(javax.naming.Name)
	 */
	public Context createSubcontext(Name name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#createSubcontext(java.lang.String)
	 */
	public Context createSubcontext(String name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#lookupLink(javax.naming.Name)
	 */
	public Object lookupLink(Name name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#lookupLink(java.lang.String)
	 */
	public Object lookupLink(String name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#getNameParser(javax.naming.Name)
	 */
	public NameParser getNameParser(Name name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#getNameParser(java.lang.String)
	 */
	public NameParser getNameParser(String name) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#composeName(javax.naming.Name, javax.naming.Name)
	 */
	public Name composeName(Name name, Name prefix) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
	 */
	public String composeName(String name, String prefix) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#addToEnvironment(java.lang.String, java.lang.Object)
	 */
	public Object addToEnvironment(String propName, Object propVal) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
	 */
	public Object removeFromEnvironment(String propName) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#getEnvironment()
	 */
	public Hashtable<?, ?> getEnvironment() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#close()
	 */
	public void close() throws NamingException {
		if (errorOnClose) {
			throw new NamingException("TEST ERROR");
		}
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.naming.Context#getNameInNamespace()
	 */
	public String getNameInNamespace() {
		return null;
	}

	/**
	 * Returns the Queue.
	 * 
	 * @return the queue.
	 */
	public Queue getQ() {
		return q;
	}

	/**
	 * Returns the QueueConnectionFactory.
	 * 
	 * @return the queue connection factory.
	 */
	public QueueConnectionFactory getQcf() {
		return qcf;
	}

	/**
	 * Sets the Queue.
	 * 
	 * @param queue the queue.
	 */
	public void setQ(Queue queue) {
		q = queue;
	}

	/**
	 * Sets the QueueConnectionFactory.
	 * 
	 * @param factory the queue connection factory.
	 */
	public void setQcf(QueueConnectionFactory factory) {
		qcf = factory;
	}

	/**
	 * Error on close?
	 * 
	 * @return true for error on close, false otherwise.
	 */
	public boolean isErrorOnClose() {
		return errorOnClose;
	}

	/**
	 * Set error on close.
	 * 
	 * @param b true for error on close, false otherwise.
	 */
	public void setErrorOnClose(boolean b) {
		errorOnClose = b;
	}

	/**
	 * Error on lookup?
	 * 
	 * @return true for error on lookup, false otherwise.
	 */
	public boolean isErrorOnLookup() {
		return errorOnLookup;
	}

	/**
	 * Set error on lookup.
	 * 
	 * @param b true for error on lookup, false otherwise.
	 */
	public void setErrorOnLookup(boolean b) {
		errorOnLookup = b;
	}

}
