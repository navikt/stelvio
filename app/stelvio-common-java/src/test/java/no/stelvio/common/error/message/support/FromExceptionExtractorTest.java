package no.stelvio.common.error.message.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.eq;
import org.junit.Test;

import no.stelvio.common.error.TestUnrecoverableException;
import no.stelvio.common.error.message.Extractor;
import no.stelvio.common.error.message.ExtractorTest;

/**
 * Unit test for {@lnk FromExceptionExtractor}.
 *
 * @author personf8e9850ed756
 */
public class FromExceptionExtractorTest extends ExtractorTest {
    private final FromExceptionExtractor fromExceptionExtractor = new FromExceptionExtractor();

    @Test
    public void messageIsExtractedFromException() {
        String message = fromExceptionExtractor.messageFor(new TestUnrecoverableException("error"));

        assertThat(message, eq("dummy: error"));
    }

    protected Extractor extractor() {
        return fromExceptionExtractor;
    }
}
