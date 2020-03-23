package no.stelvio.presentation.jsf.validator;

import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.binding.context.MessageContextUtil;
import no.stelvio.presentation.jsf.mock.SpringDefinition;
import no.stelvio.presentation.jsf.mock.UIComponentMock;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * BbanValidatorTest tests BbanValidator.
 * 
 * @version $Id$
 */
public class BbanValidatorTest {

	private static UIComponent component = new UIComponentMock();

	/** VALID_KNR. */
	public static final String VALID_KNR = "12345678903";

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
		BbanValidator validator = new BbanValidator();
		try {
			validator.validate(null, component, INVALID_KNR);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(BbanValidator.INVALID_MESSAGE_ID, new Object[] { INVALID_KNR }),
					e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFailTooManyFields.
	 */
	@Test
	public void testValidateFailTooManyFields() {
		BbanValidator validator = new BbanValidator();
		try {
			validator.validate(null, component, "123456789031");
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil
					.getMessage(BbanValidator.INVALID_MESSAGE_ID, new Object[] { "123456789031" }), e.getFacesMessage()
					.getSummary());
		}
	}

	/**
	 * testValidateFailTooFewFields.
	 */
	@Test
	public void testValidateFailTooFewFields() {
		BbanValidator validator = new BbanValidator();
		try {
			validator.validate(null, component, "1234567890");
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(BbanValidator.INVALID_MESSAGE_ID, new Object[] { "1234567890" }),
					e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFailLetters.
	 */
	@Test
	public void testValidateFailLetters() {
		BbanValidator validator = new BbanValidator();
		try {
			validator.validate(null, component, "A234567890");
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(BbanValidator.INVALID_MESSAGE_ID, new Object[] { "A234567890" }),
					e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccess.
	 */
	@Test
	public void testValidateSuccess() {
		BbanValidator validator = new BbanValidator();
		try {
			validator.validate(null, component, VALID_KNR);
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
		BbanValidator validator = new BbanValidator();
		try {
			validator.validate(null, component, "1234 56 78903");
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
		BbanValidator validator = new BbanValidator();
		try {
			validator.validate(null, component, "1234.56.78903");
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

}
