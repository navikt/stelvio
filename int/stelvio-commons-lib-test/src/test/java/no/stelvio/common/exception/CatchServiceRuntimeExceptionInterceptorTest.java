package no.stelvio.common.exception;

import junit.framework.TestCase;

import com.ibm.websphere.sca.ServiceRuntimeException;

public class CatchServiceRuntimeExceptionInterceptorTest extends TestCase {
	private CatchServiceRuntimeExceptionInterceptor interceptor = new CatchServiceRuntimeExceptionInterceptor();

	public void testSRE() {
		ServiceRuntimeException sre = new ServiceRuntimeException("This is an error");
		Throwable convertedThrowable = interceptor.convertThrowable(sre);
		assertSame(sre, convertedThrowable);
	}
}
