package no.stelvio.common.error;

import static org.junit.Assert.assertTrue;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.error.SystemUnrecoverableException;

import org.junit.Test;

/**
 * Unit test for {@link InvalidArgumentException}.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class InvalidArgumentExceptionTest {
	@Test
	public void shouldExtendSystemUnrecoverableException() {
		assertTrue(SystemUnrecoverableException.class.isAssignableFrom(InvalidArgumentException.class));
	}
	
	@Test
	public void messageTemplateIncludesArguments() {
		RequestContextSetter.setRequestContextForUnitTest();
		
		try {
			throw new InvalidArgumentException("argName", 31);
		} catch(InvalidArgumentException e) {
			assertTrue(e.getMessage().contains("argName"));
			assertTrue(e.getMessage().contains("31"));
		}
	}
}
