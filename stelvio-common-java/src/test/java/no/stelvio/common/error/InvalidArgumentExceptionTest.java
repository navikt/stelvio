package no.stelvio.common.error;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * Unit test for {@link InvalidArgumentException}.
 * 
 */
public class InvalidArgumentExceptionTest {
	
	/**
	 * Test shouldExtendFunctionalUnrecoverableException.
	 */
	@Test
	public void shouldExtendFunctionalUnrecoverableException() {
		assertTrue(FunctionalUnrecoverableException.class.isAssignableFrom(InvalidArgumentException.class));
	}
	
	/**
	 * Test exceptionCreatedWithArgumentNameAndValue.
	 */
	@Test
	public void exceptionCreatedWithArgumentNameAndValue() {
		String argumentName = "argument name";
		String argumentValue = "argument value";
		InvalidArgumentException iae = new InvalidArgumentException("An invalid argument exception occured", 
																	argumentName, argumentValue, new Exception());
		assertEquals(argumentName, iae.getArgumentName());
		assertEquals(argumentValue, iae.getArgumentValue());
	}
	
}
