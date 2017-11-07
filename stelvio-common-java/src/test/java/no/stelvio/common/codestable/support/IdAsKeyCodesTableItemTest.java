package no.stelvio.common.codestable.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import org.junit.Test;

import no.stelvio.common.codestable.TestCtiCode;
import no.stelvio.common.codestable.TestIdAsKeyCti;
import no.stelvio.common.codestable.TestIdAsKeyCtpi;

/**
 * Unit test of {@link IdAsKeyCodesTableItem}.
 * 
 * @author person66cdf88a8f67 (Accenture)
 * @version $Id$
 */
public class IdAsKeyCodesTableItemTest extends AbstractCodesTableItemTest {
	/**
	 * Test equals.
	 */
	@Test
	public void equalsIsImplementedCorrectly() {
		assertThat(TestIdAsKeyCti.createCti1().equals(TestIdAsKeyCti.createCti1()), is(true));
		assertThat(TestIdAsKeyCti.createCti1().equals(null), is(false));
		assertThat(TestIdAsKeyCti.createCti1().equals("String"), is(false));
		assertThat(TestIdAsKeyCti.createCti1().equals(TestIdAsKeyCti.createCti2()), is(false));
		assertThat(TestIdAsKeyCti.createCti1().equals(TestIdAsKeyCtpi.createCtpi1()), is(false));
	}

	/**
	 * Test hashCode.
	 */
	@Test
	public void hashCodeIsImplementedCorrectly() {
		assertThat(TestIdAsKeyCti.createCti1().hashCode(), is(equalTo(TestIdAsKeyCti.createCti1().hashCode())));
		assertThat(TestIdAsKeyCti.createCti1().hashCode(), is(not(equalTo(TestIdAsKeyCti.createCti2().hashCode()))));
	}

	/**
	 * Create empty codestableitem.
	 * 
	 * @return item
	 */
	public AbstractCodesTableItem<TestCtiCode, String> createEmpty() {
		return TestIdAsKeyCti.createEmptyCti();
	}

	/**
	 * Create filled in codestable item.
	 * 
	 * @return item
	 */
	public AbstractCodesTableItem<TestCtiCode, String> createFilledIn() {
		return TestIdAsKeyCti.createCti1();
	}

	/**
	 * Create filled in codestable item without enum.
	 * 
	 * @return item
	 */
	public AbstractCodesTableItem<TestCtiCode, String> createFilledInWithoutCorrespondingEnum() {
		return TestIdAsKeyCti.createCtiWithoutEnum();
	}
}