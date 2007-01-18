package no.stelvio.common.error.retriever;

import java.util.Collection;

import no.stelvio.common.error.support.ErrorDefinition;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public interface ErrorDefinitionRetriever {
    Collection<ErrorDefinition> retrieve() throws RetrieverFailedException;
}
