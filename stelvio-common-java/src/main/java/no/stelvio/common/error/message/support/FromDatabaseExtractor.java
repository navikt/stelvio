package no.stelvio.common.error.message.support;

import no.stelvio.common.error.message.Extractor;
import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.support.ErrorDefinition;

/**
 * Finds the message associated with the given exception by using the database
 * provided list of <code>ErrorDefinition</code>s. If the exception is not found
 * in the list, use a fallback <code>Extractor</code>.
 * 
 * @author personf8e9850ed756
 * @see ErrorDefinition
 * @see Extractor
 */
public class FromDatabaseExtractor implements Extractor {
	private ErrorDefinitionResolver errorDefinitionResolver;

	/**
	 * <code>Extractor</code> that extracts message directly from the exception
	 * is used if nothing is specified.
	 * 
	 * @see FromExceptionExtractor
	 */
	private Extractor fallbackExtractor = new FromExceptionExtractor();

	/**
	 * Resolves the error from the exception and returns a message depending on
	 * the exception.
	 * 
	 * If the error definition is null, then it returns the message for the
	 * fallback extractor. Otherwise it returns the message from the error
	 * definition, with slightly more processing for thStelvioExceptions.
	 * 
	 * now this supports having non-stelvio exceptions
	 *         in the database; is this likely? Otherwise we could just jump
	 *         directly onto fallback if a non-stelvio exception.
	 *         
	 * @param throwable
	 *            an exception
	 * @return the message 
	 */
	@Override
	public String messageFor(Throwable throwable) {
		ErrorDefinition errorDefinition = errorDefinitionResolver.resolve(throwable);

		if (null == errorDefinition) {
			return fallbackExtractor.messageFor(throwable);
		} else {
			return errorDefinition.getMessage();
		}
	}

	/**
	 * Sets the error resolver.
	 * 
	 * @param errorDefinitionResolver
	 *            the error definition resolver
	 */
	public void setErrorResolver(ErrorDefinitionResolver errorDefinitionResolver) {
		this.errorDefinitionResolver = errorDefinitionResolver;
	}

	/**
	 * Sets the fallback extractor.
	 * 
	 * @param fallbackExtractor
	 *            the fallback extractor
	 */
	public void setFallbackExtractor(Extractor fallbackExtractor) {
		this.fallbackExtractor = fallbackExtractor;
	}

}
