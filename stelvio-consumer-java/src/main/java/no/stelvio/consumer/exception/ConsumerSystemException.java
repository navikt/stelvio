package no.stelvio.consumer.exception;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * Should be thrown when a <code>RemoteException</code> is thrown from the integration layer.
 * 
 */
public class ConsumerSystemException extends SystemUnrecoverableException {

	private static final long serialVersionUID = -1578445678127445525L;

	/**
	 * Constructs a <code>ConsumerSystemException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public ConsumerSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>ConsumerSystemException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public ConsumerSystemException(String message) {
		super(message);
	}

}
