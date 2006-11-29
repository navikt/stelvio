package no.stelvio.common.codestable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import no.stelvio.common.context.RequestContext;

import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableImpl;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.CodesTablePeriodicImpl;
import no.stelvio.common.codestable.CodesTableManagerImpl;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableFactory;

import com.agical.rmock.extension.junit.RMockTestCase;

/**
 * Component test of Codestable. 
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class CodestableComponentTest extends RMockTestCase {

	//
	private final String code = TestCodesTableItem.CTI1.getCode();
	//
	private final String decode = TestCodesTableItem.CTI1.getDecode();
	//
	private final Locale locale = new Locale("nb", "NO");	
	//
	private Date date ;
	//
	private CodesTableManagerImpl codesTableManager = new CodesTableManagerImpl();	
	
	@Before
	public void setUp(){
		//Test data			
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
		modify().multiplicity(expect.atLeast(0));
		mockCodesTableFactory.retrieveCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass());
		modify().args(is.ANYTHING);
		modify().returnValue(codesTableItemPeriodics);
		modify().multiplicity(expect.atLeast(0));
		startVerification();
				
		//Initialize the test object
		this.codesTableManager.setCodesTableFactory(mockCodesTableFactory);
		
		Calendar cal = Calendar.getInstance();
		cal.set(106, 10, 15);
		this.date = new Date(cal.getTimeInMillis());
	}
	
	/**
	 * Con001.001
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testCodesTable() throws Exception{	
	
		CodesTable codesTable = new CodesTableImpl();
		codesTable = codesTableManager.getCodesTable(TestCodesTableItem.CTI1.getClass());
		
		assertEquals("Con001.001 : getCodesTable failed - doesn't hold an expected value", codesTable.getCodesTableItem(code), TestCodesTableItem.CTI1);	
	}

	/**
	 * Con002.001
	 */
    @SuppressWarnings("unchecked")
    @Test
	public void testGetDecode(){
    	//Sets the locale in the request context
    	RequestContext.setLocale(locale);
    	
    	assertEquals("Con004.001 : getDecode() failed ", codesTableManager.getCodesTable(TestCodesTableItem.CTI1.getClass()).getDecode(code), decode);
    }

    /**
     * Con003.001
     */
	@Test
    public void testGetDecodeWithNonexistingCode(){
    	String nonExistingCode = "t12code";
    	
    	try{
    		codesTableManager.getCodesTable(TestCodesTableItem.CTI1.getClass()).getDecode(nonExistingCode);
			fail("Con003.001 : Expected exception for getDecode()");
    	}catch(Exception ex){
    		assertEquals("Con003.01 : getDecode() should have thrown exception",ex.getClass().getSimpleName(), "DecodeNotFoundException");
    	}
    }

	/**
	 * Con004.001
	 */
	@Test
    public void testGetDecodeWithLocale(){
		
		assertEquals("Con004.001 : getDecode() with locale failed ", codesTableManager.getCodesTable(TestCodesTableItem.CTI1.getClass()).getDecode(code, locale), decode);	
    }

	/**
	 * Con005.001
	 */
	@Test
    public void testGetDecodeWithNonexistingLocale(){
    	
    	Locale nonSupportedLocale = new Locale("en", "GB");
    	
    	assertEquals("Con005.001 : getDecode() with non-existing locale failed ", codesTableManager.getCodesTable(TestCodesTableItem.CTI1.getClass()).getDecode(code, nonSupportedLocale), decode);
    }

	/**
	 * Con007.001
	 */
	@Test
    public void testGetCodesTablePeriodic(){
    	
    	CodesTablePeriodic codesTablePeriodic = new CodesTablePeriodicImpl();
		codesTablePeriodic = codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass());
		
		assertEquals("Con007.001 : getCodesTablePeriodic failed - doesn't hold an expected value", codesTablePeriodic.getCodesTableItem(code), TestCodesTableItemPeriodic.CTIP1);	
    }
    
	/**
	 * Con008.01
	 */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetDecodePeriodicWithNonvalidDate(){
    	//Invalid date
    	Calendar cal = Calendar.getInstance();
		cal.set(106, 9, 15);
		Date nonvalidDate = new Date(cal.getTimeInMillis());
		
    	RequestContext.setLocale(locale);
    	
    	try{
    		codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass()).getDecode(code, nonvalidDate);
			fail("Con008.001 : Expected exception for getDecode()");
    	}catch(Exception ex){
    		assertEquals("Con008.01 : getDecode() should have thrown exception",ex.getClass().getSimpleName(), "DecodeNotFoundException");
    	}
    }

    /**
     * Con009.001
     */
	@Test
    public void testGetDecodePeriodic(){	
    	//Sets the locale in the request context
    	RequestContext.setLocale(locale);
    	
    	assertEquals("Con009.001 : getDecode() failed ", codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass()).getDecode(code, date), decode);
    }
   
	/**
	 * Con010.001
	 */
	@Test
    public void testGetDecodePeriodicWithNonexistingCode(){   	
    	
    	String nonExistingCode = "t12code";
    	
    	try{
    		codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass()).getDecode(nonExistingCode, date);
			fail("Con010.001 : Expected exception for getDecode()");
    	}catch(Exception ex){
    		assertEquals("Con010.001 : getDecode() should have thrown exception",ex.getClass().getSimpleName(), "DecodeNotFoundException");
    	}
    }
    
	/**
	 * Con011.001
	 */
	@Test
    public void testGetDecodePeriodicWithLocale(){
    	assertEquals("Test : getDecode() with locale failed ", codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass()).getDecode(code, locale, date), decode);
    }

	/**
	 * Con012.001
	 */
	@Test
    public void testGetDecodePeriodicWithNonexistingLocale(){
    	
    	Locale nonSupportedLocale = new Locale("en", "GB");
    	
    	assertEquals("Test : getDecode() with locale failed ", codesTableManager.getCodesTablePeriodic(TestCodesTableItemPeriodic.CTIP1.getClass()).getDecode(code, nonSupportedLocale, date), decode);
    }
}