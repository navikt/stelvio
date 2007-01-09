package no.stelvio.common.error.message.support;

import no.stelvio.common.error.message.Extractor;

/**
 *
 * @author personf8e9850ed756
 * @todo better javadoc
 */
public class FromExceptionExtractor implements Extractor {
    /**
     * {@inheritDoc}
     */
    public String messageFrom(Throwable throwable) {
        // TODO: is this enough or should there be some more tests?
        return throwable.getLocalizedMessage();
    }
}
