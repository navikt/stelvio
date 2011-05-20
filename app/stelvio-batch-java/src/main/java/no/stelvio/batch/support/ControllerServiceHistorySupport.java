package no.stelvio.batch.support;

import java.sql.Clob;
import java.util.Collection;
import java.util.Date;

import no.stelvio.batch.domain.BatchHistDO;
import no.stelvio.batch.repository.BatchHistRepository;
import no.stelvio.batch.repository.support.HibernateBatchHistRepository;

import org.apache.commons.lang.NotImplementedException;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.BatchStatus;

/*
 * Class for saving and retrieving history on batches that have been run
 * 
 * @author person5dc3535ea7f4(Accenture)
 */
public class ControllerServiceHistorySupport {

	BatchHistRepository repository;
	private final static String BATCH_STATUS_STARTED = "STARTED"; // Corresponds
	// with
	// BatchStatus.STARTED
	private static final String BATCH_STATUS_COMPLETED = "COMPLETED"; // Corresponds
	// with
	// BatchStatus.COMPLETED
	private SessionFactory factoryFromBean;

	public ControllerServiceHistorySupport() {
		
	}

	public BatchHistRepository getRepository() {
		return repository;
	}

	public void setRepository(BatchHistRepository repository) {
		this.repository = repository;
	}

	public long saveInitialBatchInformation(String batchName, int slice) {
		BatchHistDO batchHistory = new BatchHistDO();
		batchHistory.setSlice(slice);
		return saveInitialCommonBatchInformation(batchHistory, batchName);
	}

	public long saveInitialBatchInformation(String jobName, String parameters) {
		BatchHistDO batchHistory = new BatchHistDO();
		batchHistory.setParameters(parameters);
		return saveInitialCommonBatchInformation(batchHistory, jobName);
	}

	public long saveInitialCommonBatchInformation(BatchHistDO batchHistory,
			String batchName) {
		batchHistory.setBatchname(batchName);
		batchHistory.setStatus(BATCH_STATUS_STARTED);
		batchHistory.setStartTime();
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

	// Returns last run row matchin batchName and slice
	// TODO Write proper docs
	public Collection <BatchHistDO> fetchBatchHistory(String batchName, int slice) {

		return repository.findByNameAndSlice(batchName, slice);
	}


	public BatchHistDO fetchBatchHistory(Long batchHistoryID) {
		return repository.findById(batchHistoryID);
	}

	//TODO remove this or implement it
//	public BatchHistDO fetchBatchHistory(String batchName, int slice,
//			int runNumber) {
//
//		throw new NotImplementedException();
//	}

	public Collection<BatchHistDO> fetchBatchHistory(String batchName,
			Date fromDate, Date toDate) {
		return repository
				.findByNameAndTimeInterval(batchName, fromDate, toDate);
	}

//	public Collection<BatchHistDO> fetchBatchHistoryCollection(
//			String batchName, int slice) {
//
//		return repository.findByNameAndSlice(batchName, slice);
//	}

	public void setBatchHistoryRepository(
			HibernateBatchHistRepository histRepository) {
		this.repository = histRepository;
	}

	public Collection <BatchHistDO> fetchBatchHistory(String batchName, Date startDay) {
		return repository.findByNameAndDay(batchName, startDay);
	}
}
