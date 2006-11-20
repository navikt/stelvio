package no.stelvio.common.error.support;

import java.util.Collection;

import no.stelvio.common.error.Err;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public interface ErrorsRetriever {
    Collection<Err> retrieve();
}
