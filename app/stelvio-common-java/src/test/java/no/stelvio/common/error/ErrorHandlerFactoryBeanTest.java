package no.stelvio.common.error;

import com.agical.rmock.extension.junit.RMockTestCase;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collection;
import java.util.HashSet;

/**
 * Unit test for {@link ErrorHandlerFactoryBean}.
 *
 * @author personf8e9850ed756
 */
public class ErrorHandlerFactoryBeanTest extends RMockTestCase {
    private ErrorRetriever errorRetriever;
    private ErrorHandlerFactoryBean ehfb;

    public void testReturnsCorrectType() throws Exception {
        setupRetriever();
        assertThat(ehfb.getObjectType(), is.NOT_NULL);
        assertThat(ehfb.getObjectType(), is.clazz.assignableTo(Collection.class));
    }

    public void testReturnsMapWithErrors() throws Exception {
        setupRetriever();

        @SuppressWarnings("unchecked")
        Collection<Err> errors = (Collection<Err>) ehfb.getObject();
        assertThat(errors, is.NOT_NULL);
        assertThat(errors.size(), is.eq(3));
    }

    public void testCallsRetrieveOnlyWhenInitialized() throws Exception {
        setupRetriever();

        // Should not make calls to retriever
        ehfb.getObject();
    }

    public void testShouldImplementInitializingBean() {
        assertThat(ErrorHandlerFactoryBean.class, is.clazz.assignableTo(InitializingBean.class));
    }

    public void testShouldImplementFactoryBean() {
        assertThat(ErrorHandlerFactoryBean.class, is.clazz.assignableTo(FactoryBean.class));
    }

    protected void setUp() throws Exception {
        super.setUp();

        errorRetriever = (ErrorRetriever) mock(ErrorRetriever.class);
        ehfb = new ErrorHandlerFactoryBean();
        ehfb.setErrorRetriever(errorRetriever);
    }

    private void setupRetriever() throws Exception {
        errorRetriever.retrieve();
        modify().returnValue(createExpectedErrors());
        startVerification();

        ehfb.afterPropertiesSet();
    }

    private Collection<Err> createExpectedErrors() {
        Collection<Err> expErrors = new HashSet<Err>();
        expErrors.add(new Err.Builder("1").build());
        expErrors.add(new Err.Builder("2").build());
        expErrors.add(new Err.Builder("3").build());

        return expErrors;
    }
}
