package no.stelvio.common.error;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsSame.sameInstance;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import no.stelvio.common.context.support.RequestContextSetter;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for AbstractException. Didn't run as JUnit 4.x test case. Don't know why; could be my test runner.
 * 
 * @author personf8e9850ed756
 */
public abstract class AbstractExceptionTest<T extends RuntimeException> {

	/**
	 * Test causeIsSavedForLaterUsage.
	 */
	@Test
	public void causeIsSavedForLaterUsage() {
		RuntimeException cause = new RuntimeException("The original cause");
		Exception ewc = (Exception) createExceptionWithCause(cause);

		assertThat(cause, sameInstance(ewc.getCause()));
	}

	/**
	 * Test createExceptionWithoutParametersThrowsException.
	 * 
	 * @throws IllegalAccessException
	 *             illegal access
	 * @throws InstantiationException
	 *             instantiation
	 * @throws NoSuchMethodException
	 *             no method
	 * @throws InvocationTargetException
	 *             invocation target
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createExceptionWithoutParametersThrowsException() throws IllegalAccessException, InstantiationException,
			NoSuchMethodException, InvocationTargetException {
		Class<T> exceptionClass = exceptionClass();
		Constructor<T> constructor = exceptionClass.getConstructor(Object[].class);
		constructor.newInstance();
	}

	/**
	 * Exception class.
	 * 
	 * @return exception class.
	 */
	protected abstract Class<T> exceptionClass();

	/**
	 * Create exception with cause.
	 * 
	 * @param cause
	 *            cause
	 * @return exception
	 */
	protected abstract T createExceptionWithCause(Exception cause);

	/**
	 * Create exception.
	 * 
	 * @return exception
	 */
	protected abstract T createException();

	/**
	 * Setup before test.
	 */
	@Before
	public void setUpRequestContext() {
		RequestContextSetter.setRequestContextForUnitTest();
	}
}
