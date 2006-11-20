package no.stelvio.common.error;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class TestCatcher {
    private TestThrower testThrower;

    public void catchException() {
        testThrower.throwException();
    }

    public void setTestThrower(TestThrower testThrower) {
        this.testThrower = testThrower;
    }
}
