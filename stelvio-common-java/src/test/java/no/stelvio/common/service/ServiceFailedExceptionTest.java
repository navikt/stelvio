package no.stelvio.common.service;

import junit.framework.TestCase;

import no.stelvio.common.error.TestError;
import no.stelvio.common.service.ServiceFailedException;

/**
 * ServiceFailedException Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2297 $ $Author: psa2920 $ $Date: 2005-05-31 12:06:20 +0200 (Tue, 31 May 2005) $
 */
public class ServiceFailedExceptionTest extends TestCase {

	/**
	 * Constructor for ServiceFailedExceptionTest.
	 * @param arg0
	 */
	public ServiceFailedExceptionTest(String arg0) {
		super(arg0);
	}

	public void testRollbackOnly() {

		ServiceFailedException e1 = new ServiceFailedException(TestError.ERR_100000);
		assertFalse("RollbackOnly not is set", e1.isRollbackOnly());
		e1.setRollbackOnly();
		assertTrue("RollbackOnly is set", e1.isRollbackOnly());
	}

	public void testServiceFailedExceptionErrorCode() {
		ServiceFailedException sfe = new ServiceFailedException(TestError.ERR_100000);
		assertEquals("ErrorCode should be TestError.ERR_100000", TestError.ERR_100000.getCode(), sfe.getErrorCode());
		assertNull("There should be no arguments", sfe.getArguments());

	}

	public void testServiceFailedExceptionErrorCodeObject() {
		Object argument = "person7553f5959484";
		ServiceFailedException sfe = new ServiceFailedException(TestError.ERR_100000, argument);
		assertEquals("ErrorCode should be TestError.ERR_100000", TestError.ERR_100000.getCode(), sfe.getErrorCode());
		assertEquals("There should be 1 arguments", 1, sfe.getArguments().length);
		assertEquals("Argument 1 should be person7553f5959484", argument, sfe.getArguments()[0]);
	}

	public void testServiceFailedExceptionErrorCodeObjectArray() {
		Object[] arguments = new String[] { "Petter", "Skodvin" };
		ServiceFailedException sfe = new ServiceFailedException(TestError.ERR_100000, arguments);
		assertEquals("ErrorCode should be TestError.ERR_100000", TestError.ERR_100000.getCode(), sfe.getErrorCode());
		assertEquals("There should be 2 arguments", 2, sfe.getArguments().length);
		assertEquals("Argument 1 should be Petter", "Petter", sfe.getArguments()[0]);
		assertEquals("Argument 2 should be Skodvin", "Skodvin", sfe.getArguments()[1]);

	}

	public void testServiceFailedExceptionErrorCodeThrowable() {
		Throwable cause = new RuntimeException("The Original Cause");
		ServiceFailedException sfe = new ServiceFailedException(TestError.ERR_100000, cause);
		assertEquals("ErrorCode should be TestError.ERR_100000", TestError.ERR_100000.getCode(), sfe.getErrorCode());
		assertEquals("Cause is The Original Cause", cause, sfe.getCause());
	}

	public void testServiceFailedExceptionErrorCodeThrowableObject() {
		Object argument = "person7553f5959484";
		Throwable cause = new RuntimeException("The Original Cause");
		ServiceFailedException sfe = new ServiceFailedException(TestError.ERR_100000, cause, argument);
		assertEquals("ErrorCode should be TestError.ERR_100000", TestError.ERR_100000.getCode(), sfe.getErrorCode());
		assertEquals("Cause is The Original Cause", cause, sfe.getCause());
		assertEquals("There should be 1 arguments", 1, sfe.getArguments().length);
		assertEquals("Argument 1 should be Argument", argument, sfe.getArguments()[0]);
	}

	public void testServiceFailedExceptionErrorCodeThrowableObjectArray() {
		Object[] arguments = new String[] { "Petter", "Skodvin" };
		Throwable cause = new RuntimeException("The Original Cause");
		ServiceFailedException sfe = new ServiceFailedException(TestError.ERR_100000, cause, arguments);
		assertEquals("ErrorCode should be TestError.ERR_100000", TestError.ERR_100000.getCode(), sfe.getErrorCode());
		assertEquals("Cause is The Original Cause", cause, sfe.getCause());
		assertEquals("There should be 2 arguments", 2, sfe.getArguments().length);
		assertEquals("Argument 1 should be Petter", "Petter", sfe.getArguments()[0]);
		assertEquals("Argument 2 should be Skodvin", "Skodvin", sfe.getArguments()[1]);
	}

}
