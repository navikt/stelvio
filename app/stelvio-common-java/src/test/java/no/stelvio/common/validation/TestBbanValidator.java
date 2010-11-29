package no.stelvio.common.validation;

import junit.framework.TestCase;

/**
 * Test class for BbamValidator.
 * 
 * @author??
 * 
 */
public class TestBbanValidator extends TestCase {

	/**
	 * Test norwegian bban validator.
	 */
	public void testNorwegianBbanValidation() {
		String validAccountNr = "12345678903";
		assertTrue("Validation of valid accountno failed!", BbanValidator.isValidNorwegianBban(validAccountNr));
	}

}
