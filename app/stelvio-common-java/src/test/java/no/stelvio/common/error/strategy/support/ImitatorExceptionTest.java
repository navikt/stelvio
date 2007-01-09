package no.stelvio.common.error.strategy.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.error.TestUnrecoverableException;

/**
 * Unit test for {@link ImitatorException}.
 *
 * @author personf8e9850ed756
 */
public class ImitatorExceptionTest {
    private ImitatorException imitator;

    @Test
    public void toStringShouldBeImitated() {
        assertEquals("The toString output is wrong", "java.lang.IllegalArgumentException (imitated): illegalArgument", imitator.toString());
    }

    @Test
    public void messageShouldBeEqualToImitatedException() {
        assertEquals("The messageFrom is wrong", "illegalArgument", imitator.getMessage());
    }

    @Test
    public void stacktraceElementsAreCopiedFromImitatedException() {
        TestUnrecoverableException thrown = createThrownException();

        ImitatorException imitatorException = new ImitatorException(thrown);
        assertEquals("Stack traces don't match up; ",
                thrown.getStackTrace(), imitatorException.getStackTrace());
    }

    @Test
    public void causeShouldNotBeCopied() {
        TestUnrecoverableException thrown = createThrownException();

        assertNull("Cause should be removed", new ImitatorException(thrown).getCause());
    }

    @Before
    public void createCommonImitator() throws Exception {
        imitator = new ImitatorException(new IllegalArgumentException("illegalArgument"));
    }

    private TestUnrecoverableException createThrownException() {
        try {
            throw new TestUnrecoverableException("state");
        } catch (TestUnrecoverableException e) {
            return e;
        }
    }
}
