package no.stelvio.common.codestable.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtpi;

/**
 * Test class for the DecodeNumericComparator class.
 * 
 * @author person6045563b8dec, Accenture
 * @version $revision$
 * 
 */
public class DecodeNumericComparatorTest {

	/**
	 * Test the compare method for code table items.
	 * 
	 */
	@Test
	public void testCompareToForItems() {
		DecodeNumericComparator comparator = new DecodeNumericComparator();
		assertTrue(comparator.compare(TestCti.createCtiNumericDecode1(), TestCti.createCtiNumericDecode2()) < 0);
		assertTrue(comparator.compare(TestCti.createCtiNumericDecode10(), TestCti.createCtiNumericDecode1()) > 0);
		assertTrue(comparator.compare(TestCti.createCtiNumericDecode10(), TestCti.createCtiNumericDecode10()) == 0);
	}

	/**
	 * Test the compare method for periodic code table items.
	 * 
	 */
	@Test
	public void testCompareToForPeriodicItems() {
		DecodeNumericComparator comparator = new DecodeNumericComparator();
		assertTrue(comparator.compare(TestCtpi.createCtpiNumericDecode1(), TestCtpi.createCtpiNumericDecode2()) < 0);
		assertTrue(comparator.compare(TestCtpi.createCtpiNumericDecode2(), TestCtpi.createCtpiNumericDecode1()) > 0);
		assertTrue(comparator.compare(TestCtpi.createCtpiNumericDecode2(), TestCtpi.createCtpiNumericDecode2()) == 0);
		assertTrue(comparator.compare(TestCtpi.createOverlapCtpiNumericDecode1(), 
				TestCtpi.createOverlapCtpiNumericDecode2()) < 0);
	}

	/**
	 * Test the comparator when used to sort a set of code table items.
	 * 
	 */
	@Test
	public void testComparatorSortItemSet() {
		SortedSet<TestCti> set = new TreeSet<TestCti>(new DecodeNumericComparator());
		set.addAll(createItems());
		Iterator<TestCti> iter = set.iterator();
		assertEquals(TestCti.createCtiNumericDecode1(), iter.next());
		assertEquals(TestCti.createCtiNumericDecode2(), iter.next());
		assertEquals(TestCti.createCtiNumericDecode10(), iter.next());
	}

	/**
	 * Test the comparator when used to sort a set of periodic code table items.
	 * 
	 */
	@Test
	public void testComparatorSortPeriodicItemSet() {
		SortedSet<TestCtpi> set = new TreeSet<TestCtpi>(new DecodeNumericComparator());
		set.addAll(createPeriodicItems());
		Iterator<TestCtpi> iter = set.iterator();
		assertEquals(TestCtpi.createCtpiNumericDecode1(), iter.next());
		assertEquals(TestCtpi.createOverlapCtpiNumericDecode1(), iter.next());
		assertEquals(TestCtpi.createOverlapCtpiNumericDecode2(), iter.next());
		assertEquals(TestCtpi.createCtpiNumericDecode2(), iter.next());
		assertEquals(TestCtpi.createCtpiNumericDecode10(), iter.next());
	}

	/**
	 * Create codetable items.
	 * 
	 * @return list of items
	 */
	private List<TestCti> createItems() {
		List<TestCti> list = new ArrayList<TestCti>();
		list.add(TestCti.createCtiNumericDecode1());
		list.add(TestCti.createCtiNumericDecode10());
		list.add(TestCti.createCtiNumericDecode2());
		return list;
	}

	/**
	 * Create periodic codestable items.
	 * 
	 * @return list of items
	 */
	private List<TestCtpi> createPeriodicItems() {
		List<TestCtpi> list = new ArrayList<TestCtpi>();
		list.add(TestCtpi.createCtpiNumericDecode1());
		list.add(TestCtpi.createCtpiNumericDecode10());
		list.add(TestCtpi.createCtpiNumericDecode2());
		list.add(TestCtpi.createOverlapCtpiNumericDecode1());
		list.add(TestCtpi.createOverlapCtpiNumericDecode2());
		return list;
	}

}
