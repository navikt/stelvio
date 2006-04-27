package no.nav.common.framework.ejb;

import java.rmi.RemoteException;
import java.security.Principal;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.SessionContext;
import javax.transaction.UserTransaction;

import junit.framework.TestCase;

import no.nav.common.framework.ejb.AbstractServiceFacade;
import no.nav.common.framework.error.ErrorCode;
import no.nav.common.framework.error.SystemException;
import no.nav.common.framework.error.TestServiceLocator;
import no.nav.common.framework.service.Service;
import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.service.ServiceLocator;
import no.nav.common.framework.service.ServiceNotFoundException;
import no.nav.common.framework.service.ServiceRequest;
import no.nav.common.framework.service.ServiceResponse;

/**
 * AbstractServiceFacade unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2297 $ $Author: psa2920 $ $Date: 2005-05-31 12:06:20 +0200 (Tue, 31 May 2005) $
 */
public class AbstractServiceFacadeTest extends TestCase {

	/**
	 * Constructor for AbstractServiceFacadeTest.
	 * @param arg0
	 */
	public AbstractServiceFacadeTest(String arg0) {
		super(arg0);
	}

	public void test() {
		AbstractServiceFacade f = new TestAbstractServiceFacade();
		f.setServiceLocator(new TestServiceLocator());

		try {
			f.setSessionContext(new TestSessionContext(false));
		} catch (EJBException e) {
			fail("setSessionContext() should not have thrown EJBException");
		} catch (RemoteException e) {
			fail("setSessionContext() should not have thrown RemoteException");
		}

		// Success
		try {
			Object o = f.execute(new ServiceRequest("service", "key", "success")).getData("key");
			assertEquals("Response did not hold the desired key", o, "success");
		} catch (ServiceFailedException e) {
			fail("execute() should not have thrown ServiceFailedException");
		}

		// ServiceFailedException, CMT
		try {
			f.execute(new ServiceRequest("service", "key", "ServiceFailedException"));
			fail("execute() should have thrown ServiceFailedException");
		} catch (ServiceFailedException e) {
			assertEquals(
				"execute() should have thrown UNSPECIFIED_ERROR",
				e.getErrorCode(),
				ErrorCode.UNSPECIFIED_ERROR.getCode());
		}

		// SystemException
		try {
			f.execute(new ServiceRequest("service", "key", "SystemException"));
			fail("execute() should have thrown SystemException");
		} catch (Throwable e) {
			assertTrue("execute() should have thrown SystemException", e instanceof SystemException);
		}

		// RuntimeException
		try {
			f.execute(new ServiceRequest("service", "key", "RuntimeException"));
			fail("execute() should have thrown SystemException");
		} catch (Throwable e) {
			assertTrue("execute() should have thrown RuntimeException", e instanceof RuntimeException);
		}

		// ServiceFailedException, BMT
		try {
			f.setSessionContext(new TestSessionContext(true));
		} catch (EJBException e) {
			fail("setSessionContext() should not have thrown EJBException");
		} catch (RemoteException e) {
			fail("setSessionContext() should not have thrown RemoteException");
		}

		try {
			f.execute(new ServiceRequest("service", "key", "ServiceFailedException"));
			fail("execute() should have thrown ServiceFailedException");
		} catch (ServiceFailedException e) {
			assertEquals(
				"execute() should have thrown UNSPECIFIED_ERROR",
				e.getErrorCode(),
				ErrorCode.UNSPECIFIED_ERROR.getCode());
		}
	}

	private class TestAbstractServiceFacade extends AbstractServiceFacade {

		protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
			Object o = request.getData("key");

			if ("ServiceFailedException".equals(o)) {
				ServiceFailedException sfe = new ServiceFailedException(ErrorCode.UNSPECIFIED_ERROR);
				sfe.setRollbackOnly();
				throw sfe;
			}
			if ("SystemException".equals(o)) {
				throw new SystemException(ErrorCode.UNSPECIFIED_ERROR);
			}
			if ("RuntimeException".equals(o)) {
				throw new RuntimeException("Hiiiiiiiiyyyyaaaaaarrrrggggh");
			} else {
				return new ServiceResponse("key", o);
			}
		}
	}

	private class TestServiceLocator implements ServiceLocator {
		public Service lookup(String name) throws ServiceNotFoundException {
			return null;
		}
	}

	private class TestSessionContext implements SessionContext {

		private boolean isBMT = false;

		public TestSessionContext(boolean isBMT) {
			this.isBMT = isBMT;
		}

		public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
			return null;
		}
		public EJBObject getEJBObject() throws IllegalStateException {
			return null;
		}
		public EJBHome getEJBHome() {
			return null;
		}
		public EJBLocalHome getEJBLocalHome() {
			return null;
		}

		/** @deprecated */
		public Properties getEnvironment() {
			return null;
		}
		/** @deprecated */
		public java.security.Identity getCallerIdentity() {
			return null;
		}
		public Principal getCallerPrincipal() {
			return null;
		}
		/** @deprecated */
		public boolean isCallerInRole(java.security.Identity arg0) {
			return false;
		}
		public boolean isCallerInRole(String arg0) {
			return false;
		}
		public UserTransaction getUserTransaction() throws IllegalStateException {
			return null;
		}
		public void setRollbackOnly() throws IllegalStateException {
			if (isBMT) {
				throw new IllegalStateException("Dette er BMT");
			}
		}
		public boolean getRollbackOnly() throws IllegalStateException {
			return false;
		}
	}

}
