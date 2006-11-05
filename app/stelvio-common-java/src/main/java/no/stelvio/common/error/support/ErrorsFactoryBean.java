package no.stelvio.common.error.support;

import java.util.Collection;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import no.stelvio.common.error.Err;

/**
 * @todo write javadoc
 *
 * @author personf8e9850ed756
 */                 
public class ErrorsFactoryBean implements FactoryBean, InitializingBean {
    private ErrorRetriever errorRetriever;
    private Collection<Err> errors;

    public Object getObject() throws Exception {
        return errors;
    }

    public Class getObjectType() {
        // TODO: maybe do something better to avoid NullPointerException if setup is wrong
        return errors.getClass();
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
