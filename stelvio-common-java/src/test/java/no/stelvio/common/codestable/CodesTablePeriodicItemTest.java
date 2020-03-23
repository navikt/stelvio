package no.stelvio.common.codestable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

import org.junit.Test;

import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTableItemTest;

/**
 * Unit test of {@link CodesTablePeriodicItem}.
 * 
 * @version $Id$
 */
public class CodesTablePeriodicItemTest extends AbstractCodesTableItemTest {

	/**
	 * Test that default constructor sets no values on field for periodic items.
	 */
	@Test
	public void defaultConstructorSetsNoValuesOnFieldsForPeriodicItems() {
		CodesTablePeriodicItem<TestCtiCode, String> periodicItem = (CodesTablePeriodicItem<TestCtiCode, String>) createEmpty();

		assertThat(periodicItem.getFromDate(), is(nullValue()));
		assertThat(periodicItem.getToDate(), is(nullValue()));
	}

	/**
	 * Test that equals is implemented correct when values exists.
	 */
	@Test
	public void equalsIsImplementedCorrectlyWhenNullValuesExists() {

		TestCtpi empty = TestCtpi.createEmptyCtpi();
		TestCtpi emptyExceptCode = TestCtpi.createEmptyExceptCodeCtpi();
		TestCtpi emptyExceptToDate = TestCtpi.createEmptyExceptToDateCtpi();
		TestCtpi emptyExceptFromDate = TestCtpi.createEmptyExceptFromDateCtpi();
		TestCtpi fullExceptFromDate = TestCtpi.createFullExceptFromDateCtpi();
		TestCtpi full = TestCtpi.createCtpi1();

		assertThat(empty.equals(empty), is(true));
		assertThat(empty.equals(emptyExceptCode), is(false));
		assertThat(empty.equals(emptyExceptToDate), is(false));
		assertThat(empty.equals(emptyExceptFromDate), is(false));
		assertThat(fullExceptFromDate.equals(full), is(false));

	}

	/**
	 * Test that equals is implemented correct.
	 */
	@Test
	public void equalsIsImplementedCorrectly() {
		assertThat(TestCtpi.createCtpi1().equals(TestCtpi.createCtpi1()), is(true));
		assertThat(TestCtpi.createCtpi1().equals(null), is(false));
		assertThat(TestCtpi.createCtpi1().equals("String"), is(false));
		assertThat(TestCtpi.createCtpi1().equals(TestCtpi.createCtpi2()), is(false));
		assertThat(TestCtpi.createCtpi1().equals(TestCti.createCti1()), is(false));
	}

	/**
	 * Test that hashcode is implemented correct.
	 */
	@Test
	public void hashCodeIsImplementedCorrectly() {
		assertThat(TestCtpi.createCtpi1().hashCode(), is(equalTo(TestCtpi.createCtpi1().hashCode())));
		assertThat(TestCtpi.createCtpi1().hashCode(), is(not(equalTo(TestCtpi.createCtpi2().hashCode()))));
	}

	/**
	 * Creates an empty codestable.
	 * 
	 * @return codestable
	 */
	public AbstractCodesTableItem<TestCtiCode, String> createEmpty() {
		return TestCtpi.createEmptyCtpi();
	}

	/**
	 * Creates a non-empty codestable.
	 * 
	 * @return codestable
	 */
	public AbstractCodesTableItem<TestCtiCode, String> createFilledIn() {
		return TestCtpi.createCtpi1();
	}

	/**
	 * Creates a non-empty codestable without corresponding enum.
	 * 
	 * @return codestable
	 */
	public AbstractCodesTableItem<TestCtiCode, String> createFilledInWithoutCorrespondingEnum() {
		return TestCtpi.createCtpiWithoutEnum();
	}
}