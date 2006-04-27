package no.nav.common.framework.test.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.AssertionFailedError;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.springframework.web.struts.DelegatingActionProxy;

/**
 * Request processor used in conjunction with ActionModifierActionServlet to enable the setting of a specific instance
 * of the Action class to test instead of letting the Struts framework instantiate for us.
 * <p/>
 * <p>This enables mocking of classes the Action is using and is used by the JMockableStrutsTestCase.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2106 $, $Date: 2005-03-09 10:05:35 +0100 (Wed, 09 Mar 2005) $
 * @see ActionModifierActionServlet
 * @see JMockableStrutsTestCase
 */
public class ActionModifierRequestProcessor extends RequestProcessor {
	private ActionServlet actionServlet;
	private Action action;
	private final RequestProcessor delegateRequestProcessor;

	/**
	 * Used to retrieve the current action forward from the request.
	 */
	static final String ACTION_FORWARD = ActionModifierRequestProcessor.class.getName() + ".ACTION_FORWARD";

	/**
	 * Initializes the instance with the action and action servlet to use in the test.
	 *
	 * @param action the action to use in the test. If it is null, the instance loaded by Struts is used.
	 * @param actionServlet the action servlet used in the test.
	 * @param delegateRequestProcessor the request processor to delegate all calls to.
	 * @throws IllegalArgumentException if action or action servlet is null.
	 */
	public ActionModifierRequestProcessor(
		Action action,
		ActionServlet actionServlet,
		RequestProcessor delegateRequestProcessor) {
		if (null == actionServlet) {
			throw new AssertionFailedError("actionServlet cannot be null");
		}

		if (null == delegateRequestProcessor) {
			throw new AssertionFailedError("delegateRequestProcessor cannot be null");
		}

		this.action = action;
		this.actionServlet = actionServlet;
		this.delegateRequestProcessor = delegateRequestProcessor;
	}

	public void destroy() {
		delegateRequestProcessor.destroy();
	}

	public void init(ActionServlet servlet, ModuleConfig moduleConfig) throws ServletException {
		delegateRequestProcessor.init(servlet, moduleConfig);
	}

	protected ActionForm processActionForm(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) {
		return (ActionForm) callMethodOnDelegateRequestProcessor(
			"processActionForm",
			new Class[] { HttpServletRequest.class, HttpServletResponse.class, ActionMapping.class },
			new Object[] { request, response, mapping });
	}

	/** @deprecated */
	protected void processActionForward(HttpServletRequest request, HttpServletResponse response, ActionForward forward)
		throws IOException, ServletException {
		callMethodOnDelegateRequestProcessor(
			"processActionForward",
			new Class[] { HttpServletRequest.class, HttpServletResponse.class, ActionForward.class },
			new Object[] { request, response, forward });
	}

	protected void processForwardConfig(HttpServletRequest request, HttpServletResponse response, ForwardConfig forward)
		throws IOException, ServletException {
		callMethodOnDelegateRequestProcessor(
			"processForwardConfig",
			new Class[] { HttpServletRequest.class, HttpServletResponse.class, ForwardConfig.class },
			new Object[] { request, response, forward });
	}

	protected ActionForward processActionPerform(
		HttpServletRequest request,
		HttpServletResponse response,
		Action action,
		ActionForm form,
		ActionMapping mapping)
		throws IOException, ServletException {
		final ActionForward actionForward =
			(ActionForward) callMethodOnDelegateRequestProcessor("processActionPerform",
				new Class[] {
					HttpServletRequest.class,
					HttpServletResponse.class,
					Action.class,
					ActionForm.class,
					ActionMapping.class },
				new Object[] { request, response, action, form, mapping });
		request.setAttribute(ACTION_FORWARD, actionForward);

		return actionForward;
	}

	protected void processContent(HttpServletRequest request, HttpServletResponse response) {
		callMethodOnDelegateRequestProcessor(
			"processContent",
			new Class[] { HttpServletRequest.class, HttpServletResponse.class },
			new Object[] { request, response });
	}

	protected ActionForward processException(
		HttpServletRequest request,
		HttpServletResponse response,
		Exception exception,
		ActionForm form,
		ActionMapping mapping)
		throws IOException, ServletException {
		return (ActionForward) callMethodOnDelegateRequestProcessor(
			"processException",
			new Class[] {
				HttpServletRequest.class,
				HttpServletResponse.class,
				Exception.class,
				ActionForm.class,
				ActionMapping.class },
			new Object[] { request, response, exception, form, mapping });
	}

	protected boolean processForward(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
		throws IOException, ServletException {
		final Boolean ok =
			(Boolean) callMethodOnDelegateRequestProcessor("processForward",
				new Class[] { HttpServletRequest.class, HttpServletResponse.class, ActionMapping.class },
				new Object[] { request, response, mapping });
		return ok.booleanValue();
	}

	protected boolean processInclude(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
		throws IOException, ServletException {
		final Boolean ok =
			(Boolean) callMethodOnDelegateRequestProcessor("processInclude",
				new Class[] { HttpServletRequest.class, HttpServletResponse.class, ActionMapping.class },
				new Object[] { request, response, mapping });
		return ok.booleanValue();
	}

	protected void processLocale(HttpServletRequest request, HttpServletResponse response) {
		callMethodOnDelegateRequestProcessor(
			"processLocale",
			new Class[] { HttpServletRequest.class, HttpServletResponse.class },
			new Object[] { request, response });
	}

	protected ActionMapping processMapping(HttpServletRequest request, HttpServletResponse response, String path)
		throws IOException {
		return (ActionMapping) callMethodOnDelegateRequestProcessor(
			"processMapping",
			new Class[] { HttpServletRequest.class, HttpServletResponse.class, String.class },
			new Object[] { request, response, path });
	}

	protected HttpServletRequest processMultipart(HttpServletRequest request) {
		return (HttpServletRequest) callMethodOnDelegateRequestProcessor(
			"processMultipart",
			new Class[] { HttpServletRequest.class },
			new Object[] { request });
	}

	protected void processNoCache(HttpServletRequest request, HttpServletResponse response) {
		callMethodOnDelegateRequestProcessor(
			"processNoCache",
			new Class[] { HttpServletRequest.class, HttpServletResponse.class },
			new Object[] { request, response });
	}

	protected String processPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return (String) callMethodOnDelegateRequestProcessor(
			"processPath",
			new Class[] { HttpServletRequest.class, HttpServletResponse.class },
			new Object[] { request, response });
	}

	protected void processPopulate(
		HttpServletRequest request,
		HttpServletResponse response,
		ActionForm form,
		ActionMapping mapping)
		throws ServletException {
		callMethodOnDelegateRequestProcessor(
			"processPopulate",
			new Class[] { HttpServletRequest.class, HttpServletResponse.class, ActionForm.class, ActionMapping.class },
			new Object[] { request, response, form, mapping });
	}

	protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
		final Boolean ok =
			(Boolean) callMethodOnDelegateRequestProcessor("processPreprocess",
				new Class[] { HttpServletRequest.class, HttpServletResponse.class },
				new Object[] { request, response });
		return ok.booleanValue();
	}

	protected boolean processRoles(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
		throws IOException, ServletException {
		final Boolean ok =
			(Boolean) callMethodOnDelegateRequestProcessor("processRoles",
				new Class[] { HttpServletRequest.class, HttpServletResponse.class, ActionMapping.class },
				new Object[] { request, response, mapping });
		return ok.booleanValue();
	}

	protected boolean processValidate(
		HttpServletRequest request,
		HttpServletResponse response,
		ActionForm form,
		ActionMapping mapping)
		throws IOException, ServletException {
		final Boolean ok =
			(Boolean) callMethodOnDelegateRequestProcessor("processValidate",
				new Class[] { HttpServletRequest.class, HttpServletResponse.class, ActionForm.class, ActionMapping.class },
				new Object[] { request, response, form, mapping });
		return ok.booleanValue();
	}

	protected void internalModuleRelativeForward(String uri, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		callMethodOnDelegateRequestProcessor(
			"internalModuleRelativeForward",
			new Class[] { String.class, HttpServletRequest.class, HttpServletResponse.class },
			new Object[] { uri, request, response });
	}

	protected void internalModuleRelativeInclude(String uri, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		callMethodOnDelegateRequestProcessor(
			"internalModuleRelativeInclude",
			new Class[] { String.class, HttpServletRequest.class, HttpServletResponse.class },
			new Object[] { uri, request, response });
	}

	protected void doForward(String uri, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		callMethodOnDelegateRequestProcessor(
			"doForward",
			new Class[] { String.class, HttpServletRequest.class, HttpServletResponse.class },
			new Object[] { uri, request, response });
	}

	protected void doInclude(String uri, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		callMethodOnDelegateRequestProcessor(
			"doInclude",
			new Class[] { String.class, HttpServletRequest.class, HttpServletResponse.class },
			new Object[] { uri, request, response });
	}

	/** @deprecated */
	public int getDebug() {
		final Integer debug = (Integer) callMethodOnDelegateRequestProcessor("getDebug", new Class[] {
		}, new Object[] {
		});
		return debug.intValue();
	}

	protected MessageResources getInternal() {
		return (MessageResources) callMethodOnDelegateRequestProcessor("getInternal", new Class[] {
		}, new Object[] {
		});
	}

	protected ServletContext getServletContext() {
		return (ServletContext) callMethodOnDelegateRequestProcessor("getServletContext", new Class[] {
		}, new Object[] {
		});
	}

	protected void log(String message) {
		callMethodOnDelegateRequestProcessor("log", new Class[] { String.class }, new Object[] { message });
	}

	protected void log(String message, Throwable exception) {
		callMethodOnDelegateRequestProcessor(
			"log",
			new Class[] { String.class, Exception.class },
			new Object[] { message, exception });
	}

	/**
	 * Injects into the creation of the Struts Action class, enabling the user to specify the instance of the action class
	 * that should be used by the Struts framework.
	 * <p/>
	 * <p>It will also call the real request processor's processActionCreate so any errors in struts-config.xml are shown
	 * and at the same time being able to check that the action class instance specified by the user is of the same type as
	 * specified in the <code>struts-config.xml</code>
	 *
	 * @param request the request object.
	 * @param response the response object.
	 * @param mapping the ActionMapping instance holding the info in the <code>struts-config.xml</code> for the matching
	 * request path.
	 * @return the action specified by the user in the test case.
	 * @see servletunit.struts.MockStrutsTestCase#setActionServlet(org.apache.struts.action.ActionServlet)
	 * @see ActionModifierActionServlet
	 */
	protected Action processActionCreate(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
		throws IOException {
		// Create the action from the struts-config.xml, detecting any errors in it by doing so
		Action tmp =
			(Action) callMethodOnDelegateRequestProcessor("processActionCreate",
				new Class[] { HttpServletRequest.class, HttpServletResponse.class, ActionMapping.class },
				new Object[] { request, response, mapping });

		// If user has not specified an action, the action created by Struts is used
		if (null == action) {
			action = tmp;
			// User MUST specify the same action class type as the one specified in struts-config.xml
			// BUT, for now, if Spring's DelegatingActionProxy is used, we won't check it
			// LATER: find the real action DelegatingActionProxy will use and test for that
		} else if (!tmp.getClass().equals(action.getClass()) && !DelegatingActionProxy.class.equals(tmp.getClass())) {
			throw new AssertionFailedError("Specified class should be this: " + tmp.getClass().getName());
		}

		action.setServlet(actionServlet);
		return action;
	}

	/**
	 * Calls a method on the delegate request processor.
	 *
	 * @param methodName method to call.
	 * @param params parameters to pass to method.
	 * @return the return value from the method.
	 */
	private Object callMethodOnDelegateRequestProcessor(String methodName, Class[] paramTypes, Object params[]) {
		final Object value;

		try {
			final Method method = getDeclaredMethodOnDelegateRequestProcessor(methodName, paramTypes);
			method.setAccessible(true);
			value = method.invoke(delegateRequestProcessor, params);
		} catch (IllegalAccessException iae) {
			throw new AssertionFailedError(methodName + " is inaccessible");
		} catch (InvocationTargetException ite) {
			Throwable cause = ite.getCause();

			if (cause instanceof AssertionFailedError) {
				throw (AssertionFailedError) cause;
			}

			throw new AssertionFailedError(
				"Something failed when executing " + methodName + getChainedExceptionDescription(cause));
		}

		return value;
	}

	/**
	 * Merging the messages for the chained exceptions.
	 *
	 * @param cause the exception to work with.
	 * @return a string with the merged messages.
	 */
	private String getChainedExceptionDescription(Throwable cause) {
		StringBuffer description = new StringBuffer();

		while (null != cause) {
			description.append(": ").append(cause);

			if (cause instanceof ServletException) {
				cause = ((ServletException) cause).getRootCause();
			} else {
				cause = cause.getCause();
			}
		}

		return description.toString();
	}

	private Method getDeclaredMethodOnDelegateRequestProcessor(String methodName, Class[] paramTypes) {
		Method methodFound = null;

		for (Class clazz = delegateRequestProcessor.getClass();
			null != clazz && null == methodFound;
			clazz = clazz.getSuperclass()) {
			final Method[] methods = clazz.getDeclaredMethods();

			for (int cnt = 0; cnt < methods.length && null == methodFound; cnt++) {
				Method method = methods[cnt];

				if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), paramTypes)) {
					methodFound = method;
				}
			}
		}

		if (null == methodFound) {
			throw new AssertionFailedError(methodName + " does not exist in request processor");
		}

		return methodFound;
	}
}
