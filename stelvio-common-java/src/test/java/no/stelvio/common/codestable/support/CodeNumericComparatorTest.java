package no.stelvio.common.codestable.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtpi;

import org.junit.Test;

/**
 * Test class for CodeNumericComparator, a comparator for sorting CodesTableItems with numeric code values.
 * 
 * @author person6045563b8dec, Accenture
 * 
 */
public class CodeNumericComparatorTest {

	/**
	 * Test the compare method for code table items.
	 * 
	 */
	@Test
	public void testCompareToForItems() {
		CodeNumericComparator comparator = new CodeNumericComparator();
		assertTrue(comparator.compare(TestCti.createCtiNumericCode1(), TestCti.createCtiNumericCode2()) < 0);
		assertTrue(comparator.compare(TestCti.createCtiNumericCode10(), TestCti.createCtiNumericCode1()) > 0);
		assertTrue(comparator.compare(TestCti.createCtiNumericCode10(), TestCti.createCtiNumericCode10()) == 0);
	}

	/**
	 * Test the compare method for periodic code table items.
	 * 
	 */
	@Test
	public void testCompareToForPeriodicItems() {
		CodeNumericComparator comparator = new CodeNumericComparator();
		assertTrue(comparator.compare(TestCtpi.createCtpiNumericCode1(), TestCtpi.createCtpiNumericCode2()) < 0);
		assertTrue(comparator.compare(TestCtpi.createCtpiNumericCode2(), TestCtpi.createCtpiNumericCode1()) > 0);
		assertTrue(comparator.compare(TestCtpi.createCtpiNumericCode2(), TestCtpi.createCtpiNumericCode2()) == 0);
		assertTrue(comparator.compare(TestCtpi.createOverlapCtpiNumericCode1(), TestCtpi.createOverlapCtpiNumericCode2()) < 0);
	}

	/**
	 * Test the comparator when used to sort a set of code table items.
	 * 
	 */
	@Test
	public void testComparatorSortItemSet() {
		SortedSet<TestCti> set = new TreeSet<TestCti>(new CodeNumericComparator());
		set.addAll(createItems());
		Iterator<TestCti> iter = set.iterator();
		assertEquals(TestCti.createCtiNumericCode1(), iter.next());
		assertEquals(TestCti.createCtiNumericCode2(), iter.next());
		assertEquals(TestCti.createCtiNumericCode10(), iter.next());
	}

	/**
	 * Test the comparator when used to sort a set of periodic code table items.
	 * 
	 */
	@Test
	public void testComparatorSortPeriodicItemSet() {
		SortedSet<TestCtpi> set = new TreeSet<TestCtpi>(new CodeNumericComparator());
		set.addAll(createPeriodicItems());
		Iterator<TestCtpi> iter = set.iterator();
		assertEquals(TestCtpi.createCtpiNumericCode1(), iter.next());
		assertEquals(TestCtpi.createOverlapCtpiNumericCode1(), iter.next());
		assertEquals(TestCtpi.createOverlapCtpiNumericCode2(), iter.next());
		assertEquals(TestCtpi.createCtpiNumericCode2(), iter.next());
		assertEquals(TestCtpi.createCtpiNumericCode10(), iter.next());
	}

	/**
	 * Creates items.
	 * 
	 * @return items
	 */
	private List<TestCti> createItems() {
		List<TestCti> list = new ArrayList<TestCti>();
		list.add(TestCti.createCtiNumericCode1());
		list.add(TestCti.createCtiNumericCode10());
		list.add(TestCti.createCtiNumericCode2());
		return list;
	}

	/**
	 * Create periodic items.
	 * 
	 * @return items
	 */
	private List<TestCtpi> createPeriodicItems() {
		List<TestCtpi> list = new ArrayList<TestCtpi>();
		list.add(TestCtpi.createCtpiNumericCode1());
		list.add(TestCtpi.createCtpiNumericCode10());
		list.add(TestCtpi.createCtpiNumericCode2());
		list.add(TestCtpi.createOverlapCtpiNumericCode1());
		list.add(TestCtpi.createOverlapCtpiNumericCode2());
		return list;
	}

}
