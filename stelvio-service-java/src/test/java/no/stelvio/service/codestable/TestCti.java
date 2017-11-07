package no.stelvio.service.codestable;

import javax.persistence.Entity;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.util.ReflectUtil;

/**
 * CodesTableItems for testing.
 * 
 * @author personb66fa0b5ff6e 
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
	 * Constructs a new instance by emulating the usage from an persistence provider, that is, calling default
	 * constructor and using reflection to set the fields.
	 *
	 * @param code the code
	 * @param decode the decode
	 */
	private TestCti(TestCtiCode code, String decode) {
		this(decode);

		ReflectUtil.setFieldOnInstance(this, "code", code.name());
	}

	/**
	 * Constructs a new instance by emulating the usage from an persistence provider, that is, calling default
	 * constructor and using reflection to set the fields.
	 *
	 * @param decode the decode
	 */
	private TestCti(final String decode) {
		ReflectUtil.setFieldOnInstance(this, "code", "NO_ENUM");
		ReflectUtil.setFieldOnInstance(this, "decode", decode);
		ReflectUtil.setFieldOnInstance(this, "valid", true);
	}

	/**
	 * Create empty cti.
	 * 
	 * @return cti
	 */
	public static TestCti createEmptyCti() {
		return new TestCti();
	}

	/**
	 * Create cti with code EXISTS_1.
	 * 
	 * @return cti
	 */
	public static TestCti createCti1() {
		return new TestCti(TestCtiCode.EXISTS_1, "EXISTS_1");
	}

	/**
	 * Create cti with code EXISTS_2.
	 * 
	 * @return cti
	 */
	public static TestCti createCti2() {
		return new TestCti(TestCtiCode.EXISTS_2, "EXISTS_2");
	}

	/**
	 * Create cti with code EXISTS_3.
	 * 
	 * @return cti
	 */
	public static TestCti createCti3() {
		return new TestCti(TestCtiCode.EXISTS_3, "EXISTS_3");
	}

	/**
	 * Create cti with code EXISTS_4.
	 * 
	 * @return cti
	 */
	public static TestCti createCti4() {
		return new TestCti(TestCtiCode.EXISTS_4, "EXISTS_4");
	}

	/**
	 * Create cti with code EXISTS_NOT.
	 * 
	 * @return cti
	 */
	public static TestCti createCtiExistsNot() {
		return new TestCti(TestCtiCode.EXISTS_NOT, "EXISTS_NOT");
	}

	/**
	 * Create cti with code EMPTY_DECODE.
	 * 
	 * @return cti
	 */
	public static TestCti createCtiWithEmptyDecode() {
		return new TestCti(TestCtiCode.EMPTY_DECODE, null);
	}

	/**
	 * Create cti without code.
	 * 
	 * @return cti
	 */
	public static TestCti createCtiWithoutEnum() {
		return new TestCti("NO_ENUM_DECODE");
	}
}