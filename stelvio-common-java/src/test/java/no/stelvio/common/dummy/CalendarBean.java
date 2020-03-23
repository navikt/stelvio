package no.stelvio.common.dummy;

import java.util.Calendar;

/**
 * Test bean for calendar.
 * 
 * @version $Id: CalendarBean.java 2408 2005-07-27 13:50:18Z jrd2920 $
 */
public class CalendarBean {
	private Calendar cal1;
	private Calendar cal2;
	private Calendar cal3;

	/**
	 * Get calendar 3.
	 * 
	 * @return cal3
	 */
	public Calendar getCal3() {
		return cal3;
	}

	/**
	 * Get calendar 1.
	 * 
	 * @return cal1
	 */
	public Calendar getCal1() {
		return cal1;
	}

	/**
	 * Get calendar 2.
	 * 
	 * @return cal2
	 */
	public Calendar getCal2() {
		return cal2;
	}

	/**
	 * Set calendar 3.
	 * 
	 * @param calendar
	 *            cal3
	 */
	public void setCal3(Calendar calendar) {
		cal3 = calendar;
	}

	/**
	 * Set calendar 1.
	 * 
	 * @param calendar
	 *            cal1
	 */
	public void setCal1(Calendar calendar) {
		cal1 = calendar;
	}

	/**
	 * Set calendar 2.
	 * 
	 * @param calendar
	 *            cal2
	 */
	public void setCal2(Calendar calendar) {
		cal2 = calendar;
	}
}
