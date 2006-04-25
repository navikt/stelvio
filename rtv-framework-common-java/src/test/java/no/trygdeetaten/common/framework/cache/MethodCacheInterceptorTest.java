package no.trygdeetaten.common.framework.cache;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.SystemException;

/**
 * Unit test for {@link MethodCacheInterceptor}.
 *
 * @author personf8e9850ed756
 * @version $Revision: $, $Date: $
 */
public class MethodCacheInterceptorTest extends MockObjectTestCase {
	private MethodCacheInterceptor methodCacheInterceptor;
	private Mock mockCache;

	public void testShouldBeAMethodInterceptor() {
		assertTrue("Not a MethodInterceptor", methodCacheInterceptor instanceof MethodInterceptor);
	}

	public void testCacheShouldBeSetBeforeUse() {
		try {
			new MethodCacheInterceptor().init();
			fail("SystemException should have been thrown");
		} catch (SystemException e) {
			assertEquals("Wrong error code;", FrameworkError.SERVICE_INIT_ERROR.getCode(), e.getErrorCode());
		}
	}

	public void testCacheIsUsedAfterFirstCall() throws Throwable {
		mockCache.expects(once())
				.method("get")
				.with(eq("no.trygdeetaten.common.framework.cache.MethodCacheInterceptorTest$TestBean.testMethod.17"))
				.will(returnValue(null));
		mockCache.expects(once())
				.method("put")
				.with(eq("no.trygdeetaten.common.framework.cache.MethodCacheInterceptorTest$TestBean.testMethod.17"),
				      eq("testResult"));
		mockCache.expects(once())
				.method("get")
				.with(eq("no.trygdeetaten.common.framework.cache.MethodCacheInterceptorTest$TestBean.testMethod.17"))
				.will(returnValue("testResult"));

		assertEquals("Wrong result;", "testResult", methodCacheInterceptor.invoke(new SimpleMethodInvocation()));
		assertEquals("Wrong result;", "testResult", methodCacheInterceptor.invoke(new SimpleMethodInvocation()));
	}

	protected void setUp() {
		mockCache = mock(Cache.class);
		methodCacheInterceptor = new MethodCacheInterceptor();
		methodCacheInterceptor.setCache((Cache) mockCache.proxy());
	}

	private static class SimpleMethodInvocation implements MethodInvocation {
		TestBean bean;
		Method method;

		SimpleMethodInvocation() {
			bean = new TestBean();

			try {
				method = TestBean.class.getMethod("testMethod", new Class[0]);
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException("Should not happen: " + e);
			}
		}

		public Method getMethod() {
			return method;
		}

		public Object[] getArguments() {
			return new Object[0];
		}

		public Object proceed() throws Throwable {
			return bean.testMethod();
		}

		public Object getThis() {
			return bean;
		}

		public AccessibleObject getStaticPart() {
			return null;
		}
	}

	private static class TestBean {
		public String testMethod() {
			return "testResult";
		};
	}
}
