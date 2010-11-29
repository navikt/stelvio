package no.stelvio.presentation.binding.context;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;

import no.stelvio.presentation.jsf.context.FieldInListError;

import org.junit.Assert;
import org.junit.Test;

/**
 * MessagesFixSourcePhaseListenerTest.
 * 
 * @author AW
 *
 */
public class MessagesFixSourcePhaseListenerTest {

	/**
	 * Test RegExMatchesFieldInListError.
	 * 
	 * @throws ServletException servlet exception
	 * @throws IOException ioexception
	 */
	@Test
	public void testRegExMatchesFieldInListError() throws ServletException, IOException {
		MessagesFixSourcePhaseListener listener = new MessagesFixSourcePhaseListener();

		String clientId = "0:land";
		assertTrue(listener.isFieldInListError(clientId));

		clientId = "2:land";
		assertTrue(listener.isFieldInListError(clientId));

		clientId = "12:land";
		assertTrue(listener.isFieldInListError(clientId));

		clientId = "1256565:land";
		assertTrue(listener.isFieldInListError(clientId));

		clientId = "34:heiOgHa:per";
		assertTrue(listener.isFieldInListError(clientId));

		clientId = "12h:land";
		assertFalse(listener.isFieldInListError(clientId));

		clientId = "12h34:land";
		assertFalse(listener.isFieldInListError(clientId));

		clientId = "land";
		assertFalse(listener.isFieldInListError(clientId));

		clientId = "test:land";
		assertFalse(listener.isFieldInListError(clientId));

		clientId = "t12:land";
		assertFalse(listener.isFieldInListError(clientId));
	}

	/**
	 * Test jo.
	 */
	@Test
	public void testJo() {
		String clientId = "12345:land";

		int index = clientId.indexOf(FieldInListError.SEPERATOR);
		String trimmedClientId = clientId.substring(index + 1);
		String prefix = clientId.substring(0, index + 1);

		Assert.assertEquals("land", trimmedClientId);
		Assert.assertEquals("12345:", prefix);
	}
}
