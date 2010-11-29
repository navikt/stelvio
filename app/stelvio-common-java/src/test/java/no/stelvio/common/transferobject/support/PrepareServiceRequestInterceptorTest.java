package no.stelvio.common.transferobject.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.ejb.config.PrepareServiceRequestTestService;
import no.stelvio.common.transferobject.ServiceRequest;
import no.stelvio.common.util.ReflectionException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ReflectionUtils;

/**
 * Tests PrepareServiceRequestInterceptor.
 * 
 * @author person983601e0e117 (Accenture)
 */
public class PrepareServiceRequestInterceptorTest {

	private PrepareServiceRequestTestService pojo;

	private ApplicationContext ctx;

	/**
	 * Set up the test.
	 * 
	 * @throws NoSuchFieldException no field
	 */
	@Before
	public void setUpTest() throws NoSuchFieldException {
		ctx = new ClassPathXmlApplicationContext("prepareservicerequest-interceptor-test-context.xml");
		pojo = (PrepareServiceRequestTestService) ctx.getBean("test.prepareservicerequest.pojo");
		RequestContext reqCtx = new SimpleRequestContext("screenId123", "moduleId123", "transactionId123", "componentId123");
		RequestContextSetter.setRequestContext(reqCtx);
	}

	/**
	 * Tests that the PrepareServiceRequestInterceptor copies the RequestContext from the RequestContextHolder onto the
	 * ServiceRequest subclass.
	 * 
	 * @throws IllegalAccessException illegal access
	 * @throws InvocationTargetException invocation
	 * @throws NoSuchMethodException no method
	 */
	@Test
	public void callToServiceIsIntercepted() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		TestServiceRequest serviceReq = new TestServiceRequest();

		pojo.testService(serviceReq);
		assertThat(serviceReq.getRequestContextByReflection().getScreenId(), is(equalTo("screenId123")));
		assertThat(serviceReq.getRequestContextByReflection().getTransactionId(), is(equalTo("transactionId123")));
		assertThat(serviceReq.getRequestContextByReflection().getModuleId(), is(equalTo("moduleId123")));
		assertThat(serviceReq.getRequestContextByReflection().getComponentId(), is(equalTo("componentId123")));
		assertThat("Second interceptor should not be executed before target when isExecutedInAopContext=false", serviceReq
				.isSecondInterceptorBefore(), equalTo(false));
		assertThat("Second interceptor should not be executed after target when isExecutedInAopContext=false", serviceReq
				.isSecondInterceptorAfter(), equalTo(false));
	}

	/**
	 * Tests that the proceed-implementation in PrepareServiceRequestInterceptor is implemented to allow execution of additional
	 * interceptors/aspects in an AOP chain when {@link PrepareServiceRequestInterceptor#setExecutedInAopContext(boolean)} is
	 * set to true.
	 */
	@Test
	public void interceptorCanBeExecutedInAopContext() {
		TestServiceRequest serviceReq = new TestServiceRequest();
		PrepareServiceRequestInterceptor interceptor = (PrepareServiceRequestInterceptor) ctx
				.getBean("test.prepareservicerequest.prepareServiceRequestInterceptor");
		interceptor.setExecutedInAopContext(true);
		pojo.testService(serviceReq);
		assertThat(serviceReq.getRequestContextByReflection().getScreenId(), is(equalTo("screenId123")));
		assertThat(serviceReq.getRequestContextByReflection().getTransactionId(), is(equalTo("transactionId123")));
		assertThat(serviceReq.getRequestContextByReflection().getModuleId(), is(equalTo("moduleId123")));
		assertThat(serviceReq.getRequestContextByReflection().getComponentId(), is(equalTo("componentId123")));
		assertThat("Second interceptor should have been executed before target when isExecutedInAopContext=true", serviceReq
				.isSecondInterceptorBefore(), equalTo(true));
		assertThat("Second interceptor should have been executed after target when isExecutedInAopContext=true", serviceReq
				.isSecondInterceptorAfter(), equalTo(true));

	}

	/**
	 * Test implementation of ServiceRequest.
	 * 
	 * @author person983601e0e117 (Accenture)
	 * 
	 */
	public class TestServiceRequest extends ServiceRequest {

		private static final long serialVersionUID = 1L;

		private boolean secondInterceptorBefore = false;

		private boolean secondInterceptorAfter = false;

		/**
		 * Get RequestContextByReflection.
		 * 
		 * @return RequestContextByReflection
		 */
		public RequestContext getRequestContextByReflection() {
			Field requestContextField;
			try {
				requestContextField = ServiceRequest.class.getDeclaredField("requestContext");
				ReflectionUtils.makeAccessible(requestContextField);
				RequestContext requestContext = (RequestContext) requestContextField.get(this);
				return requestContext;
			} catch (SecurityException e) {
				e.printStackTrace();
				throw new ReflectionException("Problems doing reflection with the following arguments: 'requestContext'", e);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				throw new ReflectionException("Problems doing reflection with the following arguments: 'requestContext'", e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new ReflectionException("Problems doing reflection with the following arguments: 'requestContext'", e);
			}
		}

		/**
		 * Returns true if second intercepor is before.
		 * 
		 * @return secondInterceptorBefore
		 */
		public boolean isSecondInterceptorBefore() {
			return secondInterceptorBefore;
		}

		/**
		 * Sets secondInterceptorBefore.
		 * 
		 * @param interceptedOnce
		 *            secondInterceptorBefore
		 */
		public void setSecondInterceptorBefore(boolean interceptedOnce) {
			this.secondInterceptorBefore = interceptedOnce;
		}

		/**
		 * Returns true if secont interceptor is after.
		 * 
		 * @return secondInterceptorAfter
		 */
		public boolean isSecondInterceptorAfter() {
			return secondInterceptorAfter;
		}

		/**
		 * Sets the secondInterceptorAfter.
		 * 
		 * @param interceptedTwice
		 *            secondInterceptorAfter
		 */
		public void setSecondInterceptorAfter(boolean interceptedTwice) {
			this.secondInterceptorAfter = interceptedTwice;
		}

	}

}
