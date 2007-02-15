package no.stelvio.service.error;

import java.util.Set;

import no.stelvio.common.error.retriever.ErrorDefinitionRetriever;
import no.stelvio.common.error.retriever.RetrieverFailedException;
import no.stelvio.common.error.support.ErrorDefinition;
import no.stelvio.repository.error.ErrorDefinitionRepository;

/**
 * Forwards retrieval of error definitions to a repository class.
 *
 * @author personf8e9850ed756, Accenture
 */
public class RepositoryErrorDefinitionRetriever implements ErrorDefinitionRetriever {
	private ErrorDefinitionRepository errorDefinitionRepository;

	/** {@inheritDoc} */
	public Set<ErrorDefinition> retrieveAll() throws RetrieverFailedException {
		return errorDefinitionRepository.findErrorDefinitions();
	}

	/**
	 * Sets the errorDefinitionRepository instance to use to retrieve error definitions.
	 *
	 * @param errorDefinitionRepository value to set on errorDefinitionRepository.
	 */
	public void setErrorDefinitionRepository(ErrorDefinitionRepository errorDefinitionRepository) {
		this.errorDefinitionRepository = errorDefinitionRepository;
	}
}
