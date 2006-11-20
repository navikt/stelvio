package no.stelvio.common.error.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.isNotNull;
import static org.hamcrest.object.IsCompatibleType.compatibleType;
import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import no.stelvio.common.error.ErrorResolver;

/**
 * Unit test for {@link ErrorResolverFactoryBean}.
 *
 * @author personf8e9850ed756
 */
public class ErrorResolverFactoryBeanTest {
    private ErrorsRetriever errorsRetriever;
    private ErrorResolverFactoryBean ehfb;
    private Mockery mockery;

    @Test
    public void returnsCorrectType() throws Exception {
        Class<?> clazz = ehfb.getObjectType();

        assertThat(clazz, isNotNull());
        assertThat(clazz, compatibleType(ErrorResolver.class));
    }

    @Test
    public void returnsMapWithErrors() throws Exception {
        setupAndCallRetriever();

        @SuppressWarnings("unchecked")
        ErrorResolver errorResolver = (ErrorResolver) ehfb.getObject();
        assertThat(errorResolver, isNotNull());
    }

    @Test
    public void callsRetrieveOnlyWhenInitialized() throws Exception {
        setupAndCallRetriever();

        // Should not make calls to retriever
        ehfb.getObject();
    }

    @Test
    public void shouldImplementInitializingBean() {
        assertThat(ErrorResolverFactoryBean.class, compatibleType(InitializingBean.class));
    }

    @Test
    public void shouldImplementFactoryBean() {
        assertThat(ErrorResolverFactoryBean.class, compatibleType(FactoryBean.class));
    }

    @Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        errorsRetriever = mockery.mock(ErrorsRetriever.class);

        ehfb = new ErrorResolverFactoryBean();
        ehfb.setErrorRetriever(errorsRetriever);
    }

    @After
    public void checkInvariants() {
        mockery.assertIsSatisfied();
    }

    private void setupAndCallRetriever() throws Exception {
        mockery.expects(new InAnyOrder() {{
                exactly(1).of (errorsRetriever).retrieve();
            }});

        ehfb.afterPropertiesSet();
    }
}
