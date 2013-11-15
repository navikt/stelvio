package no.stelvio.presentation.jsf.codestable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * CodesTablePeriodicItemSelectOneMenuTest.  Tests filtering codeitems based on a given date.  
 * 
 * @author ML
 */
public class CodesTablePeriodicItemSelectOneMenuTest {

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
	 * Set up before test.  Uses the setValidOnDate method to provide the date that the codeitems should 
	 * be filtered on.  
	 */
	@Before
	public void setUp() {
		selectMenu = new CodesTableItemSelectOneMenu();
		ctm = new DefaultCodesTableManager();
		createMockedItemsFactory();
		ctm.setCodesTableItemsFactory(codesTableItemsFactory);

		selectMenu.setCodesTableManager(ctm);
		selectMenu.setValidOnDate(getDate(2010, 2, 12));
	}

	/**
	 * Test selectMenuWithTestCti.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void selectMenuWithTestCtpi() {
		try {
			selectMenu.setCtiClass("no.stelvio.presentation.jsf.codestable.SimpleTestCtpi");
			selectMenu.encodeBegin(MockFacesContext20.getCurrentInstance());

			assertTrue(selectMenu.getChildCount() == 1);
			List selectItemsList = (List) ((UISelectItems) selectMenu.getChildren().get(0)).getValue();
			assertEquals(createTestCodesTablePeriodicItems().size(), selectItemsList.size());
		} catch (IOException e) {
			assertFalse("No exception should have occured", false);
		}
	}

	/**
	 * Test selectMenuWithNumericSorting.  
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void selectMenuWithNumericSorting() {
		try {
			selectMenu.setCtiClass("no.stelvio.presentation.jsf.codestable.SimpleTestCtpi");
			selectMenu.setComparator("no.stelvio.common.codestable.support.DecodeComparator");
			selectMenu.encodeBegin(MockFacesContext20.getCurrentInstance());

			assertTrue(selectMenu.getChildCount() == 1);
			List selectItemsList = (List) ((UISelectItems) selectMenu.getChildren().get(0)).getValue();
			assertEquals(createTestCodesTablePeriodicItems().size(), selectItemsList.size());
			assertEquals(((SelectItem) selectItemsList.get(0)).getValue(), SimpleTestCtpi.createExistsTimeCtpi4()
			.getCodeAsString());
			assertEquals(((SelectItem) selectItemsList.get(1)).getValue(), SimpleTestCtpi.createExistsTimeCtpi5()
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
				allowing(codesTableItemsFactory).createCodesTablePeriodicItems((Class<SimpleTestCtpi>) with(IsAnything.anything()));
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
	 * Create codestable periodic items that is added to the CodesTableItemSelectOneMenu.  
	 * Adds two valid ctpis and two non-valid ctpis.  
	 * 
	 * @return codesTablePeriodicItems
	 */
	private static Set<SimpleTestCtpi> createCodesTablePeriodicItems() {
		final Set<SimpleTestCtpi> codesTablePeriodicItems = new HashSet<SimpleTestCtpi>(4);
		codesTablePeriodicItems.add(SimpleTestCtpi.createExistsTimeCtpi1());
		codesTablePeriodicItems.add(SimpleTestCtpi.createExistsTimeCtpi3());
		codesTablePeriodicItems.add(SimpleTestCtpi.createExistsTimeCtpi4());
		codesTablePeriodicItems.add(SimpleTestCtpi.createExistsTimeCtpi5());

		return codesTablePeriodicItems;
	}
	
	/**
	 * Create codestable periodic items, and is used as reference in the tests.  Adds only the two valid ctpis.  
	 * 
	 * @return codesTablePeriodicItems
	 */
	private static Set<SimpleTestCtpi> createTestCodesTablePeriodicItems() {
		final Set<SimpleTestCtpi> codesTablePeriodicItems = new HashSet<SimpleTestCtpi>(2);
		codesTablePeriodicItems.add(SimpleTestCtpi.createExistsTimeCtpi4());
		codesTablePeriodicItems.add(SimpleTestCtpi.createExistsTimeCtpi5());

		return codesTablePeriodicItems;
	}
	
	/**
	 * Utility method for creating date objects.    
	 * 
	 * @return date
	 */
	private static Date getDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}
}
