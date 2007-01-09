package no.stelvio.common.error.message.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.eq;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import no.stelvio.common.error.ErrorResolver;
import no.stelvio.common.error.FunctionalRecoverableException;
import no.stelvio.common.error.TestRecoverableException;
import no.stelvio.common.error.TestUnrecoverableException;
import no.stelvio.common.error.message.Extractor;
import no.stelvio.common.error.message.ExtractorTest;

/**
 * Unit test for {@link FromDatabaseExtractor}.
 *
 * @author personf8e9850ed756
 * @todo fails because the setup of expectations must be all done in one block. 
 */
public class FromDatabaseExtractorTest extends ExtractorTest {
    private Mockery context;
    private FromDatabaseExtractor fromDatabaseExtractor;

    @Test
    public void messageIsExtractedFromDatabase() {
        String message = testMessage(new TestUnrecoverableException("fromDb"));
        assertThat(message, eq("error message"));
        context.assertIsSatisfied();
    }

    @Test
    public void ifExceptionIsNotFoundUseFallbackExtractor() {
        fromDatabaseExtractor.setFallback(createFallbackMock());

        String message = testMessage(new TestRecoverableException("error"));
        assertThat(message, eq("fallback"));
        context.assertIsSatisfied();
    }

    @Before
    public void setupInstance() {
        context = new Mockery();
        final ErrorResolver errorResolver = context.mock(ErrorResolver.class);

        context.expects(new InAnyOrder() {{
            one (errorResolver).resolve(with(a(FunctionalRecoverableException.class)));
        }});

        fromDatabaseExtractor = new FromDatabaseExtractor();
        fromDatabaseExtractor.setErrorResolver(errorResolver);
    }

    private Extractor createFallbackMock() {
        final Extractor extractor = context.mock(Extractor.class);

        context.expects(new InAnyOrder() {{
            allowing (extractor).messageFrom(with(an(TestRecoverableException.class))); will(returnValue("fallback"));
        }});

        return extractor;
    }

    protected Extractor extractor() {
        return fromDatabaseExtractor;
    }
}
