package no.nav.common.framework.error;

import no.nav.common.framework.error.ErrorCode;
import junit.framework.TestCase;

/**
 * Unit test of ErrorCode.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1019 $ $Author: psa2920 $ $Date: 2004-08-13 10:35:43 +0200 (Fri, 13 Aug 2004) $
 */
public class ErrorCodeTest extends TestCase {

	/**
	 * Constructor for ErrorCodeTest.
	 * @param arg0
	 */
	public ErrorCodeTest(String arg0) {
		super(arg0);
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		super.assertTrue("a.equals(a) should have matched", ErrorCode.UNSPECIFIED_ERROR.equals(ErrorCode.UNSPECIFIED_ERROR));
		super.assertFalse("a.equals(null) should not have matched", ErrorCode.UNSPECIFIED_ERROR.equals(null));
		super.assertFalse("a.equals(Object) should not have matched", ErrorCode.UNSPECIFIED_ERROR.equals("String"));

		super.assertTrue(
			"code_0.equals(another_code_0) should have matched",
			TestError.ERR_CODE_IS_0.equals(TestError.ERR_CODE_IS_0_TOO));

		super.assertTrue(
			"code_0.equals(another_code_0_in_another_class) should have matched",
			TestError.ERR_CODE_IS_0.equals(ErrorCode.UNSPECIFIED_ERROR));

		super.assertFalse(
			"code_0.equals(code_1) should not have matched",
			TestError.ERR_CODE_IS_0.equals(TestError.ERR_CODE_IS_1));
	}

	public void testHashCode() {
		assertEquals(
			"Equals is true, then hashCode is true",
			TestError.ERR_CODE_IS_0.equals(ErrorCode.UNSPECIFIED_ERROR),
			TestError.ERR_CODE_IS_0.hashCode() == ErrorCode.UNSPECIFIED_ERROR.hashCode());
	}
}
