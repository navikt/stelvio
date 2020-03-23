package no.stelvio.common.error.resolver.support;

import java.lang.reflect.Constructor;
import java.util.Collection;

import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.retriever.ErrorDefinitionRetriever;
import no.stelvio.common.error.support.ErrorDefinition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Simple factory for an error resolver. Allows for the the classes needing the error resolver not having to worry about
 * creating it; they just need to setup a dependency to a definition of this class.
 * 
 */
public class ErrorDefinitionResolverFactoryBean implements FactoryBean, InitializingBean {
	private ErrorDefinitionRetriever errorDefinitionRetriever;

	private ErrorDefinitionResolver errorDefinitionResolver;

	private static Log log = LogFactory.getLog(ErrorDefinitionResolverFactoryBean.class);

	/**
	 * Default implementation of the <code>ErrorDefinitionResolver</code> is used if not specified.
	 * 
	 * @see StaticErrorDefinitionResolver
	 */
	private Class<? extends ErrorDefinitionResolver> errorResolverClass = StaticErrorDefinitionResolver.class;

	/**
	 * Returns the ErrorDefinitionResolver. If it is not initialized it initializes it first.
	 * 
	 * @return the ErrorDefinitionResolver object.
	 */
	public Object getObject() {
		return errorDefinitionResolver;
	}

	/**
	 * Returns the ErrorDefinitionResolver class.
	 * 
	 * @return the ErrorDefinitionResolver class
	 */
	public Class getObjectType() {
		return ErrorDefinitionResolver.class;
	}

	/**
	 * Returns true to indicate that the object is a singleton.
	 * 
	 * @return true
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Tries to initialize the {@link ErrorDefinitionResolver} on starup.
	 * 
	 */
	public void afterPropertiesSet() {
		initializeResolver();
	}

	/**
	 * Sets the error definition retriever.
	 * 
	 * @param errorDefinitionRetriever
	 *            the error definition retriever
	 */
	public void setErrorDefinitionRetriever(ErrorDefinitionRetriever errorDefinitionRetriever) {
		this.errorDefinitionRetriever = errorDefinitionRetriever;
	}

	/**
	 * Sets the error resolver class.
	 * 
	 * @param errorResolverClass
	 *            the error resolver class
	 */
	public void setErrorResolverClass(Class<? extends ErrorDefinitionResolver> errorResolverClass) {
		this.errorResolverClass = errorResolverClass;
	}

	/**
	 * Initializes the resolver. It loads the error definitions first, then if successful sets teh errorDefinitionResolver to a
	 * new instance of ErrorDefinitionResolver If unsuccessful, it will produce a NullErrorDefinitionResolver
	 */
	private void initializeResolver() {

		// If ErrorDefinitionRetriever isn't specified. Nothing can be done!
		if (errorDefinitionRetriever == null) {
			log.error("ErrorDefinitionRetriever wasn't specified. "
					+ "This is incorrect usage of ErrorDefinitionResolverFactoryBean");
			errorDefinitionResolver = new NullErrorDefinitionResolver();
			return;
		}

		Collection<ErrorDefinition> errorDefinitions = null;
		try {
			errorDefinitions = errorDefinitionRetriever.retrieveAll();
		} catch (Exception e) {
			log.error("Retrival of ErrorDefinitions failed. " + "This was not expected and the matter should be investigated.",
					e);
			errorDefinitionResolver = new NullErrorDefinitionResolver();
		}
		// If ErrorDefinitions was successfully retrieved, initialize resolver
		if (errorDefinitions != null && errorDefinitions.size() != 0) {
			try {
				Constructor<? extends ErrorDefinitionResolver> constructor = errorResolverClass
						.getConstructor(Collection.class);
				errorDefinitionResolver = constructor.newInstance(errorDefinitions);
			} catch (Exception e) {
				log.error("Exception during creation of resolver. Resolver probably doesn't"
						+ " have a constructor which takes a collection as an argument", e);
				errorDefinitionResolver = new NullErrorDefinitionResolver();
			}
			log.debug("Resolver was successfully initialized with " + errorDefinitions.size() + " definitions");
		}
	}
}
