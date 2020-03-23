package no.stelvio.batch.support;

import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.support.TransactionTemplate;

import no.stelvio.batch.StelvioBatchParameterReader;
import no.stelvio.batch.domain.BatchHistDO;
import no.stelvio.batch.repository.BatchHistRepository;
import no.stelvio.batch.repository.support.HibernateBatchHistRepository;
import no.stelvio.domain.time.ChangeStamp;

/**
 * Class for saving and retrieving history on batches that have been run
 * 
 **/
public class ControllerServiceHistorySupport {
	private final static String BATCH_STATUS_STARTED = "STARTED"; // Corresponds
	private static final String BATCH_STATUS_COMPLETED = "COMPLETED"; // Corresponds

	private BatchHistRepository repository;
	private StelvioBatchParameterReader reader;
    private TransactionTemplate transactionTemplate;

	public BatchHistRepository getRepository() {
		return repository;
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
		String parameters = reader.getBatchParameters(batchName, slice);
		batchHistory.setChangeStamp(new ChangeStamp(batchName));
		if (parameters != null){
			batchHistory.setParameters(parameters);
		}else {
			batchHistory.setParameters(" ");
		}
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
		final long[] id = new long[1];
		transactionTemplate.execute(status -> {
			batchHistory.setBatchname(batchName);
			batchHistory.setStatus(BATCH_STATUS_STARTED);
			batchHistory.setStartTime();
			id[0] = repository.setHist(batchHistory);
			return null;});
		return id[0];
	}

	public boolean saveAdditionalBatchInformation(long batchHistoryId,
			int result) {
		transactionTemplate.execute(status -> {
			BatchHistDO batchHistory = fetchBatchHistory(batchHistoryId);
			batchHistory.setEndTime();

			// TODO result is returned as int, persisted as String, as of now it's
			// just casted
			batchHistory.setStatus(((Integer) result).toString());

			repository.updateBatchHist(batchHistory);
			return null;});
		return true;
	}

	public BatchHistDO fetchBatchHistory(Long batchHistoryID) {
		int connectionAttempts = 0;

		while (connectionAttempts < 2){
			try {
				return repository.findById(batchHistoryID);
			} catch (UncategorizedSQLException e){
				connectionAttempts++;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		throw new RuntimeException("Can not update batch history. Check connection to database.");
	}

	public void setRepository(BatchHistRepository repository) {
		this.repository = repository;
	}

	public void setReader(StelvioBatchParameterReader reader) {
		this.reader = reader;
	}

	public void setBatchHistoryRepository(HibernateBatchHistRepository histRepository) {
		this.repository = histRepository;
	}

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

}
