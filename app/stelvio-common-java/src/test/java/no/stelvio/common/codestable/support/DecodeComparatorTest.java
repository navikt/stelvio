package no.stelvio.common.codestable.support;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtpi;
import no.stelvio.common.util.ReflectUtil;

import org.junit.Test;

/**
 * Test class for DecodeComparator.
 * 
 * @author MA
 */
public class DecodeComparatorTest {

	/**
	 * Testing compareto for items.
	 */
	@Test
	public void testCompareToForItems() {
		DecodeComparator comparator = new DecodeComparator();
		assertTrue(comparator.compare(TestCti.createCti1(), TestCti.createCti2()) < 0);
		assertTrue(comparator.compare(TestCti.createCti2(), TestCti.createCti1()) > 0);
		assertTrue(comparator.compare(TestCti.createCti3(), TestCti.createCti3()) == 0);
	}

	/**
	 * Testing sort item set.
	 * 
	 * @throws NoSuchFieldException
	 *             no field
	 */
	@Test
	public void testComparatorSortItemSet() throws NoSuchFieldException {
		SortedSet<TestCti> set = new TreeSet<TestCti>(new DecodeComparator());
		set.addAll(createItems());
		Iterator<TestCti> iter = set.iterator();
		assertEquals(TestCti.createCti3(), iter.next());
		assertEquals(TestCti.createCti1(), iter.next());
		assertEquals(TestCti.createCti2(), iter.next());
		assertEquals(TestCti.createCti4(), iter.next());
	}

	/**
	 * Testing sort periodic item set.
	 * 
	 * @throws NoSuchFieldException
	 *             no field
	 */
	@Test
	public void testComparatorSortPeriodicItemSet() throws NoSuchFieldException {
		SortedSet<TestCtpi> set = new TreeSet<TestCtpi>(new DecodeComparator());
		set.addAll(createPeriodicItems());
		Iterator<TestCtpi> iter = set.iterator();
		assertEquals(createModifiedCtpi3(), iter.next());
		assertEquals(TestCtpi.createCtpi1(), iter.next());
		assertEquals(TestCtpi.createCtpi2(), iter.next());
		assertEquals(TestCtpi.createCtpi4(), iter.next());
	}

	/**
	 * Create items.
	 * 
	 * @return items
	 * @throws NoSuchFieldException
	 *             no field
	 */
	private List<TestCti> createItems() throws NoSuchFieldException {
		TestCti cti1 = TestCti.createCti1();
		TestCti cti2 = TestCti.createCti2();
		TestCti cti3 = TestCti.createCti3();
		TestCti cti4 = TestCti.createCti4();
		// This item should be first in a list. Wouldn't be first if normal Comparable interface is used
		ReflectUtil.setField(TestCti.class, cti3, "decode", "AAA_FIRST_DECODE");
		List<TestCti> list = new ArrayList<TestCti>();
		list.add(cti4);
		list.add(cti3);
		list.add(cti1);
		list.add(cti2);
		return list;
	}

	/**
	 * Create modified ctpi 3.
	 * 
	 * @return ctpi3
	 * @throws NoSuchFieldException
	 *             no field
	 */
	private TestCtpi createModifiedCtpi3() throws NoSuchFieldException {
		TestCtpi cti3 = TestCtpi.createCtpi3();
		TestCtpi cti1 = TestCtpi.createCtpi1();
		// This item should be first in a list. Wouldn't be first if normal Comparable interface is used
		ReflectUtil.setField(TestCtpi.class, cti3, "decode", cti1.getDecode());
		ReflectUtil.setField(TestCtpi.class, cti3, "fromDate", getDate(100, 1, 1)); // month should probably be 0
		return cti3;
	}

	/**
	 * Create periodic items.
	 * 
	 * @return periodic items
	 * @throws NoSuchFieldException
	 *             no field
	 */
	private List<TestCtpi> createPeriodicItems() throws NoSuchFieldException {
		TestCtpi cti1 = TestCtpi.createCtpi1();
		TestCtpi cti2 = TestCtpi.createCtpi2();
		TestCtpi cti3 = createModifiedCtpi3();
		TestCtpi cti4 = TestCtpi.createCtpi4();
		List<TestCtpi> list = new ArrayList<TestCtpi>();
		list.add(cti4);
		list.add(cti3);
		list.add(cti1);
		list.add(cti2);
		return list;
	}

	/**
	 * Get date.
	 * 
	 * @param year
	 *            year
	 * @param month
	 *            month
	 * @param date
	 *            day
	 * @return date
	 */
	private Date getDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

}
