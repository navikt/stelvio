package no.stelvio.common.codestable;

import java.util.ArrayList;
import java.util.List;

import com.agical.rmock.extension.junit.RMockTestCase;

/**
 * Unit test for CodesTableManager.
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableManagerTest extends RMockTestCase  {
	
	/**
	 * Tests CodesTableManager's methods for retrieval of <code>CodesTable</code>.
	 * @throws Exception
	 */
	public void testCodesTableManager() throws Exception{
		//Test data
		CodesTable codesTable = new CodesTableImpl();
		CodesTablePeriodic codesTablePeriodic = new CodesTablePeriodicImpl();
				
		List<CodesTableItem> codesTableItems = new ArrayList<CodesTableItem>();
		codesTableItems.add(TestCodesTableItem.CTI1);
		codesTableItems.add(TestCodesTableItem.CTI2);
		codesTableItems.add(TestCodesTableItem.CTI3);
		
		List<CodesTableItemPeriodic> codesTableItemPeriodics = new ArrayList<CodesTableItemPeriodic>();
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.CTIP1);
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.CTIP2);
		codesTableItemPeriodics.add(TestCodesTableItemPeriodic.CTIP3);
			
		//Mock CodesTableManger's methods
		CodesTableFactory mockCodesTableFactory = (CodesTableFactory) mock(CodesTableFactory.class);
		mockCodesTableFactory.retrieveCodesTable(TestCodesTableItem.CTI1.getClass());
		modify().args(is.ANYTHING);
		modify().returnValue(codesTableItems);
		mockCodesTableFactory.retrieveCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass());
		modify().args(is.ANYTHING);
		modify().returnValue(codesTableItemPeriodics);
		startVerification();
				
		//Initialize the test object
		CodesTableManagerImpl codesTableManager = new CodesTableManagerImpl();
		codesTableManager.setCodesTableFactory(mockCodesTableFactory);
		
		//Test the test objects method getCodesTable
		codesTable = codesTableManager.getCodesTable(TestCodesTableItem.CTI1.getClass());
		
		assertEquals("Test 1 : Codestable holds correct codestableitem 1", codesTable.getCodesTableItem(TestCodesTableItem.CTI1.getCode()).getCode(), TestCodesTableItem.CTI1.getCode());
		assertEquals("Test 2 : Codestable holds correct codestableitem 2", codesTable.getCodesTableItem(TestCodesTableItem.CTI2.getCode()).getCode(), TestCodesTableItem.CTI2.getCode());
		assertEquals("Test 3 : Codestable holds correct codestableitem 3", codesTable.getCodesTableItem(TestCodesTableItem.CTI3.getCode()).getCode(), TestCodesTableItem.CTI3.getCode());
		assertNull("Test 4 : Codestable shouldn't hold this codestableitem", codesTable.getCodesTableItem(TestCodesTableItem.CTI4.getCode()));
		
		//Test the test objects method getCodesTablePeriodic
		codesTablePeriodic = codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass());
		
		assertEquals("Test 5 : Codestableperiodic holds correct codestableitemperiodic 1", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.CTIP1.getCode()).getCode(), TestCodesTableItemPeriodic.CTIP1.getCode());
		assertEquals("Test 6 : Codestableperiodic holds correct codestableitemperiodic 2", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.CTIP2.getCode()).getCode(), TestCodesTableItemPeriodic.CTIP2.getCode());
		assertEquals("Test 7 : Codestableperiodic holds correct codestableitemperiodic 3", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.CTIP3.getCode()).getCode(), TestCodesTableItemPeriodic.CTIP3.getCode());
		assertNull("Test 8 : Codestableperiodic shouldn't hold this codestableitemperiodic", codesTablePeriodic.getCodesTableItem(TestCodesTableItemPeriodic.CTIP4.getCode()));

	}
}