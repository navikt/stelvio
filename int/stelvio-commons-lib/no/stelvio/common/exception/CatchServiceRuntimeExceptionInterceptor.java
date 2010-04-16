/**
 * 
 */
package no.stelvio.common.exception;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import no.stelvio.common.interceptor.Interceptor;
import no.stelvio.common.interceptor.InterceptorChain;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;
import com.ibm.websphere.sca.scdl.OperationType;

/**
 * Interceptor implementation that transforms service runtime exceptions (root and nested) so that the exception can safely
 * propagate through the integration platform (bus) without any marshalling problems.
 * 
 * This class is typically used in the producer layer as an "exception shield" towards the unrelying service.
 * 
 * @author test@example.com
 */
public class CatchServiceRuntimeExceptionInterceptor implements Interceptor {
	public Object doIntercept(OperationType operationType, Object input, InterceptorChain interceptorChain) {
		try {
			return interceptorChain.doIntercept(operationType, input);
		} catch (ServiceRuntimeException sre) {
			throw (ServiceRuntimeException) convertThrowable(sre);
		}
	}

	public Throwable convertThrowable(Throwable t) {
		Throwable cause = t.getCause();
		Throwable newCause = null;
		if (cause != null) {
			newCause = convertThrowable(cause);
		}
		if (isSafeClass(t)) {
			if (newCause == null || newCause == cause) {
				return t;
			} else {
				return makeNewInstanceOfSafeClass(t, newCause);
			}
		} else {
			return makeNewInstanceOfUnsafeClass(t, newCause);
		}
	}

	private boolean isSafeClass(Throwable t) {
		if (t instanceof ServiceRuntimeException || t instanceof ServiceBusinessException) {
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

	private Throwable makeNewInstanceOfUnsafeClass(Throwable t, Throwable cause) {
		Throwable result;
		String message = t.getMessage();
		if (message != null) {
			result = new RuntimeException(t.getClass().getName() + " : " + t.getMessage(), cause);
		} else {
			result = new RuntimeException(t.getClass().getName(), cause);
		}
		result.setStackTrace(t.getStackTrace());
		return result;
	}

	private Throwable makeNewInstanceOfSafeClass(Throwable t, Throwable cause) {
		Class<? extends Throwable> throwableClass = t.getClass();
		String message = t.getMessage();

		Throwable result;

		if (ServiceBusinessException.class.equals(throwableClass)) {
			ServiceBusinessException sbe = (ServiceBusinessException) t;
			ServiceBusinessException convertedSbe = new ServiceBusinessException(sbe.getData());

			// Set cause
			convertedSbe.initCause(cause);

			result = convertedSbe;
		} else if (ServiceRuntimeException.class.equals(throwableClass)) {
			ServiceRuntimeException sre = (ServiceRuntimeException) t;
			result = new ServiceRuntimeException(message, cause, sre.getServiceContext());
		} else {
			result = newInstance(throwableClass, message, cause);
		}

		// Set stack trace
		result.setStackTrace(t.getStackTrace());

		return result;
	}

	private Throwable newInstance(Class<? extends Throwable> throwableClass, final String message, final Throwable cause) {
		try {
			List<Constructor<? extends Throwable>> constructors = getConstructorCandidates(throwableClass);

			sortConstructors(constructors);

			Constructor<? extends Throwable> constructor = constructors.iterator().next();
			Class[] parameterTypes = constructor.getParameterTypes();
			Object[] parameters = new Object[parameterTypes.length];
			for (int i = 0; i < parameterTypes.length; i++) {
				Class parameterType = parameterTypes[i];
				if (String.class.isAssignableFrom(parameterType)) {
					parameters[i] = message;
				} else if (Throwable.class.isAssignableFrom(parameterType)) {
					parameters[i] = cause;
				}
			}
			return constructor.newInstance(parameters);
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			// Wrap any non-runtime exceptions in runtime exception
			throw new RuntimeException(e);
		}
	}

	private List<Constructor<? extends Throwable>> getConstructorCandidates(Class<? extends Throwable> throwableClass) {
		List<Constructor<? extends Throwable>> constructors = new ArrayList<Constructor<? extends Throwable>>();

		for (Constructor<? extends Throwable> constructor : (Constructor<? extends Throwable>[]) throwableClass
				.getConstructors()) {
			Class[] parameterTypes = constructor.getParameterTypes();
			switch (parameterTypes.length) {
			case 0:
				constructors.add(constructor);
				break;
			case 1:
				Class parameterType = parameterTypes[0];
				if (String.class.isAssignableFrom(parameterType) || Throwable.class.isAssignableFrom(parameterType)) {
					constructors.add(constructor);
				}
				break;
			case 2:
				Class parameterType1 = parameterTypes[0];
				Class parameterType2 = parameterTypes[1];
				if (String.class.isAssignableFrom(parameterType1) && Throwable.class.isAssignableFrom(parameterType2)
						|| Throwable.class.isAssignableFrom(parameterType1) && String.class.isAssignableFrom(parameterType2)) {
					constructors.add(constructor);
				}
				break;
			}
		}

		return constructors;
	}

	private void sortConstructors(List<Constructor<? extends Throwable>> constructors) {
		Collections.sort(constructors, new Comparator<Constructor<? extends Throwable>>() {
			public int compare(Constructor<? extends Throwable> constructor1, Constructor<? extends Throwable> constructor2) {
				Class<?>[] parameterTypes1 = constructor2.getParameterTypes();
				Class<?>[] parameterTypes2 = constructor1.getParameterTypes();
				int lengthDiff = parameterTypes1.length - parameterTypes2.length;
				if (lengthDiff != 0) {
					return lengthDiff;
				}
				// Equal length of parameter types
				if (parameterTypes1.length == 1) {
					return String.class.isAssignableFrom(parameterTypes1[0]) ? -1 : 1;
				}
				return 0;
			}
		});
	}
}