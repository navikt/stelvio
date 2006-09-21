package no.stelvio.web.framework.action;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.apache.struts.util.RequestUtils;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.context.RequestContext;
import no.stelvio.common.framework.error.ErrorHandler;
import no.stelvio.common.framework.error.SystemException;
import no.stelvio.web.framework.constants.Constants;

/**
 * This class Overrides the standard Struts <code>RequestProcessor</code> (for tiles) to make it possible to set the
 * <code>RequestContext</code> properties and to not run validation in specific cases.
 * 
 * @author Jonas Lindholm, Accenture
 * @author person7553f5959484, Accenture
 * @version $Id: FrameworkTilesRequestProcessor.java 2769 2006-02-16 22:17:15Z skb2930 $
 */
public class FrameworkTilesRequestProcessor extends TilesRequestProcessor {

	// To prevent populations of form after local forward
	private static final String IS_LOCAL_FORWARD = "IS_LOCAL_FORWARD";

	// The log
	private static final Log log = LogFactory.getLog(FrameworkTilesRequestProcessor.class);

	/**
	 * Does not run validation if the method to execute is WINDOW_LOAD or is specified in the action mapping's parameter
	 * property.
	 *  
	 * {@inheritDoc}
	 */
	protected boolean processValidate(
		HttpServletRequest request,
	    HttpServletResponse response,
	    ActionForm form,
	    ActionMapping mapping)
		throws IOException, ServletException {
		final String strutsParameter = mapping.getParameter();
		String method = no.stelvio.web.framework.util.RequestUtils.getMethod(request);

		if (no.stelvio.web.framework.util.RequestUtils.validate(strutsParameter, method)) {
			return super.processValidate(request, response, form, mapping);
		} else {
			return true;
		}
	}

	/**
	 * Form is not populated after a local forward, to prevent parameters 
	 * targeted for the first form to be automatically matched with the second form.
	 * This should fix defect # 240.
	 * 
	 * {@inheritDoc}
	 */
	protected void processPopulate(
		HttpServletRequest request,
	    HttpServletResponse response,
	    ActionForm form,
	    ActionMapping mapping)
		throws ServletException {

		// Fix defect # 240
		if (null == request.getAttribute(IS_LOCAL_FORWARD)) {
			try {
				super.processPopulate(request, response, form, mapping);
			} catch (Exception e) {
				throw (SystemException) ErrorHandler.handleError(
						new SystemException(FrameworkError.WEB_DOUBLE_CLICK, e));
			}

			request.setAttribute(IS_LOCAL_FORWARD, Boolean.TRUE);
		}

	}

	/**
	 * Method overrided to set <code>RequestContext</code> properties. Includes the functionality from both
	 * <code>TilesRequestProcessor</code> and <code>RequestProcessor</code>, as the properties need to be set before
	 * <i>doForward</i> is called.
	 *  
	 * {@inheritDoc}
	 */
	protected void processForwardConfig(HttpServletRequest request, HttpServletResponse response, ForwardConfig forward)
		throws IOException, ServletException {

		// Required by struts contract
		if (null == forward) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("processForwardConfig(" + forward.getPath() + ", " + forward.getContextRelative() + ")");
		}

		// Try to process the definition.
		if (processTilesDefinition(forward.getPath(), forward.getContextRelative(), request, response)) {
			if (log.isDebugEnabled()) {
				log.debug("  '" + forward.getPath() + "' - processed as definition");
			}
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("  '" + forward.getPath() + "' - processed as uri");
		}

		String forwardPath = forward.getPath();
		String uri;

		// paths not starting with / should be passed through without any processing
		// (ie. they're absolute)
		if (forwardPath.startsWith("/")) {
			uri = RequestUtils.forwardURL(request, forward); // get module relative uri
		} else {
			uri = forwardPath;
		}

		// NAV: Get the mode from the forward and set in the RequestContext and the session.
		// NAV: Only set if the property is set in struts-config.xml, i.e. the forward
		// NAV: is of type StateAwareActionForward.
		if (forward instanceof StateAwareActionForward) {

			if (null == ((StateAwareActionForward) forward).getState()) {
				throw new IllegalStateException(
					"State must be configured for <action path='/"
					+ RequestContext.getScreenId()
					+ "' ...> and  <forward name='"
					+ forward.getName()
					+ "' ...> in struts-config.xml! For details, see javadoc for "
					+ StateAwareActionForward.class.getName());
			} else {
				// ES: Removed from RequestContext
				//RequestContext.setState(((StateAwareActionForward) forward).getState());
			}
		} else {
			// ES: Removed from RequestContext
			//RequestContext.setState("normal");
		}

		// NAV: Remove FormBean from HttpSession unless forward is of type SessionScope
		if (!(forward instanceof SessionScope)) {
			ActionMapping mapping = getActionMapping(request);
			no.stelvio.web.framework.util.RequestUtils.removeSessionAttribute(request, mapping.getName());
			if (log.isDebugEnabled()) {
				log.debug("Removed " + mapping.getName() + " from session, now forwarding to " + uri);
			}
		}

		// NAV: Set next screen as module
		RequestContext.setModuleId(no.stelvio.web.framework.util.RequestUtils.getScreenId(uri));

		// NAV: Set default method in next screen
		if (null == request.getAttribute(Constants.METHOD_TO_EXECUTE)) {
			request.setAttribute(
				Constants.METHOD_TO_EXECUTE,
			    Constants.WINDOW_LOAD_METHOD);
		}

		try {
			if (forward.getRedirect()) {
				// only prepend context path for relative uri
				if (uri.startsWith("/")) {
					uri = request.getContextPath() + uri;
				}
				response.sendRedirect(response.encodeRedirectURL(uri));

			} else {
				doForward(uri, request, response);
			}
		} catch (ServletException e) {
			// To be sure we get more than the dreaded "[ServletException in: <page>] <some text>" in the view, the exception
			// with full stack trace is logged. This will make it easier to figure out what is happening. 
			log.warn("Error rendering view", e);
			throw e;
		}
	}

	/**
	 * Logs the exception thrown up to the web framework to the debug log as a stack trace.
	 *
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are processing
	 * @param exception The exception being handled
	 * @param form The ActionForm we are processing
	 * @param mapping The ActionMapping we are using
	 * @throws IOException if an input/output error occurs
	 * @throws ServletException if a servlet exception occurs
	 */
	protected ActionForward processException(HttpServletRequest request, HttpServletResponse response, Exception exception,
	                                         ActionForm form, ActionMapping mapping) throws IOException, ServletException {
		if (log.isDebugEnabled()) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);

			log.debug("Exception thrown up to web framework: " + sw);
		}

		return super.processException(request, response, exception, form, mapping);
	}

	/**
	 * Retrieves the action mapping from a request.
	 *
	 * @param request the request
	 * @return the action mapping
	 */
	private ActionMapping getActionMapping(HttpServletRequest request) {
		return (ActionMapping) request.getAttribute(Globals.MAPPING_KEY);
	}
}