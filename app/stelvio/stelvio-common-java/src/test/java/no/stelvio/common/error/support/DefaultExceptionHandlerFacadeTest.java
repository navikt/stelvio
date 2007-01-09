package no.stelvio.common.error.support;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import static org.junit.Assert.fail;
import org.junit.Test;

import no.stelvio.common.error.ExceptionHandlerFacade;
import no.stelvio.common.error.ExceptionHandlerFacadeTest;
import no.stelvio.common.error.SystemUnrecoverableException;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class DefaultExceptionHandlerFacadeTest extends ExceptionHandlerFacadeTest {
    @Test
    public void fallbackStrategyIsUsedIfDefaultThrowsException() throws Throwable {
        Mockery context = new Mockery();
        final ExceptionHandlerStrategy facadeMock = context.mock(ExceptionHandlerStrategy.class);
        final IllegalStateException exception = new IllegalStateException("test");

        context.expects(new InAnyOrder() {{
            one (facadeMock).handleException((Throwable)with(anything())); will(returnValue(exception));
        }});

        DefaultExceptionHandlerFacade facade = new DefaultExceptionHandlerFacade();
        facade.setFallbackStrategy(facadeMock);
        facade.setDefaultStrategy(new ExceptionHandlerStrategy() {
            public <T extends Throwable> T handleException(T e) {
                throw new IllegalStateException("test");
            }
        });

        try {
            facade.handle(exception);
            fail("IllegalStateException should have benne thrown");
        } catch (IllegalStateException e) {
            // should happen
        }

        context.assertIsSatisfied();
    }

    @Test
    public void shouldThrowException() throws Throwable {
        ExceptionHandlerFacade facade = exceptionHandlerFacade();

        try {
            facade.rethrow(new IllegalStateException("test"));
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // Should happen
        }

        try {
            facade.throwNew(TestSystemUnrecoverableException.class, new IllegalArgumentException("iae"), "test");
            fail("Should throw SystemUnrecoverableException");
        } catch (SystemUnrecoverableException e) {
            // Should happen
            // LATER check that cause is correct
        }
    }

    protected ExceptionHandlerFacade exceptionHandlerFacade() {
        DefaultExceptionHandlerFacade facade = new DefaultExceptionHandlerFacade();

        facade.setDefaultStrategy(new ExceptionHandlerStrategy() {
            public <T extends Throwable> T handleException(T e) {
                return e;
            }
        });

        return facade;
    }

    public static class TestSystemUnrecoverableException extends SystemUnrecoverableException {
        protected TestSystemUnrecoverableException(IllegalArgumentException cause, String dummy) {
            super(cause, dummy);
        }

        protected TestSystemUnrecoverableException(ExceptionToCopyHolder holder) {
            super(holder);
        }

        protected String messageTemplate(final int numArgs) {
            return "dummy: {0}";
        }
    }
}
