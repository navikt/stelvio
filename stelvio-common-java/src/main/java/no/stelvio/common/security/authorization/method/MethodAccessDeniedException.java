package no.stelvio.common.security.authorization.method;

import java.lang.reflect.Method;

import no.stelvio.common.security.SecurityException;

/**
 * Exception thrown if access to a method should be denied.
 * 
 */
public class MethodAccessDeniedException extends SecurityException {

	private static final long serialVersionUID = 1L;

	private transient Method method;

	/**
	 * Constructs a <code>MethodAccessDeniedException</code> with method, message and cause.
	 * 
	 * @param method -
	 *            the method.
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public MethodAccessDeniedException(Method method, String message, Throwable cause) {
		super(message, cause);
		this.method = method;
	}

	/**
	 * Constructs a <code>MethodAccessDeniedException</code> with method and message.
	 * 
	 * @param method -
	 *            the method.
	 * @param message -
	 *            the exception message.
	 */
	public MethodAccessDeniedException(Method method, String message) {
		super(message);
		this.method = method;
	}

	/**
	 * Constructs a <code>MethodAccessDeniedException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public MethodAccessDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>MethodAccessDeniedException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public MethodAccessDeniedException(String message) {
		super(message);
	}

	/**
	 * Get method.
	 * 
	 * @return method
	 */
	public Method getMethod() {
		return method;
	}

}