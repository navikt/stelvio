package no.stelvio.common.monitor;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.monitor.ErrorMonitor;
import no.stelvio.common.monitor.MonitorEvent;
import no.stelvio.common.monitor.ReportReceiver;
import no.stelvio.common.service.ServiceFailedException;

import junit.framework.TestCase;

/**
 * 
 * @author person356941106810, Accenture
 */
public class ErrorMonitorTest extends TestCase {

	/**
	 * Constructor for ErrorMonitorTest.
	 * @param arg0
	 */
	public ErrorMonitorTest(String arg0) {
		super(arg0);
	}

	public void testInit() {
		try {
			ErrorMonitor monitor = new ErrorMonitor();
			// TEST: no report destination has been set
			boolean ex = false;
			try {
				monitor.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals("Test 1", FrameworkError.MONITORING_NO_REPORT_DESTINATION_ERROR.getCode(), e.getErrorCode());

			}
			assertTrue("Test 2", ex);
			ex = false;

			// TEST: the report name has not been set
			ReportReceiver mbean = new ReportReceiver() {
				public void report(String reportName, Object[] values) {
				}
			};

			monitor.setReportDestination(mbean);
			try {
				monitor.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals("Test 3", FrameworkError.MONITORING_NO_REPORT_NAME_ERROR.getCode(), e.getErrorCode());

			}
			assertTrue("Test 4", ex);
			ex = false;

			// TEST: measuremeant pool and target is not set correctly
			monitor.setReportName("TestReport");
			monitor.setMeasurementPool(1);
			monitor.setMeasurementTarget(2);
			try {
				monitor.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals("Test 5", FrameworkError.MONITORING_ERROR_MEASUREMENT_ERROR.getCode(), e.getErrorCode());

			}
			assertTrue("Test 6", ex);
			ex = false;

			// TEST: all ok
			monitor.setMeasurementPool(10);
			monitor.setMeasurementTarget(9);
			monitor.init();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexptected exception:" + e.getMessage());
		}
	}

	public void testPreAndPostManageAndPreAndPostMonitor() {
		try {
			// TEST: nothing happens
			ErrorMonitor monitor = new ErrorMonitor();
			ReportReceiver mbean = new ReportReceiver() {
				public void report(String reportName, Object[] values) {
				}
			};
			monitor.setReportDestination(mbean);
			monitor.setErrorMessage("Test error");
			monitor.setMeasurementPool(5);
			monitor.setMeasurementTarget(3);
			monitor.setReportName("Test report");
			monitor.init();
			MonitorEvent event = new MonitorEvent();

			monitor.preManage(event);

			// TEST: we get an exception because the error ratio has been exceeded
			// make sure the threshold is reached
			for (int i = 0; i < 5; i++) {
				monitor.preMonitor(event);
			}
			event = new MonitorEvent();
			event.setException(new Exception());
			for (int i = 0; i < 3; i++) {
				monitor.postMonitor(event);
			}
			boolean ex = false;
			try {
				monitor.preManage(event);
			} catch (ServiceFailedException e) {
				ex = true;
				assertEquals("Test 1", FrameworkError.MONITORING_ERROR_SERVICE_DISABLED.getCode(), e.getErrorCode());
			}
			assertTrue("Test 2", ex);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexptected exception:" + e.getMessage());
		}

	}
}
