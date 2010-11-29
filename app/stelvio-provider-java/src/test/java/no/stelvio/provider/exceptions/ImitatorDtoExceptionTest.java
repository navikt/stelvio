package no.stelvio.provider.exceptions;

import static org.junit.Assert.assertTrue;
import no.stelvio.common.config.MissingPropertyException;
import no.stelvio.dto.error.strategy.support.ImitatorDtoException;

import org.junit.Test;

/**
 * Test is placed in provider layer, as it must be run in a Java 5.0 VM.
 * 
 * @author person983601e0e117 (Accenture)
 * 
 */
public class ImitatorDtoExceptionTest {

	/**
	 * Testing imitator creation.
	 */
	@Test
	public void testImitatorCreation() {
		MissingPropertyException ex50 = new MissingPropertyException("Required properties was not set by configuration",
				new String[] { "testProp" });
		ImitatorDtoException ex14 = new ImitatorDtoException(ex50);
		assertTrue("ImitatorException doesn't match DTO Exception", ex14.toString().contains(ex50.getClass().getName()));

	}

}
