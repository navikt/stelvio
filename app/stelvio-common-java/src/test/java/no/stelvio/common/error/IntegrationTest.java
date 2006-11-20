package no.stelvio.common.error;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class IntegrationTest extends AbstractDependencyInjectionSpringContextTests{
    private TestThrower testThrower;

    public void testShouldThrow() {
        // How to catch it went ok? First just get it to work
        testThrower.throwException();
    }

    protected String[] getConfigLocations() {
        return new String[0];  // TODO: implement body
    }

    public void setTestThrower(TestThrower testThrower) {
        this.testThrower = testThrower;
    }
}
