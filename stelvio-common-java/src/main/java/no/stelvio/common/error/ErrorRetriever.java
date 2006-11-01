package no.stelvio.common.error;

import java.util.Collection;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public interface ErrorRetriever {
    Collection<Err> retrieve();
}
