package no.stelvio.batch;

/**
 * Holds the status codes that are allowed from a Batch.
 * 
 * @author person356941106810, Accenture
 * @version $Id: BatchStatus.java 2056 2005-03-04 07:37:29Z psa2920 $
 */
public class BatchStatus {
	
	/** The batch executed OK */
	public static final int BATCH_OK = 0;
	
	/** The batch executed, but with warnings */
	public static final int BATCH_WARNING = 4;
	
	/** The batch failed */
	public static final int BATCH_ERROR = 8;
	
	/** The batch failed fatally */
	public static final int BATCH_FATAL = 16; 
}
