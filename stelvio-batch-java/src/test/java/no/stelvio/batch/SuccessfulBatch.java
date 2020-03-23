package no.stelvio.batch;

import no.stelvio.batch.exception.BatchFunctionalException;
import no.stelvio.batch.exception.BatchSystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SuccessfulBatch defines a successful batch for test purposes.
 * 
 *
 */
public class SuccessfulBatch extends AbstractBatch {

	private final Log log = LogFactory.getLog(SuccessfulBatch.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int executeBatch(int slice) throws BatchSystemException, BatchFunctionalException {
		log.debug("Execute was called");
		return BatchStatus.BATCH_OK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void flushUpdates() {
		throw new RuntimeException("This operation is not supported in this unit test batch implementation");
	}
}
