package no.stelvio.common.framework.monitor;

/**
 * Common interface for all monitoring report receivers.
 * 
 * @author person356941106810, Accenture
 */
public interface ReportReceiver {

	/**
	 * Submits a report to the Receiver. Each report is implementation specific.
	 * @param reportName the name of the report submitted
	 * @param values the values reported
	 */
	void report( String reportName, Object[] values );
}
