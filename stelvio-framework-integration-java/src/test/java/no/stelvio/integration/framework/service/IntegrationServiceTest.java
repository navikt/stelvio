package no.stelvio.integration.framework.service;

import junit.framework.TestCase;

import no.stelvio.integration.framework.service.IntegrationService;
import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.error.ErrorCode;
import no.stelvio.common.framework.monitor.ErrorMonitor;
import no.stelvio.common.framework.monitor.Monitor;
import no.stelvio.common.framework.monitor.MonitorChain;
import no.stelvio.common.framework.monitor.ReportReceiver;
import no.stelvio.common.framework.service.ServiceFailedException;
import no.stelvio.common.framework.service.ServiceRequest;
import no.stelvio.common.framework.service.ServiceResponse;

/**
 * IntegrationService Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2575 $ $Author: psa2920 $ $Date: 2005-10-20 10:12:01 +0200 (Thu, 20 Oct 2005) $
 */
public class IntegrationServiceTest extends TestCase {

	/**
	 * Constructor for IntegrationServiceTest.
	 * @param arg0
	 */
	public IntegrationServiceTest(String arg0) {
		super(arg0);
	}

	public void test() {
		IntegrationService s = new TestIntegrationService();

		Object o = "value1";
		try {
			assertSame(
				"Objects in request and response should have been the same",
				o,
				s.execute(new ServiceRequest("service", "key", o)).getData("key"));
		} catch (ServiceFailedException e) {
			fail("execute() should not have thrown ServiceFailedException");
			e.printStackTrace();
		}

		try {
			s.execute(new ServiceRequest("service", "key", "ServiceFailedException"));
			fail("execute() should have thrown ServiceFailedException");
		} catch (ServiceFailedException e) {
			assertEquals(
				"execute() should have thrown UNSPECIFIED_ERROR",
				e.getErrorCode(),
				ErrorCode.UNSPECIFIED_ERROR.getCode());
		}

		try {
			s.execute(new ServiceRequest("service", "key", "RuntimeException"));
			fail("execute() should have thrown RuntimeException");
		} catch (Throwable t) {
			assertTrue("execute() should have thrown RuntimeException", t instanceof RuntimeException);
		}

		// Test monitoring
		ErrorMonitor monitor = new ErrorMonitor();
		monitor.setErrorMessage("Nei nå gikk det feil igjen for n'te gang");
		monitor.setMeasurementPool(3);
		monitor.setMeasurementTarget(2);
		monitor.setReportName("error-report.txt");
		monitor.setReportDestination(new ReportReceiver() {

			public void report(String reportName, Object[] values) {
				StringBuffer sb = new StringBuffer(reportName);
				if (null != values) {
					sb.append(":");
					for (int i = 0; i < values.length; i++) {
						sb.append(values[i]);
						if (i != values.length - 1) {
							sb.append(",");
						}
					}
				}
				System.err.println(sb.toString());
			}
		});

		MonitorChain chain = new MonitorChain();
		chain.setMonitors(new Monitor[] { monitor });
		chain.init();

		s.setMonitorChain(chain);

		Object o2 = "value2";
		try {
			assertSame(
				"Objects in request and response should have been the same",
				o2,
				s.execute(new ServiceRequest("service", "key", o2)).getData("key"));
		} catch (ServiceFailedException e) {
			fail("execute() should not have thrown ServiceFailedException");
			e.printStackTrace();
		}

		try {
			s.execute(new ServiceRequest("service", "key", "ServiceFailedException"));
			fail("execute() should have thrown ServiceFailedException");
		} catch (ServiceFailedException e) {
			assertEquals(
				"execute() should have thrown UNSPECIFIED_ERROR",
				e.getErrorCode(),
				ErrorCode.UNSPECIFIED_ERROR.getCode());
		}

		try {
			s.execute(new ServiceRequest("service", "key", "RuntimeException"));
			fail("execute() should have thrown RuntimeException");
		} catch (Throwable t) {
			assertTrue("execute() should have thrown RuntimeException", t instanceof RuntimeException);
		}

		try {
			s.execute(new ServiceRequest("service", "key", "RuntimeException"));
			fail("execute() should have thrown ServiceFailedException");
		} catch (Throwable t) {
			assertTrue(
				"execute() should have thrown MONITORING_ERROR_SERVICE_DISABLED",
				t instanceof ServiceFailedException
					&& FrameworkError.MONITORING_ERROR_SERVICE_DISABLED.getCode() == ((ServiceFailedException) t).getErrorCode());
		}

		// Test transactions
		s.setTransactional(false);
		assertFalse("Set/Get Transactional failed", s.isTransactional());
	}

	private class TestIntegrationService extends IntegrationService {

		public TestIntegrationService() {
			super.setBeanName("TestService");
		}

		protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {

			Object o = request.getData("key");
			if ("ServiceFailedException".equals(o)) {
				throw new ServiceFailedException(ErrorCode.UNSPECIFIED_ERROR);
			} else if ("RuntimeException".equals(o)) {
				throw new RuntimeException("Ååh nei og nei, hva er det som har skjedd");
			} else {
				return new ServiceResponse("key", o);
			}
		}
	}

}
