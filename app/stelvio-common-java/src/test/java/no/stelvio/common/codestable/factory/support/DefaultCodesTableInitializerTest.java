package no.stelvio.common.codestable.factory.support;


import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsAnything;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtiPeriodic;
import no.stelvio.common.codestable.support.DefaultCodesTable;
import no.stelvio.common.codestable.support.DefaultCodesTablePeriodic;

/**
 * Unit test of CodesTableInitializer.
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */
public class DefaultCodesTableInitializerTest {

	/**
	 * Tests CodesTableInitializer with both <code>CodesTableItem</code>'s and <code>CodesTableItemPeriodic</code>'s.
	 * @throws Exception
	 */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public void testCodesTableInitializer() throws Exception {
		
		//Test data
		List<TestCti> codesTableItems = new ArrayList<TestCti>();
		codesTableItems.add(TestCti.getCti1());
		codesTableItems.add(TestCti.getCti2());
		final CodesTable codesTable = new DefaultCodesTable(codesTableItems);

		List<TestCtiPeriodic> codesTableItemPeriodics = new ArrayList<TestCtiPeriodic>();
		codesTableItemPeriodics.add(TestCtiPeriodic.getCtip1());
		codesTableItemPeriodics.add(TestCtiPeriodic.getCtip2());
		final CodesTablePeriodic codesTablePeriodic = new DefaultCodesTablePeriodic(codesTableItemPeriodics);
		
		List<Class<TestCti>> cti = new ArrayList<Class<TestCti>>();
		cti.add(TestCti.class);
		
		List<Class<TestCtiPeriodic>> ctip = new ArrayList<Class<TestCtiPeriodic>>();
		ctip.add(TestCtiPeriodic.class);

		Mockery context = new Mockery();
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
			one(codesTableManager).getCodesTable((Class<TestCti>) with(IsAnything.anything()));
			will(returnValue(codesTable));
			one(codesTableManager).getCodesTablePeriodic((Class<TestCtiPeriodic>) with(IsAnything.anything()));
			will(returnValue(codesTablePeriodic));
		}});

		//Initialize the test object
		DefaultCodesTableInitializer codesTableInitializer = new DefaultCodesTableInitializer();
		// TODO how to do this?
//		codesTableInitializer.setCodesTableClasses(cti);
//		codesTableInitializer.setCodesTablePeriodicClasses(ctip);
		codesTableInitializer.setCodesTableManager(codesTableManager);
		
		//Test the test objects method
		codesTableInitializer.afterPropertiesSet();
		context.assertIsSatisfied();
	}

	/**
	 * Tests CodesTableInitializer with only <code>CodesTableItem</code>'s.
	 * @throws Exception
	 */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public void testCodesTableInitializerGetCodesTable() throws Exception {
		//Test data
		ArrayList<TestCti> codesTableItems = new ArrayList<TestCti>();
		codesTableItems.add(TestCti.getCti1());
		codesTableItems.add(TestCti.getCti2());
		final CodesTable codesTable = new DefaultCodesTable(codesTableItems);
		
		List<Class<TestCti>> cti = new ArrayList<Class<TestCti>>();
		cti.add(TestCti.class);
		
		Mockery context = new Mockery();
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
			one(codesTableManager).getCodesTable((Class<TestCti>) with(IsAnything.anything()));
			will(returnValue(codesTable));
		}});

		//Initialize the test object
		DefaultCodesTableInitializer codesTableInitializer = new DefaultCodesTableInitializer();
		// TODO how to do this?
//		codesTableInitializer.setCodesTableClasses(cti);
		codesTableInitializer.setCodesTableManager(codesTableManager);
		
		//Test the test objects method
		codesTableInitializer.afterPropertiesSet();
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests CodesTableInitializer with both only <code>CodesTableItemPeriodic</code>'s.
	 * @throws Exception
	 */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public void testCodesTableInitializerGetCodesTablePeriodic() throws Exception {
		//Test data
		List<TestCtiPeriodic> codesTableItemPeriodics = new ArrayList<TestCtiPeriodic>();
		codesTableItemPeriodics.add(TestCtiPeriodic.getCtip1());
		codesTableItemPeriodics.add(TestCtiPeriodic.getCtip2());
		final CodesTablePeriodic codesTablePeriodic = new DefaultCodesTablePeriodic(codesTableItemPeriodics);
		
		List<Class<TestCtiPeriodic>> ctip = new ArrayList<Class<TestCtiPeriodic>>();
		ctip.add(TestCtiPeriodic.class);

		Mockery context = new Mockery();
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
			one(codesTableManager).getCodesTablePeriodic((Class<TestCtiPeriodic>) with(IsAnything.anything()));
			will(returnValue(codesTablePeriodic));
		}});

		//Initialize the test object
		DefaultCodesTableInitializer codesTableInitializer = new DefaultCodesTableInitializer();
		// TODO how to do this?
//		codesTableInitializer.setCodesTablePeriodicClasses(ctip);
		codesTableInitializer.setCodesTableManager(codesTableManager);
		
		//Test the test objects method
		codesTableInitializer.afterPropertiesSet();
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests CodesTableInitializer which empty <code>codesTableClass</code>es and <code>codesTablePeriodicClass</code>es.
	 * @throws Exception
	 */
	public void testCodesTableInitializerEmptyClassLists() throws Exception { 
						
		//Initialize the test object
		DefaultCodesTableInitializer codesTableInitializer = new DefaultCodesTableInitializer();

		try{
			codesTableInitializer.afterPropertiesSet();
			fail("Expected exception");
		} catch(Exception ex){
			assertEquals("Test 1: Expected exception ", ex.getClass().getSimpleName(), "CodesTableConfigurationException" );
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
		ArrayList<TestCti> codesTableItems = new ArrayList<TestCti>();
		codesTableItems.add(TestCti.getCti1());
		codesTableItems.add(TestCti.getCti2());
		final CodesTable codesTable = new DefaultCodesTable(codesTableItems);
		
		ArrayList<TestCtiPeriodic> codesTableItemPeriodics = new ArrayList<TestCtiPeriodic>();
		codesTableItemPeriodics.add(TestCtiPeriodic.getCtip1());
		codesTableItemPeriodics.add(TestCtiPeriodic.getCtip2());
		CodesTablePeriodic codesTablePeriodic = new DefaultCodesTablePeriodic(codesTableItemPeriodics);
		
		List<Class<TestCti>> cti = new ArrayList<Class<TestCti>>();
		cti.add(null);
		
		List<Class<TestCtiPeriodic>> ctip = new ArrayList<Class<TestCtiPeriodic>>();
		ctip.add(TestCtiPeriodic.class);

		Mockery context = new Mockery();
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
			one(codesTableManager).getCodesTable((Class<TestCti>) with(IsAnything.anything()));
			will(returnValue(codesTable));
			one(codesTableManager).getCodesTablePeriodic((Class<TestCtiPeriodic>) with(IsAnything.anything()));
			will(returnValue(null));
		}});

		//Initialize the test object
		DefaultCodesTableInitializer codesTableInitializer = new DefaultCodesTableInitializer();
		// TODO how to do this?
//		codesTableInitializer.setCodesTableClasses(cti);
//		codesTableInitializer.setCodesTablePeriodicClasses(ctip);
		codesTableInitializer.setCodesTableManager(codesTableManager);
		
		//Test the test objects method
		try{
			codesTableInitializer.afterPropertiesSet();
			fail("Exptected exception");
		} catch(Exception ex){
			assertEquals("Test 1: Expected exception ", ex.getClass().getSimpleName(), "CodesTableNotFoundException");
		}

		context.assertIsSatisfied();
	}
}