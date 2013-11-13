/**
 * 
 */
package no.stelvio.presentation.jsf.validator;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.binding.context.MessageContextUtil;
import no.stelvio.presentation.jsf.mock.SpringDefinition;
import no.stelvio.presentation.jsf.mock.UIComponentMock;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * BokstavValidatorTest tests BokstavValidator.
 * 
 * @author person10cbe95feeae (Bouvet)
 * @version $Id$
 */
public class TextValidatorTest {

	private static UIComponent component = new UIComponentMock();

	/**
	 * Setup before class.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		SpringDefinition.getContext();
		component.setId("tekstfelt");
	}

	/**
	 * Test reg exp.
	 */
	@Test
	public void testRegExp() {
		Pattern pattern = Pattern.compile("[\\sa-zÊ¯ÂA-Z∆ÿ≈]+");
		Matcher matcher = pattern.matcher("Simple Test String Ê ÿ Â");
		assertTrue(matcher.matches());
	}

	/**
	 * testWithValidInput.
	 */
	@Test
	public void testWithValidInput() {
		validateTestThatFails("Test String Ê≈", null);
	}

	/**
	 * testValidateStringValue.
	 */
	@Test
	public void testValidateStringValue() {
		validateTestThatFails("ABCabc∆ÿ≈Ê¯Â", null);
	}

	/**
	 * testValidateBogusInput.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateBogusInput() throws ParseException {
		validateTestThatFails("Bogus!123", TextValidator.TYPE_MESSAGE_ID);
	}

	/**
	 * testValidateInvalidObjectType.
	 */
	@Test
	public void testValidateInvalidObjectType() {
		validateTestThatFails(new ArrayList<Object>(), TextValidator.TYPE_MESSAGE_ID);
	}

	/**
	 * validate test that fails.
	 * 
	 * @param inputField
	 *            input
	 * @param errorMessage
	 *            errormessage
	 */
	private void validateTestThatFails(Object inputField, String errorMessage) {
		TextValidator validator = new TextValidator();
		try {
			validator.validate(null, component, inputField);
			if (StringUtils.isNotBlank(errorMessage)) {
				Assert.fail("Test failed - should have thrown an exception");
			}
		} catch (ValidatorException e) {
			if (errorMessage == null) {
				throw e; // Rethrow
			}
			Object[] args = { component.getId() };
			Assert.assertEquals(MessageContextUtil.getMessage(errorMessage, args), e.getFacesMessage().getSummary());
		}
	}
}
