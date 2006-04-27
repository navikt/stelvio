package no.nav.common.framework.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;

import junit.framework.TestCase;

import no.nav.common.framework.FrameworkError;
import no.nav.common.framework.ejb.RemoteService;
import no.nav.common.framework.ejb.ServiceDelegateImpl;
import no.nav.common.framework.error.DefaultExceptionWrapper;
import no.nav.common.framework.error.SystemException;
import no.nav.common.framework.error.TestServiceLocator;
import no.nav.common.framework.performance.MonitorKey;
import no.nav.common.framework.service.Service;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceLocator;
import no.nav.common.framework.service.ServiceNotFoundException;
import no.nav.common.framework.service.ServiceRequest;
import no.nav.common.framework.service.ServiceResponse;

/**
 * ServiceDelegateImpl Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2842 $ $Author: psa2920 $ $Date: 2006-04-25 12:38:49 +0200 (Tue, 25 Apr 2006) $
 */
public class ServiceDelegateImplTest extends TestCase {

	/**
	 * Constructor for ServiceDelegateImplTest.
	 * @param arg0
	 */
	public ServiceDelegateImplTest(String arg0) {
		super(arg0);
	}

	public void testDelegationSuccessful() {
		ServiceDelegateImpl d = new TestAbstractServiceDelegate("TestFacade");
		d.setServiceLocator(new TestServiceLocator());

		try {
			String value = "value";
			ServiceResponse response = d.execute(new ServiceRequest("The Service Name", "key", value));
			assertEquals("Delegation should have succeeded", response.getData("key"), value);
		} catch (ServiceFailedException e) {
			fail("The delegate should not have failed." + e.getMessage());
		}
	}

	public void testDelegationUnSuccessful() {
		ServiceDelegateImpl d = new TestAbstractServiceDelegate("TestFacade2");
		d.setServiceLocator(new TestServiceLocator());
		try {
			d.execute(new ServiceRequest("The Service Name", "key", null));
			fail("Delegation should not have succeeded");
		} catch (ServiceFailedException e) {
			fail("Delegate should not have thrown ServiceFailedException" + e.getMessage());
		} catch (SystemException e) {
			assertEquals(
				"Delegate should have thrown SERVICE_FACADE_NOT_FOUND",
				e.getErrorCode(),
				FrameworkError.SERVICE_FACADE_NOT_FOUND.getCode());
		}
	}

	public void testDelegationUnSuccessfulAgain() {
		ServiceDelegateImpl d = new TestAbstractServiceDelegate("TestNotFacade");
		d.setServiceLocator(new TestServiceLocator());
		try {
			d.execute(new ServiceRequest("The Service Name", "key", null));
			fail("Delegation should not have succeeded");
		} catch (ServiceFailedException e) {
			fail("Delegate should not have thrown ServiceFailedException" + e.getMessage());
		} catch (SystemException e) {
			assertEquals(
				"Delegate should have thrown SERVICE_FACADE_TYPE_ERROR",
				e.getErrorCode(),
				FrameworkError.SERVICE_FACADE_TYPE_ERROR.getCode());
		}
	}

	public void testDelegateCatchesServiceFailedException() {

		ServiceDelegateImpl d = new TestAbstractServiceDelegate("TestFacade");
		d.setServiceLocator(new TestServiceLocator());
		try {
			d.execute(new ServiceRequest("The Service Name", "key", "ServiceFailedException"));
			fail("Delegation should not have succeeded");
		} catch (Throwable e) {
			assertTrue("Delegate should have thrown ServiceFailedException", e instanceof ServiceFailedException);
		}

		Throwable cause1 = new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, "QWERTYUIP");
		Throwable cause2 = new SystemException(FrameworkError.HIBERNATE_UPDATE_EXECUTION_ERROR);
		Throwable cause3 = new RuntimeException("vrææææææl");
		Throwable cause4 = new Exception("Just a plain old exception");

		ServiceRequest request1 = new ServiceRequest("The Service Name", "key", "RemoteException");
		request1.setData("cause", cause1);
		try {
			d.execute(request1);
			fail("Delegation should not have succeeded");
		} catch (Throwable e) {
			assertTrue("Delegate should have thrown ServiceFailedException", e instanceof ServiceFailedException);
		}

		ServiceRequest request2 = new ServiceRequest("The Service Name", "key", "RemoteException");
		request2.setData("cause", cause2);
		try {
			d.execute(request2);
			fail("Delegation should not have succeeded");
		} catch (Throwable e) {
			assertTrue("Delegate should have thrown SystemException", e instanceof SystemException);
		}

		ServiceRequest request3 = new ServiceRequest("The Service Name", "key", "RemoteException");
		request3.setData("cause", cause3);
		try {
			d.execute(request3);
			fail("Delegation should not have succeeded");
		} catch (Throwable e) {
			assertTrue("Delegate should have thrown SystemException", e instanceof SystemException);
		}

		ServiceRequest request4 = new ServiceRequest("The Service Name", "key", "RemoteException");
		request4.setData("cause", cause4);
		try {
			d.execute(request4);
			fail("Delegation should not have succeeded");
		} catch (Throwable e) {
			assertTrue("Delegate should have thrown SystemException", e instanceof SystemException);
		}
	}

	private class TestAbstractServiceDelegate extends ServiceDelegateImpl {
		public TestAbstractServiceDelegate(String facadeName) {
			setDelegateMonitorKey(new MonitorKey("TestFacade", MonitorKey.BUSINESS));
			setServiceFacadeName(facadeName);
			setExceptionWrapper(new DefaultExceptionWrapper());
		}
	}

	private class TestServiceLocator implements ServiceLocator {
		public Service lookup(String name) throws ServiceNotFoundException {

			if ("TestFacade".equals(name)) {
				return new TestFacade();
			} else if ("TestNotFacade".equals(name)) {
				return new Service() {
				};
			} else {
				throw new ServiceNotFoundException(FrameworkError.SERVICE_NAME_NOT_FOUND);
			}
		}
	}

	private class TestFacade implements RemoteService {

		public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException, RemoteException {
			Object value = request.getData("key");
			if ("ServiceFailedException".equals(value)) {
				throw new ServiceFailedException(FrameworkError.SERVICE_INPUT_MISSING, "QWERTYUIP");
			} else if ("RemoteException".equals(value)) {
				Throwable cause = (Throwable) request.getData("cause");
				throw new RemoteException("Oaaaaæææææh", cause);
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
