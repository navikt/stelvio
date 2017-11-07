package no.stelvio.common.error;

/**
 * This class serves as an uchecked (unrecoverable) wrapper for checked (recoverable) types of exceptions.
 * 
 * ImplementationException should be used to wrap an exception when an interface declares it as checked, but the client
 * considers the exception as unrecoverable and wants unchecked exception behaviour.
 * 
 * This wrapper should be preferred over its counterpart <code>OperationalException</code> when wrapping an exception
 * considered an implementation (programming) error.
 * 
 * @author person19fa65691a36 (Accenture)
 */
public class ImplementationException extends FunctionalUnrecoverableException {

	private static final long serialVersionUID = -767527780715655131L;

	private Throwable wrappedException;

	/**
	 * Constructs an <code>ImplementationException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public ImplementationException(String message, Throwable cause) {
		super(message, cause);
		wrappedException = cause;
	}

	/**
	 * Method for retrieving the wrapped <code>Throwable</code>.
	 * 
	 * @return wrappedException the wrapped <code>Throwable</code>.
	 */
	public Throwable getWrappedThrowable() {
		return wrappedException;
	}
}