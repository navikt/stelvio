package no.stelvio.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import no.stelvio.batch.controller.BatchControllerServiceBi;
import no.stelvio.batch.exception.InvalidBatchEntryException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests that a batch can started and run successfully. Additionally verifies that only one unique instance of a batch can be
 * running at a given time (i.e name and slice makes a batch unique).
 * 
 * @author person08f1a7c6db2c
 */
public class StartBatchTest {

	private ApplicationContext ctx;
	private BatchControllerServiceBi batchController;
	private String infiniteBatchName = "stopTest";
	private String successfulBatchName = "startTest";
	private int slice = 0;

	/**
	 * Set up for the tests.
	 */
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("tst-stopbatch-context.xml");
		batchController = (BatchControllerServiceBi) ctx.getBean("batchService");
	}

	/**
	 * Tear down after the tests.
	 */
	@After
	public void tearDown() {
		batchController.stopBatch("stopTest", slice);
		ctx = null;
		batchController = null;
	}

	/**
	 * Verfify that batches completes.
	 */
	@Test
	public void verifyBatchCompletes() {
		int returnCode = startSuccessfulBatch(); // Want exception, thus start second batch in this thread.
		assertEquals("The batch failed, return code was not OK (0), but " + returnCode, returnCode, BatchStatus.BATCH_OK);
	}

	/**
	 * Try starting same batch twice.
	 */
	@Test(expected = InvalidBatchEntryException.class, timeout = 5000)
	public void testStartingSameBatchTwice() {
		startInfiniteBatchInAThread();
		try {
			Thread.sleep(500); // Let first batch start
			startInfiniteBatch(); // Want exception, thus start second batch in this thread.
		} catch (InterruptedException e) {
			fail("Expecting InvalidBatchEntryException");
		}
	}

	/**
	 * Start a simple batch that will succeed.
	 * 
	 * @return status code of batch
	 */
	private int startSuccessfulBatch() {
		return batchController.executeBatch(successfulBatchName, slice);
	}

	/**
	 * Start a simple infinite batch.
	 * 
	 * @return status code of batch
	 */
	private int startInfiniteBatch() {
		return batchController.executeBatch(infiniteBatchName, slice);
	}

	/**
	 * Start a simple infinite batch in a thread.
	 */
	private void startInfiniteBatchInAThread() {
		new BatchControllerServiceThreadWrapper(batchController, infiniteBatchName, slice).start();
	}
}
