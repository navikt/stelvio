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
	String monthAbove9BostNr = "01327200336";
	String monthBelow10BostNr = "04250100286";
	String normalFnr = "12345678901";

	// Pids with legal whitespace
	String monthAbove9BostNrWs = "013272 00336";
	String monthBelow10BostNrWs = "042501 00286";
	String normalFnrWs = "260676 37924";

	// Pids with illeagal whitespace
	String monthAbove9BostNrIWs = "01327 200336";
	String monthBelow10BostNrIWs = "0425010 0286";
	String normalFnrIWs = "260 67637924";

	// Pids with special Fnr
	String specialFnr0 = "12345678901";
	String specialFnr1 = "12345678901";
	String specialFnr2 = "12345678901";
	String specialFnr29Feb = "12345678901";
	String specialFnr30Feb = "12345678901";
	String specialFnr61May = "61057300000";
	String specialFnr21XXX = "21257300000";
	String specialFnrZero = "12345678901";
	String specialFnr03Jan76 = "12345678901";
	String specialFnr03Jan73 = "12345678901"; // Passes the mod11 check, but not the special circumstance check
	String specialFnr08Jun57 = "12345678901";

	// D-numbers
	String dnummerValid0 = "55125501493";
	String dnummerValid1 = "46053102085";
	String dnummerInvalidSpecial0 = "41018100000";
	String dNummerValidLowPnr1 = "52025800001";
	String dNummerValidLowPnr2 = "61105800001";
	String dNummerValidLowPnr3 = "62126000001";
	String dNummerValidLowPnr4 = "47072900002";
	String dNummerValidLowPnr5 = "62015900003";
	String dNummerValidLowPnr6 = "43092700004";
	String dNummerValidLowPnr7 = "50036000004";
	String dNummerValidLowPnr8 = "46126000006";
	String dNummerValidLowPnr9 = "71036100006";
	String dNummerValidLowPnr10 = "71031300007";
	String dNummerValidLowPnr11 = "54065600008";
	String dNummerValidLowPnr12 = "60041400008";
	String dNummerValidLowPnr13 = "56056000009";
	
	
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
		assertFalse("Special circumstance test failed for Fnr " + specialFnr03Jan73, Pid.isValidPid(specialFnr03Jan73, false) );
		
		assertFalse("Special circumstance test failed for Fnr " + specialFnr03Jan76, Pid.isValidPid(specialFnr03Jan76, false) );
		assertFalse("Special circumstance test failed for Fnr "+ specialFnr08Jun57, Pid.isValidPid(specialFnr08Jun57, false));
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
		
		assertTrue("Special circumstance test failed for Fnr " + specialFnr03Jan73, Pid.isValidPid(specialFnr03Jan73, true) );
		assertTrue("Special circumstance test failed for Fnr " + specialFnr03Jan76, Pid.isValidPid(specialFnr03Jan76, true) );
	}

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
			// should happen
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
			// should happen
		}

		try {
			new Pid(specialFnr2, true);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
		}

		try {
			new Pid(specialFnr2, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
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
		}

		try {
			new Pid(specialFnr30Feb, true);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
		}

		try {
			new Pid(specialFnr30Feb, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
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
			// should happen
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
			// should happen
		}

		try {
			new Pid(specialFnrZero, true);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
		}

		try {
			new Pid(specialFnrZero, false);
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
		}
	}

	@Test
	public void fnrCanBeValidated() {
		assertTrue("Could not create Pid using Fnr", Pid.isValidPid(normalFnr));
	}

	@Test
	public void bostNummerCanBeValidated() {
		assertTrue("Could not create fnr using a Bostnummer with birth month > 9", Pid.isValidPid(monthAbove9BostNr));
		assertTrue("Could not create fnr using a Bostnummer with birth month < 10", Pid.isValidPid(monthBelow10BostNrWs));
	}

	/**
	 * Test d-nummer
	 */
	@Test
	public void dnummerCanBeValidated() {
		// Check valid dnummer
		assertTrue("Could not create fnr using a valid Dnummer with day > 40:" + dnummerValid0, Pid.isValidPid(dnummerValid0));

		// Verify method to check dnummer
		try {
			assertTrue("Failed check of valid dnummer:" + dnummerValid1, new Pid(dnummerValid1).isDnummer());
		} catch (PidValidationException pe) {
			// should not happen
			fail("PidValidationException should not be thrown:" + pe);
		}

		// Check normal fnr
		try {
			assertFalse("Failed check of dnummer with normal fnr:" + normalFnr, new Pid(normalFnr).isDnummer());
		} catch (PidValidationException pe) {
			// should not happen
			fail("PidValidationException should not be thrown:" + pe);
		}

		// Check special dnummer
		try {
			assertFalse("Failed check of special dnummer:" + dnummerInvalidSpecial0, new Pid(dnummerInvalidSpecial0, true)
					.isDnummer());
		} catch (PidValidationException pe) {
			// should not happen
			fail("PidValidationException should not be thrown:" + pe);
		}

	}

	@Test
	public void fodselsdatoIsExtractedCorrectFromValidPid() {
		// 26. _June_ 1976
		Date expectedDate = new GregorianCalendar(1976, 5, 26).getTime();
		// 1. _December_ 1972
		Date expectedDateForAbove = new GregorianCalendar(1972, 11, 1).getTime();
		// 4. _May_ 1901
		Date expectedDateForBelow = new GregorianCalendar(1901, 4, 4).getTime();

		// TODO what about bost numbers and date?
		assertThat(new Pid(normalFnr).getFodselsdato(), is(equalTo(expectedDate)));
		assertThat(new Pid(monthAbove9BostNr).getFodselsdato(), is(equalTo(expectedDateForAbove)));
		assertThat(new Pid(monthBelow10BostNr).getFodselsdato(), is(equalTo(expectedDateForBelow)));
	}

	@Test
	public void fodselsdatoThrowsExceptionFromInvalidPid() {

		try {
			new Pid(specialFnr0, true).getFodselsdato();
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
		}

		try {
			new Pid(specialFnr1, true).getFodselsdato();
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
		}

		try {
			new Pid(specialFnr29Feb, true).getFodselsdato();
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
		}

		try {
			new Pid(specialFnr61May, true).getFodselsdato();
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
		}

		try {
			new Pid(specialFnr21XXX, true).getFodselsdato();
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
		}
	}

	private void checkWhitespaceCompliance(String pid, boolean isCompliant) {
		assertEquals("Whitespace test failed with Pid: " + pid, isCompliant, Pid.isWhitespaceCompliant(pid));
	}

	@Test
	public void calculateAge() {

		/* DOB: 1976.06.26 */
		Pid pid = new Pid("12345678901");

		/* 31 years */
		GregorianCalendar greg = new GregorianCalendar();
		greg.set(2007, 5, 27);
		int age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(31, age);

		/* 31 years */
		greg = new GregorianCalendar();
		greg.set(2007, 5, 26);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(31, age);

		/* 30 years */
		greg = new GregorianCalendar();
		greg.set(2007, 5, 25);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(30, age);

		/* 30 years */
		greg.set(2007, 4, 27);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(30, age);

		/* 30 years */
		greg = new GregorianCalendar();
		greg.set(2007, 4, 26);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(30, age);

		/* 30 years */
		greg = new GregorianCalendar();
		greg.set(2007, 4, 25);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(30, age);

		/* 10 years */
		greg = new GregorianCalendar();
		greg.set(1987, 5, 25);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(10, age);

		/* -10 years == 0 */
		greg = new GregorianCalendar();
		greg.set(1967, 5, 25);
		age = Pid.calculateAge(pid, greg.getTime());
		assertEquals(0, age);
	}

	@Test
	public void calculateAgeThrowsExceptionOnInvalidPid() {
		Pid pidSpecial = new Pid("12345678901", true);

		/* 31 years */
		GregorianCalendar greg = new GregorianCalendar();
		greg.set(2006, 6, 26);

		try {
			Pid.calculateAge(pidSpecial, greg.getTime());
			fail("PidValidationException should be thrown");
		} catch (PidValidationException pe) {
			// should happen
		}
	}

	@Test
	public void toStringReturnsFnr() {
		Pid pid = new Pid(normalFnr, false);
		System.out.println(pid.toString());
		assertThat(pid.toString(), is(equalTo(normalFnr)));

	}

	/**
	 * Verify that PIDs that are of D-number type validate using strict validation
	 * even though they end with 00000N, where N is in [0,9]. See CR 97238.
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
	 * Verify that PIDs that are of D-number type validate using relaxed validation
	 * even though they end with 00000N, where N is in [0,9]. See CR 97238.
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
