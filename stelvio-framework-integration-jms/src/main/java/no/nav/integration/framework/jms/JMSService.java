package no.nav.integration.framework.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import org.apache.commons.lang.StringUtils;

import no.nav.common.framework.Constants;
import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.ejb.LookupHelper;
import no.nav.common.framework.error.SystemException;
import no.nav.common.framework.performance.MonitorKey;
import no.nav.common.framework.performance.PerformanceMonitor;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceRequest;
import no.nav.common.framework.service.ServiceResponse;
import no.nav.integration.framework.service.IntegrationService;

/**
 * Integration service class which handles JMS communication. This service supports the following:
 * <ul>
 * 	<li>Synchronous and asynchronous operation.</li>
 * 	<li>Dynamic and static return queues.</li>
 * 	<li>Message selecting for synchronous operations on shared return queues.</li>
  * </ul>
 * 
 * @author person356941106810, Accenture
 * @version $Revision: 2737 $ $Author: skb2930 $ $Date: 2006-01-12 03:24:04 +0100 (Thu, 12 Jan 2006) $
 */
public class JMSService extends IntegrationService {

	private static final int CREATE_TEMP_QUEUE_RETRY_COUNT = 3;
	private static final String SPECIFY_RETURN_QUEUE = "specifyReturnQueue";

	/** The performance monitoring key for the whole service call. */
	protected static final MonitorKey JMSSERVICE_MONITOR_KEY = new MonitorKey("JMSService", MonitorKey.RESOURCE);
	/** The performance monitoring key for only the receive message call. */
	protected static final MonitorKey RECEIVE_MONITOR_KEY = new MonitorKey("Receive Message", MonitorKey.ADDITIONAL);

	/** Flag which indicates wether to use synchronous or asynchronous communication */
	protected boolean isSynchronous;

	/** Flag which indicates wether or not to use a temp queue as a return queue */
	protected boolean useTempQueue;

	/** Flag which indicates wether or not to specify which return queue should be used */
	protected boolean specifyReturnQueue;

	/** Holds the JNDI name of the JMS queue connection factory */
	protected String queueConnectionFactoryJndi;

	/** Holds the JNDI name of the JMS queue used to send the message */
	protected String queueJndi;

	/** Holds the JNDI name of the return queue name */
	protected String returnQueueJndi;

	/** Holds the timout value to use when using synchronous communication */
	protected long timeout;

	/** The handler to use to process the message */
	protected MessageHandler messageHandler;

	/** The formatter used to prepare the message for sending */
	protected MessageFormatter messageFormatter;

	/** The JNDI helper used for lookups */
	protected LookupHelper lookupHelper;

	/** The time to live for the message */
	protected long messageTtl;

	/** A ref to the QCF */
	protected QueueConnectionFactory queueConnectionFactory;

	/** A ref to the Queue */
	protected Queue queue;

	/** A ref to the return queue */
	protected Queue returnQueue;

	/** The message selector to use for synchronous operation */
	protected String messageSelector;

	/**
	 * Validates the configuration of this service and performs further initialization. This method
	 * should be called after all the properties have been set.
	 */
	public final void init() {
		if (log.isDebugEnabled()) {
			log.debug(
				"Initializing JMSService with the following properties:\n"
				+ "Queue JNDI name:"
				+ queueJndi
				+ "\nQueue Connection Factory JNDI name:"
				+ queueConnectionFactoryJndi
				+ "\nSynchronuous:"
				+ isSynchronous
				+ "\nUsing temp queue for return:"
				+ useTempQueue
				+ "\nReturn queue JNDI name:"
				+ returnQueueJndi
				+ "\nUsing specified return queue:"
				+ specifyReturnQueue
				+ "\nTimeout:"
				+ timeout);
		}

		if (StringUtils.isBlank(queueConnectionFactoryJndi)) {
			throw new SystemException(FrameworkError.JMS_SERVICE_PROPERTY_MISSING, "queueConnectionFactoryJndi");
		}

		if (StringUtils.isBlank(queueJndi)) {
			throw new SystemException(FrameworkError.JMS_SERVICE_PROPERTY_MISSING, "queueJndi");
		}

		if (null == lookupHelper) {
			throw new SystemException(FrameworkError.JMS_SERVICE_PROPERTY_MISSING, "lookupHelper");
		}

		if (useTempQueue && !specifyReturnQueue) {
			throw new SystemException(
				FrameworkError.JMS_INVALID_RETURN_QUEUE,
			    new String[] { "useTempQueue", SPECIFY_RETURN_QUEUE });
		}

		if (StringUtils.isNotBlank(returnQueueJndi) && !specifyReturnQueue) {
			throw new SystemException(
				FrameworkError.JMS_INVALID_RETURN_QUEUE,
			    new String[] { "returnQueueJndi", SPECIFY_RETURN_QUEUE });
		}

		if (StringUtils.isNotBlank(returnQueueJndi) && useTempQueue) {
			throw new SystemException(
				FrameworkError.JMS_INVALID_RETURN_QUEUE,
			    new String[] { "returnQueueJndi", SPECIFY_RETURN_QUEUE, "useTempQueue" });
		}

		if (isSynchronous && !specifyReturnQueue) {
			throw new SystemException(
				FrameworkError.JMS_INVALID_SYNCH_CONFIG_ERROR,
			    new String[] { "isSynchronous", SPECIFY_RETURN_QUEUE });
		}

		if (specifyReturnQueue && !useTempQueue && StringUtils.isBlank(returnQueueJndi)) {
			throw new SystemException(
				FrameworkError.JMS_INVALID_RETURN_QUEUE,
			    new String[] { SPECIFY_RETURN_QUEUE, "useTempQueue", "returnQueueJndi" });
		}

		if (null == messageFormatter) {
			throw new SystemException(FrameworkError.JMS_SERVICE_PROPERTY_MISSING, "messageFormatter");
		}

		if (isSynchronous && null == messageHandler) {
			throw new SystemException(
				FrameworkError.JMS_SERVICE_PROPERTY_MISSING,
			    new String[] { "isSynchronous", "messageHandler" });
		}

		queueConnectionFactory = (QueueConnectionFactory) lookupHelper.lookup(queueConnectionFactoryJndi, null);
		queue = (Queue) lookupHelper.lookup(queueJndi, null);

		if (StringUtils.isNotBlank(returnQueueJndi)) {
			returnQueue = (Queue) lookupHelper.lookup(returnQueueJndi, null);
		}

	}

	/**
	 * Performs an operation against a system using JMS as the means of communications.
	 * When executing in synchronous mode the method will lock waiting for a response from the
	 * system. When executing in asynchronous mode this method will always return null since
	 * it will not wait for a response.
	 *
	 * @param request the request to the backend system.
	 * @return The response from the backend system.
	 * @throws ServiceFailedException the system returned an error during the execution of the request.
	 */
	protected ServiceResponse doExecute(final ServiceRequest request) throws ServiceFailedException {
		QueueConnection conn = null;
		QueueSession session = null;

		PerformanceMonitor.start(JMSSERVICE_MONITOR_KEY, queueJndi);
		try {
			try {
				conn = queueConnectionFactory.createQueueConnection();
			} catch (JMSException e) {
				PerformanceMonitor.fail(JMSSERVICE_MONITOR_KEY);
				throw new SystemException(FrameworkError.JMS_CONNECTION_ERROR, e);
			}

			try {
				session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			} catch (JMSException e) {
				PerformanceMonitor.fail(JMSSERVICE_MONITOR_KEY);
				throw new SystemException(FrameworkError.JMS_SESSION_CREATION_ERROR, e);
			}

			final Queue retQueue = createReturnQueue(session);
			final Message[] messagesIn = createMessages(request, session, retQueue);
			final Message[] messagesOut = sendReceive(session, messagesIn, conn, retQueue);
			final ServiceResponse[] responses = createResponses(messagesOut);

			if (hasMultiple(request)) {
				PerformanceMonitor.end(JMSSERVICE_MONITOR_KEY);
				return new ServiceResponse(Constants.JMSSERVICE_MULTIPLE_MESSAGES, responses);
			} else if (hasNoResponse(responses)) {
				PerformanceMonitor.end(JMSSERVICE_MONITOR_KEY);
				return null;
			} else {
				PerformanceMonitor.end(JMSSERVICE_MONITOR_KEY);
				return responses[0];
			}
		} catch (ServiceFailedException sfe) {
			PerformanceMonitor.fail(JMSSERVICE_MONITOR_KEY);
			throw sfe;
		} catch (RuntimeException re) {
			PerformanceMonitor.fail(JMSSERVICE_MONITOR_KEY);
			throw re;
		} finally {
			if (null != session) {
				try {
					session.close();
				} catch (JMSException e) {
					if (log.isWarnEnabled()) {
						log.warn("Failed to close the JMS QueueSession. The linked exception is " + e.getLinkedException(), e);
					}
				}
			}
			if (null != conn) {
				try {
					conn.close();
				} catch (JMSException e) {
					if (log.isWarnEnabled()) {
						log.warn(
								"Failed to close the JMS QueueConnection. The linked exception is " + e.getLinkedException(),
						        e);
					}
				}
			}
		}
	}

	/**
	 * Checks whether there is no response at all.
	 *
	 * @param responses the array of <code>ServiceResponse</code>s to check against.
	 * @return true if the array of <code>ServiceResponse</code>s is empty, false otherwise.
	 */
	private boolean hasNoResponse(final ServiceResponse[] responses) {
		return null == responses || 0 == responses.length;
	}

	/**
	 * Send messages and receive messages from the specified queue.
	 * <p>
	 * The JMSCorrelationID field is used to map between messages sent and messages returned. <code>XMLMessageFormatter</code>
	 * will fill it in the message to send from the specified field in the <code>ServiceRequest</code>s. But on the receiver's
	 * end the JMSMessageID of our sent message will be put into the JMSCorrelationID of the returned message. So to be able
	 * to have the same JMSCorrelationID in the returned messages, we must map between the sent message's JMSMessageID and
	 * the returned message's JMSCorrelationID.
	 * <p>
	 * Implementation note: That the receiver is using JMSMessageID might not be correct for every service called. Then this
	 * routine must be changed to enable choosing the value to map with dynamically.
	 *
	 * @param session the <code>QueueSession</code> to use.
	 * @param messagesIn the messages to send.
	 * @param connection the <code>QueueConnection</code> to use.
	 * @param retQueue the return <code>Queue</code> to use.
	 * @return the messages received from the return queue.
	 * @see QueueSession
	 * @see QueueConnection
	 * @see Queue
	 */
	private Message[] sendReceive(final QueueSession session,
	                               final Message[] messagesIn,
	                               final QueueConnection connection,
	                               final Queue retQueue) {
		sendMessages(session, messagesIn);

		// As the receiver puts JMSMessageID into the JMSCorrelationID field, we must map from/to values here
		final Map map = new HashMap(messagesIn.length);
		final Message[] messagesOut;

		try {
			for (int idx = 0; idx < messagesIn.length; idx++) {
				Message message = messagesIn[idx];
				// Hold onto the old value of JMSCorrelationID
				map.put(message.getJMSMessageID(), message.getJMSCorrelationID());
			}

			messagesOut = receiveMessages(connection, session, retQueue, messagesIn.length);

			for (int idx = 0; idx < messagesOut.length; idx++) {
				Message message = messagesOut[idx];
				// The new value of JMSCorrelationID is the sent message's JMSMessageID.
				// To set the old value on the message, look it up in the map 
				message.setJMSCorrelationID((String) map.get(message.getJMSCorrelationID()));
			}
		} catch (JMSException e) {
			throw new SystemException(
					FrameworkError.JMS_RECEIVE_ERROR, e, "Error working with JMS correlation id/message id");
		}

		return messagesOut;
	}

	/**
	 * Create the responses from the retrieved messages.
	 *
	 * @param messages the messages to convert to responses.
	 * @return the responses created from the messages.
	 * @throws ServiceFailedException if handling of messages failed.
	 */
	private ServiceResponse[] createResponses(final Message[] messages) throws ServiceFailedException {
		final ServiceResponse[] responses = new ServiceResponse[messages.length];

		for (int idx = 0; idx < messages.length; idx++) {
			responses[idx] = messageHandler.handleMessage(messages[idx]);
		}

		if (log.isDebugEnabled()) {
			for (int idx = 0; idx < messages.length; idx++) {
				log.debug("Converted JMS message to response: " + responses[idx]);
			}
		}

		return responses;
	}

	/**
	 * Creates the messages to send.
	 *
	 * @param request the service request holding the values to send.
	 * @param session the <code>QueueSession</code> to use.
	 * @param retQueue the return <code>Queue</code> to use.
	 * @return the messages to send.
	 * @throws ServiceFailedException if formatting of the messages failed.
	 * @see #createMessage(QueueSession, ServiceRequest, Destination)
	 * @see QueueSession
	 * @see Queue
	 */
	private Message[] createMessages(final ServiceRequest request, final QueueSession session, final Queue retQueue)
		throws ServiceFailedException {
		final ServiceRequest[] requests = retrieveRequests(request);
		final Message[] messages = new Message[requests.length];

		for (int idx = 0; idx < requests.length; idx++) {
			messages[idx] = createMessage(session, requests[idx], retQueue);
		}

		if (log.isDebugEnabled()) {
			for (int idx = 0; idx < requests.length; idx++) {
				log.debug("Received request: " + requests[idx]);
				log.debug("Converted request to JMS message: " + messages[idx]);
			}
		}

		return messages;
	}

	/**
	 * Retrieves an array of requests from the request. If the request does not have an array of requests, create an array of
	 * requests of length 1 that includes the request itself.
	 *
	 * @param request the request to retreive the array of requests from.
	 * @return an array of <code>ServiceRequest</code>.
	 * @see ServiceRequest
	 */
	private ServiceRequest[] retrieveRequests(final ServiceRequest request) {
		ServiceRequest[] requests = (ServiceRequest[]) request.getData(Constants.JMSSERVICE_MULTIPLE_MESSAGES);

		if (null == requests) {
			requests = new ServiceRequest[] { request };
		}

		return requests;
	}

	private boolean hasMultiple(final ServiceRequest request) {
		return null != request.getData(Constants.JMSSERVICE_MULTIPLE_MESSAGES);
	}

	/**
	 * Creates the message to be sent and populates it with data and return queue values.
	 * 
	 * @param session the QueueSession object
	 * @param request the request to be sent.
	 * @param retQueue
	 * @throws ServiceFailedException application error during formatting of message.
	 * @return the message to be sent.
	 */
	private Message createMessage(final QueueSession session, final ServiceRequest request, final Destination retQueue)
		throws ServiceFailedException {
		final Message msg = messageFormatter.formatMessage(session, request);

		if (specifyReturnQueue) {
			try {
				msg.setJMSReplyTo(retQueue);
			} catch (JMSException e) {
				throw new SystemException(
					FrameworkError.JMS_INVALID_RETURN_QUEUE,
					e,
					"Error occurred while setting the return queue on the message.");
			}
		}

		return msg;

	}

	/**
	 * Create a temp queue or used the predefined queue as specified in setup.
	 *
	 * @param session the <code>QueueSession</code> to use when creating the queue.
	 * @return the return queue to use.
	 */
	private Queue createReturnQueue(final QueueSession session) {
		Queue retQueue = null;

		if (specifyReturnQueue) {
			// we need to set which return queue to use
			if (useTempQueue) {
				retQueue = createTempQueue(session);
			} else {
				// use the predefined queue
				retQueue = returnQueue;
			}
		}

		return retQueue;
	}

	/**
	 * Create a temporary return queue which will live for this request only.
	 * Since this sometimes fails due to connection trouble a retry count is implemented
	 * 
	 * @param session Queue session
	 * @return Queue
	 */
	private Queue createTempQueue(final QueueSession session) {
		int noOfFailedAttempts = 0;
		JMSException originalException = null;
		Queue retQ = null;

		while (CREATE_TEMP_QUEUE_RETRY_COUNT >= noOfFailedAttempts && null == retQ) {
			try {
				retQ = session.createTemporaryQueue();
			} catch (JMSException e) {
				noOfFailedAttempts++;
				originalException = e;
				log.warn(
					"Attempt to create temporary queue failed, retrying up to "
						+ CREATE_TEMP_QUEUE_RETRY_COUNT
						+ " times. Exception attached.",
					e);
			}
		}

		if (CREATE_TEMP_QUEUE_RETRY_COUNT < noOfFailedAttempts) {
			throw new SystemException(
				FrameworkError.JMS_ERROR_CREATE_TEMP_QUEUE,
				originalException,
				"Error occurred while creating a temporary queue for message return.");
		}

		return retQ;
	}

	/**
	 * Sends the given messages on the given queue.
	 * 
	 * @param session the queue session.
	 * @param msgs the messages to send.
	 */
	protected void sendMessages(final QueueSession session, final Message[] msgs) {
		QueueSender sender = null;
		// TODOS maybe group messages using the message properties JMSXGroupID (String) & JMSXGroupSeq (int), see "Message Groups rocks" on ActiveMQ site

		try {
			sender = session.createSender(queue);
		} catch (JMSException e) {
			throw new SystemException(FrameworkError.JMS_SENDER_CREATION_ERROR, e);
		}

		try {
			for (int idx = 0; idx < msgs.length; idx++) {
				sender.send(msgs[idx], Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY, messageTtl);
			}
		} catch (JMSException e) {
			throw new SystemException(FrameworkError.JMS_SEND_ERROR, e);
		}
	}

	/**
	 * @param conn the queue connection.
	 * @param session the queue session.
	 * @param queue
	 * @param msgCount
	 * @return the response from the system or an empty array if asynchronous operation.
	 */
	private Message[] receiveMessages(
		final QueueConnection conn,
		final QueueSession session,
		final Queue queue,
		final int msgCount) {
		// don't bother if we have asynch operation.
		if (!isSynchronous) {
			// so no NullPointerException will happen later on
			return new Message[] {};
		}

		final QueueReceiver receiver = createReceiver(session, queue);

		// start receiving message
		try {
			conn.start();
		} catch (JMSException e) {
			throw new SystemException(
				FrameworkError.JMS_RECEIVE_ERROR, e, "Error when starting reception on the JMS connection.");
		}

		final Message[] returnMessages = new Message[msgCount];

		for (int idx = 0; idx < returnMessages.length; idx++) {
			returnMessages[idx] = receiveMessage(receiver);
		}

		return returnMessages;
	}

	private Message receiveMessage(final QueueReceiver receiver) {
		try {
			PerformanceMonitor.start(RECEIVE_MONITOR_KEY, queueJndi);
			final Message returnMessage;

			if (0L >= timeout) {
				// expect the message to be available immediately
				returnMessage = receiver.receiveNoWait();
			} else {
				// wait for the message to arrive for <code>timeout</code> milliseconds.
				returnMessage = receiver.receive(timeout);
			}

			if (log.isDebugEnabled()) {
				log.debug("message is received: " + returnMessage);
			}

			// Throw exception if there was no message
			if (null == returnMessage) {
				throw new SystemException(FrameworkError.JMS_RECEIVE_ERROR, "Message sent, but not received.");
			}

			PerformanceMonitor.end(RECEIVE_MONITOR_KEY);

			return returnMessage;
		} catch (JMSException e) {
			PerformanceMonitor.fail(RECEIVE_MONITOR_KEY);
			throw new SystemException(FrameworkError.JMS_RECEIVE_ERROR, e, "Error while receiving message.");
		}
	}

	/**
	 * Helper method for creating a <code>QueueReceiver</code>. It will be created with a message selector if one is defined
	 * and the queue is not a temporary queue.
	 *
	 * @param session <code>Session</code> used to create receiver.
	 * @param queue <code>Queue</code> used to create receiver.
	 * @return a <code>QueueReceiver</code>.
	 */
	private QueueReceiver createReceiver(final QueueSession session, final Queue queue) {
		QueueReceiver receiver;

		try {
			if (useTempQueue || null == messageSelector) {
				receiver = session.createReceiver(queue);
			} else {
				receiver = session.createReceiver(queue, messageSelector);
			}
		} catch (JMSException e) {
			throw new SystemException(FrameworkError.JMS_RECEIVER_CREATION_ERROR, e, "Error creating a JMS receiver.");
		}

		return receiver;
	}

	/**
	 * Sets wether to use synchronous or asynchronous communication. Default is set to false.
	 *  
	 * @param useSynchronous sets wether or not to use synchrounous communication. 
	 */
	public final void setIsSynchronous(final boolean useSynchronous) {
		isSynchronous = useSynchronous;
	}

	/**
	 * Sets the JNDI name of the queue to use for sending messages. This property is required.
	 * 
	 * @param queueJndiName the JNDI name of the send queue.
	 */
	public final void setQueueJndi(final String queueJndiName) {
		queueJndi = queueJndiName;
	}

	/**
	 * Sets the JNDI name of the queue connection factory. This property is required.
	 * 
	 * @param qcfJndiName the JNDI name of the queue connection factory.
	 */
	public final void setQueueConnectionFactoryJndi(final String qcfJndiName) {
		queueConnectionFactoryJndi = qcfJndiName;
	}

	/**
	 * Sets the JNDI name of the return queue to use. This is only relevant if the service
	 * is configured not to use a temporary return queue. This property is optional.
	 * 
	 * @param returnQueueJndi the JNDI name of the return queue.
	 */
	public final void setReturnQueueJndi(final String returnQueueJndi) {
		this.returnQueueJndi = returnQueueJndi;
	}

	/**
	 * Sets wether or not the message being sent should contain a return queue. Default is set
	 * to false. This requires that the return queue name is set or that the services is configured 
	 * to use a temporary queue.
	 * 
	 * @param setReturnQueue wether or not to set the return queue on the message.
	 */
	public final void setSpecifyReturnQueue(final boolean setReturnQueue) {
		specifyReturnQueue = setReturnQueue;
	}

	/**
	 * Sets the timeout in milliseconds for how long to wait for a response when synchronous 
	 * communication is being used. Default is set to to -1. Valid values are:
	 * <ul>
	 * 	<li>-1 Expect the result immediately.</li>
	 * 	<li>0  Wait until a message is available (potentially indefinitely).</li>
	 * 	<li>&gt;0 Wait the specified number of milliseconds.</li>
	 * </ul> 
	 *  
	 * @param timeout the timeout value in milliseconds.
	 */
	public final void setTimeout(final long timeout) {
		this.timeout = timeout;
	}

	/**
	 * Sets wether or not the return queue should be a temporary queue. Default is false. This
	 * setting is ignored if the service is not configured to specify the return queue.
	 * 
	 * @param useTempQueue wether or not to use a temp queue for message return. 
	 */
	public final void setUseTempQueue(final boolean useTempQueue) {
		this.useTempQueue = useTempQueue;
	}

	/**
	 * Sets the message handler to use for processing a received message. This property is optional.
	 * 
	 * @param handler the handler instance.
	 */
	public final void setMessageHandler(final MessageHandler handler) {
		messageHandler = handler;
	}

	/**
	 * Sets the message formatter to use for prepare messages for seding. This property is optional.
	 * 
	 * @param formatter the formatter instance.
	 */
	public final void setMessageFormatter(final MessageFormatter formatter) {
		messageFormatter = formatter;
	}

	/**
	 * Sets the JNDIHelper to use for lookups of queues, connection factories, etc. This property is required.
	 * 
	 * @param helper the JNDIHelper.
	 */
	public final void setLookupHelper(final LookupHelper helper) {
		lookupHelper = helper;
	}

	/**
	 * Sets the time to live for the JMS messages being sent from this service.
	 * The default value i 0(zero), meaning the message does not expire.
	 * @param ttl the time to live in milliseconds before the message expires.
	 */
	public final void setMessageTtl(final long ttl) {
		messageTtl = ttl;
	}

	/**
	 * Sets the message selector to use when working in synchronous mode. 
	 * Using a message selector enables a receiver to read messages from a queue
	 * which has many receivers by filtering on a property in the header.
	 * 
	 * @param string the message selector
	 * @see <a href="http://hpapt03/j2ee131/api/javax/jms/Message.html">javax.jms.Message</a>
	 */
	public final void setMessageSelector(final String string) {
		messageSelector = string;
	}

}
