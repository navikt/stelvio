package no.stelvio.common.performance;

import no.stelvio.common.performance.LogMonitorImpl;
import no.stelvio.common.performance.MonitorKey;
import junit.framework.TestCase;

/**
 * LogMonitorImpl Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1062 $ $Author: psa2920 $ $Date: 2004-08-17 17:16:46 +0200 (Tue, 17 Aug 2004) $
 */
public class LogMonitorImplTest extends TestCase {

	/**
	 * Constructor for LogMonitorImplTest.
	 * @param arg0
	 */
	public LogMonitorImplTest(String arg0) {
		super(arg0);
	}

	public void test() {
		LogMonitorImpl m = new LogMonitorImpl();
		m.setLevel(5);

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
