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
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class NorskKontonummerValidatorTest {

	/** INVALID_MESSAGE_ID. */
	public static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.NorskKontonummer.INVALID";

	/** VALID_KNR. */
	public static final String VALID_KNR = "12345678903";

	/** VALID_KNR2. */
	public static final String VALID_KNR2 = "97104818270";

	/** INVALID_KNR. */
	public static final String INVALID_KNR = "12345678904";

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
		NorskKontonummerValidator validator = new NorskKontonummerValidator();
		try {
			validator.validate(null, null, INVALID_KNR);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_MESSAGE_ID, new String[] { INVALID_KNR }), e
					.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFailTooManyFields.
	 */
	@Test
	public void testValidateFailTooManyFields() {
		NorskKontonummerValidator validator = new NorskKontonummerValidator();
		try {
			validator.validate(null, null, "123456789031");
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_MESSAGE_ID, new String[] { "123456789031" }), e
					.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFailTooFewFields.
	 */
	@Test
	public void testValidateFailTooFewFields() {
		NorskKontonummerValidator validator = new NorskKontonummerValidator();
		try {
			validator.validate(null, null, "1234567890");
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_MESSAGE_ID, new String[] { "1234567890" }), e
					.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFailLetters.
	 */
	@Test
	public void testValidateFailLetters() {
		NorskKontonummerValidator validator = new NorskKontonummerValidator();
		try {
			validator.validate(null, null, "A234567890");
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_MESSAGE_ID, new String[] { "A234567890" }), e
					.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccess.
	 */
	@Test
	public void testValidateSuccess() {
		NorskKontonummerValidator validator = new NorskKontonummerValidator();
		try {
			validator.validate(null, null, VALID_KNR);
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccess2.
	 */
	@Test
	public void testValidateSuccess2() {
		NorskKontonummerValidator validator = new NorskKontonummerValidator();
		try {
			validator.validate(null, null, VALID_KNR2);
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccessWithSpaces.
	 */
	@Test
	public void testValidateSuccessWithSpaces() {
		NorskKontonummerValidator validator = new NorskKontonummerValidator();
		try {
			validator.validate(null, null, "1234 56 78903");
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccessWithSeparators.
	 */
	@Test
	public void testValidateSuccessWithSeparators() {
		NorskKontonummerValidator validator = new NorskKontonummerValidator();
		try {
			validator.validate(null, null, "1234.56.78903");
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

}
