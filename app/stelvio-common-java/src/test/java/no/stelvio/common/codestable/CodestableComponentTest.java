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
import no.stelvio.common.codestable.support.DefaultCodesTable;
import no.stelvio.common.codestable.support.DefaultCodesTableManager;
import no.stelvio.common.codestable.support.DefaultCodesTablePeriodic;

/**
 * Component test of Codestable.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodestableComponentTest {
	//
	private final String code = TestCodesTableItem.getCti1().getCode();
	//
	private final String decode = TestCodesTableItem.getCti1().getDecode();
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
		final List<CodesTableItem> codesTableItems = new ArrayList<CodesTableItem>();
		codesTableItems.add(TestCodesTableItem.getCti1());
		codesTableItems.add(TestCodesTableItem.getCti2());
		codesTableItems.add(TestCodesTableItem.getCti3());

		final List<CodesTableItemPeriodic> codesTableItemPeriodics = new ArrayList<CodesTableItemPeriodic>();
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.getCtip1());
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.getCtip2());
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.getCtip3());

		//Mock CodesTableManger's methods
		context = new Mockery();
		final CodesTableFactory codesTableFactory = context.mock(CodesTableFactory.class);

		context.expects(new InAnyOrder() {{
			one(codesTableFactory).createCodesTable((Class<CodesTableItem>) with(IsAnything.anything()));
			will(returnValue(codesTableItems));
			one(codesTableFactory).createCodesTablePeriodic((Class<CodesTableItemPeriodic>) with(IsAnything.anything()));
			will(returnValue(codesTableItemPeriodics));
		}});

		//Initialize the test object
		this.codesTableManager.setCodesTableFactory(codesTableFactory);

		Calendar cal = Calendar.getInstance();
		cal.set(106, 10, 15);
		this.date = new Date(cal.getTimeInMillis());
	}

	/**
	 * Con001.001
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testCodesTable() throws Exception {

		CodesTable codesTable = new DefaultCodesTable(new ArrayList());
		codesTable = codesTableManager.getCodesTable(TestCodesTableItem.getCti1().getClass());

		assertEquals("Con001.001 : getCodesTable failed - doesn't hold an expected value", codesTable.getCodesTableItem(code), TestCodesTableItem.getCti1());
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

		assertEquals("Con004.001 : getDecode() failed ", codesTableManager.getCodesTable(TestCodesTableItem.getCti1().getClass()).getDecode(code), decode);
		context.assertIsSatisfied();
	}

	/**
	 * Con003.001
	 */
	@Test
	public void testGetDecodeWithNonexistingCode() {
		String nonExistingCode = "t12code";

		try {
			codesTableManager.getCodesTable(TestCodesTableItem.getCti1().getClass()).getDecode(nonExistingCode);
			fail("Con003.001 : Expected exception for getDecode()");
		} catch (Exception ex) {
			assertEquals("Con003.01 : getDecode() should have thrown exception", ex.getClass().getSimpleName(), "DecodeNotFoundException");
		}

		context.assertIsSatisfied();
	}

	/**
	 * Con004.001
	 */
	@Test
	public void testGetDecodeWithLocale() {
		assertEquals("Con004.001 : getDecode() with locale failed ", codesTableManager.getCodesTable(TestCodesTableItem.getCti1().getClass()).getDecode(code, locale), decode);
		context.assertIsSatisfied();
	}

	/**
	 * Con005.001
	 */
	@Test
	public void testGetDecodeWithNonexistingLocale() {
		Locale nonSupportedLocale = new Locale("en", "GB");

		assertEquals("Con005.001 : getDecode() with non-existing locale failed ", codesTableManager.getCodesTable(TestCodesTableItem.getCti1().getClass()).getDecode(code, nonSupportedLocale), decode);
		context.assertIsSatisfied();
	}

	/**
	 * Con007.001
	 */
	@Test
	public void testGetCodesTablePeriodic() {
		CodesTablePeriodic codesTablePeriodic = new DefaultCodesTablePeriodic(new ArrayList());
		codesTablePeriodic = codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.getCtip1().getClass());

		assertEquals("Con007.001 : getCodesTablePeriodic failed - doesn't hold an expected value", codesTablePeriodic.getCodesTableItem(code), TestCodesTableItemPeriodic.getCtip1());
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
			codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.getCtip1().getClass()).getDecode(code, nonvalidDate);
			fail("Con008.001 : Expected exception for getDecode()");
		} catch (Exception ex) {
			assertEquals("Con008.01 : getDecode() should have thrown exception", ex.getClass().getSimpleName(), "DecodeNotFoundException");
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

		assertEquals("Con009.001 : getDecode() failed ", codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.getCtip1().getClass()).getDecode(code, date), decode);
		context.assertIsSatisfied();
	}

	/**
	 * Con010.001
	 */
	@Test
	public void testGetDecodePeriodicWithNonexistingCode() {
		String nonExistingCode = "t12code";

		try {
			codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.getCtip1().getClass()).getDecode(nonExistingCode, date);
			fail("Con010.001 : Expected exception for getDecode()");
		} catch (Exception ex) {
			assertEquals("Con010.001 : getDecode() should have thrown exception", ex.getClass().getSimpleName(), "DecodeNotFoundException");
		}

		context.assertIsSatisfied();
	}

	/**
	 * Con011.001
	 */
	@Test
	public void testGetDecodePeriodicWithLocale() {
		assertEquals("Test : getDecode() with locale failed ", codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.getCtip1().getClass()).getDecode(code, locale, date), decode);
		context.assertIsSatisfied();
	}

	/**
	 * Con012.001
	 */
	@Test
	public void testGetDecodePeriodicWithNonexistingLocale() {
		Locale nonSupportedLocale = new Locale("en", "GB");

		assertEquals("Test : getDecode() with locale failed ", codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.getCtip1().getClass()).getDecode(code, nonSupportedLocale, date), decode);
		context.assertIsSatisfied();
	}
}
