package no.stelvio.common.error.resolver.support;

import java.lang.reflect.Constructor;
import java.util.Collection;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.retriever.ErrorDefinitionRetriever;

/**
 * Simple factory for an error resolver. Allows for the the classes needing the error resolver not having to worry about
 * creating it; they just need to setup a dependency to a definition of this class.
 *
 * @author personf8e9850ed756
 * @todo more javadoc
 */
public class ErrorDefinitionResolverFactoryBean implements FactoryBean, InitializingBean {
	private ErrorDefinitionRetriever errorDefinitionRetriever;
	private ErrorDefinitionResolver errorDefinitionResolver;

	/**
	 * Default implementation of the <code>ErrorDefinitionResolver</code> is used if not specified.
	 *
	 * @see StaticErrorDefinitionResolver
	 */
	private Class<? extends ErrorDefinitionResolver> errorResolverClass = StaticErrorDefinitionResolver.class;

	public Object getObject() throws Exception {
		return errorDefinitionResolver;
	}

	public Class getObjectType() {
		return ErrorDefinitionResolver.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		Constructor<? extends ErrorDefinitionResolver> constructor = errorResolverClass.getConstructor(Collection.class);
		errorDefinitionResolver = constructor.newInstance(errorDefinitionRetriever.retrieveAll());
	}

	public void setErrorDefinitionRetriever(ErrorDefinitionRetriever errorDefinitionRetriever) {
		this.errorDefinitionRetriever = errorDefinitionRetriever;
	}

	public void setErrorResolverClass(Class<? extends ErrorDefinitionResolver> errorResolverClass) {
		this.errorResolverClass = errorResolverClass;
	}
}
