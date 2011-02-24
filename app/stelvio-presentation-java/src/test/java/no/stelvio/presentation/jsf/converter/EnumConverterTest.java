package no.stelvio.presentation.jsf.converter;

import static org.junit.Assert.assertEquals;

import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import no.stelvio.presentation.jsf.mock.SpringDefinition;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the EnumConverter class.
 * 
 * @author persond3cb2ee15f42
 * @version $Id$
 */
public class EnumConverterTest {

	private HtmlSelectOneMenu selectOneMenu;

	private EnumConverter enumConverter;

	/**
	 * Set up before class.
	 * 
	 * @throws Exception exception
	 */
	@BeforeClass
	public static void setUpOnce() throws Exception {
		SpringDefinition.getContext();
	}

	/**
	 * Set up before test.
	 * 
	 * @throws Exception exception
	 */
	@Before
	public void setUp() throws Exception {
		selectOneMenu = new HtmlSelectOneMenu();

		this.enumConverter = new EnumConverter();

		FacesContext facesContext = FacesContext.getCurrentInstance();

		facesContext.getViewRoot().getChildren().add(selectOneMenu);

		ValueBinding bbb = facesContext.getApplication().createValueBinding("dontknowwhatthisisfor");
		bbb.setValue(facesContext, JustSomeEnum.ONE);

		selectOneMenu.setValueBinding("value", bbb);
	}

	/**
	 * Test get string from enum.
	 * 
	 */
	@Test
	public void testGetStringFromEnum() {
		String s = enumConverter.getAsString(FacesContext.getCurrentInstance(), null, JustSomeEnum.ONE);
		assertEquals("ONE", s);
	}

	/**
	 * Test get enum from string.
	 */
	@Test
	public void testGetEnumFromString() {
		Enum s = (Enum) enumConverter.getAsObject(FacesContext.getCurrentInstance(), this.selectOneMenu, "ONE");
		assertEquals(JustSomeEnum.ONE, s);
	}

	/**
	 * An enum for test.
	 */
	enum JustSomeEnum {
		ONE, TWO, THREE;
	}
}
