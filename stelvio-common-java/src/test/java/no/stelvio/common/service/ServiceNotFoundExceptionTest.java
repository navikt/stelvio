package no.stelvio.common.service;

import no.stelvio.common.error.old.TestError;
import no.stelvio.common.service.ServiceNotFoundException;

import junit.framework.TestCase;

/**
 * ServiceNotFoundException Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1043 $ $Author: psa2920 $ $Date: 2004-08-16 17:11:51 +0200 (Mon, 16 Aug 2004) $
 */
public class ServiceNotFoundExceptionTest extends TestCase {

	/**
	 * Constructor for ServiceNotFoundExceptionTest.
	 * @param arg0
	 */
	public ServiceNotFoundExceptionTest(String arg0) {
		super(arg0);
	}

	public void test() {
		Throwable t = new RuntimeException("Dæven døtte");
		ServiceNotFoundException e1 = new ServiceNotFoundException(TestError.ERR_100000);
		ServiceNotFoundException e2 = new ServiceNotFoundException(TestError.ERR_200000, t);

		assertEquals("ErrorCode match", e1.getErrorCode(), TestError.ERR_100000.getCode());
		assertEquals("ErrorCode match", e2.getErrorCode(), TestError.ERR_200000.getCode());
		assertEquals("ErrorCode match", e2.getCause(), t);
	}
}
