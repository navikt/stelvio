package no.stelvio.common.codestable;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * Unit test of CodesTableFactory.
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class CodesTableFactoryTest extends MockObjectTestCase{

	/** Implementation class to test */
	private CodesTableFactory codesTableFactory;
	
	private CodesTable codesTable;
	private CodesTable codesTable1;
	
	private Mock mockRetriever;
	
	/**
	 * Initialize components prior to running tests.
	 */
	public void test() throws Exception {
		codesTableFactory = new CodesTableFactoryImpl();
		assertNotNull("Couldn't initiate CodesTableFactory.", codesTableFactory);
		
		codesTable = new CodesTableImpl();
		assertNotNull("Couldn't initiate CodesTable.", codesTable);
		
		codesTable.addCodesTableItem(TestCodesTableItem.CTI1);
		codesTable.addCodesTableItem(TestCodesTableItem.CTI2);
		codesTable.addCodesTableItem(TestCodesTableItem.CTI3);
		
		codesTable1 = new CodesTableImpl();
		assertNotNull("Couldn't initiate CodesTable.", codesTable);
		
		mockRetriever = mock(CodesTableRetriever.class);
		
		System.out.println("Navn " +codesTable.getClass().getName());
		System.out.println("navn1 " +codesTable);
		
		mockRetriever.expects(once()).method("retrieve").with(eq(codesTable.getClass())).will(returnValue(codesTable));
				
		codesTable1 = codesTableFactory.retrieveCodesTable(codesTable.getClass());
		
		assertNotNull("Should not be null", codesTable1);
		
	}
	
	/**
	 * Test of <code>retrieveCodesTable(Class<CodesTable> codesTable)</code>;
	 */
	/*
	public void testRetrieveCodesTable() {
							
		mockRetriever.expects(once()).method("retrieve").will(returnValue(codesTable));
		
		codesTable1 = codesTableFactory.retrieveCodesTable(codesTable.getClass());
		
		assertNotNull("Should not be null", codesTable1);
		//assertEquals("Does not contain the expected code", "t1decode1", codesTable1.get("t1code1"));
		//assertNull("Contains an unexpected value", codesTable.get("t2decode1"));
		
	}
	*/
}