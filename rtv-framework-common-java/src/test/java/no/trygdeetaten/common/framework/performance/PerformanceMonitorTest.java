package no.trygdeetaten.common.framework.performance;

import junit.framework.TestCase;

/**
 * PerformanceMonitor Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1062 $ $Author: psa2920 $ $Date: 2004-08-17 17:16:46 +0200 (Tue, 17 Aug 2004) $
 */
public class PerformanceMonitorTest extends TestCase {

	/**
	 * Constructor for PerformanceMonitorTest.
	 * @param arg0
	 */
	public PerformanceMonitorTest(String arg0) {
		super(arg0);
	}

	public void test() {
		
		// 1
		PerformanceMonitor.start(new MonitorKey("TOTAL (SYSTEM 1 + 2)", 1));

		// 2 a
		PerformanceMonitor.start(new MonitorKey("SUB SYSTEM", 2), "SUB SYSTEM 1");
		PerformanceMonitor.end(new MonitorKey("SUB SYSTEM", 2));

		// 2 b
		PerformanceMonitor.start(new MonitorKey("SUB SYSTEM", 2), "SUB SYSTEM 2");
		PerformanceMonitor.end(new MonitorKey("SUB SYSTEM", 2));

		// 1
		PerformanceMonitor.end(new MonitorKey("SUB SYSTEM", 1));

		// 1
		PerformanceMonitor.start(new MonitorKey("TOTAL (SYSTEM 1 + 2)", 1));

		// 2 a
		PerformanceMonitor.start(new MonitorKey("SUB SYSTEM", 2), "SUB SYSTEM 1");
		PerformanceMonitor.fail(new MonitorKey("SUB SYSTEM", 2), 432);

		// 1
		PerformanceMonitor.fail(new MonitorKey("SUB SYSTEM", 1));
		assertTrue(true);
	}

}
