package no.nav.common.framework.monitor;

import java.util.ArrayList;

import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import no.nav.common.framework.monitor.ErrorMonitorReportReceiver;
import no.nav.common.framework.monitor.ErrorReport;

import junit.framework.TestCase;

/**
 * 
 * @author person356941106810, Accenture
 */
public class ErrorMonitorMBeanTest extends TestCase {

	/**
	 * Constructor for ErrorMonitorMBeanTest.
	 * @param arg0
	 */
	public ErrorMonitorMBeanTest(String arg0) {
		super(arg0);
	}

	public void testReport() {
		try {
			ErrorMonitorReportReceiver mbean = new ErrorMonitorReportReceiver();
			mbean.setObjectName("Error");
			mbean.setReporterSize(10);
			mbean.init();
			
			mbean.report("Test 1", new Object[]{ new Long(100), new Long(5), new Double(0.05d) });
			mbean.report("Test 1", new Object[]{ new Long(100), new Long(6), new Double(0.06d) });
			mbean.report("Test 1", new Object[]{ new Long(100), new Long(7), new Double(0.07d) });
			
			mbean.report("Test 2", new Object[]{ new Long(50), new Long(5), new Double(0.1d) });
			mbean.report("Test 2", new Object[]{ new Long(50), new Long(6), new Double(0.12d) });
			mbean.report("Test 2", new Object[]{ new Long(50), new Long(7), new Double(0.14d) });
			
			ErrorReport latest = mbean.getLatestErrorReport("Test 5");
			assertNull("Test 1", latest);
			latest = mbean.getLatestErrorReport("Test 1");
			assertEquals("Test 2", 100, latest.getNumCalls());
			assertEquals("Test 3", 7, latest.getNumErrors());
			assertEquals("Test 4", 0.07d, latest.getErrorRatio(), 0.0d);
			
			latest = mbean.getLatestErrorReport("Test 2");
			assertEquals("Test 2", 50, latest.getNumCalls());
			assertEquals("Test 3", 7, latest.getNumErrors());
			assertEquals("Test 4", 0.14d, latest.getErrorRatio(), 0.0d);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexptected exception:" + e.getMessage());
		}
	}

	public void testInit() {
		try {
			ErrorMonitorReportReceiver mbean = new ErrorMonitorReportReceiver();
			mbean.setObjectName("ErrorMonitorMBean");
			mbean.init();
			// TEST: mbean is registered in the server
			ArrayList servers = MBeanServerFactory.findMBeanServer(null);
			MBeanServer server = (MBeanServer) servers.get(0);
			ObjectName name = new ObjectName("Monitoring:name=ErrorMonitorMBean");
			MBeanInfo info = server.getMBeanInfo(name);
			assertNotNull("Test 1", info);
			assertEquals("Test 2", "no.nav.common.framework.monitor.ErrorMonitorReportReceiver", info.getClassName());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexptected exception:" + e.getMessage());
		}
	}
}
