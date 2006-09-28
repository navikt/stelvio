package no.stelvio.common.error;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.error.ApplicationException;
import no.stelvio.common.error.ErrorCode;
import no.stelvio.common.error.ErrorConfig;
import no.stelvio.common.error.LogHandlerImpl;
import no.stelvio.common.error.Severity;
import no.stelvio.common.error.SystemException;
import no.stelvio.common.service.LocalService;
import no.stelvio.common.service.ServiceFailedException;
import no.stelvio.common.service.ServiceRequest;
import no.stelvio.common.service.ServiceResponse;
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
public class LogHandlerImplTest extends TestCase {

	LogHandlerImpl h = null;

	MessageFormatter msgFormatter = null;

	/**
	 * Initialize Handler
	 * 
	 * {@inheritDoc}
	 */
	protected void setUp() {
		h = new LogHandlerImpl();
		msgFormatter = new Log469MessageFormatterImpl("T666", "Java", "Bidrag Fase 2", "yyyyMMdd", "HHmmss");
		h.setMessageFormatter(msgFormatter);
		h.setDelegate(new LocalService() {
			public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
				Map m = new HashMap();
				m.put(new Integer(ErrorCode.UNSPECIFIED_ERROR.getCode()), new ErrorConfig(ErrorCode.UNSPECIFIED_ERROR
						.getCode(), Severity.ERROR, "En teknisk feil har oppstått: {0}. Feilen er av typen {1}."));
				m.put(new Integer(ErrorCode.UNCONFIGURED_ERROR.getCode()), new ErrorConfig(ErrorCode.UNCONFIGURED_ERROR
						.getCode(), Severity.ERROR, "En teknisk feil har oppstått. Feilkoden er {0}. Detaljer: {1}."));
				m.put(new Integer(TestError.ERR_100000.getCode()), new ErrorConfig(TestError.ERR_100000.getCode(),
						Severity.WARN, "Dette er en advarsel om at {0} muligens ikke er gyldig"));
				m.put(new Integer(TestError.ERR_200000.getCode()), new ErrorConfig(TestError.ERR_200000.getCode(),
						Severity.ERROR, "Dette er en alvorlig feil uten argumenter"));
				m.put(new Integer(TestError.ERR_300000.getCode()), new ErrorConfig(TestError.ERR_300000.getCode(),
						Severity.FATAL, "Dette er en svært alvorlig feil med en sinnsyk lang feilmelding "
								+ "som sannsynligvis aldri ville ha blitt godkjent av et "
								+ "internasjonalt annerkjent godkjennelsesorgan. Feilmeldingen "
								+ "er jo over 255 tegn lang og burde kuttes på slutten og "
								+ "erstattes av prikker som viser at teksten er for lang"));
				return new ServiceResponse("ErrorMap", m);
			}
		});
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

		assertTrue("handleError(SystemException) should have returned SystemException", h
				.handleError(new SystemException(ErrorCode.UNSPECIFIED_ERROR, new Object[] { new Integer(0),
						new Double(1.2) })) instanceof SystemException);

		assertTrue("handleError(SystemException) should have returned SystemException", h
				.handleError(new SystemException(TestError.ERR_100000, new IllegalArgumentException(
						"Ulovlig tallformat, kun heltall er lovlig"), new Double(137.89))) instanceof SystemException);

		assertTrue("handleError(SystemException) should have returned SystemException", h
				.handleError(new SystemException(TestError.ERR_200000)) instanceof SystemException);

		assertTrue("handleError(SystemException) should have returned SystemException", h
				.handleError(new SystemException(TestError.ERR_300000)) instanceof SystemException);

		assertTrue("handleError(Exception) should have returned SystemException", h.handleError(new Exception(
				"Dette er en Exception")) instanceof SystemException);
	}

	public void testHandleErrorApplicationExceptions() {

		RequestContext.setScreenId("Oppgaveliste");
		RequestContext.setModuleId("Oppgaveliste");
		RequestContext.setProcessId("HentOppgavelister");
		RequestContext.setTransactionId(String.valueOf(SequenceNumberGenerator.getNextId()));
		RequestContext.setUserId("psa2920");

		assertTrue("handleError(ApplicationException) should have returned ApplicationException", h
				.handleError(new ApplicationException(ErrorCode.UNSPECIFIED_ERROR, new Object[] { new Integer(0),
						new Double(1.2) })) instanceof ApplicationException);

		assertTrue(
				"handleError(ApplicationException) should have returned ApplicationException",
				h.handleError(new ApplicationException(TestError.ERR_100000, new IllegalArgumentException(
						"Ulovlig tallformat, kun heltall er lovlig"), new Double(137.89))) instanceof ApplicationException);

		assertTrue("handleError(ApplicationException) should have returned ApplicationException", h
				.handleError(new ApplicationException(TestError.ERR_200000)) instanceof ApplicationException);

		assertTrue("handleError(ApplicationException) should have returned ApplicationException", h
				.handleError(new ApplicationException(TestError.ERR_300000)) instanceof ApplicationException);

	}

	public void testGetMessage() {
		assertNotNull(h.getMessage(new ApplicationException(TestError.ERR_100000, new IllegalArgumentException(
				"Ulovlig tallformat, kun heltall er lovlig"), new Double(137.89))));

		assertNotNull(h.getMessage(new ApplicationException(TestError.ERR_999999, new IllegalArgumentException(
				"Ulovlig tallformat, kun heltall er lovlig"), new Double(137.89))));

		assertNotNull(h.getMessage(new Exception("Dette er en Exception")));
	}

	public void testErrorsInErrorHandling() {
		LogHandlerImpl h1 = new LogHandlerImpl();
		h1.setDelegate(new LocalService() {
			public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
				throw new ServiceFailedException(TestError.ERR_100000);
			}
		});

		try {
			h1.init();
			fail("Init failed, should have caught SystemException");
		} catch (Throwable t) {
			assertTrue("Init should have thrown SystemException", t instanceof SystemException);
		}

		LogHandlerImpl h2 = new LogHandlerImpl();
		h2.setDelegate(new LocalService() {
			public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
				throw new RuntimeException("Dette går jo aldri bra");
			}
		});

		try {
			h2.init();
			fail("Init failed, should have thrown SystemException");
		} catch (SystemException t) {
			// should happen
		}

		LogHandlerImpl h3 = new LogHandlerImpl();
		h3.setDelegate(new LocalService() {
			public ServiceResponse execute(ServiceRequest request) throws ServiceFailedException {
				return new ServiceResponse("ErrorMap", new HashMap());
			}
		});
		h3.init();

		RequestContext.setProcessId("HentOppgavelister");
		RequestContext.setTransactionId(String.valueOf(SequenceNumberGenerator.getNextId()));
	}

	public void testErrorMessageIncludeErrorCodeAndId() {
		ApplicationException le = new ApplicationException(ErrorCode.UNCONFIGURED_ERROR);

		// Should only check that the formatting is done correctly, not the
		// error id or message is retrieved correctly
		assertEquals("Do not have error code and id", "ErrCode=1,ErrId=" + le.getErrorId() + ",Message="
				+ h.getMessage(le), h.getSystemLogMessage(le));
	}
}
