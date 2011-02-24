/**
 * 
 */
package no.stelvio.presentation.jsf.codestable;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import no.stelvio.presentation.jsf.mock.SpringDefinition;

import org.apache.shale.test.mock.MockFacesContext;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Class CodesTableItemOutputTextTest tests the CodesTableItemOutputText.
 * 
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
public class CodesTableItemOutputTextTest {

	private static CodesTableItemOutputText ctiOutputText;

	/**
	 * Declare before class.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@BeforeClass
	public static void setUpOnce() throws Exception {
		SpringDefinition.getContext();
		ctiOutputText = new CodesTableItemOutputText();
	}

	/**
	 * Test codestable item output text.
	 * 
	 * @throws IOException
	 *             ioexception
	 */
	@Test
	public void testCtiOutputText() throws IOException {
		ctiOutputText.setCodeValue(KravStatusCode.TIL_BEHANDLING.name());
		ctiOutputText.setCtiClass("no.stelvio.presentation.jsf.codestable.KravStatusCti");
		ctiOutputText.encodeBegin(MockFacesContext.getCurrentInstance());
		assertEquals("Til behandling", ctiOutputText.getValue());
	}

}
