package no.nav.common.framework.monitor;

import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.error.SystemException;
import no.nav.common.framework.service.ServiceFailedException;

/**
 * This class holds one or more Monitor instances which will be triggered if monitoring is
 * configured.
 * 
 * @author person356941106810, Accenture
 */
public class MonitorChain {

	/** All the monitors that are configured for this chain */
	private Monitor[] monitors = null;

	/** All the managers that are configured for this chain */
	private Manager[] managers = null;

	/**
	 * Performs initialization and validation of the configuration.
	 */
	public void init() {
		if (monitors == null || monitors.length == 0) {
			throw new SystemException(FrameworkError.MONITORING_CHAIN_NO_MONITORS_ERROR);
		}

		// loop through all monitors and check if they are a manager also
		managers = new Manager[monitors.length];
		boolean anyManagers = false;

		for (int i = 0; i < monitors.length; i++) {
			if (monitors[i] instanceof Manager) {
				managers[i] = (Manager) monitors[i];
				anyManagers = true;
			} else {
				managers[i] = null;
			}
		}

		if (!anyManagers) {
			managers = null;
		}
	}

	/**
	 * Executes all pre-monitoring and pre-managing activities for all monitors
	 * configured for this MonitorChain.
	 * @param event the event to monitor or manage
	 * @throws ServiceFailedException a managing condition was triggered which called for an exception
	 */
	public void preExecute(MonitorEvent event) throws ServiceFailedException {
		int length = monitors.length;
		long startTime;

		// first execute all managers
		if (managers != null) {
			startTime = System.currentTimeMillis();

			for (int i = 0; i < length; i++) {
				if (managers[i] != null) {
					managers[i].preManage(event);
				}
			}

			event.setPreManageTime(System.currentTimeMillis() - startTime);
		}

		// execute all monitors
		startTime = System.currentTimeMillis();

		for (int i = 0; i < length; i++) {
			monitors[i].preMonitor(event);
		}

		event.setPreMonitorTime(System.currentTimeMillis() - startTime);
	}

	/**
	 * Executes all post-monitoring and post-managing activities for all monitors
	 * configured for this MonitorChain.
	 * @param event the event to monitor
	 * @throws ServiceFailedException a condition which calls for a exception was met
	 */
	public void postExecute(MonitorEvent event) throws ServiceFailedException {
		int length = monitors.length;
		// execute all monitors
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < length; i++) {
			monitors[i].postMonitor(event);
		}

		event.setPostMonitorTime(System.currentTimeMillis() - startTime);

		// execute all managers
		if (managers != null) {
			startTime = System.currentTimeMillis();

			for (int i = 0; i < length && managers != null; i++) {
				if (managers[i] != null) {
					managers[i].postManage(event);
				}
			}
			
			event.setPostManageTime(System.currentTimeMillis() - startTime);
		}
	}

	/**
	 * Sets all the monitors this MonitorChain should execute
	 * @param monitors the monitors which form the chain
	 */
	public void setMonitors(Monitor[] monitors) {
		this.monitors = (Monitor[]) monitors.clone();
	}

}
