package no.stelvio.common.error.strategy.support;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.error.SystemException;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Unit test for {@link RethrowExceptionHandlerStrategy}.
 *
 * @author personf8e9850ed756
 */
public class RethrowExceptionHandlerStrategyTest {
    private RethrowExceptionHandlerStrategy rethrower;

    @Test
    public void removeCause() {
        TestException te = null;
        TestException expected = null;

        try {
            a();
        } catch (TestException e) {
            expected = e;
            // Calling copy constructor
            // in rethrow strategy, the stack trace elements for the exception should be copied over too, not just for the cause
            te = new TestException(new ExceptionToCopyHolder<SystemException>(e));
            // TODO should imitate output too with the name of the original exception within
            // should imitate the cause
            te.initCause(new ImitatorException(e.getCause()));
        }

        // init stack trace
        te.getStackTrace();
        expected.getStackTrace();

        // TODO remember that err id should be the same also
        assertEquals("Stacktrace elements should be equal", expected.getCause().getStackTrace(), te.getCause().getStackTrace());
        assertNotNull("Exception should have a cause", te.getCause());
        assertNull("Exception's cause should not have a cause", te.getCause().getCause());

        te.printStackTrace();
        expected.printStackTrace();
    }

    @Test
    public void causesIsExchangedWithImitatorInRethrownException() {
        TestException te = createThrownException();
        SystemException copy = null;

        try {
            rethrower.handle(te);
        } catch (SystemException e) {
            copy = e;
        }

        System.out.println("=================");
        copy.printStackTrace();
        System.out.println("=================");
        for (Throwable cause = copy.getCause(); cause != null; cause = cause.getCause()) {
            assertTrue("Cause should be of type ImitatorException", cause instanceof ImitatorException);
        }
    }

    @Test
    public void stacktraceElementsAreTheSameInRethrownException() {
//        createRethrower()
    }

    @Before
    public void createRethrower() {
        rethrower = new RethrowExceptionHandlerStrategy();
    }

    private TestException createThrownException() {
        try {
            a();
        } catch (TestException e) {
            return e;
        }

        // Will never happen
        return null;
    }

    private void a() {
        try {
            b();
        } catch (TestException e) {
            throw new TestException(e, "b()");
        }
    }

    private void b() {
        try {
            c();
        } catch (TestException e) {
            throw new TestException(e, "c()");
        }
    }

    private void c() {
        d();
    }

    private void d() {
        throw new TestException(new IllegalArgumentException(), "d()");
    }

}
