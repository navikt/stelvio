package no.stelvio.common.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;

import junit.framework.TestCase;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.ejb.ServiceDelegateLocalImpl;
import no.stelvio.common.error.DefaultExceptionWrapper;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.performance.MonitorKey;
import no.stelvio.common.service.LocalService;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;

/**
 * ServiceDelegateLocalImplTest Unit Test.
 * 
 * @author Jonas Lindholm, Accenture
 * @version $Id: ServiceDelegateLocalImplTest.java 2268 2005-05-24 10:57:36Z
 *          psa2920 $
 */
public class ServiceDelegateLocalImplTest extends TestCase {

	/**
	 * Constructor for ServiceDelegateImplTest.
	 * 
	 * @param arg0
	 */
	public ServiceDelegateLocalImplTest(String arg0) {
		super(arg0);
	}

	public void testDelegationSuccessful() {
		ServiceDelegateLocalImpl d = new TestAbstractServiceDelegate("TestFacade");

		try {
			String value = "value";
			ServiceResponse response = d.execute(new ServiceRequest("The Service Name", "key", value));
			assertEquals("Delegation should have succeeded", response.getData("key"), value);
		} catch (ServiceFailedException e) {
			fail("The delegate should not have failed." + e.getMessage());
		}
	}

	public void testDelegationUnSuccessful() {
		ServiceDelegateLocalImpl d = new TestAbstractServiceDelegate("TestFacade2");
		try {
			d.execute(new ServiceRequest("The Service Name", "key", "ServiceFailedException"));
			fail("Delegation should not have succeeded");
		} catch (ServiceFailedException e) {
			assertEquals("Delegate should have thrown ServiceFailedException", e.getErrorCode(),
					FrameworkError.SERVICE_INPUT_MISSING.getCode());
		} catch (Exception e) {
			fail("Delegate should not have thrown Exception" + e.getMessage());
		}
	}

	public void testDelegateCatchesServiceFailedException() {
		ServiceDelegateLocalImpl d = new TestAbstractServiceDelegate("TestFacade");
		try {
			d.execute(new ServiceRequest("The Service Name", "key", "ServiceFailedException"));
			fail("Delegation should not have succeeded");
		} catch (Throwable e) {
			assertTrue("Delegate should have thrown ServiceFailedException", e instanceof ServiceFailedException);
		}

		Throwable cause1 = new SystemException(FrameworkError.HIBERNATE_UPDATE_EXECUTION_ERROR);
		Throwable cause2 = new RuntimeException("vrææææææl");
		Throwable cause3 = new Exception("Just a plain old exception");

		ServiceRequest request1 = new ServiceRequest("The Service Name", "key", "RuntimeException");
		request1.setData("cause", cause1);
		try {
			d.execute(request1);
			fail("Delegation should not have succeeded");
		} catch (Throwable e) {
			assertTrue("Delegate should have thrown SystemException", e instanceof SystemException);
		}

		ServiceRequest request2 = new ServiceRequest("The Service Name", "key", "RuntimeException");
		request2.setData("cause", cause2);
		try {
			d.execute(request2);
			fail("Delegation should not have succeeded");
		} catch (Throwable e) {
			assertTrue("Delegate should have thrown SystemException", e instanceof SystemException);
		}

		ServiceRequest request3 = new ServiceRequest("The Service Name", "key", "RuntimeException");
		request3.setData("cause", cause3);
		try {
			d.execute(request3);
			fail("Delegation should not have succeeded");
		} catch (Throwable e) {
			assertTrue("Delegate should have thrown SystemException", e instanceof SystemException);
		}

		ServiceRequest request4 = new ServiceRequest("The Service Name", "key", "ServiceFailedException");
		try {
			d.execute(request4);
			fail("Delegation should not have succeeded");
		} catch (Throwable e) {
			assertTrue("Delegate should have thrown ServiceFailedException", e instanceof ServiceFailedException);
		}
	}

	private class TestAbstractServiceDelegate extends ServiceDelegateLocalImpl {
		public TestAbstractServiceDelegate(String facadeName) {
			setDelegateMonitorKey(new MonitorKey("TestFacade", MonitorKey.BUSINESS));
			setServiceFacadeName(facadeName);
			setExceptionWrapper(new DefaultExceptionWrapper());
			setServiceFacade(new TestFacade());
		}
	}

	private class TestFacade implements LocalService {

		public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
			Object value = request.getData("key");
			if ("ServiceFailedException".equals(value)) {
				throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, "QWERTYUIP");
			} else if ("RuntimeException".equals(value)) {
				Throwable cause = (Throwable) request.getData("cause");
				throw new RuntimeException("Oaaaaæææææh", new RuntimeException("aaaaiiiiiiiiii", cause));
			}
			return new ServiceResponse("key", value);
		}

		// No need to test these methods
		public EJBHome getEJBHome() throws RemoteException {
			return null;
		}

		public Object getPrimaryKey() throws RemoteException {
			return null;
		}

		public void remove() throws RemoteException, RemoveException {
		}

		public Handle getHandle() throws RemoteException {
			return null;
		}

		public boolean isIdentical(EJBObject arg0) throws RemoteException {
			return false;
		}
	}
}
