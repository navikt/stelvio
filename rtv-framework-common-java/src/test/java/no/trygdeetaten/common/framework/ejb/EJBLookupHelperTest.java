package no.trygdeetaten.common.framework.ejb;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.service.ServiceNotFoundException;

import junit.framework.TestCase;

/**
 * EJBLookupHelper Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1129 $ $Author: psa2920 $ $Date: 2004-08-19 17:25:55 +0200 (Thu, 19 Aug 2004) $
 */
public class EJBLookupHelperTest extends TestCase {

	/**
	 * Constructor for EJBLookupHelperTest.
	 * @param arg0
	 */
	public EJBLookupHelperTest(String arg0) {
		super(arg0);
	}

	public void testInitialContextConstructionAndLookupSuccessful() {
		System.setProperty("java.naming.factory.initial", "no.trygdeetaten.common.framework.ejb.TestInitialContextFactory");
		LookupHelper h = new EJBLookupHelper();
		try {
			assertTrue("jndiName", h.lookup("TestEJBHome", null) instanceof TestEJBHome);
		} catch (Throwable t) {
			fail("lookup() should not have thrown Exception");
			t.printStackTrace();
		}
	}

	public void testInitialContextConstructionFailure() {
		System.setProperty(
			"java.naming.factory.initial",
			"no.trygdeetaten.common.framework.ejb.TestInitialContextFactoryCreationFailure");
		LookupHelper h = new EJBLookupHelper();
		try {
			h.lookup("TestEJBHome", null);
		} catch (Throwable t) {
			assertTrue(
				"lookup() should have thrown SERVICE_CONTEXT_CREATION_ERROR",
				t instanceof ServiceNotFoundException
					&& FrameworkError.SERVICE_CONTEXT_CREATION_ERROR.getCode() == ((ServiceNotFoundException) t).getErrorCode());
		}
	}

	public void testContextLookupFailures() {
		System.setProperty("java.naming.factory.initial", "no.trygdeetaten.common.framework.ejb.TestInitialContextFactory");
		LookupHelper h = new EJBLookupHelper();

		try {
			h.lookup("NamingException", null);
		} catch (Throwable t) {
			assertTrue(
				"lookup() should have thrown SERVICE_LOOKUP_ERROR",
				t instanceof ServiceNotFoundException
					&& FrameworkError.SERVICE_LOOKUP_ERROR.getCode() == ((ServiceNotFoundException) t).getErrorCode());
		}

		try {
			h.lookup("An object not of type EJBHome", null);
		} catch (Throwable t) {
			assertTrue(
				"lookup() should have thrown SERVICE_TYPE_ERROR",
				t instanceof ServiceNotFoundException
					&& FrameworkError.SERVICE_TYPE_ERROR.getCode() == ((ServiceNotFoundException) t).getErrorCode());
		}

	}

	public void testContextCloseFailure() {
		System.setProperty(
			"java.naming.factory.initial",
			"no.trygdeetaten.common.framework.ejb.TestInitialContextFactoryCloseFailure");
		LookupHelper h = new EJBLookupHelper();

		try {
			h.lookup("NamingException", null);
		} catch (Throwable t) {
			assertTrue(
				"lookup() should have thrown SERVICE_CONTEXT_DELETION_ERROR",
				t instanceof SystemException
					&& FrameworkError.SERVICE_CONTEXT_DELETION_ERROR.getCode() == ((SystemException) t).getErrorCode());
		}
	}
}
