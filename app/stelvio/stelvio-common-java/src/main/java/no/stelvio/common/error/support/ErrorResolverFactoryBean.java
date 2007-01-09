package no.stelvio.common.error.support;

import java.lang.reflect.Constructor;
import java.util.Collection;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import no.stelvio.common.error.ErrorResolver;

/**
 * Simple factory for an error resolver. Allows for the the classes needing the error resolver not having to worry about
 * creating it; they just need to setup a dependency to a definition of this class.
 *
 * @author personf8e9850ed756
 * @todo more javadoc
 */
public class ErrorResolverFactoryBean implements FactoryBean, InitializingBean {
    private ErrorsRetriever errorsRetriever;
    private ErrorResolver errorResolver;

    /**
     * Default implementation of the <code>ErrorResolver</code> is used if not specified.
     *
     * @todo better javadoc
     * @see DefaultErrorResolver
     */
    private Class<? extends ErrorResolver> errorResolverClass = DefaultErrorResolver.class;

    public Object getObject() throws Exception {
        return errorResolver;
    }

    public Class getObjectType() {
        return ErrorResolver.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        Constructor<? extends ErrorResolver> constructor = errorResolverClass.getConstructor(Collection.class);
        errorResolver = constructor.newInstance(errorsRetriever.retrieve());
    }

    public void setErrorRetriever(ErrorsRetriever errorsRetriever) {
        this.errorsRetriever = errorsRetriever;
    }

    public void setErrorResolverClass(Class<? extends ErrorResolver> errorResolverClass) {
        this.errorResolverClass = errorResolverClass;
    }
}
