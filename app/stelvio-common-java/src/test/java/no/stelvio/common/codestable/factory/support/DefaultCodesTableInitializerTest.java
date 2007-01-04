package no.stelvio.common.codestable.factory.support;


import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsAnything;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.TestCodesTableItem;
import no.stelvio.common.codestable.TestCodesTableItemPeriodic;
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
		final CodesTable codesTable = new DefaultCodesTable();
		codesTable.addCodesTableItem(TestCodesTableItem.getCti1());
		codesTable.addCodesTableItem(TestCodesTableItem.getCti2());
		
		final CodesTablePeriodic codesTablePeriodic = new DefaultCodesTablePeriodic();
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.getCtip1());
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.getCtip2());
		
		List<Class<CodesTableItem>> cti = new ArrayList<Class<CodesTableItem>>();
		cti.add(CodesTableItem.class);
		
		List<Class<CodesTableItemPeriodic>> ctip = new ArrayList<Class<CodesTableItemPeriodic>>();
		ctip.add(CodesTableItemPeriodic.class);

		Mockery context = new Mockery();
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
			one(codesTableManager).getCodesTable((Class<CodesTableItem>) with(IsAnything.anything()));
			will(returnValue(codesTable));
			one(codesTableManager).getCodesTablePeriodic((Class<CodesTableItemPeriodic>) with(IsAnything.anything()));
			will(returnValue(codesTablePeriodic));
		}});

		//Initialize the test object
		DefaultCodesTableInitializer codesTableInitializer = new DefaultCodesTableInitializer();
		codesTableInitializer.setCodesTableClasses(cti);
		codesTableInitializer.setCodesTablePeriodicClasses(ctip);
		codesTableInitializer.setCodesTableManager(codesTableManager);
		
		//Test the test objects method
		codesTableInitializer.init();
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
		final CodesTable codesTable = new DefaultCodesTable();
		codesTable.addCodesTableItem(TestCodesTableItem.getCti1());
		codesTable.addCodesTableItem(TestCodesTableItem.getCti2());
		
		List<Class<CodesTableItem>> cti = new ArrayList<Class<CodesTableItem>>();
		cti.add(CodesTableItem.class);
		
		Mockery context = new Mockery();
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
			one(codesTableManager).getCodesTable((Class<CodesTableItem>) with(IsAnything.anything()));
			will(returnValue(codesTable));
		}});

		//Initialize the test object
		DefaultCodesTableInitializer codesTableInitializer = new DefaultCodesTableInitializer();
		codesTableInitializer.setCodesTableClasses(cti);
		codesTableInitializer.setCodesTableManager(codesTableManager);
		
		//Test the test objects method
		codesTableInitializer.init();
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests CodesTableInitializer with both only <code>CodesTableItemPeriodic</code>'s.
	 * @throws Exception
	 */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	public void testCodesTableInitializerGetCodesTablePeriodic() throws Exception{
		//Test data		
		final CodesTablePeriodic codesTablePeriodic = new DefaultCodesTablePeriodic();
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.getCtip1());
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.getCtip2());
		
		List<Class<CodesTableItemPeriodic>> ctip = new ArrayList<Class<CodesTableItemPeriodic>>();
		ctip.add(CodesTableItemPeriodic.class);

		Mockery context = new Mockery();
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
			one(codesTableManager).getCodesTablePeriodic((Class<CodesTableItemPeriodic>) with(IsAnything.anything()));
			will(returnValue(codesTablePeriodic));
		}});

		//Initialize the test object
		DefaultCodesTableInitializer codesTableInitializer = new DefaultCodesTableInitializer();
		codesTableInitializer.setCodesTablePeriodicClasses(ctip);
		codesTableInitializer.setCodesTableManager(codesTableManager);
		
		//Test the test objects method
		codesTableInitializer.init();
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
			codesTableInitializer.init();
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
		final CodesTable codesTable = new DefaultCodesTable();
		codesTable.addCodesTableItem(TestCodesTableItem.getCti1());
		codesTable.addCodesTableItem(TestCodesTableItem.getCti2());
		
		CodesTablePeriodic codesTablePeriodic = new DefaultCodesTablePeriodic();
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.getCtip1());
		codesTablePeriodic.addCodesTableItem(TestCodesTableItemPeriodic.getCtip2());
		
		List<Class<CodesTableItem>> cti = new ArrayList<Class<CodesTableItem>>();
		cti.add(null);
		
		List<Class<CodesTableItemPeriodic>> ctip = new ArrayList<Class<CodesTableItemPeriodic>>();
		ctip.add(CodesTableItemPeriodic.class);

		Mockery context = new Mockery();
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		//Mock CodesTableManger's methods
		context.expects(new InAnyOrder() {{
			one(codesTableManager).getCodesTable((Class<CodesTableItem>) with(IsAnything.anything()));
			will(returnValue(codesTable));
			one(codesTableManager).getCodesTablePeriodic((Class<CodesTableItemPeriodic>) with(IsAnything.anything()));
			will(returnValue(null));
		}});

		//Initialize the test object
		DefaultCodesTableInitializer codesTableInitializer = new DefaultCodesTableInitializer();
		codesTableInitializer.setCodesTableClasses(cti);
		codesTableInitializer.setCodesTablePeriodicClasses(ctip);
		codesTableInitializer.setCodesTableManager(codesTableManager);
		
		//Test the test objects method
		try{
			codesTableInitializer.init();
			fail("Exptected exception");
		} catch(Exception ex){
			assertEquals("Test 1: Expected exception ", ex.getClass().getSimpleName(), "CodesTableNotFoundException");
		}

		context.assertIsSatisfied();
	}
}