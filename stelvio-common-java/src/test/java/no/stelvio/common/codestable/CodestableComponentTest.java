package no.stelvio.common.codestable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;

import no.stelvio.common.codestable.factory.CodesTableItemsFactory;
import no.stelvio.common.codestable.support.DefaultCodesTableManager;
import no.stelvio.common.codestable.support.DefaultCodesTablePeriodic;

/**
 * Component test of Codestable. Is kept for historic reasons as this is the original component test.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */

public class CodestableComponentTest {
	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	private final TestCtiCode code = TestCti.createCti1().getCode();
	private final String decode = TestCti.createCti1().getDecode();
	private final Locale locale = new Locale("nb", "NO");
	private Date date;

	/**
	 * Set up.
	 */
	@Before
	public void setUp() {
		Calendar cal = Calendar.getInstance();
		cal.set(106, 10, 15);
		this.date = new Date(cal.getTimeInMillis());
	}

	/** Con001.001. */
	@Test
	public void testCodesTable() {
		CodesTable<TestCti, TestCtiCode, String> codesTable = createCodesTableManager().getCodesTable(TestCti.class);

		assertEquals("Con001.001 : getCodesTable failed - doesn't hold an expected value", codesTable.getCodesTableItem(code),
				TestCti.createCti1());
	}

	/** Con002.001. */
	@Test
	public void testGetDecode() {
		// Sets the locale in the request context
		LocaleContextHolder.setLocale(locale);

		assertEquals("Con004.001 : getDecode() failed ",
				createCodesTableManager().getCodesTable(TestCti.class).getDecode(code), decode);
	}

	/** Con003.001. */
	@Test(expected = ItemNotFoundException.class)
	public void testGetDecodeWithNonexistingCode() {
		createCodesTableManager().getCodesTable(TestCti.class).getDecode(TestCtiCode.EXISTS_NOT);
	}

	/** Con004.001. */
	@Test
	public void testGetDecodeWithLocale() {
		assertEquals("Con004.001 : getDecode() with locale failed ", createCodesTableManager().getCodesTable(TestCti.class)
				.getDecode(code, locale), decode);
	}

	/** Con005.001. */
	@Test(expected = NoSuchMessageException.class)
	public void testGetDecodeWithNonexistingLocale() {
		Locale nonSupportedLocale = new Locale("en", "GB");

		assertEquals("Con005.001 : getDecode() with non-existing locale failed ", createCodesTableManager().getCodesTable(
				TestCti.class).getDecode(code, nonSupportedLocale), decode);
	}

	/** Con007.001. */
	@Test
	public void testGetCodesTablePeriodic() {
		CodesTablePeriodic<TestCtpi, TestCtiCode, String> codesTablePeriodic = createCodesTableManager().getCodesTablePeriodic(
				TestCtpi.class);

		assertEquals("Con007.001 : getCodesTablePeriodic failed - doesn't hold an expected value", codesTablePeriodic
				.getCodesTableItem(code), TestCtpi.createCtpi1());
	}

	/** Con008.01. */
	// @Ignore("Filtering based on date is not implemented yet")
	@Test(expected = ItemNotFoundException.class)
	public void testGetDecodePeriodicWithInvalidDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(106, 9, 15);
		Date invalidDate = new Date(cal.getTimeInMillis());
		LocaleContextHolder.setLocale(locale);

		createCodesTableManager().getCodesTablePeriodic(TestCtpi.class).getDecode(code, invalidDate);
	}

	/** Con009.001. */
	@Test
	public void testGetDecodePeriodic() {
		// Sets the locale in the request context
		LocaleContextHolder.setLocale(locale);

		assertEquals("Con009.001 : getDecode() failed ", createCodesTableManager().getCodesTablePeriodic(TestCtpi.class)
				.getDecode(code, date), decode);
	}

	/** Con010.001. */
	@Test(expected = ItemNotFoundException.class)
	public void testGetDecodePeriodicWithNonexistingCode() {
		createCodesTableManager().getCodesTablePeriodic(TestCtpi.class).getDecode(TestCtiCode.EXISTS_NOT, date);
	}

	/** Con011.001. */
	@Test
	public void testGetDecodePeriodicWithLocale() {
        LocaleContextHolder.setLocale(locale);
		assertEquals("Test : getDecode() with locale failed ", createCodesTableManager().getCodesTablePeriodic(TestCtpi.class)
				.getDecode(code, locale, date), decode);
	}

	/** Con012.001. */
	@Test(expected = NoSuchMessageException.class)
	public void testGetDecodePeriodicWithNonexistingLocale() {
		Locale nonSupportedLocale = new Locale("en", "GB");

		assertEquals("Test : getDecode() with locale failed ", createCodesTableManager().getCodesTablePeriodic(TestCtpi.class)
				.getDecode(code, nonSupportedLocale, date), decode);
	}

	/**
	 * Test overlap with null as to-date.
	 */
	@Test
	public void testOverlapsWithNullAsToDate() {
		List<TestCtpi> itemList = createOverlapWithNullCtpi();
		CodesTablePeriodic<TestCtpi, TestCtiCode, String> table = new DefaultCodesTablePeriodic<TestCtpi, TestCtiCode, String>(
				itemList, TestCtpi.class);
		Set<TestCtpi> itemSet = table.getCodesTableItems();
		assertThat(itemSet.size(), is(equalTo(2)));
		String ctpiWithNullToDateDecode = table.getDecode(TestCtiCode.OVERLAP_WITH_NULL, getOverlapWithNullDate());
		for (TestCtpi ctpi : itemSet) {
			if (ctpi.getCode().equals(ctpiWithNullToDateDecode)) {
				// Only one item is allowed in a CodesTablePeriodic with toDate == null
				if (ctpi.getToDate() == null) {
					assertTrue(ctpiWithNullToDateDecode != null);
				}
			}
		}
		String ctipWithoutToDateDecode = table.getDecode(TestCtiCode.OVERLAP_WITH_NULL, getOverlapWithToDate());
		assertEquals("SHOULD_BE_IN_LIST", ctipWithoutToDateDecode);

	}

	/**
	 * Creates a codestable manager.
	 * 
	 * @return codestable manager
	 */
	private CodesTableManager createCodesTableManager() {
		final List<TestCti> codesTableItems = createCodesTableItems();
		final List<TestCtpi> codesTablePeriodicItems = createCodesTablePeriodicItems();
		final CodesTableItemsFactory codesTableItemsFactory = createCodesTableItemsFactory(codesTableItems,
				codesTablePeriodicItems);
		final ApplicationContext applicationContext = createApplicationContext();

		DefaultCodesTableManager ctm = new DefaultCodesTableManager();
		ctm.setCodesTableItemsFactory(codesTableItemsFactory);
		ctm.setApplicationContext(applicationContext);

		return ctm;
	}

	/**
	 * Create application context.
	 * 
	 * @return context
	 */
	private ApplicationContext createApplicationContext() {
		final ApplicationContext applicationContext = context.mock(ApplicationContext.class);

		context.checking(new Expectations() {
			{
				allowing(applicationContext).getBean(with(any(String.class)));
				will(returnValue(new StaticMessageSource()));
			}
		});

		return applicationContext;
	}

	/**
	 * Creates codestable items factory.
	 * 
	 * @param codesTableItems
	 *            codestable items
	 * @param codesTableItemPeriodics
	 *            codestable item periodic
	 * @return factory
	 */
	private CodesTableItemsFactory createCodesTableItemsFactory(final List<TestCti> codesTableItems,
			final List<TestCtpi> codesTableItemPeriodics) {
		final CodesTableItemsFactory codesTableItemsFactory = context.mock(CodesTableItemsFactory.class);

		context.checking(new Expectations() {
			{
				atMost(1).of(codesTableItemsFactory).createCodesTableItems(TestCti.class);
				will(returnValue(codesTableItems));
				atMost(1).of(codesTableItemsFactory).createCodesTablePeriodicItems(TestCtpi.class);
				will(returnValue(codesTableItemPeriodics));
			}
		});

		return codesTableItemsFactory;
	}

	/**
	 * Creates codestable periodic items.
	 * 
	 * @return items
	 */
	private List<TestCtpi> createCodesTablePeriodicItems() {
		final List<TestCtpi> codesTableItemPeriodics = new ArrayList<TestCtpi>();
		codesTableItemPeriodics.add(TestCtpi.createCtpi1());
		codesTableItemPeriodics.add(TestCtpi.createCtpi2());
		codesTableItemPeriodics.add(TestCtpi.createCtpi3());
		codesTableItemPeriodics.add(TestCtpi.createCtpi4());
		return codesTableItemPeriodics;
	}

	/**
	 * Creates codestable items.
	 * 
	 * @return items
	 */
	private List<TestCti> createCodesTableItems() {
		final List<TestCti> codesTableItems = new ArrayList<TestCti>();
		codesTableItems.add(TestCti.createCti1());
		codesTableItems.add(TestCti.createCti2());
		codesTableItems.add(TestCti.createCti3());
		return codesTableItems;
	}

	/**
	 * Creates codestable periodic items that overlap with null.
	 * 
	 * @return items
	 */
	private List<TestCtpi> createOverlapWithNullCtpi() {
		final List<TestCtpi> codesTablePeriodicItems = new ArrayList<TestCtpi>();
		codesTablePeriodicItems.add(TestCtpi.createOverlapCtpiWithNull());
		codesTablePeriodicItems.add(TestCtpi.createOverlapCtpiWithNull2());
		codesTablePeriodicItems.add(TestCtpi.createOverlapCtpiWithNull3());
		return codesTablePeriodicItems;
	}

	/**
	 * Get overlap with null date.
	 * 
	 * @return date
	 */
	private Date getOverlapWithNullDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(2007, 5, 15, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * Get overlap with to-date.
	 * 
	 * @return date
	 */
	private Date getOverlapWithToDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(2007, 3, 10, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}
}
