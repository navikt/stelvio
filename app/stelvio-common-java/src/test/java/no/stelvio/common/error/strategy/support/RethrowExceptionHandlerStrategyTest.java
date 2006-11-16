package no.stelvio.common.error.strategy.support;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.error.TestSystemException;

/**
 * Unit test for {@link RethrowExceptionHandlerStrategy}.
 *
 * @author personf8e9850ed756
 * @todo now we only use SystemException in the test, should RecoverableException also be used?
 */
public class RethrowExceptionHandlerStrategyTest {
    private RethrowExceptionHandlerStrategy rethrower;

    @Test
    public void causesIsExchangedWithImitatorInRethrownException() {
        handleException(new ExceptionHandler() {
            public void handle(TestSystemException original, TestSystemException copy) {
                for (Throwable cause = copy.getCause(); cause != null; cause = cause.getCause()) {
                    assertTrue("Cause should be of type ImitatorException", cause instanceof ImitatorException);
                }
            }
        });
    }

    @Test
    public void stacktraceElementsAreTheSameInRethrownException() {
        handleException(new ExceptionHandler() {
            public void handle(TestSystemException original, TestSystemException copy) {
                Throwable origCause = original.getCause();
                Throwable copyCause = copy.getCause();

                // Don't need the test specifically that the copy has the same number of causes as the original
                // as we will get a NullPointerException in this for loop if this is the case
                for (; origCause != null || copyCause != null;
                     origCause = origCause.getCause(), copyCause = copyCause.getCause()) {
                    assertEquals(origCause.getStackTrace(), copyCause.getStackTrace());
                }
            }
        });
    }

    @Before
    public void createRethrower() {
        rethrower = new RethrowExceptionHandlerStrategy();
    }

    private void handleException(ExceptionHandler exceptionHandler) {
        TestSystemException te = createThrownException();

        try {
            rethrower.handle(te);
            fail("Rethrower should throw the exception");
        } catch (TestSystemException e) {
            exceptionHandler.handle(te, e);
        }
    }

    private TestSystemException createThrownException() {
        try {
            a();
        } catch (TestSystemException e) {
            return e;
        }

        // Will never happen
        return null;
    }

    private void a() {
        try {
            b();
        } catch (TestSystemException e) {
            throw new TestSystemException(e, "b()");
        }
    }

    private void b() {
        try {
            c();
        } catch (TestSystemException e) {
            throw new TestSystemException(e, "c()");
        }
    }

    private void c() {
        d();
    }

    private void d() {
        throw new TestSystemException(new IllegalArgumentException(), "d()");
    }

    private static interface ExceptionHandler {
        void handle(TestSystemException original, TestSystemException copy);
    }
}
