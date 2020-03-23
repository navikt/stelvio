package no.stelvio.common.error.resolver;

import no.stelvio.common.error.support.ErrorDefinition;

/**
 * Interface defining the contract for a class capable of resolving an <code>ErrorDefinition</code> from a
 * <code>Throwable</code>.
 *
 */
public interface ErrorDefinitionResolver {
	
	/**
	 * Resolves and returns the error definition from the throwable object.
	 * 
	 * @param throwable a Throwable
	 * @return ErrorDefinition for the specified Throwable, <code>null</code> if no match is found
	 */
	ErrorDefinition resolve(Throwable throwable);
}
