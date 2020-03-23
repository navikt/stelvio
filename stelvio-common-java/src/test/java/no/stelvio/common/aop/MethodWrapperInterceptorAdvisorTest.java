package no.stelvio.common.aop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.StaticListableBeanFactory;

/**
 * Unit test for {@link no.stelvio.common.aop.MethodWrapperInterceptorAdvisor}.
 *
 * @author personf8e9850ed756
 * @since 24.05.2005
 */
public class MethodWrapperInterceptorAdvisorTest {
	private MethodWrapperInterceptorAdvisor ia;
	private SimpleMethodInvocation methodInvocation;
	private StaticListableBeanFactory beanFactory;

	/**
	 * Set up before test.
	 *
     */
	@Before
	public void setUp() {
		ia = new MethodWrapperInterceptorAdvisor();
		methodInvocation = new SimpleMethodInvocation();
		beanFactory = createBeanFactory();
	}

	/**
	 * Test that ia should be a pointcut advisor. This test cannot fail and should be removed.
	 */
	@Test
	public void shouldBeAPointcutAdvisor() {
		assertTrue("Should be a PointcutAdvisor", ia instanceof PointcutAdvisor);
	}

	/**
	 * Test that ia should be a method interceptor. This test cannot fail and should be removed.
	 */
	@Test
	public void shouldBeAnMethodInterceptor() {
		assertTrue("Should be an MethodInterceptor", ia instanceof MethodInterceptor);
	}

	/**
	 * Test that advice should be itself.
	 */
	@Test
	public void theAdviceShouldBeItself() {
		assertSame("The Advice should be itself", ia, ia.getAdvice());
	}

	/**
	 * Test that pointcut should be a jdk regexp method pointcut.
	 */
	@Test
	public void pointcutShouldBeAJdkRegexpMethodPointcut() {
		assertTrue("Should be a JdkRegexpMethodPointcut", ia.getPointcut() instanceof JdkRegexpMethodPointcut);
	}

	/**
	 * Test CheckRegexpPatternIsUsedByPointcut.
	 * 
	 * @throws NoSuchMethodException no method
	 */
	@Test
	public void checkRegexpPatternIsUsedByPointcut() throws NoSuchMethodException {
		final Method method = MethodWrapperInterceptorAdvisorTest.class.getMethod("checkRegexpPatternIsUsedByPointcut");

		ia.setPattern(".*MethodWrapper.*ByPointcut");
		assertTrue("JdkRegexpMethodPointcut is not initialized correctly",
				((MethodMatcher) ia.getPointcut()).matches(method, null));

		ia.setPattern("shouldNotMatch");
		assertFalse("JdkRegexpMethodPointcut is not initialized correctly",
				((MethodMatcher) ia.getPointcut()).matches(method, null));

		ia.setPatterns(new String[] {"test", ".*Pointcut"});
		assertTrue("JdkRegexpMethodPointcut is not initialized correctly",
				((MethodMatcher) ia.getPointcut()).matches(method, null));

		ia.setPatterns(new String[] {"shouldNotMatch", "anotherShouldNotMatch"});
		assertFalse("JdkRegexpMethodPointcut is not initialized correctly",
				((MethodMatcher) ia.getPointcut()).matches(method, null));
	}

	/**
	 * Test ProxyOfRealReturnValueShouldBeReturnedFromInvoke.
	 * 
	 * @throws Throwable exception
	 */
	@Test
	public void proxyOfRealReturnValueShouldBeReturnedFromInvoke() throws Throwable {
		final Object result = invoke();

		assertFalse("Should not be the same class as the real return value", result.getClass().equals(TestBean.class));
		assertTrue("Should be a sub class of the real return value", result instanceof TestBean);
	}

	/**
	 * Test ShouldBeBeanFactoryAware. This test cannot fail and should be removed.
	 */
	@Test
	public void shouldBeBeanFactoryAware() {
		assertTrue("Should implement BeanFactoryAware", ia instanceof BeanFactoryAware);
	}

	/**
	 * Test InterceptorNamesAreInsertedAsAdvisorsInReturnedProxyObject.
	 * 
	 * @throws Throwable exception
	 */
	@Test
	public void interceptorNamesAreInsertedAsAdvisorsInReturnedProxyObject() throws Throwable {
		ia.setInterceptorNames(new String[] {"advisor"});

		final Advised result = (Advised) invoke();
		final Advisor[] advisors = result.getAdvisors();

		assertEquals("Should be of type TestAdvisor", TestAdvisor.class, advisors[0].getClass());
	}

	/**
	 * Create bean factory.
	 * 
	 * @return factory
	 */
	private StaticListableBeanFactory createBeanFactory() {
		final StaticListableBeanFactory bf = new StaticListableBeanFactory();
		bf.addBean("advisor", new TestAdvisor(ia));
		return bf;
	}

	/**
	 * Invoke.
	 * 
	 * @return bean
	 * @throws Throwable exception
	 */
	private Object invoke() throws Throwable {
		ia.setBeanFactory(beanFactory);
		return ia.invoke(methodInvocation);
	}

	/**
	 * A simple MethodInvokation.
	 */
	private static class SimpleMethodInvocation implements MethodInvocation {
		/**
		 * Get method.
		 * 
		 * @return null
		 */
		public Method getMethod() {
			return null;
		}

		/**
		 * Get arguments.
		 * 
		 * @return empty list of arguments
		 */
		public Object[] getArguments() {
			return new Object[0];
		}

		/**
		 * Proceed.
		 * 
		 * @return test bean
         */
		public Object proceed() {
			return new TestBean();
		}

		/**
		 * Get this.
		 * 
		 * @return null
		 */
		public Object getThis() {
			return null;
		}

		/**
		 * Get static part.
		 * 
		 * @return null
		 */
		public AccessibleObject getStaticPart() {
			return null;
		}
	}

	/**
	 * Bean to use for testing. Cannot be private as then cglib would not be able to create a new instance.
	 */
	protected static class TestBean {

	}

	/**
	 * Test advisor.
	 */
	protected static class TestAdvisor implements Advisor {
		
		private MethodWrapperInterceptorAdvisor ia;
		
		/**
		 * Creates a new instance of TestAdvisor.
		 *
		 * @param ia advisor
		 */
		public TestAdvisor(MethodWrapperInterceptorAdvisor ia) {
			this.ia = ia;
		}

		/**
		 * Is per instance.
		 * 
		 * @return false
		 */
		public boolean isPerInstance() {
			return false;
		}

		/**
		 * Get advice.
		 * 
		 * @return advice
		 */
		public Advice getAdvice() {
			return ia.getAdvice();
		}
	}
}
