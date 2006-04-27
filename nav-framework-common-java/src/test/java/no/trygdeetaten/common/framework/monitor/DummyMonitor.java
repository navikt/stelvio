package no.trygdeetaten.common.framework.monitor;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DummyMonitor implements Monitor {

	public boolean preMonitor = false;
	public boolean postMonitor = false;
	
	public void preMonitor(MonitorEvent event) {
		preMonitor = true;
	}

	public void postMonitor(MonitorEvent event) {
		postMonitor = true;
	}

}
