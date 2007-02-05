package no.stelvio.common.codestable;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.eq;
import static org.hamcrest.core.IsEqual.isFalse;
import static org.hamcrest.core.IsEqual.isTrue;
import static org.hamcrest.core.IsNull.isNotNull;
import static org.hamcrest.core.IsNull.isNull;

import java.util.Date;

import org.junit.Test;

/**
 * Unit test of CodesTableItem.
 * 
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class CodesTableItemPeriodicTest {
	/**
	 * Test of constructor for CodesTableItem.
	 */
	@Test
	public void testCodesTableItemEmptyConstructor() {
		CodesTableItemPeriodic cti = new Ctip();
		
		assertThat(cti.getCode(), isNull());
		assertThat(cti.getDecode(), isNull());
		assertThat(cti.getFromDate(), isNull());
		assertThat(cti.getToDate(), isNull());
		assertThat(cti.isValid(), isFalse());
	}

	/**
	 * Test of constructor for CodesTableItem()
	 */
	@Test
	public void testCodesTableItemConstrutor() {
		Date date = new Date();
		
		CodesTableItemPeriodic cti = new Ctip("t1code1", "t1decode1", date, date, Boolean.TRUE);
		
		assertThat(cti.getCode(), eq("t1code1"));
		assertThat(cti.getDecode(), eq("t1decode1"));
		assertThat(cti.getFromDate(), eq(date));
		assertThat(cti.getToDate(), eq(date));
		assertThat(cti.isValid(), isTrue());

	}

	/**
	 * Test toString().
	 */
	@Test
	public void testToString() {
		CodesTableItemPeriodic cti = new Ctip("t1code1", "t1decode1", new Date(), new Date(), Boolean.TRUE);
		assertThat(cti.toString(), isNotNull());
	}
	
	/**
	 * Test for boolean equals(Object).
	 */
	@Test
	public void testEqualsObject() {
		assertThat(TestCodesTableItemPeriodic.getCtip1().equals(TestCodesTableItemPeriodic.getCtip1()), isTrue());
		assertThat(TestCodesTableItemPeriodic.getCtip1().equals(null), isFalse());
		assertThat(TestCodesTableItemPeriodic.getCtip1().equals("String"), isFalse());
		assertThat(TestCodesTableItemPeriodic.getCtip1().equals(TestCodesTableItemPeriodic.getCtip2()), isFalse());
		assertThat(TestCodesTableItemPeriodic.getCtip1().equals(TestCodesTableItem.getCti2()), isFalse());
	}

	/**
	 * Test of hashCode().
	 */
	@Test
	public void testHashCode() {
		assertThat(TestCodesTableItemPeriodic.getCtip1().hashCode() == TestCodesTableItemPeriodic.getCtip5().hashCode(),
                isTrue());
		assertThat(TestCodesTableItemPeriodic.getCtip1().equals(TestCodesTableItemPeriodic.getCtip5()), isTrue());
	}

	/** Used for testing as {@link CodesTableItemPeriodic} cannot be instantiated. */
	private static class Ctip extends CodesTableItemPeriodic {
	
		//No real use in test, added to avoid warning
		private static final long serialVersionUID = 7653759985312507754L;

		public Ctip() {
		}		

		public Ctip(String code, String decode, Date fromDate, Date toDate, boolean isValid) {
			super(code, decode, fromDate, toDate, isValid);
		}
	}
}