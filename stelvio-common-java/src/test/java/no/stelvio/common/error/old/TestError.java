package no.stelvio.common.error.old;

/**
 * ErrorCodes for testing.
 * 
 * @author person7553f5959484
 * @version $Revision: 1029 $ $Author: psa2920 $ $Date: 2004-08-16 12:08:48 +0200 (Mon, 16 Aug 2004) $
 */
public class TestError extends ErrorCode {

	protected TestError(int code) {
		super(code);
	}
	public static final TestError ERR_CODE_IS_0 = new TestError(0);
	public static final TestError ERR_CODE_IS_0_TOO = new TestError(0);
	public static final TestError ERR_CODE_IS_1 = new TestError(1);

	public static final TestError ERR_100000 = new TestError(100000);
	public static final TestError ERR_200000 = new TestError(200000);
	public static final TestError ERR_300000 = new TestError(300000);

	public static final TestError ERR_999999 = new TestError(999999);
}
