 package no.trygdeetaten.common.framework.test.web;

import javax.servlet.ServletException;

import junit.framework.AssertionFailedError;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ModuleConfig;

/**
 * Gives the ability to choose our own instance of an Struts Action.
 *
 * <p>This is achieved by using this class as the action servlet for the MockStrutsTestCase. This class will create
 * an instance of ActionModifierRequestProcessor when the Struts framework asks for a request processor. This
 * request processor will give the Action instance specified by the user to the Struts framework to use in the test.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2711 $, $Date: 2005-12-13 15:06:24 +0100 (Tue, 13 Dec 2005) $
 * @see ActionModifierRequestProcessor
 */
public class ActionModifierActionServlet extends ActionServlet {
	/** The Struts Action that will be used in the test. **/
	private Action action;

	/** Only created by classes in the same package */
	ActionModifierActionServlet() {}

	/**
	 * Creates an instance of ActionModifierRequestProcessor that Struts will use as its request processor. All calls
	 * will be delegated to the request processor specified in struts-config.xml.
	 *
	 * @param config the configuration for the module; is used for retrievin the module's prefix.
	 * @return an instance of ActionModifierRequestProcessor that Struts will use as its request processor.
	 * @see ActionModifierRequestProcessor
	 * @throws ServletException if something goes wrong initializing the request processor.
	 */
	protected synchronized RequestProcessor getRequestProcessor(ModuleConfig config) throws ServletException {
		final String key = Globals.REQUEST_PROCESSOR_KEY + config.getPrefix();
		final RequestProcessor originalRequestProcessor = super.getRequestProcessor(config);
		final ActionModifierRequestProcessor requestProcessor = new ActionModifierRequestProcessor(action, this, originalRequestProcessor);

		requestProcessor.init(this, config);
		getServletContext().setAttribute(key, requestProcessor);

		return requestProcessor;
	}

	/**
	 * Overrides the name of the "unit-test" so Spring don't load it's real configuration file but the one for unit-test.
	 *
	 * @return "unit-test" so Spring don't load it's real configuration file but the one for unit-test.
	 */
	public String getServletName() {
		return "unit-test";
	}

	/**
	 * Initializes the instance with the action to use in the test.
	 *
	 * @param action the action to use in the test.
	 * @throws IllegalArgumentException if action is null.
	 */
	void registerActionInstance(Action action) {
		if (null == action) {
			throw new AssertionFailedError("action cannot be null");
		}

		this.action = action;
	}

	/**
	 * Returns the action instantiated by the user and which will be used by the Struts framework.
	 *
	 * @return the action instantiated by the user and which will be used by the Struts framework.
	 */
	Action getAction() {
		return action;
	}
}
