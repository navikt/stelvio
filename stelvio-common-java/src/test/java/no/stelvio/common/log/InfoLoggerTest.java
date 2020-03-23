package no.stelvio.common.log;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests info logger component.
 * <p>
 * Tests:
 * </p>
 * <ul>
 * <li>The log methods for the different log-levels.
 * <li>If loglevels are enabled (Requires the default info logger to be
 * configured with TRACE/DEBUG loglevel, and a test logger that should be
 * configured with INFO loglevel.
 * </ul>
 * 
 * @author person15754a4522e7
 */
public class InfoLoggerTest {

	private ApplicationContext ctx;
	private InfoLogger defaultLogger;
	private InfoLogger testLogger;

	/**
	 * Setup default logger and test logger using Spring test config file.
	 *
     */
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("tst-log-infologger-context.xml");

		//Loggers: default->TRACE, test->INFO
		this.defaultLogger = (InfoLogger) ctx.getBean("tst.log.defaultInfoLogger");
		this.testLogger = (InfoLogger) ctx.getBean("tst.log.testInfoLogger");
	}

	/**
	 * Reset test loggers.
	 */
	@After
	public void tearDown() {
		ctx = null;
		this.defaultLogger = null;
		this.testLogger = null;
	}

	/**
	 * Test debug log-method and isEnabled.
	 */
	@Test
	public void testDebug() {
		if (testLogger.isDebugEnabled()) {
			fail("Error in log configuration. Debug should not be enabled for test logger.");
		}

		if (defaultLogger.isDebugEnabled()) {
			defaultLogger.debug("Debug message");
			defaultLogger.debug("Debug message", "Arg1", 2, new Date());
		} else {
			fail("Error in log configuration. Debug should be enabled.");
		}
	}

	/**
	 * Test info log-method and isEnabled.
	 */
	@Test
	public void testInfo() {
		if (!testLogger.isInfoEnabled()) {
			fail("Error in log configuration. Info should be enabled for test logger.");
		}

		testLogger.info("Info message");
		testLogger.info("Info message", "Arg1", 2, new Date());
	}

	/**
	 * Test trace log-method and isEnabled.
	 */
	@Test
	public void testTrace() {
		if (testLogger.isTraceEnabled()) {
			fail("Error in log configuration. Trace should not be enabled for test logger.");
		}

		// Log4j has no trace-level, trace is logged as debug, so this should
		// work.
		if (defaultLogger.isTraceEnabled()) {
			defaultLogger.trace("Trace message");
			defaultLogger.trace("Trace message", "Arg1", 2, new Date());
		} else {
			fail("Error in log configuration. Trace should be enabled.");
		}
	}

}
