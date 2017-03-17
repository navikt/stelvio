package no.stelvio.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.domain.BatchParameter;
import no.stelvio.batch.exception.NullBatchException;
import no.stelvio.common.config.MissingPropertyException;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.error.SystemUnrecoverableException;
import no.stelvio.domain.time.ChangeStamp;

/**
 * Super class for unit tests of batch modules.
 * <p/>
 * Has tests that all batch modules should pass in addidtion to helper methods for setting up state before running a test.
 * 
 * @author person356941106810, Accenture
 * @author personf8e9850ed756, Accenture
 * @author person983601e0e117, Accenture
 * @version $Id: AbstractBatchTest.java 1955 2005-02-08 15:43:02Z psa2920 $
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BatchConfiguration.class)
public class AbstractBatchTest {
	private AbstractBatch testClass;

	private int desiredStatus = BatchStatus.BATCH_OK;

	@Autowired
	private AbstractBatch progressReportingAndExceptionThrowingBatch;

	private String userId = "xxx9999";
	
	/**
	 * Testing if batch has no parameters.
	 */
	@Test
	public void batchHasNoParameters() {
		BatchDO b = new BatchDO();
		b.setChangeStamp(new ChangeStamp(userId));
		b.setParameters(null);
		Properties props = testClass.getBatchProperties(b);

		assertNull("Props were not null", props);
	}

	/**
	 * Testing exception handling capabilities.
	 */
	@Test
	public void testExceptionHandlingCapabilities() {
		int result = progressReportingAndExceptionThrowingBatch.executeBatch(0);
		assertTrue(result == 0);
	}

	/**
	 * Testing if exception is thrown when batch properties is null.
	 */
	@Test(expected = NullBatchException.class)
	public void exceptionThrownWhenBatchPropertiesIsNull() {
		testClass.getBatchProperties(null);
	}

	/**
	 * Testing batch properties in unexpected format.
	 */
	@Test
	public void batchPropertiesInUnexpectedFormat() {
		BatchDO batch = new BatchDO(); // testClass.readBatchParameters("testBatch", 1);
		batch.setChangeStamp(new ChangeStamp(userId));
		batch.setBatchname("Test");
		Properties props;

		batch.setParameters("xyz");
		props = testClass.getBatchProperties(batch);
		assertEquals("Unexpected value", "", props.getProperty("xyz"));

		batch.setParameters("=xxx");
		props = testClass.getBatchProperties(batch);
		assertEquals("Unexpected value", "xxx", props.getProperty(""));
	}

	/**
	 * Testing set null as batch properties.
	 */
	@Test
	public void setNullBatchProperties() {
		BatchDO batch = new BatchDO(); // testClass.readBatchParameters("testBatch", 1);
		batch.setChangeStamp(new ChangeStamp(userId));
		batch.setBatchname("testBatch");
		batch.setParameters("x=y");
		testClass.setBatchProperties(batch, null);

		assertNull("The props should have been null", batch.getParameters());
	}

	/**
	 * Testing set null as batch in set batch properties.
	 */
	@Test(expected = SystemUnrecoverableException.class)
	public void settingNullBatchThrowsException() {
		testClass.setBatchProperties(null, new Properties());
	}

	/**
	 * Testing set batch properties, normal case.
	 */
	@Test
	public void setBatchPropertiesOk() {
		BatchDO batch = new BatchDO();
		batch.setChangeStamp(new ChangeStamp(userId));
		Properties props = new Properties();
		props.setProperty("A", "B");
		props.setProperty("C", "D");

		testClass.setBatchProperties(batch, props);

		assertNotNull("Props should not be null", batch.getParameters());
		assertTrue("Unexpected properties format", ("A=B;C=D".equals(batch.getParameters()) 
				|| "C=D;A=B".equals(batch.getParameters())));
	}

	/**
	 * Testing report progress.
	 */
	@Test
	public void reportProgress() {
		int batchStatus = progressReportingAndExceptionThrowingBatch.executeBatch(0);
		assertTrue(batchStatus == 0);
	}

	/**
	 * Testing get parameters.
	 */
	@Test
	public void testGetParameter() {
		BatchDO batch = new BatchDO();
		batch.setChangeStamp(new ChangeStamp(userId));
		batch.setBatchname("Batch");
		batch.setParameters("test1=123;workUnit=100;test2=321");
		String workUnit = testClass.getParameter(batch, BatchParameter.WORK_UNIT);
		assertEquals(workUnit, "100");
	}

	/**
	 * Testing performSanityCheck with no property set.
	 */
	@Test
	public void testPerformSanityCheckNoPropertiesSet() {
		try {
			testClass.performSanityCheck();
		} catch (MissingPropertyException mpe) {
			assertTrue(true);
		}
	}

	/**
	 * Testing performSanityCheck with no exception handler set.
	 */
	@Test
	public void testPerformSanityCheckNoExceptionHandlerSet() {
		try {
			testClass.performSanityCheck();
		} catch (MissingPropertyException mpe) {
			assertTrue(true);
		}
	}

	/**
	 * Setup for the tests.
	 */
	@Before
	public void setUp() {
		RequestContextSetter.setRequestContextForUnitTest();
		// create an implementation of the batch which we can use to test
		testClass = new AbstractBatch() {
			public int executeBatch(int slice) {
				return desiredStatus;
			}

			@Override
			protected void flushUpdates() {

			}
		};
		testClass = progressReportingAndExceptionThrowingBatch;
	}
}
