package no.stelvio.common.error;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Integration test for error handling.
 * 
 * @author personf8e9850ed756
 */
public class ErrorHandlingIntegrationTest extends AbstractDependencyInjectionSpringContextTests {
	private TestBean testBean;

	// This test doesn't work as logger isn't set up to write to system.err
	// public void testWhenDatabaseIsDownMessageShouldComeFromExceptionMessage() throws Exception {
	// PrintStream oldErr = null;
	// ByteArrayOutputStream out = null;
	//
	// try {
	// oldErr = System.err;
	// out = new ByteArrayOutputStream();
	// System.setErr(new PrintStream(out));
	// testBean.callThrower();
	// fail("ImitatorException should have been thrown");
	// } catch (RuntimeException e) {
	// assertEquals("Not the correct message written to stderr", "test", out.toString());
	// } finally {
	// System.setErr(oldErr);
	// }
	// }

	/** No relevant after upgrade to new error framework. */
	public void testShouldThrowImitatorException() {
		try {
			testBean.callThrower();
			fail("Exception should have been thrown");
		} catch (RuntimeException e) {
			// ok
		}
	}

	/**
	 * Get config locations.
	 * 
	 * @return locations
	 */
	protected String[] getConfigLocations() {
		return new String[] { "error-integration-test-context.xml" };
	}

	/**
	 * Set testbean.
	 * 
	 * @param testBean
	 *            testbean
	 */
	public void setTestBean(final TestBean testBean) {
		this.testBean = testBean;
	}
}
