package no.stelvio.presentation.jsf.codestable;

import javax.persistence.Entity;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.util.ReflectUtil;

/**
 * CodesTableItems for testing.
 * 
 * @version $Id$
 */
@Entity
public class SimpleTestCti extends CodesTableItem<SimpleTestCtiCode, String> {
	// No real use in a test class, added to avoid warning
	private static final long serialVersionUID = -2578958376917780360L;

	/**
	 * Constructor that would be used by persistence providers.
	 */
	protected SimpleTestCti() {
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
	private SimpleTestCti(SimpleTestCtiCode code, String decode) {
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
	private SimpleTestCti(String code, String decode) {
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
	private SimpleTestCti(final String decode) {
		ReflectUtil.setFieldOnInstance(this, "code", "NO_ENUM");
		ReflectUtil.setFieldOnInstance(this, "decode", decode);
		ReflectUtil.setFieldOnInstance(this, "valid", true);
	}

	/**
	 * Create empty codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createEmptyCti() {
		return new SimpleTestCti();
	}

	/**
	 * Create codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCti1() {
		return new SimpleTestCti(SimpleTestCtiCode.EXISTS_1, "EXISTS_1");
	}

	/**
	 * Create codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCti2() {
		return new SimpleTestCti(SimpleTestCtiCode.EXISTS_2, "EXISTS_2");
	}

	/**
	 * Create codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCti3() {
		return new SimpleTestCti(SimpleTestCtiCode.EXISTS_3, "EXISTS_3");
	}

	/**
	 * Create codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCti4() {
		return new SimpleTestCti(SimpleTestCtiCode.EXISTS_4, "EXISTS_4");
	}

	/**
	 * Create codetable item exists not.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCtiExistsNot() {
		return new SimpleTestCti(SimpleTestCtiCode.EXISTS_NOT, "EXISTS_NOT");
	}

	/**
	 * Create codetable item with empty decode.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCtiWithEmptyDecode() {
		return new SimpleTestCti(SimpleTestCtiCode.EMPTY_DECODE, null);
	}

	/**
	 * Create codetable item without enum.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCtiWithoutEnum() {
		return new SimpleTestCti("NO_ENUM_DECODE");
	}

	/**
	 * Create numeric codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCtiNumericDecode1() {
		return new SimpleTestCti(SimpleTestCtiCode.NUMERIC_1, "1");
	}

	/**
	 * Create numeric codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCtiNumericDecode2() {
		return new SimpleTestCti(SimpleTestCtiCode.NUMERIC_2, "2");
	}

	/**
	 * Create numeric codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCtiNumericDecode10() {
		return new SimpleTestCti(SimpleTestCtiCode.NUMERIC_10, "10");
	}

	/**
	 * Create numeric codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCtiNumericCode1() {
		return new SimpleTestCti("1", "NUMERIC_1");
	}

	/**
	 * Create numeric codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCtiNumericCode2() {
		return new SimpleTestCti("2", "NUMERIC_2");
	}

	/**
	 * Create numeric codetable item.
	 * 
	 * @return item
	 */
	public static SimpleTestCti createCtiNumericCode10() {
		return new SimpleTestCti("10", "NUMERIC_10");
	}

}
