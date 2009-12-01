package no.nav.sibushelper.helper;

public class MessagingOperationFailedException extends Exception {

	private static final long serialVersionUID = 0x48f0e5d4ee4082dbL;

	/**
	 * @param message
	 */
	public MessagingOperationFailedException(String message) {
		super(message);
	}

	/**
	 * @param throwable
	 */
	public MessagingOperationFailedException(Throwable cause) {
		super(cause);
	}

}
