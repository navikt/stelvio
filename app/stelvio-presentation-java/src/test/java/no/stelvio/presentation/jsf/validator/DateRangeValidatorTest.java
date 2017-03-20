package no.stelvio.presentation.jsf.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import no.stelvio.presentation.binding.context.MessageContextUtil;
import no.stelvio.presentation.jsf.mock.SpringDefinition;
import no.stelvio.presentation.jsf.mock.UIComponentMock;

/**
 * DateRangeValidatorTest tests DateRangeValidator.
 * 
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */

public class DateRangeValidatorTest {

	private static final String DATE_FORMAT = "dd.MM.yyyy";

	private static UIComponent component = new UIComponentMock();

	/**
	 * Setup before class.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		SpringDefinition.getContext();
		component.setId("datofelt");
	}

	/**
	 * testValidateStringValue.
	 */
	@Test
	public void testValidateStringValue() {
		DateRangeValidator validator = new DateRangeValidator();
		try {
			validator.validate(null, component, "12.01.2007");
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(DateRangeValidator.TYPE_MESSAGE_ID, null), e.getFacesMessage()
					.getSummary());
		}
	}

	/**
	 * testValidateInvalidObjectType.
	 */
	@Test
	public void testValidateInvalidObjectType() {
		DateRangeValidator validator = new DateRangeValidator();
		try {
			validator.validate(null, component, 1L);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(DateRangeValidator.TYPE_MESSAGE_ID, null), e.getFacesMessage()
					.getSummary());
		}
	}

	/**
	 * testValidateNotInRange.
	 * 
	 * @throws ParseException
	 *             exception
	 */
	@Test
	public void testValidateNotInRange() throws ParseException {
		DateRangeValidator validator = new DateRangeValidator();
		Date min = new SimpleDateFormat(DATE_FORMAT).parse("11.01.2000");
		Date max = new SimpleDateFormat(DATE_FORMAT).parse("13.01.2000");
		validator.setMinimum(min);
		validator.setMaximum(max);
		try {
			validator.validate(null, component, new SimpleDateFormat(DATE_FORMAT).parse("14.01.2000"));
			Assert.fail();
		} catch (ValidatorException e) {
			Object[] args = { component.getId(), new SimpleDateFormat(DATE_FORMAT).format(min),
					new SimpleDateFormat(DATE_FORMAT).format(max) };
			Assert.assertEquals(MessageContextUtil.getMessage(DateRangeValidator.NOT_IN_RANGE_MESSAGE_ID, args), e
					.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateMaximumFails.
	 * 
	 * @throws ParseException
	 *             exception
	 */
	@Test
	public void testValidateMaximumFails() throws ParseException {
		DateRangeValidator validator = new DateRangeValidator();
		Date max = new SimpleDateFormat(DATE_FORMAT).parse("13.01.2000");
		validator.setMaximum(max);
		try {
			validator.validate(null, component, new SimpleDateFormat(DATE_FORMAT).parse("14.01.2000"));
			Assert.fail();
		} catch (ValidatorException e) {
			Object[] args = { component.getId(), new SimpleDateFormat(DATE_FORMAT).format(max) };
			Assert.assertEquals(MessageContextUtil.getMessage(DateRangeValidator.MAXIMUM_MESSAGE_ID, args), e.getFacesMessage()
					.getSummary());
		}
	}

	/**
	 * testValidateMinimumFails.
	 * 
	 * @throws ParseException
	 *             exception
	 */
	@Test
	public void testValidateMinimumFails() throws ParseException {
		DateRangeValidator validator = new DateRangeValidator();
		Date min = new SimpleDateFormat(DATE_FORMAT).parse("11.01.2000");
		validator.setMinimum(min);
		try {
			validator.validate(null, component, new SimpleDateFormat(DATE_FORMAT).parse("10.01.2000"));
			Assert.fail();
		} catch (ValidatorException e) {
			Object[] args = { component.getId(), new SimpleDateFormat(DATE_FORMAT).format(min) };
			Assert.assertEquals(MessageContextUtil.getMessage(DateRangeValidator.MINIMUM_MESSAGE_ID, args), e.getFacesMessage()
					.getSummary());
		}
	}

	/**
	 * testValidateMinimumBogusInput.
	 * 
	 * @throws ParseException
	 *             exception
	 */
	@Test
	public void testValidateMinimumBogusInput() throws ParseException {
		DateRangeValidator validator = new DateRangeValidator();
		Date min = new SimpleDateFormat(DATE_FORMAT).parse("11.01.2000");
		validator.setMinimum(min);
		try {
			validator.validate(null, component, new SimpleDateFormat(DATE_FORMAT).parse("12.01.200"));
			Assert.fail();
		} catch (ValidatorException e) {
			Object[] args = { component.getId(), new SimpleDateFormat(DATE_FORMAT).format(min) };
			Assert.assertEquals(MessageContextUtil.getMessage(DateRangeValidator.MINIMUM_MESSAGE_ID, args), e.getFacesMessage()
					.getSummary());
		}
	}

	/**
	 * testValidateValidDate.
	 * 
	 * @throws ParseException
	 *             exception
	 */
	@Test
	public void testValidateValidDate() throws ParseException {
		DateRangeValidator validator = new DateRangeValidator();
		validator.setMinimum(new SimpleDateFormat(DATE_FORMAT).parse("11.01.2000"));
		validator.setMaximum(new SimpleDateFormat(DATE_FORMAT).parse("13.01.2000"));
		try {
			validator.validate(null, component, new SimpleDateFormat(DATE_FORMAT).parse("12.01.2000"));
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

}
