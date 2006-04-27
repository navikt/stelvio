package no.nav.web.framework.validator;

import junit.framework.TestCase;

import no.nav.web.framework.validator.FieldChecks;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionErrors;

import servletunit.HttpServletRequestSimulator;
import servletunit.ServletContextSimulator;

/**
 * Unit test for {@link FieldChecks}.
 *
 * @author person5b7fd84b3197, Accenture
 * @version $Id: FieldChecksTest.java 2648 2005-11-26 14:57:51Z ena2920 $
 **/
public class FieldChecksTest extends TestCase {

	public void testValidateFnr() {
		assertTrue("The fnr should be valid",
					FieldChecks.validateFnr("12345678901",
											new ValidatorAction(),
											new Field(),
											new ActionErrors(),
											createHttpServletRequest()));

		assertFalse("The fnr should not be valid",
					FieldChecks.validateFnr("12345678901",
											new ValidatorAction(),
											new Field(),
											new ActionErrors(),
											createHttpServletRequest()));

		assertTrue("The dnr should be valid",
					FieldChecks.validateFnr("51057300780",
											new ValidatorAction(),
											new Field(),
											new ActionErrors(),
											createHttpServletRequest()));

		assertFalse("The dnr should not be valid",
		            FieldChecks.validateFnr("51057377780",
		                                    new ValidatorAction(),
		                                    new Field(),
		                                    new ActionErrors(),
		                                    createHttpServletRequest()));

		assertFalse("The bost nr should not be valid",
					FieldChecks.validateFnr("22310500120",
											new ValidatorAction(),
											new Field(),
											new ActionErrors(),
											createHttpServletRequest()));

		assertFalse("The bost nr should not be valid",
					FieldChecks.validateFnr("22310599120",
											new ValidatorAction(),
											new Field(),
											new ActionErrors(),
											createHttpServletRequest()));
	}


	public void testValidateFnrLenght() {
		assertTrue("The fnr should be valid",
					FieldChecks.validateFnrLength("12345678901") == 0);
		assertTrue("The fnr should be valid",
					FieldChecks.validateFnrLength("") == 1);
		assertTrue("The fnr should be valid",
					FieldChecks.validateFnrLength(null) == 1);
		assertTrue("The fnr should fail length test",
					FieldChecks.validateFnrLength("170277475566") == 2);
		assertTrue("The fnr should fail length test",
					FieldChecks.validateFnrLength("1702774755") == 2);
	}
 
 	public void testValidateFnrDetails() {
		assertTrue("The fnr should be valid",
					FieldChecks.validateFnrDetails("12345678901","12345678901") == 0);
		assertTrue("The fnr should be valid",
					FieldChecks.validateFnrDetails("12345678901","12345678901") == 0);
		assertTrue("The fnr should fail mod11 test",
					FieldChecks.validateFnrDetails("12345678901","12345678901") == 1);
		assertTrue("The fnr should fail date test",
					FieldChecks.validateFnrDetails("12345678901","12345678901") == 2);
		assertTrue("The fnr should fail date test",
					FieldChecks.validateFnrDetails("12345678901","12345678901") == 2);
		assertTrue("The fnr should fail date test",
					FieldChecks.validateFnrDetails("72039910110","72039910110") == 2);

		assertTrue("The dnr should be valid",
					FieldChecks.validateFnrDetails("51057300780","12345678901") == 0);
		assertTrue("The dnr should fail mod11 test",
					FieldChecks.validateFnrDetails("51057377780","51057377780") == 1);

		assertTrue("The dnr should be valid",
					FieldChecks.validateFnrDetails("22310500120","12345678901") == 0);
		assertTrue("The dnr should fail mod11 test",
					FieldChecks.validateFnrDetails("22310599120","22310599120") == 1);
	}

	public void testMakeDnrAdjustment(){
		// testes implisitt gjennom validatFnr()
	}
	
	public void testValidateFirstDayOfMonth(){
		
	}
		
	public void testValidateLastDayOfMonth(){
	
	}
	
	/**
	 * Test at validering går bra for følgende datoformater:
	 * <ul>
	 * <li>dd.MM.yyyy</li>
	 * <li>dd.MM.yy</li>
	 * <li>ddMMyy</li>
	 * <li>dd/MM/yyyy</li>
	 * <li>dd/MM/yy</li>
	 * <li>dd\MM\yyyy</li>
	 * <li>dd\MM\yy</li>
	 * </ul> 
	 */
	public void testValidateInputDate() {
		assertTrue("Datoformat dd.MM.yyyy skulle vært gyldig",
		           validateInputDate("01.01.2005"));
		assertTrue("Datoformat dd.MM.yy skulle vært gyldig",
		           validateInputDate("01.01.05"));
		assertTrue("Datoformat ddMMyy skulle vært gyldig",
		           validateInputDate("010105"));
		assertTrue("Datoformat dd/MM/yyyy skulle vært gyldig",
		           validateInputDate("01/01/2005"));
		assertTrue("Datoformat dd\\MM\\yyyy skulle vært gyldig",
		           validateInputDate("01\\01\\2005"));
		assertTrue("Datoformat dd/MM/yy skulle vært gyldig",
		           validateInputDate("01/01/05"));
		assertTrue("Datoformat dd\\MM\\yy skulle vært gyldig",
		           validateInputDate("01\\01\\05"));
		assertFalse("Datoformat 2005.01.01 skulle IKKE vært gyldig",
				   validateInputDate("2005.01.01"));
		assertFalse("Datoformat 2005\01\01 skulle IKKE vært gyldig",
				   validateInputDate("2005\01\01"));
		assertFalse("Datoformat 2005\\01\\01 skulle IKKE vært gyldig",
				   validateInputDate("2005\\01\\01"));
		assertFalse("Datoformat 2005/01/01 skulle IKKE vært gyldig",
				   validateInputDate("2005/01/01"));
		assertFalse("Datoformat 2005//01//01 skulle IKKE vært gyldig",
				   validateInputDate("2005//01//01"));
		assertFalse("Datoformat 050101 skulle IKKE vært gyldig",
				   validateInputDate("20050101"));
	}

	/**
	 * Helper method for validating input date.
	 *
	 * @param date the date to validate as a <code>String</code>.
	 * @return true if it is a valid input date, false otherwise
	 */
	private boolean validateInputDate(final String date) {
		return FieldChecks.validateInputDate(date,
		                                      new ValidatorAction(),
		                                      new Field(),
		                                      new ActionErrors(),
		                                      createHttpServletRequest());
	}

	/**
	 * Creates a <code>HttpServletRequest</code> simulator.
	 *
	 * @return a <code>HttpServletRequest</code> simulator.
	 * @see HttpServletRequest
	 */
	private HttpServletRequestSimulator createHttpServletRequest() {
		return new HttpServletRequestSimulator(new ServletContextSimulator());
	}
}
