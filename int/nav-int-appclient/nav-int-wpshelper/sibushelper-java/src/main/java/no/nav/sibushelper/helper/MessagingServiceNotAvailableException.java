package no.nav.sibushelper.helper;

public class MessagingServiceNotAvailableException extends MessagingOperationFailedException {

	private static final long serialVersionUID = 0x9d6edd3ca5cca85L;

	/**
	 * @param message
	 */
	public MessagingServiceNotAvailableException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MessagingServiceNotAvailableException(Throwable cause) {
		super(cause);
	}

}
