package no.stelvio.common.codestable;

import java.util.ArrayList;
import java.util.List;

import com.agical.rmock.extension.junit.RMockTestCase;
import no.stelvio.common.error.UnrecoverableException;

/**
 * Unit test of CodesTableInitializer.
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodesTableInitializerTest extends RMockTestCase  {
				
	/**
	 * Tests CodesTableInitializer with both <code>CodesTableItem</code>'s and <code>CodesTableItemPeriodic</code>'s.
	 * @throws Exception
	 */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public void testCodesTableInitializer() throws Exception {
		//Test data
		CodesTable codesTable = new CodesTableImpl();
		codesTable.addCodesTableItem(TestCodesTableItem.CTI1);
		codesTable.addCodesTableItem(TestCodesTableItem.CTI2);
		
		CodesTablePeriodic codesTablePeriodic = new CodesTablePeriodicImpl();
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.CTIP1);
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.CTIP2);
		
		List<Class<CodesTableItem>> cti = new ArrayList<Class<CodesTableItem>>();
		cti.add(CodesTableItem.class);
		
		List<Class<CodesTableItemPeriodic>> ctip = new ArrayList<Class<CodesTableItemPeriodic>>();
		ctip.add(CodesTableItemPeriodic.class);
		
		//Mock CodesTableManger's methods
		CodesTableManager mockCodesTableManager = (CodesTableManager) mock(CodesTableManager.class);
		mockCodesTableManager.getCodesTable(TestCodesTableItem.CTI1.getClass());
		modify().args(is.ANYTHING);
		modify().returnValue(codesTable);
		
		mockCodesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass());
		modify().args(is.ANYTHING);
		modify().returnValue(codesTablePeriodic);
		startVerification();
				
		//Initialize the test object
		CodesTableInitializerImpl codesTableInitializer = new CodesTableInitializerImpl();
		codesTableInitializer.setCodesTableClasses(cti);
		codesTableInitializer.setCodesTablePeriodicClasses(ctip);
		codesTableInitializer.setCodesTableManager(mockCodesTableManager);
		
		//Test the test objects method
		try{
			codesTableInitializer.init();
		} catch(Exception ex){
			fail("Unexptected exception " +ex);
		}		
	}

	/**
	 * Tests CodesTableInitializer with only <code>CodesTableItem</code>'s.
	 * @throws Exception
	 */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public void testCodesTableInitializerGetCodesTable() throws Exception {
		//Test data
		CodesTable codesTable = new CodesTableImpl();
		codesTable.addCodesTableItem(TestCodesTableItem.CTI1);
		codesTable.addCodesTableItem(TestCodesTableItem.CTI2);
		
		List<Class<CodesTableItem>> cti = new ArrayList<Class<CodesTableItem>>();
		cti.add(CodesTableItem.class);
		
		//Mock CodesTableMangers method
		CodesTableManager mockCodesTableManager = (CodesTableManager) mock(CodesTableManager.class);
		mockCodesTableManager.getCodesTable(TestCodesTableItem.CTI1.getClass());
		modify().args(is.ANYTHING);
		modify().returnValue(codesTable);
		startVerification();
		
		//Initialize the test object
		CodesTableInitializerImpl codesTableInitializer = new CodesTableInitializerImpl();
		codesTableInitializer.setCodesTableClasses(cti);
		codesTableInitializer.setCodesTableManager(mockCodesTableManager);
		
		//Test the test objects method
		try{
			codesTableInitializer.init();
		} catch(Exception ex){
			fail("Unexptected exception " +ex);
		}		
	}
	
	/**
	 * Tests CodesTableInitializer with both only <code>CodesTableItemPeriodic</code>'s.
	 * @throws Exception
	 */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public void testCodesTableInitializerGetCodesTablePeriodic() throws Exception{
		//Test data		
		CodesTablePeriodic codesTablePeriodic = new CodesTablePeriodicImpl();
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.CTIP1);
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.CTIP2);
		
		List<Class<CodesTableItemPeriodic>> ctip = new ArrayList<Class<CodesTableItemPeriodic>>();
		ctip.add(CodesTableItemPeriodic.class);
		
		//Mock CodesTableMangers method
		CodesTableManager mockCodesTableManager = (CodesTableManager) mock(CodesTableManager.class);
		mockCodesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass());
		modify().args(is.ANYTHING);
		modify().returnValue(codesTablePeriodic);
		startVerification();
				
		//Initialize the test object
		CodesTableInitializerImpl codesTableInitializer = new CodesTableInitializerImpl();
		codesTableInitializer.setCodesTablePeriodicClasses(ctip);
		codesTableInitializer.setCodesTableManager(mockCodesTableManager);
		
		//Test the test objects method
		try{
			codesTableInitializer.init();
		} catch(Exception ex){
			fail("Unexptected exception " +ex);
		}	
	}
	
	/**
	 * Tests CodesTableInitializer which empty <code>codesTableClass</code>es and <code>codesTablePeriodicClass</code>es.
	 * @throws Exception
	 */
	public void testCodesTableInitializerEmptyClassLists() throws Exception { 
						
		//Initialize the test object
		CodesTableInitializerImpl codesTableInitializer = new CodesTableInitializerImpl();

		try{
			codesTableInitializer.init();
			fail("Expected exception");
		} catch(UnrecoverableException ex){
			assertEquals("Test 1: Expected exception ", "No CodesTables or CodesTablePeriodics have been set", ex.getMessage());
		}
	}
	
	/**
	 * Tests CodesTableInitializer with <code>CodesTableManger</code> returning null 
	 * because a <code>CodesTable</code> or a <code>CodesTablePeriodic</code> cannot be 
	 * retrieved from the database.
	 * @throws Exception
	 */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public void testCodesTableInitializerModifyCodesTableMangersReturnValue() throws Exception { 
		//Test data
		CodesTable codesTable = new CodesTableImpl();
		codesTable.addCodesTableItem(TestCodesTableItem.CTI1);
		codesTable.addCodesTableItem(TestCodesTableItem.CTI2);
		
		CodesTablePeriodic codesTablePeriodic = new CodesTablePeriodicImpl();
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.CTIP1);
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.CTIP2);
		
		List<Class<CodesTableItem>> cti = new ArrayList<Class<CodesTableItem>>();
		cti.add(null);
		
		List<Class<CodesTableItemPeriodic>> ctip = new ArrayList<Class<CodesTableItemPeriodic>>();
		ctip.add(CodesTableItemPeriodic.class);
		
		//Mock CodesTableMangers method
		CodesTableManager mockCodesTableManager = (CodesTableManager) mock(CodesTableManager.class);
		mockCodesTableManager.getCodesTable(TestCodesTableItem.CTI1.getClass());
		modify().args(is.ANYTHING);
		modify().returnValue(codesTable);
		
		mockCodesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass());
		modify().args(is.ANYTHING);
		modify().returnValue(null);
		startVerification();	
		
		//Initialize the test object
		CodesTableInitializerImpl codesTableInitializer = new CodesTableInitializerImpl();
		codesTableInitializer.setCodesTableClasses(cti);
		codesTableInitializer.setCodesTablePeriodicClasses(ctip);
		codesTableInitializer.setCodesTableManager(mockCodesTableManager);
		
		//Test the test objects method
		try{
			codesTableInitializer.init();
		} catch(Exception ex){
			System.out.println("ex.getMessage "+ ex.getMessage());
			assertEquals("Test 1: Expected exception ", codesTablePeriodic +" is not a codestable", ex.getMessage());
		}		
	}	
}