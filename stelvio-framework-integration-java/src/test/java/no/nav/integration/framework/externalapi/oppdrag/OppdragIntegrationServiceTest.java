package no.nav.integration.framework.externalapi.oppdrag;

import java.rmi.RemoteException;
import java.rmi.ServerException;

import junit.framework.TestCase;

import org.springframework.remoting.RemoteAccessException;

import com.ibm.no.rtv.oppdrag.bus.OppdragService;
import com.ibm.no.rtv.oppdrag.exception.CicsException;
import com.ibm.no.rtv.oppdrag.exception.InconsistencyException;
import com.ibm.no.rtv.oppdrag.exception.MappingException;
import com.ibm.no.rtv.oppdrag.exception.PredatorException;
import com.ibm.no.rtv.oppdrag.exception.PredatorUpdateFailedException;
import com.ibm.no.rtv.oppdrag.value.OppdragVO;

import no.nav.integration.framework.externalapi.oppdrag.OppdragIntegrationService;
import no.nav.common.framework.error.SystemException;
import no.nav.common.framework.monitor.ErrorMonitor;
import no.nav.common.framework.monitor.Monitor;
import no.nav.common.framework.monitor.MonitorChain;
import no.nav.common.framework.monitor.ReportReceiver;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceRequest;
import no.nav.common.framework.service.ServiceResponse;

/**
 * Test of OppdragService integration
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: OppdragIntegrationServiceTest.java 2645 2005-11-25 15:25:36Z
 *          tkc2920 $
 */
public class OppdragIntegrationServiceTest extends TestCase {

	// mock for EJB
	OppdragServiceTestMock mock = null;

	OppdragIntegrationService service = null;

	protected void setUp() {
		mock = new OppdragServiceTestMock();
		service = new OppdragIntegrationService();
		service.setTransactional(false);
		service.setProxy(mock);
		service.setEnabled(true);

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
		service.setMonitorChain(chain);
		service.setBeanName("test");
	}

	/**
	 * Test execute
	 */
	public void testExecute() throws ServiceFailedException {

		// test case 1: Everything is set
		service.init();
		ServiceRequest request = new ServiceRequest("OppdragIntegration");
		OppdragVO oppdragVO = new OppdragVO();
		request.setData("IN_OPPDRAG", oppdragVO);

		ServiceResponse response = service.execute(request);
		response.getData("OUT_OPPDRAG");

		// test case 2: missing proxy
		service.setProxy(null);

		try {
			service.init();
			fail("SystemException should have been thrown because of missing proxy");
		} catch (SystemException e) {
			// skal skje
		}

		// test case 3: missing service input
		request.setData("IN_OPPDRAG", null);
		service.setProxy(mock);
		service.init();

		try {
			service.execute(request);
			fail("ServiceFailedException should have been thrown because of missing input");
		} catch (ServiceFailedException e) {
			// skal skje
		}

		// test case 4: missing output
		mock.setMissing(true);
		request.setData("IN_OPPDRAG", oppdragVO);
		service.setProxy(mock);
		service.init();

		try {
			service.execute(request);
			fail("ServiceFailedException should have been thrown because of missing output");
		} catch (ServiceFailedException e) {
			// skal skje
		}

		// test case 5: EJB Execute exception. (Should be RemoteException but it
		// is checked locally)
		mock.setMissing(true);
		request.setData("IN_OPPDRAG", oppdragVO);
		service.setProxy(new OppdragServiceTestMockException());
		service.init();

		try {
			service.execute(request);
			fail("ServiceFailedException should have been thrown.");
		} catch (ServiceFailedException e) {
			// skal skje
		}
	}

	/**
	 * Metode som tester CICS exception fra Oppdrag
	 * 
	 * @throws Exception
	 *             hvis uforventet exeption intreffer
	 */
	public final void testCicsException() throws Exception {
		// test case: CICS fails
		OppdragVO oppdragVO = new OppdragVO();
		ServiceRequest request = new ServiceRequest("OppdragIntegration");
		request.setData("IN_OPPDRAG", oppdragVO);
		OppdragServiceTestMockExceptionWrapped proxy = new OppdragServiceTestMockExceptionWrapped();
		proxy.setExceptionType(0);
		service.setProxy(proxy);
		service.init();

		try {
			service.execute(request);
			fail("ServiceFailedException should have been thrown.");
		} catch (ServiceFailedException e) {
			assertEquals("Cause should be an instance of InconsistencyException;", e.getCause().getClass(),
					CicsException.class);
		}
	}

	/**
	 * Metode som tester inkonsistens exception fra Oppdrag
	 * 
	 * @throws Exception
	 *             hvis uforventet exeption intreffer
	 */
	public final void testInconsistencyException() throws Exception {
		// test case: inconsistency
		OppdragVO oppdragVO = new OppdragVO();
		ServiceRequest request = new ServiceRequest("OppdragIntegration");
		request.setData("IN_OPPDRAG", oppdragVO);
		OppdragServiceTestMockExceptionWrapped proxy = new OppdragServiceTestMockExceptionWrapped();
		proxy.setExceptionType(3);
		service.setProxy(proxy);
		service.init();

		try {
			service.execute(request);
			fail("ServiceFailedException should have been thrown.");
		} catch (ServiceFailedException e) {
			assertEquals("Exception should be an instance of InconsistencyException;", e.getCause().getClass(),
					InconsistencyException.class);
		}
	}

	/**
	 * Metode som tester Predator exception fra Oppdrag
	 * 
	 * @throws Exception
	 *             hvis uforventet exeption intreffer
	 */
	public final void testPredatorException() throws Exception {
		// test case: Predator fails
		OppdragVO oppdragVO = new OppdragVO();
		ServiceRequest request = new ServiceRequest("OppdragIntegration");
		request.setData("IN_OPPDRAG", oppdragVO);
		OppdragServiceTestMockExceptionWrapped proxy = new OppdragServiceTestMockExceptionWrapped();
		proxy.setExceptionType(1);
		service.setProxy(proxy);
		service.init();

		try {
			service.execute(request);
			fail("ServiceFailedException should have been thrown.");
		} catch (ServiceFailedException e) {
			assertEquals("Exception should be an instance of PredatorException;", e.getCause().getClass(),
					PredatorException.class);
		}
	}

	/**
	 * Metode som tester Predator update exception fra Oppdrag
	 * 
	 * @throws Exception
	 *             hvis uforventet exeption intreffer
	 */
	public final void testPredatorUpdateExeption() throws Exception {
		// test case: Predator update fails
		OppdragVO oppdragVO = new OppdragVO();
		ServiceRequest request = new ServiceRequest("OppdragIntegration");
		request.setData("IN_OPPDRAG", oppdragVO);
		OppdragServiceTestMockExceptionWrapped proxy = new OppdragServiceTestMockExceptionWrapped();
		proxy.setExceptionType(2);
		service.setProxy(proxy);
		service.init();

		try {
			service.execute(request);
			fail("ServiceFailedException should have been thrown.");
		} catch (ServiceFailedException e) {
			assertEquals("Exception should be an instance of PredatorUpdateFailedException;", e.getCause().getClass(),
					PredatorUpdateFailedException.class);
		}
	}

	/**
	 * Metode som tester mapping exception fra Oppdrag
	 * 
	 * @throws Exception
	 *             hvis uforventet exeption intreffer
	 */
	public final void testMappingException() throws Exception {
		// test case: Predator update fails
		OppdragVO oppdragVO = new OppdragVO();
		ServiceRequest request = new ServiceRequest("OppdragIntegration");
		request.setData("IN_OPPDRAG", oppdragVO);
		OppdragServiceTestMockExceptionWrapped proxy = new OppdragServiceTestMockExceptionWrapped();
		proxy.setExceptionType(4);
		service.setProxy(proxy);
		service.init();

		try {
			service.execute(request);
			fail("ServiceFailedException should have been thrown.");
		} catch (ServiceFailedException e) {
			assertEquals("Exception should be an instance of MappingException;", e.getCause().getClass(),
					MappingException.class);
		}
	}

	/**
	 * Mocking the oppdrag proxy.
	 */
	class OppdragServiceTestMock implements OppdragService {
		private boolean missing = false;

		/**
		 * Test implementation of Oppdrag EJB
		 * 
		 * @param arg0
		 *            oppdrag vo inn
		 */
		public OppdragVO executeQuery(OppdragVO arg0) {
			if (missing) {
				return null;
			} else {
				return arg0;
			}
		}

		/**
		 * Sets the missing flag
		 * 
		 * @param b
		 *            missing return
		 */
		public void setMissing(boolean b) {
			missing = true;
		}
	}

	/**
	 * Mocking the oppdrag proxy. Throws RuntimeException. (A unchecked
	 * exception subclassing Exception)
	 */
	class OppdragServiceTestMockException implements OppdragService {

		/**
		 * Test implementation of Oppdrag EJB
		 * 
		 * @param arg0
		 *            oppdrag vo inn
		 */
		public OppdragVO executeQuery(OppdragVO arg0) {

			throw new RuntimeException();
		}

		/**
		 * Sets the missing flag
		 * 
		 * @param b
		 *            missing return
		 */
		public void setMissing(boolean b) {
		}
	}

	/**
	 * Mocking the oppdrag proxy. Throws a wrapped exception.
	 */
	class OppdragServiceTestMockExceptionWrapped implements OppdragService {
		private int exceptionType = 0;

		/**
		 * Test implementation of Oppdrag EJB
		 * 
		 * @param arg0
		 *            oppdrag vo inn
		 */
		public OppdragVO executeQuery(OppdragVO arg0) {
			RemoteAccessException rae = null;
			if (exceptionType == 0) {
				CicsException ce = new CicsException("CICSException", new Throwable());
				RemoteException re = new RemoteException("RemoteException", ce);
				ServerException se = new ServerException("ServerExceptin", re);
				rae = new RemoteAccessException("OPPDRAG", se);

			} else if (exceptionType == 1) {
				PredatorException pe = new PredatorException("PredatorException", new Throwable());
				RemoteException re = new RemoteException("RemoteException", pe);
				ServerException se = new ServerException("ServerExceptin", re);
				rae = new RemoteAccessException("OPPDRAG", se);

			} else if (exceptionType == 2) {
				PredatorUpdateFailedException pue = new PredatorUpdateFailedException("PredatorUpdateFailedException");
				RemoteException re = new RemoteException("RemoteException", pue);
				ServerException se = new ServerException("ServerExceptin", re);
				rae = new RemoteAccessException("OPPDRAG", se);

			} else if (exceptionType == 3) {
				InconsistencyException ie = new InconsistencyException("InconsistencyException", new Throwable());
				RemoteException re = new RemoteException("RemoteException", ie);
				ServerException se = new ServerException("ServerExceptin", re);
				rae = new RemoteAccessException("OPPDRAG", se);

			} else if (exceptionType == 4) {
				MappingException me = new MappingException("MappingException", new Throwable());
				RemoteException re = new RemoteException("RemoteException", me);
				ServerException se = new ServerException("ServerExceptin", re);
				rae = new RemoteAccessException("OPPDRAG", se);

			} else {
				rae = new RemoteAccessException("OPPDRAG", new Throwable());
			}

			throw rae;
		}

		/**
		 * Sets the missing flag
		 * 
		 * @param type
		 *            missing return
		 */
		public void setExceptionType(int type) {
			exceptionType = type;
		}
	}

}
