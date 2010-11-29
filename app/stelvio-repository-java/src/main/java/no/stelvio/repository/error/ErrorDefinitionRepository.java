package no.stelvio.repository.error;

import java.util.Set;

import no.stelvio.common.error.support.ErrorDefinition;

/**
 * Defines the interfaces to be implemented for retrieving error definitions from a repository.
 *
 * @author personf8e9850ed756
 */
public interface ErrorDefinitionRepository {
	/**
	 * Retrieves a set of <code>ErrorDefinition</code>s from a repository. These will be used in error handling.
	 *
	 * @return a set of <code>ErrorDefinition</code>s.
	 * @see ErrorDefinition
	 */
	Set<ErrorDefinition> findErrorDefinitions();
}
