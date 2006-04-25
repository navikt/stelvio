package no.trygdeetaten.common.framework.monitor;

import junit.framework.TestCase;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;

/**
 * 
 * @author person356941106810, Accenture
 */
public class MonitorChainTest extends TestCase {

	/**
	 * Constructor for MonitorChainTest.
	 * @param arg0
	 */
	public MonitorChainTest(String arg0) {
		super(arg0);
	}

	public void testInit() {
		try {
			// TEST: No monitors are set
			MonitorChain chain = new MonitorChain();
			boolean ex = false;
			try {
				chain.init();
			} catch (SystemException e) {
				ex = true;
				assertEquals("Test 1:", FrameworkError.MONITORING_CHAIN_NO_MONITORS_ERROR.getCode(), e.getErrorCode());
			}

			assertTrue("Test 2", ex);
			ex = false;

			// TEST: all OK
			chain.setMonitors(new Monitor[] { new ErrorMonitor()});
			chain.init();

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception:" + e.getMessage());
		}
	}

	public void testPreExecute() {
		try {

			DummyMonitor monitor = new DummyMonitor();
			DummyManager manager = new DummyManager();
			Monitor[] monitors = new Monitor[] { manager, monitor };
			MonitorChain chain = new MonitorChain();
			chain.setMonitors(monitors);
			chain.init();

			MonitorEvent event = new MonitorEvent();

			chain.preExecute(event);
			assertTrue("Test 1", monitor.preMonitor);
			assertTrue("Test 2", manager.preMonitor);
			assertTrue("Test 3", manager.preManage);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception:" + e.getMessage());
		}
	}

	public void testPostExecute() {
		try {

			DummyMonitor monitor = new DummyMonitor();
			DummyManager manager = new DummyManager();
			Monitor[] monitors = new Monitor[] { manager, monitor };
			MonitorChain chain = new MonitorChain();
			chain.setMonitors(monitors);
			chain.init();

			MonitorEvent event = new MonitorEvent();

			chain.postExecute(event);
			assertTrue("Test 1", monitor.postMonitor);
			assertTrue("Test 2", manager.postMonitor);
			assertTrue("Test 3", manager.postManage);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception:" + e.getMessage());
		}
	}

}
