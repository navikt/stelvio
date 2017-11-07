package no.stelvio.batch.controller;

/**
 * Work controller interface for all batches.
 * 
 * @author persond9e847e67144, NAV
 * 
 * @version $Id$
 */

public interface BatchControllerServiceBi {
	/**
	 * Executes the batch with given name for given slice.
	 * 
	 * @param batchName
	 *            The name of the batch to execute.
	 * @param slice
	 *            The number identifying the slice this batch will process.
	 * @return the status code of the batch.
	 */
	int executeBatch(String batchName, int slice);
	
	/**
	 * Attempts to stops the given batch.
	 * 
	 * @param batchName
	 *            The name of the batch to execute.
	 * @param slice
	 *            The number identifying the slice this batch will process.
	 * @return <code>true</code> if a stop call was succesfully sent to the batch
	 * 			<code>false</code> if no running batch with the supplied batch name and slice was located
	 */	
	boolean stopBatch(String batchName, int slice);

}
