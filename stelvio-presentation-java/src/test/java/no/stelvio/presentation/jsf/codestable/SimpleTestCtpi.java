package no.stelvio.presentation.jsf.codestable;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;

import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.util.ReflectUtil;

/**
 * Codestableitemperiodics for testing.
 * 
 * @version $Id$
 */
@Entity
public class SimpleTestCtpi extends CodesTablePeriodicItem<SimpleTestCtiCode, String> {
	// No real use in a test class, added to avoid warning
	private static final long serialVersionUID = 2846761623694997742L;

	/**
	 * Constructor that would be used by persistence providers.
	 */
	protected SimpleTestCtpi() {
	}

	/**
	 * Creates a new instance of SimpleTestCtpi.
	 * 
	 * @param code
	 *            code
	 * @param decode
	 *            decode
	 * @param fromDate
	 *            from-date
	 * @param toDate
	 *            to-date
	 */
	private SimpleTestCtpi(SimpleTestCtiCode code, String decode, Date fromDate, Date toDate) {
		this(decode);

		ReflectUtil.setFieldOnInstance(this, "fromDate", fromDate);
		ReflectUtil.setFieldOnInstance(this, "toDate", toDate);
		ReflectUtil.setFieldOnInstance(this, "code", code.name());
	}

	/**
	 * Creates a new instance of SimpleTestCtpi.
	 * 
	 * @param code
	 *            code
	 * @param decode
	 *            decode
	 * @param fromDate
	 *            from-date
	 * @param toDate
	 *            to-date
	 */
	private SimpleTestCtpi(String code, String decode, Date fromDate, Date toDate) {
		this(decode);

		ReflectUtil.setFieldOnInstance(this, "fromDate", fromDate);
		ReflectUtil.setFieldOnInstance(this, "toDate", toDate);
		ReflectUtil.setFieldOnInstance(this, "code", code);
	}

	/**
	 * Constructs a new instance by emulating the usage from an persistence provider, that is, calling default constructor and
	 * using reflection to set the fields.
	 * 
	 * @param code
	 *            the code
	 * @param decode
	 *            the decode
	 */
	private SimpleTestCtpi(SimpleTestCtiCode code, String decode) {
		this(decode);

		ReflectUtil.setFieldOnInstance(this, "code", code.name());
	}

	/**
	 * Constructs a new instance by emulating the usage from an persistence provider, that is, calling default constructor and
	 * using reflection to set the fields. The code is represented as a String, and not an Enum in order to test codes table
	 * items with numeric code values
	 * 
	 * @param code
	 *            the code
	 * @param decode
	 *            the decode
	 */
	private SimpleTestCtpi(String code, String decode) {
		this(decode);
		ReflectUtil.setFieldOnInstance(this, "code", code);
	}

	/**
	 * Constructs a new instance by emulating the usage from an persistence provider, that is, calling default constructor and
	 * using reflection to set the fields.
	 * 
	 * @param decode
	 *            the decode
	 */
	private SimpleTestCtpi(final String decode) {
		ReflectUtil.setFieldOnInstance(this, "code", "NO_ENUM");
		ReflectUtil.setFieldOnInstance(this, "decode", decode);
		ReflectUtil.setFieldOnInstance(this, "fromDate", getTestFromDate());
		ReflectUtil.setFieldOnInstance(this, "toDate", getTestToDate());
		ReflectUtil.setFieldOnInstance(this, "valid", true);
	}

	/**
	 * Create empty codestable periodic item.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createEmptyCtpi() {
		return new SimpleTestCtpi();
	}

	/**
	 * Create codetable periodic item decode.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpiNumericDecode1() {
		return new SimpleTestCtpi(SimpleTestCtiCode.NUMERIC_1, "1");
	}

	/**
	 * Create codetable periodic item decode.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpiNumericDecode2() {
		return new SimpleTestCtpi(SimpleTestCtiCode.NUMERIC_2, "2");
	}

	/**
	 * Create codetable periodic item decode.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpiNumericDecode10() {
		return new SimpleTestCtpi(SimpleTestCtiCode.NUMERIC_10, "10");
	}

	/**
	 * Create codetable periodic item code.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpiNumericCode1() {
		return new SimpleTestCtpi("1", "NUMERIC_1");
	}

	/**
	 * Create codetable periodic item code.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpiNumericCode2() {
		return new SimpleTestCtpi("2", "NUMERIC_2");
	}

	/**
	 * Create codetable periodic item code.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpiNumericCode10() {
		return new SimpleTestCtpi("10", "NUMERIC_10");
	}

	/**
	 * Create overlap codetable periodic item decode.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createOverlapCtpiNumericDecode1() {
		return new SimpleTestCtpi(SimpleTestCtiCode.NUMERIC_1, "1", getDate(2007, 5, 1), getDate(2007, 5, 21));
	}

	/**
	 * Create overlap codetable periodic item code.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createOverlapCtpiNumericCode1() {
		return new SimpleTestCtpi("1", "NUMERIC_1", getDate(2007, 5, 1), getDate(2007, 5, 21));
	}

	/**
	 * Create overlap codetable periodic item decode.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createOverlapCtpiNumericDecode2() {
		return new SimpleTestCtpi(SimpleTestCtiCode.NUMERIC_1, "1", getDate(2007, 5, 14), getDate(2007, 5, 30));
	}

	/**
	 * Create overlap codetable periodic item code.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createOverlapCtpiNumericCode2() {
		return new SimpleTestCtpi("1", "NUMERIC_1", getDate(2007, 5, 14), getDate(2007, 5, 30));
	}

	/**
	 * Create codetable periodic item.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpi1() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_1, "EXISTS_1");
	}

	/**
	 * Create codetable periodic item.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpi2() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_2, "EXISTS_2");
	}

	/**
	 * Create codetable periodic item.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpi3() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_3, "EXISTS_3");
	}

	/**
	 * Create codetable periodic item.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpi4() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_4, "EXISTS_4");
	}

	/**
	 * Create codetable periodic item exists not.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpiExistsNot() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_NOT, "EXISTS_NOT");
	}

	/**
	 * Create codetable periodic item with empty decode.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpiWithEmptyDecode() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EMPTY_DECODE, null);
	}

	/**
	 * Create codetable periodic item without enum.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createCtpiWithoutEnum() {
		return new SimpleTestCtpi("NO_ENUM_DECODE");
	}

	/**
	 * Create overlap codetable periodic item.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createOverlapCtpi1() {
		return new SimpleTestCtpi(
				SimpleTestCtiCode.EXISTS_OVERLAP, "EXISTS_OVERLAP", getDate(2007, 5, 1), getDate(2007, 5, 21));
	}

	/**
	 * Create overlap codetable periodic item.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createOverlapCtpi2() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_OVERLAP, "EXISTS_OVERLAP", getDate(2007, 5, 14),
				getDate(2007, 5, 30));
	}

	/**
	 * Create overlap codetable periodic item with null.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createOverlapCtpiWithNull() {
		return new SimpleTestCtpi(SimpleTestCtiCode.OVERLAP_WITH_NULL, "OVERLAP_WITH_NULL", getDate(2007, 5, 14), null);
	}

	/**
	 * Create overlap codetable periodic item with null.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createOverlapCtpiWithNull2() {
		return new SimpleTestCtpi(SimpleTestCtiCode.OVERLAP_WITH_NULL, "OVERLAP_WITH_NULL_2", getDate(2007, 4, 14), null);
	}

	/**
	 * Create overlap codetable periodic item with null.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createOverlapCtpiWithNull3() {
		return new SimpleTestCtpi(SimpleTestCtiCode.OVERLAP_WITH_NULL, "SHOULD_BE_IN_LIST", getDate(2007, 3, 1), getDate(2007,
				3, 21));
	}

	/**
	 * Create codetable periodic item exists time.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createExistsTimeCtpi1() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_TIME_1, "EXISTS_TIME_1", getDate(2007, 5, 1), getDate(2007, 5, 21));
	}

	/**
	 * Create codetable periodic item exists time.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createExistsTimeCtpi2() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_TIME_1, "EXISTS_TIME_1", getDate(2007, 5, 22), getDate(2007, 5, 30));
	}

	/**
	 * Create codetable periodic item exists time.
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createExistsTimeCtpi3() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_TIME_2, "EXISTS_TIME_2", getDate(2007, 5, 1), getDate(2007, 5, 21));
	}

	/**
	 * Create codetable periodic item exists time.  Used in CodesTablePeriodicItemSelectOneMenuTest.  
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createExistsTimeCtpi4() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_TIME_1, "EXISTS_TIME_1", getDate(2008, 5, 1), getDate(2010, 5, 21));
	}
	
	/**
	 * Create codetable periodic item exists time.  Used in CodesTablePeriodicItemSelectOneMenuTest.  
	 * 
	 * @return item
	 */
	public static SimpleTestCtpi createExistsTimeCtpi5() {
		return new SimpleTestCtpi(SimpleTestCtiCode.EXISTS_TIME_2, "EXISTS_TIME_2", getDate(2007, 5, 1), getDate(2010, 5, 21));
	}
	
	/**
	 * Get from-date.
	 * 
	 * @return date
	 */
	private static Date getTestFromDate() {
		return getDate(106, 10, 1);
	}

	/**
	 * This method is available for test-classes that need to find a valid date that is between {@link #getTestFromDate()} and
	 * {@link #getTestToDate()}.
	 * 
	 * @return Date
	 */
	public static Date getBetweenFromAndToDate() {
		return getDate(106, 10, 15);
	}

	/**
	 * Get to-date.
	 * 
	 * @return date
	 */
	private static Date getTestToDate() {
		return getDate(106, 10, 30);
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
	private static Date getDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}
}