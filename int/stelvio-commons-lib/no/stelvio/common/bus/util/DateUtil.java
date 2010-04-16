package no.stelvio.common.bus.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * Utility for localized parsing and formatting of dates.
 * 
 * @author person7553f5959484, Accenture
 * @author persone5d69f3729a8, Accenture
 * @author persond56073296bff R? Accenture
 * @author person68b1d6b96576, Accenture
 * @version $Id$
 */
public final class DateUtil {

	/** Used for testing the earlist year accepted for parsing. */
	private static final int EARLIEST_YEAR = 1000;
	/** Used for calculating months between 2 dates. */
	private static final int MONTHS_IN_YEAR = 12;

	/** Legal date formats */
	private static final String WID_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private static final String WID_DATE_FORMAT_NO_MILLIS = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String DATE_FORMAT = "dd.MM.yyyy";
	private static final String SHORT_DATE_FORMAT = "dd.MM.yy";
	private static final String DB2_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String CICS_DATE_FORMAT = "yyyyMMdd";
	private static final String TSS_DATE_FORMAT = "yyyyMMdd";
	private static final String TSS_TID_REG_FORMAT = "yyyyMMddHHmm";
	private static final String OPPDRAG_DATE_FORMAT = "yyyy-MM-dd";
	private static final String TPS_DATE_FORMAT = "yyyy-MM-dd";
	private static final String BATCH_MONTH_FORMAT = "yyyyMM";
	private static final String TPS_BORN_DATE_FORMAT = "ddMMyy";
	private static final String MONTHLY_PERIOD_FORMAT = "MM.yyyy";
	private static final String TIME_OF_DAY_FORMAT = "HHmmss";
	private static final String BREVGRRUPPE_DATA_FORMAT = "ddMMyyyy";

	/** Representation of ETERNITY (31.12.9999). */
	public static final Date ETERNITY = java.sql.Date.valueOf("9999-12-31");

	/** Should not be instantiated. */
	private DateUtil() {
	}

	/**
	 * Parse a string in following formats:
	 * 
	 * <ul>
	 * <li>ddMMyy
	 * <li>dd.MM.yy
	 * <li>dd.MM.yyyy
	 * <li>dd/MM/yy
	 * <li>dd/MM/yyyy
	 * <li>dd\MM\yy
	 * <li>dd\MM\yyyy
	 * <li>dd-MM-yy
	 * <li>dd-MM-yyyy
	 * </ul>
	 * 
	 * @param input
	 *            the String to parse
	 * @param allowNull
	 *            true to allow empty String
	 * @return a Date or null if input is null and it is allowed.
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	public static Date parseInputString(final String input, final boolean allowNull) throws IllegalArgumentException {
		if (StringUtils.isBlank(input)) {
			if (allowNull) {
				return null;
			} else {
				throw new IllegalArgumentException("null is a not valid input date");
			}
		}

		if (8 == input.length()) {
			return parseCommon(replaceSeparators(input), allowNull, SHORT_DATE_FORMAT, true, false, false);
		} else if (10 == input.length()) {
			return parseCommon(replaceSeparators(input), allowNull, DATE_FORMAT, true, false, false);
		} else if (6 == input.length()) {
			return parseCommon(input, allowNull, TPS_BORN_DATE_FORMAT, true, false, false);
		} else {
			throw new IllegalArgumentException("Failed to parse <" + input + ">. Not a valid input date");
		}
	}

	/**
	 * Parse a String into a Date.
	 * 
	 * @param input
	 *            the String to parse
	 * @return a Date, null if input is null or an empty String
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	public static Date parse(final String input) throws IllegalArgumentException {
		return parse(input, true);
	}

	/**
	 * Parse a String into a Date.
	 * 
	 * @param input
	 *            the String to parse
	 * @param allowNull
	 *            true to allow empty String
	 * @return a Date, null if input is null or an empty String
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	public static Date parse(final String input, final boolean allowNull) throws IllegalArgumentException {
		return parseCommon(input, allowNull, DATE_FORMAT, true, true, false);
	}

	/**
	 * Parser dato angitt som string fra TPS (formatet er yyyy-MM-dd)
	 * 
	 * @param input
	 *            dato som en streng
	 * @return Date objekt som er parset fra strengen
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	public static Date parseTpsDate(final String input) throws IllegalArgumentException {
		return parseCommon(input, true, TPS_DATE_FORMAT, false, true, false);
	}

	/**
	 * Parse a CICS date into a Java Date.
	 * 
	 * @param input
	 *            the String to parse.
	 * @return a Date, null if input is null or an empty String.
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	public static Date parseCicsDate(final String input) throws IllegalArgumentException {
		return parseCommon(input, true, CICS_DATE_FORMAT, true, true, false);
	}

	/**
	 * Parse a TSS date into a Java Date.
	 * 
	 * @param input
	 *            the String to parse.
	 * @return a Date, null if input is null or an empty String.
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	public static Date parseTSSDate(final String input) throws IllegalArgumentException {
		return parseCommon(input, true, TSS_DATE_FORMAT, true, true, false);
	}

	/**
	 * Parse a TSS tid_reg into a Java Date.
	 * 
	 * @param input
	 *            the String to parse.
	 * @return a Date, null if input is null or an empty String.
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	public static Date parseTSSTidReg(final String input) throws IllegalArgumentException {
		return parseCommon(input, true, TSS_TID_REG_FORMAT, true, false, false);
	}

	/**
	 * Parse a String into a Date with batch month pattern (yyyyMM).
	 * 
	 * @param input
	 *            the String to parse
	 * @param allowNull
	 *            true to allow empty String
	 * @return a Date, null if input is null or an empty String
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	public static Date parseBatchMonth(final String input) throws IllegalArgumentException {
		return parseCommon(input, true, BATCH_MONTH_FORMAT, true, true, true);
	}

	/**
	 * Parse a string in format yyyy-MM-dd HH:mm:ss into a Date
	 * 
	 * @param input
	 *            the String to parse
	 * @param allowNull
	 *            true to allow empty String
	 * @return a Date, null if input is null or an empty String
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	public static Date parseDB2String(final String input) throws IllegalArgumentException {
		return parseCommon(input, true, DB2_DATE_FORMAT, true, false, false);
	}

	/**
	 * Parse a string in format yyyy-MM-dd'T'HH:mm:ss'Z' GMT-time into a Date locale time
	 * 
	 * @param input
	 *            the String to parse
	 * @return a Date, null if input is null or an empty String
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	public static Date parseWIDString(final String input) throws IllegalArgumentException {

		Date output = null;

		if (StringUtils.isBlank(input)) {
			return output;
		} else {
			try {
				// Workaround for handling datetime's without milliseconds (.SSS)
				// checks if '.' is part of the input datestring
				String format = WID_DATE_FORMAT;
				if (input.length() > 19 && !".".equalsIgnoreCase(input.substring(19, 20))) {
					format = WID_DATE_FORMAT_NO_MILLIS;
				}
				// parse to a date in GMT-time, then change to local-time through calendar.
				DateFormat dateFormat = createDateFormat(format);

				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
				Date gmtDate = dateFormat.parse(input);
				Calendar cal = Calendar.getInstance(TimeZone.getDefault());
				cal.setTime(gmtDate);
				return cal.getTime();
			} catch (ParseException pe) {
				throw new IllegalArgumentException("Failed to parse " + input + ": " + pe);
			}
		}
	}

	/**
	 * Check if the date is last day of month.
	 * 
	 * @param date
	 *            the date to check.
	 * @return true if date is last day of month.
	 */
	public static boolean isLastDayOfMonth(Date date) {
		if (null == date) {
			throw new IllegalArgumentException("null is a not valid input date");
		}

		Calendar calendar = createCalendar(date);

		final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		final int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		return dayOfMonth == lastDayOfMonth;
	}

	/**
	 * Format a Date into a localized String (dd.MM.yyyy).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date or empty string for null input or "eternity" (31.12.9999).
	 * @see #ETERNITY
	 */
	public static String format(final Date input) {
		if (null == input || ETERNITY.equals(input)) {
			return null;
		} else {
			return createDateFormat(DATE_FORMAT).format(input);
		}
	}

	/**
	 * Formats a Date into a monthly period format (MM.yyyy).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatMonthlyPeriod(final Date input) {
		if (null == input) {
			return null;
		} else {
			return createDateFormat(MONTHLY_PERIOD_FORMAT).format(input);
		}
	}

	/**
	 * Formats a Date into WID format, in GMT-time (XSD-DateTime: yyyy-MM-dd'T'HH:mm:ss'Z').
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 * 
	 * 
	 */
	public static String formatWIDString(final Date input) {
		if (input == null) {
			return null;
		}
		DateFormat dateFormat = createDateFormat(WID_DATE_FORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(input);
	}

	/**
	 * Formats a Date into DB2 format (yyyy-MM-dd HH:mm:ss).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatDB2String(final Date input) {
		if (input == null) {
			return null;
		}
		return createDateFormat(DB2_DATE_FORMAT).format(input);
	}

	/**
	 * Formats a Date into CICS format (yyyyMMdd).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatCICSString(final Date input) {
		if (input == null) {
			return null;
		}
		return createDateFormat(CICS_DATE_FORMAT).format(input);
	}

	/**
	 * Formats a Date into Brevgruppe format (ddMMyyyy).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatBrevgruppeString(final Date input) {
		if (input == null) {
			return null;
		}
		return createDateFormat(BREVGRRUPPE_DATA_FORMAT).format(input);
	}

	/**
	 * Formats a Date into TSS format (yyyyMMdd).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatTSSString(final Date input) {
		if (null == input) {
			return null;
		} else {
			return createDateFormat(TSS_DATE_FORMAT).format(input);
		}

	}

	/**
	 * Formats a Date into time of day format (MMmmss).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatTimeOfDay(final Date input) {
		if (null == input) {
			return null;
		} else {
			return createDateFormat(TIME_OF_DAY_FORMAT).format(input);
		}

	}

	/**
	 * Formats a Date into TSS format (yyyyMMdd).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatTSSTidRegString(final Date input) {
		if (input == null) {
			return null;
		}
		return createDateFormat(TSS_TID_REG_FORMAT).format(input);
	}

	/**
	 * Formats a Date into Oppdrag format (yyyy-MM-dd).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatOppdragString(final Date input) {
		if (input == null) {
			return null;
		}
		return createDateFormat(OPPDRAG_DATE_FORMAT).format(input);
	}

	/**
	 * Formats a Date into batch month format (yyyyMM).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatBatchMonthString(final Date input) {
		if (input == null) {
			return null;
		}
		return createDateFormat(BATCH_MONTH_FORMAT).format(input);
	}

	/**
	 * Formats a Data into TPS born date format (ddMMyy).
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatTpsBornDateString(final Date input) {
		if (input == null) {
			return null;
		}
		return createDateFormat(TPS_BORN_DATE_FORMAT).format(input);
	}

	/**
	 * Formats a input String to date format dd.mm.yyyy
	 * 
	 * @param input
	 *            the date to format.
	 * @return the formatted date.
	 */
	public static String formatInputDateString(final String input) {
		Date date = parseInputString(input, true);

		if (null == date) {
			return null;
		} else {
			return createDateFormat(DATE_FORMAT).format(date);
		}
	}

	/**
	 * Checks if the day of the specified date is today's day.
	 * 
	 * @param date
	 *            the date to check.
	 * @return true if the specified date's day is today's day, false otherwise.
	 */
	public static boolean isToday(final Date date) {
		return isSameDay(date, new Date());
	}

	/**
	 * Checks if the day of the specified date is after today's day.
	 * 
	 * @param date
	 *            the date to check.
	 * @return true if specified date is after today's date, false otherwise.
	 */
	public static boolean isAfterToday(final Date date) {
		if (null == date) {
			throw new IllegalArgumentException("null is a not valid input date");
		}

		return !isBeforeToday(date) && !isToday(date);
	}

	/**
	 * Checks if the day of the specified date is before today's date.
	 * 
	 * @param date
	 *            the date to check.
	 * @return true if specified date is before today's date, false otherwise.
	 */
	public static boolean isBeforeToday(final Date date) {
		return isBeforeDay(date, null);
	}

	/**
	 * Checks if one date is before another date. Only uses the date portion of the input, not taking the time portion in
	 * account.
	 * 
	 * @param firstDate
	 *            the first date.
	 * @param secondDate
	 *            the second date to check against.
	 * @return true if first date is before the second date, false otherwise.
	 */
	public static boolean isBeforeDay(final Date firstDate, final Date secondDate) {
		if (null == firstDate) {
			return false;
		}

		final Calendar firstCalendar = createEmptyTimeFieldsCalendar(firstDate);
		// Use todays date if second date is empty
		final Calendar secondCalendar = createEmptyTimeFieldsCalendar(null == secondDate ? new Date() : secondDate);

		return firstCalendar.getTime().before(secondCalendar.getTime());
	}

	/**
	 * Check if the date is the same day.
	 * 
	 * @param firstDate
	 *            the first date.
	 * @param secondDate
	 *            the second date to check against.
	 * @return true if first date is the same day as the second date, false otherwise.
	 */
	public static boolean isSameDay(Date firstDate, Date secondDate) {
		if (null == firstDate || null == secondDate) {
			return false;
		}

		final Calendar firstCalendar = createEmptyTimeFieldsCalendar(firstDate);
		final Calendar secondCalendar = createEmptyTimeFieldsCalendar(secondDate);

		return firstCalendar.equals(secondCalendar);
	}

	/**
	 * Method that checks if a date is between two other dates.
	 * 
	 * @param compDate
	 *            date that is being checked.
	 * @param fomDate
	 *            FOM-date.
	 * @param tomDate
	 *            TOM-date.
	 * @return boolean if date is in period.
	 */
	public static boolean isDateInPeriod(final Date compDate, final Date fomDate, final Date tomDate) {
		if (null == fomDate || null == compDate) {
			return false;
		}

		boolean tomOK = false;

		if (null != tomDate) {
			if (isBeforeDay(compDate, tomDate) || isSameDay(compDate, tomDate)) {
				tomOK = true;
			}
		} else {
			tomOK = true;
		}

		return (isBeforeDay(fomDate, compDate) || isSameDay(compDate, fomDate)) && tomOK;
	}

	/**
	 * Checks whether the date is the first day of a month or not.
	 * 
	 * @param date
	 *            the date to check.
	 * @return true if the date is the first day of a month, false otherwise.
	 */
	public static boolean isFirstDayOfMonth(Date date) {
		if (null == date) {
			throw new IllegalArgumentException("null is a not valid input date");
		}

		final Calendar calendar = createCalendar(date);

		return calendar.getActualMinimum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns yesterday's date formatted for display.
	 * 
	 * @return yesterday.
	 */
	public static Date getYesterday() {
		return getRelativeDateFromNow(-1);
	}

	/**
	 * Returns the date n days from now.
	 * 
	 * @param days
	 *            number of days relative from today (e.g. -14 = 2 weeks ago, 7 = in a week)
	 * @return the date.
	 */
	public static Date getRelativeDateFromNow(int days) {
		return getRelativeDateByDays(new Date(), days);
	}

	/**
	 * Creates a sorted list of <code>Date</code>s with the newest first.
	 * 
	 * @param dates
	 *            a set with the <code>Date</code>s to sort.
	 * @return a sorted list of <code>Date</code>s with the newest first.
	 */
	@SuppressWarnings("unchecked")
	public static List sortDatesNewestFirst(Set dates) {
		if (null == dates) {
			throw new IllegalArgumentException("null is a not valid input date");
		}

		ArrayList arrayList2 = new ArrayList(dates);
		ArrayList arrayList = arrayList2;
		List sortedDates = arrayList;
		Collections.sort(sortedDates, Collections.reverseOrder()); // The newest date becomes the first

		return sortedDates;
	}

	/**
	 * Finds the last day in the month before the given date.
	 * 
	 * @param date
	 *            the date to work against.
	 * @return the last day in the month before the given date.
	 */
	public static Date findLastDayInMonthBefore(final Date date) {
		final Calendar calendar = createCalendar(date);

		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		return calendar.getTime();
	}

	/**
	 * Returns the first day of month for the given date.
	 * 
	 * @param date
	 *            a given date
	 * @return the first day of the month for the given date.
	 */
	public static Date getFirstDayOfMonth(Date date) {
		return getFirstOrLastDayOfMonth(date, true);
	}

	/**
	 * Returns the last day of month for the given date.
	 * 
	 * @param date
	 *            a given date
	 * @return the last day of the month for the given date.
	 */
	public static Date getLastDayOfMonth(Date date) {
		return getFirstOrLastDayOfMonth(date, false);
	}

	/**
	 * Metode for ?inne datoer X ?frem/tilbake i tid
	 * 
	 * @param date
	 *            Dato
	 * @param years ?
	 *            fremover (positive tall) eller ?bakover (negative tall)
	 * @return Datoen X ?frem eller tilbake
	 */
	public static Date getRelativeDateByYear(Date date, int years) {
		final Calendar calendar = createCalendar(date);
		calendar.add(Calendar.YEAR, years);

		return calendar.getTime();
	}

	/**
	 * Metode for ?inne datoer X m?der fremover/tilbake i tid
	 * 
	 * @param date
	 *            Datoen
	 * @param months
	 *            M?der fremover / bakover
	 * @return Datoen X m?der fremover/tilbake
	 */
	public static Date getRelativeDateByMonth(Date date, int months) {
		final Calendar calendar = createCalendar(date);
		calendar.add(Calendar.MONTH, months);

		return calendar.getTime();
	}

	/**
	 * Metode for ?inne datoer X dager frem / tilbake i tid
	 * 
	 * @param date
	 *            Datoe
	 * @param days
	 *            dager frem/tilbake
	 * @return Datoen X dager fremover/tilbake
	 */
	public static Date getRelativeDateByDays(Date date, int days) {
		final Calendar calendar = createCalendar(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);

		return calendar.getTime();
	}

	/**
	 * Setter klokkeslettet til 00:00:00.00
	 * 
	 * @param inDate
	 *            Aktuell dato
	 * @return nullstilt dato.
	 */
	public static Date setTimeToZero(Date inDate) {
		return createEmptyTimeFieldsCalendar(inDate).getTime();
	}

	/**
	 * Returnerer ?t i en dato.
	 * 
	 * @param inDate
	 *            angitt dato.
	 * @return ?t i datoen.
	 */
	public static int getYear(Date inDate) {
		return getField(inDate, Calendar.YEAR);
	}

	/**
	 * Returnerer m?d i en dato.
	 * 
	 * @param inDate
	 *            angitt dato.
	 * @return m?d i datoen.
	 */
	public static int getMonth(Date inDate) {
		return getField(inDate, Calendar.MONTH);
	}

	/**
	 * Metode som finner f? dagen i ?t for angitt dato. Timer, minutter, sekunder og millisekunder er nullstilt.
	 * 
	 * @param inDate
	 *            hvilken dato som skal benyttes
	 * @return f? dag
	 */
	public static Date getFirstDateInYear(Date inDate) {
		final Calendar calendar = createEmptyTimeFieldsCalendar(inDate);

		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

		return calendar.getTime();
	}

	/**
	 * Metode som finner siste dagen i ?t for angitt dato Timer, minutter, sekunder og millisekunder er nullstilt.
	 * 
	 * @param inDate
	 *            hvilken dato som skal benyttes
	 * @return siste dag
	 */
	public static Date getLastDateInYear(Date inDate) {
		final Calendar calendar = createEmptyTimeFieldsCalendar(inDate);

		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		return calendar.getTime();
	}

	/**
	 * Returns the number of month between two dates.
	 * 
	 * @param fromDate
	 *            date from.
	 * @param toDate
	 *            date to.
	 * @return relative number of months between fromDate and toDate.
	 */
	public static int getMonthBetween(Date fromDate, Date toDate) {
		final Calendar fromCalendar = createCalendar(fromDate);
		final Calendar toCalendar = createCalendar(toDate);

		int fromTotalMonths = MONTHS_IN_YEAR * fromCalendar.get(Calendar.YEAR) + fromCalendar.get(Calendar.MONTH);
		int toTotalMonths = MONTHS_IN_YEAR * toCalendar.get(Calendar.YEAR) + toCalendar.get(Calendar.MONTH);

		return Math.abs(fromTotalMonths - toTotalMonths);
	}

	/**
	 * Henter datoen 18 ar tilbake fra angitt dato. Feks hvis parameter dato er 15/5-2005 vil retur dato vare 15/5-1987
	 * 
	 * @param date
	 *            den nye datoen
	 * @return Date 18 ?tilbake fra date eller null hvis date er null
	 */
	public static Date get18YearsBack(Date date) {
		if (null == date) {
			return null;
		}

		final Calendar calendar = createEmptyTimeFieldsCalendar(date);
		calendar.roll(Calendar.YEAR, -18);

		return calendar.getTime();
	}

	/**
	 * Creates a non-lenient <code>DateFormat</code> from the specified date format.
	 * 
	 * @param dateFormat
	 *            the date format the created <code>DateForm</code> will have.
	 * @return a non-lenient <code>DateFormat</code> with the specified date format.
	 */
	public static DateFormat createDateFormat(final String dateFormat) {
		SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateInstance();
		format.setLenient(false);
		format.applyLocalizedPattern(dateFormat);

		return format;
	}

	/**
	 * Helper method for parsing dates in a string.
	 * 
	 * @param input
	 *            the input string to parse into a date.
	 * @param allowNull
	 *            whether null is allowed as input for the date.
	 * @param dateFormat
	 *            the format to use for parsing the date string.
	 * @param noEarlyDates
	 *            whether early dates should be allowed.
	 * @param resetTimePart
	 *            whether the time part of the date should be reset to all zeros.
	 * @param resetDayOfMonth
	 *            whether the day of month of the date should be reset to the first day of the month.
	 * @return the parsed date.
	 * @throws IllegalArgumentException
	 *             if input is not legal.
	 */
	private static Date parseCommon(final String input, final boolean allowNull, String dateFormat, boolean noEarlyDates,
			boolean resetTimePart, boolean resetDayOfMonth) throws IllegalArgumentException {
		Date output = null;

		if (StringUtils.isBlank(input)) {
			if (!allowNull) {
				throw new IllegalArgumentException("null is a not valid input date");
			}
		} else {
			try {
				output = createDateFormat(dateFormat).parse(input);
			} catch (ParseException pe) {
				throw new IllegalArgumentException("Failed to parse " + input + ": " + pe);
			}

			if (null != output) {
				final Calendar calendar = createCalendar(output);

				if (noEarlyDates) {
					// We don't accept years earlier than 1000
					if (EARLIEST_YEAR > calendar.get(Calendar.YEAR)) {
						throw new IllegalArgumentException("Don't accept years earlier than " + EARLIEST_YEAR + ": " + input);
					}
				}

				if (resetTimePart) {
					clearTimeFields(calendar);
				}

				if (resetDayOfMonth) {
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
				}

				output = calendar.getTime();
			}
		}

		return output;
	}

	/**
	 * Helper method for replacing not formattable separators with formattable separators.
	 * 
	 * @param input
	 *            the string to replace separators in.
	 * @return string with separators replaced.
	 */
	private static String replaceSeparators(final String input) {
		return StringUtils.replaceChars(input, "-/\\", "...");
	}

	/**
	 * Creates a non-lenient calendar with empty time fields.
	 * 
	 * @param date
	 *            the <code>Date</code> to create a calendar from.
	 * @return a non-lenient calendar with empty time fields.
	 * @see #createCalendar(Date)
	 */
	private static Calendar createEmptyTimeFieldsCalendar(final Date date) {
		final Calendar calendar = createCalendar(date);
		clearTimeFields(calendar);

		return calendar;
	}

	/**
	 * Creates a non-lenient calendar.
	 * 
	 * @param date
	 *            the <code>Date</code> to create a calendar from.
	 * @return a non-lenient calendar.
	 * @see #createCalendar(Date)
	 */
	private static Calendar createCalendar(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setLenient(false);
		calendar.setTime(date);

		return calendar;
	}

	/**
	 * Clears the time fields of a calendar.
	 * 
	 * @param calendar
	 *            the calendar to clear the time fields of.
	 */
	private static void clearTimeFields(final Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Returns the given field for a date. Be careful when calling this from inside another synchronized block as it could lead
	 * to a dealock.
	 * 
	 * @param inDate
	 *            the date to return the given field for.
	 * @param dateField
	 *            hvilket felt som skal finnes (vil v? feks Calendar.YEAR)
	 * @return verdien p?eltet
	 */
	private static int getField(Date inDate, int dateField) {
		final Calendar calendar = createCalendar(inDate);
		return calendar.get(dateField);
	}

	/**
	 * Returns the first or last of month for a given date. Be careful when calling this from inside another synchronized block
	 * as it could lead to a dealock.
	 * 
	 * @param date
	 *            a given date
	 * @param first
	 *            is true then first day in month
	 * @return Date for the first or last day of month
	 */
	private static Date getFirstOrLastDayOfMonth(Date date, boolean first) {
		final Calendar calendar = createCalendar(date);
		int dayOfMonth;

		if (first) {
			dayOfMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		} else {
			dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}

		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		return calendar.getTime();
	}

	/**
	 * Immutable date, that is, only read access. All setter methods throws an <code>UnsupportedOperation</code> exception.
	 * 
	 * @see UnsupportedOperationException
	 * @see Date
	 */
	public static final class ReadOnlyDate extends Date {
		/**
		 * 
		 */
		private static final long serialVersionUID = -722295701518640035L;
		/**
		 * 
		 */
		private static final Calendar calendar = Calendar.getInstance();

		/**
		 * Constructs a date from the year, month and day of month values.
		 * 
		 * @param year
		 *            year to use.
		 * @param month
		 *            month to use.
		 * @param dayOfMonth
		 *            day of month to use.
		 */
		public ReadOnlyDate(final int year, final int month, final int dayOfMonth) {
			super(longValue(year, month, dayOfMonth));
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @deprecated
		 */
		public void setYear(int year) {
			throw new UnsupportedOperationException("read-only date");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @deprecated
		 */
		public void setMonth(int month) {
			throw new UnsupportedOperationException("read-only date");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @deprecated
		 */
		public void setDate(int date) {
			throw new UnsupportedOperationException("read-only date");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @deprecated
		 */
		public void setHours(int hours) {
			throw new UnsupportedOperationException("read-only date");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @deprecated
		 */
		public void setMinutes(int minutes) {
			throw new UnsupportedOperationException("read-only date");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @deprecated
		 */
		public void setSeconds(int seconds) {
			throw new UnsupportedOperationException("read-only date");
		}

		/** {@inheritDoc} */
		public void setTime(long time) {
			throw new UnsupportedOperationException("read-only date");
		}

		/**
		 * Helper method for calculating a time in millis representation of the year, month and day of month values.
		 * 
		 * @param year
		 *            year to use.
		 * @param month
		 *            month to use.
		 * @param dayOfMonth
		 *            day of month to use.
		 * @return time in millis representation of the year, month and day values.
		 */
		private static long longValue(final int year, final int month, final int dayOfMonth) {
			calendar.set(year, month, dayOfMonth);

			return calendar.getTimeInMillis();
		}
	}
}
