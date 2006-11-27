package no.stelvio.common.error.support;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import org.junit.Test;

import no.stelvio.common.error.ExceptionHandlerFacade;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public class ErrorHandlingAdviceTest {
    @Test
    public void exceptionIsSentToFacade() throws Throwable {
        Mockery context = new Mockery();
        final ExceptionHandlerFacade facade = context.mock(ExceptionHandlerFacade.class);
        final IllegalArgumentException throwable = new IllegalArgumentException("test");

        context.expects(new InAnyOrder() {{
            one (facade).handleException(with(same(throwable)));
        }});

        ErrorHandlingAdvice advice = new ErrorHandlingAdvice();
        advice.setExceptionHandlerFacade(facade);

        advice.afterThrowing(throwable);
        context.assertIsSatisfied();
    }
}
