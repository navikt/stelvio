package no.stelvio.common.codestable.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Predicate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.codestable.CodesTable;
import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.ItemNotFoundException;
import no.stelvio.common.codestable.TestCti;
import no.stelvio.common.codestable.TestCtiCode;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.SimpleRequestContext;

/**
 * Unit test of CodesTable.
 *
 * @author personb66fa0b5ff6e
 * @version $Id$
 * @todo refactor test, that is, have an abstract test super class that tests common functionality
 */
public class DefaultCodesTableTest {

	/** Implementation class to test */
	private CodesTable<TestCti, TestCtiCode, Integer> codesTable;

	/** Initialize components prior to running tests. */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	@Before
	public void setupDefaultCodesTable() {
		RequestContextHolder.
				setRequestContext(new SimpleRequestContext("screenId", "moduleId", "processId", "transactionId"));

		List<TestCti> list = new ArrayList<TestCti>();
		list.add(TestCti.getCti1());
		list.add(TestCti.getCti2());
		list.add(TestCti.getCti3());

		codesTable = new DefaultCodesTable<TestCti, TestCtiCode, Integer>(list);
	}

	/** Test of getCodesTableItem(). */
	@Test
	public void getCodesTableItem() {
		//Test: get an item that does not exist
		try {
			codesTable.getCodesTableItem(TestCtiCode.EXISTS_1);
			fail("ItemNotFoundException should have been thrown");
		} catch (ItemNotFoundException e) {
			// should happen
		}

		//Test: get an item
		TestCti itemExist = codesTable.getCodesTableItem(TestCtiCode.EXISTS_1);
		assertNotNull("Test 2: the item does not exist", itemExist);
		assertEquals("Test 3: Unexpected code", "t1code1", itemExist.getCode());
		assertEquals("Test 4: Unexpected decode", "t1decode1", itemExist.getDecode());

	}

	/** Test of addPredicate(Predicate predicate). */
	@Test
	public void addPredicateAndRemovePredicate() {
		Predicate pred2 = new Predicate() {
			public boolean evaluate(Object object) {
				CodesTableItem<?, Integer> codesTableItem = (CodesTableItem<?, Integer>) object;

				return codesTableItem.getDecode().compareTo(1) == 0;
			}
		};

		//Test: get the items in the codestable
		assertNotNull("Test 1: the item does not exist", codesTable.getCodesTableItem(TestCtiCode.EXISTS_1));
		assertNotNull("Test 2: the item does not exist", codesTable.getCodesTableItem(TestCtiCode.EXISTS_2));
		assertNotNull("Test 3: the item does not exist", codesTable.getCodesTableItem(TestCtiCode.EXISTS_3));

		codesTable.addPredicate(pred2);

		try {
			//	Test: get an item that does not exist
			codesTable.getCodesTableItem(TestCtiCode.EXISTS_2);
			fail("Item " + codesTable.getCodesTableItem(TestCtiCode.EXISTS_2) + " should not have been found");
		} catch (ItemNotFoundException ex) {
			//do nothing
		}

		//Test: get items in the filtered codestable
		assertNotNull("Test 8: the item does not exist", codesTable.getCodesTableItem(TestCtiCode.EXISTS_1));

		codesTable.resetPredicates();

		//Test: get the items in the codestable
		assertNotNull("Test 9: the item does not exist", codesTable.getCodesTableItem(TestCtiCode.EXISTS_1));
		assertNotNull("Test 10: the item does not exist", codesTable.getCodesTableItem(TestCtiCode.EXISTS_2));
		assertNotNull("Test 11: the item does not exist", codesTable.getCodesTableItem(TestCtiCode.EXISTS_3));
	}

	/** Test of getDecode(Object code). */
	@Test
	public void getDecodeWithCode() {

		//Test: get a decode for a code that does not exist
		try {
			codesTable.getDecode(TestCtiCode.EXISTS_NOT);
			fail("Expected exception");
		} catch (Exception ex) {
			assertEquals("Test 1: getDecode() should have thrown exception", "DecodeNotFoundException", ex.getClass().getSimpleName());
		}

		//Test: get a decode
		assertEquals("Test 3: unexptected decode", codesTable.getDecode(TestCti.getCti1().getCode()), "t1decode1");
	}

	@Test
	public void canValidateThatCodeExists() {
		assertTrue("Should exist", codesTable.validateCode("t1code1"));
		assertFalse("Should not exist", codesTable.validateCode("existsNot"));
	}
}