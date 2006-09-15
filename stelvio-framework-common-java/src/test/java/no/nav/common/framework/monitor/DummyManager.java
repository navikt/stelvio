package no.nav.common.framework.monitor;

import no.nav.common.framework.monitor.Manager;
import no.nav.common.framework.monitor.MonitorEvent;
import no.nav.common.framework.service.ServiceFailedException;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyManager implements Manager {
	
	public boolean preManage = false;
	public boolean preMonitor = false;
	public boolean postManage = false;
	public boolean postMonitor = false;
	
	
	public void preManage(MonitorEvent event) throws ServiceFailedException {
		preManage = true;
	}

	public void postManage(MonitorEvent event) throws ServiceFailedException {
		postManage = true;
	}

	public void preMonitor(MonitorEvent event) {
		preMonitor = true;
	}

	public void postMonitor(MonitorEvent event) {
		postMonitor = true;
	}

}
