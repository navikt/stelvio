package no.stelvio.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;
import no.stelvio.batch.repository.BatchRepository;

/**
 * Reads parameters from Stelvio batch table.
 *
 */
public class StelvioBatchParameterReader implements BatchParameterReader {
	private final Log logger = LogFactory.getLog(getClass());
	private BatchRepository batchRepository;

	/**  
	 * Reads parameters from T_BATCH for given batch name and slice 0.
	 */
	@Override
	public String getBatchParameters(String batchName) {
		return this.getBatchParameters(batchName, 0);		
	}

	public String getBatchParameters(String batchName, int slice) {
		try {
		BatchDO batchDo = batchRepository.findByNameAndSlice(batchName, slice);
		return batchDo.getParameters().replace(';', ',');
		} catch (InvalidBatchEntryException e) {
			if (logger.isInfoEnabled()) {
				logger.info("Batch ikke konfigurert i T_BATCH", e);
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
