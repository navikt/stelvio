package no.stelvio.common.error.logging;

import static org.junit.Assert.assertTrue;

import no.stelvio.common.error.support.Severity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test class for ExceptionLoggerInterceptor.
 * 
 * @author MA
 * 
 */
public class ExceptionLoggerInterceptorTest {

	private ApplicationContext ctx = null;
	private ExceptionThrower thrower = null;
	private Log4jTestAppender appender;

	private final String requestContextProperties = 
		"UserId=user1, ComponentId=component1, ScreenId=screen1, TransactionId=trans1.";

	/**
	 * Set up environment before test.
	 */
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("test-exceptionlogger-context.xml");
		thrower = (ExceptionThrower) ctx.getBean("exceptionThrower");
		appender = new Log4jTestAppender();
		Logger log = LogManager.getLogger(ExceptionLogger.class);
		log.removeAllAppenders(); // Remove
		log.addAppender(appender);

	}

	/**
	 * Test intercepts and logs.
	 */
	@Test
	public void interceptsAndLogs() {
		appender.addMessageToVerify("Message: 'Test'.", Severity.WARN);
		try {
			thrower.throwException(new Exception("Test"));
		} catch (Exception e) {
			// Do nothing
		}
		appender.printResults();
		assertTrue(appender.getVerificationResults(), appender.verify());
	}

	/**
	 * Clean after testing.
	 */
	@After
	public void tearDown() {
		ctx = null;
		thrower = null;
		appender = null;
	}

}
