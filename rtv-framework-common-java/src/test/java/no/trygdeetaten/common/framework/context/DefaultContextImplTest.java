package no.trygdeetaten.common.framework.context;

import java.util.Hashtable;

import junit.framework.TestCase;

/**
 * DefaultContextImpl Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1058 $ $Author: psa2920 $ $Date: 2004-08-17 16:15:17 +0200 (Tue, 17 Aug 2004) $
 */
public class DefaultContextImplTest extends TestCase {

	/**
	 * Constructor for DefaultContextImplTest.
	 * @param arg0
	 */
	public DefaultContextImplTest(String arg0) {
		super(arg0);
	}

	public void test() {

		DefaultContextImpl c = new DefaultContextImpl();

		assertNull("Object should have been null", c.get("key"));
		Object o = "value";
		c.put("key", o);
		assertSame("Objects should have been the same", o, c.get("key"));

		c.remove();
		assertNull("Object should have been null", c.get("key"));

		c.importContext(null);
		try {
			c.importContext("Petter");
			fail("importContext(String) should have thrown ClassCastException");
		} catch (Throwable t) {
			assertTrue("importContext(String) should have thrown ClassCastException", t instanceof ClassCastException);
		}

		Hashtable h = new Hashtable();
		h.put("key", o);

		c.importContext(h);
		assertSame("Objects should have been the same", o, c.get("key"));

		Object o2 = "value2";
		c.put("key2", o2);

		Hashtable h2 = (Hashtable) c.exportContext();
		assertSame("Objects should have been the same", o2, h2.get("key2"));

		c.remove();
		c.put("key3", "value3");
		c.importContext(h2);
		assertNull("Imported Hashtable should have cleared existing content", c.get("key3"));

		assertNull("Null key should have returned null value", c.get(null));
	}

}
