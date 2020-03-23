package no.stelvio.common.validation;

import junit.framework.TestCase;

/**
 * Test class for IbanValidator.
 * 
 */
public class TestIbanValidator extends TestCase {

	/**
	 * Checks that valid Iban passes the validation check. Paper iban is an Iban with added spaces to be easier to read for
	 * humans. Electronic iban is an iban without spaces, optimized for computers.
	 */
	public void testValidIbans() {
		String validFrenchElectronicIban = "FR1420041010050500013M02606";
		String validFrenchPaperIban = "FR14 2004 1010 0505 0001 3M02 606";
		String validBelgianElectronicIban = "BE62510007547061";
		String validBelgianPaperIban = "BE62 5100 0754 7061";

		assertTrue("Validation of belgian electronic iban failed!", IbanValidator.isValidIban(validBelgianElectronicIban));
		assertTrue("Validation of belgian paper iban failed!", IbanValidator.isValidIban(validBelgianPaperIban));

		assertTrue("Validation of french electronic iban failed!", IbanValidator.isValidIban(validFrenchElectronicIban));
		assertTrue("Validation of french paper iban failed!", IbanValidator.isValidIban(validFrenchPaperIban));
	}

}
