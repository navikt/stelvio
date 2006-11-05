package no.stelvio.common.monitor;

import no.stelvio.common.config.ConfigurationException;
import no.stelvio.common.service.ServiceFailedException;

/**
 * 
 * @author person356941106810, Accenture
 */
public class ErrorMonitor implements Manager {

	// holds the destination of the reports
	private ReportReceiver reportDestination = null;

	// holds the name of the report to log with the destination
	private String reportName = null;

	// the number of measurements to track
	private int measurementPool = 0;

	// the number of measurements which must fail
	private int measurementTarget = 0;

	// keeps track of the current number of errors
	private long currentCount = 0;

	// keeps track of the current number calls
	private long currentNumCalls = 0;

	// the ratio of calls which must fail
	private double errorRatio = 0.0d;

	// the current error ratio
	private double currentRatio = 0.0d;

	// the error message 
	private String errorMessage = null;

	// wether or not the management feature is turned off or on
	private boolean manage = true;

	/**
	 * Initializes the component and performs validation
	 */
	public void init() {
		if (reportDestination == null) {
			throw new ConfigurationException("reportDestination");
		}

		if (reportName == null) {
			throw new ConfigurationException("reportName");
		}
		if (measurementPool <= 0) {
			manage = false;
		} else {
			if (measurementTarget < 0 || measurementTarget >= measurementPool) {
				throw new ConfigurationException("measurementPool");
			}
			// calculate target ratio
			errorRatio = ((double) measurementTarget) / ((double) measurementPool);
		}

	}

	/**
	 * Performs managing activities before the monitor starts and before the actual method call
	 * to manage.
	 * 
	 * Checks if the error threshold has been reaced. If it has, an exception is thrown
	 * before the service is called.
	 * @param event the event to manage
	 * @throws ServiceFailedException the error threshold has been passed.
	 */
	public void preManage(MonitorEvent event) throws ServiceFailedException {
		// make sure at least the defined number of calls have been executed
		if (manage && currentNumCalls >= measurementPool) {
			currentRatio = ((double) currentCount) / ((double) currentNumCalls);
			if (currentRatio >= errorRatio) {
				if (errorMessage != null) {
					throw new ServiceFailedException(errorMessage);
				} else {
					throw new ServiceFailedException();
				}
			}
		}

	}

	/**
	 * Performs managing activited after the monitor has finished monitoring the method call and
	 * after the actual method call to manage.
	 * 
	 * This method has no purpose in ErrorMonitor
	 * @param event the event to manage
	 * @throws ServiceFailedException a condition which calls for a exception was met
	 */
	public void postManage(MonitorEvent event) throws ServiceFailedException {
		// Do nothing
	}

	/**
	 * Performs monitoring activities before the actual method call to the service.
	 * 
	 * @param event the event to monitor
	 * @see no.stelvio.common.monitor.Monitor#preMonitor(no.stelvio.common.monitor.MonitorEvent)
	 */
	public void preMonitor(MonitorEvent event) {
		// synchronize on something other than myself
		synchronized (reportName) {
			currentNumCalls++;
		}
	}

	/**
	 * Performs monitoring activities after the actual method call to the service.
	 * 
	 * This method keeps a track of all exceptions thrown from a service in order to check
	 * of the error threshold has been met
	 * @param event the event to monitor.
	 * @see no.stelvio.common.monitor.Monitor#postMonitor(no.stelvio.common.monitor.MonitorEvent)
	 */
	public void postMonitor(MonitorEvent event) {
		if (event.getException() != null) {
			synchronized (reportName) {
				currentCount++;
			}
		}
		Object[] reportValues = new Object[3];
		// number of calls
		reportValues[0] = currentNumCalls;
		// number of errors
		reportValues[1] = currentCount;
		// current ratio
		reportValues[2] = currentRatio;
		reportDestination.report(reportName, reportValues);
	}

	/**
	 * Sets the error message that should be a part of the exception when the service is disabled.
	 * @param string the error message
	 */
	public void setErrorMessage(String string) {
		errorMessage = string;
	}

	/**
	 * Sets the number of calls the error ratio should be measured over 
	 * @param i the pool
	 */
	public void setMeasurementPool(int i) {
		measurementPool = i;
	}

	/**
	 * Sets the number of calls which must fail before this service is disabled
	 * @param i number of calls
	 */
	public void setMeasurementTarget(int i) {
		measurementTarget = i;
	}

	/**
	 * Sets the MBean which should receive the reports from this monitor/manager.
	 * @param bean the report destination.
	 */
	public void setReportDestination(ReportReceiver bean) {
		reportDestination = bean;
	}

	/**
	 * Sets the name of the report which should be given to the MBean
	 * @param string the report name.
	 */
	public void setReportName(String string) {
		reportName = string;
	}

}
