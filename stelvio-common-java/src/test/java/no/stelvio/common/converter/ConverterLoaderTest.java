package no.stelvio.common.converter;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link no.stelvio.common.converter.ConverterLoader}.
 * 
 */
public class ConverterLoaderTest {
	private ConverterLoader loader = null;

	/**
	 * Test convertersIsLoadedWhenNoErrorsPresentInSetup.
	 */
	@Test
	public void convertersIsLoadedWhenNoErrorsPresentInSetup() {
		setupConverters("java.util.Date");

		loader.init();
	}

	/**
	 * Test convertersIsNoLoadedWhenErrorsPresentInSetup.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void convertersIsNoLoadedWhenErrorsPresentInSetup() {
		setupConverters("java.uti.Date");

		loader.init();
	}

	/**
	 * Setup before test.
	 */
	@Before
	public void setupForTest() {
		loader = new ConverterLoader();
	}

	/**
	 * Set up converters.
	 * 
	 * @param sd
	 *            date
	 */
	private void setupConverters(String sd) {
		Map<String, DateConverter> map = new HashMap<String, DateConverter>();
		map.put(sd, new DateConverter());
		loader.setConverters(map);
	}
}
