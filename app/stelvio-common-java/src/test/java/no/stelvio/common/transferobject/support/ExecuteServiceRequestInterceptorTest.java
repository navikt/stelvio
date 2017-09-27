package no.stelvio.common.transferobject.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.log.MdcConstants;
import no.stelvio.common.transferobject.ServiceRequest;
import no.stelvio.common.util.ReflectUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test class for ExecuteServiceRequestInterceptor.
 * 
 * @author MA
 */
public class ExecuteServiceRequestInterceptorTest {

	private TestExecuteServiceRequestInterceptorService pojo;
	private TestServiceRequest request;

	/**
	 * Set-up before test.
	 * 
	 * @throws NoSuchFieldException
	 *             no field
	 */
	@Before
	public void setUpTest() throws NoSuchFieldException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("servicerequest-interceptor-test-context.xml");
		pojo = (TestExecuteServiceRequestInterceptorService) ctx.getBean("test.servicerequest.pojo");
		request = createTestServiceRequest();
		RequestContextSetter.setRequestContext(new SimpleRequestContext(null, null, null, null));
	}

	/**
	 * Test that call is intercepted.
	 */
	@Test
	public void testCallIsIntercepted() {

		// RequestContext should only contain null values before interception
		RequestContext reqCtxBeforePojoCall = RequestContextHolder.currentRequestContext();
		assertThat(reqCtxBeforePojoCall.getScreenId(), not(equalTo("screenId123")));

		// This call should be intercepted
		String message = pojo.sayHelloTo(request).getMsg();

		assertThat(message, is(equalTo("Hello there, Jens-Petrus")));

		// Interceptor should reset requestContext
		// Subsequently call to currentRequestException should throw IllegalStateException
		/* Removed for PK-13403 to revert bugfixintroduced in 4.1.1 "Added after-invoke code in ExecuteServiceRequestInterceptor and added reset of RequestContext there"
		assertThat(callToCurrentRequestContextThrowsException(), is(true)); */

	}


	/**
	 * Test that MDC is updated when call is intercepted.
	 */
	@Test
	public void testMDCIsUpdated() {

		// RequestContext should only contain null values before interception
		RequestContext reqCtxBeforePojoCall = RequestContextHolder.currentRequestContext();		
		assertThat(reqCtxBeforePojoCall.getScreenId(), not(equalTo("screenId123")));

		// This call should be intercepted
		String message = pojo.sayHelloTo(request).getMsg();

		assertThat(message, is(equalTo("Hello there, Jens-Petrus")));

		/* Removed for PK-13403 to revert bugfixintroduced in 4.1.1 "Added after-invoke code in ExecuteServiceRequestInterceptor and added reset of RequestContext there"
		// MDC should be reset after call
		assertThat(MDC.get(MdcConstants.MDC_SCREEN), nullValue());
		assertThat(MDC.get(MdcConstants.MDC_MODULE), nullValue());
		assertThat(MDC.get(MdcConstants.MDC_TRANSACTION), nullValue());		
		assertThat(MDC.get(MdcConstants.MDC_APPLICATION), nullValue());*/

	}

	/**
	 * Create test service request.
	 * 
	 * @return request
	 * @throws NoSuchFieldException
	 *             no field
	 */
	private TestServiceRequest createTestServiceRequest() throws NoSuchFieldException {
		TestServiceRequest servReq = new TestServiceRequest("Jens-Petrus");
		RequestContext reqCtx = new SimpleRequestContext("screenId123", "moduleId123", "transactionId123", "componentId123");
		ReflectUtil.setField(ServiceRequest.class, servReq, "requestContext", reqCtx);
		return servReq;
	}

	
	/**
	 * Clean up after test.
	 */
	@After
	public void tearDown() {
		RequestContextSetter.resetRequestContext();
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

}
