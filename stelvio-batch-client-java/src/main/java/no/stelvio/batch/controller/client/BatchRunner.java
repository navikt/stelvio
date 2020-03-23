package no.stelvio.batch.controller.client;

import no.stelvio.batch.controller.BatchControllerServiceBi;

/**
 * Interface for starting standalone batch.
 * 
 *
 * @version $Id$
 */
public interface BatchRunner {
	/**
	 * Start the batch and write output and error to log.
	 * 
	 * @param batchName
	 *            The name of the batch
	 * @param slice
	 *            The slice with which to run the batch
	 * @return The status from running the given batch and slice
	 * 
	 */
	int runBatch(String batchName, int slice);

	/**
	 * Stop the batch.
	 * 
	 * @param batchName
	 *            The name of the batch
	 * @param slice
	 *            The slice number used as in param when the batch was executed
	 * 
	 * @return <code>true</code> if a stop call was successfully sent to the batch <code>false</code> if no running batch with
	 *         the supplied batch name and slice was located
	 * 
	 */
	boolean stopBatch(String batchName, int slice);

	/**
	 * Gets the controller service.
	 * 
	 * @return BatchControllerServiceBi The controller service
	 * 
	 */
	BatchControllerServiceBi getControllerService();

	/**
	 * Sets the batch controller service.
	 * 
	 * @param controllerService
	 *            The BatchControllerSerivceBi to set
	 * 
	 */
	void setControllerService(BatchControllerServiceBi controllerService);

}
