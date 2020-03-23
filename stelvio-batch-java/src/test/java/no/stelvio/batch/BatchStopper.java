package no.stelvio.batch;

/**
 * Class that will wait (to let client code start the batch first) then attempt to stop batch.
 * 
 *
 */
public class BatchStopper {

	private BatchBi batch;

	/**
	 * Creates a new instance of BatchStopper.
	 * 
	 * @param batch
	 *            that will be attempted stopped
	 */
	public BatchStopper(BatchBi batch) {
		this.batch = batch;
	}

	/**
	 * Will wait the specified milliseconds before attempting top stop the batch.
	 * 
	 * @param waitTimeInMillis wait time
	 */
	public void waitAndStopBatch(int waitTimeInMillis) {
		try {
			Thread.sleep(waitTimeInMillis);
			batch.stopBatch();
		} catch (Exception e) {
			// Do nothing
		}
	}

	/**
	 * Will wait 500 millis (0,5 sec) then stop the batch.
	 * 
	 */
	public void waitAndStopBatch() {
		waitAndStopBatch(500);
	}

}
