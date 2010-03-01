package no.stelvio.common.bus.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import junit.framework.TestCase;

public class FnrUtilTest extends TestCase {
	//	 individsifre 500-759
	private String fnr1855to1899 = "12345678901"; //01.01.1860
	//	 individsifre 000-499
	private String fnr1900to1999 = "12345678901"; //01.01.1930
	//	 individsifre 900-999
	private String fnr1940to1999 = "12345678901"; //01.01.1940
	//	 individsifre 500-999
	private String fnr2000to2039 = "12345678901"; //01.01.2030

	/* -----VALID D-NUMMER ----*/
	//	add 4 to first number (day)
	private String dnr = "41018031416"; // 01.01.1980
	private String dnr2 = "71018031439"; // 31.01.1980
	
	/*----- VALID BOSTNR -----*/
	// 	add 2 to third number
	//	 individsifre 500-759
	private String bost1855to1899 = "01216050270"; //01.01.1860
	//	 individsifre 000-499
	private String bost1900to1999 = "01323010192"; //01.12.1930
	//	 individsifre 900-999
	private String bost1940to1999 = "01324090122"; //01.12.1940
	//	 individsifre 500-999
	private String bost2000to2039 = "01213090247"; //01.01.2030
	
	/*---- VALID H-NUMMER ----*/
	//	 add 4 to third number
	private String hnr1855to1899 = "01416050186"; //01.01.1860
	private String hnr1900to1999 = "01523010189"; //01.12.1930
	private String hnr1940to1999 = "01524090119"; //01.12.1940
	private String hnr2000to2039 = "01413090152"; //01.01.2030
	
	private String fdat = "12345678901"; //19.04.2001
	
	/* ---- INVALID FNR, H-NUMMER, BOSTNR AND D-NUMMER */
	private String invalid_dnr1 = "72016050146"; // invalid dnr
	private String invalid_dnr2 = "40013010146"; // invalid dnr
	private String invalid_hnr1 = "01406050196"; // invalid hnr
	private String invalid_hnr2 = "01533010179"; // invalid hnr
	private String invalid_bost1 = "01200502708"; // invalid bost
	private String invalid_bost2 = "01334090122"; // invalid bost
	private String invalid_fnr1= "01804731446"; // invalid bost
	private String invalid_fnr2 = "12345678901"; // invalid bost
	
	public void testIsValidFnr()
	{	
		// SHOULD PASS
		assertTrue(FnrUtil.isValidFnr(fnr1855to1899));
		assertTrue(FnrUtil.isValidFnr(fnr1900to1999));
		assertTrue(FnrUtil.isValidFnr(fnr1940to1999));
		assertTrue(FnrUtil.isValidFnr(fnr2000to2039));
			
		assertTrue(FnrUtil.isValidFnr(dnr));
		assertTrue(FnrUtil.isValidFnr(dnr2));
			
		assertTrue(FnrUtil.isValidFnr(bost1855to1899));
		assertTrue(FnrUtil.isValidFnr(bost1900to1999));
		assertTrue(FnrUtil.isValidFnr(bost1940to1999));
		assertTrue(FnrUtil.isValidFnr(bost2000to2039));

		assertTrue(FnrUtil.isValidFnr(hnr1855to1899));
		assertTrue(FnrUtil.isValidFnr(hnr1900to1999));
		assertTrue(FnrUtil.isValidFnr(hnr1940to1999));
		assertTrue(FnrUtil.isValidFnr(hnr2000to2039));
		
		assertTrue(FnrUtil.isValidFnr(fdat, true));
		
		// SHOULD FAIL
		assertFalse(FnrUtil.isValidFnr(invalid_dnr1));
		assertFalse(FnrUtil.isValidFnr(invalid_dnr2));
		assertFalse(FnrUtil.isValidFnr(invalid_hnr1));
		assertFalse(FnrUtil.isValidFnr(invalid_hnr2));
		assertFalse(FnrUtil.isValidFnr(invalid_bost1));
		assertFalse(FnrUtil.isValidFnr(invalid_bost2));
		assertFalse(FnrUtil.isValidFnr(invalid_fnr1));
		assertFalse(FnrUtil.isValidFnr(invalid_fnr2));
	}
	
	public void testGetAgeAtDateForFnr()
	{
		Calendar atDate = Calendar.getInstance();
		atDate.set(2008,Calendar.JANUARY,1);
		
		//----------- Test fnr 
		assertEquals(148,FnrUtil.getAgeAtDateForFnr(fnr1855to1899, atDate)); //01.01.1860
		assertEquals(78,FnrUtil.getAgeAtDateForFnr(fnr1900to1999, atDate)); //01.01.1930
		assertEquals(68,FnrUtil.getAgeAtDateForFnr(fnr1940to1999, atDate)); //01.01.1940
		assertEquals(0,FnrUtil.getAgeAtDateForFnr(fnr2000to2039, atDate)); //01.01.2030, should return -1 since birth date is in the future
		
		// ----------- Test dnr
		assertEquals(28,FnrUtil.getAgeAtDateForFnr(dnr, atDate)); //01.01.1980
		assertEquals(27,FnrUtil.getAgeAtDateForFnr(dnr2, atDate)); //31.01.1980, 27 since atDate = 01.01.2008
		
		//------------ Test bostnr 
		assertEquals(148,FnrUtil.getAgeAtDateForFnr(bost1855to1899, atDate)); //01.01.1860
		assertEquals(77,FnrUtil.getAgeAtDateForFnr(bost1900to1999, atDate)); // 01.12.1930, 77 since atDate = 01.01.2008
		assertEquals(67,FnrUtil.getAgeAtDateForFnr(bost1940to1999, atDate)); //01.12.1940, 67 since atDate = 01.01.2008
		assertEquals(0,FnrUtil.getAgeAtDateForFnr(bost2000to2039, atDate)); //01.01.2030
			
		assertEquals(148,FnrUtil.getAgeAtDateForFnr(hnr1855to1899, atDate)); //01.01.1860
		assertEquals(77,FnrUtil.getAgeAtDateForFnr(hnr1900to1999, atDate)); //01.12.1930
		assertEquals(67,FnrUtil.getAgeAtDateForFnr(hnr1940to1999, atDate)); //01.12.1940
		assertEquals(0,FnrUtil.getAgeAtDateForFnr(hnr2000to2039, atDate)); //01.01.2030
		
		//------------ Test with invalid fnr
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(invalid_dnr1, atDate));
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(invalid_dnr2, atDate));
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(invalid_hnr1, atDate));
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(invalid_hnr2, atDate));
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(invalid_bost1, atDate));
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(invalid_bost2, atDate));
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(invalid_fnr1, atDate));
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(invalid_fnr2, atDate));
		
		//------------- Test other
		Calendar atDate2 = Calendar.getInstance();
		atDate2.set(2008,Calendar.MAY,7); // 07.05.2008
		
		String fnr1 = "12345678901"; //08.05.1860
		String fnr2 = "12345678901"; //07.05.1860
		String fnr3 = "12345678901"; //06.05.1860

		assertEquals(147,FnrUtil.getAgeAtDateForFnr(fnr1, atDate2));
		assertEquals(148,FnrUtil.getAgeAtDateForFnr(fnr2, atDate2));
		assertEquals(148,FnrUtil.getAgeAtDateForFnr(fnr3, atDate2));
	}
	
	public void testGetBirthdateForFnr()
	{
		//----------- Test fnr 
		assertEquals(getDate(1860, Calendar.JANUARY, 1),FnrUtil.getBirthdateForFnr(fnr1855to1899)); //01.01.1860
		assertEquals(getDate(1930, Calendar.JANUARY, 1),FnrUtil.getBirthdateForFnr(fnr1900to1999)); //01.01.1930
		assertEquals(getDate(1940, Calendar.JANUARY, 1),FnrUtil.getBirthdateForFnr(fnr1940to1999)); //01.01.1940
		assertEquals(getDate(2030, Calendar.JANUARY, 1),FnrUtil.getBirthdateForFnr(fnr2000to2039)); //01.01.2030
		
		// ----------- Test dnr
		assertEquals(getDate(1980, Calendar.JANUARY, 1),FnrUtil.getBirthdateForFnr(dnr)); //01.01.1980
		assertEquals(getDate(1980, Calendar.JANUARY, 31),FnrUtil.getBirthdateForFnr(dnr2)); //31.01.1980
		
		//------------ Test bostnr 
		assertEquals(getDate(1860, Calendar.JANUARY, 1),FnrUtil.getBirthdateForFnr(bost1855to1899)); //01.01.1860
		assertEquals(getDate(1930, Calendar.DECEMBER, 1),FnrUtil.getBirthdateForFnr(bost1900to1999)); // 01.12.1930
		assertEquals(getDate(1940, Calendar.DECEMBER, 1),FnrUtil.getBirthdateForFnr(bost1940to1999)); //01.12.1940
		assertEquals(getDate(2030, Calendar.JANUARY, 1),FnrUtil.getBirthdateForFnr(bost2000to2039)); //01.01.2030
			
		assertEquals(getDate(1860, Calendar.JANUARY, 1),FnrUtil.getBirthdateForFnr(hnr1855to1899)); //01.01.1860
		assertEquals(getDate(1930, Calendar.DECEMBER, 1),FnrUtil.getBirthdateForFnr(hnr1900to1999)); //01.12.1930
		assertEquals(getDate(1940, Calendar.DECEMBER, 1),FnrUtil.getBirthdateForFnr(hnr1940to1999)); //01.12.1940
		assertEquals(getDate(2030, Calendar.JANUARY, 1),FnrUtil.getBirthdateForFnr(hnr2000to2039)); //01.01.2030
		
		//------------ Test with invalid fnr
		assertNull(FnrUtil.getBirthdateForFnr(invalid_dnr1));
		assertNull(FnrUtil.getBirthdateForFnr(invalid_dnr2));
		assertNull(FnrUtil.getBirthdateForFnr(invalid_hnr1));
		assertNull(FnrUtil.getBirthdateForFnr(invalid_hnr2));
		assertNull(FnrUtil.getBirthdateForFnr(invalid_bost1));
		assertNull(FnrUtil.getBirthdateForFnr(invalid_bost2));
		assertNull(FnrUtil.getBirthdateForFnr(invalid_fnr1));
		assertNull(FnrUtil.getBirthdateForFnr(invalid_fnr2));
		
		String fnr1 = "12345678901"; //08.05.1860
		String fnr2 = "12345678901"; //07.05.1860
		String fnr3 = "12345678901"; //06.05.1860

		assertEquals(getDate(1860, Calendar.MAY, 8),FnrUtil.getBirthdateForFnr(fnr1));
		assertEquals(getDate(1860, Calendar.MAY, 7),FnrUtil.getBirthdateForFnr(fnr2));
		assertEquals(getDate(1860, Calendar.MAY, 6),FnrUtil.getBirthdateForFnr(fnr3));
	}
	
	private Date getDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, date);
		return cal.getTime();
	}
}
