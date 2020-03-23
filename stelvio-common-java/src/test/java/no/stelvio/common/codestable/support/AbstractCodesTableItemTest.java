package no.stelvio.common.codestable.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import org.junit.Test;

import no.stelvio.common.codestable.TestCtiCode;
import no.stelvio.common.codestable.TestCti;

/**
 * Unit test for {@link AbstractCodesTableItem}. All classes extending <code>AbstractCodesTableItem</code> should have their
 * test classes extend this class.
 * 
 */
public abstract class AbstractCodesTableItemTest {
	/**
	 * Test default constructor.
	 */
	@Test
	public void defaultConstructorSetsNoValues() {
		AbstractCodesTableItem<TestCtiCode, String> codesTableItem = createEmpty();

		// Cannot check the value of code as then an exception is thrown
		// assertThat(codesTableItem.getCode(), is(nullValue()));
		assertThat(codesTableItem.getDecode(), is(nullValue()));
		assertThat(codesTableItem.isValid(), is(false));
	}

	/**
	 * Test that code is retrieved for filled-in instances.
	 */
	@Test
	public void canRetrieveCodeForFilledInInstance() {
		assertThat(createFilledIn().getCode(), is(equalTo(TestCtiCode.EXISTS_1)));
		assertThat(createFilledIn().getCodeAsString(), is(equalTo(TestCtiCode.EXISTS_1.name())));
	}

	/**
	 * Test that enum can be retrieved for code.
	 */
	@Test
	public void canRetrieveEnumClassForCode() {
		assertThat(AbstractCodesTableItem.<TestCtiCode> getCodeClass(TestCti.class), is(equalTo(TestCtiCode.class)));
	}

	/**
	 * Test canTestThatCodeIsEqualWithoutLookingUpEnumInCti.
	 */
	@Test
	public void canTestThatCodeIsEqualWithoutLookingUpEnumInCti() {
		assertThat(createFilledIn().isCodeEqualTo(TestCtiCode.EXISTS_1), is(true));
		assertThat(createFilledIn().isCodeEqualTo(TestCtiCode.EXISTS_2), is(false));
		assertThat(createFilledInWithoutCorrespondingEnum().isCodeEqualTo(TestCtiCode.EXISTS_2), is(false));
	}

	/**
	 * Test that code can be retrieved as string.
	 */
	@Test
	public void canRetrieveCodeAsString() {
		assertThat(createFilledIn().getCodeAsString(), is(equalTo("EXISTS_1")));
		assertThat(createFilledInWithoutCorrespondingEnum().getCodeAsString(), is(equalTo("NO_ENUM")));
	}

	/**
	 * Test tostring.
	 */
	@Test
	public void toStringIsNotEmpty() {
		assertThat(createFilledIn().toString(), is(notNullValue()));
	}

	/**
	 * Returns an instance of a subclass of <code>AbstractCodesTableItem</code> from the default constructor. Must be
	 * implemented by test classes for sub classes of <code>AbstractCodesTableItem</code> by returning an instance of the class
	 * to test created from the default constructor.
	 * 
	 * @return an instance of a subclass of <code>AbstractCodesTableItem</code> that will be tested.
	 */
	public abstract AbstractCodesTableItem<TestCtiCode, String> createEmpty();

	/**
	 * Returns an instance of a subclass of <code>AbstractCodesTableItem</code> with filled in values. Must be implemented by
	 * test classes for sub classes of <code>AbstractCodesTableItem</code> by returning an instance of the class to test with
	 * filled in values.
	 * 
	 * @return an instance of a subclass of <code>AbstractCodesTableItem</code> that will be tested.
	 */
	public abstract AbstractCodesTableItem<TestCtiCode, String> createFilledIn();

	/**
	 * Returns an instance of a subclass of <code>AbstractCodesTableItem</code> with filled in values where the code value don't
	 * have a corresponding enum constant. Must be implemented by test classes for sub classes of
	 * <code>AbstractCodesTableItem</code> by returning an instance of the class to test with filled in values.
	 * 
	 * @return an instance of a subclass of <code>AbstractCodesTableItem</code> that will be tested.
	 */
	public abstract AbstractCodesTableItem<TestCtiCode, String> createFilledInWithoutCorrespondingEnum();
}
