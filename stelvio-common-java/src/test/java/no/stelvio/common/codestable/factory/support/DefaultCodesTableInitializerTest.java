package no.stelvio.common.codestable.factory.support;

import static java.util.Collections.EMPTY_SET;

import java.util.Collections;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import no.stelvio.common.codestable.CodesTableConfigurationException;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.CodesTableNotFoundException;
import no.stelvio.common.codestable.CodesTablePeriodicItem;
import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtpi;

/**
 * Unit test of {@link DefaultCodesTableInitializer}.
 * 
 * @author personb66fa0b5ff6e, Accenture
 * @version $Id$
 */

public class DefaultCodesTableInitializerTest {
	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();

	/**
	 * Test codesTableConfigurationExceptionIsThrownWhenNoItemClassesIsSet.
	 */
	@Test(expected = CodesTableConfigurationException.class)
	public void codesTableConfigurationExceptionIsThrownWhenNoItemClassesIsSet() {
		new DefaultCodesTableInitializer().afterPropertiesSet();
	}

	/**
	 * Test whenNoPeriodicItemClassesIsSetManagerIsCalledOnlyForRegularItems.
	 */
	@Test
	public void whenNoPeriodicItemClassesIsSetManagerIsCalledOnlyForRegularItems() {
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		context.checking(new Expectations() {
			{
				oneOf(codesTableManager).getCodesTable(TestCti.class);
			}
		});

		DefaultCodesTableInitializer cti = createDefaultCodesTableInitializer(codesTableManager, 
				Collections.<Class<? extends CodesTableItem<? extends Enum, ?>>> singleton(TestCti.class), EMPTY_SET);

		cti.afterPropertiesSet();
	}

	/**
	 * Test whenNoRegularItemClassesIsSetManagerIsCalledOnlyForPeriodicItems.
	 */
	@Test
	public void whenNoRegularItemClassesIsSetManagerIsCalledOnlyForPeriodicItems() {
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		context.checking(new Expectations() {
			{
				oneOf(codesTableManager).getCodesTablePeriodic(with(equal(TestCtpi.class)));
			}
		});

		DefaultCodesTableInitializer cti = createDefaultCodesTableInitializer(codesTableManager, EMPTY_SET, 
				Collections.<Class<? extends CodesTablePeriodicItem<? extends Enum, ?>>> singleton(TestCtpi.class));

		cti.afterPropertiesSet();
	}

	/**
	 * Test codesTableNotFoundExceptionExceptionIsThrownWhenCodesTableManagerReturnsNullForARegularItemClass.
	 */
	@Test(expected = CodesTableNotFoundException.class)
	public void codesTableNotFoundExceptionExceptionIsThrownWhenCodesTableManagerReturnsNullForARegularItemClass() {
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		context.checking(new Expectations() {
			{
				oneOf(codesTableManager).getCodesTable(TestCti.class);
				will(returnValue(null));
			}
		});

		DefaultCodesTableInitializer cti = createDefaultCodesTableInitializer(codesTableManager, Collections
				.<Class<? extends CodesTableItem<? extends Enum, ?>>> singleton(TestCti.class), EMPTY_SET);

		cti.afterPropertiesSet();

	}

	/**
	 * Test codesTableNotFoundExceptionExceptionIsThrownWhenCodesTableManagerReturnsNullForAPeriodicItemClass.
	 */
	@Test(expected = CodesTableNotFoundException.class)
	public void codesTableNotFoundExceptionExceptionIsThrownWhenCodesTableManagerReturnsNullForAPeriodicItemClass() {
		final CodesTableManager codesTableManager = context.mock(CodesTableManager.class);

		context.checking(new Expectations() {
			{
				oneOf(codesTableManager).getCodesTablePeriodic(TestCtpi.class);
				will(returnValue(null));
			}
		});

		DefaultCodesTableInitializer cti = createDefaultCodesTableInitializer(codesTableManager, EMPTY_SET, 
				Collections.<Class<? extends CodesTablePeriodicItem<? extends Enum, ?>>> singleton(TestCtpi.class));

		cti.afterPropertiesSet();

	}

	/**
	 * Creates a <code>DefaultCodesTableInitializer</code> that has its <code>CodesTableManager</code> and both list of item
	 * classes setup.
	 * 
	 * @param codesTableManager
	 *            the <code>CodesTableManager</code> to initialize the instance with.
	 * @param codesTableItemClasses
	 *            the list of <code>CodesTableItem</code> classes to initialize the instance with.
	 * @param codesTablePeriodicItemClasses
	 *            the list of <code>CodesTablePeriodicItem</code> classes to initialize the instance with.
	 * @return an instance of <code>DefaultCodesTableInitializer</code>.
	 */
	private DefaultCodesTableInitializer createDefaultCodesTableInitializer(CodesTableManager codesTableManager,
			Set<Class<? extends CodesTableItem<? extends Enum, ?>>> codesTableItemClasses,
			Set<Class<? extends CodesTablePeriodicItem<? extends Enum, ?>>> codesTablePeriodicItemClasses) {
		DefaultCodesTableInitializer cti = new DefaultCodesTableInitializer();
		cti.setCodesTableManager(codesTableManager);
		cti.setCodesTableItemClasses(codesTableItemClasses);
		cti.setCodesTablePeriodicItemClasses(codesTablePeriodicItemClasses);

		return cti;
	}
}
