package no.trygdeetaten.common.framework.performance;

/**
 * Monitor implementation that is not monitoring at all.
 * This class should be used when performance is essential,
 * and no monitoring or logging is required.
 *
 * @author person7553f5959484
 * @version $Revision: 1130 $ $Author: psa2920 $ $Date: 2004-08-19 17:39:09 +0200 (Thu, 19 Aug 2004) $
 */
public final class DefaultMonitorImpl extends AbstractMonitor {

	/**
	 * @see no.trygdeetaten.common.framework.performance.Monitor#start(no.trygdeetaten.common.framework.performance.MonitorKey)
	 */
	public void start(MonitorKey key) {
		return; // Do Nothing!
	}

	/**
	 * @see no.trygdeetaten.common.framework.performance.Monitor#end(no.trygdeetaten.common.framework.performance.MonitorKey)
	 */
	public void end(MonitorKey key) {
		return; // Do Nothing!
	}

	/**
	 * @see no.trygdeetaten.common.framework.performance.Monitor#fail(no.trygdeetaten.common.framework.performance.MonitorKey)
	 */
	public void fail(MonitorKey key) {
		return; // Do Nothing!
	}

	/**
	 * @see no.trygdeetaten.common.framework.performance.Monitor#start(no.trygdeetaten.common.framework.performance.MonitorKey, java.lang.String)
	 */
	public void start(MonitorKey key, String contextName) {
		return; // Do Nothing!
	}

	/**
	 * @see no.trygdeetaten.common.framework.performance.Monitor#fail(no.trygdeetaten.common.framework.performance.MonitorKey, int)
	 */
	public void fail(MonitorKey key, int line) {
		return; // Do Nothing!
	}
}
