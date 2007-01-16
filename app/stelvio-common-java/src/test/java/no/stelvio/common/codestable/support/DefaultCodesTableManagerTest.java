package no.stelvio.common.codestable.support;


import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsAnything;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.TestCodesTableItem;
import no.stelvio.common.codestable.TestCodesTableItemPeriodic;
import no.stelvio.common.codestable.factory.CodesTableFactory;

/**
 * Unit test for CodesTableManager.
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableManagerTest {
	private Mockery context;

	/**
	 * Tests CodesTableManager's methods for retrieval of <code>CodesTable</code>.
	 * @throws Exception
	 */
	@Test
	public void testCodesTableManager() throws Exception{
		//Test data
		final List<CodesTableItem> codesTableItems = new ArrayList<CodesTableItem>();
		codesTableItems.add(TestCodesTableItem.getCti1());
		codesTableItems.add(TestCodesTableItem.getCti2());
		codesTableItems.add(TestCodesTableItem.getCti3());
		
		final List<CodesTableItemPeriodic> codesTableItemPeriodics = new ArrayList<CodesTableItemPeriodic>();
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.getCtip1());
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.getCtip2());
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.getCtip3());

		context = new Mockery();
		final CodesTableFactory codesTableFactory = context.mock(CodesTableFactory.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
			one(codesTableFactory).createCodesTable((Class<CodesTableItem>) with(IsAnything.anything()));
			will(returnValue(codesTableItems));
			one(codesTableFactory).createCodesTablePeriodic((Class<CodesTableItemPeriodic>) with(IsAnything.anything()));
			will(returnValue(codesTableItemPeriodics));
		}});

		//Initialize the test object
		DefaultCodesTableManager codesTableManager = new DefaultCodesTableManager();
		codesTableManager.setCodesTableFactory(codesTableFactory);

		//Test the test objects method getCodesTable
		CodesTable codesTable = codesTableManager.getCodesTable(TestCodesTableItem.getCti1().getClass());

		assertEquals("Test 1 : Codestable holds correct codestableitem 1", codesTable.getCodesTableItem(TestCodesTableItem.getCti1().getCode()).getCode(), TestCodesTableItem.getCti1().getCode());
		assertEquals("Test 2 : Codestable holds correct codestableitem 2", codesTable.getCodesTableItem(TestCodesTableItem.getCti2().getCode()).getCode(), TestCodesTableItem.getCti2().getCode());
		assertEquals("Test 3 : Codestable holds correct codestableitem 3", codesTable.getCodesTableItem(TestCodesTableItem.getCti3().getCode()).getCode(), TestCodesTableItem.getCti3().getCode());
		assertNull("Test 4 : Codestable shouldn't hold this codestableitem", codesTable.getCodesTableItem(TestCodesTableItem.getCti4().getCode()));
		
		//Test the test objects method getCodesTablePeriodic
		CodesTablePeriodic codesTablePeriodic = codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.getCtip1().getClass());

		assertEquals("Test 5 : Codestableperiodic holds correct codestableitemperiodic 1", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.getCtip1().getCode()).getCode(), TestCodesTableItemPeriodic.getCtip1().getCode());
		assertEquals("Test 6 : Codestableperiodic holds correct codestableitemperiodic 2", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.getCtip2().getCode()).getCode(), TestCodesTableItemPeriodic.getCtip2().getCode());
		assertEquals("Test 7 : Codestableperiodic holds correct codestableitemperiodic 3", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.getCtip3().getCode()).getCode(), TestCodesTableItemPeriodic.getCtip3().getCode());
		assertNull("Test 8 : Codestableperiodic shouldn't hold this codestableitemperiodic", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.getCtip4().getCode()));

		context.assertIsSatisfied();
	}
}