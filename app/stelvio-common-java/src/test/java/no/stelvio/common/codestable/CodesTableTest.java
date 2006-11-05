package no.stelvio.common.codestable;

import java.util.Locale;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import no.stelvio.common.error.SystemException;

/**
 * Unit test of CodesTable.
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class CodesTableTest extends AbstractDependencyInjectionSpringContextTests {

	/** Implementation class to test */
	private CodesTable codesTable;
	
	/**
	 * @return the location of the spring configuration xml-file.
	 */
	@Override
	protected String[] getConfigLocations() {
		return new String[] {
				"common-java_test_codestable_beans.xml"
		};
	}

	/**
	 * Initialize components prior to running tests.
	 */
	/*public void onSetUp() throws Exception {
		codesTable = (CodesTable) applicationContext.getBean("codesTable");
		assertNotNull("Couldn't initiate CodesTable using standard POJO implementation.", codesTable);
		
		codesTable.addCodesTableItem(TestCodesTableItem.CTI1);
		codesTable.addCodesTableItem(TestCodesTableItem.CTI2);
		codesTable.addCodesTableItem(TestCodesTableItem.CTI3);
	}*/
	
	/**
	 * Test of getCodesTableItem().
	 */
	public void testGetCodesTableItem(){
		//Test: get an item that does not exist
		try{
			codesTable.getCodesTableItem("t8code15");
			fail("SystemException should have been thrown");
		}catch(SystemException e){
            // should happen
		}

		//Test: get an item
		/*CodesTableItemI itemExist = codesTable.getCodesTableItem("t1code1");
		assertNotNull("Test 1: the item does not exist", itemExist);
		assertEquals("Test 2: Unexpected code", "t1code1", itemExist.getCode());
		assertEquals("Test 3: Unexpected decode", "t1decode1", itemExist.getDecode());	*/
	}
		
	/**
	 * Test of addPredicate(Predicate predicate).
	 */
//	public void testAddPredicateAndRemovePredicate(){
//		
//		Predicate pred1 = new Predicate(){
//			public boolean evaluate(Object object) {
//				CodesTableItemI codesTableItem = (CodesTableItemI)object;
//				
//				Locale locale = new Locale("nb", "NO");
//				
//				return codesTableItem.getLocale().equals(locale);
//			}
//		};
//
//		Predicate pred2 = new Predicate(){
//			public boolean evaluate(Object object) {
//				CodesTableItemI codesTableItem = (CodesTableItemI)object;
//				
//				return codesTableItem.getCode().toString().startsWith("t1");
//			}
//		};
//		
//		//Test: get the items in the codestable 
//		assertNotNull("Test 1: the item does not exist", codesTable.getCodesTableItem("t1code1"));
//		assertNotNull("Test 2: the item does not exist", codesTable.getCodesTableItem("t2code2"));
//		assertNotNull("Test 3: the item does not exist", codesTable.getCodesTableItem("t3code3"));
//		
//		codesTable.addPredicate(pred1);
//		
//		//Test: get an item that does not exist
//		try{
//			codesTable.getCodesTableItem("t3code3");
//			fail("SystemException should have been thrown");
//		}catch(SystemException e){
//			assertEquals("Unexpected error code", FrameworkError.CODES_TABLE_NOT_FOUND.getCode(), e.getErrorCode());
//		}
//		
//		//Test: get items in the filtered codestable
//		assertNotNull("Test 4: the item does not exist", codesTable.getCodesTableItem("t1code1"));
//		assertNotNull("Test 5: the item does not exist", codesTable.getCodesTableItem("t2code2"));
//		
//		codesTable.addPredicate(pred2);
//		
//		//Test: get an item that does not exist
//		try{
//			codesTable.getCodesTableItem("t2code2");
//			fail("SystemException should have been thrown");
//		}catch(SystemException e){
//			assertEquals("Unexpected error code", FrameworkError.CODES_TABLE_NOT_FOUND.getCode(), e.getErrorCode());
//		}
//		
//		//Test: get items in the filtered codestable
//		assertNotNull("Test 6: the item does not exist", codesTable.getCodesTableItem("t1code1"));
//		
//		codesTable.resetPrediacte();
//		
//		//Test: get the items in the codestable 
//		assertNotNull("Test 7: the item does not exist", codesTable.getCodesTableItem("t1code1"));
//		assertNotNull("Test 8: the item does not exist", codesTable.getCodesTableItem("t2code2"));
//		assertNotNull("Test 9: the item does not exist", codesTable.getCodesTableItem("t3code3"));	
//	}
		
	/**
	 * Test of getDecode(Object code).
	 */
	public void testGetDecodeWithCode(){
		//Test: get a decode for a code that does not exist
		try{
			codesTable.getDecode("t8code15");
			fail("SystemException should have been thrown");
		}catch(SystemException e){
            // should happen
		}

		//Test: get a decode
		String decode = codesTable.getDecode(TestCodesTableItem.CTI1.getCode());
		assertNotNull("Test 1: decode not found", decode);
		assertEquals("Test 2: unexptected decode", "t1decode1", decode);
	}
	
	/**
	 * Test of getDecode(Object code, Locale locale).
	 */
	public void testGetDecodeWithCodeAndLocale(){
		Locale locale = new Locale("nb", "NO");
		
		//Test: get a decode for a code that does not exist
		try{
			codesTable.getDecode("t8code15");
			fail("SystemException should have been thrown");
		}catch(SystemException e){
            // should happen
		}

		//Test: get a decode
		String decode = codesTable.getDecode(TestCodesTableItem.CTI1.getCode(), locale);
		assertNotNull("Test 1: decode not found", decode);
		assertEquals("Test 2: unexptected decode", "t1decode1", decode);
	}
	
	/**
	 * Cleans up after tests are complete.
	 */
	@Override
	public void onTearDown() {
		codesTable = null;
		setDirty();
	}	
}