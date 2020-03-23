package no.stelvio.batch.controller.client.support;

import no.stelvio.batch.BatchStatus;
import no.stelvio.batch.controller.SpringBatchEnabledBatchControllerServiceBi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Extension of {@link DefaultBatchRunner} to support execution/launching of spring batches.
 * 
 */
public class SpringBatchEnabledBatchRunner extends DefaultBatchRunner {

	private final Log log = LogFactory.getLog(SpringBatchEnabledBatchRunner.class);

	/**
	 * Starts a spring batch execution.
	 * 
	 * @param batchName
	 *            Name of batch/job.
	 * @param parameters
	 *            Input parameters to batch.
	 * @return Batch status, see {@link BatchStatus}.
	 */
	public int runBatch(String batchName, String parameters) {
		int result = -1;
		long time1 = System.currentTimeMillis();

		try {
			SpringBatchEnabledBatchControllerServiceBi controller = getController();

			if (log.isInfoEnabled()) {
				log.info("Starting batch: " + batchName + " using parameters: " + parameters + " with user: "
						+ getCorbaUserId());
			}
			result = controller.executeBatch(batchName, parameters);

		} catch (Exception ex) {
			if (log.isFatalEnabled()) {
				log.fatal(
						"Batch " + batchName + " parameters: " + parameters + "terminated with exception: " + ex.getMessage(),
						ex);
			}
			result = BatchStatus.BATCH_FATAL;
		} finally {
			long time2 = System.currentTimeMillis();
			long timeSecs = (time2 - time1) / 1000;

			if (log.isInfoEnabled()) {
				log.info("Batch: " + batchName + " parameters: " + parameters + " terminated with status: " + result);
				log.info("Total time (sec) = " + timeSecs);
			}
		}

		return result;
	}

	/**
	 * Sends a stop request to a running batch execution.
	 * 
	 * @param batchName
	 *            Name of batch/job to stop.
	 * @return true if the message was successfully sent (does not guarantee that the job has stopped)
	 */
	public boolean stopBatch(String batchName) {
		boolean result = false;
		try {
			SpringBatchEnabledBatchControllerServiceBi controller = getController();

			if (log.isInfoEnabled()) {
				log.info("Stopping batch: " + batchName + " with user: " + getCorbaUserId());
			}
			result = controller.stopBatch(batchName);
		} catch (Exception e) {
			if (log.isFatalEnabled()) {
				log
						.fatal("Error occured while trying to stop batch " + batchName + ". The exception was: "
								+ e.getMessage(), e);
			}
			result = false;
		}
		return result;
	}

	/**
	 * Retrieves the controllerservice.
	 * 
	 * @return the batch controller, see {@link SpringBatchEnabledBatchControllerServiceBi}
	 */
	private SpringBatchEnabledBatchControllerServiceBi getController() {
		SpringBatchEnabledBatchControllerServiceBi controller = 
			(SpringBatchEnabledBatchControllerServiceBi) getControllerService();
		if (controller == null) {
			throw new RuntimeException("Could not get controller. See errorlog for details.");
		}
		return controller;
	}

}
