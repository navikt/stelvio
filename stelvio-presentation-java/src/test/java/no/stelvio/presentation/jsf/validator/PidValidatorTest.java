/**
 * 
 */
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
 * PidValidatorTest tests PidValidator.
 * 
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class PidValidatorTest {

	private static final String INVALID_MESSAGE_ID = "no.stelvio.presentation.validator.Pid.INVALID";

	private static final String COMPONENT_ID = "Fodselsnummer";

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
		PidValidator validator = new PidValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);

		try {
			validator.validate(null, component, "12345678901");
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_MESSAGE_ID, new String[] { COMPONENT_ID }), e
					.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFailEmptyString.
	 */
	@Test
	public void testValidateFailEmptyString() {
		PidValidator validator = new PidValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);

		try {
			validator.validate(null, component, "");
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_MESSAGE_ID, new String[] { COMPONENT_ID }), e
					.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccessNull.
	 */
	@Test
	public void testValidateSuccessNull() {
		PidValidator validator = new PidValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		try {
			validator.validate(null, component, null);
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
		PidValidator validator = new PidValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		try {
			validator.validate(null, component, "03102942719");
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

}
