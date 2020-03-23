package no.stelvio.common.error;

/**
 * Exceptions implementing the Stelvio error code feature must implement this
 * interface for their error code field. The implementation is passed to the
 * exceptions' constructor
 * 
 *
 * @see {@link #UnrecoverableException(String, ErrorCode)}
 * @see {@link #UnrecoverableException(String, ErrorCode, Throwable)}
 * @see {@link #RecoverableException(String, ErrorCode)}
 * @see {@link #RecoverableException(String, ErrorCode, Throwable)}
 */
public interface ErrorCode {
	
	/**
	 * Returns the error code for the exception.
	 * 
	 * @return the error code.
	 */
	String getErrorCode();
	
}