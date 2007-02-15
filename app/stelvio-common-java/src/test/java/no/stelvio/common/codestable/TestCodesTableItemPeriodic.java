package no.stelvio.common.codestable;


import java.util.Calendar;
import java.util.Date;

/**
 * Codestableitemperiodics for testing.
 * 
 * @author personb66fa0b5ff6e 
 * @version $Id$
 */
public class TestCodesTableItemPeriodic extends CodesTableItemPeriodic {
	
	
	//No real use in a test class, added to avoid warning
	private static final long serialVersionUID = 2846761623694997742L;
	
	/**
	 * Constructor for TestCodestableItemPeriodic.
	 * @param code the code
	 * @param decode the decode
	 * @param fromDate the valid from date
	 * @param toDate the valid to date
	 * @param isValid the validity of the item
	 */
	public TestCodesTableItemPeriodic(String code, String decode, Date fromDate, Date toDate, boolean isValid){
		super(code, decode, fromDate, toDate, isValid);
	}

	public TestCodesTableItemPeriodic() {}
	
	private static Date getTestFromDate(){
		Calendar fromcal = Calendar.getInstance();
		fromcal.set(106, 10, 1);	
		return new Date(fromcal.getTimeInMillis());
	}
	
	private static Date getTestToDate(){
		Calendar fromcal = Calendar.getInstance();
		fromcal.set(106, 10, 30);	
		return new Date(fromcal.getTimeInMillis());
	}
	
	public static TestCodesTableItemPeriodic getCtip1() {
		return new TestCodesTableItemPeriodic("t1code1", "t1decode1", getTestFromDate(), getTestToDate(),  true);
	}

	public static TestCodesTableItemPeriodic getCtip2() {
		return new TestCodesTableItemPeriodic("t2code2", "t2decode2", getTestFromDate(), getTestToDate(), true);
	}

	public static TestCodesTableItemPeriodic getCtip3() {
		return new TestCodesTableItemPeriodic("t3code3", "t3decode3", getTestFromDate(), getTestToDate(), true);
	}

	public static TestCodesTableItemPeriodic getCtip4() {
		return new TestCodesTableItemPeriodic("t4code4", "t4decode4", getTestFromDate(), getTestToDate(), true);
	}
	
	public static TestCodesTableItemPeriodic getCtip5() {
		return new TestCodesTableItemPeriodic("t1code1", "t1decode1", getTestFromDate(), getTestToDate(), true);
	}

	public static TestCodesTableItemPeriodic getCtiWithEmptyDecode() {
		return new TestCodesTableItemPeriodic("emptyDecode", null, getTestFromDate(), getTestToDate(), true);
	}
}