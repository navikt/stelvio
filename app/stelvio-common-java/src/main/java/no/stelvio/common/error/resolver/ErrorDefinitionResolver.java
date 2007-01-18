package no.stelvio.common.error.resolver;

import no.stelvio.common.error.support.ErrorDefinition;

/**
 * @author personf8e9850ed756
 * @todo write javadoc
 */
public interface ErrorDefinitionResolver {
    ErrorDefinition resolve(Throwable throwable);
}
