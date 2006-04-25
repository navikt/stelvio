package no.trygdeetaten.common.framework.performance;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.config.Config;
import no.trygdeetaten.common.framework.config.ConfigurationException;
import no.trygdeetaten.common.framework.error.ErrorHandler;
import no.trygdeetaten.common.framework.error.SystemException;

/**
 * PerformanceMonitor is the facade for all performance monitoring,
 * and one of the only public classes in Performance Monitoring.
 * <p/>
 * PerformanceMonitor is implemented as a thread safe Singleton.
 *
 * @author person7553f5959484
 * @version $Revision: 1750 $ $Author: skb2930 $ $Date: 2005-01-04 08:45:02 +0100 (Tue, 04 Jan 2005) $
 * @see no.trygdeetaten.common.framework.performance.Monitor
 */
public final class PerformanceMonitor {

	// The performance monitoring configuration file
	private static final String CONFIGURATION_FILE = "performance-monitoring.xml";

	// The performance monitor configuration key
	private static final String MONITOR_KEY = "PerformanceMonitor";

	//The PerformanceMonitor instance
	// initialize here: lazy instantiation is not thread-safe
	private static PerformanceMonitor theInstance = new PerformanceMonitor();

	// The current configured monitor
	private Config perfConfig;

	/**
	 * Private constructor
	 */
	private PerformanceMonitor() {

		try {
			perfConfig = Config.getConfig(CONFIGURATION_FILE);
		} catch (Throwable t) {
			try {
				throw new ConfigurationException(FrameworkError.PERFORMANCE_CONFIGURATION_ERROR, t, CONFIGURATION_FILE);
			} catch (ConfigurationException ce) {
				throw (SystemException) ErrorHandler.handleError(ce);
			}
		}
	}

	/**
	 * Start monitoring a unit of work.
	 * 
	 * @param key monitor key
	 */
	public static void start(MonitorKey key) {
		Monitor mon = (Monitor) theInstance.perfConfig.getBean(MONITOR_KEY);
		mon.start(key);
	}

	/**
	 * End monitoring of current unit of work.
	 * 
	 * @param key monitor key
	 */
	public static void end(MonitorKey key) {
		Monitor mon = (Monitor) theInstance.perfConfig.getBean(MONITOR_KEY);
		mon.end(key);
	}

	/**
	 * Fail monitoring of current unit of work.
	 * 
	 * @param key monitor key
	 */
	public static void fail(MonitorKey key) {
		Monitor mon = (Monitor) theInstance.perfConfig.getBean(MONITOR_KEY);
		mon.fail(key);
	}

	/**
	 * Fail monitoring of current unit of work.
	 * 
	 * @param key monitor key
	 * @param line line
	 */
	public static void fail(MonitorKey key, int line) {
		Monitor mon = (Monitor) theInstance.perfConfig.getBean(MONITOR_KEY);
		mon.fail(key, line);
	}

	/**
	 * Start monitoring a unit of work.
	 * 
	 * @param key monitor key
	 * @param contextName context name
	 */
	public static void start(MonitorKey key, String contextName) {
		Monitor mon = (Monitor) theInstance.perfConfig.getBean(MONITOR_KEY);
		mon.start(key, contextName);
	}

}