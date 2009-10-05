/**
 * 
 */
package no.stelvio.common.exception;

import no.stelvio.common.bus.util.ErrorHelperUtil;
import no.stelvio.common.interceptor.Interceptor;
import no.stelvio.common.interceptor.InterceptorChain;

import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.scdl.OperationType;

/**
 * Interceptor implementation that transforms service runtime exceptions (root
 * and nested) so that the exception can safely propagate through the
 * integration platform (bus) without any marshalling problems.
 * 
 * This class is typically used in the producer layer as an "exception shield"
 * towards the unrelying service.
 * 
 * @author test@example.com
 */
public class CatchServiceRuntimeExceptionInterceptor implements Interceptor {
	private static final StackTraceElement[] EMPTY_STACK_TRACE_ELEMENTS = new StackTraceElement[0];

	public Object doIntercept(OperationType operationType, Object input, InterceptorChain interceptorChain) {
		try {
			return interceptorChain.doIntercept(operationType, input);
		} catch (ServiceRuntimeException sre) {
			throw (ServiceRuntimeException) convertThrowable(sre);
		}
	}

	public Throwable convertThrowable(Throwable t) {
		Throwable root = t;
		Throwable cause = root.getCause();
		boolean causeSame = true;

		while (cause != null) {
			Throwable newCause = cause.getCause();
			causeSame = newCause == cause;
			if (newCause == null) {
				root = cause;
			}
			cause = newCause;
		}

		boolean safeClass = isSafeClass(root);

		Throwable result;
		if (safeClass && causeSame) {
			// Nothing to convert (safe class and causeSame) - just return t
			result = root;
		} else {
			if (ServiceRuntimeException.class.equals(root.getClass())) {
				// Special handling for SRE (not including subclasses) to get
				// service context right
				ServiceRuntimeException sre = (ServiceRuntimeException) root;
				result = new ServiceRuntimeException(root.getMessage(), cause, sre.getServiceContext());
			} else if (safeClass) {
				result = makeNewInstanceOfSafeClass(root, cause);
			} else {
				result = new ServiceRuntimeException(ErrorHelperUtil.convertSBEStackTrace((Exception) root));

			}
		}
		result.setStackTrace(EMPTY_STACK_TRACE_ELEMENTS);
		return result;
	}

	private Throwable makeNewInstanceOfSafeClass(Throwable t, Throwable cause) {
		try {
			Class<? extends Throwable> throwableClass = t.getClass();
			String message = t.getMessage();

			Throwable result;
			if (message != null) {
				try {
					result = throwableClass.getConstructor(String.class).newInstance(message);
				} catch (NoSuchMethodException nsme) {
					// Constructor taking string (message) as argument not found
					// Fallback to the public empty constructor that any
					// serializable class must have
					result = throwableClass.newInstance();
				}
			} else {
				result = throwableClass.newInstance();
			}
			if (cause != null) {
				result.initCause(cause);
			}
			return result;
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			// Wrap any non-runtime exceptions in runtime exception
			throw new RuntimeException(e);
		}
	}

	private boolean isSafeClass(Throwable t) {
		if (t instanceof ServiceRuntimeException) {
			return true;
		} else {
			Package p = t.getClass().getPackage();
			if (p != null && p.getName().startsWith("java.")) {
				return true;
			} else {
				return false;
			}
		}
	}
}