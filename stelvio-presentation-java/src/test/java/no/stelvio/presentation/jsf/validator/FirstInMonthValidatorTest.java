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
 * FirstInMonthValidatorTest tests FirstInMonthValidator.
 * 
 * @version $Id$
 */
public class FirstInMonthValidatorTest {

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
	 * @throws ParseException
	 *             exception
	 */
	@Test
	public void testValidateFail() throws ParseException {
		FirstInMonthValidator validator = new FirstInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		Date date = new SimpleDateFormat(DATE_FORMAT).parse("02.12.2005");

		try {
			validator.validate(null, component, date);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(FirstInMonthValidator.INVALID_MESSAGE_ID,
					new String[] { COMPONENT_ID }), e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateFailEmptyString.
	 * 
	 * @throws ParseException
	 *             exception
	 */
	@Test
	public void testValidateFailEmptyString() {
		FirstInMonthValidator validator = new FirstInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		String date = "";

		try {
			validator.validate(null, component, date);
			Assert.fail();
		} catch (ValidatorException e) {
			Assert.assertEquals(MessageContextUtil.getMessage(FirstInMonthValidator.TYPE_MESSAGE_ID,
					new String[] { COMPONENT_ID }), e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateSuccessNullInput.
	 *
     */
	@Test
	public void testValidateSuccessNullInput() {
		FirstInMonthValidator validator = new FirstInMonthValidator();
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
	 * @throws ParseException
	 *             exception
	 */
	@Test
	public void testValidateSuccess() throws ParseException {
		FirstInMonthValidator validator = new FirstInMonthValidator();
		UIComponent component = new UIComponentMock();
		component.setId(COMPONENT_ID);
		Date date = new SimpleDateFormat(DATE_FORMAT).parse("01.12.2005");
		try {
			validator.validate(null, component, date);
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}

}
