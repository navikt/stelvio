package no.stelvio.common.error.logging;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.ThrowsAdvice;

/**
 * 
 * Interceptor used to log exceptions
 * 
 * Wraps the {@link ExceptionLogger} to enable logging by using AOP.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class ExceptionLoggerInterceptor implements ThrowsAdvice {

	private ExceptionLogger exceptionLogger;

	private static final Log LOG = LogFactory.getLog(ExceptionLoggerInterceptor.class);

	/**
	 * Method that is called after a component that is intercepted throws an exception.
	 * 
	 * @param method
	 *            the method that was interceptod
	 * @param args
	 *            the argument passed to the intercepted method
	 * @param target
	 *            the target that was intercepted
	 * @param ex
	 *            exception
	 */
	public void afterThrowing(Method method, Object[] args, Object target, Throwable ex) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Intercepted throwable of type '" + ex.getClass().getName() + "' thrown by target class '"
						+ target.getClass().getName() + "' and method '" + method.getName() + "'");
			}
			exceptionLogger.log(ex);
		} catch (Throwable t2) {
			System.err.println("Unable to log exception. Exception thrown while attempting to log :");
			System.err.println(t2.getMessage());
			t2.printStackTrace(System.err);
			System.err.println("Exception that should have been logged:");
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}

	/**
	 * Set exception logger.
	 * 
	 * @param exceptionLogger
	 *            exception logger
	 */
	public void setExceptionLogger(ExceptionLogger exceptionLogger) {
		this.exceptionLogger = exceptionLogger;
	}
}
