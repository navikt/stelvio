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
public class ExecuteServiceRequestInterceptorWithoutRequestContextTest {

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
		request = createTestServiceRequestWithoutRequestContext();
	}

	/**
	 * Test that call is intercepted and default RequestConetxt is set.
	 */
	@Test
	public void testNoRequestContext() {

		// This call should be intercepted
		pojo.sayHelloTo(request).getMsg();
		RequestContext reqCtxAfterPojoCall = RequestContextHolder.currentRequestContext();		

		// ServiceRequest shoudl contain the default RequestContext
		assertThat(reqCtxAfterPojoCall.getComponentId(), is(equalTo("unknown-componentId")));
		assertThat(reqCtxAfterPojoCall.getModuleId(), is(equalTo("unknown-moduleId")));
		assertThat(reqCtxAfterPojoCall.getProcessId(), is(equalTo("unknown-processId")));
		assertThat(reqCtxAfterPojoCall.getScreenId(), is(equalTo("unknown-screenId")));
		assertThat(reqCtxAfterPojoCall.getTransactionId(), is(equalTo("unknown-transactionId")));
		assertThat(reqCtxAfterPojoCall.getUserId(), is(equalTo("unknown-userId")));

	}


	/**
	 * Create test service request with no RequestContext.
	 * 
	 * @return request - without RequestConetxt
	 * @throws NoSuchFieldException
	 *             no field
	 */
	private TestServiceRequest createTestServiceRequestWithoutRequestContext() throws NoSuchFieldException {
		TestServiceRequest servReq = new TestServiceRequest("Jens-Petrus");
		ReflectUtil.setField(TestServiceRequest.class, servReq, "requestContext", null);
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
