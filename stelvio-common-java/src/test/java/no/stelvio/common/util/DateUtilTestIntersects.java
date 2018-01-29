package no.stelvio.common.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests to Check whether a date range intersects another date range or not.
 * 
 * To check for intersection there are several cases to be covered:
 * 
 * <pre>
 *       |-------|          (the first period, called period 1)
 *       |-------|		  0 same period is an intersection
 * |---|                  1 no intersection with period 1
 *                 |----| 2 no intersection with period 1
 *  |-------|             3 intersection, it ends before period 1 ends
 *             |------|   4 intersection, it starts before period 1 ends)
 * |----------------|     5 intersection, starts before and ends after period 1
 *                
 * |-----|				  6 Special case, ends when period 1 starts
 *               |------| 7 Special case, begins when period 1 ends
 * </pre>
 * 
 * @author person7c5197dbb870 (Capgemini)
 * 
 */
public class DateUtilTestIntersects {

	private Date d1Start;

	private Date d1End;

	private Date d2Start;

	private Date d2End;

	private Calendar cal;

	/**
	 * Define date 1.
	 * 
	 */
	@Before
	public void setUp() {
		cal = Calendar.getInstance();

		cal.set(1990, 0, 1);
		d1Start = cal.getTime();
		cal.set(2000, 0, 1);
		d1End = cal.getTime();
	}

	/**
	 * no intersection, ref the fig.
	 * 
	 */
	@Test
	public void testCase1() {
		cal.set(1980, 0, 1);
		d2Start = cal.getTime();
		cal.set(1985, 11, 31);
		d2End = cal.getTime();
		boolean intersects = DateUtil.intersects(d1Start, d1End, d2Start, d2End, false);
		assertFalse("Date 1 starts after date 2 ends, but an intersect was reported", intersects);
	}

	/**
	 * no intersection, ref the fig.
	 * 
	 */
	@Test
	public void testCase2() {
		cal.set(2000, 0, 2);
		d2Start = cal.getTime();
		cal.set(2010, 0, 1);
		d2End = cal.getTime();
		boolean intersects = DateUtil.intersects(d1Start, d1End, d2Start, d2End, false);
		assertFalse("Date 1 ends before date 2 starts, but an intersect was reported", intersects);
	}

	/**
	 * intersection, ref the fig.
	 * 
	 */
	@Test
	public void testCase3() {
		cal.set(1985, 0, 1);
		d2Start = cal.getTime();
		cal.set(1995, 0, 1);
		d2End = cal.getTime();
		boolean intersects = DateUtil.intersects(d1Start, d1End, d2Start, d2End, false);
		assertTrue("Date 2 ends before date 1 ends, but NO intersection was reported!", intersects);
	}

	/**
	 * intersection, ref the fig.
	 */
	@Test
	public void testCase4() {
		cal.set(1997, 0, 1);
		d2Start = cal.getTime();
		cal.set(2005, 0, 1);
		d2End = cal.getTime();
		boolean intersects = DateUtil.intersects(d1Start, d1End, d2Start, d2End, false);
		assertTrue("Date 2 starts before date 1 ends, but NO inserction was reported!", intersects);
	}

	/**
	 * intersection, ref the fig.
	 * 
	 */
	@Test
	public void testCase5() {
		cal.set(1989, 0, 1);
		d2Start = cal.getTime();
		cal.set(2005, 0, 1);
		d2End = cal.getTime();
		boolean intersects = DateUtil.intersects(d1Start, d1End, d2Start, d2End, false);
		assertTrue("Date 2 starts before date 1 starts and ends after date 1 ends, but NO inserction was reported!", 
				intersects);
	}

	/**
	 * special case version a, ref the fig.
	 * 
	 */
	@Test
	public void testCase6a() {
		cal.set(1985, 0, 1);
		d2Start = cal.getTime();
		cal.set(1990, 0, 1);
		d2End = cal.getTime();
		boolean intersects = DateUtil.intersects(d1Start, d1End, d2Start, d2End, true);
		assertTrue("Treats contact as intersection, but got none", intersects);
	}

	/**
	 * special case version b, ref the fig.
	 * 
	 */
	@Test
	public void testCase6b() {
		cal.set(1985, 0, 1);
		d2Start = cal.getTime();
		cal.set(1990, 0, 1);
		d2End = cal.getTime();
		boolean intersects = DateUtil.intersects(d1Start, d1End, d2Start, d2End, false);
		assertFalse("Treats contact not as intersection, but got one", intersects);
	}

	/**
	 * special case version a, ref the fig.
	 * 
	 */
	@Test
	public void testCase7a() {
		cal.set(2000, 0, 1);
		d2Start = cal.getTime();
		cal.set(2005, 0, 1);
		d2End = cal.getTime();
		boolean intersects = DateUtil.intersects(d1Start, d1End, d2Start, d2End, true);
		assertTrue("Treats contact as intersection, but got none", intersects);
	}

	/**
	 * special case version b, ref the fig.
	 * 
	 */
	@Test
	public void testCase7b() {
		cal.set(2000, 0, 1);
		d2Start = cal.getTime();
		cal.set(2005, 0, 1);
		d2End = cal.getTime();
		boolean intersects = DateUtil.intersects(d1Start, d1End, d2Start, d2End, false);
		assertFalse("Treats contact not as intersection, but got one", intersects);
	}

}
