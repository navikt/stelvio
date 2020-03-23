package no.stelvio.presentation.jsf.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import no.stelvio.presentation.jsf.mock.FacesContextMock;
import no.stelvio.presentation.jsf.mock.SpringDefinition;
import no.stelvio.presentation.jsf.mock.UIComponentMock;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * CustomLongRangeValidatorTest.
 * 
 *
 */
public class CustomLongRangeValidatorTest {

	private static UIComponent component = new UIComponentMock();

	private FacesContext facesContext = new FacesContextMock();

	/**
	 * Setup before class.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		SpringDefinition.getContext();
		component.setId("postCode");
	}

	/**
	 * testCustomLongRangeValdatorSuccess.
	 */
	@Test
	public void testCustomLongRangeValdatorSuccess() {
		CustomLongRangeValidator customLongRangeValidator = new CustomLongRangeValidator();
		customLongRangeValidator.setMaximum(10001);

		Long value = new Long(10000);

		try {
			customLongRangeValidator.validate(facesContext, component, value);
		} catch (ValidatorException e) {
			Assert.fail();
			e.printStackTrace();
		}

		Assert.assertTrue(true);
	}

}
