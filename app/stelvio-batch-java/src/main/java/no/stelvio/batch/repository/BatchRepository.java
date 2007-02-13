package no.stelvio.batch.repository;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;

/**
 * The BatchRepository is used by batch jobs to update the BatchDO.
 * The BatchDO holds that status for a batch and parameters used during execution of the batch.
 * 
 * @author person983601e0e117 (Accenture)
 *
 */
public interface BatchRepository {

	/**
	 * Updates the BatchDO
	 * Should not persist a <strong>new</strong> BatchDO.
	 * New BatchDOs should be inserted by DBA
	 * @param batch the BatchDO to update
	 *
	 */
	public void updateBatch(BatchDO batch);
	
	/**
	 * Retrieves a BatchDO by the unique name
	 * @param batchName
	 * @return the BatchDO
	 * @throws InvalidBatchEntryException if zero, or more than one BatchDO is returned from persistence store
	 */
	public BatchDO findByName(String batchName) throws InvalidBatchEntryException;
	
}
