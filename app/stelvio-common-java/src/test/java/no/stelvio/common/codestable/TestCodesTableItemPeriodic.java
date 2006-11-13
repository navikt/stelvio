package no.stelvio.common.codestable;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import no.stelvio.common.codestable.CodesTableItemPeriodic;

/**
 * Codestableitemperiodics for testing.
 * 
 * @author personb66fa0b5ff6e 
 * @version $Id$
 */
public class TestCodesTableItemPeriodic extends CodesTableItemPeriodic {
	
	private static final Locale locale1 = new Locale("nb", "NO");
	private static final Locale locale2 = new Locale("nn", "NO");
	
	/**
	 * Constructor for TestCodestableItemPeriodic.
	 * @param code the code
	 * @param decode the decode
	 * @param fromDate the valid from date
	 * @param toDate the valid to date
	 * @param locale the locale
	 * @param isValid the validity of the item
	 */
	public TestCodesTableItemPeriodic(String code, String decode, Date fromDate, Date toDate, Locale locale, Boolean isValid){
		super(code, decode, fromDate, toDate, locale, isValid);
	}

	public static final TestCodesTableItemPeriodic CTIP1 = new TestCodesTableItemPeriodic("t1code1", "t1decode1", getTestFromDate(), getTestToDate(), locale1, Boolean.TRUE);
	public static final TestCodesTableItemPeriodic CTIP2 = new TestCodesTableItemPeriodic("t2code2", "t2decode2", getTestFromDate(), getTestToDate(), locale1, Boolean.TRUE);
	public static final TestCodesTableItemPeriodic CTIP3 = new TestCodesTableItemPeriodic("t3code3", "t3decode3", getTestFromDate(), getTestToDate(), locale2, Boolean.TRUE);
	public static final TestCodesTableItemPeriodic CTIP4 = new TestCodesTableItemPeriodic("t4code4", "t4decode4", getTestFromDate(), getTestToDate(), locale2, Boolean.TRUE);
	public static final TestCodesTableItemPeriodic CTIP5 = new TestCodesTableItemPeriodic("t1code1", "t1decode1", getTestFromDate(), getTestToDate(), locale1, Boolean.TRUE);

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