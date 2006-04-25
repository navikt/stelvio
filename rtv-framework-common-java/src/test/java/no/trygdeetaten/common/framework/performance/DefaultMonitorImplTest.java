package no.trygdeetaten.common.framework.performance;

import junit.framework.TestCase;

/**
 * DefaultMonitorImpl Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1062 $ $Author: psa2920 $ $Date: 2004-08-17 17:16:46 +0200 (Tue, 17 Aug 2004) $
 */
public class DefaultMonitorImplTest extends TestCase {

	/**
	 * Constructor for DefaultMonitorImplTest.
	 * @param arg0
	 */
	public DefaultMonitorImplTest(String arg0) {
		super(arg0);
	}

	public void test() {
		DefaultMonitorImpl m = new DefaultMonitorImpl();

		// 1
		m.start(new MonitorKey("TOTAL (SYSTEM 1 + 2)", 1));

		// 2 a
		m.start(new MonitorKey("SUB SYSTEM", 2), "SUB SYSTEM 1");
		m.end(new MonitorKey("SUB SYSTEM", 2));

		// 2 b
		m.start(new MonitorKey("SUB SYSTEM", 2), "SUB SYSTEM 2");
		m.end(new MonitorKey("SUB SYSTEM", 2));

		// 1
		m.end(new MonitorKey("SUB SYSTEM", 1));

		// 1
		m.start(new MonitorKey("TOTAL (SYSTEM 1 + 2)", 1));

		// 2 a
		m.start(new MonitorKey("SUB SYSTEM", 2), "SUB SYSTEM 1");
		m.fail(new MonitorKey("SUB SYSTEM", 2), 432);

		// 1
		m.fail(new MonitorKey("SUB SYSTEM", 1));
		assertTrue(true);
	}

}
