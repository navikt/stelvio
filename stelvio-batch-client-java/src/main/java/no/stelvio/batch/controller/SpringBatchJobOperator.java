package no.stelvio.batch.controller;

import no.stelvio.batch.BatchStatus;


/**
 * Interface for executing Spring batch jobs.
 *
 */
public interface SpringBatchJobOperator {


	/**
	 * Start/Restart batch with given name.
	 * @param jobName Name of batch/job to execute.
	 * @param parameters Input parameters for job-execution.
	 * @return Status of execution, @see {@link BatchStatus}.
	 */
	int executeBatch(String jobName, String parameters);
	
	/**
	 * Sends a stop request to a running batch execution.
	 * @param jobName Name of batch/job to stop.
	 * @return true if the message was successfully sent (does not guarantee
	 * that the job has stopped)
	 */
	boolean stopBatch(String jobName);

}
