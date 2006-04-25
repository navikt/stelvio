package no.trygdeetaten.common.framework.ejb;

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

/**
 * SessionAdapter Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2080 $ $Author: psa2920 $ $Date: 2005-03-08 11:52:38 +0100 (Tue, 08 Mar 2005) $
 */
public class SessionAdapterTest extends TestCase {

	/**
	 * Constructor for SessionAdapterTest.
	 * @param arg0
	 */
	public SessionAdapterTest(String arg0) {
		super(arg0);
	}

	public void test() {
		TestSessionAdapter a = new TestSessionAdapter();
		try {
			a.ejbActivate();
		} catch (EJBException e) {
			fail("ejbActivate() should not have thrown EJBException");
			e.printStackTrace();
		} catch (RemoteException e) {
			fail("ejbActivate() should not have thrown RemoteException");
			e.printStackTrace();
		}

		try {
			a.ejbPassivate();
		} catch (EJBException e) {
			fail("ejbPassivate() should not have thrown EJBException");
			e.printStackTrace();
		} catch (RemoteException e) {
			fail("ejbPassivate() should not have thrown RemoteException");
			e.printStackTrace();
		}

		try {
			a.ejbRemove();
		} catch (EJBException e) {
			fail("ejbRemove() should not have thrown EJBException");
			e.printStackTrace();
		} catch (RemoteException e) {
			fail("ejbRemove() should not have thrown RemoteException");
			e.printStackTrace();
		}

		SessionContext ctx = new SessionContext() {

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
			}

			public boolean getRollbackOnly() throws IllegalStateException {
				return false;
			}
		};

		try {
			a.setSessionContext(ctx);
		} catch (EJBException e) {
			fail("setSessionContext() should not have thrown EJBException");
			e.printStackTrace();
		} catch (RemoteException e) {
			fail("setSessionContext() should not have thrown RemoteException");
			e.printStackTrace();
		}

		assertEquals("getSessionContext() should have returned the context", ctx, a.getSessionContext());

	}

	private class TestSessionAdapter extends SessionAdapter {
	}

}
