package no.trygdeetaten.common.framework.error.system;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import no.trygdeetaten.common.framework.FrameworkError;

/**
 * Unit test for {@link TPSException}.
 *
 * @author personf8e9850ed756
 * @version $Revision: $, $Date: $
 */
public class TPSExceptionTest extends TestCase {
	private TPSException notAnswering;
	private TPSException unavailable;
	private TPSException rc8;
	private TPSException rc9;
	private TPSException rc12;

	public void testConstructorsTakeIdentifier() {
		assertEquals("Not the correct identifier string returned;",
		             "identifier1",
		             TPSException.systemUnavailableError(null, "identifier1").getIdentifier());
	}

	public void testHasStaticMethodsForEveryTypeOfException() {
		assertEquals("Wrong error code;", FrameworkError.SYSTEM_NOT_ANSWERING.getCode(), notAnswering.getErrorCode());
		assertEquals("Wrong argument;", "TPS", notAnswering.getArguments()[0]);

		assertEquals("Wrong error code;", FrameworkError.SYSTEM_UNAVAILABLE_ERROR.getCode(), unavailable.getErrorCode());
		assertEquals("Wrong identifier;", "identifier", unavailable.getIdentifier());

		assertEquals("Wrong error code;", FrameworkError.TPS_ERROR_08.getCode(), rc8.getErrorCode());
		assertEquals("Wrong error message;", "errorMessage1", rc8.getArguments()[0]);
		assertEquals("Wrong identifier;", "identifier1", rc8.getIdentifier());

		assertEquals("Wrong error code;", FrameworkError.TPS_ERROR_09.getCode(), rc9.getErrorCode());
		assertEquals("Wrong error message;", "errorMessage2", rc9.getArguments()[0]);
		assertEquals("Wrong identifier;", "identifier2", rc9.getIdentifier());

		assertEquals("Wrong error code;", FrameworkError.TPS_ERROR_12.getCode(), rc12.getErrorCode());
		assertEquals("Wrong error message;", "errorMessage3", rc12.getArguments()[0]);
		assertEquals("Wrong identifier;", "identifier3", rc12.getIdentifier());
	}

	public void testCannotInstansiateViaDefaultConstructor() throws NoSuchMethodException,
	                                                         InvocationTargetException,
	                                                         InstantiationException {
		final Constructor defaultConstructor = TPSException.class.getDeclaredConstructor(new Class[]{});

		try {
			defaultConstructor.newInstance(null);
			fail("IllegalAccessException should have been thrown");
		} catch (IllegalAccessException e) {
			// should happen
		}
	}

	public void testCopyConstructorCopiesAllFields() {
		final TPSException exp = new TPSException(TPSException.failedWithReturnCode08("errorMessage", "identifier"));

		assertEquals("Wrong error code", FrameworkError.TPS_ERROR_08.getCode(), exp.getErrorCode());
		assertEquals("Wrong error message", "errorMessage", exp.getArguments()[0]);
		assertEquals("Wrong identifier", "identifier", exp.getIdentifier());
	}

	public void testChecksTheErrorCodeToBeFailed() {
		assertTrue("Wrong check", notAnswering.isSystemNotAnsweringError());
		assertTrue("Wrong check", unavailable.isSystemUnavailableError());
		assertTrue("Wrong check", rc8.isReturnCode08Error());
		assertTrue("Wrong check", rc9.isReturnCode09Error());
		assertTrue("Wrong check", rc12.isReturnCode12Error());
	}

	protected void setUp() throws Exception {
		notAnswering = TPSException.systemNotAnswering();
		unavailable = TPSException.systemUnavailableError(new RuntimeException(), "identifier");
		rc8 = TPSException.failedWithReturnCode08("errorMessage1", "identifier1");
		rc9 = TPSException.failedWithReturnCode09("errorMessage2", "identifier2");
		rc12 = TPSException.failedWithReturnCode12("errorMessage3", "identifier3");
	}
}
