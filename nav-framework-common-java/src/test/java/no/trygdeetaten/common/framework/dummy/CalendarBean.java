package no.trygdeetaten.common.framework.dummy;

import java.util.Calendar;

/**
 * Test bean for calendar.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: CalendarBean.java 2408 2005-07-27 13:50:18Z jrd2920 $
 */
public class CalendarBean {
	Calendar cal1;
	Calendar cal2;
	Calendar cal3;

	/**
	 * @return
	 */
	public Calendar getCal3() {
		return cal3;
	}

	/**
	 * @return
	 */
	public Calendar getCal1() {
		return cal1;
	}

	/**
	 * @return
	 */
	public Calendar getCal2() {
		return cal2;
	}

	/**
	 * @param calendar
	 */
	public void setCal3(Calendar calendar) {
		cal3 = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setCal1(Calendar calendar) {
		cal1 = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setCal2(Calendar calendar) {
		cal2 = calendar;
	}
}
