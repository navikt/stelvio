package no.stelvio.batch;

import no.stelvio.batch.controller.BatchControllerServiceBi;

/**
 * A BatchControllerServiceThreadWrapper for test.
 * 
 *
 */
class BatchControllerServiceThreadWrapper extends Thread {
	private BatchControllerServiceBi controller;
	private String batchName;
	private int slice;

	/**
	 * Creates a new instance of BatchControllerServiceThreadWrapper.
	 * 
	 * @param batchController
	 *            batch controller
	 * @param batchName
	 *            batch name
	 * @param slice
	 *            slice
	 */
	public BatchControllerServiceThreadWrapper(BatchControllerServiceBi batchController, String batchName, int slice) {
		controller = batchController;
		this.slice = slice;
		this.batchName = batchName;
	}

	/**
	 * run batch.
	 */
	@Override
	public void run() {
		controller.executeBatch(batchName, slice);
	}
}
