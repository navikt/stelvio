package no.stelvio.common.framework.performance;

/**
 * Monitor is the interface that all monitors used in
 * Performance Monitoring must implement.
 *
 * @author person7553f5959484
 * @version $Revision: 192 $ $Author: psa2920 $ $Date: 2004-05-13 17:42:12 +0200 (Thu, 13 May 2004) $
 */
interface Monitor {

	/**
	 * Start of transaction
	 * @param key String used with transaction.
	 */
	void start(MonitorKey key);

	/**
	 * End of transaction
	 * @param key String used with transaction.
	 */
	void end(MonitorKey key);

	/**
	 * Fail of transaction
	 * @param key String used with transaction.
	 */
	void fail(MonitorKey key);
	
	/**
	 * Fail of transaction
	 * @param key String used with transaction.
	 * @param line The line number of the call
	 */
	void fail(MonitorKey key, int line);

	/**
	 * Start of transaction
	 * @param key MonitorKey used with transaction.
	 * @param contextName aditional description.
	 */
	void start(MonitorKey key, String contextName);
}