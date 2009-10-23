package no.stelvio.common.exception;

import static no.stelvio.common.exception.ExceptionAssert.*;

import org.junit.Test;

import com.ibm.websphere.sca.ServiceRuntimeException;

public class CatchServiceRuntimeExceptionInterceptorTest {
	private CatchServiceRuntimeExceptionInterceptor interceptor = new CatchServiceRuntimeExceptionInterceptor();

	@Test
	public void testSRE() {
		ServiceRuntimeException sre = new ServiceRuntimeException("This is an error");
		Throwable convertedThrowable = interceptor.convertThrowable(sre);
		assertEquals(sre, convertedThrowable);
	}
}
