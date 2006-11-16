package no.stelvio.common.error.strategy.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.error.TestSystemException;

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
        assertEquals("The message is wrong", "illegalArgument", imitator.getMessage());
    }

    @Test
    public void stacktraceElementsAreCopiedFromImitatedException() {
        TestSystemException thrown = createThrownException();

        ImitatorException imitatorException = new ImitatorException(thrown);
        assertEquals("Stack traces don't match up; ",
                thrown.getStackTrace(), imitatorException.getStackTrace());
    }

    @Test
    public void causeShouldNotBeCopied() {
        TestSystemException thrown = createThrownException();

        assertNull("Cause should be removed", new ImitatorException(thrown).getCause());
    }

    @Before
    public void createCommonImitator() throws Exception {
        imitator = new ImitatorException(new IllegalArgumentException("illegalArgument"));
    }

    private TestSystemException createThrownException() {
        try {
            throw new TestSystemException("state");
        } catch (TestSystemException e) {
            return e;
        }
    }
}
