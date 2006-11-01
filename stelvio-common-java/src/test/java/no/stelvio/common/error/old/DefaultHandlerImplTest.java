package no.stelvio.common.error.old;

import junit.framework.TestCase;
import no.stelvio.common.error.ErrorCode;
import no.stelvio.common.error.SystemException;

/**
 * DefaultHandlerImpl Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1021 $ $Author: psa2920 $ $Date: 2004-08-13 11:10:04 +0200 (Fri, 13 Aug 2004) $
 */
public class DefaultHandlerImplTest extends TestCase {

	/**
	 * Constructor for DefaultHandlerImplTest.
	 * @param arg0
	 */
	public DefaultHandlerImplTest(String arg0) {
		super(arg0);
	}

	public void testHandlerWithLogging() {
		Handler h1 = null;
		try {
			h1 = new DefaultHandlerImpl();
			h1.init();
			assertTrue("Initialization is successful", true);
		} catch (Throwable t) {
			fail("Initialization should have been successful");
		}

		Throwable re = new RuntimeException("My Beautiful Exception!");
		Throwable se = new SystemException(ErrorCode.UNSPECIFIED_ERROR);
		assertEquals("RuntimeException is handled", re, h1.handleError(re));

		assertEquals("RuntimeException's localized message is returned", re.getLocalizedMessage(), h1.getMessage(re));
		assertEquals("LoggableException's message is returned", se.getMessage(), h1.getMessage(se));
	}

}
