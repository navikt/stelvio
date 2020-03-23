package no.stelvio.batch.exception;

import no.stelvio.batch.domain.BatchDO;

/**
 * Exception thrown when a batch parameter can't be found in the database.
 * 
 *
 */
public class NoSuchParameterException extends BatchSystemException {

	private static final long serialVersionUID = -1285992859677094787L;

	/**
	 * The parameter that can't be found.
	 */
	private String parameter;

	/**
	 * The name of the batch that failed.
	 */
	private BatchDO batchDO;

	/**
	 * Constructs a <code>NoSuchParameterException</code> with message and cause.
	 * 
	 * @param message
	 *            the exception message.
	 * @param cause
	 *            the throwable that caused the exception to be raised.
	 */
	public NoSuchParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>NoSuchParameterException</code> with message and cause.
	 * 
	 * @param message
	 *            the exception message.
	 * @param batchDO
	 *            the batch
	 * @param param
	 *            parameter
	 * @param cause
	 *            the throwable that caused the exception to be raised.
	 */
	public NoSuchParameterException(String message, BatchDO batchDO, String param, Throwable cause) {
		super(message, cause);
		this.parameter = param;
		this.batchDO = batchDO;
	}

	/**
	 * Constructs a <code>NoSuchParameterException</code> with message and cause.
	 * 
	 * @param message
	 *            the exception message.
	 * @param batchDO
	 *            the batch
	 * @param param
	 *            parameter
	 */
	public NoSuchParameterException(String message, BatchDO batchDO, String param) {
		super(message);
		this.parameter = param;
		this.batchDO = batchDO;
	}

	/**
	 * Constructs a <code>NoSuchParameterException</code> with message.
	 * 
	 * @param message
	 *            the exception message.
	 */
	public NoSuchParameterException(String message) {
		super(message);
	}

	/**
	 * Gets the parameter.
	 * 
	 * @return the param
	 */
	public String getParam() {
		return parameter;
	}

	/**
	 * Gets the batch.
	 * 
	 * @return the batchDO
	 */
	public BatchDO getBatchDO() {
		return batchDO;
	}

}
