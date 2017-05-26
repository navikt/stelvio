package no.stelvio.presentation.context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.log.MDCOperations;
import no.stelvio.common.log.MdcConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

/**
 * Test class for testing the capabilities of the RequestContextFilter
 * 
 * The private method <code>{@link #executeFilterSimulator(Filter, ServletRequest, boolean)} </code> is used to simulate a
 * filter being run in a Servlet container.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class RequestContextFilterTest extends AbstractFilterTest {

	private static final String REQUEST_CONTEXT = RequestContext.class.getName();

	private ApplicationContext ctx = null;

	/**
	 * Test method that checks that a component Id is set if a component Id has been configured in ApplicationContext.
	 * 
	 * @throws ServletException
	 *             servlet exception
	 * @throws IOException
	 *             io exception
	 */
	@Test
	public void filterSetsComponentIdInContext() throws ServletException, IOException {
		Filter filter = (Filter) ctx.getBean("test.requestcontextfilter");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		RequestContext reqCtx = new SimpleRequestContext("screen", "processid", "tx", "compid");

		session.setAttribute(REQUEST_CONTEXT, reqCtx);

		executeFilterSimulator(filter, request, createMockFilterChainForComponentIdTest(), "RequestContextFilter");

		// Filter should reset requestContext
		// Subsequently call to currentRequestException should throw IllegalStateException
		assertThat(callToCurrentRequestContextThrowsException(), is(true));

	}

	/**
	 * Test filterSetsTransactionIdInContext.
	 * 
	 * @throws ServletException
	 *             servlet exception
	 * @throws IOException
	 *             io exception
	 */
	@Test
	public void filterSetsTransactionIdInContext() throws ServletException, IOException {
		Filter filter = (Filter) ctx.getBean("test.requestcontextfilter");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);

		RequestContext reqCtx = new SimpleRequestContext("screen", "module", "tx", "compid");

		session.setAttribute(REQUEST_CONTEXT, reqCtx);

		executeFilterSimulator(filter, request, createMockFilterChainForTransactionIdTest(), "RequestContextFilter");

		// Filter should reset requestContext
		// Subsequently call to currentRequestException should throw IllegalStateException
		assertThat(callToCurrentRequestContextThrowsException(), is(true));

	}
	
	/**
	 * Test method that checks that MDC is reset after invocation of filter.
	 * 
	 * @throws ServletException
	 *             servlet exception
	 * @throws IOException
	 *             io exception
	 */
	@Test
	public void filterResetsMDC() throws ServletException, IOException {
		Filter filter = (Filter) ctx.getBean("test.requestcontextfilter");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		RequestContext reqCtx = new SimpleRequestContext("screen", "processid", "tx", "compid");
		RequestContextSetter.setRequestContext(reqCtx);
		MDCOperations.setMdcProperties();
		
		// assert that MDC has been set
		assertThat(MDC.get(MdcConstants.MDC_SCREEN), is(equalTo("screen")));
		assertThat(MDC.get(MdcConstants.MDC_MODULE), is(equalTo("processid")));
		assertThat(MDC.get(MdcConstants.MDC_TRANSACTION), is(equalTo("tx")));		
		assertThat(MDC.get(MdcConstants.MDC_APPLICATION), is(equalTo("compid")));

		session.setAttribute(REQUEST_CONTEXT, reqCtx);

		executeFilterSimulator(filter, request, createMockFilterChainForComponentIdTest(), "RequestContextFilter");		

		// Filter should reset requestContext
		// Subsequently call to currentRequestException should throw IllegalStateException
		assertThat(callToCurrentRequestContextThrowsException(), is(true));
		
		// Filter should reset MDC
		assertThat(MDC.get(MdcConstants.MDC_SCREEN), nullValue());
		assertThat(MDC.get(MdcConstants.MDC_MODULE), nullValue());
		assertThat(MDC.get(MdcConstants.MDC_TRANSACTION), nullValue());		
		assertThat(MDC.get(MdcConstants.MDC_APPLICATION), nullValue());

	}

	/**
	 * Checks whether a call to RequestcontextHolder.currentRequestContext() throws an IllegalStateException.
	 * 
	 * @return true if exception is thrown, otherwise false
	 */
	private boolean callToCurrentRequestContextThrowsException() {
		try {
			RequestContextHolder.currentRequestContext();
		} catch (IllegalStateException e) {
			return true;
		}
		return false;
	}

	/**
	 * Creates a Mock filter chain asserts that transactionId has been set by filter.
	 * 
	 * @return mock filter
	 */
	private FilterChain createMockFilterChainForTransactionIdTest() {
		FilterChain assertionFilterChain = new AbstractMockFilterChain() {
			public void assertion() {
				assertNotNull("Transaction Id was null, should have been set by filter", RequestContextHolder
						.currentRequestContext().getTransactionId());
				assertThat(RequestContextHolder.currentRequestContext().getTransactionId(), not(equalTo("tx")));
			}
		};
		return assertionFilterChain;
	}

	/**
	 * Creates a Mock filter chain asserts that componentId has been set by filter.
	 * 
	 * @return mock filter
	 */
	private FilterChain createMockFilterChainForComponentIdTest() {
		FilterChain assertionFilterChain = new AbstractMockFilterChain() {
			public void assertion() {
				assertNotNull("Component Id was null, should have been set by filter", RequestContextHolder
						.currentRequestContext().getComponentId());
				assertThat(RequestContextHolder.currentRequestContext().getComponentId(), not(equalTo("compId")));
			}
		};
		return assertionFilterChain;
	}

	/**
	 * Set up before test.
	 */
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("test-requestcontextfilter-context.xml");
	}

	/**
	 * Clean up after test.
	 */
	@After
	public void tearDown() {
		ctx = null;
	}

}
