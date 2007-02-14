package no.stelvio.batch.exception;

/**
 * Thrown when batch parameters for a BatchDO isn't configured properly.
 * @author person983601e0e117 (Accenture)
 *
 */
public class InvalidParameterFormatException extends BatchException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5009815541698395000L;

	/**
	 * Creates a new InvalidParameterFormatException
	 * @param batchName unique name of the batch
	 * @params params batch parameters
	 */
	public InvalidParameterFormatException(String batchName, String params) {
		super(batchName,params);
	}

	/**
	 * Creates a new InvalidParameterFormatException
	 * @param cause the throwable cause of this exception
	 * @param batchName unique name of the batch
	 * @params params batch parameters 
	 */
	public InvalidParameterFormatException(Throwable cause, String batchName, String params) {
		super(cause, batchName, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate(int numArgs) {
		return "{0} contains params that are not in the correct format. Params: {1}";  
	}

}
