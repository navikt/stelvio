package no.trygdeetaten.common.framework.test.web;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import junit.framework.AssertionFailedError;
import servletunit.ServletContextSimulator;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ModuleConfig;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

/**
 * Unit test of {@link ActionModifierActionServlet}.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2711 $, $Date: 2005-12-13 15:06:24 +0100 (Tue, 13 Dec 2005) $
 */
public class ActionModifierActionServletTest extends MockObjectTestCase {
	private Mock mockAction;
	private ActionModifierActionServlet actionServlet;
	private ServletContextSimulator servletContext;
	private Mock mockModuleConfig;
	private boolean[] called = new boolean[] { false };

	/**
	 * It should have unit-test as its servlet name so Spring won't load the same configuration file in test.
	 */
	public void testHasUnitTestAsItsServletName() {
		assertEquals("Wrong name;", "unit-test", actionServlet.getServletName());
	}

	/**
	 * Need to use our RequestProcessor so the Action class can be set and not instantiated by the framework.
	 *
	 * @throws ServletException should not be thrown.
	 */
	public void testOurRequestProcessorIsUsed() throws ServletException {
		setupRequestProcessorInContext();

		assertEquals("Wrong class;",
		        ActionModifierRequestProcessor.class,
		        actionServlet.getRequestProcessor((ModuleConfig) mockModuleConfig.proxy()).getClass());
	}

	public void testOurRequestProcessorDelegatesCallsToOriginal() throws ServletException, IOException {
		setupRequestProcessorInContext();

		final RequestProcessor requestProcessor = actionServlet.getRequestProcessor((ModuleConfig) mockModuleConfig.proxy());
		try {
			requestProcessor.process(null, null);
			fail("AssertionFailedError should have been thrown");
		} catch (AssertionFailedError afe) {
			assertEquals("Wrong error message;", "Something failed when executing processPath: " +
			        "java.lang.NullPointerException", afe.getMessage());
		}

		assertTrue("Method calls are not delegated", called[0]);
	}

	private void setupRequestProcessorInContext() {
		mockModuleConfig.expects(exactly(2)).method("getPrefix").withNoArguments().will(returnValue(""));
		servletContext.setAttribute(Globals.REQUEST_PROCESSOR_KEY, new RequestProcessor() {
			protected HttpServletRequest processMultipart(HttpServletRequest request) {
				called[0] = true;
				return null;
			}
		});
	}

	public void testSpecifiedActionCannotBeNull() {
		try {
			actionServlet.registerActionInstance(null);
			fail("AssertionFailedError should have been thrown");
		} catch (AssertionFailedError afe) {
			// should happen
		}
	}

	protected void setUp() throws Exception {
		mockModuleConfig = mock(ModuleConfig.class);

		servletContext = new ServletContextSimulator();
		mockAction = mock(Action.class);

		actionServlet = new ActionModifierActionServlet() {
			// Need this so our action servlet can put our request processor into the servlet context
			// so it will be used by the Struts framework
			public ServletContext getServletContext() {
				return servletContext;
			}
		};
		actionServlet.registerActionInstance((Action) mockAction.proxy());
	}
}
