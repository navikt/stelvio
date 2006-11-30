package no.stelvio.common.codestable;


import java.util.Date;
import java.util.Locale;

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
		CodesTableItemPeriodic cti = new CodesTableItemPeriodic();
		
		assertThat(cti.getCode(), isNull());
		assertThat(cti.getDecode(), isNull());
		assertThat(cti.getFromDate(), isNull());
		assertThat(cti.getToDate(), isNull());
		assertThat(cti.getLocale(), isNull());
		assertThat(cti.getIsValid(), isNull());
	}

	/**
	 * Test of constructor for CodesTableItem()
	 */
	@Test
	public void testCodesTableItemConstrutor() {
		
		Date date = new Date();
		Locale locale = new Locale("nb", "NO");
		
		CodesTableItemPeriodic cti = new CodesTableItemPeriodic("t1code1", "t1decode1", date, date, locale, Boolean.TRUE);
		
		assertThat(cti.getCode(), eq("t1code1"));
		assertThat(cti.getDecode(), eq("t1decode1"));
		assertThat(cti.getFromDate(), eq(date));
		assertThat(cti.getToDate(), eq(date));
        assertThat(cti.getLocale(), eq(locale));
		assertThat(cti.getIsValid(), isTrue());

	}

	/**
	 * Test of CodesTableItem's getters and setters.
	 */
	@Test
	public void testSettersAndGetters() {
		
		Date date = new Date();
		Locale locale = new Locale("nb", "NO");
		
		CodesTableItemPeriodic cti = new CodesTableItemPeriodic();
		cti.setCode("t1code1");
		cti.setDecode("t1decode1");
		cti.setFromDate(date);
		cti.setToDate(date);
		cti.setLocale(locale);
		cti.setIsValid(Boolean.TRUE);

        assertThat(cti.getCode(), eq("t1code1"));
        assertThat(cti.getDecode(), eq("t1decode1"));
        assertThat(cti.getFromDate(), eq(date));
        assertThat(cti.getToDate(), eq(date));
		assertThat(cti.getLocale(), eq(locale));
		assertThat(cti.getIsValid(), isTrue());
	}

	/**
	 * Test toString().
	 */
	@Test
	public void testToString() {
		CodesTableItemPeriodic cti = new CodesTableItemPeriodic("t1code1", "t1decode1", new Date(), new Date(), new Locale("nb", "NO"), Boolean.TRUE);
		assertThat(cti.toString(), isNotNull());
	}
	
	/**
	 * Test for boolean equals(Object).
	 */
	@Test
	public void testEqualsObject() {
	
		assertThat(TestCodesTableItemPeriodic.CTIP1.equals(TestCodesTableItemPeriodic.CTIP1), isTrue());
		assertThat(TestCodesTableItemPeriodic.CTIP1.equals(null), isFalse());
		assertThat(TestCodesTableItemPeriodic.CTIP1.equals("String"), isFalse());
		assertThat(TestCodesTableItemPeriodic.CTIP1.equals(TestCodesTableItemPeriodic.CTIP2), isFalse());
		assertThat(TestCodesTableItemPeriodic.CTIP1.equals(TestCodesTableItem.CTI2), isFalse());
	}

	/**
	 * Test of hashCode().
	 */
	@Test
	public void testHashCode() {
		assertThat(TestCodesTableItemPeriodic.CTIP1.hashCode() == TestCodesTableItemPeriodic.CTIP5.hashCode(),
                isTrue());
		assertThat(TestCodesTableItemPeriodic.CTIP1.equals(TestCodesTableItemPeriodic.CTIP5), isTrue());
	}
}