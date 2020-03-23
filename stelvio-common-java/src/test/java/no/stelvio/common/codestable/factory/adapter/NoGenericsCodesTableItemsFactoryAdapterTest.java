package no.stelvio.common.codestable.factory.adapter;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtpi;
import no.stelvio.common.codestable.factory.NoGenericsCodesTableItemsFactory;

/**
 * Unit test for {@link NoGenericsCodesTableItemsFactoryAdapter}.
 * 
 */

public class NoGenericsCodesTableItemsFactoryAdapterTest {
	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();

	/**
	 * Test forwardsCallToInjectedCodesTableItemFactory.
	 */
	@Test
	public void forwardsCallToInjectedCodesTableItemFactory() {
		final NoGenericsCodesTableItemsFactory ctif = context.mock(NoGenericsCodesTableItemsFactory.class);

		context.checking(new Expectations() {
			{
				oneOf(ctif).createCodesTableItems(TestCti.class);
				oneOf(ctif).createCodesTablePeriodicItems(TestCtpi.class);
			}
		});

		NoGenericsCodesTableItemsFactoryAdapter ctifa = new NoGenericsCodesTableItemsFactoryAdapter();
		ctifa.setNoGenericsCodesTableItemsFactory(ctif);

		ctifa.createCodesTableItems(TestCti.class);
		ctifa.createCodesTablePeriodicItems(TestCtpi.class);
	}
}
