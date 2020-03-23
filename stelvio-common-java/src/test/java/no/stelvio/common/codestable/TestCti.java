package no.stelvio.common.codestable;

import javax.persistence.Entity;

import no.stelvio.common.util.ReflectUtil;

/**
 * CodesTableItems for testing.
 * 
 * @version $Id$
 */
@Entity
public class TestCti extends CodesTableItem<TestCtiCode, String> {
	// No real use in a test class, added to avoid warning
	private static final long serialVersionUID = -2578958376917780360L;

	/**
	 * Constructor that would be used by persistence providers.
	 */
	protected TestCti() {
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
	private TestCti(TestCtiCode code, String decode) {
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
	private TestCti(String code, String decode) {
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
	private TestCti(final String decode) {
		ReflectUtil.setFieldOnInstance(this, "code", "NO_ENUM");
		ReflectUtil.setFieldOnInstance(this, "decode", decode);
		ReflectUtil.setFieldOnInstance(this, "valid", true);
	}

	/**
	 * Creates an empty codestable item.
	 * 
	 * @return item
	 */
	public static TestCti createEmptyCti() {
		return new TestCti();
	}

	/**
	 * Creates a codestable item.
	 * 
	 * @return item
	 */
	public static TestCti createCti1() {
		return new TestCti(TestCtiCode.EXISTS_1, "EXISTS_1");
	}

	/**
	 * Creates a codestable item.
	 * 
	 * @return item
	 */
	public static TestCti createCti2() {
		return new TestCti(TestCtiCode.EXISTS_2, "EXISTS_2");
	}

	/**
	 * Creates a codestable item.
	 * 
	 * @return item
	 */
	public static TestCti createCti3() {
		return new TestCti(TestCtiCode.EXISTS_3, "EXISTS_3");
	}

	/**
	 * Creates a codestable item.
	 * 
	 * @return item
	 */
	public static TestCti createCti4() {
		return new TestCti(TestCtiCode.EXISTS_4, "EXISTS_4");
	}

	/**
	 * Creates a codestable item with code EXISTS_NOT.
	 * 
	 * @return item
	 */
	public static TestCti createCtiExistsNot() {
		return new TestCti(TestCtiCode.EXISTS_NOT, "EXISTS_NOT");
	}

	/**
	 * Creates a codestable item with empty decode.
	 * 
	 * @return item
	 */
	public static TestCti createCtiWithEmptyDecode() {
		return new TestCti(TestCtiCode.EMPTY_DECODE, null);
	}

	/**
	 * Creates a codestable item without enum.
	 * 
	 * @return item
	 */
	public static TestCti createCtiWithoutEnum() {
		return new TestCti("NO_ENUM_DECODE");
	}

	/**
	 * Creates a codestable item with numeric decode.
	 * 
	 * @return item
	 */
	public static TestCti createCtiNumericDecode1() {
		return new TestCti(TestCtiCode.NUMERIC_1, "1");
	}

	/**
	 * Creates a codestable item with numeric decode.
	 * 
	 * @return item
	 */
	public static TestCti createCtiNumericDecode2() {
		return new TestCti(TestCtiCode.NUMERIC_2, "2");
	}

	/**
	 * Creates a codestable item with numeric decode.
	 * 
	 * @return item
	 */
	public static TestCti createCtiNumericDecode10() {
		return new TestCti(TestCtiCode.NUMERIC_10, "10");
	}

	/**
	 * Creates a codestable item with numeric code.
	 * 
	 * @return item
	 */
	public static TestCti createCtiNumericCode1() {
		return new TestCti("1", "NUMERIC_1");
	}

	/**
	 * Creates a codestable item with numeric code.
	 * 
	 * @return item
	 */
	public static TestCti createCtiNumericCode2() {
		return new TestCti("2", "NUMERIC_2");
	}

	/**
	 * Creates a codestable item with numeric code.
	 * 
	 * @return item
	 */
	public static TestCti createCtiNumericCode10() {
		return new TestCti("10", "NUMERIC_10");
	}

}
