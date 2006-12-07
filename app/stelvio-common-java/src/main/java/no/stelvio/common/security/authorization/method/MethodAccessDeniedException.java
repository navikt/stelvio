package no.stelvio.common.security.authorization.method;

import java.lang.reflect.Method;

import no.stelvio.common.security.SecurityException;

/**
 * Exception thrown if access to a method should be denied.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class MethodAccessDeniedException extends SecurityException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param method
	 *            the method access is denied to.
	 */
	public MethodAccessDeniedException(Method method) {
		super(method);
	}

	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param method
	 *            the method access is denied to.
	 */
	public MethodAccessDeniedException(String method) {
		super(method);
	}

	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param cause
	 * 			  the root cause for the exception.
	 * @param method
	 *            the method in which access was denied.
	 */
	public MethodAccessDeniedException(Throwable cause, Method method) {
		super(cause, method);
	}

	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param cause
	 * 			  the root cause for the exception.
	 * @param method
	 *            the method access is denied to.
	 */
	public MethodAccessDeniedException(Throwable cause, String method) {
		super(cause, method);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate() {
		return "Access is denied to method: {0}";
	}

}
