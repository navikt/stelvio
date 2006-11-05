package no.stelvio.common.error;

import junit.framework.TestCase;

/**
 * Unit test of Severity.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1954 $ $Author: psa2920 $ $Date: 2005-02-08 14:35:42 +0100 (Tue, 08 Feb 2005) $
 */
public class SeverityTest extends TestCase {
	public void testConstants() {
		assertTrue("WARN < ERROR", Severity.ERROR.isMoreFatalThan(Severity.WARN));
		assertTrue("WARN < FATAL", Severity.FATAL.isMoreFatalThan(Severity.WARN));
		assertTrue("ERROR < FATAL", Severity.FATAL.isMoreFatalThan(Severity.ERROR));
	}
}
