package no.trygdeetaten.business.framework.service;

import no.trygdeetaten.common.framework.FrameworkError;
import no.trygdeetaten.common.framework.error.ErrorCode;
import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.service.ServiceFailedException;
import no.trygdeetaten.common.framework.service.ServiceRequest;
import no.trygdeetaten.common.framework.service.ServiceResponse;

import junit.framework.TestCase;

/**
 * BusinessService Unit Test.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1074 $ $Author: psa2920 $ $Date: 2004-08-18 09:00:16 +0200 (Wed, 18 Aug 2004) $
 */
public class BusinessServiceTest extends TestCase {

	/**
	 * Constructor for BusinessServiceTest.
	 * @param arg0
	 */
	public BusinessServiceTest(String arg0) {
		super(arg0);
	}

	public void test() {
		BusinessService s = new TestBusinessService();

		Object o = "value1";
		try {
			assertSame(
				"Objects in request and response should have been the same",
				o,
				s.execute(new ServiceRequest("service", "key", o)).getData("key"));
		} catch (ServiceFailedException e) {
			fail("execute() should not have thrown ServiceFailedException");
			e.printStackTrace();
		}

		try {
			s.execute(new ServiceRequest("service", "key", null));
			fail("execute() should have thrown ServiceFailedException");
		} catch (ServiceFailedException e) {
			assertEquals(
				"execute() should have thrown SERVICE_INPUT_MISSING",
				e.getErrorCode(),
				FrameworkError.SERVICE_INPUT_MISSING.getCode());
		}

		try {
			s.execute(new ServiceRequest("service", "key", "ServiceFailedException"));
			fail("execute() should have thrown ServiceFailedException");
		} catch (ServiceFailedException e) {
			assertEquals(
				"execute() should have thrown UNSPECIFIED_ERROR",
				e.getErrorCode(),
				ErrorCode.UNSPECIFIED_ERROR.getCode());
		}

		try {
			s.execute(new ServiceRequest("service", "key", "RuntimeException"));
			fail("execute() should have thrown RuntimeException");
		} catch (Throwable t) {
			assertTrue("execute() should have thrown RuntimeException", t instanceof RuntimeException);
		}

		try {
			s.execute(new ServiceRequest("service", "key", "ReleaseFailure"));
			fail("execute() should have thrown SystemException");
		} catch (Throwable t) {
			assertTrue(
				"execute() should have thrown RuntimeException",
				t instanceof SystemException
					&& FrameworkError.SERVICE_DESTROY_ERROR.getCode() == ((SystemException) t).getErrorCode());
		}
	}

	private class TestBusinessService extends BusinessService {

		private boolean failRelease = false;

		protected ServiceResponse doExecute(ServiceRequest request) throws ServiceFailedException {
			super.validate(request, "key");

			Object o = request.getData("key");
			if ("ServiceFailedException".equals(o)) {
				throw new ServiceFailedException(ErrorCode.UNSPECIFIED_ERROR);
			}
			if ("RuntimeException".equals(o)) {
				throw new RuntimeException("≈Âh nei og nei, hva er det som har skjedd");
			}
			if ("ReleaseFailure".equals(o)) {
				failRelease = true;
				return new ServiceResponse("key", o);
			} else {
				return new ServiceResponse("key", o);
			}
		}

		protected void release() {
			if (failRelease) {
				throw new RuntimeException("Klarer ikke rydde opp");
			}
		}

	}

}
