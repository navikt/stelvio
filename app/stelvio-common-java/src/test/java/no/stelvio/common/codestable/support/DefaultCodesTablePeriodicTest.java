package no.stelvio.common.codestable.support;


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.collections.Predicate;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.TestCodesTableItem;
import no.stelvio.common.codestable.TestCodesTableItemPeriodic;
import no.stelvio.common.context.RequestContext;

/**
 * Unit test of CodesTablePeriodic.
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class DefaultCodesTablePeriodicTest extends AbstractDependencyInjectionSpringContextTests {

	/** Implementation class to test */
	private CodesTablePeriodic codesTablePeriodic;
	
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
		codesTablePeriodic = (CodesTablePeriodic) applicationContext.getBean("codesTablePeriodic");
		assertNotNull("Couldn't initiate CodesTablePeriodic using standard POJO implementation.", codesTablePeriodic);
		
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.CTIP1);
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.CTIP2);
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.CTIP3);
	}
	
	/**
	 * Test of getCodesTableItem().
	 */
	public void testGetCodesTableItem(){
		//Test: get an item that does not exist
		assertNull("Test 1 : A null-value should have been returned" , codesTablePeriodic.getCodesTableItem("t8code15"));

		//Test: get an item
		CodesTableItemPeriodic itemExist = codesTablePeriodic.getCodesTableItem("t1code1");
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
				CodesTableItemPeriodic codesTableItemPeriodic = (CodesTableItemPeriodic)object;
				
				Locale locale = new Locale("nb", "NO");
				
				return codesTableItemPeriodic.getLocale().equals(locale);
			}
		};

		Predicate pred2 = new Predicate(){
			public boolean evaluate(Object object) {
				CodesTableItemPeriodic codesTableItemPeriodic = (CodesTableItemPeriodic)object;
				
				return codesTableItemPeriodic.getCode().toString().startsWith("t1");
			}
		};
		
		//Test: get the items in the codestable 
		assertNotNull("Test 1: the item does not exist", codesTablePeriodic.getCodesTableItem("t1code1"));
		assertNotNull("Test 2: the item does not exist", codesTablePeriodic.getCodesTableItem("t2code2"));
		assertNotNull("Test 3: the item does not exist", codesTablePeriodic.getCodesTableItem("t3code3"));
		
		codesTablePeriodic.addPredicate(pred1);
		
		//Test: get an item that does not exist
		assertNull("Test 4 : A null-value should have been returned" , codesTablePeriodic.getCodesTableItem("t3code3")); 
		
		//Test: get items in the filtered codestable
		assertNotNull("Test 5: the item does not exist", codesTablePeriodic.getCodesTableItem("t1code1"));
		assertNotNull("Test 6: the item does not exist", codesTablePeriodic.getCodesTableItem("t2code2"));
		
		codesTablePeriodic.addPredicate(pred2);
		
		//Test: get an item that does not exist
		assertNull("Test 7 : A null-value should have been returned" , codesTablePeriodic.getCodesTableItem("t2code2"));
		
		//Test: get items in the filtered codestable
		assertNotNull("Test 8: the item does not exist", codesTablePeriodic.getCodesTableItem("t1code1"));
		
		codesTablePeriodic.resetPredicates();
		
		//Test: get the items in the codestable 
		assertNotNull("Test 9: the item does not exist", codesTablePeriodic.getCodesTableItem("t1code1"));
		assertNotNull("Test 10: the item does not exist", codesTablePeriodic.getCodesTableItem("t2code2"));
		assertNotNull("Test 11: the item does not exist", codesTablePeriodic.getCodesTableItem("t3code3"));
	}
	
	/**
	 * Test of getDecode(Object code, Date date).
	 */
	public void testGetDecodeWithCode(){
		
		Calendar cal = Calendar.getInstance();
		cal.set(106, 10, 15);
		Date date = new Date(cal.getTimeInMillis());
		
		//Set the locale in the RequestContext
		Locale locale = new Locale("nb", "NO");
		RequestContext.setLocale(locale);
		
		try{
			codesTablePeriodic.getDecode("t8code15", date);
			fail("Expected exception");	
		} catch(Exception ex){
			assertEquals("Test 1: getDecode() should have thrown exception",ex.getClass().getSimpleName(), "DecodeNotFoundException");
		}

		assertEquals("Test 3: unexptected decode", codesTablePeriodic.getDecode(TestCodesTableItemPeriodic.CTIP1.getCode(), date), "t1decode1");
	}
	
	/**
	 * Test of getDecode(Object code, Locale locale, Date date).
	 */
	public void testGetDecodeWithCodeAndLocale(){
		Locale locale = new Locale("nb", "NO");
		
		Calendar cal = Calendar.getInstance();
		cal.set(106, 10, 15);
		Date date = new Date(cal.getTimeInMillis());
	
		try{
			codesTablePeriodic.getDecode("t8code15", locale, date);
			fail("Expected exception");	
		} catch(Exception ex){
			assertEquals("Test 1: getDecode() should have thrown exception",ex.getClass().getSimpleName(), "DecodeNotFoundException");
		}

		assertEquals("Test 3: unexptected decode", codesTablePeriodic.getDecode(TestCodesTableItem.CTI1.getCode(), locale, date), "t1decode1");
	}
	
	/**
	 * Cleans up after tests are complete.
	 */
	@Override
	public void onTearDown() {
		codesTablePeriodic = null;
		setDirty();
	}	
}