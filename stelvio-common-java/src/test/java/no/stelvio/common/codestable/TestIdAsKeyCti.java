package no.stelvio.common.codestable;

import javax.persistence.Entity;

import no.stelvio.common.codestable.support.IdAsKeyCodesTableItem;
import no.stelvio.common.util.ReflectUtil;

/**
 * CodesTableItems for testing.
 * 
 * @version $Id$
 */
@Entity
public class TestIdAsKeyCti extends IdAsKeyCodesTableItem<TestCtiCode, String> {
	// No real use in a test class, added to avoid warning
	private static final long serialVersionUID = -2578958376917780360L;

	/**
	 * Constructor that would be used by persistence providers.
	 */
	protected TestIdAsKeyCti() {
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
	private TestIdAsKeyCti(TestCtiCode code, String decode) {
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
	private TestIdAsKeyCti(final String decode) {
		ReflectUtil.setFieldOnInstance(this, "code", "NO_ENUM");
		ReflectUtil.setFieldOnInstance(this, "decode", decode);
		ReflectUtil.setFieldOnInstance(this, "valid", true);
	}

	/**
	 * Creates an empty codestable item.
	 * 
	 * @return empty item
	 */
	public static TestIdAsKeyCti createEmptyCti() {
		return new TestIdAsKeyCti();
	}

	/**
	 * Creates a codestable item.
	 * 
	 * @return item
	 */
	public static TestIdAsKeyCti createCti1() {
		return new TestIdAsKeyCti(TestCtiCode.EXISTS_1, "EXISTS_1");
	}

	/**
	 * Creates a codestable item.
	 * 
	 * @return item
	 */
	public static TestIdAsKeyCti createCti2() {
		return new TestIdAsKeyCti(TestCtiCode.EXISTS_2, "EXISTS_2");
	}

	/**
	 * Creates a codestable item.
	 * 
	 * @return item
	 */
	public static TestIdAsKeyCti createCti3() {
		return new TestIdAsKeyCti(TestCtiCode.EXISTS_3, "EXISTS_3");
	}

	/**
	 * Creates a codestable item.
	 * 
	 * @return item
	 */
	public static TestIdAsKeyCti createCti4() {
		return new TestIdAsKeyCti(TestCtiCode.EXISTS_4, "EXISTS_4");
	}

	/**
	 * Creates a codestable item with code EXISTS_NOT.
	 * 
	 * @return item
	 */
	public static TestIdAsKeyCti createCtiExistsNot() {
		return new TestIdAsKeyCti(TestCtiCode.EXISTS_NOT, "EXISTS_NOT");
	}

	/**
	 * Creates a codestable item with empty decode.
	 * 
	 * @return item
	 */
	public static TestIdAsKeyCti createCtiWithEmptyDecode() {
		return new TestIdAsKeyCti(TestCtiCode.EMPTY_DECODE, null);
	}

	/**
	 * Creates a codestable item without enum.
	 * 
	 * @return item
	 */
	public static TestIdAsKeyCti createCtiWithoutEnum() {
		return new TestIdAsKeyCti("NO_ENUM_DECODE");
	}
}