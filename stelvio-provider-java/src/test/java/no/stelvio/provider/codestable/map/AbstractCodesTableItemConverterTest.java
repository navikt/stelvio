package no.stelvio.provider.codestable.map;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.provider.codestable.TestCti;
import no.stelvio.provider.codestable.TestCtpi;
import no.stelvio.common.codestable.factory.CodesTableItemsFactory;
import no.stelvio.common.codestable.support.DefaultCodesTableManager;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

/***
 * This class is not a test of {@link AbstractCodesTableItemConverter}, but rather contains common test facilities for its
 * subclasses.
 * 
 */
public abstract class AbstractCodesTableItemConverterTest {

	private Mockery context = new JUnit4Mockery();
	private AbstractCodesTableItemConverter converter;

	/**
	 * Creates a <code>CodesTableManager</code> required for the converter.
	 * 
	 * Under normal circumstances, this would be injected by Spring.
	 * 
	 * @return codetable manager
	 */
	protected CodesTableManager createCodesTableManager() {

		final List<TestCti> codesTableItems = createCodesTableItems();
		final List<TestCtpi> codesTablePeriodicItems = createCodesTablePeriodicItems();
		final CodesTableItemsFactory codesTableItemsFactory = createCodesTableItemsFactory(codesTableItems,
				codesTablePeriodicItems);

		DefaultCodesTableManager codeTableManager = new DefaultCodesTableManager();
		codeTableManager.setCodesTableItemsFactory(codesTableItemsFactory);

		return codeTableManager;
	}

	/**
	 * Creates a <code>CodesTableItemsFactory</code> required for the codestable manager.
	 * 
	 * Under normal circumstances, this would be injected by Spring.
	 * 
	 * @param codesTableItems the codestable items
	 * @param codeTablePeriodicItems the codestable periodic items
	 * @return codestable items factory
	 */
	private CodesTableItemsFactory createCodesTableItemsFactory(final List<TestCti> codesTableItems,
			final List<TestCtpi> codeTablePeriodicItems) {

		final CodesTableItemsFactory codesTableItemsFactory = context.mock(CodesTableItemsFactory.class);

		context.checking(new Expectations() {
			{
				atLeast(1).of(codesTableItemsFactory).createCodesTableItems(TestCti.class);
				will(returnValue(codesTableItems));
				atLeast(1).of(codesTableItemsFactory).createCodesTablePeriodicItems(TestCtpi.class);
				will(returnValue(codeTablePeriodicItems));
			}
		});

		return codesTableItemsFactory;
	}

	/**
	 * Creates a <code>List</code> of <code>TestCti</code>s (non-periodic code table items) for testing.
	 * 
	 * @return list of codestable items
	 */
	private List<TestCti> createCodesTableItems() {
		final List<TestCti> codesTableItems = new ArrayList<TestCti>();
		codesTableItems.add(TestCti.createCti1());
		codesTableItems.add(TestCti.createCti2());
		codesTableItems.add(TestCti.createCti3());
		return codesTableItems;
	}

	/**
	 * Creates a <code>List</code> of <code>TestCtpi</code>s (periodic code table items) for testing.
	 * 
	 * @return list of codestable periodic items
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
	 * Sets the converter.
	 *
	 * @param converter the converter to set
	 */
	public void setConverter(AbstractCodesTableItemConverter converter) {
		this.converter = converter;
	}

	/**
	 * Get the converter.
	 *
	 * @return the converter
	 */
	public AbstractCodesTableItemConverter getConverter() {
		return converter;
	}

}
