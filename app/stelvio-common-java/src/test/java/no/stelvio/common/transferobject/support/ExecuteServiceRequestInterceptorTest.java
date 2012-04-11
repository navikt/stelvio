package no.stelvio.common.transferobject.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.util.ReflectUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test class for ExecuteServiceRequestInterceptor.
 * 
 * @author MA
 */
public class ExecuteServiceRequestInterceptorTest {

	private TestExecuteServiceRequestInterceptorService pojo;

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
		RequestContextSetter.setRequestContext(new SimpleRequestContext(null, null, null, null));
	}

	/**
	 * Test that call is intercepted.
	 * @throws NoSuchFieldException 
	 */
	@Test
	public void callIsInterceptedWhenRequestContextIsSet() throws NoSuchFieldException {
		TestServiceRequest request = createTestServiceRequest();
		
		// RequestContext should only contain null values before interception
		RequestContext reqCtxBeforePojoCall = RequestContextHolder.currentRequestContext();
		assertThat(reqCtxBeforePojoCall.getScreenId(), not(equalTo("screenId123")));

		// This call should be intercepted
		String message = pojo.sayHelloTo(request).getMsg();

		assertThat(message, is(equalTo("Hello there, Jens-Petrus")));

		RequestContext reqCtxAfterPojoCall = RequestContextHolder.currentRequestContext();

		assertEquals("componentId123", reqCtxAfterPojoCall.getComponentId());
		assertThat(reqCtxAfterPojoCall.getScreenId(), is(equalTo("screenId123")));
		assertThat(reqCtxAfterPojoCall.getTransactionId(), is(equalTo("transactionId123")));
		assertThat(reqCtxAfterPojoCall.getModuleId(), is(equalTo("moduleId123")));
		assertThat(reqCtxAfterPojoCall.getComponentId(), is(equalTo("componentId123")));

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
		ReflectUtil.setField(TestServiceRequest.class, servReq, "requestContext", reqCtx);
		return servReq;
	}

	@Test(expected = IllegalStateException.class)
	public void interceptorResetsContextWhenContextIsNull() throws NoSuchFieldException {

		TestServiceRequest request = new TestServiceRequest("Jens-Petrus");
		ReflectUtil.setField(TestServiceRequest.class, request, "requestContext", null);
		
		// RequestContext should only contain null values before interception
		RequestContext reqCtxBeforePojoCall = RequestContextHolder.currentRequestContext();
		assertThat(reqCtxBeforePojoCall.getScreenId(), not(equalTo("screenId123")));

		// This call should be intercepted
		String message = pojo.sayHelloTo(request).getMsg();

		assertThat(message, is(equalTo("Hello there, Jens-Petrus")));

		// This method call should throw an exception because the context is no longer set.
		RequestContextHolder.currentRequestContext();	
	}
	
	/**
	 * Clean up after test.
	 */
	@After
	public void tearDown() {
		RequestContextSetter.resetRequestContext();
	}

}
