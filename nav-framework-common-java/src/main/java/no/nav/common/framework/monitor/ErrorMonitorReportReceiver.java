package no.nav.common.framework.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.error.SystemException;

/**
 * This MBean gathers information from all ErrorMonitors.
 * @author person356941106810, Accenture
 */
public class ErrorMonitorReportReceiver implements ErrorMonitorReportReceiverMBean {
	private final static Log log = LogFactory.getLog(ErrorMonitorReportReceiver.class);

	// the number of reports which should stored at any given time
	private int reporterSize = 1000;

	// the MBean ObjectName of this MBean
	private String objectName = null;

	// holds all the error reports
	private HashMap errorReports = null;

	/**
	 * Performs initialization and validation of the configuration for this MBean.
	 */
	public void init() {
		if (reporterSize <= 0) {
			throw new SystemException(FrameworkError.MONITORING_ERROR_INVALID_REPORT_SIZE_ERROR);
		}

		if (objectName == null) {
			throw new SystemException(FrameworkError.MONITORING_ERROR_INVALID_OBJECT_NAME_ERROR);
		}

		ArrayList servers = MBeanServerFactory.findMBeanServer(null);
		MBeanServer server = null;

		if (servers == null || servers.size() == 0) {
			server = MBeanServerFactory.createMBeanServer();
		} else {
			server = (MBeanServer) servers.get(0);
		}

		ObjectName name = null;

		try {
			name = new ObjectName("Monitoring:name=" + objectName);
		} catch (MalformedObjectNameException e) {
			throw new SystemException(FrameworkError.MONITORING_ERROR_INVALID_OBJECT_NAME_ERROR, e, objectName);
		}

		if (!server.isRegistered(name)) {
			try {
				server.registerMBean(this, name);
			} catch (InstanceAlreadyExistsException iae) {
				// Defect #2984 As we have a cluster, we cannot guarantee that the MBean is not registered between the check
				// and our registering, so we only log this
				if (log.isDebugEnabled()) {
					log.debug("Failed to register service for management", iae);
				}
			} catch (MBeanRegistrationException e) {
				throw new SystemException(FrameworkError.MONITORING_ERROR_MBEAN_REGISTRATION_ERROR, e);
			} catch (NotCompliantMBeanException e) {
				throw new SystemException(FrameworkError.MONITORING_ERROR_MBEAN_REGISTRATION_ERROR, e);
			}
		}

		errorReports = new HashMap(reporterSize);
	}

	/**
	 * Reports the execution results of an ErrorMonitor.
	 * Three values are expected:
	 * <ul>
	 * 	<li>0 - The current number of calls registered</li>
	 * 	<li>1 - The current number of errors registered</li>
	 * 	<li>2 - The current call/error ratio</li>
	 * </ul>
	 * NB. This method is synchronized. This MIGHT cause a performance bottleneck.
	 * @param reportName the name of the report to log
	 * @param values the values which will be reported.
	 * 
	 * @see ReportReceiver#report(String, Object[])
	 */
	public synchronized void report(String reportName, Object[] values) {
		List reportList = (List) errorReports.get(reportName);

		if (reportList == null) {
			reportList = new ArrayList(reporterSize);
			errorReports.put(reportName, reportList);
		}

		if (reportList.size() >= reporterSize) {
			// remove the oldest item from the list
			reportList.remove(0);
		}

		reportList.add(createErrorReport(values));
	}

	/**
	 * Returns the MBean ObjectName this MBean is registered as.
	 * @return the MBean name
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * Returns the number of reports this MBean keeps track of
	 * @return the number of reports
	 */
	public int getReporterSize() {
		return reporterSize;
	}

	/**
	 * Sets the ObjectName the this MBean should be registrered as.
	 * @param string the name
	 */
	public void setObjectName(String string) {
		objectName = string;
	}

	/**
	 * Sets the number of reports this MBean keeps track of
	 * @param i the number of reports
	 */
	public void setReporterSize(int i) {
		reporterSize = i;
	}

	/**
	 * Retrieves the latest of all error reports
	 * @param reportName the name of the report to retrieve
	 * @return the latest error report
	 */
	public ErrorReport getLatestErrorReport(String reportName) {
		List reportList = (List) errorReports.get(reportName);
		ErrorReport report = null;
		if (reportList != null) {
			report = (ErrorReport) reportList.get(reportList.size() - 1);
		}
		return report;
	}

	/**
	 * Creates an <code>ErrorReport</code>.
	 *
	 * @param values the values to put into the <code>ErrorReport</code>.
	 * @return an instance of <code>ErrorReport</code>.
	 * @see ErrorReport
	 */
	private ErrorReport createErrorReport(final Object[] values) {
		ErrorReport report = new ErrorReport();
		
		report.setNumCalls(((Long) values[0]).longValue());
		report.setNumErrors(((Long) values[1]).longValue());
		report.setErrorRatio(((Double) values[2]).doubleValue());

		return report;
	}
}
