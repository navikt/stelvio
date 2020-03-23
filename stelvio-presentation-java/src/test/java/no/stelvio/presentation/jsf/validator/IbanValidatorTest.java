/**
 * 
 */
package no.stelvio.presentation.jsf.validator;

import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.binding.context.MessageContextUtil;
import no.stelvio.presentation.jsf.mock.SpringDefinition;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * IbanValidatorTest tests IbanValidator.
 * 
 * @version $Id$
 */
public class IbanValidatorTest {

	/** INVALID_MESSAGE_ID. */
	public static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.Iban.INVALID";

	/** INVALID_IBAN. */
	public static final String INVALID_IBAN = "12345678901";

	/** VALID_IBAN. */
	public static final String VALID_IBAN = "NO2097132379169";

	/**
	 * Setup before class.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		SpringDefinition.getContext();
	}

	/**
	 * testValidateFail.
	 */
	@Test
	public void testValidateFail() {
		IbanValidator validator = new IbanValidator();
		try {
			validator.validate(null, null, INVALID_IBAN);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_MESSAGE_ID, new String[] { INVALID_IBAN }), e
					.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccessWhiteSpaces.
	 */
	@Test
	public void testValidateSuccessWhiteSpaces() {
		IbanValidator validator = new IbanValidator();
		try {
			validator.validate(null, null, "NO 2097132379169");
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccessSeparators.
	 */
	@Test
	public void testValidateSuccessSeparators() {
		IbanValidator validator = new IbanValidator();
		try {
			validator.validate(null, null, "NO.2097132379169");
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccess.
	 */
	@Test
	public void testValidateSuccess() {
		IbanValidator validator = new IbanValidator();
		try {
			validator.validate(null, null, VALID_IBAN);
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

}
