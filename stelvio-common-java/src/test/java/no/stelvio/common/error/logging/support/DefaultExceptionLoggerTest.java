package no.stelvio.common.error.logging.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.context.support.SimpleRequestContext;
import no.stelvio.common.error.ErrorCode;
import no.stelvio.common.error.FunctionalRecoverableException;
import no.stelvio.common.error.FunctionalUnrecoverableException;
import no.stelvio.common.error.SystemUnrecoverableException;
import no.stelvio.common.error.logging.ExceptionLogger;
import no.stelvio.common.error.logging.Log4jTestAppender;
import no.stelvio.common.error.logging.LoggingTestDebugException;
import no.stelvio.common.error.logging.LoggingTestUnrecoverableException;
import no.stelvio.common.error.support.Severity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * DefaultExceptionLoggerTest.
 * 
 * @author MA
 *
 */
public class DefaultExceptionLoggerTest {

	private Log4jTestAppender appender;

	private DefaultExceptionLogger defaultExceptionLogger;

	/**
	 * Set up before test.
	 */
	@Before
	public void setUp() {
		defaultExceptionLogger = new DefaultExceptionLogger();
		ReloadableResourceBundleMessageSource logLevelMessageSource = new ReloadableResourceBundleMessageSource();
		logLevelMessageSource.setBasename("loglevels");
		defaultExceptionLogger.setLogLevelMessageSource(logLevelMessageSource);
		appender = new Log4jTestAppender();
		Logger log = LogManager.getLogger(ExceptionLogger.class);
		log.removeAllAppenders(); // Remove
		log.addAppender(appender);
	}

	/**
	 * Test exceptionIsPrintedAtErrorByDefault.
	 */
	@Test
	public void exceptionIsPrintedAtErrorByDefault() {
		String messageToPrint = "Message: 'hh'.";
		defaultExceptionLogger.setLogLevelMessageSource(null);
		appender.addMessageToVerify(messageToPrint, Severity.ERROR);
		defaultExceptionLogger.log(new Exception("hh"));
		appender.printResults();
		assertTrue(appender.getVerificationResults(), appender.verify());
	}

	/**
	 * Test defaultLogLevelDefinedByPropertiesIsUsed.
	 */
	@Test
	public void defaultLogLevelDefinedByPropertiesIsUsed() {
		String messageToPrint = "Message: ''.";
		appender.addMessageToVerify(messageToPrint, Severity.WARN);
		defaultExceptionLogger.log(new Exception(""));
		assertTrue(appender.getVerificationResults(), appender.verify());
	}

	/**
	 * Uses loglevel defined for ErrorCode=1234 ->FATAL. Logs the exception properties and adds requestContext properties.
	 * 
	 */
	@Test
	public void exceptionIsPrintedWithPropertiesAndSeverityFromResource() {
		String msg = "This is a message";
		RequestContextSetter.setRequestContext(new SimpleRequestContext("screen1", "module1", "trans1", "component1", "user1"));
		String messageToPrint = "Message: '".concat(msg).concat("'.");
		messageToPrint = messageToPrint + " ErrorCode=1234. ";
		appender.addMessageToVerify(messageToPrint, Severity.ERROR);
		SystemUnrecoverableException exToThrow = new LoggingTestUnrecoverableException(msg, MyErrorCodes.MY_CODE_1);
		defaultExceptionLogger.log(exToThrow);
		appender.printResults();
		assertTrue(appender.getVerificationResults(), appender.verify());
	}

	/**
	 * Uses loglevel defined for ErrorCode=1234 ->FATAL. Logs the exception properties and adds requestContext properties.
	 * 
	 */
	@Test
	public void exceptionIsPrintedWithTemplateArgumentsAndSeverityFromResource() {
		RequestContextSetter.setRequestContext(new SimpleRequestContext("screen1", "module1", "trans1", "component1", "user1"));

		// The expected output message
		String messageToPrint = "Message: 'generic1'.";
		messageToPrint = messageToPrint + " ErrorCode=" + MyErrorCodes.MY_CODE_1.getErrorCode() + ". ";

		// Loglevel and message that is expected
		appender.addMessageToVerify(messageToPrint, Severity.ERROR);

		SystemUnrecoverableException exToThrow = new LoggingTestUnrecoverableException("generic1", MyErrorCodes.MY_CODE_1);
		defaultExceptionLogger.log(exToThrow);
		appender.printResults();
		assertTrue(appender.getVerificationResults(), appender.verify());
	}

	/**
	 * Test exceptionIsPrintedAtDebugLevel.
	 */
	@Test
	public void exceptionIsPrintedAtDebugLevel() {
		String messageContent = "Debug statment";
		String messageToPrint = "Message: '" + messageContent + "'. ErrorCode=null. ";
		appender.addMessageToVerify(messageToPrint, Severity.DEBUG);
		defaultExceptionLogger.log(new LoggingTestDebugException(messageContent));
		assertTrue(appender.getVerificationResults(), appender.verify());
	}

	/**
	 * Test ShouldExtractProperties.
	 */
	@Test
	public void testShouldExtractProperties() {
		DefaultExceptionLogger logger = new DefaultExceptionLogger();
		assertFalse(logger.shouldExtractProperties(Exception.class));
		assertTrue(logger.shouldExtractProperties(SystemUnrecoverableException.class));
		assertTrue(logger.shouldExtractProperties(FunctionalRecoverableException.class));
		assertTrue(logger.shouldExtractProperties(FunctionalUnrecoverableException.class));
	}

	/**
	 * Test IsExceptionWithErrorCode.
	 */
	@Test
	public void testIsExceptionWithErrorCode() {
		DefaultExceptionLogger logger = new DefaultExceptionLogger();
		assertTrue("SystemUnrecoverableExceptions declare errorCodes", logger
				.isExceptionWithErrorCode(new LoggingTestUnrecoverableException("", null)));
		assertFalse("Exception does not declare errorCode", logger.isExceptionWithErrorCode(new Exception()));
		assertFalse("FunctionalUnrecoverableExceptions does not declare errorCodes", 
				logger.isExceptionWithErrorCode(new FunctionalUnrecoverableException("test") {
					private static final long serialVersionUID = 1L;
				}));
	}

	/**
	 * Test GetExceptionCodeFromException.
	 */
	@Test
	public void testGetExceptionCodeFromException() {
		DefaultExceptionLogger logger = new DefaultExceptionLogger();
		ErrorCode errorCode = new ErrorCode() {
			public String getErrorCode() {
				return "test";
			}
		};
		assertEquals("Unable to extract errorCode", "test", logger
				.getErrorCodeFromException(new LoggingTestUnrecoverableException("", errorCode)));
	}

	/**
	 * Test exceptionIsCorrectlyCategorized.
	 */
	@Test
	public void exceptionIsCorrectlyCategorized() {
		DefaultExceptionLogger logger = new DefaultExceptionLogger();

		FunctionalUnrecoverableException functionalException = new FunctionalUnrecoverableException("Test") {
			private static final long serialVersionUID = 1L;
		};
		assertTrue(logger.isFunctionalException(functionalException));

		FunctionalRecoverableException functionalException2 = new FunctionalRecoverableException("Test") {
			private static final long serialVersionUID = 1L;
		};
		assertTrue(logger.isFunctionalException(functionalException2));

		Exception nonFunctionalException = new Exception();
		assertFalse(logger.isFunctionalException(nonFunctionalException));
	}

	/**
	 * Test exceptionContainsInputMessage.
	 */
	@Test
	public void exceptionContainsInputMessage() {
		String errorMessage = "This is a test error message";
		defaultExceptionLogger.log(errorMessage, new FunctionalUnrecoverableException("TestFunctionalUnrecoverable") {
			private static final long serialVersionUID = 1L;
		});
		assertTrue(appender.isMessageLogged(errorMessage));

		defaultExceptionLogger.log(errorMessage, new FunctionalRecoverableException("TestFunctionalRecoverable") {
			private static final long serialVersionUID = 1L;
		});
		assertTrue(appender.isMessageLogged(errorMessage));

		defaultExceptionLogger.log(errorMessage, new SystemUnrecoverableException("TestSystemUnrecoverable") {
			private static final long serialVersionUID = 1L;
		});
		assertTrue(appender.isMessageLogged(errorMessage));
	}

	/**
	 * Test checkForAdditionalMessage.
	 */
	@Test
	public void checkForAdditionalMessage() {
		assertFalse(defaultExceptionLogger.thereIsAnAdditionalMessage(null));
		assertFalse(defaultExceptionLogger.thereIsAnAdditionalMessage(""));
		assertTrue(defaultExceptionLogger.thereIsAnAdditionalMessage("Test string"));
	}

	/**
	 * Clean up after test.
	 */
	@After
	public void tearDown() {
		defaultExceptionLogger = null;
		appender = null;
	}

	/**
	 *  My error codes for the test.
	 */
	enum MyErrorCodes implements ErrorCode {
		MY_CODE_1("1234"), MY_CODE_2("4567");

		private String errorCode;

		/**
		 * Creates a new instance of MyErrorCodes.
		 *
		 * @param errorCode error code
		 */
		MyErrorCodes(String errorCode) {
			this.errorCode = errorCode;
		}

		/**
		 * Get error code.
		 * 
		 * @return error code
		 */
		public String getErrorCode() {
			return errorCode;
		}
	}
}
