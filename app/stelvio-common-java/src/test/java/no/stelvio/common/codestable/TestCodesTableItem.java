package no.stelvio.common.codestable;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import no.stelvio.common.codestable.CodesTableItem;

/**
 * Codestableitems for testing.
 * 
 * @author personb66fa0b5ff6e 
 * @version $Id$
 */
public class TestCodesTableItem extends CodesTableItem {
	
	private static final Locale locale1 = new Locale("nb", "NO");
	private static final Locale locale2 = new Locale("nn", "NO");
	
	public TestCodesTableItem(String code, String decode, Date fromDate, Date toDate, Locale locale, Boolean isValid){
		//super(code, decode, fromDate, toDate, locale, isValid);
	}
	
	public static final TestCodesTableItem CTI1 = new TestCodesTableItem("t1code1", "t1decode1", getTestFromDate(), getTestToDate(), locale1, Boolean.TRUE);
	public static final TestCodesTableItem CTI2 = new TestCodesTableItem("t2code2", "t2decode2", getTestFromDate(), getTestToDate(), locale1, Boolean.TRUE);
	public static final TestCodesTableItem CTI3 = new TestCodesTableItem("t3code3", "t3decode3", getTestFromDate(), getTestToDate(), locale2, Boolean.TRUE);
	public static final TestCodesTableItem CTI4 = new TestCodesTableItem("t4code4", "t4decode4", getTestFromDate(), getTestToDate(), locale2, Boolean.TRUE);
	public static final TestCodesTableItem CTI5 = new TestCodesTableItem("t1code1", "t1decode1", getTestFromDate(), getTestToDate(), locale1, Boolean.TRUE);	
	
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
}