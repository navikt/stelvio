package no.stelvio.common.codestable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import org.junit.Test;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTableItemTest;

/**
 * Unit test of {@link CodesTableItem}.
 * 
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class CodesTableItemTest extends AbstractCodesTableItemTest {

	/**
	 * Test equals.
	 */
	@Test
	public void equalsIsImplementedCorrectly() {
		assertThat(TestCti.createCti1().equals(TestCti.createCti1()), is(true));
		assertThat(TestCti.createCti1().equals(null), is(false));
		assertThat(TestCti.createCti1().equals("String"), is(false));
		assertThat(TestCti.createCti1().equals(TestCti.createCti2()), is(false));
		assertThat(TestCti.createCti1().equals(TestCtpi.createCtpi1()), is(false));
	}

	/**
	 * Test create empty codestable ite.
	 */
	@Test
	public void hashCodeIsImplementedCorrectly() {
		assertThat(TestCti.createCti1().hashCode(), is(equalTo(TestCti.createCti1().hashCode())));
		assertThat(TestCti.createCti1().hashCode(), is(not(equalTo(TestCti.createCti2().hashCode()))));
	}

	/**
	 * Create an empty codestable item.
	 * 
	 * @return item
	 */
	public AbstractCodesTableItem<TestCtiCode, String> createEmpty() {
		return TestCti.createEmptyCti();
	}

	/**
	 * Create a codestable item.
	 * 
	 * @return item
	 */
	public AbstractCodesTableItem<TestCtiCode, String> createFilledIn() {
		return TestCti.createCti1();
	}

	/**
	 * Create a codestable item without enum.
	 * 
	 * @return item
	 */
	public AbstractCodesTableItem<TestCtiCode, String> createFilledInWithoutCorrespondingEnum() {
		return TestCti.createCtiWithoutEnum();
	}
}