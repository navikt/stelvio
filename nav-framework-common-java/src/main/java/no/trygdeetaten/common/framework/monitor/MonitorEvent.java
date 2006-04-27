package no.trygdeetaten.common.framework.monitor;

/**
 * Contains information about the event beinge monitored
 * 
 * @author person356941106810, Accenture
 */
public class MonitorEvent {
	
	/** Holds information about any errors that have ocurred */
	private Exception exception = null;
	
	/** Holds the execution time time for preMonitor */
	private long preMonitorTime = 0l;
	
	/** Holds the execution time for postMonitor */
	private long postMonitorTime = 0l;
	
	/** Holds the execution time for preManage */
	private long preManageTime = 0l;
	
	/** Holds the execution time for postManage */
	private long postManageTime = 0l;
	
	
	/**
	 * Returns the exception associated with this monitor event (if any)
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * Returns the accumulated time spent in postManage
	 * @return the execution time
	 */
	public long getPostManageTime() {
		return postManageTime;
	}

	/**
	 * Returns the accumulated  time spent in postMonitor
	 * @return the execution time
	 */
	public long getPostMonitorTime() {
		return postMonitorTime;
	}

	/**
	 * Returns the accumulated time spent in preManage
	 * @return the execution time
	 */
	public long getPreManageTime() {
		return preManageTime;
	}

	/**
	 * Returns the accumulated time spent in preMonitor
	 * @return the execution time
	 */
	public long getPreMonitorTime() {
		return preMonitorTime;
	}

	/**
	 * Sets the time spent in postManage. This should be the accumulated time
	 * spent in all the monitors in the same chain.
	 * @param l the accumulated execution time
	 */
	public void setPostManageTime(long l) {
		postManageTime = l;
	}

	/**
	 * Sets the time spent in postMonitor. This should be the accumulated time
	 * spent in all the monitors in the same chain.
	 * @param l the accumulated execution time
	 */
	public void setPostMonitorTime(long l) {
		postMonitorTime = l;
	}

	/**
	 * Sets the time spent in preManage. This should be the accumulated time
	 * spent in all the monitors in the same chain.
	 * @param l the accumulated execution time
	 */
	public void setPreManageTime(long l) {
		preManageTime = l;
	}

	/**
	 * Sets the time spent in preMonitor. This should be the accumulated time
	 * spent in all the monitors in the same chain.
	 * @param l the accumulated execution time
	 */
	public void setPreMonitorTime(long l) {
		preMonitorTime = l;
	}

	/**
	 * Sets the exception which was cause by the service exectuting
	 * @param exception the exception
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}

}
