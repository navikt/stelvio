package no.stelvio.common.error;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;

/**
 * @todo write javadoc
 *
 * @author personf8e9850ed756
 */                 
public class ErrorHandlerFactoryBean implements FactoryBean, InitializingBean {
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
