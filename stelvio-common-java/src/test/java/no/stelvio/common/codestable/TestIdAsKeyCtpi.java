package no.stelvio.common.codestable;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;

import no.stelvio.common.codestable.support.IdAsKeyCodesTablePeriodicItem;
import no.stelvio.common.util.ReflectUtil;

/**
 * Codestableitemperiodics for testing.
 * 
 * @version $Id$
 */
@Entity
public class TestIdAsKeyCtpi extends IdAsKeyCodesTablePeriodicItem<TestCtiCode, String> {
	// No real use in a test class, added to avoid warning
	private static final long serialVersionUID = 2846761623694997742L;

	/**
	 * Constructor that would be used by persistence providers.
	 */
	protected TestIdAsKeyCtpi() {
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
	private TestIdAsKeyCtpi(TestCtiCode code, String decode) {
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
	private TestIdAsKeyCtpi(final String decode) {
		ReflectUtil.setFieldOnInstance(this, "code", "NO_ENUM");
		ReflectUtil.setFieldOnInstance(this, "decode", decode);
		ReflectUtil.setFieldOnInstance(this, "fromDate", getTestFromDate());
		ReflectUtil.setFieldOnInstance(this, "toDate", getTestToDate());
		ReflectUtil.setFieldOnInstance(this, "valid", true);
	}

	/**
	 * Create an empty codetable periodic item.
	 * 
	 * @return empty ctpi
	 */
	public static TestIdAsKeyCtpi createEmptyCtpi() {
		return new TestIdAsKeyCtpi();
	}

	/**
	 * Create a codetable periodic item.
	 * 
	 * @return ctpi
	 */
	public static TestIdAsKeyCtpi createCtpi1() {
		return new TestIdAsKeyCtpi(TestCtiCode.EXISTS_1, "EXISTS_1");
	}

	/**
	 * Create a codetable periodic item.
	 * 
	 * @return ctpi
	 */
	public static TestIdAsKeyCtpi createCtpi2() {
		return new TestIdAsKeyCtpi(TestCtiCode.EXISTS_2, "EXISTS_2");
	}

	/**
	 * Create a codetable periodic item.
	 * 
	 * @return ctpi
	 */
	public static TestIdAsKeyCtpi createCtpi3() {
		return new TestIdAsKeyCtpi(TestCtiCode.EXISTS_3, "EXISTS_3");
	}

	/**
	 * Create a codetable periodic item.
	 * 
	 * @return ctpi
	 */
	public static TestIdAsKeyCtpi createCtpi4() {
		return new TestIdAsKeyCtpi(TestCtiCode.EXISTS_4, "EXISTS_4");
	}

	/**
	 * Create a codetable periodic item with code EXISTS_NOT.
	 * 
	 * @return ctpi
	 */
	public static TestIdAsKeyCtpi createCtpiExistsNot() {
		return new TestIdAsKeyCtpi(TestCtiCode.EXISTS_NOT, "EXISTS_NOT");
	}

	/**
	 * Create a codetable periodic item with empty decode.
	 * 
	 * @return ctpi
	 */
	public static TestIdAsKeyCtpi createCtpiWithEmptyDecode() {
		return new TestIdAsKeyCtpi(TestCtiCode.EMPTY_DECODE, null);
	}

	/**
	 * Create a codetable periodic item without enum.
	 * 
	 * @return ctpi
	 */
	public static TestIdAsKeyCtpi createCtpiWithoutEnum() {
		return new TestIdAsKeyCtpi("NO_ENUM_DECODE");
	}

	/**
	 * Get a from date for test.
	 * 
	 * @return date
	 */
	private static Date getTestFromDate() {
		Calendar fromcal = Calendar.getInstance();
		fromcal.clear();
		fromcal.set(106, 10, 1);
		return new Date(fromcal.getTimeInMillis());
	}

	/**
	 * Get a to date for test.
	 * 
	 * @return date
	 */
	private static Date getTestToDate() {
		Calendar fromcal = Calendar.getInstance();
		fromcal.clear();
		fromcal.set(106, 10, 30);
		return new Date(fromcal.getTimeInMillis());
	}
}
