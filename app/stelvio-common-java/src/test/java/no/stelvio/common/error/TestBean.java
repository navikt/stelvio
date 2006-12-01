package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class TestBean {
	private TestThrower testThrower;

	public void callThrower() {
		testThrower.throwException();
	}

	public void setTestThrower(final TestThrower testThrower) {
		this.testThrower = testThrower;
	}
}
