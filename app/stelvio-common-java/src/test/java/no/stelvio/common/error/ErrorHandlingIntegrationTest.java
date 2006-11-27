package no.stelvio.common.error;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Integration test for error handling.
 *
 * @author personf8e9850ed756
 */
public class ErrorHandlingIntegrationTest extends AbstractDependencyInjectionSpringContextTests{
    private TestCatcher testCatcher;

    public void testShouldThrow() {
        try {
            testCatcher.catchException();
            fail("ImitatorException should have been thrown");
        } catch (RuntimeException e) {
            // Cannot catch the ImitatorException here as it is package private
            assertEquals("Wrong exception thrown",
                    "no.stelvio.common.error.strategy.support.ImitatorException", e.getClass().getName());
        }
    }

    protected String[] getConfigLocations() {
        return new String[] { "integration-test-context.xml" };
    }

    public void setTestCatcher(TestCatcher testCatcher) {
        this.testCatcher = testCatcher;
    }
}
