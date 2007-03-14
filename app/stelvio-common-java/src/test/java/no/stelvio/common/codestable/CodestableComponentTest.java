package no.stelvio.common.codestable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hamcrest.core.IsAnything;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import no.stelvio.common.codestable.factory.CodesTableFactory;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;
import no.stelvio.common.codestable.support.DefaultCodesTableManager;

/**
 * Component test of Codestable.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodestableComponentTest {
	//
	private final TestCtiCode code = TestCti.getCti1().getCode();
	//
	private final Integer decode = TestCti.getCti1().getDecode();
	//
	private final Locale locale = new Locale("nb", "NO");
	//
	private Date date;
	//
	private DefaultCodesTableManager codesTableManager = new DefaultCodesTableManager();
	private Mockery context;

	@Before
	public void setUp() {
		//Test data			
		final List<TestCti> codesTableItems = new ArrayList<TestCti>();
		codesTableItems.add(TestCti.getCti1());
		codesTableItems.add(TestCti.getCti2());
		codesTableItems.add(TestCti.getCti3());

		final List<TestCtpi> codesTableItemPeriodics = new ArrayList<TestCtpi>();
		codesTableItemPeriodics.add(TestCtpi.getCtip1());
		codesTableItemPeriodics.add(TestCtpi.getCtip2());
		codesTableItemPeriodics.add(TestCtpi.getCtip3());

		//Mock CodesTableManger's methods
		context = new Mockery();
		final CodesTableItemsFactory codesTableItemsFactory = context.mock(CodesTableItemsFactory.class);

		context.expects(new InAnyOrder() {{
			one(codesTableItemsFactory).createCodesTableItems((Class<TestCti>) with(IsAnything.anything()));
			will(returnValue(codesTableItems));
			one(codesTableItemsFactory).createCodesTablePeriodicItems((Class<TestCtpi>) with(IsAnything.anything()));
			will(returnValue(codesTableItemPeriodics));
		}});

		//Initialize the test object
		this.codesTableManager.setCodesTableItemsFactory(codesTableItemsFactory);

		Calendar cal = Calendar.getInstance();
		cal.set(106, 10, 15);
		this.date = new Date(cal.getTimeInMillis());
	}

	/**
	 * Con001.001
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testCodesTable() {
		CodesTable<TestCti, TestCtiCode, Integer> codesTable = codesTableManager.getCodesTable(TestCti.class);

		assertEquals("Con001.001 : getCodesTable failed - doesn't hold an expected value",
				codesTable.getCodesTableItem(code), TestCti.getCti1());
		context.assertIsSatisfied();
	}

	/**
	 * Con002.001
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetDecode() {
		//Sets the locale in the request context
		LocaleContextHolder.setLocale(locale);

		assertEquals("Con004.001 : getDecode() failed ",
				codesTableManager.getCodesTable(TestCti.class).getDecode(code), decode);
		context.assertIsSatisfied();
	}

	/**
	 * Con003.001
	 */
	@Test
	public void testGetDecodeWithNonexistingCode() {
		try {
			codesTableManager.getCodesTable(TestCti.class).getDecode(TestCtiCode.EXISTS_NOT);
			fail("Con003.001 : Expected exception for getDecode()");
		} catch (Exception ex) {
			assertEquals("Con003.01 : getDecode() should have thrown exception",
					ex.getClass().getSimpleName(), "DecodeNotFoundException");
		}

		context.assertIsSatisfied();
	}

	/**
	 * Con004.001
	 */
	@Test
	public void testGetDecodeWithLocale() {
		assertEquals("Con004.001 : getDecode() with locale failed ",
				codesTableManager.getCodesTable(TestCti.class).getDecode(code, locale), decode);
		context.assertIsSatisfied();
	}

	/**
	 * Con005.001
	 */
	@Test
	public void testGetDecodeWithNonexistingLocale() {
		Locale nonSupportedLocale = new Locale("en", "GB");

		assertEquals("Con005.001 : getDecode() with non-existing locale failed ",
				codesTableManager.getCodesTable(TestCti.class).getDecode(code, nonSupportedLocale), decode);
		context.assertIsSatisfied();
	}

	/**
	 * Con007.001
	 */
	@Test
	public void testGetCodesTablePeriodic() {
		CodesTablePeriodic<TestCtpi, TestCtiCode, String> codesTablePeriodic =
				codesTableManager.getCodesTablePeriodic(TestCtpi.class);

		assertEquals("Con007.001 : getCodesTablePeriodic failed - doesn't hold an expected value",
				codesTablePeriodic.getCodesTableItem(code), TestCtpi.getCtip1());
		context.assertIsSatisfied();
	}

	/**
	 * Con008.01
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetDecodePeriodicWithNonvalidDate() {
		//Invalid date
		Calendar cal = Calendar.getInstance();
		cal.set(106, 9, 15);
		Date nonvalidDate = new Date(cal.getTimeInMillis());

		LocaleContextHolder.setLocale(locale);

		try {
			codesTableManager.getCodesTablePeriodic(TestCtpi.class).getDecode(code, nonvalidDate);
			fail("Con008.001 : Expected exception for getDecode()");
		} catch (Exception ex) {
			assertEquals("Con008.01 : getDecode() should have thrown exception",
					ex.getClass().getSimpleName(), "DecodeNotFoundException");
		}

		context.assertIsSatisfied();
	}

	/**
	 * Con009.001
	 */
	@Test
	public void testGetDecodePeriodic() {
		//Sets the locale in the request context
		LocaleContextHolder.setLocale(locale);

		assertEquals("Con009.001 : getDecode() failed ",
				codesTableManager.getCodesTablePeriodic(TestCtpi.class).getDecode(code, date), decode);
		context.assertIsSatisfied();
	}

	/**
	 * Con010.001
	 */
	@Test
	public void testGetDecodePeriodicWithNonexistingCode() {
		String nonExistingCode = "t12code";

		try {
			codesTableManager.getCodesTablePeriodic(TestCtpi.class).getDecode(nonExistingCode, date);
			fail("Con010.001 : Expected exception for getDecode()");
		} catch (Exception ex) {
			assertEquals("Con010.001 : getDecode() should have thrown exception",
					ex.getClass().getSimpleName(), "DecodeNotFoundException");
		}

		context.assertIsSatisfied();
	}

	/**
	 * Con011.001
	 */
	@Test
	public void testGetDecodePeriodicWithLocale() {
		assertEquals("Test : getDecode() with locale failed ",
				codesTableManager.getCodesTablePeriodic(TestCtpi.class).getDecode(code, locale, date), decode);
		context.assertIsSatisfied();
	}

	/**
	 * Con012.001
	 */
	@Test
	public void testGetDecodePeriodicWithNonexistingLocale() {
		Locale nonSupportedLocale = new Locale("en", "GB");

		assertEquals("Test : getDecode() with locale failed ",
				codesTableManager.getCodesTablePeriodic(TestCtpi.class).
						getDecode(code, nonSupportedLocale, date), decode);
		context.assertIsSatisfied();
	}
}
