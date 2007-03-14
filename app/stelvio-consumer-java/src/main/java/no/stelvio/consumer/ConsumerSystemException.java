package no.stelvio.consumer;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * Should be thrown when a <code>RemoteException</code> is thrown from the integration layer.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class ConsumerSystemException extends SystemUnrecoverableException {
	private static final long serialVersionUID = -1578445678127445525L;

	/**
	 * Creates an instance with the given cause.
	 * 
	 * @param cause the cause for throwing this exception.
	 * @param serviceId the id of the service called when it failed.
	 */
	public ConsumerSystemException(Throwable cause, String serviceId) {
		super(cause, serviceId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate(int numArgs) {
		return "Technical error while invoking service with id {0}";
	}
}
