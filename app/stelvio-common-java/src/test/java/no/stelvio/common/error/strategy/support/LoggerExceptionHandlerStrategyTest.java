package no.stelvio.common.error.strategy.support;

import junit.framework.TestCase;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.error.RecoverableException;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.util.Log469MessageFormatterImpl;
import no.stelvio.common.util.MessageFormatter;
import no.stelvio.common.util.SequenceNumberGenerator;

/**
 * Unit test of LogHandlerImpl.
 * 
 * @author person7553f5959484
 * @version $Revision: 2843 $ $Author: psa2920 $ $Date: 2006-02-15 13:00:22
 *          +0100 (on, 15 feb 2006) $
 */
public class LoggerExceptionHandlerStrategyTest extends TestCase {

	LoggerExceptionHandlerStrategy h = null;

	MessageFormatter msgFormatter = null;

	/**
	 * Initialize Handler
	 * 
	 * {@inheritDoc}
	 */
	protected void setUp() {
		h = new LoggerExceptionHandlerStrategy();
		msgFormatter = new Log469MessageFormatterImpl("T666", "Java", "Bidrag Fase 2", "yyyyMMdd", "HHmmss");
		h.setMessageFormatter(msgFormatter);
		h.init();
	}

	/**
	 * Destroy Handler.
	 * 
	 * {@inheritDoc}
	 */
	protected void tearDown() {
		h = null;
	}

	/*
	 * Test for Throwable handleError(Throwable)
	 */
	public void testHandleErrorSystemExceptions() {

		RequestContext.setScreenId("Oppgaveliste");
		RequestContext.setModuleId("Oppgaveliste");
		RequestContext.setProcessId("HentOppgavelister");
		RequestContext.setTransactionId(String.valueOf(SequenceNumberGenerator.getNextId()));
		RequestContext.setUserId("psa2920");

/* TODO make a better test
		assertTrue("handleError(SystemException) should have returned SystemException", h
				.handleError(new SystemException(new Object[] { new Integer(0),
						new Double(1.2) })) instanceof SystemException);

		assertTrue("handleError(SystemException) should have returned SystemException", h
				.handleError(new SystemException(new IllegalArgumentException(
						"Ulovlig tallformat, kun heltall er lovlig"), new Double(137.89))) instanceof SystemException);

		assertTrue("handleError(SystemException) should have returned SystemException", h
				.handleError(new SystemException()) instanceof SystemException);

		assertTrue("handleError(SystemException) should have returned SystemException", h
				.handleError(new SystemException()) instanceof SystemException);

		assertTrue("handleError(Exception) should have returned SystemException", h.handleError(new Exception(
				"Dette er en Exception")) instanceof SystemException);
*/

	}

	public void testHandleErrorApplicationExceptions() {

		RequestContext.setScreenId("Oppgaveliste");
		RequestContext.setModuleId("Oppgaveliste");
		RequestContext.setProcessId("HentOppgavelister");
		RequestContext.setTransactionId(String.valueOf(SequenceNumberGenerator.getNextId()));
		RequestContext.setUserId("psa2920");

/* TODO make a better test
		assertTrue("handleError(RecoverableException) should have returned RecoverableException", h
				.handleError(new RecoverableException(new Object[] { new Integer(0),
						new Double(1.2) })) instanceof RecoverableException);

		assertTrue(
				"handleError(RecoverableException) should have returned RecoverableException",
				h.handleError(new RecoverableException(new IllegalArgumentException(
						"Ulovlig tallformat, kun heltall er lovlig"), new Double(137.89))) instanceof RecoverableException);

		assertTrue("handleError(RecoverableException) should have returned RecoverableException", h
				.handleError(new RecoverableException()) instanceof RecoverableException);

		assertTrue("handleError(RecoverableException) should have returned RecoverableException", h
				.handleError(new RecoverableException()) instanceof RecoverableException);
*/

	}

	public void testGetMessage() {
/* TODO make a better test
		assertNotNull(h.getMessage(new RecoverableException(new IllegalArgumentException(
				"Ulovlig tallformat, kun heltall er lovlig"), new Double(137.89))));

		assertNotNull(h.getMessage(new RecoverableException(new IllegalArgumentException(
				"Ulovlig tallformat, kun heltall er lovlig"), new Double(137.89))));
*/

		assertNotNull(h.getMessage(new Exception("Dette er en Exception")));
	}

	public void testErrorsInErrorHandling() {
		LoggerExceptionHandlerStrategy h1 = new LoggerExceptionHandlerStrategy();

		try {
			h1.init();
//	TODO better here		fail("Init failed, should have caught SystemException");
		} catch (Throwable t) {
			assertTrue("Init should have thrown SystemException", t instanceof SystemException);
		}

		LoggerExceptionHandlerStrategy h2 = new LoggerExceptionHandlerStrategy();

		try {
			h2.init();
//	TODO		fail("Init failed, should have thrown SystemException");
		} catch (SystemException t) {
			// should happen
		}

		LoggerExceptionHandlerStrategy h3 = new LoggerExceptionHandlerStrategy();
		h3.init();

		RequestContext.setProcessId("HentOppgavelister");
		RequestContext.setTransactionId(String.valueOf(SequenceNumberGenerator.getNextId()));
	}

	public void testErrorMessageIncludeErrorCodeAndId() {
		RecoverableException le = new TestRecoverableException("message");

		// Should only check that the formatting is done correctly, not the
		// error id or message is retrieved correctly
		assertEquals("Do not have error code and id", "ErrCode=1,ErrId=" + le.getErrorId() + ",Message="
				+ h.getMessage(le), h.getSystemLogMessage(le));
	}

    private class TestRecoverableException extends RecoverableException {
        public TestRecoverableException(String message) {
            super(message);
        }

        protected String getMessageTemplate() {
            return "testApplicationException: {0}";
        }

        protected int getMessageTemplateArgumentsLength() {
            return 1;
        }
    }
}
