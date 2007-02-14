package no.stelvio.batch.exception;

/**
 * Thrown when the number of entries in the databas for a batch name is invalid. 
 * Typically this means that the number of lines returned when querying by batch name is more than 1 or is zero. 
 * @author person983601e0e117 (Accenture)
 *
 */
public class InvalidBatchEntryException extends BatchException{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -1091438174826992714L;


	/**
	 * Constructs new InvalidBatchEntryException
	 * @param batchName name of batch that's invalid
	 */
	public InvalidBatchEntryException(String batchName) {
		super(batchName);
	}

	/**
	 * Constructs new InvalidBatchEntryException
	 * @param cause
	 * @param batchName name of batch that's invalid
	 */
	public InvalidBatchEntryException(Throwable cause, String batchName) {
		super(cause, batchName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate(int numArgs) {
		return  "{0} has zero or more than 1 entries in the database";
	}

}
