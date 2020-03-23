package no.stelvio.common.error;

/**
 * Test bean.
 * 
 */
public class TestBean {
	private TestThrower testThrower;

	/**
	 * Call test thrower.
	 */
	public void callThrower() {
		testThrower.throwException();
	}

	/**
	 * Set test thrower.
	 * 
	 * @param testThrower test thrower
	 */
	public void setTestThrower(final TestThrower testThrower) {
		this.testThrower = testThrower;
	}
}
