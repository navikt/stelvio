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
		codesTableItems.add(TestCodesTableItem.CTI1);
		codesTableItems.add(TestCodesTableItem.CTI2);
		codesTableItems.add(TestCodesTableItem.CTI3);
		
		final List<CodesTableItemPeriodic> codesTableItemPeriodics = new ArrayList<CodesTableItemPeriodic>();
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.CTIP1);
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.CTIP2);
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.CTIP3);

		context = new Mockery();
		final CodesTableFactory codesTableFactory = context.mock(CodesTableFactory.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
			one(codesTableFactory).retrieveCodesTable((Class<CodesTableItem>) with(IsAnything.anything()));
			will(returnValue(codesTableItems));
			one(codesTableFactory).retrieveCodesTablePeriodic((Class<CodesTableItemPeriodic>) with(IsAnything.anything()));
			will(returnValue(codesTableItemPeriodics));
		}});

		//Initialize the test object
		DefaultCodesTableManager codesTableManager = new DefaultCodesTableManager();
		codesTableManager.setCodesTableFactory(codesTableFactory);

		//Test the test objects method getCodesTable
		CodesTable codesTable = codesTableManager.getCodesTable(TestCodesTableItem.CTI1.getClass());

		assertEquals("Test 1 : Codestable holds correct codestableitem 1", codesTable.getCodesTableItem(TestCodesTableItem.CTI1.getCode()).getCode(), TestCodesTableItem.CTI1.getCode());
		assertEquals("Test 2 : Codestable holds correct codestableitem 2", codesTable.getCodesTableItem(TestCodesTableItem.CTI2.getCode()).getCode(), TestCodesTableItem.CTI2.getCode());
		assertEquals("Test 3 : Codestable holds correct codestableitem 3", codesTable.getCodesTableItem(TestCodesTableItem.CTI3.getCode()).getCode(), TestCodesTableItem.CTI3.getCode());
		assertNull("Test 4 : Codestable shouldn't hold this codestableitem", codesTable.getCodesTableItem(TestCodesTableItem.CTI4.getCode()));
		
		//Test the test objects method getCodesTablePeriodic
		CodesTablePeriodic codesTablePeriodic = codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass());

		assertEquals("Test 5 : Codestableperiodic holds correct codestableitemperiodic 1", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.CTIP1.getCode()).getCode(), TestCodesTableItemPeriodic.CTIP1.getCode());
		assertEquals("Test 6 : Codestableperiodic holds correct codestableitemperiodic 2", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.CTIP2.getCode()).getCode(), TestCodesTableItemPeriodic.CTIP2.getCode());
		assertEquals("Test 7 : Codestableperiodic holds correct codestableitemperiodic 3", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.CTIP3.getCode()).getCode(), TestCodesTableItemPeriodic.CTIP3.getCode());
		assertNull("Test 8 : Codestableperiodic shouldn't hold this codestableitemperiodic", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.CTIP4.getCode()));

		context.assertIsSatisfied();
	}
}