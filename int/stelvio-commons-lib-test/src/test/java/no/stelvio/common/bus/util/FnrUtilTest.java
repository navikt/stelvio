package no.stelvio.common.bus.util;

import java.util.Calendar;

import junit.framework.TestCase;

public class FnrUtilTest extends TestCase {
	//	 individsifre 500-759
	String fnr1855to1899 = "12345678901"; //01.01.1860
	//	 individsifre 000-499
	String fnr1900to1999 = "12345678901"; //01.01.1930
	//	 individsifre 900-999
	String fnr1940to1999 = "12345678901"; //01.01.1940
	//	 individsifre 500-999
	String fnr2000to2039 = "12345678901"; //01.01.2030

	/* -----VALID D-NUMMER ----*/
	//	add 4 to first number (day)
	String dnr = "41018031416"; // 01.01.1980
	String dnr2 = "71018031439"; // 31.01.1980
	
	/*----- VALID BOSTNR -----*/
	// 	add 2 to third number
	//	 individsifre 500-759
	String bost1855to1899 = "01216050270"; //01.01.1860
	//	 individsifre 000-499
	String bost1900to1999 = "01323010192"; //01.12.1930
	//	 individsifre 900-999
	String bost1940to1999 = "01324090122"; //01.12.1940
	//	 individsifre 500-999
	String bost2000to2039 = "01213090247"; //01.01.2030
	
	/*---- VALID H-NUMMER ----*/
	//	 add 4 to third number
	String hnr1855to1899 = "01416050186"; //01.01.1860
	String hnr1900to1999 = "01523010189"; //01.12.1930
	String hnr1940to1999 = "01524090119"; //01.12.1940
	String hnr2000to2039 = "01413090152"; //01.01.2030
	
	/* ---- INVALID FNR, H-NUMMER, BOSTNR AND D-NUMMER */
	String invalid_dnr1 = "72016050146"; // invalid dnr
	String invalid_dnr2 = "40013010146"; // invalid dnr
	String invalid_hnr1 = "01406050196"; // invalid hnr
	String invalid_hnr2 = "01533010179"; // invalid hnr
	String invalid_bost1 = "01200502708"; // invalid bost
	String invalid_bost2 = "01334090122"; // invalid bost
	String invalid_fnr1= "01804731446"; // invalid bost
	String invalid_fnr2 = "12345678901"; // invalid bost
	
	public void testMakeDnrOrBostnrAdjustments() 
	{
		String adjusted_fnr1855to1899 = FnrUtil.makeDnrOrBostnrAdjustments(fnr1855to1899);
		String adjusted_fnr1900to1999 = FnrUtil.makeDnrOrBostnrAdjustments(fnr1900to1999);
		String adjusted_fnr1940to1999 = FnrUtil.makeDnrOrBostnrAdjustments(fnr1940to1999);
		String adjusted_fnr2000to2039 = FnrUtil.makeDnrOrBostnrAdjustments(fnr2000to2039);
		
		// we should get same fnr back
		assertEquals(fnr1855to1899, adjusted_fnr1855to1899);
		assertEquals(fnr1900to1999, adjusted_fnr1900to1999);
		assertEquals(fnr1940to1999, adjusted_fnr1940to1999);
		assertEquals(fnr2000to2039, adjusted_fnr2000to2039);

		// test dnr
		String expected_dnr = "12345678901"; //01.01.1980
		String expected_dnr2 = "12345678901"; // 31.01.1980
		
		String adjusted_dnr = FnrUtil.makeDnrOrBostnrAdjustments(dnr);
		String adjusted_dnr2 = FnrUtil.makeDnrOrBostnrAdjustments(dnr2);
		
		assertEquals(expected_dnr, adjusted_dnr);
		assertEquals(expected_dnr2, adjusted_dnr2);
		
		// test hnr
		String expected_hnr1855to1899 = "12345678901";
		String expected_hnr1900to1999 = "12345678901";
		String expected_hnr1940to1999 = "12345678901";
		String expected_hnr2000to2039 = "12345678901";
		
		String adjusted_hnr1855to1899 = FnrUtil.makeDnrOrBostnrAdjustments(hnr1855to1899);
		String adjusted_hnr1900to1999 = FnrUtil.makeDnrOrBostnrAdjustments(hnr1900to1999);
		String adjusted_hnr1940to1999 = FnrUtil.makeDnrOrBostnrAdjustments(hnr1940to1999);
		String adjusted_hnr2000to2039 = FnrUtil.makeDnrOrBostnrAdjustments(hnr2000to2039);
		
		assertEquals(expected_hnr1855to1899, adjusted_hnr1855to1899);
		assertEquals(expected_hnr1900to1999, adjusted_hnr1900to1999);
		assertEquals(expected_hnr1940to1999, adjusted_hnr1940to1999);
		assertEquals(expected_hnr2000to2039, adjusted_hnr2000to2039);

		// test bost
		String expected_bost1855to1899 = "12345678901";
		String expected_bost1900to1999 = "12345678901";
		String expected_bost1940to1999 = "12345678901";
		String expected_bost2000to2039 = "12345678901";
		
		String adjusted_bost1855to1899 = FnrUtil.makeDnrOrBostnrAdjustments(bost1855to1899);
		String adjusted_bost1900to1999 = FnrUtil.makeDnrOrBostnrAdjustments(bost1900to1999);
		String adjusted_bost1940to1999 = FnrUtil.makeDnrOrBostnrAdjustments(bost1940to1999);
		String adjusted_bost2000to2039 = FnrUtil.makeDnrOrBostnrAdjustments(bost2000to2039);
		
		assertEquals(expected_bost1855to1899, adjusted_bost1855to1899);
		assertEquals(expected_bost1900to1999, adjusted_bost1900to1999);
		assertEquals(expected_bost1940to1999, adjusted_bost1940to1999);
		assertEquals(expected_bost2000to2039, adjusted_bost2000to2039);
		
		// test invalid
		String adjusted_invalid_dnr1 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_dnr1);
		String adjusted_invalid_dnr2= FnrUtil.makeDnrOrBostnrAdjustments(invalid_dnr2);
		String adjusted_invalid_hnr1 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_hnr1);
		String adjusted_invalid_hnr2 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_hnr2);
		String adjusted_invalid_bost1 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_bost1);
		String adjusted_invalid_bost2 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_bost2);
		String adjusted_invalid_fnr1 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_fnr1);
		String adjusted_invalid_fnr2 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_fnr2);
		
		// We should get same no-valid fnr back
		assertEquals(invalid_dnr1, adjusted_invalid_dnr1);
		assertEquals(invalid_dnr2, adjusted_invalid_dnr2);
		assertEquals(invalid_hnr1, adjusted_invalid_hnr1);
		assertEquals(invalid_hnr2, adjusted_invalid_hnr2);
		assertEquals(invalid_bost1, adjusted_invalid_bost1);
		assertEquals(invalid_bost2, adjusted_invalid_bost2);
		assertEquals(invalid_fnr1, adjusted_invalid_fnr1);
		assertEquals(invalid_fnr2, adjusted_invalid_fnr2);
	}
	
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
	
	public void testGet4DigitYearOfBirth()
	{	
		// Test year of birth for fnr
		String adjusted_fnr1855to1899 = FnrUtil.makeDnrOrBostnrAdjustments(fnr1855to1899); //01.01.1847
		String adjusted_fnr1900to1999 = FnrUtil.makeDnrOrBostnrAdjustments(fnr1900to1999); //01.01.1930
		String adjusted_fnr1940to1999 = FnrUtil.makeDnrOrBostnrAdjustments(fnr1940to1999); //01.01.1940
		String adjusted_fnr2000to2039 = FnrUtil.makeDnrOrBostnrAdjustments(fnr2000to2039); //01.01.2030
		
		int fnr_aar1 = FnrUtil.get4DigitYearOfBirth(adjusted_fnr1855to1899);
		int fnr_aar2 = FnrUtil.get4DigitYearOfBirth(adjusted_fnr1900to1999);
		int fnr_aar3 = FnrUtil.get4DigitYearOfBirth(adjusted_fnr1940to1999);
		int fnr_aar4 = FnrUtil.get4DigitYearOfBirth(adjusted_fnr2000to2039);
		
		assertEquals(1860, fnr_aar1);
		assertEquals(1930, fnr_aar2);
		assertEquals(1940, fnr_aar3);
		assertEquals(2030, fnr_aar4);
		
		// Test year of birth for dnr
		String adjusted_dnr1= FnrUtil.makeDnrOrBostnrAdjustments(dnr); //01.01.1980
		String adjusted_dnr2 = FnrUtil.makeDnrOrBostnrAdjustments(dnr2); //31.01.1980
		
		int dnr1_aar1 = FnrUtil.get4DigitYearOfBirth(adjusted_dnr1);
		int dnr2_aar2 = FnrUtil.get4DigitYearOfBirth(adjusted_dnr2);
		
		assertEquals(1980, dnr1_aar1);
		assertEquals(1980, dnr2_aar2);
		
		// test year of birth for bost
		String adjusted_bost1855to1899 = FnrUtil.makeDnrOrBostnrAdjustments(bost1855to1899); //01.01.1860
		String adjusted_bost1900to1999 = FnrUtil.makeDnrOrBostnrAdjustments(bost1900to1999); //01.01.1930
		String adjusted_bost1940to1999 = FnrUtil.makeDnrOrBostnrAdjustments(bost1940to1999); //01.01.1940
		String adjusted_bost2000to2039 = FnrUtil.makeDnrOrBostnrAdjustments(bost2000to2039); //01.01.2030
		
		int bost_aar1 = FnrUtil.get4DigitYearOfBirth(adjusted_bost1855to1899);
		int bost_aar2 = FnrUtil.get4DigitYearOfBirth(adjusted_bost1900to1999);
		int bost_aar3 = FnrUtil.get4DigitYearOfBirth(adjusted_bost1940to1999);
		int bost_aar4 = FnrUtil.get4DigitYearOfBirth(adjusted_bost2000to2039);
		
		assertEquals(1860, bost_aar1);
		assertEquals(1930, bost_aar2);
		assertEquals(1940, bost_aar3);
		assertEquals(2030, bost_aar4);

		// Test year of birth for hnr
		String adjusted_hnr1855to1899 = FnrUtil.makeDnrOrBostnrAdjustments(hnr1855to1899); //01.01.1860
		String adjusted_hnr1900to1999 = FnrUtil.makeDnrOrBostnrAdjustments(hnr1900to1999); //01.01.1930
		String adjusted_hnr1940to1999 = FnrUtil.makeDnrOrBostnrAdjustments(hnr1940to1999); //01.01.1940
		String adjusted_hnr2000to2039 = FnrUtil.makeDnrOrBostnrAdjustments(hnr2000to2039); //01.01.2030
		
		int hnr_aar1 = FnrUtil.get4DigitYearOfBirth(adjusted_hnr1855to1899);
		int hnr_aar2 = FnrUtil.get4DigitYearOfBirth(adjusted_hnr1900to1999);
		int hnr_aar3 = FnrUtil.get4DigitYearOfBirth(adjusted_hnr1940to1999);
		int hnr_aar4 = FnrUtil.get4DigitYearOfBirth(adjusted_hnr2000to2039);
		
		assertEquals(1860, hnr_aar1);
		assertEquals(1930, hnr_aar2);
		assertEquals(1940, hnr_aar3);
		assertEquals(2030, hnr_aar4);
		
		// Test year of birth for invalid fnr, hnr, dnr and bostnr
		String adjusted_invalid_dnr1 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_dnr1);
		String adjusted_invalid_dnr2 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_dnr2);
		String adjusted_invalid_hnr1 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_hnr1);
		String adjusted_invalid_hnr2 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_hnr2);
		
		String adjusted_invalid_bost1 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_bost1);
		String adjusted_invalid_bost2 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_bost2);
		String adjusted_invalid_fnr1 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_fnr1);
		String adjusted_invalid_fnr2 = FnrUtil.makeDnrOrBostnrAdjustments(invalid_fnr2);
		
		int invalid_aar1 = FnrUtil.get4DigitYearOfBirth(adjusted_invalid_dnr1);
		int invalid_aar2 = FnrUtil.get4DigitYearOfBirth(adjusted_invalid_dnr2);
		int invalid_aar3 = FnrUtil.get4DigitYearOfBirth(adjusted_invalid_hnr1);
		int invalid_aar4 = FnrUtil.get4DigitYearOfBirth(adjusted_invalid_hnr2);
		
		int invalid_aar5 = FnrUtil.get4DigitYearOfBirth(adjusted_invalid_bost1);
		int invalid_aar6 = FnrUtil.get4DigitYearOfBirth(adjusted_invalid_bost2);
		int invalid_aar7 = FnrUtil.get4DigitYearOfBirth(adjusted_invalid_fnr1);
		int invalid_aar8 = FnrUtil.get4DigitYearOfBirth(adjusted_invalid_fnr2);
		
		assertEquals(1860, invalid_aar1);
		assertEquals(1930, invalid_aar2);
		assertEquals(1860, invalid_aar3);
		assertEquals(1930, invalid_aar4);
		
		assertEquals(1905, invalid_aar5);
		assertEquals(1940, invalid_aar6);
		assertEquals(1947, invalid_aar7);
		assertEquals(1930, invalid_aar8);
	}
	
	public void testGetAgeAtDateForFnr()
	{
		Calendar atDate = Calendar.getInstance();
		atDate.set(2008,1,1);
		
		//----------- Test fnr 
		assertEquals(148,FnrUtil.getAgeAtDateForFnr(fnr1855to1899, atDate)); //01.01.1860
		assertEquals(78,FnrUtil.getAgeAtDateForFnr(fnr1900to1999, atDate)); //01.01.1930
		assertEquals(68,FnrUtil.getAgeAtDateForFnr(fnr1940to1999, atDate)); //01.01.1940
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(fnr2000to2039, atDate)); //01.01.2030, should return -1 since birth date is in the future
		
		// ----------- Test dnr
		assertEquals(28,FnrUtil.getAgeAtDateForFnr(dnr, atDate)); //01.01.1980
		assertEquals(27,FnrUtil.getAgeAtDateForFnr(dnr2, atDate)); //31.01.1980, 27 since atDate = 01.01.2008
		
		//------------ Test bostnr 
		assertEquals(148,FnrUtil.getAgeAtDateForFnr(bost1855to1899, atDate)); //01.01.1860
		assertEquals(77,FnrUtil.getAgeAtDateForFnr(bost1900to1999, atDate)); // 01.12.1930, 77 since atDate = 01.01.2008
		assertEquals(67,FnrUtil.getAgeAtDateForFnr(bost1940to1999, atDate)); //01.12.1940, 67 since atDate = 01.01.2008
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(bost2000to2039, atDate)); //01.01.2030
			
		assertEquals(148,FnrUtil.getAgeAtDateForFnr(hnr1855to1899, atDate)); //01.01.1860
		assertEquals(77,FnrUtil.getAgeAtDateForFnr(hnr1900to1999, atDate)); //01.12.1930
		assertEquals(67,FnrUtil.getAgeAtDateForFnr(hnr1940to1999, atDate)); //01.12.1940
		assertEquals(-1,FnrUtil.getAgeAtDateForFnr(hnr2000to2039, atDate)); //01.01.2030
		
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
		atDate2.set(2008,5,7); // 07.05.2008
		
		String fnr1 = "12345678901"; //08.05.1860
		String fnr2 = "12345678901"; //07.05.1860
		String fnr3 = "12345678901"; //06.05.1860

		assertEquals(147,FnrUtil.getAgeAtDateForFnr(fnr1, atDate2));
		assertEquals(148,FnrUtil.getAgeAtDateForFnr(fnr2, atDate2));
		assertEquals(148,FnrUtil.getAgeAtDateForFnr(fnr3, atDate2));
	}
}
