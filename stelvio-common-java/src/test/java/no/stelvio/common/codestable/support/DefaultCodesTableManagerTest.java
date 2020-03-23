package no.stelvio.common.codestable.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsSame.sameInstance;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.core.IsAnything;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.NotCodesTableException;
import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtiCode;
import no.stelvio.common.codestable.TestCtpi;
import no.stelvio.common.codestable.TestIdAsKeyCti;
import no.stelvio.common.codestable.TestIdAsKeyCtpi;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;

/**
 * Unit test for {@link DefaultCodesTableManager}.
 * 
 * @version $Id$
 */

public class DefaultCodesTableManagerTest {
	public JUnitRuleMockery context = new JUnitRuleMockery();

	private DefaultCodesTableManager dctm;

	private Method methodGct;

	private Method methodGctp;

	/**
	 * Test notCodesTableExceptionIsThrownWhenInputIsNullForRetrievalOfCodesTable.
	 * 
	 * @throws Throwable
	 *             exception
	 */
	@Test(expected = NotCodesTableException.class)
	public void notCodesTableExceptionIsThrownWhenInputIsNullForRetrievalOfCodesTable() throws Throwable {
		try {
			// Need to use reflection as the generics prohibits compiling a clean version
			methodGct.invoke(dctm, new Object[] { null });
		} catch (InvocationTargetException e) {
			// the real exception is wrapped inside the ite when the method called with reflection throws an exception
			throw e.getCause();
		}
	}

	/**
	 * Test notCodesTableExceptionIsThrownWhenInputIsNullForRetrievalOfCodesTablePeriodic.
	 * 
	 * @throws Throwable
	 *             exception
	 */
	@Test(expected = NotCodesTableException.class)
	public void notCodesTableExceptionIsThrownWhenInputIsNullForRetrievalOfCodesTablePeriodic() throws Throwable {
		try {
			// Need to use reflection as the generics prohibits compiling a clean version
			methodGctp.invoke(dctm, new Object[] { null });
		} catch (InvocationTargetException e) {
			// the real exception is wrapped inside the ite when the method called with reflection throws an exception
			throw e.getCause();
		}
	}

	/**
	 * Test notCodesTableExceptionIsThrownWhenInputIsWrongClassForRetrievalOfCodesTable.
	 * 
	 * @throws Throwable
	 *             exception
	 */
	@Test(expected = NotCodesTableException.class)
	public void notCodesTableExceptionIsThrownWhenInputIsWrongClassForRetrievalOfCodesTable() throws Throwable {
		try {
			// Need to use reflection as the generics prohibits compiling a clean version
			methodGct.invoke(dctm, String.class);
		} catch (InvocationTargetException e) {
			// the real exception is wrapped inside the ite when the method called with reflection throws an exception
			throw e.getCause();
		}
	}

	/**
	 * Test notCodesTableExceptionIsThrownWhenInputIsWrongClassForRetrievalOfCodesTablePeriodic.
	 * 
	 * @throws Throwable
	 *             exception
	 */
	@Test(expected = NotCodesTableException.class)
	public void notCodesTableExceptionIsThrownWhenInputIsWrongClassForRetrievalOfCodesTablePeriodic() throws Throwable {
		try {
			// Need to use reflection as the generics prohibits compiling a clean version
			methodGctp.invoke(dctm, String.class);
		} catch (InvocationTargetException e) {
			// the real exception is wrapped inside the ite when the method called with reflection throws an exception
			throw e.getCause();
		}
	}

	/**
	 * Test that correct codestable is returned.
	 */
	@Test
	public void correctCodesTableIsReturned() {
		CodesTable<TestCti, TestCtiCode, String> codesTable = dctm.getCodesTable(TestCti.class);

		assertThat(codesTable.getCodesTableItems(), is(equalTo(createCodesTableItems())));
	}

	/**
	 * Test that correct codestable periodic is returned.
	 */
	@Test
	public void correctCodesTablePeriodicIsReturned() {
		CodesTablePeriodic<TestCtpi, TestCtiCode, String> codesTablePeriodic = dctm.getCodesTablePeriodic(TestCtpi.class);

		assertThat(codesTablePeriodic.getCodesTableItems(), is(equalTo(createCodesTablePeriodicItems())));
	}

	/**
	 * Test eachCodesTableRetrievalForSameCodesTableShouldReturnANewInstance.
	 */
	@Test
	public void eachCodesTableRetrievalForSameCodesTableShouldReturnANewInstance() {
		CodesTable<TestCti, TestCtiCode, String> ct1 = dctm.getCodesTable(TestCti.class);
		CodesTable<TestCti, TestCtiCode, String> ct2 = dctm.getCodesTable(TestCti.class);

		assertThat(ct1, is(not(sameInstance(ct2))));

		CodesTablePeriodic<TestCtpi, TestCtiCode, String> ctp1 = dctm.getCodesTablePeriodic(TestCtpi.class);
		CodesTablePeriodic<TestCtpi, TestCtiCode, String> ctp2 = dctm.getCodesTablePeriodic(TestCtpi.class);

		assertThat(ctp1, is(not(sameInstance(ctp2))));
	}

	/**
	 * Test applicationContextIsForwardedToConstructedCodesTableIfPossible.
	 * 
	 * @throws NoSuchFieldException
	 *             no fiels
	 * @throws IllegalAccessException
	 *             illegal access
	 */
	@Test
	public void applicationContextIsForwardedToConstructedCodesTableIfPossible() throws NoSuchFieldException,
			IllegalAccessException {
		final StaticApplicationContext applicationContext = new StaticApplicationContext();
		dctm.setApplicationContext(applicationContext);

		final CodesTable<TestIdAsKeyCti, TestCtiCode, String> ct = dctm.getCodesTable(TestIdAsKeyCti.class);
		final CodesTablePeriodic<TestIdAsKeyCtpi, TestCtiCode, String> ctp = dctm.getCodesTablePeriodic(TestIdAsKeyCtpi.class);

		final Field field = AbstractCodesTable.class.getDeclaredField("applicationContext");
		field.setAccessible(true);

		final Object beanFactoryOnCt = field.get(ct);
		assertThat(beanFactoryOnCt, is(notNullValue()));

		final Object beanFactoryOnCtp = field.get(ctp);
		assertThat(beanFactoryOnCtp, is(notNullValue()));
	}

	/**
	 * setup before test.
	 * 
	 * @throws NoSuchMethodException
	 *             no method
	 */
	@Before
	public void setupState() throws NoSuchMethodException {
		dctm = new DefaultCodesTableManager();
		dctm.setCodesTableItemsFactory(createMockedItemsFactory());

		methodGct = DefaultCodesTableManager.class.getMethod("getCodesTable", Class.class);
		methodGctp = DefaultCodesTableManager.class.getMethod("getCodesTablePeriodic", Class.class);
	}

	/**
	 * Create mocked items factory.
	 * 
	 * @return factory
	 */
	private CodesTableItemsFactory createMockedItemsFactory() {
		final List<TestCti> codesTableItems = new ArrayList<TestCti>(createCodesTableItems());
		final List<TestCtpi> codesTableItemPeriodics = new ArrayList<TestCtpi>(createCodesTablePeriodicItems());
		final CodesTableItemsFactory codesTableItemsFactory = context.mock(CodesTableItemsFactory.class);

		context.checking(new Expectations() {
			{
				allowing(codesTableItemsFactory).createCodesTableItems((Class<TestCti>) with(IsAnything.anything()));
				will(returnValue(codesTableItems));
				allowing(codesTableItemsFactory).createCodesTablePeriodicItems((Class<TestCtpi>) with(IsAnything.anything()));
				will(returnValue(codesTableItemPeriodics));
			}
		});

		return codesTableItemsFactory;
	}

	/**
	 * Create codestable periodic items.
	 * 
	 * @return items
	 */
	private Set<TestCtpi> createCodesTablePeriodicItems() {
		final Set<TestCtpi> codesTablePeriodicItems = new HashSet<TestCtpi>(2);
		codesTablePeriodicItems.add(TestCtpi.createExistsTimeCtpi1());
		codesTablePeriodicItems.add(TestCtpi.createExistsTimeCtpi3());

		return codesTablePeriodicItems;
	}

	/**
	 * Create codestable periodic items map.
	 * 
	 * @return items map
	 */
	private Map<String, TestCtpi> createCodesTablePeriodicItemsMap() {
		final Map<String, TestCtpi> codesTablePeriodicItems = new HashMap<String, TestCtpi>(2);
		TestCtpi ctpi;
		ctpi = TestCtpi.createExistsTimeCtpi1();
		codesTablePeriodicItems.put(ctpi.getCodeAsString(), ctpi);
		ctpi = TestCtpi.createExistsTimeCtpi3();
		codesTablePeriodicItems.put(ctpi.getCodeAsString(), ctpi);

		return codesTablePeriodicItems;
	}

	/**
	 * Create codestable items.
	 * 
	 * @return items
	 */
	private Set<TestCti> createCodesTableItems() {
		final Set<TestCti> codesTableItems = new HashSet<TestCti>(3);
		codesTableItems.add(TestCti.createCti1());
		codesTableItems.add(TestCti.createCti2());
		codesTableItems.add(TestCti.createCti3());

		return codesTableItems;
	}
}