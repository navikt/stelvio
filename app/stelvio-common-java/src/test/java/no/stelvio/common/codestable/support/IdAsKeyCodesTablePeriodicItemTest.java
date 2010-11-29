package no.stelvio.common.codestable.support;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import org.junit.Test;

import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtiCode;
import no.stelvio.common.codestable.TestIdAsKeyCtpi;

/**
 * Unit test of {@link IdAsKeyCodesTablePeriodicItem}.
 *
 * @author person66cdf88a8f67 (Accenture)
 * @version $Id$
 */
public class IdAsKeyCodesTablePeriodicItemTest extends AbstractCodesTableItemTest {
	@Test
	public void defaultConstructorSetsNoValuesOnFieldsForPeriodicItems() {
		IdAsKeyCodesTablePeriodicItem<TestCtiCode, String> periodicItem =
				(IdAsKeyCodesTablePeriodicItem<TestCtiCode, String>) createEmpty();

		assertThat(periodicItem.getFromDate(), is(nullValue()));
		assertThat(periodicItem.getToDate(), is(nullValue()));
	}

	@Test
	public void equalsIsImplementedCorrectly() {
		assertThat(TestIdAsKeyCtpi.createCtpi1().equals(TestIdAsKeyCtpi.createCtpi1()), is(true));
		assertThat(TestIdAsKeyCtpi.createCtpi1().equals(null), is(false));
		assertThat(TestIdAsKeyCtpi.createCtpi1().equals("String"), is(false));
		assertThat(TestIdAsKeyCtpi.createCtpi1().equals(TestIdAsKeyCtpi.createCtpi2()), is(false));
		assertThat(TestIdAsKeyCtpi.createCtpi1().equals(TestCti.createCti1()), is(false));
	}

	@Test
	public void hashCodeIsImplementedCorrectly() {
		assertThat(TestIdAsKeyCtpi.createCtpi1().hashCode(), is(equalTo(TestIdAsKeyCtpi.createCtpi1().hashCode())));
		assertThat(TestIdAsKeyCtpi.createCtpi1().hashCode(), is(not(equalTo(TestIdAsKeyCtpi.createCtpi2().hashCode()))));
	}

	public AbstractCodesTableItem<TestCtiCode, String> createEmpty() {
		return TestIdAsKeyCtpi.createEmptyCtpi();
	}

	public AbstractCodesTableItem<TestCtiCode, String> createFilledIn() {
		return TestIdAsKeyCtpi.createCtpi1();
	}

	public AbstractCodesTableItem<TestCtiCode, String> createFilledInWithoutCorrespondingEnum() {
		return TestIdAsKeyCtpi.createCtpiWithoutEnum();
	}
}