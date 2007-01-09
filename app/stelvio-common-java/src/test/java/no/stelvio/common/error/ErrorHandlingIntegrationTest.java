package no.stelvio.common.error;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Integration test for error handling.
 *
 * @author personf8e9850ed756
 */
public class ErrorHandlingIntegrationTest extends AbstractDependencyInjectionSpringContextTests{
	private TestBean testBean;

	public void testWhenDatabaseIsDownMessageShouldComeFromExceptionMessage() throws Exception {
		PrintStream oldErr = null;
		ByteArrayOutputStream out = null;

		try {
			oldErr = System.err;
			out = new ByteArrayOutputStream();
			System.setErr(new PrintStream(out));
			TestErrorsRetriever.setThrowException(true);

			testBean.callThrower();
			fail("ImitatorException should have been thrown");
		} catch (RuntimeException e) {
			assertEquals("Not the correct message written to stderr", "test", out.toString());
		} finally {
			System.setErr(oldErr);
		}
	}

	public void testShouldThrowImitatorException() {
		try {
			testBean.callThrower();
			fail("ImitatorException should have been thrown");
		} catch (RuntimeException e) {
			// Cannot catch the ImitatorException here as it is package private
			assertEquals("Wrong exception thrown",
					"no.stelvio.common.error.strategy.support.ImitatorException", e.getClass().getName());
		}
	}

	protected String[] getConfigLocations() {
        return new String[] {"error-integration-test-context.xml"};
    }

	public void setTestBean(final TestBean testBean) {
		this.testBean = testBean;
	}
}
