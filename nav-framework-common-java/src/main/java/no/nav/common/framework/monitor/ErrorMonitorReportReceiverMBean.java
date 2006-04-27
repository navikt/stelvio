package no.nav.common.framework.monitor;

/**
 * 
 * @author person356941106810, Accenture
 */
public interface ErrorMonitorReportReceiverMBean extends ReportReceiver {
	/**
	 * Returns the MBean ObjectName this MBean is registered as.
	 * @return the MBean name
	 */
	String getObjectName();
	/**
	 * Returns the number of reports this MBean keeps track of
	 * @return the number of reports
	 */
	int getReporterSize();
	/**
	 * Sets the ObjectName the this MBean should be registrered as.
	 * @param string the name
	 */
	void setObjectName(String string);
	/**
	 * Sets the number of reports this MBean keeps track of
	 * @param i the number of reports
	 */
	void setReporterSize(int i);
	
	/**
	 * Retreives the latest error report.
	 * @param reportName the name of the report to retrieve
	 * @return the latest error report
	 */
	ErrorReport getLatestErrorReport(String reportName);
}