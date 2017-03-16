package no.stelvio.common.error.strategy.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsSame.sameInstance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.error.TestRecoverableException;
import no.stelvio.common.error.TestUnrecoverableException;
import no.stelvio.common.error.message.Extractor;
import no.stelvio.common.error.resolver.ErrorDefinitionResolver;
import no.stelvio.common.error.support.ErrorDefinition;
import no.stelvio.common.error.support.Severity;
import no.stelvio.common.util.ReflectUtil;

/**
 * Unit test of {@link LoggerExceptionHandlerStrategy}.
 * 
 * @author personcb9a87e24a5f
 * @author person983601e0e117
 */

public class LoggerExceptionHandlerStrategyTest {
	@Rule
	public final JUnitRuleMockery context = new JUnitRuleMockery();
	private LoggerExceptionHandlerStrategy leh;

	/**
	 * Verifies that if a LogName is specified, this will be used as a LogName in the underlying Log implementation.
	 */
	@Test
	public void loggerIsInitializedWithName() {
		leh.setLogName("TEST");
		Exception e = new Exception("test");
		leh.handleException(e); // This will initialize log
		String logName = getNameFromLogger(leh, e);
		assertThat(logName, equalTo("TEST"));
	}

	/**
	 * Verifies that if no logName is specified the value of the class creating the exception (ie. using the new operator) is
	 * used as the name in the underlying Log-implementation. In the case of this method, the name should default to:
	 * LoggerExceptionHandlerStrategyTest.class.getName()
	 */
	@Test
	public void loggerInitializationDefaultsToClassNameOfExceptionCreatorAsName() {
		Exception e = new Exception("test");
		String logName = getNameFromLogger(leh, e);
		assertThat(logName, equalTo(this.getClass().getName()));
	}

	/**
	 * Test same exception is returned.
	 * 
	 */
	@Test
	public void sameExceptionIsReturned() {
		Throwable originalStelvio = new TestUnrecoverableException("error");
		Throwable handledStelvio = leh.handleException(originalStelvio);

		assertThat(handledStelvio, sameInstance(originalStelvio));

		Throwable original = new IllegalArgumentException("error");
		Throwable handled = leh.handleException(original);

		assertThat(handled, sameInstance(original));
	}

	/**
	 * Test messageIsRetrievedFromExtractorAndLoggedWithCorrectSeverity.
	 * 
	 * @throws IllegalAccessException
	 *             illegal access
	 * @throws NoSuchFieldException
	 *             no field
	 * @throws InstantiationException
	 *             instantiation
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void messageIsRetrievedFromExtractorAndLoggedWithCorrectSeverity() throws IllegalAccessException,
			NoSuchFieldException, InstantiationException {
		final Log log = context.mock(Log.class);

		Field logField = leh.getClass().getDeclaredField("loggers");
		logField.setAccessible(true);
		Map<String, Log> loggers = (Map<String, Log>) logField.get(leh);
		loggers.put(this.getClass().getName(), log);

		final Throwable t1 = new TestRecoverableException("error1");

		context.checking(new Expectations() {
			{
				allowing(log).warn("test message", t1);
			}
		});

		leh.handleException(t1);
	}

	/**
	 * Setup before test.
	 */
	@Before
	public void setupForTest() {
		leh = createInstanceToTest();
		RequestContextSetter.setRequestContextForUnitTest();
	}

	/**
	 * Create instance to test.
	 * 
	 * @return instance
	 */
	private LoggerExceptionHandlerStrategy createInstanceToTest() {
		LoggerExceptionHandlerStrategy leh = new LoggerExceptionHandlerStrategy();
		leh.setExtractor(createExtractorMock());
		leh.setErrorResolver(createErrorResolverMock());
		return leh;
	}

	/**
	 * Create extractor mock.
	 * 
	 * @return extractor mock
	 */
	private Extractor createExtractorMock() {
		final Extractor extractor = context.mock(Extractor.class);

		context.checking(new Expectations() {
			{
				allowing(extractor).messageFor(with(aNonNull(Throwable.class)));
				will(returnValue("test message"));
			}
		});

		return extractor;
	}

	/**
	 * Create error resolver mock.
	 * 
	 * @return error resolver mock
	 */
	private ErrorDefinitionResolver createErrorResolverMock() {
		final ErrorDefinitionResolver errorDefinitionResolver = context.mock(ErrorDefinitionResolver.class);
		final ErrorDefinition result = new ErrorDefinition.Builder(String.class).severity(Severity.WARN).build();

		context.checking(new Expectations() {
			{
				allowing(errorDefinitionResolver).resolve(with(aNonNull(Throwable.class)));
				will(returnValue(result));
			}
		});

		return errorDefinitionResolver;
	}

	/**
	 * This method retrieves the name of the underlying log used by the LoggerExceptionHandlerStrategy The method relies on the
	 * field name containing the log name in the underlying log being "name" (String value). This is true for Log4JLogger,
	 * LogKitLogger, Jdk14Logger and Jdk13LumberjackLogger. Method will also work for Loggers that stores log name in variable
	 * "logName", this is true for commons-logging SimpleLog implementation
	 * 
	 * @param leh
	 *            logger exception handler strategy
	 * @param t
	 *            exception
	 * @return name from logger
	 */
	private String getNameFromLogger(LoggerExceptionHandlerStrategy leh, Throwable t) {

		try {
			Method method = leh.getClass().getDeclaredMethod("getLogger", Throwable.class);
			method.setAccessible(true);
			Log log = (Log) ReflectionUtils.invokeMethod(method, leh, new Object[] { t });
			try {
				return (String) ReflectUtil.getFielValueFromInstance(log, "name");
			} catch (Exception e) {
				// org.apache.commons.logging.impl.SimpleLog has log name stored in variable "logName"
				return (String) ReflectUtil.getFielValueFromInstance(log, "logName");
			}
		} catch (Exception e) {
			Assert.fail("Exception in getNameFromLogger");
			return null;
		}
	}

}
