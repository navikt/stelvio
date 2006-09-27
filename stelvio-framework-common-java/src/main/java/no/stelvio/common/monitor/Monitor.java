package no.stelvio.common.monitor;

/**
 * Interface which defines a monitor. All Monitor implementations in a MonitorChain must 
 * implement this interface.
 * 
 * @author ted.sanne
 */
public interface Monitor {
	
	/**
	 * Performs monitoring activities before the actual method call to the service. 
	 * @param event the MonitorEvent
	 */
	void preMonitor(MonitorEvent event);
	
	/**
	 * Performs monitoring activities after the actual method call to the service.
	 * @param event the MonitorEvent
	 */
	void postMonitor(MonitorEvent event);
}
