package no.stelvio.common.error.resolver.support;

import java.util.Collection;

import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.support.ErrorDefinition;

/**
 * Used by the {@link ErrorDefinitionResolverFactoryBean} when it is unable to initialize an intelligent ErrorDefinition.
 * 
 * This implementation will always return null
 * 
 *
 */
public class NullErrorDefinitionResolver implements ErrorDefinitionResolver {

	/**
	 * This constructor is only included to be used by the {@link ErrorDefinitionResolverFactoryBean}. if some one for some
	 * reason wants to explicitly specify the NullErrorDefinitionResolver as the ErrorDefinitionResolver.
	 * 
	 * @param errors
	 *            errors
	 */
	public NullErrorDefinitionResolver(Collection<ErrorDefinition> errors) {
		// Do nothing!! Will always return null, no matter what!
		this();
	}

	/**
	 * Creates a new instance of NullErrorDefinitionResolver.
	 */
	public NullErrorDefinitionResolver() {

	}

	/**
	 * Resolve. This metgod always return null.
	 * 
	 * @param throwable throwable
	 * @return error definition
	 */
	public ErrorDefinition resolve(Throwable throwable) {
		return null;
	}
}
