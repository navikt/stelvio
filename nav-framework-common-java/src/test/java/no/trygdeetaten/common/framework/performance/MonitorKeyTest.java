package no.trygdeetaten.common.framework.performance;

import junit.framework.TestCase;

/**
 * MonitorKey Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1062 $ $Author: psa2920 $ $Date: 2004-08-17 17:16:46 +0200 (Tue, 17 Aug 2004) $
 */
public class MonitorKeyTest extends TestCase {

	/**
	 * Constructor for MonitorKeyTest.
	 * @param arg0
	 */
	public MonitorKeyTest(String arg0) {
		super(arg0);
	}

	public void test() {
		String description = "Description";
		int level = 9999;
		MonitorKey key = new MonitorKey(description, level);
		assertEquals("level error", level, key.getLevel());
		assertEquals("description error", description + ", " + level, key.toString());
	}

}
