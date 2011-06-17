package no.stelvio.batch.support;

import java.sql.Clob;
import java.util.Collection;
import java.util.Date;

import no.stelvio.batch.domain.BatchHistDO;
import no.stelvio.batch.repository.BatchHistRepository;
import no.stelvio.batch.repository.support.HibernateBatchHistRepository;
import no.stelvio.domain.time.ChangeStamp;

import org.apache.commons.lang.NotImplementedException;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.BatchStatus;

/**
 * Class for saving and retrieving history on batches that have been run
 * 
 * @author person5dc3535ea7f4(Accenture)
 **/
public class ControllerServiceHistorySupport {

	BatchHistRepository repository;
	private final static String BATCH_STATUS_STARTED = "STARTED"; // Corresponds
	// with
	// BatchStatus.STARTED
	private static final String BATCH_STATUS_COMPLETED = "COMPLETED"; // Corresponds
	// with
	// BatchStatus.COMPLETED

	public ControllerServiceHistorySupport() {
		
	}

	public BatchHistRepository getRepository() {
		return repository;
	}

	public void setRepository(BatchHistRepository repository) {
		this.repository = repository;
	}
	
	/**
	 * Saves batch information in T_BATCH_HIST when a (classic) batch is started (when the execute method is called).  
	 * Used in DefaultBatchControllerService's execute method.  
	 * 
	 * @param batchName
	 * @param slice
	 * @return
	 */
	public long saveInitialBatchInformation(String batchName, int slice) {
		BatchHistDO batchHistory = new BatchHistDO();
		batchHistory.setChangeStamp(new ChangeStamp(batchName));
		batchHistory.setSlice(slice);
		return saveInitialCommonBatchInformation(batchHistory, batchName);
	}

	/**
	 * Saves batch information in T_BATCH_HIST when a spring batch is started (when the execute method is called). 
	 * Not used yet.  
	 * 
	 * @param jobName
	 * @param parameters
	 * @return
	 */
	public long saveInitialBatchInformation(String jobName, String parameters) {
		BatchHistDO batchHistory = new BatchHistDO();
		batchHistory.setChangeStamp(new ChangeStamp(jobName));
		
		if (parameters != null){
			batchHistory.setParameters(parameters);			
		}
		else {
			batchHistory.setParameters(" ");
		}
		
		return saveInitialCommonBatchInformation(batchHistory, jobName);
	}

	private long saveInitialCommonBatchInformation(BatchHistDO batchHistory,
			String batchName) {
		batchHistory.setBatchname(batchName);
		batchHistory.setStatus(BATCH_STATUS_STARTED);
		batchHistory.setStartTime();
		batchHistory.setParameters(" ");
		return repository.setHist(batchHistory);
	}

	public boolean saveAdditionalBatchInformation(long batchHistoryId,
			int result) {

		BatchHistDO batchHistory = fetchBatchHistory(batchHistoryId);
		batchHistory.setEndTime();

		// TODO result is returned as int, persisted as String, as of now it's
		// just casted
		batchHistory.setStatus(((Integer) result).toString());

		repository.updateBatchHist(batchHistory);

		return true;
	}

	// Returns last run row matching batchName and slice
	// TODO Write proper docs

	public void setBatchHistoryRepository(
			HibernateBatchHistRepository histRepository) {
		this.repository = histRepository;
	}

	public BatchHistDO fetchBatchHistory(Long batchHistoryID) {
		return repository.findById(batchHistoryID);
	}

}
