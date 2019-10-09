package no.stelvio.presentation.security.page.error.support;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.presentation.security.page.PageAccessDeniedException;
import no.stelvio.presentation.security.page.definition.parse.support.JeeRole;
import no.stelvio.presentation.security.page.definition.parse.support.JeeRoles;
import no.stelvio.presentation.security.page.definition.parse.support.JsfPage;
import no.stelvio.presentation.security.session.SecuritySessionAttribute;

/**
 * Authentication level handler.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AuthenticationLevelHandler {

	private final Log logger = LogFactory.getLog(this.getClass());

	private Properties authenticationLevels;
	private static final int NO_AUTHENTICATION_LEVEL_REQUIRED = -1;
	private String errorPage;

	/**
	 * Get authenticationLevels.
	 * 
	 * @return authenticationLevels
	 */
	public Properties getAuthenticationLevels() {
		return authenticationLevels;
	}

	/**
	 * Set authenticationLevels.
	 * 
	 * @param authenticationLevels
	 *            authenticationLevels
	 */
	public void setAuthenticationLevels(Properties authenticationLevels) {
		this.authenticationLevels = authenticationLevels;
	}

	/**
	 * Get required page authentication level.
	 * 
	 * @param page
	 *            page
	 * @return authentication level
	 */
	public int getRequiredPageAuthenticationLevel(JsfPage page) {
		int level = NO_AUTHENTICATION_LEVEL_REQUIRED;
		List<JeeRoles> roleSets = page.getRoleSets();
		for (JeeRoles jeeroles : roleSets) {

			if (jeeroles != null) {
				List<JeeRole> roles = jeeroles.getRoles();
				for (JeeRole role : roles) {
					level = getLevel(role.getRole());
					if (level != NO_AUTHENTICATION_LEVEL_REQUIRED) {
						return level;
					}
				}
			}
		}
		return level;
	}

	/**
	 * Get authentication level.
	 * 
	 * @param role
	 *            role
	 * @return level
	 */
	private int getLevel(String role) {
		int endIndex = authenticationLevels.size();

		for (int i = endIndex; i > 0; i--) {
			String levelRole = authenticationLevels.getProperty(i + "");
			if (role.equalsIgnoreCase(levelRole)) {
				return i;
			}
		}

		return NO_AUTHENTICATION_LEVEL_REQUIRED;
	}

	/**
	 * Handle step up.
	 * 
	 * @param t
	 *            throwable
	 * @return true if stepped up
	 * @throws IOException
	 *             io exception
	 */
	public boolean handleStepUp(Throwable t) throws IOException {
		if (t instanceof PageAccessDeniedException) {
			PageAccessDeniedException e = (PageAccessDeniedException) t;
			JsfPage page = e.getSecureJSFPage();
			int level = getRequiredPageAuthenticationLevel(page);
			boolean stepup = (level != NO_AUTHENTICATION_LEVEL_REQUIRED);
			if (stepup) {
				doStepUpAuthentication(page, level);
			}
			return stepup;
		}

		return false;
	}

	/**
	 * Do step up authetication.
	 * 
	 * @param page
	 *            page
	 * @param requiredLevel
	 *            required level
	 * @throws IOException
	 *             io exception
	 */
	private void doStepUpAuthentication(JsfPage page, int requiredLevel) throws IOException {

		ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();

		ViewHandler vh = FacesContext.getCurrentInstance().getApplication().getViewHandler();

		HttpServletRequest request = ((HttpServletRequest) exctx.getRequest());

		String params = request.getQueryString() != null ? "?" + request.getQueryString() : "";

		String originPageURI = vh.getActionURL(FacesContext.getCurrentInstance(), page.getPageName()) + params;

		HttpSession session = request.getSession();

		AuthenticationLevelChangeRequest levelChangeRequest = new AuthenticationLevelChangeRequest();
		levelChangeRequest.setInitiateChangeUrl(this.errorPage);
		levelChangeRequest.setRequestedPageUrl(originPageURI);
		levelChangeRequest.setAbsoluteRequestedPageUrl(generateAbsoluteUrl(request, originPageURI));
		levelChangeRequest.setRequiredAuthenticationLevel(requiredLevel);
		levelChangeRequest.setRequiredAuthenticationLevelName(authenticationLevels.getProperty(requiredLevel + ""));

		session.setAttribute(SecuritySessionAttribute.AUTHENTICATION_LEVEL_CHANGE_REQUEST.getName(), levelChangeRequest);

		if (logger.isDebugEnabled()) {
			logger.debug("The page, " + originPageURI + ", requires authentication level '"
					+ authenticationLevels.getProperty(requiredLevel + "") + "', a redirect is now sent to the errorpage - "
					+ this.errorPage);
		}

		exctx.redirect(this.errorPage);
	}

	/**
	 * Generate absolute url.
	 * 
	 * @param request
	 *            request
	 * @param requestedPage
	 *            requested page
	 * @return url
	 */
	public String generateAbsoluteUrl(HttpServletRequest request, String requestedPage) {

		String header = request.getHeader("Referer");
		if (header != null) {
			// Hostname is in index 2, contextpath or junction in index 3
			String[] elements = header.split("/");

			String protocol = elements[0] + "//";
			String host = elements[2];
			StringBuffer url = new StringBuffer(protocol + host);

			if (elements.length > 3) {
				String context = elements[3];
				if (!requestedPage.startsWith("/" + context)) {
					url.append("/").append(context).append(requestedPage);
				} else {
					url.append(requestedPage);
				}
			} else {
				url.append(requestedPage);
			}

			return url.toString();
		}
		return requestedPage;
	}

	/**
	 * Get errorPage.
	 * 
	 * @return errorPage
	 */
	public String getErrorPage() {
		return errorPage;
	}

	/**
	 * Set errorPage.
	 * 
	 * @param errorPage
	 *            errorPage
	 */
	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
}