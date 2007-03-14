package no.stelvio.consumer;

import static org.junit.Assert.*;
import no.stelvio.common.context.support.RequestContextSetter;
import no.stelvio.common.error.SystemUnrecoverableException;

import org.junit.Test;

/**
 * Unit test for {@link ConsumerSystemException}.
 * 
 * @author personf8e9850ed756, Accenture
 */
public class ConsumerSystemExceptionTest {
	@Test
	public void shouldExtendSystemUnrecoverableException() {
		assertTrue(SystemUnrecoverableException.class.isAssignableFrom(ConsumerSystemException.class));
	}
	
	@Test
	public void messageTemplateIncludesServiceId() {
		RequestContextSetter.setRequestContextForUnitTest();
		
		try {
			throw new ConsumerSystemException(new IllegalArgumentException("test"), "serviceId1");
		} catch(ConsumerSystemException e) {
			assertTrue(e.getMessage().contains("serviceId1"));
		}
	}
}
