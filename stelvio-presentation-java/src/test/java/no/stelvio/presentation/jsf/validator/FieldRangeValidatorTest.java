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
 * FieldRangeValidatorTest.
 * 
 * @version $Id$
 * 
 */
public class FieldRangeValidatorTest {
	/** MINIMUM. */
	public static final String MINIMUM_MESSAGE_ID = "no.stelvio.presentation.validator.FieldRange.MINIMUM";

	/** MAXIMUM. */
	public static final String MAXIMUM_MESSAGE_ID = "no.stelvio.presentation.validator.FieldRange.MAXIMUM";

	/** NOT IN RANGE. */
	public static final String NOT_IN_RANGE_MESSAGE_ID = "no.stelvio.presentation.validator.FieldRange.NOT_IN_RANGE_MESSAGE_ID";

	/** INVALID LENGTH. */
	public static final String INVALID_MAXM_MIN_LENGTH_MESSAGE_ID = 
			"no.stelvio.presentation.validator.FieldRange.INVALID_MAXM_MIN_LENGTH_MESSAGE_ID";

	private static UIComponent component = new UIComponentMock();

	/**
	 * Setup before class.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		SpringDefinition.getContext();
		component.setId("TextField");
	}

	/**
	 * testValidateFieldFailValue.
	 */
	@Test
	public void testValidateFieldFailValue() {
		FieldRangeValidator validator = new FieldRangeValidator();
		int min = 5;
		int max = 8;

		validator.setMinimum(min);
		validator.setMaximum(max);
		try {
			validator.validate(null, component, "Test");
			Assert.fail();
		} catch (ValidatorException e) {
			Object[] args = { component.getId(), min, max };

			Assert.assertEquals(MessageContextUtil.getMessage(NOT_IN_RANGE_MESSAGE_ID, args), e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFieldValue.
	 */
	@Test
	public void testValidateFieldValue() {
		FieldRangeValidator validator = new FieldRangeValidator();
		int min = 5;
		int max = 8;

		validator.setMinimum(min);
		validator.setMaximum(max);
		try {
			validator.validate(null, component, "Tester");
		} catch (ValidatorException e) {
			Assert.fail();
		}
	}

	/**
	 * testValidateMaxMinFailValue.
	 */
	@Test
	public void testValidateMaxMinFailValue() {
		FieldRangeValidator validator = new FieldRangeValidator();
		int min = 5;
		int max = 5;

		validator.setMinimum(min);
		validator.setMaximum(max);
		try {
			validator.validate(null, component, "Tester");
			Assert.fail();
		} catch (ValidatorException e) {
			Object[] args = { min, max };
			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_MAXM_MIN_LENGTH_MESSAGE_ID, args), e.getFacesMessage()
					.getSummary());
		}
	}

	/**
	 * testValidateMinimumFails.
	 */
	@Test
	public void testValidateMinimumFails() {
		FieldRangeValidator validator = new FieldRangeValidator();
		int min = -1;
		validator.setMinimum(min);
		try {
			validator.validate(null, component, "Test");
			Assert.fail();
		} catch (ValidatorException e) {
			Object[] args = { component.getId(), min };
			Assert.assertEquals(MessageContextUtil.getMessage(MINIMUM_MESSAGE_ID, args), e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateMaximumFails.
	 */
	@Test
	public void testValidateMaximumFails() {
		FieldRangeValidator validator = new FieldRangeValidator();
		int max = 0;
		validator.setMaximum(max);
		try {
			validator.validate(null, component, "Test");
			Assert.fail();
		} catch (ValidatorException e) {
			Object[] args = { component.getId(), max };
			Assert.assertEquals(MessageContextUtil.getMessage(MAXIMUM_MESSAGE_ID, args), e.getFacesMessage().getSummary());
		}
	}
}
