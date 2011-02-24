package no.stelvio.provider.codestable;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;

import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.util.ReflectUtil;

/**
 * Codestableitemperiodics for testing.
 * 
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
@Entity
public class TestCtpi extends CodesTablePeriodicItem<TestCtiCode, String> {
	// No real use in a test class, added to avoid warning
	private static final long serialVersionUID = 2846761623694997742L;

	/**
	 * Constructor that would be used by persistence providers.
	 */
	protected TestCtpi() {
	}

	/**
	 * Creates a new instance of TestCtpi.
	 *
	 * @param code code
	 * @param decode decode
	 * @param fromDate from date
	 * @param toDate to date
	 */
	private TestCtpi(TestCtiCode code, String decode, Date fromDate, Date toDate) {
		this(decode);

		ReflectUtil.setFieldOnInstance(this, "fromDate", fromDate);
		ReflectUtil.setFieldOnInstance(this, "toDate", toDate);
		ReflectUtil.setFieldOnInstance(this, "code", code.name());
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
	private TestCtpi(TestCtiCode code, String decode) {
		this(decode);

		ReflectUtil.setFieldOnInstance(this, "code", code.name());
	}

	/**
	 * Constructs a new instance by emulating the usage from an persistence provider, that is, calling default constructor and
	 * using reflection to set the fields.
	 * 
	 * @param decode
	 *            the decode
	 */
	private TestCtpi(final String decode) {
		ReflectUtil.setFieldOnInstance(this, "code", "NO_ENUM");
		ReflectUtil.setFieldOnInstance(this, "decode", decode);
		ReflectUtil.setFieldOnInstance(this, "fromDate", getTestFromDate());
		ReflectUtil.setFieldOnInstance(this, "toDate", getTestToDate());
		ReflectUtil.setFieldOnInstance(this, "valid", true);
	}
	
	/**
	 * Creates an empty ctpi.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createEmptyCtpi() {
		return new TestCtpi();
	}

	/**
	 * Creates a ctpi with code EXISTS_1.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createCtpi1() {
		return new TestCtpi(TestCtiCode.EXISTS_1, "EXISTS_1");
	}

	/**
	 * Creates a ctpi with code EXISTS_2.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createCtpi2() {
		return new TestCtpi(TestCtiCode.EXISTS_2, "EXISTS_2");
	}

	/**
	 * Creates a ctpi with code EXISTS_3.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createCtpi3() {
		return new TestCtpi(TestCtiCode.EXISTS_3, "EXISTS_3");
	}

	/**
	 * Creates a ctpi with code EXISTS_4.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createCtpi4() {
		return new TestCtpi(TestCtiCode.EXISTS_4, "EXISTS_4");
	}

	/**
	 * Creates a ctpi with code EXISTS_NOT.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createCtpiExistsNot() {
		return new TestCtpi(TestCtiCode.EXISTS_NOT, "EXISTS_NOT");
	}

	/**
	 * Creates a ctpi with empty decode.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createCtpiWithEmptyDecode() {
		return new TestCtpi(TestCtiCode.EMPTY_DECODE, null);
	}

	/**
	 * Creates a ctpi without code.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createCtpiWithoutEnum() {
		return new TestCtpi("NO_ENUM_DECODE");
	}

	/**
	 * Creates a ctpi with code EXISTS_OVERLAP.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createOverlapCtpi1() {
		return new TestCtpi(TestCtiCode.EXISTS_OVERLAP, "EXISTS_OVERLAP", getDate(2007, 5, 1), getDate(2007, 5, 21));
	}

	/**
	 * Creates a ctpi with code EXISTS_OVERLAP.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createOverlapCtpi2() {
		return new TestCtpi(TestCtiCode.EXISTS_OVERLAP, "EXISTS_OVERLAP", getDate(2007, 5, 14), getDate(2007, 5, 30));
	}

	/**
	 * Creates a ctpi with code OVERLAP_WITH_NULL.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createOverlapCtpiWithNull() {
		return new TestCtpi(TestCtiCode.OVERLAP_WITH_NULL, "OVERLAP_WITH_NULL", getDate(2007, 5, 14), null);
	}

	/**
	 * Creates a ctpi with code OVERLAP_WITH_NULL.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createOverlapCtpiWithNull2() {
		return new TestCtpi(TestCtiCode.OVERLAP_WITH_NULL, "OVERLAP_WITH_NULL_2", getDate(2007, 4, 14), null);
	}

	/**
	 * Creates a ctpi with code OVERLAP_WITH_NULL.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createOverlapCtpiWithNull3() {
		return new TestCtpi(TestCtiCode.OVERLAP_WITH_NULL, "SHOULD_BE_IN_LIST", getDate(2007, 3, 1), getDate(2007, 3, 21));
	}

	/**
	 * Creates a ctpi with code EXISTS_TIME_1.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createExistsTimeCtpi1() {
		return new TestCtpi(TestCtiCode.EXISTS_TIME_1, "EXISTS_TIME_1", getDate(2007, 5, 1), getDate(2007, 5, 21));
	}

	/**
	 * Creates a ctpi with code EXISTS_TIME_1.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createExistsTimeCtpi2() {
		return new TestCtpi(TestCtiCode.EXISTS_TIME_1, "EXISTS_TIME_1", getDate(2007, 5, 22), getDate(2007, 5, 30));
	}

	/**
	 * Creates a ctpi with code EXISTS_TIME_2.
	 * 
	 * @return ctpi
	 */
	public static TestCtpi createExistsTimeCtpi3() {
		return new TestCtpi(TestCtiCode.EXISTS_TIME_2, "EXISTS_TIME_2", getDate(2007, 5, 1), getDate(2007, 5, 21));
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
		return (getDate(106, 10, 30));
	}

	/**
	 * Get date.
	 * 
	 * @param year year
	 * @param month month
	 * @param date day
	 * @return date
	 */
	private static Date getDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}
}
