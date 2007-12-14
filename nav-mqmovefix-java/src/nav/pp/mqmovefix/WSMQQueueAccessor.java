package nav.pp.mqmovefix;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;

import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQConnection;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueue;

public class WSMQQueueAccessor {

	private String fName;

	private String fQmName, fQmHost, fChannelName, fQueueName;

	private int fQmPort;

	private MQConnectionFactory fConnFact = null;

	/**
	 * Create instance with given parameters
	 * 
	 * @param name
	 * @param qmName
	 * @param qmHost
	 * @param qmPort
	 * @param channelName
	 * @param queueName
	 */
	public WSMQQueueAccessor(String name, String qmName, String qmHost,
			int qmPort, String channelName, String queueName) {
		this.fName = name;
		this.fQmName = qmName;
		this.fQmHost = qmHost;
		this.fQmPort = qmPort;
		this.fChannelName = channelName;
		this.fQueueName = queueName;
	}

	/**
	 * @return connection
	 */
	public Connection getConnection() throws JMSException {

		Connection conn = null;
		try {
			initConnectionFactory();
			conn = fConnFact.createConnection();
			((MQConnection) conn).setEoqTimeout(10);
			return conn;
		} catch (JMSException e) {
			System.err.println("Exception connecting to MQ queue: " + e);
		}
		return conn;
	}

	/**
	 * @return destination
	 */
	public Destination getDestination() throws JMSException {
		MQQueue queue = new MQQueue(fQueueName);
		// queue.setTargetClient(JMSC.MQJMS_CLIENT_NONJMS_MQ);
		return queue;
	}

	/**
	 * Navn
	 */
	public String getName() {
		return fName;
	}

	/**
	 * Initialise connection pool
	 * 
	 * @throws JMSException
	 */
	private void initConnectionFactory() throws JMSException {
		fConnFact = new MQConnectionFactory();
		fConnFact.setQueueManager(fQmName);
		fConnFact.setHostName(fQmHost);
		fConnFact.setPort(fQmPort);
		fConnFact.setChannel(fChannelName);
		fConnFact.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP); // Use
		// TCPIP,
		// not
		// default
		// binding
		// mode
		fConnFact.setUseConnectionPooling(true);
	}
}
