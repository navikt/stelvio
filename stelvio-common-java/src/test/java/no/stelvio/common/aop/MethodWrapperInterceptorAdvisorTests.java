/*
 * Copyright 2002-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.stelvio.common.aop;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import no.stelvio.common.aop.MethodWrapperInterceptorAdvisor;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
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
public class MethodWrapperInterceptorAdvisorTests extends TestCase {
    private MethodWrapperInterceptorAdvisor ia;
    private SimpleMethodInvocation methodInvocation;
    private StaticListableBeanFactory beanFactory;

    public void testShouldBeAPointcutAdvisor() {
        assertTrue("Should be a PointcutAdvisor", ia instanceof PointcutAdvisor);
    }

    public void testShouldBeAnMethodInterceptor() {
        assertTrue("Should be an MethodInterceptor", ia instanceof MethodInterceptor);
    }

    public void testTheAdviceShouldBeItself() {
        assertSame("The Advice should be itself", ia, ia.getAdvice());
    }

    public void testPointcutShouldBeAJdkRegexpMethodPointcut() {
        assertTrue("Should be a JdkRegexpMethodPointcut", ia.getPointcut() instanceof JdkRegexpMethodPointcut);
    }

    public void testCheckRegexpPatternIsUsedByPointcut() throws NoSuchMethodException
    {
        final Method method = MethodWrapperInterceptorAdvisorTests.class.getMethod("testCheckRegexpPatternIsUsedByPointcut", null);

        ia.setPattern(".*MethodWrapper.*test.*ByPointcut");
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

    public void testProxyOfRealReturnValueShouldBeReturnedFromInvoke() throws Throwable
    {
        final Object result = invoke();

        assertFalse("Should not be the same class as the real return value", result.getClass().equals(TestBean.class));
        assertTrue("Should be a sub class of the real return value", result instanceof TestBean);
    }

    public void testShouldBeBeanFactoryAware() {
        assertTrue("Should implement BeanFactoryAware", ia instanceof BeanFactoryAware);
    }

    public void testInterceptorNamesAreInsertedAsAdvisorsInReturnedProxyObject() throws Throwable
    {
        ia.setInterceptorNames(new String[] {"advisor"});

        final Advised result = (Advised) invoke();
        final Advisor[] advisors = result.getAdvisors();

        assertEquals("Should be of type TestAdvisor", TestAdvisor.class, advisors[0].getClass());
    }

    protected void setUp() throws Exception
    {
        ia = new MethodWrapperInterceptorAdvisor();
        methodInvocation = new SimpleMethodInvocation();
        beanFactory = createBeanFactory();
    }

    private StaticListableBeanFactory createBeanFactory()
    {
        final StaticListableBeanFactory bf = new StaticListableBeanFactory();
        bf.addBean("advisor", new TestAdvisor());
        return bf;
    }

    private Object invoke() throws Throwable
    {
	    ia.setBeanFactory(beanFactory);
	    return ia.invoke(methodInvocation);
    }

    private static class SimpleMethodInvocation implements MethodInvocation
    {
        public Method getMethod()
        {
            return null;
        }

        public Object[] getArguments()
        {
            return new Object[0];
        }

        public Object proceed() throws Throwable
        {
            return new TestBean();
        }

        public Object getThis()
        {
            return null;
        }

        public AccessibleObject getStaticPart()
        {
            return null;
        }
    }

    /**
     * Bean to use for testing. Cannot be private as then cglib would not be able to create a new instance.
     */
    protected static class TestBean {

    }

    private static class TestAdvisor implements Advisor {

        public boolean isPerInstance()
        {
            // TODO: change this
            return false;
        }

        public Advice getAdvice()
        {
            // TODO: change this
            return null;
        }
    }
}
