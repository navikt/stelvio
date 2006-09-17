package no.stelvio.common.framework.error;

import no.stelvio.common.framework.error.Severity;
import junit.framework.TestCase;

/**
 * Unit test of Severity.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1954 $ $Author: psa2920 $ $Date: 2005-02-08 14:35:42 +0100 (Tue, 08 Feb 2005) $
 */
public class SeverityTest extends TestCase {

	/**
	 * Constructor for SeverityTest.
	 * @param arg0
	 */
	public SeverityTest(String arg0) {
		super(arg0);
	}

	public void testConstants() {
		new Severity();
		super.assertTrue("WARN < ERROR", Severity.WARN.intValue() < Severity.ERROR.intValue());
		super.assertTrue("WARN < FATAL", Severity.WARN.intValue() < Severity.FATAL.intValue());
		super.assertTrue("ERROR < FATAL", Severity.ERROR.intValue() < Severity.FATAL.intValue());

	}

}
