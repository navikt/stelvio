package no.stelvio.batch;

import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.exception.InvalidBatchEntryException;
import no.stelvio.batch.exception.InvalidParameterFormatException;
import no.stelvio.batch.exception.NullBatchException;
import no.stelvio.common.error.logging.ExceptionLogger;
import no.stelvio.common.log.InfoLogger;

/**
 * Interface for classes implementing scheduled batch logic.
 * 
 */
public interface BatchBi {

	/**
	 * Executes a batch and returns the status. This method drives the batch, and must be implemented by all batches.
	 * 
	 * @param slice
	 *            slice number for parallel batches, first number is 0.
	 * 
	 * @return the status code of the batch {@link BatchStatus}.
	 */
	int executeBatch(int slice);

	/**
	 * Fetch batch properties.
	 * 
	 * @param batchDO
	 *            The batch
	 * @return The properties
	 * @throws NullBatchException
	 *             if batch is null
	 * @throws InvalidParameterFormatException
	 *             if invalid parameter format
	 */
	Properties fetchBatchProperties(BatchDO batchDO) throws NullBatchException, InvalidParameterFormatException;

	/**
	 * Reads the batch configuration for the given batch.
	 * 
	 * @param batchName
	 *            the name of the batch to read config for
	 * @param slice
	 *            the number identifying the slice this batch will process
	 * @return the batch configuration or null if no config exists.
	 * @throws InvalidBatchEntryException
	 *             if the specified batch was not found or duplicate entries in database matches criteria.
	 */
	BatchDO readBatchParameters(String batchName, int slice) throws InvalidBatchEntryException;

	/**
	 * Get required parameters.
	 * 
	 * @return required parameters
	 */
	Collection<? extends String> getRequiredParameters();

	/**
	 * Get optional parameters.
	 * 
	 * @return optional parameters
	 */
	Collection<? extends String> getOptionalParameters();

	/**
	 * Returns the batch name.
	 * 
	 * @return the name of the batch
	 */
	String getBatchName();

	/**
	 * Get the exceptionLogger configured for the batch.
	 * 
	 * @return exception logger
	 */
	ExceptionLogger getExceptionLogger();

	/**
	 * Get the infoLogger configured for the batch.
	 * 
	 * @return Info logger
	 */
	InfoLogger getInfoLogger();

	/**
	 * Get batch slice.
	 * 
	 * @return Batch slice
	 */
	int getSlice();

	/**
	 * Gets timestamp when batch job was created.
	 * 
	 * @return The timeStamp
	 */
	Date getTimeStamp();

	/**
	 * Returns <code>true</code> if it has been requested that the execution of the batch should stop.
	 * 
	 * @return <code>true</code> if batch should stop
	 */
	boolean isStopRequested();

	/**
	 * Sets the batch name.
	 * 
	 * @param batchName
	 *            the name given to the batch
	 */
	void setBatchName(String batchName);

	/**
	 * Sets batch slice.
	 * 
	 * @param slice
	 *            Batch slice.
	 */
	void setSlice(int slice);

	/**
	 * Sets batch timestamp. Should be set when batch job is created.
	 * 
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	void setTimeStamp(Date timeStamp);

	/**
	 * Indicates that the batch must be stopped. The method call requests that the batch is stopped, it is however not
	 * automatically stopped. The batch implementation must be implemented to handle stop requests if batches are to be stopped
	 * sucessfully. The framework it self will not force batch to be stopped.
	 * 
	 */
	void stopBatch();

}
