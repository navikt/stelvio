package no.stelvio.presentation.jsf.validator;

import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;

import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.presentation.binding.context.MessageContextUtil;
import no.stelvio.presentation.jsf.mock.DefaultCodesTableManagerMock;
import no.stelvio.presentation.jsf.mock.SpringDefinition;
import no.stelvio.presentation.jsf.mock.UIComponentMock;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * CodesTableItemExistValidatorTest tests CodesTableItemExistValidator.
 * 
 * @author person37c6059e407e (Capgemini)
 * @version $Id$
 */
public class CodesTableItemExistValidatorTest {
	private static UIComponent component = new UIComponentMock();

	private CodesTableManager codesTableManager;

	/** Invalid format. */
	public static final String INVALID_FORMAT = "no.stelvio.presentation.validator.CodesTableItemExistValidator.INVALID_FORMAT";

	/** Invalid postcode. */
	public static final String INVALID_POSTCODE = 
			"no.stelvio.presentation.validator.CodesTableItemExistValidator.INVALID_POSTCODE";

	private static final String CTICLASS = "no.stelvio.presentation.jsf.mock.PoststedCtiMock";

	/**
	 * Setup before class.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		SpringDefinition.getContext();
		component.setId("postCode");
	}

	/**
	 * testValidateCodesTableItemValueFail.
	 */
	@Test
	public void testValidateCodesTableItemValueFail() {
		CodesTableItemExistValidator validator = new CodesTableItemExistValidator();

		/* Create codesTableManager Mock for testing */
		codesTableManager = new DefaultCodesTableManagerMock();
		validator.setCodesTableManager(codesTableManager);
		validator.setCtiClass(CTICLASS);

		String postCode = "0030";

		try {
			validator.validate(null, component, postCode);
			Assert.fail();
		} catch (ValidatorException e) {
			Object[] args = { component.getId(), postCode };

			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_POSTCODE, args), e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateCodesTableItemValueInvalidFormat.
	 */
	@Test
	public void testValidateCodesTableItemValueInvalidFormat() {
		CodesTableItemExistValidator validator = new CodesTableItemExistValidator();

		/* Create codesTableManager Mock for testing */
		codesTableManager = new DefaultCodesTableManagerMock();
		validator.setCodesTableManager(codesTableManager);
		validator.setCtiClass(CTICLASS);

		String postCode = "x0500";

		try {
			validator.validate(null, component, postCode);
			Assert.fail();
		} catch (ValidatorException e) {
			Object[] args = { component.getId(), postCode };

			Assert.assertEquals(MessageContextUtil.getMessage(INVALID_FORMAT, args), e.getFacesMessage().getSummary());
		}
	}

	/**
	 * testValidateCodesTableItemValue.
	 */
	@Test
	public void testValidateCodesTableItemValue() {
		CodesTableItemExistValidator validator = new CodesTableItemExistValidator();

		/* Create codesTableManager Mock for testing */
		codesTableManager = new DefaultCodesTableManagerMock();
		validator.setCodesTableManager(codesTableManager);
		validator.setCtiClass(CTICLASS);

		String postCode = "5000";

		try {
			validator.validate(null, component, postCode);
			Assert.assertTrue(true);
		} catch (ValidatorException e) {
			Assert.fail(e.getFacesMessage().getSummary());
		}
	}
}
