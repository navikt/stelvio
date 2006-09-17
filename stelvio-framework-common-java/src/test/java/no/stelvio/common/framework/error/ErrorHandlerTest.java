package no.stelvio.common.framework.error;

import no.stelvio.common.framework.error.ErrorHandler;
import junit.framework.TestCase;

/**
 * ErrorHandler Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1037 $ $Author: psa2920 $ $Date: 2004-08-16 15:25:10 +0200 (Mon, 16 Aug 2004) $
 */
public class ErrorHandlerTest extends TestCase {

	private String message = "Ohhh my God, Noooooooooo!!!";
	private Throwable t = new RuntimeException(message);

	/**
	 * Constructor for ErrorHandlerTest.
	 * @param arg0
	 */
	public ErrorHandlerTest(String arg0) {
		super(arg0);
	}

	public void testHandleError() {
		assertEquals("t should have been the same", t, ErrorHandler.handleError(t));
	}

	public void testGetMessage() {
		assertEquals("message should have been the same", t.getLocalizedMessage(), ErrorHandler.getMessage(t));
	}

	public void testGetStacktraceAsString() {
		assertNull(ErrorHandler.getStacktraceAsString(null));
		assertNotNull(ErrorHandler.getStacktraceAsString(t));
	}

	public void testInit() {
		ErrorHandler.init();
	}
}
