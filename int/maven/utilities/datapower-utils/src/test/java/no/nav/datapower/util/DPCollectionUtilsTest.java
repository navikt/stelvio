package no.nav.datapower.util;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class DPCollectionUtilsTest extends TestCase {

	public DPCollectionUtilsTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

//	public void testNewLinkedList() {
//		fail("Not yet implemented");
//	}
//
//	public void testNewArrayList() {
//		fail("Not yet implemented");
//	}
//
//	public void testNewHashSet() {
//		fail("Not yet implemented");
//	}
//
//	public void testNewHashMap() {
//		fail("Not yet implemented");
//	}
//
//	public void testPrintLinesCollectionOfTPrintStream() {
//		fail("Not yet implemented");
//	}
//
//	public void testPrintLinesCollectionOfTPrintStreamString() {
//		fail("Not yet implemented");
//	}
//
//	public void testPrintLinesMapOfKVPrintStreamCharSequence() {
//		fail("Not yet implemented");
//	}

	public void testMapFromString() {
		String mapString = "{'mapEntry1Key ' : 'mapEntry1Value ', 'mapEntry2Key':'mapEntry2Value'}";
		Map<String, String> map = DPCollectionUtils.mapFromString(mapString);
		assertTrue("Should contain key 'mapEntry1Key'", map.containsKey("mapEntry1Key"));
		assertTrue("Should contain key 'mapEntry2Key'", map.containsKey("mapEntry2Key"));
		assertEquals("Incorrect value of 'mapEntry1'", "mapEntry1Value", map.get("mapEntry1Key"));
		assertEquals("Incorrect value of 'mapEntry2'", "mapEntry2Value", map.get("mapEntry2Key"));
	}

	public void testListFromString() {
		String listString = "['listEntry1 ', 'listEntry2']";
		List<String> list = DPCollectionUtils.listFromString(listString);
		assertEquals("Should contain exactly 2 entries", 2, list.size());
		assertTrue("Should contain entry 'listEntry1'", list.contains("listEntry1"));
		assertTrue("Should contain entry 'listEntry2'", list.contains("listEntry2"));
	}

}
