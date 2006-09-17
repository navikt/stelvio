package no.stelvio.common.framework.jmx;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory to be used to ensure statistics integrity within one JVM instance.
 * 
 * @author person7553f5959484
 * @version $Id: ServiceLevelMBeanFactory.java 2608 2005-11-03 08:34:46Z skb2930 $
 */
public final class ServiceLevelMBeanFactory {
	private static final Map mbeans = new HashMap();

	/**
	 * Retrieves a <code>ServiceLevelMBean</code> from the cache or creates a new one if not in the cache.
	 *
	 * @param serviceType the service type.
	 * @param serviceName the service name.
	 * @return the only instance of the specific MBean within current JVM.
	 */
	public static ServiceLevelMBean getMBean(String serviceType, String serviceName) {
		ServiceLevelMBean mbean;

		// Must synchronize all access to the global cache to ensure it is synchronized between two threads
		// This is a consequence of Java's memory model [JLS, 17]
		// Wont synchronize on the method since the creation of ServiceLevelMBean can be quite expensive
		synchronized (mbeans) {
			mbean = (ServiceLevelMBean) mbeans.get(serviceType + ":" + serviceName);
		}

		if (null == mbean) {
			mbean = new ServiceLevelMBean(serviceType, serviceName);

			// See previous comment
			synchronized (mbeans) {
				mbeans.put(serviceType + ":" + serviceName, mbean);
			}
		}

		return mbean;
	}

}
