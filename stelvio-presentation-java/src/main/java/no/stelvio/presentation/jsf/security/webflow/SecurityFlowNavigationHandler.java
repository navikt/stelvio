package no.stelvio.presentation.jsf.security.webflow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import no.stelvio.presentation.security.page.PageAuthenticationRequest;
import no.stelvio.presentation.security.page.PageAuthenticationRequiredException;
import no.stelvio.presentation.security.page.constants.Constants;
import no.stelvio.presentation.security.page.definition.JeeSecurityObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageResolver;
import org.springframework.binding.message.StateManageableMessageContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

/**
 * A SecurityNavigationHandler which extends the SWF FlowNavigationHandler. Its purpose is to check whether the first view
 * connected to a flow requires authentication before the flow starts executing. If this is the case the user will be forced to
 * authenticate. If a view does not require authentication or if the user already is authenticated, the FlowNavigationHandler is
 * allowed to resume control of the navigation.
 * 
 * @version $Id$
 */
public class SecurityFlowNavigationHandler extends FlowExecutionListenerAdapter {

	private static final String LOGIN = "login";

	private static final String DEAFULT_INVALID_LOGIN = "Login failed";

	private static final String DEFAULT_MESSAGE_SOURCE = "messageSource";

	private String loginFailed;

	private JeeSecurityObject securityObject;

	private static final Log log = LogFactory.getLog(SecurityFlowNavigationHandler.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.webflow.execution.FlowExecutionListenerAdapter#requestSubmitted(org.springframework.webflow.execution.RequestContext)
	 */
	@Override
	public void requestSubmitted(RequestContext context) {

		ParameterMap map = context.getRequestParameters();

		if (map.contains(LOGIN)) {
			StateManageableMessageContext mcontext = (StateManageableMessageContext) context.getMessageContext();

			ApplicationContext appCtx = context.getFlowExecutionContext().getDefinition().getApplicationContext();

			if (appCtx.containsBean(DEFAULT_MESSAGE_SOURCE)) {
				MessageSource messages = (MessageSource) appCtx.getBean(DEFAULT_MESSAGE_SOURCE, MessageSource.class);
				mcontext.setMessageSource(messages);
			}

			MessageResolver resolver = new MessageBuilder().error().code(loginFailed).defaultText(DEAFULT_INVALID_LOGIN)
					.build();
			mcontext.addMessage(resolver);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void sessionStarting(RequestContext context, FlowSession session, MutableAttributeMap input) {
		authorizePageAccess((HttpServletRequest) context.getExternalContext().getNativeRequest());
	}

	/**
	 * Checks if the outcome in form of a requested page is protected and and makes an access-decision.
	 * 
	 * @param req
	 *            the request
	 * @return true if the requested page does not requires authentication, false otherwise.
	 */
	private boolean authorizePageAccess(HttpServletRequest req) {

		if (securityObject == null) {
			securityObject = new JeeSecurityObject();
			securityObject.initializeSecurityDefinitions(req);
		}

		String flowId = null;

		if (RequestContextHolder.getRequestContext().getFlowExecutionContext().isActive()) {
			flowId = RequestContextHolder.getRequestContext().getActiveFlow().getId();
		}

		if (flowId != null) {
			HttpSession session = req.getSession();

			try {
				return securityObject.authorizePageAccess(flowId, session, req);
			} catch (PageAuthenticationRequiredException e) {
				if (log.isDebugEnabled()) {
					log.debug("A PageAuthenticationRequiredException has been caught - redirect "
							+ "should have been sent to login. No further actions will be taken.");
				}

				PageAuthenticationRequest auth = e.getAuthenticationRequest();
				String page = auth.getUrlUponSuccessfulAuthentication();
				session.setAttribute(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION, page);
				auth.setUrlUponSuccessfulAuthentication(page);
				throw new PageAuthenticationRequiredException(auth, "Not authenticated to view page with id '" + flowId + "'");
			}
		}

		return true;
	}

	/**
	 * Set login failed.
	 * 
	 * @param loginFailed login failed
	 */
	public void setLoginFailed(String loginFailed) {
		this.loginFailed = loginFailed;
	}
}
