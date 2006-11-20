package no.stelvio.common.converter;

import java.util.HashMap;

import junit.framework.TestCase;
import no.stelvio.common.error.UnrecoverableException;

/**
 * 
 * @author person5b7fd84b3197, Accenture
 */
public class ConverterLoaderTest extends TestCase {

	private ConverterLoader loader = null;

	/**
	 * Setup mathod.
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		loader = new ConverterLoader();

	}
	/**
	 * Test successfull init
	 *
	 */
	public void testInit() {
		HashMap map = new HashMap();
		map.put("java.util.Date", new DateConverter());
		loader.setConverters(map);
		
		loader.init();
	}
	
	/**
	 * Test fail init
	 *
	 */
	public void testInitFail() {
		HashMap map = new HashMap();
		map.put("java.uti.Date", new DateConverter());
		loader.setConverters(map);
		try {
			loader.init();
			fail();
		} catch (UnrecoverableException e) {
			assertTrue("Should Fail", true);
		}
		
	}

}
