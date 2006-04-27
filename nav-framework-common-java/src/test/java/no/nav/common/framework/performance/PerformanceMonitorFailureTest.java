package no.nav.common.framework.performance;

import java.io.File;
import java.io.IOException;

import no.nav.common.framework.performance.MonitorKey;
import no.nav.common.framework.performance.PerformanceMonitor;

import junit.framework.TestCase;

/**
 * PerformanceMonitor Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1062 $ $Author: psa2920 $ $Date: 2004-08-17 17:16:46 +0200 (Tue, 17 Aug 2004) $
 */
public class PerformanceMonitorFailureTest extends TestCase {

	/**
	 * Constructor for PerformanceMonitorTest.
	 * @param arg0
	 */
	public PerformanceMonitorFailureTest(String arg0) {
		super(arg0);
	}

	public void testErrorInPerformanceMonitoring() {

		try {
			rename("performance-monitoring.xml", "performance-monitoring.xml.backup");
			try {
				PerformanceMonitor.start(new MonitorKey("DESCRIPTION", 1));
				fail("Constructor should have thrown SystemException");
			} catch (Throwable t) {
				assertTrue("Constructor should have thrown ExceptionInInitializerError", t instanceof ExceptionInInitializerError);
			}
			rename("performance-monitoring.xml.backup", "performance-monitoring.xml");
		} catch (IOException io) {
			io.printStackTrace();
		}
		assertTrue(true);
	}

	private void rename(String filename, String newFilename) throws IOException {
		File file = new File(Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
		file.renameTo(new File(file.getParentFile(), newFilename));
	}

}
