package no.stelvio.batch.repository;

import java.util.Collection;
import java.util.Date;

import no.stelvio.batch.domain.BatchHistDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;

/**
 * The BatchHistRepository is used by batch jobs to update the BatchHistDO.
 * The BatchHistDO holds that status for a batch and parameters used during execution of the batch.
 * 
 *
 */
public interface BatchHistRepository {

	/**
	 * Updates the BatchDO.
	 * Should not persist a <strong>new</strong> BatchDO.
	 * New BatchHistDO should be inserted by DBA
	 * @param batchHistory the BatchHistDO to update
	 *
	 */
	void updateBatchHist(BatchHistDO batchHistory);
	
	/**
	 * Retrieves available entries of BatchHistDO by the unique combination of name and slice.
	 * @param batchName the batch name
	 * @param slice the slice number
	 * @return the collection of BatchHistDO
	 * @throws InvalidBatchEntryException if zero, or more than one BatchHistDO is returned from persistence store
	 */
	Collection<BatchHistDO> findByNameAndSlice(String batchName, int slice);
	
	BatchHistDO findLastRunByNameAndSlice(String string, int i);

	long setHist(BatchHistDO batchHistDO);

	BatchHistDO findById(Long batchHistoryID);

	Collection<BatchHistDO> findByNameAndTimeInterval(String batchName, Date fromDate, Date toDate);

	Collection<BatchHistDO> findByNameAndDay(String batchName, Date startDay);
	
}
