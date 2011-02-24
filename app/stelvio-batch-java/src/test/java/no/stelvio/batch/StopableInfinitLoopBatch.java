package no.stelvio.batch;

import no.stelvio.batch.exception.BatchFunctionalException;
import no.stelvio.batch.exception.BatchSystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements a stopable infinit loop batch.
 * 
 * @author MA
 *
 */
public class StopableInfinitLoopBatch extends AbstractBatch {

	private static Log log = LogFactory.getLog(StopableInfinitLoopBatch.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int executeBatch(int slice) throws BatchSystemException, BatchFunctionalException {

		log.debug("Execute was called");

		do {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new BatchSystemException("Unexpected exception in Thread.sleep", e) {
					private static final long serialVersionUID = 1L;
				};
			}
		} while (!isStopRequested());

		return BatchStatus.BATCH_STOPPED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void flushUpdates() {
		throw new RuntimeException("This operation is not supported in this unit test batch implementation");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopBatch() {
		super.stopBatch();
		log.debug("Stop was called on batch");
	}

}
