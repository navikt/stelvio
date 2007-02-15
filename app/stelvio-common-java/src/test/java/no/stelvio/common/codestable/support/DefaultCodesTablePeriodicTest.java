package no.stelvio.common.codestable.support;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.Predicate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import no.stelvio.common.codestable.CodesTableItemPeriodic;
import no.stelvio.common.codestable.CodesTablePeriodic;
import no.stelvio.common.codestable.DecodeNotFoundException;
import no.stelvio.common.codestable.ItemNotFoundException;
import no.stelvio.common.codestable.TestCodesTableItemPeriodic;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.SimpleRequestContext;

/**
 * Unit test of CodesTablePeriodic.
 *
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class DefaultCodesTablePeriodicTest {

	/** Implementation class to test */
	private CodesTablePeriodic<CodesTableItemPeriodic> codesTablePeriodic;

	/** Initialize components prior to running tests. */
	//TODO: FIX CASTING
	@SuppressWarnings("unchecked")
	@Before
	public void setupDefaultCodesTablePeriodic() {
		RequestContextHolder.
				setRequestContext(new SimpleRequestContext("screenId", "moduleId", "processId", "transactionId"));

		List<CodesTableItemPeriodic> list = new ArrayList<CodesTableItemPeriodic>();
		list.add(TestCodesTableItemPeriodic.getCtip1());
		list.add(TestCodesTableItemPeriodic.getCtip2());
		list.add(TestCodesTableItemPeriodic.getCtip3());
		list.add(TestCodesTableItemPeriodic.getCtiWithEmptyDecode());

		codesTablePeriodic = new DefaultCodesTablePeriodic<CodesTableItemPeriodic>(list);
	}

	/** Test of getCodesTableItem(). */
	@Test
	public void getCodesTableItem() {
		//Test: get an item that does not exist
		try {
			codesTablePeriodic.getCodesTableItem("t8code15");
			fail("ItemNotFoundException should have been thrown");
		} catch (ItemNotFoundException e) {
			// should happen
		}

		//Test: get an item
		CodesTableItemPeriodic itemExist = codesTablePeriodic.getCodesTableItem("t1code1");
		assertNotNull("Test 2: the item does not exist", itemExist);
		assertEquals("Test 3: Unexpected code", "t1code1", itemExist.getCode());
		assertEquals("Test 4: Unexpected decode", "t1decode1", itemExist.getDecode());
	}

	/** Test of addPredicate(Predicate predicate). */
	@Test
	public void addPredicateAndRemovePredicate() {
		Predicate predicate = new Predicate() {
			public boolean evaluate(Object object) {
				CodesTableItemPeriodic codesTableItemPeriodic = (CodesTableItemPeriodic) object;

				return codesTableItemPeriodic.getCode().startsWith("t1");
			}
		};

		//Test: get the items in the codestable 
		assertNotNull("Test 1: the item does not exist", codesTablePeriodic.getCodesTableItem("t1code1"));
		assertNotNull("Test 2: the item does not exist", codesTablePeriodic.getCodesTableItem("t2code2"));
		assertNotNull("Test 3: the item does not exist", codesTablePeriodic.getCodesTableItem("t3code3"));

		codesTablePeriodic.addPredicate(predicate);

		// Test: get items from the filtered codestable
		assertNotNull("Test 5: the item does not exist", codesTablePeriodic.getCodesTableItem("t1code1"));

		try {
			// Test: get an item that does not exist in the filtered code stable
			codesTablePeriodic.getCodesTableItem("t2code2");
			fail("ItemNotFoundException should have been thrown");
		} catch (ItemNotFoundException e) {
			// should happen
		}

		try {
			// Test: get an item that does not exist in the filtered code stable
			codesTablePeriodic.getCodesTableItem("t3code3");
			fail("ItemNotFoundException should have been thrown");
		} catch (ItemNotFoundException e) {
			// should happen
		}

		codesTablePeriodic.resetPredicates();

		//Test: get the items in the codestable 
		assertNotNull("Test 9: the item does not exist", codesTablePeriodic.getCodesTableItem("t1code1"));
		assertNotNull("Test 10: the item does not exist", codesTablePeriodic.getCodesTableItem("t2code2"));
		assertNotNull("Test 11: the item does not exist", codesTablePeriodic.getCodesTableItem("t3code3"));
	}

	/** Test of getDecode(Object code, Date date). */
	@Test
	public void getDecodeWithCode() {
		Calendar cal = Calendar.getInstance();
		cal.set(106, 10, 15);
		Date date = new Date(cal.getTimeInMillis());

		//Set the locale in the RequestContext
		Locale locale = new Locale("nb", "NO");
		LocaleContextHolder.setLocale(locale);

		try {
			codesTablePeriodic.getDecode("emptyDecode", date);
			fail("DecodeNotFoundException should have been thrown because decode should be null");
		} catch (DecodeNotFoundException ex) {
			// should happen
		}

		assertEquals("Test 3: unexptected decode", codesTablePeriodic.getDecode(TestCodesTableItemPeriodic.getCtip1().getCode(), date), "t1decode1");
	}
}