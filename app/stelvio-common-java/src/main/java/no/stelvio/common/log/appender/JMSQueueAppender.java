package no.stelvio.common.log.appender;

import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

/**
 * A simple appender that publishes events to a JMS Queue. The events
 * are serialized and transmitted as JMS message type {@link TextMessage}.
 *
 * <p>JMS Queues and QueueConnectionFactories are administered objects that are retrieved
 * using JNDI messaging which in turn requires the retrieval of a JNDI Context.</p>
 * 
 * <p>This class is a modified version of the JMSAppender that comes with Log4J. This version
 * is using Queues instead of Topics. The configuration parameters are therefore almost identical.
 * The following modifications have been made:
 * <ul>
 * 	<li>The appender uses Queues instead of Topics</li>
 * 	<li>A TextMessage is sent rather than a ObjectMessage</li>
 * 	<li>Added more detailed error logging in activateOptions</li>
 * 	<li>Added the requirement of a layout to format the text format before sending.</li>
 * </ul>
 * </p>
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author person356941106810, Accenture
 * @version $Id: JMSQueueAppender.java 2741 2006-01-20 09:07:01Z skb2930 $
 */
public class JMSQueueAppender extends AppenderSkeleton {

	String securityPrincipalName = null;
	String securityCredentials = null;
	String initialContextFactoryName = null;
	String urlPkgPrefixes = null;
	String providerURL = null;
	String queueBindingName = null;
	String qcfBindingName = null;
	String userName = null;
	String password = null;

	QueueConnection queueConnection = null;
	QueueSession queueSession = null;
	QueueSender queueSender = null;

	private static final String MESSAGE_TERMINATOR = "].";

	/**
	 * The <b>QueueConnectionFactoryBindingName</b> option takes a
	 * string value. Its value will be used to lookup the appropriate
	 * <code>QueueConnectionFactory</code> from the JNDI context.
	 * 
	 * @param qcfBindingName the JNDI name of the QueueConnectionFactory.
	 */
	public void setQueueConnectionFactoryBindingName(String qcfBindingName) {
		this.qcfBindingName = qcfBindingName;
	}

	/**
	 * Returns the value of the <b>QueueConnectionFactoryBindingName</b> option.
	 * 
	 * @return the JNDI name of the QueueConnectionFactory.
	 */
	public String getQueueConnectionFactoryBindingName() {
		return qcfBindingName;
	}

	/**
	 * The <b>QueueBindingName</b> option takes a string value. Its value will be used to lookup the appropriate
	 * <code>Queue</code> from the JNDI context.
	 * 
	 * @param queueBindingName the JNDI name of the Queue.
	 */
	public void setQueueBindingName(String queueBindingName) {
		this.queueBindingName = queueBindingName;
	}

	/**
	 * Returns the value of the <b>QueueBindingName</b> option.
	 * 
	 * @return the JNDI name of the Queue.
	 */
	public String getQueueBindingName() {
		return queueBindingName;
	}

	/**
	 * Options are activated and become effective only after calling this method.
	 * 
	 * @see org.apache.log4j.spi.OptionHandler
	 */
	public void activateOptions() {
		if (!validateOptions()) {
			return;
		}

		QueueConnectionFactory queueConnectionFactory = null;

		LogLog.debug("Getting initial context.");
		// If we have configured specific JNDI properties use those
		Context jndi = getJndiContext();

		if (jndi == null) {
			// return if we get no context
			return;
		}

		LogLog.debug("Looking up [" + qcfBindingName + "] and [" + queueBindingName + MESSAGE_TERMINATOR);
		Queue queue = null;

		try {
			queueConnectionFactory = (QueueConnectionFactory) lookup(jndi, qcfBindingName);
			queue = (Queue) lookup(jndi, queueBindingName);
		} catch (NamingException e) {
			errorHandler.error(
				"Error while looking up QueueConnectionFactory named ["
					+ qcfBindingName
					+ "] or Queue named ["
					+ queueBindingName
					+ "] for appender named ["
					+ name
					+ MESSAGE_TERMINATOR,
				e,
				ErrorCode.GENERIC_FAILURE);
			return;
		}

		LogLog.debug("About to create QueueConnection.");
		getQueueConnection(queueConnectionFactory);
		if (queueConnection == null) {
			// do not continue if we have no connection
			return;
		}

		LogLog.debug("Creating QueueSession, non-transactional, " + "in AUTO_ACKNOWLEDGE mode.");
		getQueueSession();
		if (queueSession == null) {
			return;
		}

		LogLog.debug("Creating QueueSender.");
		getQueueSender(queue);
		if (queueSender == null) {
			return;
		}

		LogLog.debug("Starting QueueConnection.");
		try {
			queueConnection.start();
		} catch (JMSException e) {
			errorHandler.error(
				"Error while starting queue connection for appender named [" + name + MESSAGE_TERMINATOR,
				e,
				ErrorCode.GENERIC_FAILURE);
			return;
		}

		try {
			jndi.close();
		} catch (NamingException e) {
			errorHandler.error(
				"Error while closing JNDI context for appender named [" + name + MESSAGE_TERMINATOR,
				e,
				ErrorCode.GENERIC_FAILURE);
			return;
		}
	}

	/**
	 * Creates a queue sender for the specified queue.
	 *
	 * @param queue the queue to create a queue sender for.
	 */
	private void getQueueSender(Queue queue) {
		try {
			queueSender = queueSession.createSender(queue);
		} catch (JMSException e) {
			errorHandler.error(
				"Error while creating queue sender for appender named [" + name + MESSAGE_TERMINATOR,
				e,
				ErrorCode.GENERIC_FAILURE);
		}
	}

	/**
	 * Creates a queue session.
	 */
	private void getQueueSession() {
		try {
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			errorHandler.error(
				"Error while creating queue session for appender named [" + name + MESSAGE_TERMINATOR,
				e,
				ErrorCode.GENERIC_FAILURE);
		}
	}

	/**
	 * Creates a queue connection.
	 *
	 * @param queueConnectionFactory the factory to use to create a connection.
	 */
	private void getQueueConnection(QueueConnectionFactory queueConnectionFactory) {
		try {
			if (userName != null) {
				queueConnection = queueConnectionFactory.createQueueConnection(userName, password);
			} else {
				queueConnection = queueConnectionFactory.createQueueConnection();
			}
		} catch (JMSException e) {
			errorHandler.error(
				"Error while creating queue connection for appender named [" + name + MESSAGE_TERMINATOR,
				e,
				ErrorCode.GENERIC_FAILURE);
		}
	}

	/**
	 * Retrieves the JNDI context.
	 *
	 * @return the JNDI context.
	 */
	private Context getJndiContext() {
		Context jndi = null;
		if (initialContextFactoryName != null) {

			LogLog.debug("Using configured JNDI context");
			Properties env = new Properties();
			env.setProperty(Context.INITIAL_CONTEXT_FACTORY, initialContextFactoryName);

			if (providerURL != null) {
				env.setProperty(Context.PROVIDER_URL, providerURL);
			} else {
				LogLog.warn(
					"You have set InitialContextFactoryName option but not the "
						+ "ProviderURL. This is likely to cause problems.");
			}
			if (urlPkgPrefixes != null) {
				env.setProperty(Context.URL_PKG_PREFIXES, urlPkgPrefixes);
			}

			if (securityPrincipalName != null) {
				env.setProperty(Context.SECURITY_PRINCIPAL, securityPrincipalName);

				if (securityCredentials != null) {
					env.setProperty(Context.SECURITY_CREDENTIALS, securityCredentials);
				} else {
					LogLog.warn(
						"You have set SecurityPrincipalName option but not the "
							+ "SecurityCredentials. This is likely to cause problems.");
				}
			}
			try {
				jndi = new InitialContext(env);
			} catch (NamingException e) {
				errorHandler.error(
					"Error while initializing JNDI context for appender named [" + name + MESSAGE_TERMINATOR,
					e,
					ErrorCode.GENERIC_FAILURE);
			}
		} else {
			// use the already present JNDI context
			LogLog.debug("Using predefined JNDI context.");
			try {
				jndi = new InitialContext();
			} catch (NamingException e) {
				errorHandler.error(
					"Error while initializing JNDI context for appender named [" + name + MESSAGE_TERMINATOR,
					e,
					ErrorCode.GENERIC_FAILURE);
			}
		}

		return jndi;
	}

	/**
	 * Validates that the minimum requirements of the appender is met. These are:
	 * <ul>
	 * 	<li>A QueueConnectionFactory JNDI name is set</li>
	 *  <li>A Queue JNDI name is set</li>
	 * </ul>
	 *
	 * @return true if the requirements are met.
	 */
	protected boolean validateOptions() {
		boolean result = true;
		if (qcfBindingName == null || "".equals(qcfBindingName.trim())) {
			errorHandler.error(
				"The binding name for a QueueConnectionFactory is missing for appender [" + name + MESSAGE_TERMINATOR);
			result = false;
		} else if (queueBindingName == null || "".equals(queueBindingName.trim())) {
			errorHandler.error("The binding name for a Queue is missing for appender [" + name + MESSAGE_TERMINATOR);
			result = false;
		}
		return result;
	}

	/**
	 * Convenience method for looking up object on the JNDI context.
	 * 
	 * @param ctx the JNDI context.
	 * @param name the name of the object to look up.
	 * @return the object on the JNDI context.
	 * @throws NamingException lookup failed.
	 */
	protected Object lookup(Context ctx, String name) throws NamingException {
		try {
			return ctx.lookup(name);
		} catch (NameNotFoundException e) {
			LogLog.error("Could not find name [" + name + MESSAGE_TERMINATOR);
			throw e;
		}
	}

	/**
	 * Validates that this appender is actually ready to do work.
	 * 
	 * @return false if the appender is not ready to work.
	 */
	protected boolean checkEntryConditions() {
		String fail = null;

		if (this.queueConnection == null) {
			fail = "No QueueConnection";
		} else if (this.queueSession == null) {
			fail = "No QueueSession";
		} else if (this.queueSender == null) {
			fail = "No QueueSender";
		} else if (this.layout == null) {
			fail = "No layout set";
		}

		if (fail != null) {
			errorHandler.error(fail + " for JMSQueueAppender named [" + name + MESSAGE_TERMINATOR);
			return false;
		}
		return true;

	}

	/**
	 * Close this JMSAppender. Closing releases all resources used by the
	 * appender. A closed appender cannot be re-opened.
	 */
	public synchronized void close() {
		// The synchronized modifier avoids concurrent append and close operations
		if (closed) {
			return;
		}

		LogLog.debug("Closing appender [" + name + MESSAGE_TERMINATOR);
		closed = true;

		try {
			if (queueConnection != null) {
				// There is no need to close producers, consumers and sessions of a closed connection 
				queueConnection.close();
			}
		} catch (Exception e) {
			LogLog.error("Error while closing JMSQueueAppender [" + name + MESSAGE_TERMINATOR, e);
		}
		// Help garbage collection
		queueSender = null;
		queueSession = null;
		queueConnection = null;
	}

	/**
	 * This method called by {@link AppenderSkeleton#doAppend} method to
	 * do most of the real appending work.
	 * 
	 * @param event the logging event to log.
	 */
	public void append(LoggingEvent event) {
		if (!checkEntryConditions()) {
			return;
		}
		String msgContent = this.layout.format(event);

		if (layout.ignoresThrowable()) {
			String[] s = event.getThrowableStrRep();
			if (s != null) {
				int len = s.length;
				msgContent = msgContent.concat(":");
				for (int i = 0; i < len; i++) {
					msgContent = msgContent.concat(s[i]);
					msgContent = msgContent.concat(Layout.LINE_SEP);
				}
			}
		}

		try {
			TextMessage msg = queueSession.createTextMessage(msgContent);
			queueSender.send(msg);
		} catch (Exception e) {
			errorHandler.error(
				"Could not publish message in JMSAppender [" + name + MESSAGE_TERMINATOR,
				e,
				ErrorCode.GENERIC_FAILURE);
		}
	}

	/**
	 * Returns the value of the <b>InitialContextFactoryName</b> option.
	 * See {@link #setInitialContextFactoryName} for more details on the
	 * meaning of this option.
	 * 
	 * @return the name of the InitialContextFactory.
	 */
	public String getInitialContextFactoryName() {
		return initialContextFactoryName;
	}

	/**
	 * Setting the <b>InitialContextFactoryName</b> method will cause this <code>JMSQueueAppender</code> instance to use
	 * the {@link javax.naming.InitialContext#InitialContext(java.util.Hashtable)} method instead of the no-argument
	 * constructor. If you set this option, you should also at least set the <b>ProviderURL</b> option.
	 *
	 * @param initialContextFactoryName the name of the InitialContextFactory to use.
	 * @see #setProviderURL(String)
	 */
	public void setInitialContextFactoryName(String initialContextFactoryName) {
		this.initialContextFactoryName = initialContextFactoryName;
	}

	/**
	 * Returns the configured provider URL used for JNDI lookups.
	 *  
	 * @return the provider URL.
	 */
	public String getProviderURL() {
		return providerURL;
	}

	/**
	 * Sets the provider URL to use for JNDI lookups.
	 *
	 * @param providerURL the provider URL.
	 */
	public void setProviderURL(String providerURL) {
		this.providerURL = providerURL;
	}

	/**
	 * Returns the URL package prefixes used for JNDI lookups.
	 *
	 * @return the URL package prefixes.
	 */
	String getURLPkgPrefixes() {
		return urlPkgPrefixes;
	}

	/**
	 * Sets the URL package prefixes used for JNDI lookups.
	 *
	 * @param urlPkgPrefixes the URL package prefixes.
	 */
	public void setURLPkgPrefixes(String urlPkgPrefixes) {
		this.urlPkgPrefixes = urlPkgPrefixes;
	}

	/**
	 * Returns the Security credentials used when accessing the JNDI context.
	 * 
	 * @return the security credentials.
	 */
	public String getSecurityCredentials() {
		return securityCredentials;
	}

	/**
	 * Sets the security credentials used for JNDI lookups.
	 *
	 * @param securityCredentials the security credentials.
	 */
	public void setSecurityCredentials(String securityCredentials) {
		this.securityCredentials = securityCredentials;
	}

	/**
	 * Returns the Security principal name used when accessing the JNDI context.
	 *
	 * @return the security principal name.
	 */
	public String getSecurityPrincipalName() {
		return securityPrincipalName;
	}

	/**
	 * Sets the Security principal name used when accessing the JNDI context.
	 *
	 * @param securityPrincipalName the security principal name.
	 */
	public void setSecurityPrincipalName(String securityPrincipalName) {
		this.securityPrincipalName = securityPrincipalName;
	}

	/**
	 * Returns the user name used when creating a queue connection.
	 *
	 * @return the user name.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * The user name to use when {@link
	 * QueueConnectionFactory#createQueueConnection(String, String)
	 * creating a queue session}.  If you set this option, you should
	 * also set the <b>Password</b> option.
	 *
	 * @param userName the user name for creation.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Retrieves the password used when creating a queue connection.
	 * 
	 * @return the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * The password to use when creating a queue session.
	 *
	 * @param password the password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * The JMSAppender sends serialized events and consequently does not
	 * require a layout.
	 *
	 * @return will always return true
	 */
	public boolean requiresLayout() {
		return true;
	}
}