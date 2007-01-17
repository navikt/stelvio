package no.stelvio.common.context.support;

import java.util.Hashtable;

import junit.framework.TestCase;

/**
 * Log4jContext Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @author person4f9bc5bd17cc, Accenture
 * @version $Revision: 1057 $ $Author: psa2920 $ $Date: 2004-08-17 16:08:01 +0200 (Tue, 17 Aug 2004) $
 */
public class Log4jContextTest extends TestCase {

	/**
	 * Constructor for Log4jContextImplTest.
	 * @param arg0
	 */
	public Log4jContextTest(String arg0) {
		super(arg0);
	}

	public void test() {
		Log4jContext c = new Log4jContext();

		assertNull("Exported Hashtable should have been null", c.exportContext());

		Hashtable<String, Object> h = new Hashtable<String, Object>();
		Object o = "value";
		h.put("key", o);

		c.importContext(h);
		assertSame("Objects should have been the same", o, c.get("key"));

		assertNull("Object should have been null", c.get("key2"));

		Object o2 = "value2";
		c.put("key2", o2);

		assertSame("Objects should have been the same", o2, c.get("key2"));

		c.removeAll();
		assertNull("Object should have been null", c.get("key2"));

		c.importContext(null);
		try {
			c.importContext("Petter");
			fail("importContext(String) should have thrown ClassCastException");
		} catch (Throwable t) {
			assertTrue("importContext(String) should have thrown ClassCastException", t instanceof ClassCastException);
		}

		Object o3 = "value3";
		c.put("key3", o3);
		Hashtable h2 = (Hashtable) c.exportContext();
		assertSame("Objects should have been the same", o3, h2.get("key3"));

		c.removeAll();
		
		c.put("key4", "value4");
		c.importContext(h2);
		assertNull("Imported Hashtable should have cleared existing content", c.get("key4"));
	}

}
