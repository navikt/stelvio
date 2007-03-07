package no.stelvio.common.validation;

import no.stelvio.common.validation.BbanValidator;
import junit.framework.TestCase;

public class TestBbanValidator extends TestCase {
	
	public void testNorwegianBbanValidation(){
		String validAccountNr = "12345678903";
		assertTrue("Validation of valid accountno failed!",BbanValidator.isValidNorwegianBban(validAccountNr));
	}

}
