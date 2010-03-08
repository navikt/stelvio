package no.stelvio.common.exception;

import static no.stelvio.common.exception.ExceptionAssert.*;

import org.junit.Test;

import com.ibm.websphere.sca.ServiceBusinessException;
import com.ibm.websphere.sca.ServiceRuntimeException;

public class CatchServiceRuntimeExceptionInterceptorTest {
	private CatchServiceRuntimeExceptionInterceptor interceptor = new CatchServiceRuntimeExceptionInterceptor();

	@Test
	public void testJava() {
		ClassNotFoundException throwable = new ClassNotFoundException("Not found");
		Throwable convertedThrowable = interceptor.convertThrowable(throwable);
		assertEquals(throwable, convertedThrowable);
	}
	
	@Test
	public void testJavaWithLocalCause() {
		IllegalArgumentException throwable = new IllegalArgumentException(new DummyException("Error in system"));
		Throwable convertedThrowable = interceptor.convertThrowable(throwable);
		assertStructureEquals(throwable, convertedThrowable);
	}

	@Test
	public void testLocal() {
		DummyException throwable = new DummyException("Error in system");
		Throwable convertedThrowable = interceptor.convertThrowable(throwable);
		assertStructureEquals(throwable, convertedThrowable);
	}

	@Test
	public void testSRE() {
		ServiceRuntimeException throwable = new ServiceRuntimeException("This is an error");
		Throwable convertedThrowable = interceptor.convertThrowable(throwable);
		assertEquals(throwable, convertedThrowable);
	}

	@Test
	public void testSREWithJavaCause() {
		ServiceRuntimeException sre = new ServiceRuntimeException("This is an error", new IllegalArgumentException());
		Throwable convertedThrowable = interceptor.convertThrowable(sre);
		assertEquals(sre, convertedThrowable);
	}

	@Test
	public void testSREWithLocalCause() {
		ServiceRuntimeException sre = new ServiceRuntimeException("This is an error", new DummyException("Error occured"));
		Throwable convertedThrowable = interceptor.convertThrowable(sre);
		assertStructureEquals(sre, convertedThrowable);
	}

	@Test
	public void testSREWithSBECause() {
		ServiceBusinessException sbe = new ServiceBusinessException(new Long(12355));
		ServiceRuntimeException sre = new ServiceRuntimeException("This is an error", sbe);
		Throwable convertedThrowable = interceptor.convertThrowable(sre);
		assertStructureEquals(sre, convertedThrowable);
	}
	
	@Test
	public void testLocalWithLocalCause() {
		DummyException throwable = new DummyException("This is an error", new DummyException("Error occured"));
		Throwable convertedThrowable = interceptor.convertThrowable(throwable);
		assertStructureEquals(throwable, convertedThrowable);
	}
	
	@Test
	public void testLocalWithJavaWithJavaCause() {
		DummyException throwable = new DummyException("This is an error", new IllegalArgumentException("Error occured", new IllegalStateException("Nested error")));
		Throwable convertedThrowable = interceptor.convertThrowable(throwable);
		assertStructureEquals(throwable, convertedThrowable);
		assertEquals(throwable.getCause(), convertedThrowable.getCause());
	}

	@Test
	public void testSREWithSRECauseWithSRECause() {
		ServiceRuntimeException sre = new ServiceRuntimeException("Error #1", new ServiceRuntimeException("Error #2",
				new ServiceRuntimeException("Error #3")));
		Throwable convertedThrowable = interceptor.convertThrowable(sre);
		assertStructureEquals(sre, convertedThrowable);
	}

	public class DummyException extends Exception {
		private static final long serialVersionUID = 1L;

		public DummyException() {
			super();
			// TODO Auto-generated constructor stub
		}

		public DummyException(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public DummyException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}

		public DummyException(Throwable cause) {
			super(cause);
			// TODO Auto-generated constructor stub
		}
	}
}
