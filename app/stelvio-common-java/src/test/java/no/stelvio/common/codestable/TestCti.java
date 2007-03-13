package no.stelvio.common.codestable;

import javax.persistence.Entity;

/**
 * CodesTableItems for testing.
 * 
 * @author personb66fa0b5ff6e 
 * @version $Id$
 */
@Entity
public class TestCti extends CodesTableItem<TestCtiCode, Integer> {
	private static final long serialVersionUID = -2578958376917780360L;

	/** Constructor for TestCodesTableItem.
	 * 
	 * @param code the code
	 * @param decode the decode
	 * @param isValid the validity of the item
	 */
	public TestCti(TestCtiCode code, Integer decode, boolean isValid){
		super(code, decode, isValid);
	}

	protected TestCti() {
	}

	public static TestCti getCti1() {
		return new TestCti(TestCtiCode.EXISTS_1, 1, true);
	}
	
	public static TestCti getCti2() {
		return new TestCti(TestCtiCode.EXISTS_2, 2,  true);
	}

	public static TestCti getCti3() {
		return new TestCti(TestCtiCode.EXISTS_3, 3,  true);
	}

	public static TestCti getCti4() {
		return new TestCti(TestCtiCode.EXISTS_4, 4,  true);
	}

	public static TestCti getCti5() {
		return new TestCti(TestCtiCode.EXISTS_1, 1, true);
	}
}