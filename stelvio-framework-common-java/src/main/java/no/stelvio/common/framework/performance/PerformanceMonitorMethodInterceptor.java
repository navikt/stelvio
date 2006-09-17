package no.stelvio.common.framework.performance;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Interception around advice that monitors method invocation.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: PerformanceMonitorMethodInterceptor.java 2740 2006-01-19 12:27:41Z skb2930 $
 */
public class PerformanceMonitorMethodInterceptor implements MethodInterceptor {

	/** The monitoring key. */
	private MonitorKey monitorKey = null;

	/**
	 * Constructs the advice with specified performance monitoring key.
	 * 
	 * @param monitorKey the monitoring key.
	 */
	public PerformanceMonitorMethodInterceptor(MonitorKey monitorKey) {
		this.monitorKey = monitorKey;
	}

	/**
	 * Wrapps method invocation with starting and ending or failing performance monitoring.
	 * 
	 * {@inheritDoc}
	 */
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {

		PerformanceMonitor.start(monitorKey, methodInvocation.getMethod().getName());
		try {
			Object result = methodInvocation.proceed();
			PerformanceMonitor.end(monitorKey);
			return result;
		} catch (Throwable t) {
			PerformanceMonitor.fail(monitorKey);
			throw t;
		}
	}
}
