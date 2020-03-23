package no.stelvio.batch.exception;

/**
 * Thrown when batch parameters for a BatchDO isn't configured properly.
 * 
 *
 */
public class InvalidParameterFormatException extends BatchSystemException {

	private static final long serialVersionUID = 5009815541698395000L;

	/**
	 * String representation of the parameters that isn't configured properly.
	 */
	private String params;

	/**
	 * Constructs an <code>InvalidParameterFormatException</code> with message and cause.
	 * 
	 * @param message
	 *            the exception message.
	 * @param cause
	 *            the throwable that caused the exception to be raised.
	 */
	public InvalidParameterFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs an <code>InvalidParameterFormatException</code> with message, params and cause.
	 * 
	 * @param message
	 *            the exception message.
	 * @param params
	 *            the params that is not configured properly
	 * @param cause
	 *            the throwable that caused the exception to be raised.
	 */
	public InvalidParameterFormatException(String message, String params, Throwable cause) {
		super(message, cause);
		this.params = params;
	}

	/**
	 * Constructs an <code>InvalidParameterFormatException</code> with message.
	 * 
	 * @param message
	 *            the exception message.
	 */
	public InvalidParameterFormatException(String message) {
		super(message);
	}

	/**
	 * Get the parameters.
	 * 
	 * @return the params
	 */
	public String getParams() {
		return params;
	}
}
