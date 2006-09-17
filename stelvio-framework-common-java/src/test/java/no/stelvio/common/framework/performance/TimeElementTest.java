package no.stelvio.common.framework.performance;

import no.stelvio.common.framework.performance.TimeElement;
import junit.framework.TestCase;

/**
 * TimeElement Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1062 $ $Author: psa2920 $ $Date: 2004-08-17 17:16:46 +0200 (Tue, 17 Aug 2004) $
 */
public class TimeElementTest extends TestCase {

	/**
	 * Constructor for TimeElementTest.
	 * @param arg0
	 */
	public TimeElementTest(String arg0) {
		super(arg0);
	}

	public void test() {
		long start = 0;
		TimeElement t = new TimeElement(start);
		assertEquals("start time should have been 0", start, t.getStartTime());
		assertEquals("nested duration should have been 0", 0, t.getNestedDuration());
		String name = "ContextName";
		t.setContextName(name);
		assertEquals("Get/Set ContextName failure", name, t.getContextName());

		long duration = 15;
		t.addNestedDuration(duration);

		assertEquals("start time should have been 0", start, t.getStartTime());
		assertEquals("nested duration should have been 15", duration, t.getNestedDuration());
	}

}
