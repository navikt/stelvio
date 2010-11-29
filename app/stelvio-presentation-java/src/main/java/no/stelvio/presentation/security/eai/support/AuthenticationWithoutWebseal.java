package no.stelvio.presentation.security.eai.support;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import no.stelvio.presentation.security.eai.AuthenticationFailureException;
import no.stelvio.presentation.security.session.SecuritySessionAttribute;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AuthenticationWithoutWebseal.
 * 
 * @author persondab2f89862d3, Accenture
 * @see StepUpAuthentication
 */
public class AuthenticationWithoutWebseal extends StepUpAuthentication {

	private final Log LOGGER = LogFactory.getLog(this.getClass());
	private String originalUserIdAttrName = WebsealHeaderNames.ORIGINAL_USERID;
	// "original-user-id";
	private String authenticationLevelAttrName = WebsealHeaderNames.AUTHENTICATION_LEVEL;
	// "authentication-level";
	private String accessManagerUserAttrName = WebsealHeaderNames.ACCESS_MANAGER_USER;
	// "iv-user";
	private String securedUrlPattern = "/jsfauthentication";
	private String defaultUrlUponSuccessfulStepup = "/";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void authenticate() {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Authenticating the user by extracting headerdata and putting it in session.");
		}

		super.authenticate();
		HeaderData data = getHeaderData();
		HttpSession session = request.getSession();

		if (data != null) {

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Found headerdata. Redirecting to " + securedUrlPattern + "..");
			}

			Object tmp = session.getAttribute(SecuritySessionAttribute.JSFPAGE_TOGO_AFTER_AUTHENTICATION.getName());

			if (tmp == null) {
				String urlUponSuccessfulStepup = data.getUrlUponSuccessfulAuth();
				if (urlUponSuccessfulStepup != null) {
					session.setAttribute(SecuritySessionAttribute.JSFPAGE_TOGO_AFTER_AUTHENTICATION.getName(),
							urlUponSuccessfulStepup);
				} else {
					session.setAttribute(SecuritySessionAttribute.JSFPAGE_TOGO_AFTER_AUTHENTICATION.getName(),
							defaultUrlUponSuccessfulStepup);
				}
			}

			session.setAttribute(originalUserIdAttrName, data.getXattrOriginalUserId());
			session.setAttribute(authenticationLevelAttrName, data.getAuthLevel());
			session.setAttribute(accessManagerUserAttrName, data.getUserIdentity());

			try {
				response.sendRedirect(securedUrlPattern);
			} catch (IOException e) {
				throw new AuthenticationFailureException("Could not redirect to /jsfauthentication");
			}
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Could not find any headerdata.");
			}
		}
	}

	/**
	 * Get accessManagerUserAttrName.
	 * 
	 * @return accessManagerUserAttrName
	 */
	public String getAccessManagerUserAttrName() {
		return accessManagerUserAttrName;
	}

	/**
	 * Set accessManagerUserAttrName.
	 * 
	 * @param accessManagerUserAttrName accessManagerUserAttrName
	 */
	public void setAccessManagerUserAttrName(String accessManagerUserAttrName) {
		this.accessManagerUserAttrName = accessManagerUserAttrName;
	}

	/**
	 * Get accessManagerUserAttrName.
	 * 
	 * @return accessManagerUserAttrName
	 */
	public String getAuthenticationLevelAttrName() {
		return authenticationLevelAttrName;
	}

	/**
	 * Set accessManagerUserAttrName.
	 * 
	 * @param authenticationLevelAttrName accessManagerUserAttrName
	 */
	public void setAuthenticationLevelAttrName(String authenticationLevelAttrName) {
		this.authenticationLevelAttrName = authenticationLevelAttrName;
	}

	/**
	 * Get accessManagerUserAttrName.
	 * 
	 * @return accessManagerUserAttrName
	 */
	public String getOriginalUserIdAttrName() {
		return originalUserIdAttrName;
	}

	/**
	 * Set accessManagerUserAttrName.
	 * 
	 * @param originalUserIdAttrName accessManagerUserAttrName
	 */
	public void setOriginalUserIdAttrName(String originalUserIdAttrName) {
		this.originalUserIdAttrName = originalUserIdAttrName;
	}

	/**
	 * Get securedUrlPattern.
	 * 
	 * @return securedUrlPattern
	 */
	public String getSecuredUrlPattern() {
		return securedUrlPattern;
	}

	/**
	 * Set securedUrlPattern.
	 * 
	 * @param securedUrlPattern securedUrlPattern
	 */
	public void setSecuredUrlPattern(String securedUrlPattern) {
		this.securedUrlPattern = securedUrlPattern;
	}
}
