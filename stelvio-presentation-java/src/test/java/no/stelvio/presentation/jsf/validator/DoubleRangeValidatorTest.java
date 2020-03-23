/**
 * 
 */
package no.stelvio.presentation.jsf.validator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
 * DoubleRangeValidatorTest tests DoubleRangeValidator.
 * 
 * @version $Id$
 */
public class DoubleRangeValidatorTest {

	private static UIComponent component = new UIComponentMock();

	/**
	 * Setup before class.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		SpringDefinition.getContext();
		component.setId("nummerfelt");
	}

	/**
	 * testWithValidInput.
	 */
	@Test
	public void testWithValidInput() {
		validateTestThatFails(1D, 3D, null, 2D);
	}

	/**
	 * testValidateStringValue.
	 */
	@Test
	public void testValidateStringValue() {
		validateTestThatFails(null, null, null, "1");
	}

	/**
	 * testValidateBogusInput.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateBogusInput() throws ParseException {
		validateTestThatFails(null, null, DoubleRangeValidator.TYPE_MESSAGE_ID, "Bogus");
	}

	/**
	 * testValidateInvalidObjectType.
	 */
	@Test
	public void testValidateInvalidObjectType() {
		validateTestThatFails(null, null, DoubleRangeValidator.TYPE_MESSAGE_ID, new ArrayList<Object>());
	}

	/**
	 * testValidateNotInRange.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateNotInRange() throws ParseException {
		validateTestThatFails(1D, 2D, DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID, 3D);
	}

	/**
	 * testValidateMaximumFails.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateMaximumFails() throws ParseException {
		validateTestThatFails(null, 1D, DoubleRangeValidator.MAXIMUM_MESSAGE_ID, 2D);
	}

	/**
	 * testValidateMinimumFails.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateMinimumFails() throws ParseException {
		validateTestThatFails(2D, null, DoubleRangeValidator.MINIMUM_MESSAGE_ID, 1D);
	}

	/**
	 * Validate.
	 * 
	 * @param min
	 *            minimum
	 * @param max
	 *            maximum
	 * @param errorMessage
	 *            errormessage
	 * @param inputField
	 *            inputfield
	 */
	private void validateTestThatFails(Double min, Double max, String errorMessage, Object inputField) {
		DoubleRangeValidator validator = new DoubleRangeValidator();
		validator.setMinimum(min);
		validator.setMaximum(max);
		try {
			validator.validate(null, component, inputField);
			if (StringUtils.isNotBlank(errorMessage)) {
				Assert.fail("Test failed - should have thrown an exception");
			}
		} catch (ValidatorException e) {
			List<Object> args = new ArrayList<Object>();
			args.add(component.getId());
			if (min != null) {
				args.add(min);
			}
			if (max != null) {
				args.add(max);
			}
			Assert.assertEquals(MessageContextUtil.getMessage(errorMessage, args.toArray()), e.getFacesMessage().getSummary());
		}
	}
}