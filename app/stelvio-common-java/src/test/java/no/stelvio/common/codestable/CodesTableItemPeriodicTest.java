package no.stelvio.common.codestable;


import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.eq;
import static org.hamcrest.core.IsEqual.isFalse;
import static org.hamcrest.core.IsEqual.isTrue;
import static org.hamcrest.core.IsNull.isNotNull;
import static org.hamcrest.core.IsNull.isNull;
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
		Ctip cti = new Ctip();

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

		Ctip cti = new Ctip(TestCtiCode.EXISTS_1, "t1decode1", date, date, Boolean.TRUE);

		assertThat(cti.getCode(), eq(TestCtiCode.EXISTS_1));
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
		Ctip cti = new Ctip(TestCtiCode.EXISTS_1, "t1decode1", new Date(), new Date(), Boolean.TRUE);
		assertThat(cti.toString(), isNotNull());
	}
	
	/**
	 * Test for boolean equals(Object).
	 */
	@Test
	public void testEqualsObject() {
		assertThat(TestCtiPeriodic.getCtip1().equals(TestCtiPeriodic.getCtip1()), isTrue());
		assertThat(TestCtiPeriodic.getCtip1().equals(null), isFalse());
		assertThat(TestCtiPeriodic.getCtip1().equals("String"), isFalse());
		assertThat(TestCtiPeriodic.getCtip1().equals(TestCtiPeriodic.getCtip2()), isFalse());
		assertThat(TestCtiPeriodic.getCtip1().equals(TestCti.getCti2()), isFalse());
	}

	/**
	 * Test of hashCode().
	 */
	@Test
	public void testHashCode() {
		assertThat(TestCtiPeriodic.getCtip1().hashCode() == TestCtiPeriodic.getCtip5().hashCode(),
                isTrue());
		assertThat(TestCtiPeriodic.getCtip1().equals(TestCtiPeriodic.getCtip5()), isTrue());
	}

	/** Used for testing as {@link CodesTableItemPeriodic} cannot be instantiated. */
	private static class Ctip extends CodesTableItemPeriodic<TestCtiCode, String> {
	
		//No real use in test, added to avoid warning
		private static final long serialVersionUID = 7653759985312507754L;

		public Ctip() {
		}		

		public Ctip(TestCtiCode code, String decode, Date fromDate, Date toDate, boolean isValid) {
			super(code, decode, fromDate, toDate, isValid);
		}
	}
}