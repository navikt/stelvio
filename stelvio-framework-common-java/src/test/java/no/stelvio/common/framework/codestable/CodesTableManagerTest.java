package no.stelvio.common.framework.codestable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.jmock.MockObjectTestCase;
import org.jmock.Mock;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.cache.HashtableCache;
import no.stelvio.common.framework.codestable.CodesTableItem;
import no.stelvio.common.framework.codestable.CodesTableManager;
import no.stelvio.common.framework.codestable.CodesTableManager.Filter;
import no.stelvio.common.framework.error.SystemException;
import no.stelvio.common.framework.service.LocalService;
import no.stelvio.common.framework.service.ServiceResponse;

/**
 * Test class for CodesTableManager
 *
 * @author person356941106810, Accenture
 * @version $Id: CodesTableManagerTest.java 2594 2005-10-28 11:33:50Z skb2930 $
 */
public class CodesTableManagerTest extends MockObjectTestCase {
	private CodesTableManager manager;
	private Mock mockDelegate;

	public void testOnlySubclassesOfCodesTableItemWillBeLoaded() {
		try {
			manager.getCodesTable(String.class);
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Unexpected error code", FrameworkError.CODES_TABLE_INIT_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testNoCodestableNamesHaveBeenSetWillThrowException() {
		try {
			new CodesTableManager().init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Unexpected error code", FrameworkError.SERVICE_INIT_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testUsingFilterWillGetCorrectCodesTableEntries() {
		setupDelegate();

		final Map filteredCodesTable = manager.getFilteredCodesTable(CT1.class, new Filter() {
			public boolean include(CodesTableItem item) {
				return !"t1code1".equals(item.getKode());
			}
		});

		assertEquals("Not the correct size;", 2, filteredCodesTable.size());
	}

	public void testNoDelegateHasBeenSetWillThrowException() {
		manager.setDelegate(null);

		try {
			manager.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Unexpected error code", FrameworkError.SERVICE_INIT_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testNonExistantCodestableHasBeenSetWillThrowException() {
		setupDelegate();
		final List ctNames = new ArrayList(1);
		ctNames.add(String.class.getName());

		manager.setCodesTableNames(ctNames);

		try {
			manager.init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Unexpected error code", FrameworkError.CODES_TABLE_NOT_FOUND.getCode(), e.getErrorCode());
		}
	}

	public void testOk() {
		setupDelegate();

		manager.init();
	}

	public void testGetCodesTableThatDoesNotExist() {
		setupDelegate();

		try {
			manager.getCodesTable(CT.class);
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Incorrect error code", FrameworkError.CODES_TABLE_NOT_FOUND.getCode(), e.getErrorCode());
		}
	}

	public void testGetCodesTableThatHasNotBeenInitialized() {
		Map t1 = new HashMap();
		t1.put("t1code1", "t1decode1");

		final ServiceResponse resp = new ServiceResponse();
		resp.setData(CT.class.getName(), t1);

		mockDelegate.expects(once()).method("execute").will(returnValue(resp));

		Map codesTable = manager.getCodesTable(CT.class);

		assertNotNull("Should not be null", codesTable);
		assertEquals("Does not contain the expected code", "t1decode1", codesTable.get("t1code1"));
		assertNull("Contains an unexpected value", codesTable.get("t2decode1"));
	}

	public void testGetCodesTableItem() {
		setupDelegate();

		// test: get an item that does not exist
		CodesTableItem itemNotExist = manager.getCodesTableItem(CT3.class, "t3code15");
		assertNull("Test 1: unexpected item exists", itemNotExist);

		// test: get an item
		CodesTableItem itemExist = manager.getCodesTableItem(CT3.class, "t3code2");
		assertNotNull("Test 2: the item does not exist", itemExist);
		assertEquals("Test 3: Unexpected code", "t3code2", itemExist.getKode());
		assertEquals("Test 4: Unexpected dekode", "t3decode2", itemExist.getDekode());
	}

	public void testGetDecode() {
		setupDelegate();

		// test: get a dekode for a code that does not exist
		Object decode = manager.getDecode(CT3.class, "t3code15");
		assertNull("Test 1: unexpected dekode", decode);

		// test: get a dekode
		decode = manager.getDecode(CT3.class, "t3code1");
		assertNotNull("Test 2: dekode not found", decode);
		assertEquals("Test 3: unexptected dekode", "t3decode1", decode);
	}

	public void testGetCode() {
		setupDelegate();

		// test: get a dekode for a code that does not exist
		Object code = manager.getCode(CT3.class, "t3decode21");
		assertNull("Test 1: unexpected code", code);

		// test: get a dekode
		code = manager.getCode(CT3.class, "t3decode2");
		assertNotNull("Test 2: code not found", code);
		assertEquals("Test 3: unexptected dekode", "t3code2", code);
	}

	public void testSort() {
		setupDelegate();

		Map map = manager.getFilteredCodesTable(CT4.class, new Filter() {
			public boolean include(CodesTableItem item) {
				return true;
			}
		});

		ArrayList liste = new ArrayList(map.values());
		assertEquals("A is not # 1", "A", ((CodesTableItem) liste.get(0)).getDekode());
		assertEquals("B is not # 2", "B", ((CodesTableItem) liste.get(1)).getDekode());
		assertEquals("C is not # 3", "C", ((CodesTableItem) liste.get(2)).getDekode());
		assertEquals("D is not # 4", "D", ((CodesTableItem) liste.get(3)).getDekode());
		assertEquals("E is not # 5", "E", ((CodesTableItem) liste.get(4)).getDekode());
	}

	protected void setUp() {
		mockDelegate = mock(LocalService.class);

		manager = new CodesTableManager();
		manager.setCache(new HashtableCache());

		List ctNames = new ArrayList();
		ctNames.add(CT1.class.getName());
		ctNames.add(CT2.class.getName());
		ctNames.add(CT3.class.getName());
		ctNames.add(CT4.class.getName());
		manager.setCodesTableNames(ctNames);
		manager.setDelegate((LocalService) mockDelegate.proxy());
	}

	private void setupDelegate() {
		ServiceResponse resp = new ServiceResponse();

		Map t1 = new HashMap();
		t1.put("t1code1", new CT("t1code1", "t1decode1"));
		t1.put("t1code2", new CT("t1code2", "t1decode2"));
		t1.put("t1code3", new CT("t1code3", "t1decode3"));

		Map t2 = new HashMap();
		t2.put("t2code1", new CT("t2code1", "t2decode1"));
		t2.put("t2code2", new CT("t2code2", "t2decode2"));
		t2.put("t2code3", new CT("t2code3", "t2decode3"));

		Map t3 = new HashMap();
		t3.put("t3code1", new CT("t3code1", "t3decode1"));
		t3.put("t3code2", new CT("t3code2", "t3decode2"));

		Map map = new LinkedHashMap();
		map.put("a", new CT("a", "A"));
		map.put("b", new CT("b", "B"));
		map.put("c", new CT("c", "C"));
		map.put("d", new CT("d", "D"));
		map.put("e", new CT("e", "E"));

		resp.setData(CT1.class.getName(), t1);
		resp.setData(CT2.class.getName(), t2);
		resp.setData(CT3.class.getName(), t3);
		resp.setData(CT4.class.getName(), map);

		mockDelegate.expects(once()).method("execute").will(returnValue(resp));
	}

	private static class CT1 extends CT { }
	private static class CT2 extends CT { }
	private static class CT3 extends CT { }
	private static class CT4 extends CT { }
	private static class CT extends CodesTableItem {
		private String dekode;
		private String kode;

		CT() { }

		CT(final String kode, final String dekode) {
			this.kode = kode;
			this.dekode = dekode;
		}

		/**
		 * Returns the code represented by this item.
		 *
		 * @return the code.
		 */
		public String getKode() {
			return kode;
		}

		/**
		 * Returns the dekode represented by this item.
		 *
		 * @return the dekode.
		 */
		public String getDekode() {
			return dekode;
		}

		/**
		 * Sets the code represented by this item.
		 *
		 * @param string the code
		 */
		public void setKode(String string) {
		}

		/**
		 * Sets the dekode represented by this item
		 *
		 * @param string the dekode
		 */
		public void setDekode(String string) {
		}

		/**
		 * Returns the validity of this item.
		 *
		 * @return true if item is valid, false otherwise
		 */
		public boolean isErGyldig() {
			return false;
		}

		/**
		 * Returns the first date the item is valid from.
		 *
		 * @return the first date the item is valid
		 */
		public Date getFomDato() {
			return null;
		}

		/**
		 * Returns the last date the item is valid from.
		 *
		 * @return the last date the item is valid
		 */
		public Date getTomDato() {
			return null;
		}

		/**
		 * Sets the validity of this item.
		 *
		 * @param isValid true to mark item as valid, false otherwise
		 */
		public void setErGyldig(boolean isValid) {
		}

		/**
		 * Sets the first date the item is valid from.
		 *
		 * @param date the first date the item is valid
		 */
		public void setFomDato(Date date) {
		}

		/**
		 * Sets the last date the item is valid from.
		 *
		 * @param date the last date the item is valid
		 */
		public void setTomDato(Date date) {
		}
	}
}
