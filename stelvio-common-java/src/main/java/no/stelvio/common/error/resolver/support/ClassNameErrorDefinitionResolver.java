package no.stelvio.common.error.resolver.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.support.ErrorDefinition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple ErrorDefinitionResolver that simply uses the class name of the supplied Throwable to match against an
 * ErrorDefinition.
 * 
 * If the collection passed to the constructor contains more than one ErrorDefinition with the same class name see
 * {@link ErrorDefinition#getClassName()} the instance of ErrorDefinition that will be returned is undefined.
 * 
 * This implementation should never be used if the collection of ErrorDefinitions passed through the constructor contains more
 * than one ErrorDefinition per exception class name.
 * 
 *
 */
public class ClassNameErrorDefinitionResolver implements ErrorDefinitionResolver {

	private Log log = LogFactory.getLog(ClassNameErrorDefinitionResolver.class);

	/**
	 * Map of class names corresponding to a ErrorDefinition.
	 */
	private Map<String, ErrorDefinition> classNameErrorDefinitionMap = new HashMap<String, ErrorDefinition>();

	/**
	 * Initializes this class by filling the map with Class name/ErrorDefinition key/value-pair.
	 * 
	 * @param errors
	 *            errors
	 */
	public ClassNameErrorDefinitionResolver(Collection<ErrorDefinition> errors) {
		for (ErrorDefinition definition : errors) {
			if (!classNameErrorDefinitionMap.containsKey(definition.getClassName())) {
				classNameErrorDefinitionMap.put(definition.getClassName(), definition);
			} else {
				log.info("ClassNameErrorDefinitionResolver doesn't support duplicate ErrorDefinitions"
						+ " for one exception class name. Duplicates exsists for: " + definition.getClassName());
			}
		}
	}

	/**
	 * Method that provided a Throwable resolves it's ErrorDefinition.
	 * 
	 * @param throwable
	 *            a throwable object
	 * @return ErrorDefinition for the specified Throwable, <code>null</code> if no match is found
	 */
	public ErrorDefinition resolve(Throwable throwable) {
		String throwableClassName = throwable.getClass().getName();
		if (classNameErrorDefinitionMap.containsKey(throwableClassName)) {
			return classNameErrorDefinitionMap.get(throwableClassName);
		} else {
			// No definition found
			return null;
		}
	}

}
