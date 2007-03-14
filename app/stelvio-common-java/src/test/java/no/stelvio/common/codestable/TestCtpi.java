package no.stelvio.common.codestable;


import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;

/**
 * Codestableitemperiodics for testing.
 * 
 * @author personb66fa0b5ff6e 
 * @version $Id$
 */
@Entity
public class TestCtpi extends CodesTablePeriodicItem<TestCtiCode, String> {
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
	public TestCtpi(TestCtiCode code, String decode, Date fromDate, Date toDate, boolean isValid){
		super(code, decode, fromDate, toDate, isValid);
	}

	public TestCtpi() {}
	
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
	
	public static TestCtpi getCtip1() {
		return new TestCtpi(TestCtiCode.EXISTS_1, "t1decode1", getTestFromDate(), getTestToDate(),  true);
	}

	public static TestCtpi getCtip2() {
		return new TestCtpi(TestCtiCode.EXISTS_2, "t2decode2", getTestFromDate(), getTestToDate(), true);
	}

	public static TestCtpi getCtip3() {
		return new TestCtpi(TestCtiCode.EXISTS_3, "t3decode3", getTestFromDate(), getTestToDate(), true);
	}

	public static TestCtpi getCtip4() {
		return new TestCtpi(TestCtiCode.EXISTS_4, "t4decode4", getTestFromDate(), getTestToDate(), true);
	}
	
	public static TestCtpi getCtip5() {
		return new TestCtpi(TestCtiCode.EXISTS_NOT, "exists_not", getTestFromDate(), getTestToDate(), true);
	}

	public static TestCtpi getCtiWithEmptyDecode() {
		return new TestCtpi(TestCtiCode.EMPTY_DECODE, null, getTestFromDate(), getTestToDate(), true);
	}
}