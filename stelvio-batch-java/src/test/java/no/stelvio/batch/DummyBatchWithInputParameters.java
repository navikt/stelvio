package no.stelvio.batch;

import no.stelvio.batch.exception.BatchFunctionalException;
import no.stelvio.batch.exception.BatchSystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DummyBatchWithInputParameters extends AbstractBatch{

	/**
	 * Implements a batch that can take input parameters to specify how long the batch should run, and 
	 * which exit code should be returned.  The timeToRun is approximate.  
	 * 
	 *
	 */

	private static Log log = LogFactory.getLog(DummyBatchWithInputParameters.class);
	
	private int timeToRun;
	
	private int exitCode;

	/**
	 * Default constructor if no parameters are sent, calls other constructor with default 
	 * parameters (timeToRun = 2 seconds, exitCode = OK)
	 */
	public DummyBatchWithInputParameters()
	{
		this(2, BatchStatus.BATCH_OK);
	}
	
	/**
	 * Constructor that sets how long the batch should run and what exitCode should be 
	 * returned when it finishes.  
	 * 
	 * @param timeToRun
	 * @param exitCode
	 */
	public DummyBatchWithInputParameters(int timeToRun, int exitCode)
	{
		this.timeToRun = timeToRun;
		this.exitCode = exitCode;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int executeBatch(int slice) throws BatchSystemException, BatchFunctionalException {

		log.debug("Execute was called");
		
		long timeInMS = timeToRun*1000;

		try {
			Thread.sleep(timeInMS);
		} catch (InterruptedException e) {
			throw new BatchSystemException("Unexpected exception in Thread.sleep", e) {
				private static final long serialVersionUID = 1L;
			};
		}
		
		return exitCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void flushUpdates() {
		throw new RuntimeException("This operation is not supported in this unit test batch implementation");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopBatch() {
		super.stopBatch();
		log.debug("Stop was called on batch");
	}
	
	public int getTimeToRun() {
		return timeToRun;
	}

	public void setTimeToRun(int timeToRun) {
		this.timeToRun = timeToRun;
	}

	public int getExitCode() {
		return exitCode;
	}

	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}
}
