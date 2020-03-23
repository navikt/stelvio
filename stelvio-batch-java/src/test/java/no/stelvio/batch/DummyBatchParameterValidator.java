package no.stelvio.batch;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.exception.BatchFunctionalException;
import no.stelvio.batch.exception.BatchSystemException;
import no.stelvio.batch.exception.InvalidBatchEntryException;

/**
 * AbstractBatch implementation used to test the BatchValidatorInterceptor.
 * 
 *
 */
public class DummyBatchParameterValidator extends AbstractBatch {

	private BatchDO batchDO;

	/**
	 * Creates a new instance of DummyBatchParameterValidator.
	 * 
	 */
	public DummyBatchParameterValidator() {
		batchDO = new BatchDO();
		batchDO.setBatchname("Batch");
		batchDO.setParameters("requiredParameter=123;workUnit=100;progressInterval=321");
	}

	/**
	 * Read batch parameters, using the local batchDO.
	 * 
	 * @param batchName ignored
	 * @param slice ignored
	 * @return batch
	 * @throws InvalidBatchEntryException not thrown
	 */
	@Override
	public BatchDO readBatchParameters(String batchName, int slice) throws InvalidBatchEntryException {
		return batchDO;
	}

	/**
	 * Execute batch, doing nothing but returning ok status.
	 * 
	 * @param slice ignored
	 * @return BATCH_OK
	 * @throws BatchFunctionalException not thrown
	 * @throws BatchSystemException not thrown
	 */
	@Override
	public int executeBatch(int slice) throws BatchSystemException, BatchFunctionalException {
		return BatchStatus.BATCH_OK;
	}

	/**
	 * Flush updates, doing nothing.
	 */
	@Override
	protected void flushUpdates() {
	}

	/**
	 * Get batch.
	 * 
	 * @return batch
	 */
	public BatchDO getBatchDO() {
		return batchDO;
	}

	/**
	 * Set batch.
	 * 
	 * @param b
	 *            batch
	 */
	public void setBatchDO(BatchDO b) {
		this.batchDO = b;
	}

}
