package no.stelvio.common.codestable.support;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hamcrest.core.IsAnything;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Ignore;
import org.junit.Test;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtiCode;
import no.stelvio.common.codestable.TestCtpi;
import no.stelvio.common.codestable.factory.CodesTableFactory;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;

/**
 * Unit test for CodesTableManager.
 *
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableManagerTest {

	/**
	 * Tests CodesTableManager's methods for retrieval of <code>CodesTable</code>.
	 *
	 * @throws Exception
	 */
	@Test
	public void codesTableManager() {
		//Test data
		final List<TestCti> codesTableItems = new ArrayList<TestCti>();
		codesTableItems.add(TestCti.getCti1());
		codesTableItems.add(TestCti.getCti2());
		codesTableItems.add(TestCti.getCti3());

		final List<TestCtpi> codesTableItemPeriodics = new ArrayList<TestCtpi>();
		codesTableItemPeriodics.add(TestCtpi.getCtip1());
		codesTableItemPeriodics.add(TestCtpi.getCtip2());
		codesTableItemPeriodics.add(TestCtpi.getCtip3());

		Mockery context = new Mockery();
		final CodesTableItemsFactory codesTableItemsFactory = context.mock(CodesTableItemsFactory.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
				one(codesTableItemsFactory).createCodesTableItems((Class<TestCti>) with(IsAnything.anything()));
				will(returnValue(codesTableItems));
				one(codesTableItemsFactory).createCodesTablePeriodicItems((Class<TestCtpi>) with(IsAnything.anything()));
				will(returnValue(codesTableItemPeriodics));
			}
		});

		//Initialize the test object
		DefaultCodesTableManager codesTableManager = new DefaultCodesTableManager();
		codesTableManager.setCodesTableItemsFactory(codesTableItemsFactory);

		//Test the test objects method getCodesTable
		CodesTable<TestCti, TestCtiCode, Integer> codesTable = codesTableManager.getCodesTable(TestCti.class);
		Set tableItems = codesTable.getCodeTableItems();

		assertEquals("Test 1 : Codestable holds correct codestableitem 1",
				codesTable.getCodesTableItem(TestCti.getCti1().getCode()).getCode(), TestCti.getCti1().getCode());
		assertEquals("Test 2 : Codestable holds correct codestableitem 2",
				codesTable.getCodesTableItem(TestCti.getCti2().getCode()).getCode(), TestCti.getCti2().getCode());
		assertEquals("Test 3 : Codestable holds correct codestableitem 3",
				codesTable.getCodesTableItem(TestCti.getCti3().getCode()).getCode(), TestCti.getCti3().getCode());
		assertNull("Test 4 : Codestable shouldn't hold this codestableitem",
				codesTable.getCodesTableItem(TestCti.getCti4().getCode()));

		//Test the test objects method getCodesTablePeriodic
		CodesTablePeriodic<TestCtpi, TestCtiCode, String> codesTablePeriodic =
				codesTableManager.getCodesTablePeriodic(TestCtpi.class);

		assertEquals("Test 5 : Codestableperiodic holds correct codestableitemperiodic 1",
				codesTablePeriodic.getCodesTableItem(TestCtpi.getCtip1().getCode()).getCode(), TestCtpi.getCtip1().getCode());
		assertEquals("Test 6 : Codestableperiodic holds correct codestableitemperiodic 2",
				codesTablePeriodic.getCodesTableItem(TestCtpi.getCtip2().getCode()).getCode(), TestCtpi.getCtip2().getCode());
		assertEquals("Test 7 : Codestableperiodic holds correct codestableitemperiodic 3",
				codesTablePeriodic.getCodesTableItem(TestCtpi.getCtip3().getCode()).getCode(), TestCtpi.getCtip3().getCode());
		assertNull("Test 8 : Codestableperiodic shouldn't hold this codestableitemperiodic",
				codesTablePeriodic.getCodesTableItem(TestCtpi.getCtip4().getCode()));

		context.assertIsSatisfied();
	}

	@Ignore("Not ready yet")
	@Test
	public void shouldMakeACopyOfCodesTableReturnedFromFactory() {
		DefaultCodesTableManager ctm = new DefaultCodesTableManager();
	}

	@Test
	public void usage() {
		CodesTableManager codesTableManager = new DefaultCodesTableManager();
		CodesTable<TestCti, TestCtiCode, Integer> table = codesTableManager.getCodesTable(TestCti.class);
		Set<TestCti> testCtis = table.getCodeTableItems();
		TestCti item = table.getCodesTableItem(TestCtiCode.EXISTS_1);
		Integer decode = table.getDecode(TestCtiCode.EXISTS_2);

		CodesTablePeriodic<TestCtpi, TestCtiCode, String> tablePeriodic =
				codesTableManager.getCodesTablePeriodic(TestCtpi.class);
		Set<TestCtpi> periodicCtis = tablePeriodic.getCodeTableItems();
		TestCtpi codesTableItemPer = tablePeriodic.getCodesTableItem(TestCtiCode.EXISTS_1);
		String decodePer = tablePeriodic.getDecode(TestCtiCode.EXISTS_2, new Date());
	}
}