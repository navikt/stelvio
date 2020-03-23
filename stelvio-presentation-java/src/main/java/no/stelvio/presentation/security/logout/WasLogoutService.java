package no.stelvio.presentation.security.logout;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A LogoutService capable of logging a user out from a WebSphere Application Server.
 * <p>
 * The WasLogoutService builds an IBM specific URI to log the current user out from the WAS server, and specifies which response
 * page to use after a successful logout.
 * 
 * @version $Id$
 * */
public class WasLogoutService implements LogoutService {

	private static final Log LOG = LogFactory.getLog(WasLogoutService.class);
	/** Default logout action. */
	protected static final String DEFAULT_LOGOUT_ACTION = "/ibm_security_logout";
	/** Default logout exitpage param name. */
	protected static final String DEFAULT_LOGOUT_EXITPAGE_PARAM_NAME = "logoutExitPage";
	/** destination param name. */
	protected static final String DESTINATION_PARAM_NAME = "destination";

	private String logoutAction = DEFAULT_LOGOUT_ACTION;
	private String logoutExitPageParamName = DEFAULT_LOGOUT_EXITPAGE_PARAM_NAME;

	private String logoutHandlerUrl;
	private String defaultStartPage;

	private HttpServletRequest request;
	private HttpServletResponse response;

	@Override
	public void logout() throws IOException {
		logoutToUrl(null);
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logoutToUrl(null, request, response);
	}

	@Override
	public void logoutToUrl(String destinationUrl) throws IOException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Attempting to log out using the ExternalContext.");
		}
		this.setRequest(null);
		this.setResponse(null);
		String url = buildRedirectUrl(destinationUrl);
		this.getExternalContext().redirect(url);
	}

	@Override
	public void logoutToUrl(String destinationUrl, HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Attempting to log out using the HttpServletResponse.");
		}
		this.setRequest(request);
		this.setResponse(response);
		String url = buildRedirectUrl(destinationUrl);
		response.sendRedirect(url);
	}

	/**
	 * Build a redirect URL that will be used to perform a logout. The URL is constructed in the following way: <br>
	 * <code>[logoutAction]?[logoutExitPageParamName]=[destinationUrl | defaultStartPage]</code> <br>
	 * <br>
	 * <b>Example url:</b> <code>/ibm_security_logout?logoutExitPage=somepage.html</code>
	 * 
	 * @param destinationUrl
	 *            the URL the user should be redirected to after a successful logout.
	 * @return the URL used to log out from the current session.
	 */
	protected String buildRedirectUrl(String destinationUrl) {
		StringBuilder logout = new StringBuilder(logoutAction).append('?').append(logoutExitPageParamName).append('=');

		if (useLogoutHandler()) {
			logout.append(this.logoutHandlerUrl).append('?').append(DESTINATION_PARAM_NAME).append('=');
		}

		String destination = destinationUrl != null ? destinationUrl : defaultStartPage;
		logout.append(destination);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Building the logoutUrl : " + logout.toString());
		}

		return logout.toString();
	}

	/**
	 * Checks if the logoutHandler has a value, and therefore shall be used.
	 * 
	 * @return true if the logoutHandler shall be used, otherwise false
	 */
	protected boolean useLogoutHandler() {
		return logoutHandlerUrl != null;
	}

	/**
	 * Helper method that returns the ExternalContext for the current FacesContext instance.
	 * 
	 * @return the current ExternalContext
	 */
	protected ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	/**
	 * Helper method that returns the HttpServletRequest instance for the current FacesContext instance.
	 * 
	 * @return the current HttpServletRequest instance
	 */
	protected HttpServletRequest getRequest() {
		if (request != null) {
			return this.request;
		} else {
			ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
			return (HttpServletRequest) exctx.getRequest();
		}
	}

	/**
	 * Helper method that returns the HttpServletResponse instance of the current FacesContext instance.
	 * 
	 * @return the current HttpServletResponse instance
	 */
	protected HttpServletResponse getResponse() {
		if (response != null) {
			return this.response;
		} else {
			ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
			return (HttpServletResponse) exctx.getResponse();
		}

	}

	/**
	 * Return the default start page.
	 * 
	 * @return the default start page.
	 */
	public String getDefaultStartPage() {
		return defaultStartPage;
	}

	/**
	 * Set the default start page.
	 * 
	 * @param defaultStartPage
	 *            the start page
	 */
	public void setDefaultStartPage(String defaultStartPage) {
		this.defaultStartPage = defaultStartPage;
	}

	/**
	 * Return the logout action.
	 * 
	 * @return the logout action.
	 */
	public String getLogoutAction() {
		return logoutAction;
	}

	/**
	 * Set the logout action.
	 * 
	 * @param logoutAction
	 *            the logout action
	 */
	public void setLogoutAction(String logoutAction) {
		this.logoutAction = logoutAction;
	}

	/**
	 * Return the logout exitpage parameter name.
	 * 
	 * @return the logout exitpage parameter name.
	 */
	public String getLogoutExitPageParamName() {
		return logoutExitPageParamName;
	}

	/**
	 * Set the logout exit page parameter name.
	 * 
	 * @param logoutExitPageParamName
	 *            the logout exitpage parameter name.
	 */
	public void setLogoutExitPageParamName(String logoutExitPageParamName) {
		this.logoutExitPageParamName = logoutExitPageParamName;
	}

	/**
	 * Get the logout handler URL.
	 * 
	 * @return logoutHandlerUrl, the logout handler URL.
	 */
	public String getLogoutHandlerUrl() {
		return logoutHandlerUrl;
	}

	/**
	 * Set the logout handler URL.
	 * 
	 * @param logoutHandlerUrl
	 *            the logout handler URL.
	 */
	public void setLogoutHandlerUrl(String logoutHandlerUrl) {
		this.logoutHandlerUrl = logoutHandlerUrl;
	}

	/**
	 * Set the HttpServletRequest.
	 * 
	 * @param request
	 *            the HttpServletRequest.
	 */
	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Set the HttpServletResponse.
	 * 
	 * @param response
	 *            the HttpServletResponse.
	 */
	protected void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
