package no.stelvio.common.framework.util;

import junit.framework.TestCase;

import no.stelvio.common.framework.error.ApplicationException;
import no.stelvio.common.framework.util.FNRUtil;

/**
 * Test-klasse for FNR util.
 * 
 * @author person7553f5959484, Accenture
 * @author persone5d69f3729a8, Accenture
 * @author Espen Næss, Accenture
 * 
 * @version $Id: FNRUtilTest.java 2649 2005-11-26 15:53:14Z ena2920 $
 */
public class FNRUtilTest extends TestCase {

//	public void testGyldigMod11(){
//		
//		for(int i = 10000; i < 11000; i++){
//			String fnr = "290296";
//			fnr = fnr.concat("" + i);
//			assertFalse("Gyldig fnr = " + fnr, FNRUtil.isMod11Compliant(fnr));
//		}
//	}


	public void testIsFnrNr() {
		assertTrue(FNRUtil.isFnr("12345678901"));
		assertTrue(FNRUtil.isFnr("12345678901"));
		assertTrue(FNRUtil.isFnr("12345678901"));
		assertFalse(FNRUtil.isFnr(null));
		assertFalse(FNRUtil.isFnr(""));
		assertFalse(FNRUtil.isFnr(" "));
		assertFalse(FNRUtil.isFnr("170270475566"));
		assertFalse(FNRUtil.isFnr("1702704755X"));
		assertFalse(FNRUtil.isFnr("1702704755 "));
	}
	
	
	public void testIsDnrNr() {
		assertTrue(FNRUtil.isDnr("51057300780"));
		assertTrue(FNRUtil.isDnr("46126304919"));
		assertTrue(FNRUtil.isDnr("53036708596"));
		assertFalse(FNRUtil.isDnr(null));
		assertFalse(FNRUtil.isDnr(""));
		assertFalse(FNRUtil.isDnr(" "));
		assertFalse(FNRUtil.isDnr("530367085966"));
		assertFalse(FNRUtil.isDnr("5303670859X"));
		assertFalse(FNRUtil.isDnr("5303670859 "));
		
		
		
	}


	public void testIsPerson() {
		assertTrue(FNRUtil.isPerson("12345678901")); // fnr
		assertFalse(FNRUtil.isPerson("22310500120")); // bost
		assertTrue(FNRUtil.isPerson("51057300780")); // dnr
		assertFalse(FNRUtil.isPerson("22300500130")); // bost
	}


	public void testIsDoFodt() throws ApplicationException {
		assertFalse("Barnet er ikke død-født;", FNRUtil.isDoFodt("12345678901"));
		assertTrue("Barnet er død-født;", FNRUtil.isDoFodt("12345678901"));

		try {
			FNRUtil.isDoFodt("jalla");
			fail("Denne skal kaste en ApplicationException siden fnr ikke gyldig");
		} catch (IllegalArgumentException e) {
			// skal skje
		}
	}


	public void testMod11Compliant() {
		assertTrue(FNRUtil.isPerson("12345678901")); // fnr
		assertFalse(FNRUtil.isPerson("12345678901")); // fnr endet
		assertTrue(FNRUtil.isPerson("51057300780")); // dnr
		assertFalse(FNRUtil.isPerson("51057700780")); // dnr endret
		assertFalse(FNRUtil.isPerson("80000338153")); // org. nr.
	}

	public void testGetPersonnummerDel() {
		assertEquals("Skulle ikke ha feilet!", FNRUtil.getPersonnummerDel("12345678901"), "35133"); // fnr
		assertEquals("Skulle ikke ha feilet!", FNRUtil.getPersonnummerDel("12345678901"), "29907"); // fnr
		assertNull("Skulle ha feilet!", FNRUtil.getPersonnummerDel("12345678901")); // fnr
	}

	public void testIsFnrDateValid() {
		assertTrue(FNRUtil.isFnrDateValid("12345678901")); // ekte fnr
		assertFalse(FNRUtil.isFnrDateValid("12345678901")); // ekte fnr, endret
	}

	public void testIsValidFnr() {
		// testes implisitt gjennom IsFnr() og isDnr()
	}


}
