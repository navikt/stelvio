package no.stelvio.common.error.strategy.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.error.TestUnrecoverableException;

/**
 * Unit test for {@link MorpherExceptionHandlerStrategy}.
 *
 * @author personf8e9850ed756
 * @todo now we only use UnrecoverableException in the test, should RecoverableException also be used?
 */
public class MorpherExceptionHandlerStrategyTest {
    private MorpherExceptionHandlerStrategy rethrower;

    @Test
    public void causesIsExchangedWithImitatorInRethrownException() {
        handleException(new ExceptionHandler() {
            public void handle(TestUnrecoverableException original, TestUnrecoverableException copy) {
                for (Throwable cause = copy.getCause(); cause != null; cause = cause.getCause()) {
                    assertTrue("Cause should be of type ImitatorException", cause instanceof ImitatorException);
                }
            }
        });
    }

    @Test
    public void stacktraceElementsAreTheSameInRethrownException() {
        handleException(new ExceptionHandler() {
            public void handle(TestUnrecoverableException original, TestUnrecoverableException copy) {
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
	public void setupRequestContext() {
		RequestContextHolder.setRequestContext(
				new RequestContext("userId", "screenId", "moduleId", "processId", "transactionId"));
	}

    @Before
    public void createRethrower() {
        rethrower = new MorpherExceptionHandlerStrategy();
    }

    private void handleException(ExceptionHandler exceptionHandler) {
        exceptionHandler.handle(createThrownException(), rethrower.handleException(createThrownException()));
    }

    private TestUnrecoverableException createThrownException() {
        try {
            a();
        } catch (TestUnrecoverableException e) {
            return e;
        }

        // Will never happen
        return null;
    }

    private void a() {
        try {
            b();
        } catch (TestUnrecoverableException e) {
            throw new TestUnrecoverableException(e, "b()");
        }
    }

    private void b() {
        try {
            c();
        } catch (TestUnrecoverableException e) {
            throw new TestUnrecoverableException(e, "c()");
        }
    }

    private void c() {
        d();
    }

    private void d() {
        throw new TestUnrecoverableException(new IllegalArgumentException(), "d()");
    }

    private static interface ExceptionHandler {
        void handle(TestUnrecoverableException original, TestUnrecoverableException copy);
    }
}
