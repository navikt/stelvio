package no.stelvio.common.codestable.factory.adapter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtpi;
import no.stelvio.common.codestable.factory.NoGenericsCodesTableItemsFactory;

/**
 * Unit test for {@link NoGenericsCodesTableItemsFactoryAdapter}.
 * 
 * @author personf8e9850ed756, Accenture
 */
@RunWith(JMock.class)
public class NoGenericsCodesTableItemsFactoryAdapterTest {
	private Mockery context = new JUnit4Mockery();

	/**
	 * Test forwardsCallToInjectedCodesTableItemFactory.
	 */
	@Test
	public void forwardsCallToInjectedCodesTableItemFactory() {
		final NoGenericsCodesTableItemsFactory ctif = context.mock(NoGenericsCodesTableItemsFactory.class);

		context.checking(new Expectations() {
			{
				one(ctif).createCodesTableItems(TestCti.class);
				one(ctif).createCodesTablePeriodicItems(TestCtpi.class);
			}
		});

		NoGenericsCodesTableItemsFactoryAdapter ctifa = new NoGenericsCodesTableItemsFactoryAdapter();
		ctifa.setNoGenericsCodesTableItemsFactory(ctif);

		ctifa.createCodesTableItems(TestCti.class);
		ctifa.createCodesTablePeriodicItems(TestCtpi.class);
	}
}
