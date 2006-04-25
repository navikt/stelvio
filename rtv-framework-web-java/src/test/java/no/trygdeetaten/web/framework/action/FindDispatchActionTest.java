package no.trygdeetaten.web.framework.action;

import java.util.ArrayList;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
import servletunit.HttpServletRequestSimulator;
import servletunit.ServletContextSimulator;
import servletunit.struts.MockStrutsTestCase;

import no.trygdeetaten.web.framework.constants.Constants;

/** @author person356941106810, Accenture */
public class FindDispatchActionTest extends MockStrutsTestCase {

	/*
	 * Test for ActionForward execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public void testExecuteActionMappingActionFormHttpServletRequestHttpServletResponse() {
		setRequestPathInfo("/testFindDispatchAction.do");
		addRequestParameter("executeTest", "");
		// do not set windowid
		// do not set any admin messages
		actionPerform();
		verifyNoActionErrors();

		// test set admin messages
		ArrayList l = new ArrayList(3);
		l.add("Message 1");
		l.add("Message 2");
		l.add("Message 3");
		getActionServlet().getServletContext().setAttribute(Constants.ADMIN_MESSAGE, l);
		actionPerform();
		verifyActionMessages(new String[]{"Message 1", "Message 3", "Message 2"});
	}

	public void testCheckingForMessages() {
		final FindDispatchAction findDispatchAction = new FindDispatchAction() {};
		final HttpServletRequestSimulator request = new HttpServletRequestSimulator(new ServletContextSimulator());

		assertFalse("Should not contain action messages", findDispatchAction.isActionMessages(request));

		final ActionMessages actionMessages = new ActionMessages();
		actionMessages.add("test", new ActionMessage("test"));
		request.setAttribute(Globals.MESSAGE_KEY, actionMessages);
		assertTrue("Should contain action messages", findDispatchAction.isActionMessages(request));
	}
}
