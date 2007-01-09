package no.stelvio.common.codestable;

import java.util.Locale;

import no.stelvio.common.codestable.CodesTableItem;

/**
 * CodesTableItems for testing.
 * 
 * @author personb66fa0b5ff6e 
 * @version $Id$
 */
public class TestCodesTableItem extends CodesTableItem {
	
	private static final Locale locale1 = new Locale("nb", "NO");
	private static final Locale locale2 = new Locale("nn", "NO");
	
	/** Constructor for TestCodesTableItem.
	 * 
	 * @param code the code
	 * @param decode the decode
	 * @param locale the locale
	 * @param isValid the validity of the item
	 */
	public TestCodesTableItem(String code, String decode, Locale locale, Boolean isValid){
		super(code, decode, locale, isValid);
	}
	
	public TestCodesTableItem() {}
	
	public static TestCodesTableItem getCti1() {
		return new TestCodesTableItem("t1code1", "t1decode1", locale1, Boolean.TRUE);
	}
	
	public static TestCodesTableItem getCti2() {
		return new TestCodesTableItem("t2code2", "t2decode2", locale1, Boolean.TRUE);
	}

	public static TestCodesTableItem getCti3() {
		return new TestCodesTableItem("t3code3", "t3decode3", locale2, Boolean.TRUE);
	}

	public static TestCodesTableItem getCti4() {
		return new TestCodesTableItem("t4code4", "t4decode4", locale2, Boolean.TRUE);
	}

	public static TestCodesTableItem getCti5() {
		return new TestCodesTableItem("t1code1", "t1decode1", locale1, Boolean.TRUE);
	}
}