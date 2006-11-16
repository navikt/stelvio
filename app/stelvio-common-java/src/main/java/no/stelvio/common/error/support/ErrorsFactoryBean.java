package no.stelvio.common.error.support;

import java.util.Collection;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import no.stelvio.common.error.Err;

/**
 * Simple factory for shared list of errors. Allows for the the classes needing this list not having to worry about
 * retrieving it; they just need to setup a dependency to a definition of this class.  
 *
 * @author personf8e9850ed756
 * @todo more javadoc
 */
public class ErrorsFactoryBean implements FactoryBean, InitializingBean {
    private ErrorRetriever errorRetriever;
    private Collection<Err> errors;

    public Object getObject() throws Exception {
        return errors;
    }

    public Class getObjectType() {
        return Collection.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        errors = errorRetriever.retrieve();
    }

    public void setErrorRetriever(ErrorRetriever errorRetriever) {
        this.errorRetriever = errorRetriever;
    }
}
