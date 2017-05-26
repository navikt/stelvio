package no.stelvio.provider.error.strategy.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import no.stelvio.common.error.FunctionalUnrecoverableException;
import no.stelvio.common.error.strategy.ExceptionHandlerStrategy;
import no.stelvio.dto.error.strategy.support.ImitatorDtoException;
import no.stelvio.dto.exception.FunctionalUnrecoverableDtoException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test class for testing MorphToSdk14ExceptionHandlerStrategy.
 * 
 * @author MA
 */
public class MorphToSdk14ExceptionHandlerStrategyTest {

	private ApplicationContext ctx;

	/**
	 * Creating a context before test.
	 */
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("test-morphtosdk14-context.xml");
	}

	/**
	 * Verify that runtime exceptions are morphed.
	 */
	@Test
	public void verifyRuntimeExceptionsAreMorphed() {
		ExceptionHandlerStrategy strategy = (ExceptionHandlerStrategy) ctx.getBean("test.morpher.morpherstrategy");
		Object e = strategy.handleException(new IllegalArgumentException("testing runtime"));

		assertThat(e, instanceOf(ImitatorDtoException.class));
	}

	/**
	 * Verify that functional unrecoverable exceptions are intercepted and morphed.
	 */
	@Test
	public void verifyFunctionalUnrecoverableExceptionIsInterceptedAndMorphed() {
		ExceptionHandlerStrategy strategy = (ExceptionHandlerStrategy) ctx.getBean("test.morpher.morpherstrategy");
		Object e = strategy.handleException(new MyUnrecoverableFunctionalException("testing functionalUnrecoverable"));
		assertThat(e, instanceOf(ImitatorDtoException.class));
	}

	/**
	 * Verify that functional unrecoverable dto-exceptions are intercepted but not morphed.
	 */
	@Test
	public void verifyFunctionalUnrecoverableDtoExceptionIsInterceptedButNotMorphed() {
		ExceptionHandlerStrategy strategy = (ExceptionHandlerStrategy) ctx.getBean("test.morpher.morpherstrategy");
		Object e = strategy.handleException(new MyUnrecoverableFunctionalDtoException("testing functionalUnrecoverableDto"));
		assertThat(e, instanceOf(MyUnrecoverableFunctionalDtoException.class));
	}

	/**
	 * Defines a FunctionalUnrecoverableException test class. 
	 */
	private class MyUnrecoverableFunctionalException extends FunctionalUnrecoverableException {

		private static final long serialVersionUID = -4290703691993427255L;

		/**
		 * Creates a new instance of MyUnrecoverableFunctionalException.
		 *
		 * @param testString message
		 */
		public MyUnrecoverableFunctionalException(String testString) {
			super(testString);
		}
	}

	/**
	 * Defines a FunctionalUnrecoverableDtoException test class.
	 */
	private class MyUnrecoverableFunctionalDtoException extends FunctionalUnrecoverableDtoException {

		private static final long serialVersionUID = -784365459593119858L;

		/**
		 * Creates a new instance of MyUnrecoverableFunctionalDtoException.
		 *
		 * @param testString message
		 */
		public MyUnrecoverableFunctionalDtoException(String testString) {
			super(testString);
		}

	}

}
