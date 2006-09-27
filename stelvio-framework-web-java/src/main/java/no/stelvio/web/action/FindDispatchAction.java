package no.stelvio.web.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.actions.DispatchAction;

import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.config.Config;
import no.stelvio.common.context.RequestContext;
import no.stelvio.common.jmx.ServiceLevelConstants;
import no.stelvio.common.jmx.ServiceLevelMBean;
import no.stelvio.common.jmx.ServiceLevelMBeanFactory;
import no.stelvio.common.service.ServiceDelegate;
import no.stelvio.web.constants.Constants;
import no.stelvio.web.util.RequestUtils;

/**
 * Overrides <code>setServlet</code> and the <code>execute</code> method of Action.
 * Get and returns <i>BusinessDelegate</i> from the session context.  
 *
 * @author Jonas Lindholm, Accenture
 * @version $Id: FindDispatchAction.java 2761 2006-02-07 14:01:51Z skb2930 $
 */
public abstract class FindDispatchAction extends DispatchAction {

	// --------------------------------------------------------- Constants

	/**
	 * The session attributes key under which the form that results from the current
	 * synchronized action is stored.
	 */
	private static final String FORM_KEY = "SYNCHRONIZED.FORM";

	/**
	 * The session attributes key under which the exception that results from the current
	 * synchronized action is stored.
	 */
	private static final String EXCEPTION_KEY = "SYNCHRONIZED.EXCEPTION";

	/**
	 * The session attributes key under which the forward that results from the current
	 * synchronized action is stored.
	 */
	private static final String FORWARD_KEY = "SYNCHRONIZED.FORWARD";

	/**
	 * The session attributes key under which the errors that results from the current
	 * synchronized action is stored.
	 */
	private static final String ERRORS_KEY = "SYNCHRONIZED.ERRORS";

	/**
	 * The request attributes key under which the synchronizer token is stored.
	 */
	protected static final String REQUEST_TOKEN_KEY = org.apache.struts.taglib.html.Constants.TOKEN_KEY;

	/**
	 * The session attributes key under which the token that identifies the current
	 * synchronized action is stored.
	 */
	private static final String CURRENT_TOKEN_KEY = "SYNCHRONIZED.CURRENT_TOKEN";

	/**
	 * The name of the default method to dispatch control to.
	 */
	private static final String DEFAULT_METHOD = Constants.METHOD_PREFIX + RequestUtils.DEFAULT_METHOD;

	// ----------------------------------------------------- Variables

	/** Logger to be used for writing <i>TRACE</i> and <i>DEBUG</i> messages. */
	protected Log log = LogFactory.getLog(getClass());

	/** The presentation services configuration. */
	protected Config config = null;

	/** Holds the <code>ServiceLevelMBean</code> to use. */
	private ServiceLevelMBean serviceLevelMBean = null;

	// --------------------------------------------------------- Public Methods

	/**
	 * Override the Action's setServlet to access the configuration of presentation services 
	 *
	 * @param servlet The new controller servlet, if any
	 */
	public void setServlet(ActionServlet servlet) {
		super.setServlet(servlet);

		// Set the configuration instance if not in destruction mode
		if (null != getServlet()) {
			config = (Config) getServlet().getServletContext().getAttribute(Config.PRESENTATION_SERVICES);
		}
	}

	/**
	 * Return the <i>BusinessDelegate</i> bean defined in the Spring config file that is loaded
	 * at startup of the servlet. The <i>BusinessDelegate</i> bean direct the request to the business
	 * logic layer. 
	 *
	 * @return The BusinessDelegate bean
	 */
	public ServiceDelegate getBusinessDelegate() {
		return (ServiceDelegate) config.getBean(Constants.BUSINESS_DELEGATE_BEAN);
	}

	/**
	 * Return the <i>CodesTableManager</i> bean defined in the Spring config file that is loaded
	 * at startup of the servlet. The <i>CodesTableManager</i> provides caching and lookup services
	 * for codes tables.
	 *
	 * @return The CodesTableManager bean
	 */
	public CodesTableManager getCodesTableManager() {
		return (CodesTableManager) config.getBean(Constants.CODES_TABLE_MANAGER_BEAN);
	}

	/**
	 * The method overrides the superclass execute method with the following logic included:
	 * <ul>
	 * 		<li> Get the method from the request parameter and pass it to the action to be executed </li>
	 * 		<li> Set the windowid from the request parameters and set in the request context  </li>
	 * 		<li> Set a flag in the request context if there are admin messages  </li>
	 * </ul>
	 * 
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * 
	 * @return An ActionForward instance describing where and how control should be forwarded
	 *  
	 * @exception Exception if the application business logic throws
	 *  an exception
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		// Get admin messages
		List msgs = (List) getServlet().getServletContext().getAttribute(Constants.ADMIN_MESSAGE);

		if (null != msgs) {
			ActionMessages actionMessages = new ActionMessages();
			Iterator msg = msgs.iterator();

			while (msg.hasNext()) {
				actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage((String) msg.next()));
			}

			saveMessages(request, actionMessages);
		}

		// Identify the method to execute
		String method = RequestUtils.getMethod(request);

		if (null != request.getParameter(Constants.DONT_CHECK_TOKEN)) {
			// Defect # 101 - Don't bother updating or validating tokens when this parameter is included
			return dispatchMethod(mapping, form, request, response, method);
		} else if ("GET".equals(request.getMethod())) {
			//	Don't bother validating tokens upon HTTP GET
			super.saveToken(request);
			return dispatchMethod(mapping, form, request, response, method);
		} else if (DEFAULT_METHOD.equals(method)) {
			// No need for synchronized execution when loading a window
			// This branch handles the circumstances where window is reloaded 
			// after a button submit
			super.saveToken(request);
			return dispatchMethod(mapping, form, request, response, method);
		} else if (null != request.getAttribute(Constants.DONT_CHECK_TOKEN)) {
			// No need for synchronized execution when returning to a window
			// to execute specified method.
			super.saveToken(request);
			return dispatchMethod(mapping, form, request, response, method);
		} else {
			// Synchronized on the session id, instead of the session itself
			// because the session is a facade that is not guaranteed to be
			// the same object on each request. Its id is the same String
			// instance however.
			HttpSession session = request.getSession();
			synchronized (session.getId()) {

				// Get the request token, session token and current token
				String requestToken = request.getParameter(REQUEST_TOKEN_KEY);
				// String sessionToken = (String) session.getAttribute(Globals.TRANSACTION_TOKEN_KEY);
				String currentToken = (String) session.getAttribute(CURRENT_TOKEN_KEY);

				//				if (log.isDebugEnabled()) {
				//					log.debug("----- Request token: " + requestToken);
				//					log.debug("----- Session token: " + sessionToken);
				//					log.debug("----- Current token: " + currentToken);
				//				}

				// If the request token is the current token then recover the
				// results from the previous synchronized action.
				if (null != currentToken && currentToken.equals(requestToken)) {
					if (log.isDebugEnabled()) {
						log.debug("More than one request has submitted this token");
					}
					return recover(mapping, form, request, response);
				}

				// If token is invalid, process the synchronization error.
				if (!isTokenValid(request)) {
					if (log.isDebugEnabled()) {
						log.debug("This token was not valid");
					}
					return mapping.findForward("Dobbeltklikk");
				}

				// Generate a new token for the next request
				super.saveToken(request);

				// Reset session attributes.
				session.removeAttribute(FORM_KEY);
				session.removeAttribute(EXCEPTION_KEY);
				session.removeAttribute(FORWARD_KEY);
				session.removeAttribute(ERRORS_KEY);

				// Store the current token in the session.
				session.setAttribute(CURRENT_TOKEN_KEY, requestToken);

				try {
					// Execute synchronized action.
					ActionForward forward = dispatchMethod(mapping, form, request, response, method);

					// Keep the form resulting from the action.
					session.setAttribute(FORM_KEY, form);

					// Keep the forward resulting from the action.
					session.setAttribute(FORWARD_KEY, forward);

					// Keep the errors resulting from the action.
					session.setAttribute(ERRORS_KEY, request.getAttribute(Globals.ERROR_KEY));

					return forward;
				} catch (Exception e) {
					// Keep the exceptions resulting from the action and rethrow them.
					session.setAttribute(EXCEPTION_KEY, e);
					throw e;
				}
			}
		}
	}

	/**
	 * Surrounds the original method with debug statements for logging start, end and failure of method call.
	 * 
	 * {@inheritDoc}
	 */
	protected ActionForward dispatchMethod(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response,
		String name)
		throws Exception {

		if (null == serviceLevelMBean) {
			serviceLevelMBean =
				ServiceLevelMBeanFactory.getMBean(
					ServiceLevelConstants.SERVICE_TYPE_PRESENTATION,
					RequestContext.getModuleId() + "-" + RequestContext.getProcessId());
		}

		long start = System.currentTimeMillis();
		try {
			ActionForward forward = super.dispatchMethod(mapping, form, request, response, name);
			serviceLevelMBean.add(System.currentTimeMillis() - start, null);
			return forward;
		} catch (Exception e) {
			serviceLevelMBean.add(System.currentTimeMillis() - start, e);
			throw e;
		}
	}

	/**
	 * Recover the results of the synchronized action.
	 * 
	 * @param mapping The ActionMapping used to select this instance
	 * @param actionForm The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * 
	 * @return An ActionForward instance describing where and how control should be forwarded
	 *  
	 * @exception Exception if the application business logic throws
	 *  an exception
	 */
	protected ActionForward recover(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		if (log.isDebugEnabled()) {
			log.debug("This token is about to be recovered");
		}

		HttpSession session = request.getSession();

		// Recover the exception if any and rethrow it.
		Exception e = (Exception) session.getAttribute(EXCEPTION_KEY);
		if (null != e) {
			throw e;
		}

		// Recover the form.
		ActionForm form = (ActionForm) session.getAttribute(FORM_KEY);

		// Put back the form in the appropriate context.
		if ("request".equals(mapping.getScope())) {
			request.setAttribute(mapping.getAttribute(), form);
		} else {
			// Put back a copy to protect the form from being repopulated
			// by upcoming requests.
			// Note: This might be avoided in future releases of Struts if a flag
			// could be set to prevent auto-population under certain circumstances.
			if (null != form) {
				session.setAttribute(mapping.getAttribute(), BeanUtils.cloneBean(form));
			}
		}

		// Recover and save the errors.
		saveErrors(request, (ActionErrors) session.getAttribute(ERRORS_KEY));

		// Recover and return the forward.
		return (ActionForward) session.getAttribute(FORWARD_KEY);
	}

	/**
	 * Retrieves the action errors stored on the request, or creates new if non currently stored.
	 * 
	 * @param request the http request currently being processed.
	 * @return the action errors.
	 */
	protected ActionErrors getErrors(HttpServletRequest request) {
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (null == errors) {
			return new ActionErrors();
		} else {
			return errors;
		}
	}

	/**
	 * Method for checking whether any ActionErrors have been added.
	 * 
	 * @param request the http request currently being processed.
	 * @return true if there exist action errors, false otherwise.
	 */
	protected boolean isActionErrors(HttpServletRequest request) {
		return 0 < getErrors(request).size();
	}

	/**
	 * Convenient method for adding an action error with no replacement values.
	 * 
	 * @param request the http request currently being processed.
	 * @param key message key for this error message.
	 */
	protected void addError(HttpServletRequest request, String key) {
		ActionErrors errors = getErrors(request);
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key));
		request.setAttribute(Globals.ERROR_KEY, errors);
	}

	/**
	 * Convenient method for adding an action error with the specified replacement value.
	 * 
	 * @param request the http request currently being processed.
	 * @param key message key for this error message.
	 * @param value first and only replacement value.
	 */
	protected void addError(HttpServletRequest request, String key, Object value) {
		ActionErrors errors = getErrors(request);
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key, value));
		request.setAttribute(Globals.ERROR_KEY, errors);
	}

	/**
	 * Convenient method for adding an action error with the specified replacement values.
	 * 
	 * @param request the http request currently being processed.
	 * @param key message key for this error message.
	 * @param values the replacement values.
	 */
	protected void addError(HttpServletRequest request, String key, Object[] values) {
		ActionErrors errors = getErrors(request);
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key, values));
		request.setAttribute(Globals.ERROR_KEY, errors);
	}

	/**
	 * Convenient method for adding an action error with no replacement values and returning to the action's input.
	 * 
	 * @param mapping the ActionMapping used to select this instance.
	 * @param request the http request currently being processed.
	 * @param key message key for this error message.
	 * @return the ActionForward instance forwarding control to the action's input.
	 */
	protected ActionForward addErrorAndForwardToInput(ActionMapping mapping, HttpServletRequest request, String key) {
		addError(request, key);
		return mapping.getInputForward();
	}

	/**
	 * Convenient method for adding an action error with the specified replacement value and returning to the action's input.
	 * 
	 * @param mapping the ActionMapping used to select this instance.
	 * @param request the http request currently being processed.
	 * @param key message key for this error message.
	 * @param value first and only replacement value.
	 * @return the ActionForward instance forwarding control to the action's input.
	 */
	protected ActionForward addErrorAndForwardToInput(
		ActionMapping mapping,
		HttpServletRequest request,
		String key,
		Object value) {
		addError(request, key, value);
		return mapping.getInputForward();
	}

	/**
	 * Convenient method for adding an action error with the specified replacement values and returning to the action's input.
	 * 
	 * @param mapping the ActionMapping used to select this instance.
	 * @param request the http request currently being processed.
	 * @param key message key for this error message.
	 * @param values the replacement values.
	 * @return the ActionForward instance forwarding control to the action's input.
	 */
	protected ActionForward addErrorAndForwardToInput(
		ActionMapping mapping,
		HttpServletRequest request,
		String key,
		Object[] values) {
		addError(request, key, values);
		return mapping.getInputForward();
	}

	/**
	 * Retrieves the action messages stored on the request, or creates new if non currently stored.
	 * 
	 * @param request the http request currently being processed.
	 * @return the action messages.
	 */
	protected ActionMessages getMessages(HttpServletRequest request) {
		ActionMessages msgs = (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
		if (null == msgs) {
			return new ActionMessages();
		} else {
			return msgs;
		}
	}

	/**
	 * Method for checking whether any ActionMessages have been added.
	 *
	 * @param request the http request currently being processed.
	 * @return true if there exist action messages, false otherwise.
	 */
	protected boolean isActionMessages(HttpServletRequest request) {
		return 0 < getMessages(request).size();
	}

	/**
	 * Convenient method for adding an action message with no replacement values.
	 * 
	 * @param request the http request currently being processed.
	 * @param key message key for this message.
	 */
	protected void addMessage(HttpServletRequest request, String key) {
		ActionMessages msgs = getMessages(request);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key));
		request.setAttribute(Globals.MESSAGE_KEY, msgs);
	}

	/**
	 * Convenient method for adding an action message with the specified replacement value.
	 * 
	 * @param request the http request currently being processed.
	 * @param key message key for this message.
	 * @param value first and only replacement value.
	 */
	protected void addMessage(HttpServletRequest request, String key, Object value) {
		ActionMessages msgs = getMessages(request);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, value));
		request.setAttribute(Globals.MESSAGE_KEY, msgs);
	}

	/**
	 * Convenient method for adding an action message with the specified replacement values.
	 * 
	 * @param request the http request currently being processed.
	 * @param key message key for this message.
	 * @param values the replacement values.
	 */
	protected void addMessage(HttpServletRequest request, String key, Object[] values) {
		ActionMessages msgs = getMessages(request);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, values));
		request.setAttribute(Globals.MESSAGE_KEY, msgs);
	}

	/**
	 * Forwards to a pre-defined mapping. Name is sent as request parameter named "to".
	 * Usage: <code>http://myserver.com/ThisPage.do?executeForward=now&to=NextPage</code>
	 * 
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 * 
	 * @return An ActionForward instance describing where and how control should be forwarded
	 *  
	 * @exception Exception if the application business logic throws
	 *  an exception
	 */
	public ActionForward executeForward(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		// The named destination must be sent as request parameter
		String name = request.getParameter("to");
		if (null == name) {
			if (log.isWarnEnabled()) {
				log.warn("Can't forward because request.getParameter(\"to\") returned null.");
			}
			response.sendError(
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				"Can't redirect without the name of the destination mapping. Request parameter named 'to' is missing.");
		}
		ActionForward forward = mapping.findForward(name);
		if (null == forward) {
			if (log.isWarnEnabled()) {
				log.warn("Can't forward because mapping.findForward(" + name + ") returned null.");
			}
			response.sendError(
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				"Can't redirect because struts-config.xml doesn't contain a forward named '" + name + "'.");
		}
		return forward;
	}
}
