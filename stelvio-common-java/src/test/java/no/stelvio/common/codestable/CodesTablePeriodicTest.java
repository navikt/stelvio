package no.stelvio.common.codestable;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import no.stelvio.common.FrameworkError;
import no.stelvio.common.error.SystemException;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Unit test of CodesTablePeriodic.
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class CodesTablePeriodicTest extends AbstractDependencyInjectionSpringContextTests {

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
	public void onSetUp() throws Exception {
		codesTablePeriodic = (CodesTablePeriodic) applicationContext.getBean("codesTablePeriodic");
		assertNotNull("Couldn't initiate CodesTablePeriodic using standard POJO implementation.", codesTablePeriodic);
		
		/*codesTablePeriodic.addCodesTableItem(TestCodesTableItem.CTI1);
		codesTablePeriodic.addCodesTableItem(TestCodesTableItem.CTI2);
		codesTablePeriodic.addCodesTableItem(TestCodesTableItem.CTI3);*/
	}
	
	/**
	 * Test of getCodesTableItem().
	 */
	public void testGetCodesTableItem(){
		//Test: get an item that does not exist
		try{
			codesTablePeriodic.getCodesTableItem("t8code15");
			fail("SystemException should have been thrown");
		}catch(SystemException e){
			assertEquals("Unexpected error code", FrameworkError.CODES_TABLE_NOT_FOUND.getCode(), e.getErrorCode());
		}

		//Test: get an item
		/*CodesTableItemI itemExist = codesTablePeriodic.getCodesTableItem("t1code1");
		assertNotNull("Test 1: the item does not exist", itemExist);
		assertEquals("Test 2: Unexpected code", "t1code1", itemExist.getCode());
		assertEquals("Test 3: Unexpected decode", "t1decode1", itemExist.getDecode());	*/
	}
	
	/**
	 * Test of getDecode(Object code, Date date).
	 */
	public void testGetDecodeWithCode(){
		
		Calendar cal = Calendar.getInstance();
		cal.set(106, 10, 15);
		Date date = new Date(cal.getTimeInMillis());
		
		//Test: get a decode for a code that does not exist
		try{
			codesTablePeriodic.getDecode("t8code15", date);
			fail("SystemException should have been thrown");
		}catch(SystemException e){
			assertEquals("Unexpected error code", FrameworkError.CODES_TABLE_NOT_FOUND.getCode(), e.getErrorCode());
		}

		//Test: get a decode
		/*String decode = codesTablePeriodic.getDecode(TestCodesTableItem.CTI1.getCode(), date);
		assertNotNull("Test 1: decode not found", decode);
		assertEquals("Test 2: unexptected decode", "t1decode1", decode);*/
	}
	
	/**
	 * Test of getDecode(Object code, Locale locale, Date date).
	 */
	public void testGetDecodeWithCodeAndLocale(){
		Locale locale = new Locale("nb", "NO");
		
		Calendar cal = Calendar.getInstance();
		cal.set(106, 10, 15);
		Date date = new Date(cal.getTimeInMillis());
		
		//Test: get a decode for a code that does not exist
		try{
			codesTablePeriodic.getDecode("t8code15");
			fail("SystemException should have been thrown");
		}catch(SystemException e){
			assertEquals("Unexpected error code", FrameworkError.CODES_TABLE_NOT_FOUND.getCode(), e.getErrorCode());
		}

		//Test: get a decode
		/*String decode = codesTablePeriodic.getDecode(TestCodesTableItem.CTI1.getCode(), locale, date);
		assertNotNull("Test 1: decode not found", decode);
		assertEquals("Test 2: unexptected decode", "t1decode1", decode);*/
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