package no.stelvio.common.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the DateDateUtil class.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2833 $ $Author: skb2930 $ $Date: 2006-03-10 10:50:32 +0100 (Fri, 10 Mar 2006) $
 */
public class DateUtilTest {
	private Date early;

	private Date late;

	/** Used for comparing dates. */
	private static final SimpleDateFormat THE_DATE_FORMAT;

	/** Instance of calendar to use. */
	private static final Calendar CALENDAR = Calendar.getInstance();

	private TimeZone jdkDefaultTimeZone;
	static {
		// Initialize the date format
		THE_DATE_FORMAT = (SimpleDateFormat) DateFormat.getDateInstance();
		THE_DATE_FORMAT.setLenient(false);
		THE_DATE_FORMAT.applyLocalizedPattern("dd.MM.yyyy");
	}

	/**
	 * Initializes the calendar instance to the current time.
	 */
	@Before
	public void setUp() {
		jdkDefaultTimeZone = TimeZone.getDefault();

		CALENDAR.setTimeInMillis(System.currentTimeMillis());

		Calendar cal = Calendar.getInstance();
		cal.set(1980, Calendar.FEBRUARY, 10);
		early = cal.getTime();
		cal.set(2000, 10, 10);
		late = cal.getTime();
	}

	/**
	 * Cleanup after test.
	 */
	@After
	public void tearDown() {
		// reset the timeZone if it has been manipulated by the test
		TimeZone.setDefault(jdkDefaultTimeZone);
	}

	/**
	 * Test findEarliestDateByDay.
	 */
	@Test
	public void findEarliestCompareWithNull() {
		Date expected = new GregorianCalendar(2008, 10, 20).getTime();
		Date result = DateUtil.findEarliestDateByDay(expected, null);
		assertEquals(expected, result);
	}

	/**
	 * Test findLatestDateByDay.
	 */
	@Test
	public void findLatestCompareWithNull() {
		Date expected = new GregorianCalendar(2008, 10, 20).getTime();
		Date result = DateUtil.findLatestDateByDay(expected, null);
		assertEquals(expected, result);
	}

	/**
	 * Test parseCompactDate.
	 */
	@Test
	public void parseCompactDateOK() {
		parseAndTest("31122006", calendar(2006, 12, 31), new DateParser() {
			public Date parse(final String date) {
				return DateUtil.parseCompactDate(date);
			}
		});
	}

	/**
	 * Test parseCompactDate.
	 */
	@Test
	public void parseCompactDateFailYear() {
		try {
			parseAndTest("311220", calendar(2006, 12, 31), new DateParser() {
				public Date parse(final String date) {
					return DateUtil.parseCompactDate(date);
				}
			});
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}
	}

	/**
	 * Test parseCompactDate.
	 */
	@Test
	public void parseCompactDateFailMonth() {
		try {
			parseAndTest("31132006", calendar(2006, 12, 31), new DateParser() {
				public Date parse(final String date) {
					return DateUtil.parseCompactDate(date);
				}
			});
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}
	}

	/**
	 * Test parseCompactDate.
	 */
	@Test
	public void parseCompactDateFailDay() {
		try {
			parseAndTest("00122006", calendar(2006, 12, 31), new DateParser() {
				public Date parse(final String date) {
					return DateUtil.parseCompactDate(date);
				}
			});
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}
	}

	/**
	 * Test parseCompactDate.
	 */
	@Test
	public void parseCompactDateFailFormat() {
		try {
			parseAndTest("31.12.2006", calendar(2006, 12, 31), new DateParser() {
				public Date parse(final String date) {
					return DateUtil.parseCompactDate(date);
				}
			});
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}
	}

	/**
	 * Test parseCicsDate.
	 */
	@Test
	public void parseCicsDateOK() {
		parseAndTest("20030722", calendar(2003, 7, 22), new DateParser() {
			public Date parse(final String date) {
				return DateUtil.parseCicsDate(date);
			}
		});
	}

	/**
	 * Test parseTssDate.
	 */
	@Test
	public void parseTssDateOk() {
		parseAndTest("20030722", calendar(2003, 7, 22), new DateParser() {
			public Date parse(final String date) {
				return DateUtil.parseTSSDate(date);
			}
		});
	}

	/**
	 * Test parseTssDate.
	 */
	@Test
	public void parseTssTidRegOk() {
		parseAndTest("200307222145", calendar(2003, 7, 22, 21, 45, 0), new DateParser() {
			public Date parse(final String date) {
				return DateUtil.parseTSSTidReg(date);
			}
		});
	}

	/**
	 * Test formatDBString.
	 */
	@Test
	public void formatDbStringOk() {
		formatAndTest("2003-07-22 21:45:36", calendar(2003, 7, 22, 21, 45, 36), new DateFormatter() {
			public String format(final Date date) {
				return DateUtil.formatDBString(date);
			}
		});
	}

	/**
	 * Test formatTssString.
	 */
	@Test
	public void formatTssStringOk() {
		formatAndTest("20030722", calendar(2003, 7, 22), new DateFormatter() {
			public String format(final Date date) {
				return DateUtil.formatTSSString(date);
			}
		});
	}

	/**
	 * Test formatCicsString.
	 */
	@Test
	public void formatCicsStringOk() {
		formatAndTest("20030722", calendar(2003, 7, 22), new DateFormatter() {
			public String format(final Date date) {
				return DateUtil.formatCICSString(date);
			}
		});
	}

	/**
	 * Test formatTSSTidRegString.
	 */
	@Test
	public void formatTssTidRegStringOk() {
		formatAndTest("200307222146", calendar(2003, 7, 22, 21, 46, 0), new DateFormatter() {
			public String format(final Date date) {
				return DateUtil.formatTSSTidRegString(date);
			}
		});
	}

	/**
	 * Test formatOppdragString.
	 */
	@Test
	public void formatOppdragStringOk() {
		formatAndTest("2003-07-22", calendar(2003, 7, 22), new DateFormatter() {
			public String format(final Date date) {
				return DateUtil.formatOppdragString(date);
			}
		});
	}

	/**
	 * Test formatInputDateString.
	 */
	@Test
	public void formatInputDateStringOk() {
		assertEquals("Not the correct format;", "22.07.2003", DateUtil.formatInputDateString("22.07.03"));
	}

	/**
	 * Test getYesterday.
	 */
	@Test
	public void getYesterdayOk() {
		final Calendar calendar = Calendar.getInstance();
		final Date now = new Date();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		DateUtil.setDateCreator(new DateUtil.DateCreator() {
			public Date createDate() {
				return now;
			}
		});

		assertEquals("Not the correct date;", calendar.getTime(), DateUtil.getYesterday());
	}

	/**
	 * Test first day of month.
	 */
	@Test
	public void getFirstDayOfMonthOk() {
		final Calendar calendar = Calendar.getInstance();

		calendar.set(2003, Calendar.MAY, 24);
		final Date date = calendar.getTime();

		calendar.set(2003, Calendar.MAY, 1);

		assertEquals("Not the correct date;", calendar.getTime(), DateUtil.getFirstDayOfMonth(date));
	}

	/**
	 * Test last day of month.
	 */
	@Test
	public void getLastDayOfMonthOk() {
		final Calendar calendar = Calendar.getInstance();

		calendar.set(2003, Calendar.MAY, 14);
		final Date date = calendar.getTime();

		calendar.set(2003, Calendar.MAY, 31);

		assertEquals("Not the correct date;", calendar.getTime(), DateUtil.getLastDayOfMonth(date));
	}

	/**
	 * Test parse date.
	 */
	@Test
	public void parseDateOK() {
		// Test parsing of valid norwegian numbers
		assertNull("Test parsing of null", DateUtil.parse(null));
		assertNull("Test parsing of an empty String", DateUtil.parse(""));

		// Note! Months 0-11
		CALENDAR.set(1973, 0, 14);
		assertTrue("Test parsing of a date in last century", isSameDay(DateUtil.parse("14.01.1973"), CALENDAR.getTime()));

		CALENDAR.set(2003, 6, 22);
		assertTrue("Test parsing of today's date", isSameDay(DateUtil.parse("22.07.2003"), CALENDAR.getTime()));
	}

	/**
	 * Test parse resets time part.
	 */
	@Test
	public void parseResetsTimePart() {
		CALENDAR.setTime(DateUtil.parse("22.07.2003 11:00"));

		assertEquals("Hours should have been reset;", 0, CALENDAR.get(Calendar.HOUR));
		assertEquals("Minutes should have been reset;", 0, CALENDAR.get(Calendar.MINUTE));
		assertEquals("Seconds should have been reset;", 0, CALENDAR.get(Calendar.SECOND));
		assertEquals("Milliseconds should have been reset;", 0, CALENDAR.get(Calendar.MILLISECOND));
	}

	/**
	 * Parse batch month resets day of month.
	 */
	@Test
	public void parseBatchMonthResetsDayOfMonth() {
		CALENDAR.setTime(DateUtil.parseBatchMonth("200307", false));

		assertEquals("Day of month should have been reset;", 1, CALENDAR.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Test parsing of TPS date.
	 */
	@Test
	public void parseTps() {
		// TEST CASE 1: NORMAL VALID DATE IN LAST CENTURY
		CALENDAR.set(1973, 0, 14);
		assertTrue("Test parsing of a date in last century", isSameDay(DateUtil.parseTpsDate("1973-01-14"), CALENDAR.getTime()));

		// TEST CASE 2: NORMAL VALID DATE
		CALENDAR.set(2005, 0, 1);
		assertTrue("Test parsing of a date in this century", isSameDay(DateUtil.parseTpsDate("2005-01-01"), CALENDAR.getTime()));

		// TEST CASE 3: INPUT IS NULL
		try {
			DateUtil.parseTpsDate(null);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// TEST CASE 4: INPUT IS NOT A VALID TPS DATE
		try {
			DateUtil.parseTpsDate("JallaBallaDate");
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test parsing of input date from screen.
	 */
	@Test
	public void parseInput() {
		// TEST CASE 1: NORMAL VALID DATE IN LAST CENTURY
		CALENDAR.set(1973, 0, 14);
		assertTrue("Test parsing of a date in last century: 14-01-1973", isSameDay(DateUtil.parseInputString("14-01-1973",
				false), CALENDAR.getTime()));
		assertTrue("Test parsing of a date in last century 14.01.1973", isSameDay(DateUtil
				.parseInputString("14.01.1973", false), CALENDAR.getTime()));
		assertTrue("Test parsing of a date in last century 14/01/1973", isSameDay(DateUtil
				.parseInputString("14/01/1973", false), CALENDAR.getTime()));
		assertTrue("Test parsing of a date in last century 14\01\1973", isSameDay(DateUtil.parseInputString("14\\01\\1973",
				false), CALENDAR.getTime()));
		assertTrue("Test parsing of a date in last century 140173", isSameDay(DateUtil.parseInputString("140173", false),
				CALENDAR.getTime()));

		// TEST CASE 2: NORMAL VALID DATE
		CALENDAR.set(2005, 0, 1);
		assertTrue("Test parsing of a date in last century: 01-01-2005", isSameDay(DateUtil.parseInputString("01-01-2005",
				false), CALENDAR.getTime()));
		assertTrue("Test parsing of a date in last century 01.01.2005", isSameDay(DateUtil
				.parseInputString("01.01.2005", false), CALENDAR.getTime()));
		assertTrue("Test parsing of a date in last century 01/01/2005", isSameDay(DateUtil
				.parseInputString("01/01/2005", false), CALENDAR.getTime()));
		assertTrue("Test parsing of a date in last century 01\01\2005", isSameDay(DateUtil.parseInputString("01\\01\\2005",
				false), CALENDAR.getTime()));
		assertTrue("Test parsing of a date in last century 010105", isSameDay(DateUtil.parseInputString("010105", false),
				CALENDAR.getTime()));

		// TEST CASE 3: INPUT IS NULL
		try {
			DateUtil.parseInputString(null, false);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// TEST CASE 4: INPUT IS NOT A VALID DATE
		try {
			DateUtil.parseInputString("Jallaball", false);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test parse date fails.
	 */
	@Test
	public void parseDateFail() {
		// Test parsing of invalid dates
		try {
			DateUtil.parse(null, false);
			fail("Should have thrown an IllegalArgumentException for null");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			DateUtil.parse("", false);
			fail("Should have thrown an IllegalArgumentException for empty date");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			DateUtil.parse("XYZ", false);
			fail("Should have thrown an IllegalArgumentException for 'XYZ'");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			DateUtil.parse("14011973", false);
			fail("Should have thrown an IllegalArgumentException for '14011973'");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			DateUtil.parse("14/01/1973", false);
			fail("Should have thrown an IllegalArgumentException for '14/01/1973'");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			DateUtil.parse("14.00.1973", false);
			fail("Should have thrown an IllegalArgumentException for '14.00.1973'");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			DateUtil.parse("33.01.1973", false);
			fail("Should have thrown an IllegalArgumentException for '33.01.1973'");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			DateUtil.parse("14.01.73", false);
			fail("Should have thrown an IllegalArgumentException for '14.01.73'");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test format date.
	 */
	@Test
	public void formatDate() {
		assertEquals("Test formatting of null", "", DateUtil.format(null));

		// Note! Months 0-11
		CALENDAR.set(1973, 0, 14);
		assertEquals("Test formatting of a valid date", "14.01.1973", DateUtil.format(CALENDAR.getTime()));
	}

	/**
	 * Test format time of day.
	 */
	@Test
	public void formatTimeOfDay() {
		TimeZone.setDefault(TimeZone.getTimeZone("CET"));
		Date dagensDato = new Date(12345678);
		assertEquals("Test formatting of a valid time of day ", "042545", DateUtil.formatTimeOfDay(dagensDato));
	}

	/**
	 * Test format monthly period.
	 */
	@Test
	public void formatMonthlyPeriod() {
		assertEquals("Test formatting of null", "", DateUtil.formatMonthlyPeriod(null));

		// Note! Months 0-11
		CALENDAR.set(1973, 0, 14);
		assertEquals("Test formatting of a valid date", "01.1973", DateUtil.formatMonthlyPeriod(CALENDAR.getTime()));
	}

	/**
	 * Test isAfterToday.
	 */
	@Test
	public void isAfterToday() {
		final Date now = new Date();

		try {
			DateUtil.isAfterToday(null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		assertFalse("Today is not after today", DateUtil.isAfterToday(now));

		final Calendar calendar = createCalendar(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		assertFalse("Yesterday is not after today", DateUtil.isAfterToday(calendar.getTime()));

		calendar.add(Calendar.DAY_OF_MONTH, 2);
		assertTrue("Tomorrow is after today", DateUtil.isAfterToday(calendar.getTime()));
	}

	/**
	 * Test isBeforeToday.
	 */
	@Test
	public void isBeforeToday() {
		final Date now = new Date();

		assertFalse("Null is not past today", DateUtil.isBeforeToday(null));
		assertFalse("Today is not past today", DateUtil.isBeforeToday(now));

		final Calendar calendar = createCalendar(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		assertTrue("Yesterday is past today", DateUtil.isBeforeToday(calendar.getTime()));

		calendar.add(Calendar.DAY_OF_MONTH, 2);
		assertFalse("Tomorrow is not past today", DateUtil.isBeforeToday(calendar.getTime()));
	}

	/**
	 * Create calendar.
	 * 
	 * @param now
	 *            now
	 * @return calendar
	 */
	private static Calendar createCalendar(final Date now) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);

		return calendar;
	}

	/**
	 * Test isBeforeDay.
	 */
	@Test
	public void isBeforeDay() {
		assertFalse("Null is not before today", DateUtil.isBeforeDay(null, null));

		final Date now = new Date();
		final Calendar firstCalendar = createCalendar(now);
		final Calendar secondCalendar = createCalendar(now);

		firstCalendar.add(Calendar.DAY_OF_MONTH, -1);
		assertTrue("First should be before second", DateUtil.isBeforeDay(firstCalendar.getTime(), secondCalendar.getTime()));

		firstCalendar.add(Calendar.DAY_OF_MONTH, 2);
		assertFalse("First should not before second", DateUtil.isBeforeDay(firstCalendar.getTime(), secondCalendar.getTime()));
	}

	/**
	 * Test isSameDay.
	 */
	@Test
	public void isSameDay() {
		assertFalse("Null is not the same as null", DateUtil.isSameDay(null, null));

		Calendar calendar2 = (Calendar) CALENDAR.clone();
		assertTrue("Date1 is same day as date2", DateUtil.isSameDay(CALENDAR.getTime(), calendar2.getTime()));

		CALENDAR.add(Calendar.DATE, 6);
		assertFalse("Date1 is not same day as date2", DateUtil.isSameDay(CALENDAR.getTime(), calendar2.getTime()));
	}

	/**
	 * Test that date should become first day in month before.
	 */
	@Test
	public void dateShouldBecomeFirstDayInMonthBefore() {
		CALENDAR.setLenient(false);
		// This is 1. november 2004
		CALENDAR.set(2004, 10, 1);

		// Run
		Date actualDate = DateUtil.findLastDayInMonthBefore(CALENDAR.getTime());

		// Check that it is 31. october 2004
		CALENDAR.set(2004, 9, 31);
		assertEquals("Not the correct date;", CALENDAR.getTime(), actualDate);

	}

	/**
	 * Testing the DateUtil.formatBatchMonthString().
	 */
	@Test
	public void formatBatchMonthString() {
		// With null throws Exceptions
		try {
			DateUtil.formatBatchMonthString(null);
			fail("Excepted a NullPointerException");
		} catch (NullPointerException ex) {
			assertTrue(true);
		}

		// Test with date
		CALENDAR.setLenient(false);
		// This is 1. september 2004
		CALENDAR.set(2004, 8, 1);

		assertEquals("200409", DateUtil.formatBatchMonthString(CALENDAR.getTime()));
	}

	/**
	 * Test parse batch month.
	 */
	@Test
	public void parseBatchMonth() {
		// With null throws Exceptions
		try {
			DateUtil.parseBatchMonth(null, false);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// With null return null
		assertEquals(DateUtil.parseBatchMonth(null, true), null);

		// With bad date string
		try {
			DateUtil.parseBatchMonth("2004-01", false);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// With good date string
		CALENDAR.setLenient(false);
		// This is 1. september 2004
		CALENDAR.set(2004, 8, 1);
		Date returnDate = DateUtil.parseBatchMonth("200409", false);

		assertTrue("Test with good date string not the same", isSameDay(returnDate, CALENDAR.getTime()));
	}

	/**
	 * Test parsing DB2 string.
	 */
	@Test
	public void parseDBString() {
		// TEST CASE 1: Normal operation
		DateUtil.parseDBString("2005-02-11 16:00:00", false);

		// TEST CASE 2: Invalid string
		try {
			DateUtil.parseDBString("2005-02-11xxx", false);
			fail("Test parsing of invalid string should throw a IllegalArgumentException; Not OK!");
		} catch (Exception e) {
			assertTrue("Test parsing of 2005-02-11xxx throws IllegalArgumentException", e instanceof IllegalArgumentException);
		}

		// TEST CASE 3: input is null and not allowed
		try {
			DateUtil.parseDBString(null, false);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// TEST CASE 4: input is null and is allowed
		assertNull("Test parsing of NULL and allowed should return NULL", DateUtil.parseDBString(null, true));
	}

	/**
	 * Metode som tester DateUtil.getRelativeDateByYear.
	 */
	@Test
	public void getRelativeDateByYear() {
		Date testDate = DateUtil.parse("01.01.2000");

		Date nextYear = DateUtil.getRelativeDateByYear(testDate, +1);
		Date prevYear = DateUtil.getRelativeDateByYear(testDate, -1);

		assertEquals("nextYear er feil", "01.01.2001", DateUtil.format(nextYear));
		assertEquals("lastYear er feil", "01.01.1999", DateUtil.format(prevYear));
	}

	/**
	 * Metode som tester DateUtil.getRelativeDateByMonth.
	 */
	@Test
	public void getRelativeDateByMonth() {
		Date testDate = DateUtil.parse("01.01.2000");

		Date nextMonth = DateUtil.getRelativeDateByMonth(testDate, +1);
		Date prevMonth = DateUtil.getRelativeDateByMonth(testDate, -1);

		assertEquals("nextMonth er feil", DateUtil.format(nextMonth), "01.02.2000");
		assertEquals("prevMonth er feil", DateUtil.format(prevMonth), "01.12.1999");
	}

	/**
	 * Metode som tester DateUtil.getRelativeDateByDay.
	 */
	@Test
	public void getRelativeDateByDay() {
		Date testDate = DateUtil.parse("01.01.2000");

		Date nextDay = DateUtil.getRelativeDateByDays(testDate, +1);
		Date prevDay = DateUtil.getRelativeDateByDays(testDate, -1);

		assertEquals("nextDay er feil", DateUtil.format(nextDay), "02.01.2000");
		assertEquals("prevDay er feil", DateUtil.format(prevDay), "31.12.1999");
	}

	/**
	 * Tester at isToday fungerer.
	 */
	@Test
	public void isToday() {
		assertTrue("Skal v�re dagens dato", DateUtil.isToday(new Date()));

		CALENDAR.set(GregorianCalendar.YEAR, 2005);
		CALENDAR.set(GregorianCalendar.MONTH, 0);
		CALENDAR.set(GregorianCalendar.DAY_OF_MONTH, 31);

		assertFalse("Skal ikke v�re ikke dagens dato", DateUtil.isToday(CALENDAR.getTime()));
	}

	/** Sjekker en dato som er siste i måneden. */
	@Test
	public void isLastDayOfMonthTrue() {
		CALENDAR.set(GregorianCalendar.YEAR, 2005);
		CALENDAR.set(GregorianCalendar.MONTH, 0);
		CALENDAR.set(GregorianCalendar.DAY_OF_MONTH, 31);

		assertTrue("Forventet siste i måneden.", DateUtil.isLastDayOfMonth(CALENDAR.getTime()));
	}

	/**
	 * Sjekker en dato som ikke er siste i måneden.
	 */
	@Test
	public void isLastDayOfMonthFalse() {
		CALENDAR.set(GregorianCalendar.YEAR, 2005);
		CALENDAR.set(GregorianCalendar.MONTH, 0);
		CALENDAR.set(GregorianCalendar.DAY_OF_MONTH, 30);

		assertFalse("Forventet ikke siste i måneden.", DateUtil.isLastDayOfMonth(CALENDAR.getTime()));
	}

	/** Sjekker en dato som er første i måneden. */
	@Test
	public void isLastDayOfMonthFirst() {
		CALENDAR.set(GregorianCalendar.YEAR, 2005);
		CALENDAR.set(GregorianCalendar.MONTH, 0);
		CALENDAR.set(GregorianCalendar.DAY_OF_MONTH, 1);

		assertFalse("Forventet ikke siste i måneden.", DateUtil.isLastDayOfMonth(CALENDAR.getTime()));
	}

	/**
	 * Sjekker at vi får IllegalArgumentException dersom vi sender inn null.
	 */
	@Test
	public void lastDayOfMonthThrowsException() {
		try {
			DateUtil.isLastDayOfMonth(null);
			fail("Skulle fått IllegalArgumentException");
		} catch (IllegalArgumentException ie) {
			assertTrue(true);
		}
	}

	/**
	 * Sjekker at vi får IllegalArgumentException dersom vi sender inn null.
	 */
	@Test
	public void firstDayOfMonthThrowsException() {
		try {
			DateUtil.isFirstDayOfMonth(null);
			fail("Skulle fått IllegalArgumentException");
		} catch (IllegalArgumentException ie) {
			assertTrue(true);
		}
	}

	/**
	 * Tester metoden IsBeforeDayYear.
	 */
	@Test
	public void isBeforeDayYear() {
		assertFalse("Null is not before today", DateUtil.isBeforeDay(null, null));

		Calendar calendar2 = (Calendar) CALENDAR.clone();
		calendar2.add(Calendar.YEAR, 1);
		assertTrue("Date1 is before date2", DateUtil.isBeforeDay(CALENDAR.getTime(), calendar2.getTime()));

		CALENDAR.add(Calendar.YEAR, 6);
		assertFalse("Date1 is not before date2", DateUtil.isBeforeDay(CALENDAR.getTime(), calendar2.getTime()));
	}

	/**
	 * Tester metoden IsBeforeDayYear.
	 */
	@Test
	public void isBeforeDayMonth() {
		assertFalse("Null is not before today", DateUtil.isBeforeDay(null, null));

		Calendar calendar2 = (Calendar) CALENDAR.clone();
		calendar2.add(Calendar.MONTH, 1);
		assertTrue("Date1 is before date2", DateUtil.isBeforeDay(CALENDAR.getTime(), calendar2.getTime()));

		CALENDAR.add(Calendar.MONTH, 6);
		assertFalse("Date1 is not before date2", DateUtil.isBeforeDay(CALENDAR.getTime(), calendar2.getTime()));
	}

	/**
	 * Test TPS born date formatting.
	 */
	@Test
	public void formatTpsBornDate() {
		// Note! Months 0-11
		CALENDAR.set(1956, 0, 1);
		assertEquals("010156", DateUtil.formatTpsBornDateString(CALENDAR.getTime()));
	}

	/**
	 * Test format.
	 */
	@Test
	public void format() {
		assertEquals("Should have returned empty String for null", "", DateUtil.format(null));
		assertEquals("Should have returned empty String for ETERNITY", "", DateUtil.format(DateUtil.ETERNITY));
		assertEquals("Should have returned empty String for any other date", "14.01.1973", DateUtil.format(java.sql.Date
				.valueOf("1973-01-14")));
	}

	/**
	 * Test the getYear method.
	 */
	@Test
	public void getYear() {
		CALENDAR.set(Calendar.YEAR, 2000);

		assertEquals("Year should be 2000;", 2000, DateUtil.getYear(CALENDAR.getTime()));
	}

	/**
	 * Test the getMonth method.
	 */
	@Test
	public void getMonth() {
		CALENDAR.set(Calendar.MONTH, Calendar.FEBRUARY);
		CALENDAR.set(Calendar.YEAR, 2000);
		CALENDAR.set(Calendar.DAY_OF_MONTH, 10);
		CALENDAR.set(Calendar.HOUR_OF_DAY, 12);
		CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
		CALENDAR.set(Calendar.MINUTE, 0);
		CALENDAR.set(Calendar.SECOND, 0);
		CALENDAR.set(Calendar.MILLISECOND, 0);

		Date testDate = CALENDAR.getTime();
		assertEquals("Year should be february; Not OK!", Calendar.FEBRUARY, DateUtil.getMonth(testDate));
	}

	/**
	 * Test the getFirstDateInYear method.
	 */
	@Test
	public void getFirstDateInYear() {
		CALENDAR.set(Calendar.YEAR, 2000);
		CALENDAR.set(Calendar.MONTH, Calendar.MARCH);
		CALENDAR.set(Calendar.DAY_OF_MONTH, 20);

		CALENDAR.setTime(DateUtil.getFirstDateInYear(CALENDAR.getTime()));

		assertEquals("Month should be JANUARY; Not OK!", Calendar.JANUARY, CALENDAR.get(Calendar.MONTH));
		assertEquals("Day of month should be 1; Not OK!", 1, CALENDAR.get(Calendar.DAY_OF_MONTH));
		assertEquals("Year should be 2000; Not OK!", 2000, CALENDAR.get(Calendar.YEAR));
	}

	/**
	 * Test the getLastDateInYear method.
	 */
	@Test
	public void getLastDateInYear() {
		CALENDAR.set(Calendar.YEAR, 2000);
		CALENDAR.set(Calendar.MONTH, Calendar.MARCH);
		CALENDAR.set(Calendar.DAY_OF_MONTH, 20);

		CALENDAR.setTime(DateUtil.getLastDateInYear(CALENDAR.getTime()));

		assertEquals("Month should be DECEMBER; Not OK!", Calendar.DECEMBER, CALENDAR.get(Calendar.MONTH));
		assertEquals("Day of month should be 31; Not OK!", 31, CALENDAR.get(Calendar.DAY_OF_MONTH));
		assertEquals("Year should be 2000; Not OK!", 2000, CALENDAR.get(Calendar.YEAR));
	}

	/**
	 * Test getMonthBetween.
	 */
	@Test
	public final void getMonthBetween() {
		CALENDAR.clear();
		CALENDAR.set(2004, 0, 1);
		Date from = CALENDAR.getTime();
		CALENDAR.set(2004, 2, 1);
		Date to = CALENDAR.getTime();
		assertEquals("Should be 2 month differnece.", 2, DateUtil.getMonthBetween(from, to));

		CALENDAR.set(2004, 10, 1);
		from = CALENDAR.getTime();
		CALENDAR.set(2005, 0, 1);
		to = CALENDAR.getTime();
		assertEquals("Should be 2 month differnece.", 2, DateUtil.getMonthBetween(from, to));

		CALENDAR.set(2004, 10, 1);
		from = CALENDAR.getTime();
		CALENDAR.set(2006, 0, 1);
		to = CALENDAR.getTime();
		assertEquals("Should be 14 month differnece.", 14, DateUtil.getMonthBetween(from, to));

		CALENDAR.set(2004, 0, 1);
		from = CALENDAR.getTime();
		CALENDAR.set(2004, 0, 1);
		to = CALENDAR.getTime();
		assertEquals("Should be 0 month differnece.", 0, DateUtil.getMonthBetween(from, to));

		// Changed from and to
		CALENDAR.set(2005, 0, 1);
		from = CALENDAR.getTime();
		CALENDAR.set(2004, 0, 1);
		to = CALENDAR.getTime();
		assertEquals("Should be 12 month differnece.", 12, DateUtil.getMonthBetween(from, to));
	}

	/**
	 * Tests that rolling 18 years back works correctly.
	 */
	@Test
	public final void get18YearsBack() {
		// TEST CASE 1: normal operasjon
		Calendar fasit = Calendar.getInstance();
		Date testDate = DateUtil.get18YearsBack(fasit.getTime());
		CALENDAR.setTime(testDate);

		assertEquals("Should be rolled back 18 years", fasit.get(Calendar.YEAR) - 18, CALENDAR.get(Calendar.YEAR));
		assertEquals("Month should not be changed", fasit.get(Calendar.MONTH), CALENDAR.get(Calendar.MONTH));
		assertEquals("Day should not be changed", fasit.get(Calendar.DAY_OF_MONTH), CALENDAR.get(Calendar.DAY_OF_MONTH));
		assertEquals("Hours should be cleared to the start of the day", 0, CALENDAR.get(Calendar.HOUR_OF_DAY));

		// TEST CASE 2: parameter is null
		assertNull("null as input should return null", DateUtil.get18YearsBack(null));
	}

	/**
	 * Test find maximum date.
	 */
	@Test
	public void findMaximumDate() {

		Date result = DateUtil.findLatestDateByDay(early, late);
		assertEquals(result, late);
		result = DateUtil.findLatestDateByDay(late, early);
		assertEquals(result, late);
		result = DateUtil.findLatestDateByDay(early, early);
		assertEquals(result, early);
	}

	/**
	 * Test find minimum date.
	 */
	@Test
	public void findMinimumDate() {

		Date result = DateUtil.findEarliestDateByDay(early, late);
		assertEquals(result, early);
		result = DateUtil.findEarliestDateByDay(late, early);
		assertEquals(result, early);
		result = DateUtil.findEarliestDateByDay(early, early);
		assertEquals(result, early);
	}

	/**
	 * Test after and before by day.
	 */
	@Test
	public void afterAndBeforeByDay() {
		assertTrue(DateUtil.isAfterByDay(late, early, true));
		assertFalse(DateUtil.isAfterByDay(early, late, true));

		assertTrue(DateUtil.isBeforeByDay(early, late, true));
		assertFalse(DateUtil.isBeforeByDay(late, early, true));

		assertTrue(DateUtil.isAfterByDay(late, late, true));
		assertFalse(DateUtil.isAfterByDay(late, late, false));

		assertTrue(DateUtil.isBeforeByDay(late, late, true));
		assertFalse(DateUtil.isBeforeByDay(late, late, false));

		assertTrue(DateUtil.isAfterByDay(late, null, true));
		assertFalse(DateUtil.isAfterByDay(null, late, false));

		assertTrue(DateUtil.isBeforeByDay(null, late, true));
		assertFalse(DateUtil.isBeforeByDay(late, null, false));

		assertTrue(DateUtil.isAfterByDay(null, null, true));
		assertFalse(DateUtil.isAfterByDay(null, null, false));

		assertTrue(DateUtil.isBeforeByDay(null, null, true));
		assertFalse(DateUtil.isBeforeByDay(null, null, false));

	}

	/**
	 * Test parse date.
	 * 
	 * @throws ParseException
	 *             parsing date failed
	 */
	@Test
	public void parseDate() throws ParseException {
		String illegalDate = "2000-13-12";
		boolean gotException = false;
		Date date = null;
		try {
			date = DateUtil.parseISOString(illegalDate);
		} catch (ParseException e) {
			gotException = true;
		}
		assertTrue("Should raise an exception, but did not", gotException);

		illegalDate = "2000.3.12";
		gotException = false;
		try {
			date = DateUtil.parseISOString(illegalDate);
		} catch (ParseException e) {
			gotException = true;
		}
		assertTrue("Should raise an exception, but did not", gotException);

		illegalDate = "3.12.2000";
		gotException = false;
		try {
			date = DateUtil.parseISOString(illegalDate);
		} catch (ParseException e) {
			gotException = true;
		}
		assertTrue("Should raise an exception, but did not", gotException);

		String bogus = "bogus";
		gotException = false;
		try {
			date = DateUtil.parseISOString(bogus);
		} catch (ParseException e) {
			gotException = true;
		}
		assertTrue("Should raise an exception, but did not", gotException);

		String iso1 = "2000-01-25T14:28:54";
		date = DateUtil.parseISOString(iso1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		assertEquals(2000, cal.get(Calendar.YEAR));
		assertEquals(1, cal.get(Calendar.MONTH) + 1);
		assertEquals(25, cal.get(Calendar.DAY_OF_MONTH));
		assertEquals(14, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(28, cal.get(Calendar.MINUTE));
		assertEquals(54, cal.get(Calendar.SECOND));
		assertEquals(0, cal.get(Calendar.MILLISECOND));

		String iso2 = "2005-05-17";
		date = DateUtil.parseISOString(iso2);
		cal.setTime(date);
		assertEquals(2005, cal.get(Calendar.YEAR));
		assertEquals(5, cal.get(Calendar.MONTH) + 1);
		assertEquals(17, cal.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, cal.get(Calendar.MINUTE));
		assertEquals(0, cal.get(Calendar.SECOND));
		assertEquals(0, cal.get(Calendar.MILLISECOND));

	}

	@Test
	public void shouldReturnTheFirstOfFebruaryWhenGetFirstDayOfNextMonthIsCalledWithAnyDateInJanuary() {

		Date dateInJanuary = DateUtil.createDate(2018, 1, 17);
		Date firstOfFebruary = DateUtil.createDate(2018, 2,1);

		assertThat(DateUtil.getFirstDayOfNextMonth(dateInJanuary), is(firstOfFebruary));
	}

	/**
	 * Is two dates the same date.
	 * 
	 * @param date1
	 *            first date
	 * @param date2
	 *            second date
	 * @return true if date1 and date2 is the same date
	 */
	private boolean isSameDay(final Date date1, final Date date2) {
		if (null == date1 && null != date2) {
			return false;
		} else if (null != date1 && null == date2) {
			return false;
		} else if (null == date1 && null == date2) {
			return true;
		}

		final String strDate1 = THE_DATE_FORMAT.format(date1);
		final String strDate2 = THE_DATE_FORMAT.format(date2);

		return strDate1.equals(strDate2);
	}

	/**
	 * Creates a calendar with the specified year, month and day of month. The time fields is cleared.
	 * 
	 * @param year
	 *            the year to create the calendar with.
	 * @param month
	 *            the month to create the calendar with.
	 * @param dayOfMonth
	 *            the day of month to create the calendar with.
	 * @return a calendar.
	 */
	private Calendar calendar(int year, int month, int dayOfMonth) {
		return calendar(year, month, dayOfMonth, 0, 0, 0);
	}

	/**
	 * Creates a calendar with the specified year, month, day of month, hour, minute and second.
	 * 
	 * @param year
	 *            the year to create the calendar with.
	 * @param month
	 *            the month to create the calendar with.
	 * @param dayOfMonth
	 *            the day of month to create the calendar with.
	 * @param hour
	 *            the hour to create the calendar with.
	 * @param minute
	 *            the minute to create the calendar with.
	 * @param second
	 *            the second to create the calendar with.
	 * @return a calendar.
	 */
	private Calendar calendar(int year, int month, int dayOfMonth, int hour, int minute, int second) {
		// Month is zero-based
		return new GregorianCalendar(year, month - 1, dayOfMonth, hour, minute, second);
	}

	/**
	 * Common code for parsing a date string and testing the result.
	 * 
	 * @param date
	 *            the date as a string which should be parsed.
	 * @param expected
	 *            holds the expected values from parsing as a Calendar.
	 * @param dateParser
	 *            the parser to use, that is, the code that should be tested.
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
	 * @param expected
	 *            the expected result from the formatting.
	 * @param calendar
	 *            the <code>Calendar</code> holding the date to test formatting on.
	 * @param dateFormatter
	 *            the formatter to use, that is, the code that should be tested.
	 */
	private void formatAndTest(String expected, final Calendar calendar, final DateFormatter dateFormatter) {
		assertEquals("Not the correct format;", expected, dateFormatter.format(calendar.getTime()));
	}

	/**
	 * Template interface that is implemented with the <code>DateUtil</code> parse method that should be tested.
	 */
	private interface DateParser {
		/**
		 * Parse a string and find the date.
		 * 
		 * @param date
		 *            date as a string
		 * @return date
		 */
		Date parse(final String date);
	}

	/**
	 * Template interface that is implemented with the <code>DateUtil</code> format method that should be tested.
	 */
	private interface DateFormatter {
		/**
		 * Format a date.
		 * 
		 * @param date
		 *            date
		 * @return date as a string
		 */
		String format(final Date date);
	}
}
