package no.stelvio.common.context;

import java.util.Hashtable;

import no.stelvio.common.context.ThreadLocalMap;

import junit.framework.TestCase;

/**
 * ThreadLocalMap Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1055 $ $Author: psa2920 $ $Date: 2004-08-17 15:38:08 +0200 (Tue, 17 Aug 2004) $
 */
public class ThreadLocalMapTest extends TestCase {

	/**
	 * Constructor for ThreadLocalMapTest.
	 * @param arg0
	 */
	public ThreadLocalMapTest(String arg0) {
		super(arg0);
	}

	public void test() {
		TestThreadLocalMap map = new TestThreadLocalMap();
		Hashtable h = new Hashtable();
		h.put("key", "value");
		Hashtable o = (Hashtable) map.childValue(h);

		assertEquals(o, h);
		assertEquals(o.get("key"), h.get("key"));

		assertNull(map.childValue(null));
	}

	private class TestThreadLocalMap extends ThreadLocalMap {
		public Object childValue(Object parentValue) {
			return super.childValue(parentValue);
		}
	}
}
