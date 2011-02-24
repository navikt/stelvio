package no.stelvio.domain.person;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

/**
 * Tests the functionality of the PID class by using a set of allowed and disallowed PIDs.
 * 
 */
public class PidTest {
	private String monthAbove9BostNr = "01327200336";
	private String monthBelow10BostNr = "04250100286";
	private String normalFnr = "12345678901";

	// Pids with legal whitespace
	private String monthAbove9BostNrWs = "013272 00336";
	private String monthBelow10BostNrWs = "042501 00286";
	private String normalFnrWs = "260676 37924";

	// Pids with illeagal whitespace
	private String monthAbove9BostNrIWs = "01327 200336";
	private String monthBelow10BostNrIWs = "0425010 0286";
	private String normalFnrIWs = "260 67637924";

	// Pids with special Fnr
	private String specialFnr0 = "12345678901";
	private String specialFnr1 = "12345678901";
	private String specialFnr2 = "12345678901";
	private String specialFnr29Feb = "12345678901";
	private String specialFnr30Feb = "12345678901";
	private String specialFnr61May = "61057300000";
	private String specialFnr21XXX = "21257300000";
	private String specialFnrZero = "12345678901";
	private String specialFnr03Jan76 = "12345678901";
	private String specialFnr03Jan73 = "12345678901"; // Passes the mod11 check, but not the special circumstance check
	private String specialFnr08Jun57 = "12345678901";

	// D-numbers
	private String dnummerValid0 = "55125501493";
	private String dnummerValid1 = "46053102085";
	private String dnummerInvalidUnlessAcceptSpecialCircumstances = "41018100000";
	// private String dnummerInvalidSpecial0 = "41018100000";
	private String dNummerValidLowPnr1 = "52025800001";
	private String dNummerValidLowPnr2 = "61105800001";
	private String dNummerValidLowPnr3 = "62126000001";
	private String dNummerValidLowPnr4 = "47072900002";
	private String dNummerValidLowPnr5 = "62015900003";
	private String dNummerValidLowPnr6 = "43092700004";
	private String dNummerValidLowPnr7 = "50036000004";
	private String dNummerValidLowPnr8 = "46126000006";
	private String dNummerValidLowPnr9 = "71036100006";
	private String dNummerValidLowPnr10 = "71031300007";
	private String dNummerValidLowPnr11 = "54065600008";
	private String dNummerValidLowPnr12 = "60041400008";
	private String dNummerValidLowPnr13 = "56056000009";

	/**
	 * Verify that whitespace compliance check is ok.
	 */
	@Test
	public void whitespaceComplianceCheckIsOk() {
		// These three validations should be ok
		checkWhitespaceCompliance(monthAbove9BostNrWs, true);
		checkWhitespaceCompliance(monthBelow10BostNrWs, true);
		checkWhitespaceCompliance(normalFnrWs, true);

		// These three validations should NOT be ok
		checkWhitespaceCompliance(monthAbove9BostNrIWs, false);
		checkWhitespaceCompliance(monthBelow10BostNrIWs, false);
		checkWhitespaceCompliance(normalFnrIWs, false);
	}

	/**
	 * Verify that special circumstances are not allowed when using strict Fnr validation.
	 */
	@Test
	public void specialCircumstanceFnrValidatedDisable() {
		// validate the Pids with special circumstances disabled

		assertTrue("Special circumstance test without acceptspecialcircumstance set failed for Fnr" + specialFnr0, !Pid
				.isValidPid(specialFnr0, false));
		assertTrue("Special circumstance test without acceptspecialcircumstance set failed for Fnr" + specialFnr0, !Pid
				.isValidPid(specialFnr0));
		assertTrue("Special circumstance test without acceptspecialcircumstance set failed for Fnr" + specialFnr1, !Pid
				.isValidPid(specialFnr1, false));
		assertTrue("Special circumstance test without acceptspecialcircumstance set failed for Fnr" + specialFnr1, !Pid
				.isValidPid(specialFnr1));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr2, !Pid.isValidPid(specialFnr2));

		assertTrue("Special circumstance test failed for Fnr" + specialFnr29Feb, !Pid.isValidPid(specialFnr29Feb));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr29Feb, !Pid.isValidPid(specialFnr29Feb, false));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr30Feb, !Pid.isValidPid(specialFnr30Feb));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr29Feb, !Pid.isValidPid(specialFnr30Feb, false));

		assertTrue("Special circumstance test failed for Fnr" + specialFnr61May, !Pid.isValidPid(specialFnr61May));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr61May, !Pid.isValidPid(specialFnr61May, false));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr21XXX, !Pid.isValidPid(specialFnr21XXX));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr21XXX, !Pid.isValidPid(specialFnr21XXX, false));
		assertTrue("Special circumstance test failed for Fnr" + specialFnrZero, !Pid.isValidPid(specialFnrZero));
		assertTrue("Special circumstance test failed for Fnr" + specialFnrZero, !Pid.isValidPid(specialFnrZero, false));
		assertFalse("Special circumstance test failed for Fnr " + specialFnr03Jan73, Pid.isValidPid(specialFnr03Jan73, false));

		assertFalse("Special circumstance test failed for Fnr " + specialFnr03Jan76, Pid.isValidPid(specialFnr03Jan76, false));
		assertFalse("Special circumstance test failed for Fnr " + specialFnr08Jun57, Pid.isValidPid(specialFnr08Jun57, false));
	}

	/**
	 * Verify that special circumstances are allowed when using relaxed Fnr validation.
	 */
	@Test
	public void specialCircumstanceFnrValidatedEnabled() {
		// validate the Pids with special circumstances enabled
		assertTrue("Special circumstance test failed for Fnr" + specialFnr0, Pid.isValidPid(specialFnr0, true));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr1, Pid.isValidPid(specialFnr1, true));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr2, !Pid.isValidPid(specialFnr2));

		assertTrue("Special circumstance test failed for Fnr" + specialFnr29Feb, Pid.isValidPid(specialFnr29Feb, true));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr30Feb, !Pid.isValidPid(specialFnr30Feb, true));

		assertTrue("Special circumstance test failed for Fnr" + specialFnr61May, Pid.isValidPid(specialFnr61May, true));
		assertTrue("Special circumstance test failed for Fnr" + specialFnr21XXX, Pid.isValidPid(specialFnr21XXX, true));
		assertTrue("Special circumstance test failed for Fnr" + specialFnrZero, !Pid.isValidPid(specialFnrZero, true));

		assertTrue("Special circumstance test failed for Fnr " + specialFnr03Jan73, Pid.isValidPid(specialFnr03Jan73, true));
		assertTrue("Special circumstance test failed for Fnr " + specialFnr03Jan76, Pid.isValidPid(specialFnr03Jan76, true));
	}

	/**
	 * Verify pid with special circumstances.
	 */
	@Test
	public void createPidsWithSpecialCircumstance() {
		// create Pids
		try {
			new Pid(specialFnr0, true);
		} catch (PidValidationException pe) {
			fail("PidValidationException should not be thrown");
		}

		try {
			new Pid(specialFnr0, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr1, true);
		} catch (PidValidationException pe) {
			fail("PidValidationException should not be thrown");
		}

		try {
			new Pid(specialFnr1, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr2, true);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr2, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr29Feb, true);
		} catch (PidValidationException pe) {
			fail("PidValidationException should not be thrown");
		}

		try {
			new Pid(specialFnr29Feb, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr30Feb, true);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr30Feb, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr61May, true);
		} catch (PidValidationException pe) {
			fail("PidValidationException should not be thrown");
		}

		try {
			new Pid(specialFnr61May, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr21XXX, true);
		} catch (PidValidationException pe) {
			fail("PidValidationException should not be thrown");
		}

		try {
			new Pid(specialFnr21XXX, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnrZero, true);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnrZero, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}
	}

	/**
	 * Test validation of fnr.
	 */
	@Test
	public void fnrCanBeValidated() {
		assertTrue("Could not create Pid using Fnr", Pid.isValidPid(normalFnr));
	}

	/**
	 * Test validation of bost nr.
	 */
	@Test
	public void bostNummerCanBeValidated() {
		assertTrue("Could not create fnr using a Bostnummer with birth month > 9", Pid.isValidPid(monthAbove9BostNr));
		assertTrue("Could not create fnr using a Bostnummer with birth month < 10", Pid.isValidPid(monthBelow10BostNrWs));
	}

	/**
	 * Test d-nummer.
	 */
	@Test
	public void dnummerCanBeValidated() {
		// Check valid dnummer
		assertTrue("Could not create fnr using a valid Dnummer with day > 40:" + dnummerValid0, Pid.isValidPid(dnummerValid0));

		// Verify method to check dnummer
		try {
			assertTrue("Failed check of valid dnummer:" + dnummerValid1, new Pid(dnummerValid1).isDnummer());
		} catch (PidValidationException pe) {
			fail("PidValidationException should not be thrown:" + pe);
		}

		// Check normal fnr
		try {
			assertFalse("Failed check of dnummer with normal fnr:" + normalFnr, new Pid(normalFnr).isDnummer());
		} catch (PidValidationException pe) {
			fail("PidValidationException should not be thrown:" + pe);
		}

		// Check that dnummer verification of a special case dnummer is accepted when special circumstance is true
		try {
			assertTrue("Failed check of special dnummer:" + dnummerInvalidUnlessAcceptSpecialCircumstances, new Pid(
					dnummerInvalidUnlessAcceptSpecialCircumstances, true).isDnummer());
		} catch (PidValidationException pe) {
			fail("PidValidationException should not be thrown:" + pe);
		}

	}

	/**
	 * Test that fødselsdato is extracted correct from valid pid.
	 */
	@Test
	public void fodselsdatoIsExtractedCorrectFromValidPid() {
		// 26. _June_ 1976
		Date expectedDate = new GregorianCalendar(1976, GregorianCalendar.JUNE, 26).getTime();
		// 1. _December_ 1972
		Date expectedDateForAbove = new GregorianCalendar(1972, GregorianCalendar.DECEMBER, 1).getTime();
		// 4. _May_ 1901
		Date expectedDateForBelow = new GregorianCalendar(1901, GregorianCalendar.MAY, 4).getTime();

		assertThat(new Pid(normalFnr).getFodselsdato(), is(equalTo(expectedDate)));
		assertThat(new Pid(monthAbove9BostNr).getFodselsdato(), is(equalTo(expectedDateForAbove)));
		assertThat(new Pid(monthBelow10BostNr).getFodselsdato(), is(equalTo(expectedDateForBelow)));
	}

	/**
	 * Test that fødselsdato throws exception when invalid pid.
	 */
	@Test
	public void fodselsdatoThrowsExceptionFromInvalidPid() {

		try {
			new Pid(specialFnr0, true).getFodselsdato();
			fail("PidValidationException should be thrown for pid " + specialFnr0);
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr1, true).getFodselsdato();
			fail("PidValidationException should be thrown for pid " + specialFnr1);
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr29Feb, true).getFodselsdato();
			fail("PidValidationException should be thrown for pid " + specialFnr29Feb);
		} catch (PidValidationException pe) {
			assertTrue(true);
		}

		try {
			new Pid(specialFnr61May, true).getFodselsdato();
		} catch (PidValidationException pe) {
			fail("PidValidationException should not be thrown with Pid: " + specialFnr61May);
		}

		try {
			new Pid(specialFnr21XXX, true).getFodselsdato();
			fail("PidValidationException should be thrown for pid " + specialFnr21XXX);
		} catch (PidValidationException pe) {
			assertTrue(true);
		}
	}

	/**
	 * Check whitespace compliance.
	 * 
	 * @param pid pid
	 * @param isCompliant true if compliant
	 */
	private void checkWhitespaceCompliance(String pid, boolean isCompliant) {
		assertEquals("Whitespace test failed with Pid: " + pid, isCompliant, Pid.isWhitespaceCompliant(pid));
	}

	/**
	 * Test age calculation, valid cases.
	 */
	@Test
	public void calculateAge() {

		/* DOB: 1976.06.26 */
		Pid pid = new Pid("12345678901");

		/* DOB: 1944.02.29 */
		Pid pidSkuddar = new Pid("12345678901");
		
		GregorianCalendar greg = new GregorianCalendar();
		int age;

		/* valid dnumber, DOB: 1980.01.31 */
		Pid dnumber = new Pid("71018031439");

		/* 1 year */
		greg.set(1980, GregorianCalendar.JANUARY, 1);
		age = Pid.calculateAge(dnumber, greg.getTime());
		assertEquals(0, age);

		/* 0 years */
		greg.set(1981, GregorianCalendar.JANUARY, 30);
		age = Pid.calculateAge(dnumber, greg.getTime());
		assertEquals(0, age);

		/* 1 year */
		greg.set(1981, GregorianCalendar.JANUARY, 31);
		age = Pid.calculateAge(dnumber, greg.getTime());
		assertEquals(1, age);

		/* 31 years */
		greg.set(2007, GregorianCalendar.JUNE, 25);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(30, age);

		/* 31 years */
		greg.set(2007, GregorianCalendar.JUNE, 26);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(31, age);

		/* 30 years */
		greg.set(2007, GregorianCalendar.JUNE, 27);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(31, age);

		/* 30 years */
		greg.set(2006, GregorianCalendar.JUNE, 25);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(29, age);

		/* 30 years */
		greg.set(2006, GregorianCalendar.JUNE, 26);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(30, age);

		/* 30 years */
		greg.set(2006, GregorianCalendar.JUNE, 27);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(30, age);

		/* 10 years */
		greg.set(1987, GregorianCalendar.JUNE, 25);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(10, age);

		/* -9 years == 0 */
		greg.set(1967, GregorianCalendar.JUNE, 25);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(0, age);

		// test case with valid d-nummer
		/* DOB: 1955.12.15 */
		Pid pidValid = new Pid(dnummerValid0, false);

		/* 41 years */
		greg.set(1997, GregorianCalendar.DECEMBER, 14);
		age = Pid.calculateAge(pidValid, greg.getTime());
		assertEquals(41, age);

		/* 42 years */
		greg.set(1997, GregorianCalendar.DECEMBER, 15);
		age = Pid.calculateAge(pidValid, greg.getTime());
		assertEquals(42, age);

		// test case with invalid d-nummer (do not pass modulus test)
		/* DOB: 1981.01.01 */
		Pid pidInvalidUnlessAcceptSpecialCircumstances = new Pid(dnummerInvalidUnlessAcceptSpecialCircumstances, true);

		/* 19 years */
		greg.set(2000, GregorianCalendar.DECEMBER, 31);
		age = Pid.calculateAge(pidInvalidUnlessAcceptSpecialCircumstances, greg.getTime());
		assertEquals(19, age);

		/* 20 years */
		greg.set(2001, GregorianCalendar.JANUARY, 1);
		age = Pid.calculateAge(pidInvalidUnlessAcceptSpecialCircumstances, greg.getTime());
		assertEquals(20, age);

		/* 20 years */
		greg.set(2001, GregorianCalendar.MAY, 23);
		age = Pid.calculateAge(pidInvalidUnlessAcceptSpecialCircumstances, greg.getTime());
		assertEquals(20, age);

		/* Born skuddar/current year NOT skuddar - 66 years */
		greg.set(2011, GregorianCalendar.FEBRUARY, 27);
		age = Pid.calculateAge(pidSkuddar, greg.getTime());
		assertEquals(66, age);

		/* Born skuddar/current year NOT skuddar - 67 years (by law: the birthday is on 28/2 when not skuddar) */
		greg.set(2011, GregorianCalendar.FEBRUARY, 28);
		age = Pid.calculateAge(pidSkuddar, greg.getTime());
		assertEquals(67, age);
		
		/* Born skuddar/current year skuddar - 67 years (by law: the birthday is on 29/2 when skuddar) */
		greg.set(2012, GregorianCalendar.FEBRUARY, 28);
		age = Pid.calculateAge(pidSkuddar, greg.getTime());
		assertEquals(67, age);
		
		/* Born skuddar/current year skuddar - 68 years (by law: the birthday is on 29/2 when skuddar) */
		greg.set(2012, GregorianCalendar.FEBRUARY, 29);
		age = Pid.calculateAge(pidSkuddar, greg.getTime());
		assertEquals(68, age);
	}

	/**
	 * Test age calculation, invalid cases.
	 */
	@Test
	public void calculateAgeThrowsExceptionOnInvalidPid() {
		Pid pidSpecial = new Pid("12345678901", true);

		/* 31 years */
		GregorianCalendar greg = new GregorianCalendar();
		greg.set(2006, GregorianCalendar.JULY, 26);

		try {
			Pid.calculateAge(pidSpecial, greg.getTime());
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			assertTrue(true);
		}
	}

	/**
	 * Test tostring.
	 */
	@Test
	public void toStringReturnsFnr() {
		Pid pid = new Pid(normalFnr, false);
		assertThat(pid.toString(), is(equalTo(normalFnr)));
	}

	/**
	 * Verify that PIDs that are of D-number type validate using strict validation even though they end with 00000N, where N is
	 * in [0,9]. See CR 97238.
	 */
	@Test
	public void creatingStrictDnrWithLowPnr() {
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr1, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr2, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr3, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr4, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr5, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr6, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr7, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr8, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr9, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr10, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr11, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr12, false));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr13, false));
	}

	/**
	 * Verify that PIDs that are of D-number type validate using relaxed validation even though they end with 00000N, where N is
	 * in [0,9]. See CR 97238.
	 */
	@Test
	public void creatingRelaxedDnrWithLowPnr() {
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr1, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr2, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr3, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr4, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr5, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr6, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr7, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr8, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr9, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr10, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr11, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr12, true));
		assertTrue("Could not create Dnr", Pid.isValidPid(dNummerValidLowPnr13, true));
	}
}
