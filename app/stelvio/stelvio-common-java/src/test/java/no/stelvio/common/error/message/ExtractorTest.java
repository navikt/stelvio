package no.stelvio.common.error.message;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.isNotNull;
import org.junit.Test;

import no.stelvio.common.error.TestUnrecoverableException;

/**
 * Common test for unit tests for implementations of {@link Extractor}.
 *
 * @author personf8e9850ed756
 */
public abstract class ExtractorTest {
    @Test
    public void messageIsNotNull() {
        assertThat(testMessage(new TestUnrecoverableException("error")), isNotNull());
    }

    protected String testMessage(final Exception exception) {
        return extractor().messageFrom(exception);
    }

    protected abstract Extractor extractor();
}
