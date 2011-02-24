package no.stelvio.common.codestable;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;

import no.stelvio.common.codestable.factory.support.DefaultCodesTableInitializer;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;
import no.stelvio.common.codestable.support.DefaultCodesTableManager;

/**
 * Integration test for the codes table support in Stelvio.
 * 
 * @author personf8e9850ed756, Accenture
 */
@RunWith(JMock.class)
public class CodesTableIntegrationTest {
	private Mockery context = new JUnit4Mockery();

	/**
	 * Test that manager returns the correct codestable.
	 */
	@Test
	public void managerReturnsTheCorrectCodesTable() {
		CodesTableManager codesTableManager = createCodesTableManager();

		assertThat(codesTableManager.getCodesTable(TestCti.class).getCodesTableItems().size(), is(equalTo(3)));
		assertThat(codesTableManager.getCodesTablePeriodic(TestCtpi.class).getCodesTableItems().size(), is(equalTo(4)));
	}

	/**
	 * Test that initializer loads the correct codestable.
	 */
	@Test
	public void initializerLoadsTheCorrectCodesTables() {
		DefaultCodesTableInitializer cti = new DefaultCodesTableInitializer();
		cti.setCodesTableManager(createCodesTableManager());
		cti.setCodesTableItemClasses(Collections.<Class<? extends CodesTableItem<? extends Enum, ?>>> singleton(TestCti.class));
		cti.setCodesTablePeriodicItemClasses(Collections
				.<Class<? extends CodesTablePeriodicItem<? extends Enum, ?>>> singleton(TestCtpi.class));

		cti.afterPropertiesSet();
	}

	/**
	 * Create codestable manager.
	 * 
	 * @return manager
	 */
	private CodesTableManager createCodesTableManager() {
		DefaultCodesTableManager ctm = new DefaultCodesTableManager();
		ctm.setCodesTableItemsFactory(createCodesTableItemsFactory());

		return ctm;
	}

	/**
	 * Create codestable item factory.
	 * 
	 * @return factory
	 */
	private CodesTableItemsFactory createCodesTableItemsFactory() {
		final CodesTableItemsFactory codesTableItemsFactory = context.mock(CodesTableItemsFactory.class);

		context.checking(new Expectations() {
			{
				one(codesTableItemsFactory).createCodesTableItems(TestCti.class);
				will(returnValue(createCodesTableItems()));
				one(codesTableItemsFactory).createCodesTablePeriodicItems(TestCtpi.class);
				will(returnValue(createCodesTablePeriodicItems()));
			}
		});

		return codesTableItemsFactory;
	}

	/**
	 * Create codestable periodic items.
	 * 
	 * @return codestable item periodic
	 */
	private List<TestCtpi> createCodesTablePeriodicItems() {
		final List<TestCtpi> codesTableItemPeriodics = new ArrayList<TestCtpi>();
		codesTableItemPeriodics.add(TestCtpi.createCtpi1());
		codesTableItemPeriodics.add(TestCtpi.createCtpi2());
		codesTableItemPeriodics.add(TestCtpi.createCtpi3());
		codesTableItemPeriodics.add(TestCtpi.createCtpi4());
		return codesTableItemPeriodics;
	}

	/**
	 * Create codestable items.
	 * 
	 * @return codestable item
	 */
	private List<TestCti> createCodesTableItems() {
		final List<TestCti> codesTableItems = new ArrayList<TestCti>();
		codesTableItems.add(TestCti.createCti1());
		codesTableItems.add(TestCti.createCti2());
		codesTableItems.add(TestCti.createCti3());
		return codesTableItems;
	}
}
