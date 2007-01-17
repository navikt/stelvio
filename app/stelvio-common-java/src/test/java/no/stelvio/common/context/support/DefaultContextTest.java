package no.stelvio.common.context.support;

import java.util.Hashtable;

import junit.framework.TestCase;

/**
 * DefaultContext Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @author person4f9bc5bd17cc, Accenture
 * @version $Revision: 1058 $ $Author: psa2920 $ $Date: 2004-08-17 16:15:17 +0200 (Tue, 17 Aug 2004) $
 */
public class DefaultContextTest extends TestCase {

	/**
	 * Constructor for DefaultContextImplTest.
	 * @param arg0
	 */
	public DefaultContextTest(String arg0) {
		super(arg0);
	}

	public void test() {

		DefaultContext c = new DefaultContext();

		assertNull("Object should have been null", c.get("key"));
		Object o = "value";
		c.put("key", o);
		assertSame("Objects should have been the same", o, c.get("key"));

		c.removeAll();
		assertNull("Object should have been null", c.get("key"));

		c.importContext(null);
		try {
			c.importContext("Petter");
			fail("importContext(String) should have thrown ClassCastException");
		} catch (Throwable t) {
			assertTrue("importContext(String) should have thrown ClassCastException", t instanceof ClassCastException);
		}

		Hashtable<String, Object> h = new Hashtable<String, Object>();
		h.put("key", o);

		c.importContext(h);
		assertSame("Objects should have been the same", o, c.get("key"));

		Object o2 = "value2";
		c.put("key2", o2);

		Hashtable h2 = (Hashtable) c.exportContext();
		assertSame("Objects should have been the same", o2, h2.get("key2"));

		c.removeAll();
		c.put("key3", "value3");
		c.importContext(h2);
		assertNull("Imported Hashtable should have cleared existing content", c.get("key3"));

		assertNull("Null key should have returned null value", c.get(null));
	}

}
