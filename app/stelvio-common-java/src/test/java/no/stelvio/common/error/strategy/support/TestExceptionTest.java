package no.stelvio.common.error.strategy.support;

import org.junit.Test;

/**
 * TODO: Add description
 */
public class TestExceptionTest {
    @Test
    public void sdfSdf() {
        TestException te = new TestException("state");
        new TestException(te, "sdf");
    }
}
