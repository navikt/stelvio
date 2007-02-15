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
import no.stelvio.common.codestable.TestCodesTableItem;
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
	private CodesTable<CodesTableItem> codesTable;

	/** Initialize components prior to running tests. */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	@Before
	public void setupDefaultCodesTable() {
		RequestContextHolder.
				setRequestContext(new SimpleRequestContext("screenId", "moduleId", "processId", "transactionId"));

		List<CodesTableItem> list = new ArrayList<CodesTableItem>();
		list.add(TestCodesTableItem.getCti1());
		list.add(TestCodesTableItem.getCti2());
		list.add(TestCodesTableItem.getCti3());

		codesTable = new DefaultCodesTable<CodesTableItem>(list);
	}

	/** Test of getCodesTableItem(). */
	@Test
	public void getCodesTableItem() {
		//Test: get an item that does not exist
		try {
			codesTable.getCodesTableItem("t8code15");
			fail("ItemNotFoundException should have been thrown");
		} catch (ItemNotFoundException e) {
			// should happen
		}

		//Test: get an item
		CodesTableItem itemExist = codesTable.getCodesTableItem("t1code1");
		assertNotNull("Test 2: the item does not exist", itemExist);
		assertEquals("Test 3: Unexpected code", "t1code1", itemExist.getCode());
		assertEquals("Test 4: Unexpected decode", "t1decode1", itemExist.getDecode());

	}

	/** Test of addPredicate(Predicate predicate). */
	@Test
	public void addPredicateAndRemovePredicate() {
		Predicate pred2 = new Predicate() {
			public boolean evaluate(Object object) {
				CodesTableItem codesTableItem = (CodesTableItem) object;

				return codesTableItem.getCode().startsWith("t1");
			}
		};

		//Test: get the items in the codestable
		assertNotNull("Test 1: the item does not exist", codesTable.getCodesTableItem("t1code1"));
		assertNotNull("Test 2: the item does not exist", codesTable.getCodesTableItem("t2code2"));
		assertNotNull("Test 3: the item does not exist", codesTable.getCodesTableItem("t3code3"));

		codesTable.addPredicate(pred2);

		try {
			//	Test: get an item that does not exist
			codesTable.getCodesTableItem("t2code2");
			fail("Item " + codesTable.getCodesTableItem("t2code2") + " should not have been found");
		} catch (ItemNotFoundException ex) {
			//do nothing
		}

		//Test: get items in the filtered codestable
		assertNotNull("Test 8: the item does not exist", codesTable.getCodesTableItem("t1code1"));

		codesTable.resetPredicates();

		//Test: get the items in the codestable
		assertNotNull("Test 9: the item does not exist", codesTable.getCodesTableItem("t1code1"));
		assertNotNull("Test 10: the item does not exist", codesTable.getCodesTableItem("t2code2"));
		assertNotNull("Test 11: the item does not exist", codesTable.getCodesTableItem("t3code3"));
	}

	/** Test of getDecode(Object code). */
	@Test
	public void getDecodeWithCode() {

		//Test: get a decode for a code that does not exist
		try {
			codesTable.getDecode("t8code15");
			fail("Expected exception");
		} catch (Exception ex) {
			assertEquals("Test 1: getDecode() should have thrown exception", "DecodeNotFoundException", ex.getClass().getSimpleName());
		}

		//Test: get a decode
		assertEquals("Test 3: unexptected decode", codesTable.getDecode(TestCodesTableItem.getCti1().getCode()), "t1decode1");
	}

	@Test
	public void canValidateThatCodeExists() {
		assertTrue("Should exist", codesTable.validateCode(TestEnum.t1code1));
		assertFalse("Should not exist", codesTable.validateCode(TestEnum.existsNot));
	}
	
	public static enum TestEnum {
		t1code1,
		existsNot
	}
}