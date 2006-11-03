package no.stelvio.common.jmx;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Interception around advice that reports service level statistics.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: ServiceLevelStatisticsMethodInterceptor.java 2603 2005-11-01 17:31:59Z psa2920 $
 */
public class ServiceLevelStatisticsMethodInterceptor implements MethodInterceptor {

	/** Holds the <code>ServiceLevelMBean</code> to use. */
	private ServiceLevelMBean serviceLevelMBean = null;

	/**
	 * Constructs the advice with specified service name.
	 * 
	 * @param serviceName the service name.
	 */
	public ServiceLevelStatisticsMethodInterceptor(String serviceName) {
		serviceLevelMBean = ServiceLevelMBeanFactory.getMBean(ServiceLevelConstants.SERVICE_TYPE_INTEGRATION, serviceName);
	}

	/**
	 * Wrapps method invocation with performance statistics.
	 * 
	 * {@inheritDoc}
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {

		long start = System.currentTimeMillis();
		try {
			Object result = methodInvocation.proceed();
			serviceLevelMBean.add(System.currentTimeMillis() - start, null);
			return result;
		} catch (Throwable t) {
			serviceLevelMBean.add(System.currentTimeMillis() - start, t);
			throw t;
		}
	}
}
