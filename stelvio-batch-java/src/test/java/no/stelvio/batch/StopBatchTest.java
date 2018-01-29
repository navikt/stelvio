package no.stelvio.batch;

import static org.junit.Assert.assertTrue;
import no.stelvio.batch.controller.BatchControllerServiceBi;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;

/**
 * Test class for stopping batch.
 * 
 * @author MA
 *
 */
public class StopBatchTest {

	private ApplicationContext ctx;

	/**
	 * Setup this test. Creating a context.
	 */
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("tst-stopbatch-context.xml");
	}

	/**
	 * Ter down test. Removing context.
	 */
	@After
	public void tearDown() {
		ctx = null;
	}

	/**
	 * Test if batch can be stopped.
	 */
	@Test
	@Ignore
	public void batchCanBeStopped() {
		BatchBi batch = (BatchBi) ctx.getBean("stopableBatch");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		new BatchStopper(batch).waitAndStopBatch();
		batch.executeBatch(0);
		stopWatch.stop();
		assertTrue("Batch should have been stopped after at least 1 and no more than 2 seconds", 
				stopWatch.getTotalTimeSeconds() >= 1.0
				&& stopWatch.getTotalTimeSeconds() <= 2.0);
	}

	/**
	 * Test if test can be stopped by controller service.
	 */
	@Test
	@Ignore
	public void batchCanBeStoppedByControllerService() {
		BatchControllerServiceBi batchController = (BatchControllerServiceBi) ctx.getBean("batchService");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		new BatchControllerServiceThreadWrapper(batchController, "stopTest", 0).start();
		try {
			Thread.sleep(500); // Let batch start
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean batchWasLocatedAndStopped = batchController.stopBatch("stopTest", 0);
		stopWatch.stop();
		assertTrue("The batch was not located by the registry and could not be stopped", batchWasLocatedAndStopped);
		assertTrue("Batch should have been terminated in less than one second", stopWatch.getTotalTimeSeconds() < 1);
	}
}
