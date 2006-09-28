package no.stelvio.common.monitor;

import no.stelvio.common.service.ServiceFailedException;

/**
 * Interface which defines a Manager. All implementations in a MonitorChain which
 * should have automatic managing to trigger on some monitored value must implement
 * this interface.
 * 
 * @author person356941106810, Accenture
 */
public interface Manager extends Monitor {
	
	/**
	 * Performs managing activities before the monitor starts and before the actual method call
	 * to manage.
	 *
	 * @param event the MonitorEvent
	 * @throws ServiceFailedException a condition which calls for a exception was met
	 */
	void preManage(MonitorEvent event) throws ServiceFailedException;
	
	/**
	 * Performs managing activited after the monitor has finished monitoring the method call and
	 * after the actual method call to manage.
	 * 
	 * @param event the MonitorEvent
	 * @throws ServiceFailedException a condition which calls for a exception was met
	 */
	void postManage(MonitorEvent event) throws ServiceFailedException;
}
