package no.stelvio.common.ejb;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.ejb.EJBLookupHelper;
import no.stelvio.common.ejb.LookupHelper;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.service.ServiceNotFoundException;

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
		System.setProperty("java.naming.factory.initial", "no.stelvio.common.ejb.TestInitialContextFactory");
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
			"no.stelvio.common.ejb.TestInitialContextFactoryCreationFailure");
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
		System.setProperty("java.naming.factory.initial", "no.stelvio.common.ejb.TestInitialContextFactory");
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
			"no.stelvio.common.ejb.TestInitialContextFactoryCloseFailure");
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
