package no.stelvio.batch;


/**
 * Can be used to get additional job parameters for Spring Batches.
 * 
 * @author person47c121e3ccb5, BEKK
 *
 */
public interface BatchParameterReader {

	/**
	 * Reads parameters from a parameter source.
	 * @param batchName Name of batch to get parameters for.
	 * @return A String of parameters, where parameters are separated with ','
	 */
	String getBatchParameters(String batchName);

}
