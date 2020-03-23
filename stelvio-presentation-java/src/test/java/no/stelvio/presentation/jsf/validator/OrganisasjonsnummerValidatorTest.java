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
 * OrganisasjonsnummerValidatorTest.
 * 
 * @version $Id$
 * 
 */
public class OrganisasjonsnummerValidatorTest {
	/** INVALID_MESSAGE_ID. */
	public static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.Organisasjonsnummer.INVALID";
	/** LENGTH_MESSAGE_ID. */
	public static final String LENGTH_MESSAGE_ID = "no.stelvio.presentation.validator.Organisasjonsnummer.LENGTH";
	/** INVALID_ORGNR_LENTH. */
	public static final String INVALID_ORGNR_LENTH = "90567801";
	/** INVALID_ORGNR_TYPE. */
	public static final String INVALID_ORGNR_TYPE = "91216790P";
	/** VALID_ORGNR. */
	public static final String VALID_ORGNR = "905678011";

	private static UIComponent component = new UIComponentMock();

	/**
	 * Setup before class.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		SpringDefinition.getContext();
	}

	/**
	 * testValidateStringValue.
	 */
	@Test
	public void testValidateStringValue() {
		OrganisasjonsnummerValidator validator = new OrganisasjonsnummerValidator();
		try {
			validator.validate(null, component, INVALID_ORGNR_TYPE);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_MESSAGE_ID, new String[] { INVALID_ORGNR_TYPE }), e
					.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateLengthValue.
	 */
	@Test
	public void testValidateLengthValue() {
		OrganisasjonsnummerValidator validator = new OrganisasjonsnummerValidator();
		try {
			validator.validate(null, component, INVALID_ORGNR_LENTH);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_MESSAGE_ID, new String[] { INVALID_ORGNR_LENTH }), e
					.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateValue.
	 */
	@Test
	public void testValidateValue() {
		OrganisasjonsnummerValidator validator = new OrganisasjonsnummerValidator();
		try {
			validator.validate(null, component, VALID_ORGNR);
		} catch (ValidatorException e) {
			Assert.fail();
		}
	}
}
