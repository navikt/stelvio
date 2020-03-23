package no.stelvio.common.error.support;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test of Severity.
 * 
 * @version $Revision: 1954 $ $Author: psa2920 $ $Date: 2005-02-08 14:35:42 +0100 (Tue, 08 Feb 2005) $
 */
public class SeverityTest {
	/**
	 * Test constants.
	 */
	@Test
	public void testConstants() {
		assertTrue("WARN < ERROR", Severity.ERROR.isMoreFatalThan(Severity.WARN));
		assertTrue("WARN < FATAL", Severity.FATAL.isMoreFatalThan(Severity.WARN));
		assertTrue("ERROR < FATAL", Severity.FATAL.isMoreFatalThan(Severity.ERROR));
	}
}
