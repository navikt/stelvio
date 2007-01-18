package no.stelvio.common.error.resolver.support;

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

import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.retriever.ErrorDefinitionRetriever;
import no.stelvio.common.error.retriever.RetrieverFailedException;

/**
 * Unit test for {@link ErrorDefinitionResolverFactoryBean}.
 *
 * @author personf8e9850ed756
 */
public class ErrorDefinitionResolverFactoryBeanTest {
    private ErrorDefinitionRetriever errorDefinitionRetriever;
    private ErrorDefinitionResolverFactoryBean ehfb;
    private Mockery mockery;

    @Test
    public void returnsCorrectType() throws Exception {
        Class<?> clazz = ehfb.getObjectType();

        assertThat(clazz, isNotNull());
        assertThat(clazz, compatibleType(ErrorDefinitionResolver.class));
    }

    @Test
    public void returnsMapWithErrors() throws Exception {
        setupAndCallRetriever();

        @SuppressWarnings("unchecked")
        ErrorDefinitionResolver errorDefinitionResolver = (ErrorDefinitionResolver) ehfb.getObject();
        assertThat(errorDefinitionResolver, isNotNull());
    }

    @Test
    public void callsRetrieveOnlyWhenInitialized() throws Exception {
        setupAndCallRetriever();

        // Should not make calls to retriever
        ehfb.getObject();
    }

    @Test
    public void shouldImplementInitializingBean() {
        assertThat(ErrorDefinitionResolverFactoryBean.class, compatibleType(InitializingBean.class));
    }

    @Test
    public void shouldImplementFactoryBean() {
        assertThat(ErrorDefinitionResolverFactoryBean.class, compatibleType(FactoryBean.class));
    }

	/**
	 * When the <code>ErrorDefinitionRetriever</code> fails, an exception that can be handled specifically by the facade
	 * is thrown.
	 */
	@Test(expected = RetrieverFailedException.class)
	public void whenErrorDefinitionRetrieverFailsDetectableExceptionIsThrown() throws Exception {
		mockery.expects(new InAnyOrder() {{
		        one (errorDefinitionRetriever).retrieve(); will(throwException(new IllegalStateException("problems")));
		    }});

		ehfb.afterPropertiesSet();
	}


	@Before
    public void setUp() throws Exception {
        mockery = new Mockery();
        errorDefinitionRetriever = mockery.mock(ErrorDefinitionRetriever.class);

        ehfb = new ErrorDefinitionResolverFactoryBean();
        ehfb.setErrorRetriever(errorDefinitionRetriever);
    }

    @After
    public void checkInvariants() {
        mockery.assertIsSatisfied();
    }

    private void setupAndCallRetriever() throws Exception {
        mockery.expects(new InAnyOrder() {{
                exactly(1).of (errorDefinitionRetriever).retrieve();
            }});

        ehfb.afterPropertiesSet();
    }
}
