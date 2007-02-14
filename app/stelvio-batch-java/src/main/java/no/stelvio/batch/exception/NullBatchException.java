package no.stelvio.batch.exception;

/**
 * Exception thrown when a specified Batch is null
 * @author person983601e0e117 (Accenture)
 *
 */
public class NullBatchException extends BatchException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3364616443275434225L;

	/**
	 * No-arg constructor
	 *
	 */
	public NullBatchException() {
		super();
	}

	/**
	 * Constructor that takes the root cause of this exception
	 * @param cause
	 */
	public NullBatchException(Throwable cause) {
		super(cause);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate(int numArgs) {
		return "Batch cannot be null";
	}

}
