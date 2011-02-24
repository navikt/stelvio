package no.stelvio.common.log.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Unit test for {@link Log4jLogFactory}.
 * 
 * @author person66cdf88a8f67, Accenture
 * @version $Id: $
 */
public class Log4jLogFactoryTest {
	private Log4jLogFactory logFactory;

	/**
	 * Test loggerInstancesForAClassIsNotEqualWithAReleaseInBetween.
	 */
	@Test
	public void loggerInstancesForAClassIsNotEqualWithAReleaseInBetween() {
		Log log1 = logFactory.getInstance(getClass());
		logFactory.release();
		Log log2 = logFactory.getInstance(getClass());

		assertThat(log1, is(not(sameInstance(log2))));
	}

	/**
	 * Test retrievingLogWithClassMultipleTimesShouldBeSameAndCorrectType.
	 */
	@Test
	public void retrievingLogWithClassMultipleTimesShouldBeSameAndCorrectType() {
		Log log1 = logFactory.getInstance(getClass());
		assertThat(log1, is(instanceOfLog4jLogger()));

		Log log2 = logFactory.getInstance(getClass());
		assertThat(log1, is(sameInstance(log2)));
	}

	/**
	 * Test retrievingLogWithClassNameMultipleTimesShouldBeSameAndCorrectType.
	 */
	@Test
	public void retrievingLogWithClassNameMultipleTimesShouldBeSameAndCorrectType() {
		String className = getClass().getName();

		Log log1 = logFactory.getInstance(className);
		assertThat(log1, is(instanceOfLog4jLogger()));

		Log log2 = logFactory.getInstance(className);
		assertThat(log1, is(sameInstance(log2)));
	}

	/**
	 * Test retrievingLogWithClassAndClassNameMultipleTimesShouldBeSame.
	 */
	@Test
	public void retrievingLogWithClassAndClassNameMultipleTimesShouldBeSame() {
		Log log1 = logFactory.getInstance(getClass());
		Log log2 = logFactory.getInstance(getClass().getName());

		assertThat(log1, is(sameInstance(log2)));
	}

	/**
	 * Test attributeIsSetAndRemovedAfterRemoveAttributeCall.
	 */
	@Test
	public void attributeIsSetAndRemovedAfterRemoveAttributeCall() {
		logFactory.setAttribute("TEST", "TESTVALUE");
		String attribute1 = (String) logFactory.getAttribute("TEST");
		assertThat(attribute1, is(equalTo("TESTVALUE")));

		logFactory.removeAttribute("TEST");
		String attribute2 = (String) logFactory.getAttribute("TEST");
		assertThat(attribute2, is(nullValue()));
	}

	/**
	 * Test refresh.
	 */
	@Test
	@Ignore("Should be looked into")
	public void testRefresh() {
		Properties prop = new Properties();
		URL url = Thread.currentThread().getContextClassLoader().getResource(System.getProperty("log4j.configuration"));
		try {
			// reset the file
			FileOutputStream out = new FileOutputStream(url.getFile(), false);
			prop.setProperty("log4j.logger.no.stelvio", "ERROR, Console, TEST");
			prop.store(out, null);
			out.close();
			LogFactory fac = LogFactory.getFactory();

			// set to 5 second wait
			fac.setAttribute(Log4jLogFactory.REFRESH_INTERVAL, "5000");
			Thread.sleep(6000);
			Log log1 = fac.getInstance(this.getClass());
			Assert.assertFalse("Test 1: Debug is enabled, please reset prop. file", log1.isDebugEnabled());

			InputStream in = url.openStream();
			prop.load(in);
			in.close();

			// assert that we are in a valid state
			Assert.assertEquals("Test 2: Unexpected LOGGER configuration", "ERROR, Console, TEST", prop
					.getProperty("log4j.logger.no.stelvio"));
			// set the level to DEBUG
			prop.setProperty("log4j.logger.no.stelvio", "DEBUG, Console, TEST");

			out = new FileOutputStream(url.getFile(), false);
			prop.store(out, null);
			out.close();
			// sleep for 6 seconds to make sure the changes take effect
			Thread.sleep(6000);
			Assert.assertTrue("Test 3: Debug logging is not enabled", log1.isDebugEnabled());

		} catch (Exception e) {
			e.printStackTrace(System.err);
			Assert.fail("Unexpected exception:" + e.getMessage());
		}
	}

	/**
	 * Setup before test.
	 */
	@Before
	public void setUpLogFactoryToUse() {
		// System.setProperty("org.apache.commons.logging.LogFactory", "no.stelvio.common.log.factory.Log4jLogFactory");
		// System.setProperty(LogFactory.DIAGNOSTICS_DEST_PROPERTY, "STDOUT");
		logFactory = new Log4jLogFactory();
	}

	/**
	 * Cleanup after test.
	 */
	@After
	public void resetLogFactoryAfterUse() {
		System.setProperty("org.apache.commons.logging.LogFactory", "");
		System.setProperty(LogFactory.DIAGNOSTICS_DEST_PROPERTY, "");
	}

	/**
	 * Create new IsInstanceofLog4JLogger.
	 * 
	 * @return logger
	 */
	private static IsInstanceofLog4JLogger instanceOfLog4jLogger() {
		return new IsInstanceofLog4JLogger();
	}

	/**
	 * IsInstanceofLog4JLogger.
	 */
	private static class IsInstanceofLog4JLogger extends BaseMatcher<Log> {

		/**
		 * Return true if o matches Log4JLogger.
		 * 
		 * @param o
		 *            to be checked
		 * @return true if o is a Log4JLogger
		 */
		public boolean matches(Object o) {
			return o instanceof Log4JLogger;
		}

		/**
		 * Describe to description.
		 * 
		 * @param description
		 *            description
		 */
		public void describeTo(Description description) {
			description.appendText("an instance of ").appendText(Log4JLogger.class.getName());
		}
	}
}
