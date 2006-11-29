package no.stelvio.common.error.strategy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.LogFactoryImpl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsSame.same;
import static org.hamcrest.text.StringContains.containsString;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.error.ErrorDefinition;
import no.stelvio.common.error.ErrorResolver;
import no.stelvio.common.error.TestRecoverableException;
import no.stelvio.common.error.TestUnrecoverableException;
import no.stelvio.common.error.message.Extractor;

/**
 * Unit test of {@link LoggerExceptionHandlerStrategy}.
 * 
 * @author personcb9a87e24a5f
 * @todo fails because the setup of expectations must be all done in one block.
 */
public class LoggerExceptionHandlerStrategyTest {
    private LoggerExceptionHandlerStrategy leh;
    private Mockery context;
    private Log log;

    @Test
    public void exceptionShouldBeMarkedLogged() {
        TestUnrecoverableException systemException = leh.handleException(new TestUnrecoverableException("error"));

        assertTrue("Should have been marked as logged", systemException.isLogged());
        context.assertIsSatisfied();
    }

    @Test
    public void sameExceptionIsReturned() throws Throwable {
        Throwable systemException = new TestUnrecoverableException("error");
        Throwable throwable = leh.handleException(systemException);

        assertThat(throwable, same(systemException));
        context.assertIsSatisfied();
    }

    @Test
    public void messageIsCreatedFromTemplateInErrorsTableAndLogged() throws TestRecoverableException {
        context.expects(new InAnyOrder() {{
            one (log).debug("test");
        }});

        leh.handleException(new TestRecoverableException("error"));
        context.assertIsSatisfied();
    }

    @Test
    public void messageContainsArgumentsToException() throws TestRecoverableException {
        context.expects(new InAnyOrder() {{
            one (log).debug(with(containsString("error")));
        }});

        leh.handleException(new TestRecoverableException("error"));
        context.assertIsSatisfied();
    }

    @Before
    public void setupForTest() {
        context = new Mockery();
        leh = createInstanceToTest();

        System.setProperty("org.apache.commons.logging.LogFactory",
                "no.stelvio.common.error.strategy.support.LoggerExceptionHandlerStrategyTest$TestLogFactory");
        LogFactory.releaseAll();
        ((TestLogFactory) LogFactory.getFactory()).setLog(log);
        log = context.mock(Log.class);
	}

    private LoggerExceptionHandlerStrategy createInstanceToTest() {
        LoggerExceptionHandlerStrategy leh = new LoggerExceptionHandlerStrategy();
        leh.setExtractor(createExtractorMock());
        leh.setErrorResolver(createErrorResolverMock());
        return leh;
    }

    private Extractor createExtractorMock() {
        final Extractor extractor = context.mock(Extractor.class);

        context.expects(new InAnyOrder() {{
            one (extractor).messageFrom(with(a(Throwable.class))); will(returnValue("test"));
        }});

        return extractor;
    }

    private ErrorResolver createErrorResolverMock() {
        final ErrorResolver errorResolver = context.mock(ErrorResolver.class);
        final ErrorDefinition result = new ErrorDefinition.Builder(String.class).message("test: {0}").build();

        context.expects(new InAnyOrder() {{
            one (errorResolver).resolve(with(a(Throwable.class))); will(returnValue(result));
            one (errorResolver).resolve(with(a(Throwable.class))); will(returnValue(result));
        }});

        return errorResolver;
    }

    public static class TestLogFactory extends LogFactoryImpl {
        private Log log;

        public Log getInstance(Class clazz) {
            return log;
        }

        public void setLog(Log log) {
            this.log = log;
        }
    }
}
