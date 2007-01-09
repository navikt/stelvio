package no.stelvio.common.error.support;

import no.stelvio.common.error.ErrorDefinition;

import java.util.Collection;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 * @todo rename to ErrorDefinitionRetriever
 */
public interface ErrorsRetriever {
    Collection<ErrorDefinition> retrieve();
}
