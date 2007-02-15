package no.stelvio.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

/**
 * Test the DateDateUtil class.
 *
 * @author person7553f5959484, Accenture
 * @version $Revision: 2833 $ $Author: skb2930 $ $Date: 2006-03-10 10:50:32 +0100 (Fri, 10 Mar 2006) $
 */
public class DateUtilTest extends TestCase {

	/** Used for comparing dates. */
	private static final SimpleDateFormat theDateFormat;
	/** Instance of calendar to use. */
	private static final Calendar calendar = Calendar.getInstance();

	static {
		// Initialize the date format
		theDateFormat = (SimpleDateFormat) DateFormat.getDateInstance();
		theDateFormat.setLenient(false);
		theDateFormat.applyLocalizedPattern("dd.MM.yyyy");
	}

	public void testParseCicsDateOK() {
		parseAndTest("20030722", calendar(2003, 7, 22), new DateParser() {
			public Date parse(final String date) {
				return DateUtil.parseCicsDate(date);
			}
		});
	}

	public void testParseTssDateOk() {
		parseAndTest("20030722", calendar(2003, 7, 22), new DateParser() {
			public Date parse(final String date) {
				return DateUtil.parseTSSDate(date);
			}
		});
	}

	public void testParseTssTidRegOk() {
		parseAndTest("200307222145", calendar(2003, 7, 22, 21, 45, 0), new DateParser() {
			public Date parse(final String date) {
				return DateUtil.parseTSSTidReg(date);
			}
		});
	}

	public void testFormatDb2StringOk() {
		formatAndTest("2003-07-22 21:45:36", calendar(2003, 7, 22, 21, 45, 36), new DateFormatter() {
			public String format(final Date date) {
				return DateUtil.formatDB2String(date);
			}
		});
	}

	public void testFormatTssStringOk() {
		formatAndTest("20030722", calendar(2003, 7, 22), new DateFormatter() {
					public String format(final Date date) {
						return DateUtil.formatTSSString(date);
					}
				});
	}

	public void testFormatCicsStringOk() {
		formatAndTest("20030722", calendar(2003, 7, 22), new DateFormatter() {
					public String format(final Date date) {
						return DateUtil.formatCICSString(date);
					}
				});
	}

	public void testFormatTssTidRegStringOk() {
		formatAndTest("200307222146", calendar(2003, 7, 22, 21, 46, 0), new DateFormatter() {
					public String format(final Date date) {
						return DateUtil.formatTSSTidRegString(date);
					}
				});
	}

	public void testFormatOppdragStringOk() {
		formatAndTest("2003-07-22", calendar(2003, 7, 22), new DateFormatter() {
			public String format(final Date date) {
				return DateUtil.formatOppdragString(date);
			}
		});
	}

	public void testFormatInputDateStringOk() {
		assertEquals("Not the correct format;", "22.07.2003", DateUtil.formatInputDateString("22.07.03"));
	}

	public void testGetYesterdayOk() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		assertEquals("Not the correct date;", calendar.getTime(), DateUtil.getYesterday());
	}

	public void testGetFirstDayOfMonthOk() {
		final Calendar calendar = Calendar.getInstance();

		calendar.set(2003, Calendar.MAY, 24);
		final Date date = calendar.getTime();

		calendar.set(2003, Calendar.MAY, 1);

		assertEquals("Not the correct date;", calendar.getTime(), DateUtil.getFirstDayOfMonth(date));
	}

	public void testGetLastDayOfMonthOk() {
		final Calendar calendar = Calendar.getInstance();

		calendar.set(2003, Calendar.MAY, 14);
		final Date date = calendar.getTime();

		calendar.set(2003, Calendar.MAY, 31);

		assertEquals("Not the correct date;", calendar.getTime(), DateUtil.getLastDayOfMonth(date));
	}


	public void testParseDateOK() {
		// Test parsing of valid norwegian numbers
		assertNull("Test parsing of null", DateUtil.parse(null));
		assertNull("Test parsing of an empty String", DateUtil.parse(""));

		// Note! Months 0-11
		calendar.set(1973, 0, 14);
		assertTrue("Test parsing of a date in last century", isSameDay(DateUtil.parse("14.01.1973"), calendar.getTime()));

		calendar.set(2003, 6, 22);
		assertTrue("Test parsing of today's date", isSameDay(DateUtil.parse("22.07.2003"), calendar.getTime()));
	}

	public void testParseResetsTimePart() {
		calendar.setTime(DateUtil.parse("22.07.2003 11:00"));

		assertEquals("Hours should have been reset;", 0, calendar.get(Calendar.HOUR));
		assertEquals("Minutes should have been reset;", 0, calendar.get(Calendar.MINUTE));
		assertEquals("Seconds should have been reset;", 0, calendar.get(Calendar.SECOND));
		assertEquals("Milliseconds should have been reset;", 0, calendar.get(Calendar.MILLISECOND));
	}

	public void testParseBatchMonthResetsDayOfMonth() {
		calendar.setTime(DateUtil.parseBatchMonth("200307", false));

		assertEquals("Day of month should have been reset;", 1, calendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Test parsing of TPS date
	 *
	 * @throws Exception if there is a test that should not fail
	 */
	public void testParseTps() throws Exception {
		// TEST CASE 1: NORMAL VALID DATE IN LAST CENTURY
		calendar.set(1973, 0, 14);
		assertTrue(
				"Test parsing of a date in last century",
				isSameDay(DateUtil.parseTpsDate("1973-01-14"), calendar.getTime()));

		// TEST CASE 2: NORMAL VALID DATE
		calendar.set(2005, 0, 1);
		assertTrue(
				"Test parsing of a date in this century",
				isSameDay(DateUtil.parseTpsDate("2005-01-01"), calendar.getTime()));

		// TEST CASE 3: INPUT IS NULL
		try {
			DateUtil.parseTpsDate(null);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		// TEST CASE 4: INPUT IS NOT A VALID TPS DATE
		try {
			DateUtil.parseTpsDate("JallaBallaDate");
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// should happen
		}
	}

	/**
	 * Test parsing of input date from screen
	 *
	 * @throws Exception if there is a test that should not fail
	 */
	public void testParseInput() throws Exception {
		// TEST CASE 1: NORMAL VALID DATE IN LAST CENTURY
		calendar.set(1973, 0, 14);
		assertTrue(
				"Test parsing of a date in last century: 14-01-1973",
				isSameDay(DateUtil.parseInputString("14-01-1973", false), calendar.getTime()));
		assertTrue(
				"Test parsing of a date in last century 14.01.1973",
				isSameDay(DateUtil.parseInputString("14.01.1973", false), calendar.getTime()));
		assertTrue(
				"Test parsing of a date in last century 14/01/1973",
				isSameDay(DateUtil.parseInputString("14/01/1973", false), calendar.getTime()));
		assertTrue(
				"Test parsing of a date in last century 14\01\1973",
				isSameDay(DateUtil.parseInputString("14\\01\\1973", false), calendar.getTime()));
		assertTrue(
				"Test parsing of a date in last century 140173",
				isSameDay(DateUtil.parseInputString("140173", false), calendar.getTime()));

		// TEST CASE 2: NORMAL VALID DATE
		calendar.set(2005, 0, 1);
		assertTrue(
				"Test parsing of a date in last century: 01-01-2005",
				isSameDay(DateUtil.parseInputString("01-01-2005", false), calendar.getTime()));
		assertTrue(
				"Test parsing of a date in last century 01.01.2005",
				isSameDay(DateUtil.parseInputString("01.01.2005", false), calendar.getTime()));
		assertTrue(
				"Test parsing of a date in last century 01/01/2005",
				isSameDay(DateUtil.parseInputString("01/01/2005", false), calendar.getTime()));
		assertTrue(
				"Test parsing of a date in last century 01\01\2005",
				isSameDay(DateUtil.parseInputString("01\\01\\2005", false), calendar.getTime()));
		assertTrue(
				"Test parsing of a date in last century 010105",
				isSameDay(DateUtil.parseInputString("010105", false), calendar.getTime()));

		// TEST CASE 3: INPUT IS NULL
		try {
			DateUtil.parseInputString(null, false);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		// TEST CASE 4: INPUT IS NOT A VALID DATE
		try {
			DateUtil.parseInputString("Jallaball", false);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// should happen
		}
	}

	public void testParseDateFail() {
		// Test parsing of invalid dates
		try {
			DateUtil.parse(null, false);
			fail("Should have thrown an IllegalArgumentException for null");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		try {
			DateUtil.parse("", false);
			fail("Should have thrown an IllegalArgumentException for empty date");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		try {
			DateUtil.parse("XYZ", false);
			fail("Should have thrown an IllegalArgumentException for 'XYZ'");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		try {
			DateUtil.parse("14011973", false);
			fail("Should have thrown an IllegalArgumentException for '14011973'");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		try {
			DateUtil.parse("14/01/1973", false);
			fail("Should have thrown an IllegalArgumentException for '14/01/1973'");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		try {
			DateUtil.parse("14.00.1973", false);
			fail("Should have thrown an IllegalArgumentException for '14.00.1973'");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		try {
			DateUtil.parse("33.01.1973", false);
			fail("Should have thrown an IllegalArgumentException for '33.01.1973'");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		try {
			DateUtil.parse("14.01.73", false);
			fail("Should have thrown an IllegalArgumentException for '14.01.73'");
		} catch (IllegalArgumentException e) {
			// should happen
		}
	}

	public void testFormatDate() {
		assertEquals("Test formatting of null", "", DateUtil.format(null));

		// Note! Months 0-11
		calendar.set(1973, 0, 14);
		assertEquals("Test formatting of a valid date", "14.01.1973", DateUtil.format(calendar.getTime()));
	}

	public void testFormatTimeOfDay() {
		Date dagensDato = new Date(12345678);

		assertEquals("Test formatting of a valid time of day", "042545", DateUtil.formatTimeOfDay(dagensDato));
	}

	public void testFormatMonthlyPeriod() {
		assertEquals("Test formatting of null", "", DateUtil.formatMonthlyPeriod(null));

		// Note! Months 0-11
		calendar.set(1973, 0, 14);
		assertEquals("Test formatting of a valid date", "01.1973", DateUtil.formatMonthlyPeriod(calendar.getTime()));
	}

	public void testIsAfterToday() {
		final Date now = new Date();

		try {
			DateUtil.isAfterToday(null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// Should happen
		}

		assertFalse("Today is not after today", DateUtil.isAfterToday(now));

		final Calendar calendar = createCalendar(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		assertFalse("Yesterday is not after today", DateUtil.isAfterToday(calendar.getTime()));

		calendar.add(Calendar.DAY_OF_MONTH, 2);
		assertTrue("Tomorrow is after today", DateUtil.isAfterToday(calendar.getTime()));
	}

	public void testIsBeforeToday() {
		final Date now = new Date();

		assertFalse("Null is not past today", DateUtil.isBeforeToday(null));
		assertFalse("Today is not past today", DateUtil.isBeforeToday(now));

		final Calendar calendar = createCalendar(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		assertTrue("Yesterday is past today", DateUtil.isBeforeToday(calendar.getTime()));

		calendar.add(Calendar.DAY_OF_MONTH, 2);
		assertFalse("Tomorrow is not past today", DateUtil.isBeforeToday(calendar.getTime()));
	}

	private static Calendar createCalendar(final Date now) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);

		return calendar;
	}

	public void testIsBeforeDay() {
		assertFalse("Null is not before today", DateUtil.isBeforeDay(null, null));

		final Date now = new Date();
		final Calendar firstCalendar = createCalendar(now);
		final Calendar secondCalendar = createCalendar(now);

		firstCalendar.add(Calendar.DAY_OF_MONTH, -1);
		assertTrue("First should be before second", DateUtil.isBeforeDay(firstCalendar.getTime(), secondCalendar.getTime()));

		firstCalendar.add(Calendar.DAY_OF_MONTH, 2);
		assertFalse("First should not before second", DateUtil.isBeforeDay(firstCalendar.getTime(), secondCalendar.getTime()));
	}

	public void testIsSameDay() {
		assertFalse("Null is not the same as null", DateUtil.isSameDay(null, null));

		Calendar calendar2 = (Calendar) calendar.clone();
		assertTrue("Date1 is same day as date2", DateUtil.isSameDay(calendar.getTime(), calendar2.getTime()));

		calendar.add(Calendar.DATE, 6);
		assertFalse("Date1 is not same day as date2", DateUtil.isSameDay(calendar.getTime(), calendar2.getTime()));
	}

	public void testDateShouldBecomeFirstDayInMonthBefore() {
		calendar.setLenient(false);
		// This is 1. november 2004
		calendar.set(2004, 10, 1);

		// Run
		Date actualDate = DateUtil.findLastDayInMonthBefore(calendar.getTime());

		// Check that it is 31. october 2004
		calendar.set(2004, 9, 31);
		assertEquals("Not the correct date;", calendar.getTime(), actualDate);

	}

	/** Testing the DateUtil.formatBatchMonthString() */
	public void testFormatBatchMonthString() {
		//With null throws Exceptions
		try {
			DateUtil.formatBatchMonthString(null);
			fail("Excepted a NullPointerException");
		} catch (NullPointerException ex) {
			// should happen
		}

		//Test with date
		calendar.setLenient(false);
		// This is 1. september 2004
		calendar.set(2004, 8, 1);

		assertEquals("200409", DateUtil.formatBatchMonthString(calendar.getTime()));
	}

	public void testParseBatchMonth() {
		//With null throws Exceptions
		try {
			DateUtil.parseBatchMonth(null, false);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		//With null return null
		assertEquals(DateUtil.parseBatchMonth(null, true), null);

		//With bad date string
		try {
			DateUtil.parseBatchMonth("2004-01", false);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		//With good date string
		calendar.setLenient(false);
		// This is 1. september 2004
		calendar.set(2004, 8, 1);
		Date returnDate = DateUtil.parseBatchMonth("200409", false);

		assertTrue("Test with good date string not the same", isSameDay(returnDate, calendar.getTime()));
	}

	/** Test parsing DB2 string */
	public void testParseDB2String() throws Exception {
		// TEST CASE 1: Normal operation
		DateUtil.parseDB2String("2005-02-11 16:00:00", false);

		// TEST CASE 2: Invalid string
		try {
			DateUtil.parseDB2String("2005-02-11xxx", false);
			fail("Test parsing of invalid string should throw a IllegalArgumentException; Not OK!");
		} catch (Exception e) {
			assertTrue("Test parsing of 2005-02-11xxx throws IllegalArgumentException", e instanceof IllegalArgumentException);
		}

		// TEST CASE 3: input is null and not allowed
		try {
			DateUtil.parseDB2String(null, false);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// should happen
		}

		// TEST CASE 4: input is null and is allowed
		assertNull("Test parsing of NULL and allowed should return NULL", DateUtil.parseDB2String(null, true));
	}

	/** Metode som tester DateUtil.getRelativeDateByYear */
	public void testGetRelativeDateByYear() {
		Date testDate = DateUtil.parse("01.01.2000");

		Date nextYear = DateUtil.getRelativeDateByYear(testDate, +1);
		Date prevYear = DateUtil.getRelativeDateByYear(testDate, -1);

		assertEquals("nextYear er feil", "01.01.2001", DateUtil.format(nextYear));
		assertEquals("lastYear er feil", "01.01.1999", DateUtil.format(prevYear));
	}

	/** Metode som tester DateUtil.getRelativeDateByMonth */
	public void testGetRelativeDateByMonth() {
		Date testDate = DateUtil.parse("01.01.2000");

		Date nextMonth = DateUtil.getRelativeDateByMonth(testDate, +1);
		Date prevMonth = DateUtil.getRelativeDateByMonth(testDate, -1);

		assertEquals("nextMonth er feil", DateUtil.format(nextMonth), "01.02.2000");
		assertEquals("prevMonth er feil", DateUtil.format(prevMonth), "01.12.1999");
	}

	/** Metode som tester DateUtil.getRelativeDateByDay */
	public void testGetRelativeDateByDay() {
		Date testDate = DateUtil.parse("01.01.2000");

		Date nextDay = DateUtil.getRelativeDateByDays(testDate, +1);
		Date prevDay = DateUtil.getRelativeDateByDays(testDate, -1);

		assertEquals("nextDay er feil", DateUtil.format(nextDay), "02.01.2000");
		assertEquals("prevDay er feil", DateUtil.format(prevDay), "31.12.1999");
	}

	/** Tester at isToday fungerer. */
	public void testIsToday() {
		assertTrue("Skal v�re dagens dato", DateUtil.isToday(new Date()));

		calendar.set(GregorianCalendar.YEAR, 2005);
		calendar.set(GregorianCalendar.MONTH, 0);
		calendar.set(GregorianCalendar.DAY_OF_MONTH, 31);

		assertFalse("Skal ikke v�re ikke dagens dato", DateUtil.isToday(calendar.getTime()));
	}


	/** Sjekker en dato som er siste i måneden. */
	public void testIsLastDayOfMonthTrue() {
		calendar.set(GregorianCalendar.YEAR, 2005);
		calendar.set(GregorianCalendar.MONTH, 0);
		calendar.set(GregorianCalendar.DAY_OF_MONTH, 31);

		assertTrue("Forventet siste i måneden.", DateUtil.isLastDayOfMonth(calendar.getTime()));
	}

	/** Sjekker en dato som ikke er siste i måneden. */
	public void testIsLastDayOfMonthFalse() {
		calendar.set(GregorianCalendar.YEAR, 2005);
		calendar.set(GregorianCalendar.MONTH, 0);
		calendar.set(GregorianCalendar.DAY_OF_MONTH, 30);

		assertFalse("Forventet ikke siste i måneden.", DateUtil.isLastDayOfMonth(calendar.getTime()));
	}

	/** Sjekker en dato som er første i måneden. */
	public void testIsLastDayOfMonthFirst() {
		calendar.set(GregorianCalendar.YEAR, 2005);
		calendar.set(GregorianCalendar.MONTH, 0);
		calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);

		assertFalse("Forventet ikke siste i måneden.", DateUtil.isLastDayOfMonth(calendar.getTime()));
	}

	/** Sjekker at vi får IllegalArgumentException dersom vi sender inn null */
	public void testLastDayOfMonthThrowsException() {
		try {
			DateUtil.isLastDayOfMonth(null);
			fail("Skulle fått IllegalArgumentException");
		} catch (IllegalArgumentException ie) {
			// should happen
		}
	}

	/** Sjekker at vi får IllegalArgumentException dersom vi sender inn null */
	public void testFirstDayOfMonthThrowsException() {
		try {
			DateUtil.isFirstDayOfMonth(null);
			fail("Skulle fått IllegalArgumentException");
		} catch (IllegalArgumentException ie) {
			// should happen
		}
	}

	/** Tester metoden IsBeforeDayYear. */
	public void testIsBeforeDayYear() {
		assertFalse("Null is not before today", DateUtil.isBeforeDay(null, null));

		Calendar calendar2 = (Calendar) calendar.clone();
		calendar2.add(Calendar.YEAR, 1);
		assertTrue("Date1 is before date2", DateUtil.isBeforeDay(calendar.getTime(), calendar2.getTime()));

		calendar.add(Calendar.YEAR, 6);
		assertFalse("Date1 is not before date2", DateUtil.isBeforeDay(calendar.getTime(), calendar2.getTime()));
	}

	/** Tester metoden IsBeforeDayYear. */
	public void testIsBeforeDayMonth() {
		assertFalse("Null is not before today", DateUtil.isBeforeDay(null, null));

		Calendar calendar2 = (Calendar) calendar.clone();
		calendar2.add(Calendar.MONTH, 1);
		assertTrue("Date1 is before date2", DateUtil.isBeforeDay(calendar.getTime(), calendar2.getTime()));

		calendar.add(Calendar.MONTH, 6);
		assertFalse("Date1 is not before date2", DateUtil.isBeforeDay(calendar.getTime(), calendar2.getTime()));
	}

	/**
	 * Test TPS born date formatting.
	 *
	 * @throws Exception if a exception is thrown that is not expected
	 */
	public void testFormatTpsBornDate() throws Exception {
		// Note! Months 0-11
		calendar.set(1956, 0, 1);
		assertEquals("010156", DateUtil.formatTpsBornDateString(calendar.getTime()));
	}

	public void testFormat() {
		assertEquals("Should have returned empty String for null", "", DateUtil.format(null));
		assertEquals("Should have returned empty String for ETERNITY", "", DateUtil.format(DateUtil.ETERNITY));
		assertEquals(
				"Should have returned empty String for any other date",
				"14.01.1973",
				DateUtil.format(java.sql.Date.valueOf("1973-01-14")));
	}

	/** Test the getYear method. */
	public void testGetYear() {
		calendar.set(Calendar.YEAR, 2000);

		assertEquals("Year should be 2000;", 2000, DateUtil.getYear(calendar.getTime()));
	}

	/** Test the getMonth method. */
	public void testGetMonth() {
		calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
		calendar.set(Calendar.YEAR, 2000);
		calendar.set(Calendar.DAY_OF_MONTH, 10);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date testDate = calendar.getTime();
		assertEquals("Year should be february; Not OK!", Calendar.FEBRUARY, DateUtil.getMonth(testDate));
	}

	/**
	 * Test the getFirstDateInYear method
	 *
	 * @throws Exception if a exception is thrown that is not expected
	 */
	public void testGetFirstDateInYear() throws Exception {
		calendar.set(Calendar.YEAR, 2000);
		calendar.set(Calendar.MONTH, Calendar.MARCH);
		calendar.set(Calendar.DAY_OF_MONTH, 20);

		calendar.setTime(DateUtil.getFirstDateInYear(calendar.getTime()));

		assertEquals("Month should be JANUARY; Not OK!", Calendar.JANUARY, calendar.get(Calendar.MONTH));
		assertEquals("Day of month should be 1; Not OK!", 1, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals("Year should be 2000; Not OK!", 2000, calendar.get(Calendar.YEAR));
	}

	/**
	 * Test the getLastDateInYear method
	 *
	 * @throws Exception if a exception is thrown that is not expected
	 */
	public void testGetLastDateInYear() throws Exception {
		calendar.set(Calendar.YEAR, 2000);
		calendar.set(Calendar.MONTH, Calendar.MARCH);
		calendar.set(Calendar.DAY_OF_MONTH, 20);

		calendar.setTime(DateUtil.getLastDateInYear(calendar.getTime()));

		assertEquals("Month should be DECEMBER; Not OK!", Calendar.DECEMBER, calendar.get(Calendar.MONTH));
		assertEquals("Day of month should be 31; Not OK!", 31, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals("Year should be 2000; Not OK!", 2000, calendar.get(Calendar.YEAR));
	}

	/** Test getMonthBetween */
	public final void testGetMonthBetween() {
		calendar.clear();
		calendar.set(2004, 0, 1);
		Date from = calendar.getTime();
		calendar.set(2004, 2, 1);
		Date to = calendar.getTime();
		assertEquals("Should be 2 month differnece.", 2, DateUtil.getMonthBetween(from, to));

		calendar.set(2004, 10, 1);
		from = calendar.getTime();
		calendar.set(2005, 0, 1);
		to = calendar.getTime();
		assertEquals("Should be 2 month differnece.", 2, DateUtil.getMonthBetween(from, to));

		calendar.set(2004, 10, 1);
		from = calendar.getTime();
		calendar.set(2006, 0, 1);
		to = calendar.getTime();
		assertEquals("Should be 14 month differnece.", 14, DateUtil.getMonthBetween(from, to));

		calendar.set(2004, 0, 1);
		from = calendar.getTime();
		calendar.set(2004, 0, 1);
		to = calendar.getTime();
		assertEquals("Should be 0 month differnece.", 0, DateUtil.getMonthBetween(from, to));

		// Changed from and to
		calendar.set(2005, 0, 1);
		from = calendar.getTime();
		calendar.set(2004, 0, 1);
		to = calendar.getTime();
		assertEquals("Should be 12 month differnece.", 12, DateUtil.getMonthBetween(from, to));
	}

	/** Tests that rolling 18 years back works correctly. */
	public final void testGet18YearsBack() {
		// TEST CASE 1: normal operasjon
		Calendar fasit = Calendar.getInstance();
		Date testDate = DateUtil.get18YearsBack(fasit.getTime());
		calendar.setTime(testDate);

		assertEquals("Should be rolled back 18 years", fasit.get(Calendar.YEAR) - 18, calendar.get(Calendar.YEAR));
		assertEquals("Month should not be changed", fasit.get(Calendar.MONTH), calendar.get(Calendar.MONTH));
		assertEquals("Day should not be changed", fasit.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals("Hours should be cleared to the start of the day", 0, calendar.get(Calendar.HOUR_OF_DAY));

		// TEST CASE 2: parameter is null
		assertNull("null as input should return null", DateUtil.get18YearsBack(null));
	}

	/** Initializes the calendar instance to the current time. */
	protected void setUp() throws Exception {
		calendar.setTimeInMillis(System.currentTimeMillis());
	}

	private boolean isSameDay(final Date date1, final Date date2) {
		if (null == date1 && null != date2) {
			return false;
		} else if (null != date1 && null == date2) {
			return false;
		}

		final String strDate1 = theDateFormat.format(date1);
		final String strDate2 = theDateFormat.format(date2);

		return strDate1.equals(strDate2);
	}

	/**
	 * Creates a calendar with the specified year, month and day of month. The time fields is cleared.
	 *
	 * @param year the year to create the calendar with.
	 * @param month the month to create the calendar with.
	 * @param dayOfMonth the day of month to create the calendar with.
	 * @return a calendar.
	 */
	private Calendar calendar(int year, int month, int dayOfMonth) {
		return calendar(year, month, dayOfMonth, 0, 0, 0);
	}

	/**
	 * Creates a calendar with the specified year, month, day of month, hour, minute and second.
	 *
	 * @param year the year to create the calendar with.
	 * @param month the month to create the calendar with.
	 * @param dayOfMonth the day of month to create the calendar with.
	 * @param month the month to create the calendar with.
	 * @param minute the month to create the calendar with.
	 * @param second the month to create the calendar with.
	 * @return a calendar.
	 */
	private Calendar calendar(int year, int month, int dayOfMonth, int hour, int minute, int second) {
		// Month is zero-based
		return new GregorianCalendar(year, month - 1, dayOfMonth, hour, minute, second);
	}


	/**
	 * Common code for parsing a date string and testing the result.
	 *
	 * @param date the date as a string which should be parsed.
	 * @param expected holds the expected values from parsing as a Calendar.
	 * @param dateParser the parser to use, that is, the code that should be tested.
	 */
	private void parseAndTest(final String date, final Calendar expected, final DateParser dateParser) {
		final Calendar actual = Calendar.getInstance();
		actual.setTime(dateParser.parse(date));

		assertEquals("Not the correct year;", expected.get(Calendar.YEAR), actual.get(Calendar.YEAR));
		// Month is zero-based
		assertEquals("Not the correct month;", expected.get(Calendar.MONTH), actual.get(Calendar.MONTH));
		assertEquals("Not the correct day;", expected.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH));

		assertEquals("Not the correct hour;", expected.get(Calendar.HOUR_OF_DAY), actual.get(Calendar.HOUR_OF_DAY));
		assertEquals("Not the correct minute;", expected.get(Calendar.MINUTE), actual.get(Calendar.MINUTE));
		assertEquals("Not the correct second;", expected.get(Calendar.SECOND), actual.get(Calendar.SECOND));
		assertEquals("Milliseconds should be cleared;", 0, actual.get(Calendar.MILLISECOND));
	}

	/**
	 * Common code for formatting a date and testing the result.
	 *
	 * @param expected the expected result from the formatting.
	 * @param calendar the <code>Calendar</code> holding the date to test formatting on.
	 * @param dateFormatter the formatter to use, that is, the code that should be tested.
	 */
	private void formatAndTest(String expected, final Calendar calendar, final DateFormatter dateFormatter) {
		assertEquals("Not the correct format;", expected, dateFormatter.format(calendar.getTime()));
	}

	/** Template interface that is implemented with the <code>DateUtil</code> parse method that should be tested. */
	private static interface DateParser {
		Date parse(final String date);
	}

	/** Template interface that is implemented with the <code>DateUtil</code> format method that should be tested. */
	private static interface DateFormatter {
		String format(final Date date);
	}
}