package no.stelvio.common.codestable;

import java.util.Locale;

import no.stelvio.common.context.RequestContext;

import org.apache.commons.collections.Predicate;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

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
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public void onSetUp() throws Exception {
		codesTable = (CodesTable) applicationContext.getBean("codesTable");
		assertNotNull("Couldn't initiate CodesTable using standard POJO implementation.", codesTable);
		
		codesTable.addCodesTableItem(TestCodesTableItem.CTI1);
		codesTable.addCodesTableItem(TestCodesTableItem.CTI2);
		codesTable.addCodesTableItem(TestCodesTableItem.CTI3);
		
	}
	
	/**
	 * Test of getCodesTableItem().
	 */
	public void testGetCodesTableItem(){
		
		//Test: get an item that does not exist
		assertNull("Test 1 : A null-value should have been returned" , codesTable.getCodesTableItem("t8code15")); 

		//Test: get an item
		CodesTableItem itemExist = codesTable.getCodesTableItem("t1code1");
		assertNotNull("Test 2: the item does not exist", itemExist);
		assertEquals("Test 3: Unexpected code", "t1code1", itemExist.getCode());
		assertEquals("Test 4: Unexpected decode", "t1decode1", itemExist.getDecode());	

	}
		
	/**
	 * Test of addPredicate(Predicate predicate).
	 */
	public void testAddPredicateAndRemovePredicate(){
		
		Predicate pred1 = new Predicate(){
			public boolean evaluate(Object object) {
				CodesTableItem codesTableItem = (CodesTableItem)object;
				
				Locale locale = new Locale("nb", "NO");
				
				return codesTableItem.getLocale().equals(locale);
			}
		};

		Predicate pred2 = new Predicate(){
			public boolean evaluate(Object object) {
				CodesTableItem codesTableItem = (CodesTableItem)object;
				
				return codesTableItem.getCode().toString().startsWith("t1");
			}
		};
		
		//Test: get the items in the codestable 
		assertNotNull("Test 1: the item does not exist", codesTable.getCodesTableItem("t1code1"));
		assertNotNull("Test 2: the item does not exist", codesTable.getCodesTableItem("t2code2"));
		assertNotNull("Test 3: the item does not exist", codesTable.getCodesTableItem("t3code3"));
		
		codesTable.addPredicate(pred1);
		
		//Test: get an item that does not exist
		assertNull("Test 4 : A null-value should have been returned" , codesTable.getCodesTableItem("t3code3")); 
		
		//Test: get items in the filtered codestable
		assertNotNull("Test 5: the item does not exist", codesTable.getCodesTableItem("t1code1"));
		assertNotNull("Test 6: the item does not exist", codesTable.getCodesTableItem("t2code2"));
		
		codesTable.addPredicate(pred2);
		
		//Test: get an item that does not exist
		assertNull("Test 7 : A null-value should have been returned" , codesTable.getCodesTableItem("t2code2"));
		
		//Test: get items in the filtered codestable
		assertNotNull("Test 8: the item does not exist", codesTable.getCodesTableItem("t1code1"));
		
		codesTable.resetPrediacte();
		
		//Test: get the items in the codestable 
		assertNotNull("Test 9: the item does not exist", codesTable.getCodesTableItem("t1code1"));
		assertNotNull("Test 10: the item does not exist", codesTable.getCodesTableItem("t2code2"));
		assertNotNull("Test 11: the item does not exist", codesTable.getCodesTableItem("t3code3"));
	}
	
	/**
	 * Test of getDecode(Object code).
	 */
	public void testGetDecodeWithCode(){
		//Test: get a decode for a code that does not exist
		assertNull("Test 1 : A null-value should have been returned" , codesTable.getDecode("t8code15"));
		
		//Set the locale in the RequestContext
		Locale locale = new Locale("nb", "NO");
		RequestContext.setLocale(locale);
		
		//Test: get a decode
		String decode = codesTable.getDecode(TestCodesTableItem.CTI1.getCode());
		assertNotNull("Test 2: decode not found", decode);
		assertEquals("Test 3: unexptected decode", "t1decode1", decode);
	}
	
	/**
	 * Test of getDecode(Object code, Locale locale).
	 */
	public void testGetDecodeWithCodeAndLocale(){
		Locale locale = new Locale("nb", "NO");
		
		//Test: get a decode for a code that does not exist
		assertNull("Test 1 : A null-value should have been returned" , codesTable.getDecode("t8code15"));

		//Test: get a decode
		String decode = codesTable.getDecode(TestCodesTableItem.CTI1.getCode(), locale);
		assertNotNull("Test 2: decode not found", decode);
		assertEquals("Test 3: unexptected decode", "t1decode1", decode);
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