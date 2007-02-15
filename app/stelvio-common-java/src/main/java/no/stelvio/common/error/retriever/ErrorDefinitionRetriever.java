package no.stelvio.common.error.retriever;

import java.util.Set;

import no.stelvio.common.error.support.ErrorDefinition;

/**
 * Defines the interfaces to be implemented for retrieving error definitions.
 *
 * @author personf8e9850ed756
 */
public interface ErrorDefinitionRetriever {
	/**
	 * Retrieves a set of <code>ErrorDefinitions</code>s to be used in error handling.
	 *
	 * @return a set of <code>ErrorDefinitions</code>s.
	 * @throws RetrieverFailedException if retrieving error definitions failed.
	 * @see ErrorDefinition
	 */
    Set<ErrorDefinition> retrieve() throws RetrieverFailedException;
}
