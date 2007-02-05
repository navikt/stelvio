package no.stelvio.common.codestable;


/**
 * CodesTableItems for testing.
 * 
 * @author personb66fa0b5ff6e 
 * @version $Id$
 */
public class TestCodesTableItem extends CodesTableItem {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2578958376917780360L;

	/** Constructor for TestCodesTableItem.
	 * 
	 * @param code the code
	 * @param decode the decode
	 * @param locale the locale
	 * @param isValid the validity of the item
	 */
	public TestCodesTableItem(String code, String decode, Boolean isValid){
		super(code, decode, isValid);
	}
	
	public TestCodesTableItem() {}
	
	public static TestCodesTableItem getCti1() {
		return new TestCodesTableItem("t1code1", "t1decode1", Boolean.TRUE);
	}
	
	public static TestCodesTableItem getCti2() {
		return new TestCodesTableItem("t2code2", "t2decode2",  Boolean.TRUE);
	}

	public static TestCodesTableItem getCti3() {
		return new TestCodesTableItem("t3code3", "t3decode3",  Boolean.TRUE);
	}

	public static TestCodesTableItem getCti4() {
		return new TestCodesTableItem("t4code4", "t4decode4",  Boolean.TRUE);
	}

	public static TestCodesTableItem getCti5() {
		return new TestCodesTableItem("t1code1", "t1decode1", Boolean.TRUE);
	}
}