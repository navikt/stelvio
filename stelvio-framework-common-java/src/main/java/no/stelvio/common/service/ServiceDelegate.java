package no.stelvio.common.service;

import no.stelvio.common.performance.MonitorKey;

/**
 * ServiceDelegate is a generic interface to be implemented by delegates
 * that needs to lookup services using a <i>Service Locator</i>.
 * 
 * @author person7553f5959484
 * @version $Id: ServiceDelegate.java 1949 2005-02-08 11:33:09Z psa2920 $
 */
public interface ServiceDelegate extends LocalService {

	/**
	 * Assign an implementation of the <i>Service Locator</i> to this instance.
	 * 
	 * @param serviceLocator the <i>Service Locator</i> instance.
	 */
	void setServiceLocator(ServiceLocator serviceLocator);

	/**
	 * Sets the name of the service facade to use.
	 * 
	 * @param serviceFacadeName the name of the service facade 
	 */
	void setServiceFacadeName(String serviceFacadeName);

	/**
	 * Sets the monitor key to use for this delegate
	 * 
	 * @param key the monitor key.
	 */
	void setDelegateMonitorKey(MonitorKey key);
}