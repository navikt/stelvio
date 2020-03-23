package no.stelvio.batch.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.batch.BatchParameterReader;
import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;
import no.stelvio.batch.repository.BatchRepository;

/**
 * Reads parameters from Stelvio batch table.
 *
 */
public class DefaultBatchParameterReader implements BatchParameterReader {
	private final Log logger = LogFactory.getLog(getClass());
	private BatchRepository batchRepository;

	/**  
	 * Reads parameters from T_BATCH for given batch name and slice 0.
	 */
	@Override
	public String getBatchParameters(String batchName) {
		try {
		BatchDO batchDo = batchRepository.findByNameAndSlice(batchName, 0);
		return batchDo.getParameters().replace(';', ',');
		} catch (InvalidBatchEntryException e) {
			if (logger.isWarnEnabled()) {
				logger.warn("Batch ikke konfigurert i T_BATCH", e);
			}
			return null;
		}
		
	}

	/**
	 * Sets implementation to use.
	 * @param batchRepository Repository to set.
	 */
	public void setBatchRepository(BatchRepository batchRepository) {
		this.batchRepository = batchRepository;
	}

}
