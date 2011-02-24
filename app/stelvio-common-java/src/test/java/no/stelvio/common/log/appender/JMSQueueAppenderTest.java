package no.stelvio.common.log.appender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JMSQueueAppender unit test.
 * 
 * @author person356941106810, Accenture
 * @version $Id: JMSQueueAppenderTest.java 2193 2005-04-06 08:01:35Z psa2920 $
 */
public class JMSQueueAppenderTest {

	private JMSQueueAppender appender = new JMSQueueAppender();
	private TestErrorhandler handler = new TestErrorhandler();
	private String oldFac;
	private TestInitialContext ic;

	/**
	 * Set up.
	 * 
	 * @throws NamingException
	 *             naming exception if getInitialContext fails
	 */
	@Before
	public void setUp() throws NamingException {
		ic = (TestInitialContext) new TestInitialContextFactory().getInitialContext(null);
		oldFac = System.getProperty(Context.INITIAL_CONTEXT_FACTORY);

		handler.setNumErrors(0);
		appender.setErrorHandler(handler);
		appender.setName("TestJMSQueueAppender");
	}

	/**
	 * Clean up.
	 */
	@After
	public void tearDown() {
		if (oldFac != null) {
			System.setProperty(Context.INITIAL_CONTEXT_FACTORY, oldFac);
		}
	}

	/**
	 * Test activate options.
	 */
	@Test
	public void activateOptions() {
		// test: validation when JNDI names are not set
		appender.activateOptions();
		assertEquals("Test 1: Unexpected number of errors", 1, handler.getNumErrors());
		assertEquals("Test 1: Unexpected error message",
				"The binding name for a QueueConnectionFactory is missing for appender [TestJMSQueueAppender].", handler
						.getTestErrorString());
		handler.setNumErrors(0);
		appender.setQueueConnectionFactoryBindingName("jms/qcf");
		assertEquals("Test 1.1: unexpected qcf binding name", "jms/qcf", appender.getQueueConnectionFactoryBindingName());
		appender.activateOptions();
		assertEquals("Test 2: Unexpected number of errors", 1, handler.getNumErrors());
		assertEquals("Test 2: Unexpected error message",
				"The binding name for a Queue is missing for appender [TestJMSQueueAppender].", handler.getTestErrorString());
		handler.setNumErrors(0);
		appender.setQueueBindingName("jms/queue");
		assertEquals("Test 2.1: unexpected queue binding name", "jms/queue", appender.getQueueBindingName());

		// test: Nothing is not configured. Error should occur
		// Since the InitialContext is not configured, we really don't know what will happen,
		// but we DO know that we will get an error of some sort
		TestInitialContextFactory.setErrorOnCreate(true);
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, TestInitialContextFactory.class.getName());
		System.setProperty(Context.PROVIDER_URL, "iiop://test:999");
		appender.activateOptions();
		assertEquals("Test 3: Unexpected number of errors", 1, handler.getNumErrors());
		assertEquals("Test 3: Unexpected error message",
				"Error while initializing JNDI context for appender named [TestJMSQueueAppender].", handler
						.getTestErrorString());
		handler.setNumErrors(0);
		TestInitialContextFactory.setErrorOnCreate(false);

		// test: No options are set. We should get a number of errors
		ic.setErrorOnLookup(true);
		appender.setInitialContextFactoryName("no.stelvio.common.log.appender.TestInitialContextFactory");
		appender.setProviderURL("iiop://test:999");
		appender.activateOptions();
		assertEquals("Test 4: Unexpected number of errors", 1, handler.getNumErrors());
		assertEquals(
				"Test 4: Unexpected error message",
				"Error while looking up QueueConnectionFactory named [jms/qcf] or Queue named [jms/queue] for appender named [TestJMSQueueAppender].",
				handler.getTestErrorString());
		handler.setNumErrors(0);
		ic.setErrorOnLookup(false);

		// test: error during creation of QueueConnection
		TestQueueConnectionFactory testFac = new TestQueueConnectionFactory();
		testFac.setErrorOnCreate(true);
		ic.setQcf(testFac);
		ic.setQ(new Queue() {
			public String getQueueName() {
				return "Q";
			}
		});
		appender.activateOptions();
		assertEquals("Test 5 : Unexpected number of errors", 1, handler.getNumErrors());
		assertEquals("Test 5: Unexpected error message",
				"Error while creating queue connection for appender named [TestJMSQueueAppender].", handler
						.getTestErrorString());
		handler.setNumErrors(0);
		testFac.setErrorOnCreate(false);

		// test: error during creation of QueueSession
		TestQueueConnection conn = new TestQueueConnection();
		conn.setErrorOnCreate(true);
		testFac.setConn(conn);
		appender.activateOptions();
		assertEquals("Test 6: Unexpected number of errors", 1, handler.getNumErrors());
		assertEquals("Test 6: Unexpected error message",
				"Error while creating queue session for appender named [TestJMSQueueAppender].", handler.getTestErrorString());
		handler.setNumErrors(0);
		conn.setErrorOnCreate(false);

		// test: error during creation of QueueSender
		TestQueueSession sess = new TestQueueSession();
		conn.setSession(sess);
		sess.setErrorOnCreate(true);
		appender.activateOptions();
		assertEquals("Test 7: Unexpected number of errors", 1, handler.getNumErrors());
		assertEquals("Test 7: Unexpected error message",
				"Error while creating queue sender for appender named [TestJMSQueueAppender].", handler.getTestErrorString());
		handler.setNumErrors(0);
		sess.setErrorOnCreate(false);

		// test: error during connection.start
		sess.setSender(new TestQueueSender());
		conn.setErrorOnStart(true);
		appender.activateOptions();
		assertEquals("Test 8: Unexpected number of errors", 1, handler.getNumErrors());
		assertEquals("Test 8: Unexpected error message",
				"Error while starting queue connection for appender named [TestJMSQueueAppender].", handler
						.getTestErrorString());
		handler.setNumErrors(0);
		conn.setErrorOnStart(false);

		// test: error during InitialContext.close
		ic.setErrorOnClose(true);
		appender.activateOptions();
		assertEquals("Test 9: Unexpected number of errors", 1, handler.getNumErrors());
		assertEquals("Test 9: Unexpected error message",
				"Error while closing JNDI context for appender named [TestJMSQueueAppender].", handler.getTestErrorString());
		handler.setNumErrors(0);
		ic.setErrorOnClose(false);

		// test: clean run
		appender.activateOptions();
		assertEquals("Test 10: Unexpected number of errors", 0, handler.getNumErrors());
	}

	/**
	 * Test append logging event.
	 * 
	 * @throws JMSException
	 *             exception
	 */
	@Test
	public void appendLoggingEvent() throws JMSException {
		// test: entry conditions are not met. Error should occur
		appender.append(null);
		assertEquals("Test 1: Unexpected number of errors.", 1, handler.getNumErrors());
		assertEquals("Test 1: Unexptected error message",
				"No QueueConnection for JMSQueueAppender named [TestJMSQueueAppender].", handler.getTestErrorString());
		handler.setNumErrors(0);

		// test: entry conditions are met, error occurs during sending of message
		// set up everything needed for activateOptions() to succeed
		appender.setLayout(new PatternLayout("%p - %m "));
		appender.setQueueConnectionFactoryBindingName("jms/qcf");
		appender.setQueueBindingName("jms/queue");
		appender.setInitialContextFactoryName("no.stelvio.common.log.appender.TestInitialContextFactory");
		appender.setProviderURL("iiop://test:999");
		TestQueueConnectionFactory testFac = new TestQueueConnectionFactory();
		ic.setQcf(testFac);
		ic.setQ(new Queue() {
			public String getQueueName() {
				return "Q";
			}
		});
		TestQueueConnection conn = new TestQueueConnection();
		testFac.setConn(conn);
		TestQueueSession sess = new TestQueueSession();
		conn.setSession(sess);
		TestQueueSender sender = new TestQueueSender();
		sess.setSender(sender);
		sender.setErrorOnOperation(true);
		LoggingEvent event = new LoggingEvent("testCat", new Category("TestCat") {
		}, Priority.ERROR, "Test message", null);
		appender.activateOptions();
		// the actual method to test
		appender.append(event);

		assertEquals("Test 2: Unexpected number of errors.", 1, handler.getNumErrors());
		assertEquals("Test 2: Unexptected error message", "Could not publish message in JMSAppender [TestJMSQueueAppender].",
				handler.getTestErrorString());
		handler.setNumErrors(0);

		// test: message on queue is correct
		sender.setErrorOnOperation(false);
		appender.append(event);
		assertEquals("Test 2: Unexpected number of errors.", 0, handler.getNumErrors());
		TextMessage msg = (TextMessage) sender.getMsg();
		assertEquals("Test 2: Unexpected logging format", "ERROR - Test message ", msg.getText());

		// test: logging event contains exception, message on queue is correct
		event = new LoggingEvent("testCat", new Category("TestCat") {
		}, Priority.ERROR, "Test message", new Exception("Test"));
		appender.append(event);
		assertEquals("Test 3: Unexpected number of errors.", 0, handler.getNumErrors());
		msg = (TextMessage) sender.getMsg();
		assertTrue("Test 3: Unexpected logging format", msg.getText().startsWith(
				"ERROR - Test message :java.lang.Exception: Test"));
	}

}
