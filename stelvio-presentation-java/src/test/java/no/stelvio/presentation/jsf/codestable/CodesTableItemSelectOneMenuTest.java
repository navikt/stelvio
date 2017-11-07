package no.stelvio.presentation.jsf.codestable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;

import no.stelvio.common.codestable.factory.CodesTableItemsFactory;
import no.stelvio.common.codestable.support.DefaultCodesTableManager;
import no.stelvio.presentation.jsf.mock.SpringDefinition;

import org.apache.myfaces.test.mock.MockFacesContext20;
import org.hamcrest.core.IsAnything;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * CodesTableItemSelectOneMenuTest.
 * 
 * @author ML
 */
public class CodesTableItemSelectOneMenuTest {

	private static Mockery context = new JUnit4Mockery();

	private static CodesTableItemSelectOneMenu selectMenu;

	private static DefaultCodesTableManager ctm;

	private static CodesTableItemsFactory codesTableItemsFactory = context.mock(CodesTableItemsFactory.class);;

	/**
	 * Set up before class.
	 * 
	 * @throws Exception
	 *             exception
	 */
	@BeforeClass
	public static void setUpOnce() throws Exception {
		SpringDefinition.getContext();
	}

	/**
	 * Set up before test.
	 */
	@Before
	public void setUp() {
		selectMenu = new CodesTableItemSelectOneMenu();
		ctm = new DefaultCodesTableManager();
		createMockedItemsFactory();
		ctm.setCodesTableItemsFactory(codesTableItemsFactory);

		selectMenu.setCodesTableManager(ctm);
	}

	/**
	 * Test selectMenuWithTestCti.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void selectMenuWithTestCti() {
		try {
			selectMenu.setCtiClass("no.stelvio.presentation.jsf.codestable.SimpleTestCti");
			selectMenu.encodeBegin(MockFacesContext20.getCurrentInstance());

			assertTrue(selectMenu.getChildCount() == 1);
			List selectItemsList = (List) ((UISelectItems) selectMenu.getChildren().get(0)).getValue();
			assertEquals(createCodesTableItems().size(), selectItemsList.size());

			
		} catch (IOException e) {
			assertFalse("No exception should have occured", false);
		}
	}

	/*
	 * IRPORT-1639:
	 * - Test that buildSelectItems() does not rebuild the list when getChildCount > 0
	 * - Regardless of the value of DisplayValue
	 */
	@Test
	public void buildSelectItems() {
		try {
			selectMenu.setCtiClass("no.stelvio.presentation.jsf.codestable.SimpleTestCti");

			// Build an initial menu with one item
			assertTrue(selectMenu.getChildCount() == 0);
			selectMenu.encodeBegin(MockFacesContext20.getCurrentInstance());
			assertTrue(selectMenu.getChildCount() == 1);
		
			// Verify that menu does not grow if displayValue is true
			selectMenu.setDisplayValueOnly(true);
			selectMenu.encodeBegin(MockFacesContext20.getCurrentInstance());
			assertTrue(selectMenu.getChildCount() == 1);
			
			// Verify that menu does not grow if displayValue is false
			selectMenu.setDisplayValueOnly(false);
			selectMenu.encodeBegin(MockFacesContext20.getCurrentInstance());
			assertTrue(selectMenu.getChildCount() == 1);

			} catch (Exception e) {
			assertFalse("No exception should occur here", false);
		}
	}
	
	/**
	 * Test selectMenuWithNumericSorting.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void selectMenuWithNumericSorting() {
		try {
			selectMenu.setCtiClass("no.stelvio.presentation.jsf.codestable.SimpleTestCti");
			selectMenu.setComparator("no.stelvio.common.codestable.support.DecodeNumericComparator");
			selectMenu.encodeBegin(MockFacesContext20.getCurrentInstance());

			assertTrue(selectMenu.getChildCount() == 1);
			List selectItemsList = (List) ((UISelectItems) selectMenu.getChildren().get(0)).getValue();
			assertEquals(createCodesTableItems().size(), selectItemsList.size());
			assertEquals(((SelectItem) selectItemsList.get(0)).getValue(), SimpleTestCti.createCtiNumericDecode1()
					.getCodeAsString());
			assertEquals(((SelectItem) selectItemsList.get(1)).getValue(), SimpleTestCti.createCtiNumericDecode2()
					.getCodeAsString());
			assertEquals(((SelectItem) selectItemsList.get(2)).getValue(), SimpleTestCti.createCtiNumericDecode10()
					.getCodeAsString());

		} catch (IOException e) {
			assertFalse("No exception should have occured", false);
		}
	}

	/**
	 * Create mocked item factory.
	 * 
	 * @return factory
	 */
	@SuppressWarnings("unchecked")
	private static CodesTableItemsFactory createMockedItemsFactory() {
		final List<SimpleTestCti> codesTableItems = new ArrayList<SimpleTestCti>(createCodesTableItems());
		final List<SimpleTestCtpi> codesTableItemPeriodics = new ArrayList<SimpleTestCtpi>(createCodesTablePeriodicItems());

		context.checking(new Expectations() {
			{
				allowing(codesTableItemsFactory).createCodesTableItems((Class<SimpleTestCti>) with(IsAnything.anything()));
				will(returnValue(codesTableItems));
				allowing(codesTableItemsFactory).createCodesTablePeriodicItems(
						(Class<SimpleTestCtpi>) with(IsAnything.anything()));
				will(returnValue(codesTableItemPeriodics));
			}
		});

		return codesTableItemsFactory;
	}

	/**
	 * Create codestable items.
	 * 
	 * @return items
	 */
	private static Set<SimpleTestCti> createCodesTableItems() {
		final Set<SimpleTestCti> codesTableItems = new HashSet<SimpleTestCti>(3);
		codesTableItems.add(SimpleTestCti.createCtiNumericDecode1());
		codesTableItems.add(SimpleTestCti.createCtiNumericDecode10());
		codesTableItems.add(SimpleTestCti.createCtiNumericDecode2());

		return codesTableItems;
	}

	/**
	 * Create codestable periodic items.
	 * 
	 * @return items
	 */
	private static Set<SimpleTestCtpi> createCodesTablePeriodicItems() {
		final Set<SimpleTestCtpi> codesTablePeriodicItems = new HashSet<SimpleTestCtpi>(2);
		codesTablePeriodicItems.add(SimpleTestCtpi.createExistsTimeCtpi1());
		codesTablePeriodicItems.add(SimpleTestCtpi.createExistsTimeCtpi3());

		return codesTablePeriodicItems;
	}

}
