/**
 * 
 */
package no.stelvio.presentation.jsf.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.binding.context.MessageContextUtil;
import no.stelvio.presentation.jsf.mock.SpringDefinition;
import no.stelvio.presentation.jsf.mock.UIComponentMock;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * LastInMonthValidatorTest tests LastInMonthValidator.
 * 
 * @version $Id$
 */
public class LastInMonthValidatorTest {

	private static final String COMPONENT_ID = "Datofelt";

	private static final String DATE_FORMAT = "dd.MM.yyyy";

	/**
	 * Setup before class.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		SpringDefinition.getContext();
	}

	/**
	 * testValidateFail.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateFail() throws ParseException {
		LastInMonthValidator validator = new LastInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		Date date = new SimpleDateFormat(DATE_FORMAT).parse("30.12.2005");

		try {
			validator.validate(null, component, date);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(LastInMonthValidator.INVALID_MESSAGE_ID,
					new String[] { COMPONENT_ID }), e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFailShortMonth.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateFailShortMonth() throws ParseException {
		LastInMonthValidator validator = new LastInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		Date date = new SimpleDateFormat(DATE_FORMAT).parse("29.12.2005");

		try {
			validator.validate(null, component, date);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(LastInMonthValidator.INVALID_MESSAGE_ID,
					new String[] { COMPONENT_ID }), e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFailSkuddar.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateFailSkuddar() throws ParseException {
		LastInMonthValidator validator = new LastInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		Date date = new SimpleDateFormat(DATE_FORMAT).parse("28.02.2008");

		try {
			validator.validate(null, component, date);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(LastInMonthValidator.INVALID_MESSAGE_ID,
					new String[] { COMPONENT_ID }), e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFailEmptyString.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateFailEmptyString() throws ParseException {
		LastInMonthValidator validator = new LastInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		String date = "";

		try {
			validator.validate(null, component, date);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(LastInMonthValidator.TYPE_MESSAGE_ID,
					new String[] { COMPONENT_ID }), e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccessNullInput.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateSuccessNullInput() throws ParseException {
		LastInMonthValidator validator = new LastInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		Date date = null;
		try {
			validator.validate(null, component, date);
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccess.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateSuccess() throws ParseException {
		LastInMonthValidator validator = new LastInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		Date date = new SimpleDateFormat(DATE_FORMAT).parse("31.12.2005");
		try {
			validator.validate(null, component, date);
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccessSkuddar.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateSuccessSkuddar() throws ParseException {
		LastInMonthValidator validator = new LastInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		Date date = new SimpleDateFormat(DATE_FORMAT).parse("29.02.2008");
		try {
			validator.validate(null, component, date);
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccessShortMonth.
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testValidateSuccessShortMonth() throws ParseException {
		LastInMonthValidator validator = new LastInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		Date date = new SimpleDateFormat(DATE_FORMAT).parse("30.04.2008");
		try {
			validator.validate(null, component, date);
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

}
