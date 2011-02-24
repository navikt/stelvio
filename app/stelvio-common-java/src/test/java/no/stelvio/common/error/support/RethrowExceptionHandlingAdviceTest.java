package no.stelvio.common.error.support;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

/**
 * Unit test for {@link RethrowExceptionHandlingAdvice}.
 * 
 * @author personf8e9850ed756
 */
public class RethrowExceptionHandlingAdviceTest {
	private final Mockery context = new JUnit4Mockery();

	/**
	 * Test that exception is thrown.
	 */
	@Test
	public void exceptionsIsThrown() {

		try {

			final IllegalArgumentException throwable = new IllegalArgumentException("test");

			RethrowExceptionHandlingAdvice advice = new RethrowExceptionHandlingAdvice();

			advice.afterThrowing(throwable);
			fail("Expected throwable to be thrown.");

		} catch (Throwable t) {
			assertTrue(true);
		}

	}
}
