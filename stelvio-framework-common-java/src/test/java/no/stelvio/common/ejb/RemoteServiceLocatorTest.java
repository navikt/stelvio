package no.stelvio.common.ejb;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;

import junit.framework.TestCase;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.ejb.LookupHelper;
import no.stelvio.common.ejb.RemoteService;
import no.stelvio.common.ejb.RemoteServiceLocator;
import no.stelvio.common.service.Service;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceNotFoundException;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;

/**
 * RemoteServiceLocator Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1061 $ $Author: psa2920 $ $Date: 2004-08-17 17:16:33 +0200 (Tue, 17 Aug 2004) $
 */
public class RemoteServiceLocatorTest extends TestCase {

	/**
	 * Constructor for RemoteServiceLocatorTest.
	 * @param arg0
	 */
	public RemoteServiceLocatorTest(String arg0) {
		super(arg0);
	}

	public void test() {

		RemoteServiceLocator sl = new RemoteServiceLocator();
		sl.setLookupHelper(new LookupHelper() {

			public Object lookup(String name, Hashtable environment) throws ServiceNotFoundException {

				if ("ejb/TestEJBHome".equals(name)) {
					return new TestEJBHome();
				} else {
					throw new ServiceNotFoundException(FrameworkError.SERVICE_LOOKUP_ERROR);
				}
			}
		});

		// Test name == null
		try {
			sl.lookup(null);
			fail("lookup(null) should have failed");
		} catch (ServiceNotFoundException sfe) {
			assertEquals(
				"lookup(null) should have returned SERVICE_NAME_MISSING",
				sfe.getErrorCode(),
				FrameworkError.SERVICE_NAME_MISSING.getCode());
		}

		// Test name length < 1
		try {
			sl.lookup("");
			fail("lookup('') should have failed");
		} catch (ServiceNotFoundException sfe) {
			assertEquals(
				"lookup('') should have returned SERVICE_NAME_MISSING",
				sfe.getErrorCode(),
				FrameworkError.SERVICE_NAME_MISSING.getCode());
		}

		// Test name not found
		try {
			sl.lookup("Not Configured");
			fail("lookup('Not Configured') should have failed");
		} catch (ServiceNotFoundException sfe) {
			assertEquals(
				"lookup('Not Configured') should have returned SERVICE_NAME_NOT_FOUND",
				sfe.getErrorCode(),
				FrameworkError.SERVICE_NAME_NOT_FOUND.getCode());
		}

		// Test JNDI name not specified
		try {
			sl.lookup("IncompleteConfiguredFacade");
			fail("lookup('IncompleteConfiguredFacade') should have failed");
		} catch (ServiceNotFoundException sfe) {
			assertEquals(
				"lookup('IncompleteConfiguredFacade') should have returned SERVICE_CONFIG_ERROR",
				sfe.getErrorCode(),
				FrameworkError.SERVICE_CONFIG_ERROR.getCode());
		}

		// Test service not found in cache
		try {
			sl.lookup("IncompleteConfiguredFacade");
			fail("lookup('IncompleteConfiguredFacade') should have failed");
		} catch (ServiceNotFoundException sfe) {
			assertEquals(
				"lookup('IncompleteConfiguredFacade') should have returned SERVICE_CONFIG_ERROR",
				sfe.getErrorCode(),
				FrameworkError.SERVICE_CONFIG_ERROR.getCode());
		}

		// Test service found in cache
		try {
			Service rs = sl.lookup("TestEJBHome");
			assertTrue("RemoteService should be of type TestEJB", rs instanceof TestEJB);

		} catch (ServiceNotFoundException sfe) {
			fail("lookup('TestEJBHome') should not have failed");
			sfe.printStackTrace();
		}

	}

	private class TestEJBHome implements EJBHome {

		public TestEJB create() throws CreateException, RemoteException {
			return new TestEJB();
		}

		public void remove(Handle arg0) throws RemoteException, RemoveException {
		}
		public void remove(Object arg0) throws RemoteException, RemoveException {
		}
		public EJBMetaData getEJBMetaData() throws RemoteException {
			return null;
		}
		public HomeHandle getHomeHandle() throws RemoteException {
			return null;
		}
	}

	private class TestEJB implements RemoteService {
		public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException, RemoteException {
			return new ServiceResponse("key", request.getData("key"));
		}
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
