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

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.error.TestRecoverableException;
import no.stelvio.common.error.TestUnrecoverableException;
import no.stelvio.common.error.message.Extractor;
import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.support.ErrorDefinition;

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

	@Test
	public void messageContainsRequestContextValues() {
		
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

		RequestContext requestContext = new SimpleRequestContext("ScreenId", null, "ProcessId", "TransactionId");
		RequestContextHolder.setRequestContext(requestContext);
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
            one (extractor).messageFor(with(a(Throwable.class))); will(returnValue("test"));
        }});

        return extractor;
    }

    private ErrorDefinitionResolver createErrorResolverMock() {
        final ErrorDefinitionResolver errorDefinitionResolver = context.mock(ErrorDefinitionResolver.class);
        final ErrorDefinition result = new ErrorDefinition.Builder(String.class).message("test: {0}").build();

        context.expects(new InAnyOrder() {{
            one (errorDefinitionResolver).resolve(with(a(Throwable.class))); will(returnValue(result));
            one (errorDefinitionResolver).resolve(with(a(Throwable.class))); will(returnValue(result));
        }});

        return errorDefinitionResolver;
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
