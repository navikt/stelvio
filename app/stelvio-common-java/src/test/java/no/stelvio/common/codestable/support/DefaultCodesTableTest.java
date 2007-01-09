package no.stelvio.common.codestable.support;

import java.util.Locale;

import org.apache.commons.collections.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.TestCodesTableItem;
import no.stelvio.common.context.RequestContext;

/**
 * Unit test of CodesTable.
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class DefaultCodesTableTest extends AbstractDependencyInjectionSpringContextTests {

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
	@Before
	public void onSetUp() throws Exception {
		codesTable = (CodesTable) applicationContext.getBean("codesTable");
		assertNotNull("Couldn't initiate CodesTable using standard POJO implementation.", codesTable);
		
		codesTable.addCodesTableItem(TestCodesTableItem.getCti1());
		codesTable.addCodesTableItem(TestCodesTableItem.getCti2());
		codesTable.addCodesTableItem(TestCodesTableItem.getCti3());
		
	}
	
	/**
	 * Test of getCodesTableItem().
	 */
	@Test
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
	@Test
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
		
		codesTable.resetPredicates();
		
		//Test: get the items in the codestable 
		assertNotNull("Test 9: the item does not exist", codesTable.getCodesTableItem("t1code1"));
		assertNotNull("Test 10: the item does not exist", codesTable.getCodesTableItem("t2code2"));
		assertNotNull("Test 11: the item does not exist", codesTable.getCodesTableItem("t3code3"));
	}
	
	/**
	 * Test of getDecode(Object code).
	 */
	@Test
	public void testGetDecodeWithCode(){

		//Set the locale in the RequestContext
		Locale locale = new Locale("nb", "NO");
		RequestContext.setLocale(locale);
		
		//Test: get a decode for a code that does not exist
		try{
			codesTable.getDecode("t8code15");
			fail("Expected exception");	
		} catch(Exception ex){
			assertEquals("Test 1: getDecode() should have thrown exception",ex.getClass().getSimpleName(), "DecodeNotFoundException");
		}

		//Test: get a decode
		assertEquals("Test 3: unexptected decode", codesTable.getDecode(TestCodesTableItem.getCti1().getCode()), "t1decode1");
	}
	
	/**
	 * Test of getDecode(Object code, Locale locale).
	 */
	@Test
	public void testGetDecodeWithCodeAndLocale(){
		
		Locale locale = new Locale("nb", "NO");
			
		//Test: get a decode for a code that does not exist
		try{
			codesTable.getDecode("t8code15", locale);
			fail("Expected exception");	
		} catch(Exception ex){
			assertEquals("Test 1: getDecode() should have thrown exception",ex.getClass().getSimpleName(), "DecodeNotFoundException");
		}

		//Test: get a decode
		assertEquals("Test 3: unexptected decode", codesTable.getDecode(TestCodesTableItem.getCti1().getCode(), locale) , "t1decode1");
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