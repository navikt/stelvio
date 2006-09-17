package no.stelvio.common.framework.test.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import junit.framework.AssertionFailedError;

import no.stelvio.common.framework.test.web.ActionModifierRequestProcessor;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

/**
 * Unit test for {@link ActionModifierRequestProcessor}.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2711 $, $Date: 2005-12-13 15:06:24 +0100 (Tue, 13 Dec 2005) $
 */
public class ActionModifierRequestProcessorTest extends MockObjectTestCase {
	private ActionModifierRequestProcessor requestProcessor;
	private Mock mockActionServlet;

	public void testActionServletShouldNotBeNull() {
		try {
			new ActionModifierRequestProcessor(new Action(), null, new RequestProcessor());
			fail("AssertionFailedError should have been thrown");
		} catch (AssertionFailedError afe) {
			assertEquals("Wrong error message;", "actionServlet cannot be null", afe.getMessage());
		}
	}

	public void testDelegateRequestProcessorShouldNotBeNull() {
		try {
			new ActionModifierRequestProcessor(new Action(), new ActionServlet(), null);
			fail("AssertionFailedError should have been thrown");
		} catch (AssertionFailedError afe) {
			assertEquals("Wrong error message;", "delegateRequestProcessor cannot be null", afe.getMessage());
		}
	}

	/**
	 * Sets up a request processor without specifying which action to return -> The one defined in Struts' config should
	 * be used.
	 */
	public void testActionCanBeNullThenActionFromConfigWillBeUsed() throws IOException {
		requestProcessor = new ActionModifierRequestProcessor(null, new ActionServlet(), new RequestProcessor());
		final Action action =
			runProcessActionCreate("no.stelvio.common.framework.test.web.ActionModifierRequestProcessorTest$SecondDummyAction");

		assertEquals("Not the correct Action class returned;", SecondDummyAction.class, action.getClass());
	}

	public void testActionShouldBeInstantiatedWithActionServlet() throws IOException {
		final Action action =
			runProcessActionCreate("no.stelvio.common.framework.test.web.ActionModifierRequestProcessorTest$FirstDummyAction");

		// Is it ok?
		assertSame("Not the same action servlet instance", mockActionServlet.proxy(), action.getServlet());
	}

	/**
	 * Tests that an IllegalArgumentException is thrown if the class type specified in struts-config.xml (here it is mocked
	 * as the input to <code>runProcessActionCreate()</code>) is not of the same type as the user has specified when
	 * testing.
	 *
	 * @throws IOException should not be thrown.
	 */
	public void testActionClassTypeFromStrutsConfigMustBeUsed() throws IOException {
		try {
			runProcessActionCreate("no.stelvio.common.framework.test.web.ActionModifierRequestProcessorTest$SecondDummyAction");
			fail("AssertionFailedError should have been thrown");
		} catch (AssertionFailedError afe) {
			assertEquals(
				"Not the correct exception message",
				"Specified class should be this: "
					+ "no.stelvio.common.framework.test.web.ActionModifierRequestProcessorTest$SecondDummyAction",
				afe.getMessage());
		}

	}

	public void testDelegateCallsToWrappedRequestProcessor() throws IOException, ServletException {
		final boolean[] isCalled = new boolean[] { false };

		final RequestProcessor requestProcessor =
			new ActionModifierRequestProcessor(null, new ActionServlet(), new RequestProcessor() {
			protected HttpServletRequest processMultipart(HttpServletRequest request) {
				isCalled[0] = true;
				return null;
			}
		});

		try {
			requestProcessor.process(null, null);
			fail("AssertionFailedError should have been thrown");
		} catch (AssertionFailedError afe) {
			assertEquals(
				"Wrong error message;",
				"Something failed when executing processPath: " + "java.lang.NullPointerException",
				afe.getMessage());
		}

		assertTrue("The method is not called", isCalled[0]);
	}

	/**
	 * Creates a mock for action mapping, sets it up with the specified action class name and runs the
	 * <code>requestProcessor.processActionCreate()</code> to create the action instance.
	 *
	 * @param actionClassName the name of the action that should be created by <code>requestProcessor.processActionCreate()</code>.
	 * When using the request processor in a test case, it will fetch this class name from <code>struts-config.xml</code>.
	 * @return the instance of the class specified in actionClassName.
	 * @throws IOException should not be thrown.
	 */
	private Action runProcessActionCreate(final String actionClassName) throws IOException {
		final Mock mockActionMapping = mock(ActionMapping.class);
		// Sets which class the original request processor should instantiate.
		mockActionMapping.expects(once()).method("getType").will(returnValue(actionClassName));

		// Run the test
		return requestProcessor.processActionCreate(null, null, (ActionMapping) mockActionMapping.proxy());
	}

	protected void setUp() throws Exception {
		mockActionServlet = mock(ActionServlet.class);
		requestProcessor =
			new ActionModifierRequestProcessor(
				new FirstDummyAction(),
				(ActionServlet) mockActionServlet.proxy(),
				new RequestProcessor());
	}

	public static class FirstDummyAction extends Action {
	};
	public static class SecondDummyAction extends Action {
	};
}
