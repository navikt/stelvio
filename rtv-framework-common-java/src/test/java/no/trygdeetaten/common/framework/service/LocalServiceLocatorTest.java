package no.trygdeetaten.common.framework.service;

import no.trygdeetaten.common.framework.FrameworkError;

import junit.framework.TestCase;

/**
 * LocalServiceLocatorTest unit test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1211 $ $Author: tsb2920 $ $Date: 2004-08-31 08:48:17 +0200 (Tue, 31 Aug 2004) $
 */
public class LocalServiceLocatorTest extends TestCase {

	/**
	 * Constructor for LocalServiceLocatorTest.
	 * @param arg0
	 */
	public LocalServiceLocatorTest(String arg0) {
		super(arg0);
	}

	public void test() {

		LocalServiceLocator sl = new LocalServiceLocator("test-local-service-locator.xml");
		assertTrue("Located Service", sl.lookup("TestLocalService") instanceof LocalService);

		try {
			sl.lookup(null);
			fail("lookup(null) should have failed");
		} catch (ServiceNotFoundException sfe) {
			assertEquals(
				"lookup(null) should have returned SERVICE_NAME_MISSING",
				sfe.getErrorCode(),
				FrameworkError.SERVICE_NAME_MISSING.getCode());
		}

		try {
			sl.lookup("");
			fail("lookup('') should have failed");
		} catch (ServiceNotFoundException sfe) {
			assertEquals(
				"lookup('') should have returned SERVICE_NAME_MISSING",
				sfe.getErrorCode(),
				FrameworkError.SERVICE_NAME_MISSING.getCode());
		}

		try {
			sl.lookup("Not Configured");
			fail("lookup('Not Configured') should have failed");
		} catch (ServiceNotFoundException sfe) {
			assertEquals(
				"lookup('Not Configured') should have returned SERVICE_CREATION_ERROR",
				sfe.getErrorCode(),
				FrameworkError.SERVICE_CREATION_ERROR.getCode());
		}

	}

}
