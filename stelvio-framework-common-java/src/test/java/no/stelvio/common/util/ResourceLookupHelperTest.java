package no.stelvio.common.util;

import junit.framework.TestCase;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.ejb.LookupHelper;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.service.ServiceNotFoundException;
import no.stelvio.common.util.ResourceLookupHelper;

/**
 * 
 * @author person356941106810, Accenture
 */
public class ResourceLookupHelperTest extends TestCase {

	/**
	 * Constructor for ResourceLookupHelperTest.
	 * @param arg0
	 */
	public ResourceLookupHelperTest(String arg0) {
		super(arg0);
	}

	public void testInitialContextConstructionAndLookupSuccessful() {
		System.setProperty("java.naming.factory.initial", "no.stelvio.common.ejb.TestInitialContextFactory");
		LookupHelper h = new ResourceLookupHelper();
		try {
			assertTrue("jndiName", h.lookup("Resource", null) instanceof Byte);
		} catch (Throwable t) {
			t.printStackTrace();
			fail("lookup() should not have thrown Exception");
		}
	}

	public void testInitialContextConstructionFailure() {
		System.setProperty(
			"java.naming.factory.initial",
			"no.stelvio.common.ejb.TestInitialContextFactoryCreationFailure");
		LookupHelper h = new ResourceLookupHelper();
		try {
			h.lookup("Resource", null);
		} catch (Throwable t) {
			assertTrue(
				"lookup() should have thrown SERVICE_CONTEXT_CREATION_ERROR",
				t instanceof ServiceNotFoundException
					&& FrameworkError.SERVICE_CONTEXT_CREATION_ERROR.getCode() == ((ServiceNotFoundException) t).getErrorCode());
		}
	}

	public void testContextLookupFailures() {
		System.setProperty("java.naming.factory.initial", "no.stelvio.common.ejb.TestInitialContextFactory");
		LookupHelper h = new ResourceLookupHelper();

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
		LookupHelper h = new ResourceLookupHelper();

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
