package no.stelvio.presentation.security.eai.support;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.InitializingBean;

import no.stelvio.presentation.security.eai.AuthenticationFailureException;
import no.stelvio.presentation.security.session.SecuritySessionAttribute;

/**
 * Authenticates a user when a step up is performed.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class StepUpAuthentication extends AbstractAuthentication implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		if (isNull(this.getConfig(), this.getEaiHeaderType(), this.getAccessManagerUser())) {

			throw new Exception("Please ensure that all of the following properties are set:"
					+ "[config, eaiHeaderType, accessManagerUser]");
		}
	}

	/**
	 * Checks if an object/objects is null.
	 * 
	 * @param property properties
	 * @return true if an object is null, otherwise return false.
	 */
	public boolean isNull(Object... property) {
		for (Object object : property) {
			if (object == null) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void authenticate() {

		HttpSession session = request.getSession();

		Object user = session.getAttribute(SecuritySessionAttribute.EAI_ORIGINAL_USER_ID.getName());
		Object level = session.getAttribute(SecuritySessionAttribute.EAI_AUTHENTICATION_LEVEL.getName());

		if (user != null && level != null) {
			String userId = (String) user;
			String authLevel = (String) level;

			HeaderData data = new HeaderData(this.getConfig());
			data.setUserIdentity(this.getAccessManagerUser());
			data.setXattrOriginalUserId(userId);
			data.setAuthLevel(authLevel);
			data.setUrlUponSuccessfulAuth(this.getUrlUponSuccessfulStepUp());
			super.setHeaderData(data);
			super.putEaiHeadersOnResponse();
		} else {
			throw new AuthenticationFailureException("Could not find userId and authentication level.");
		}
	}

	/**
	 * Private helper method.
	 * 
	 * @return the url to go to upon successful step up.
	 */
	private String getUrlUponSuccessfulStepUp() {

		Object value = request.getSession().getAttribute(SecuritySessionAttribute.EAI_URL_UPON_SUCCESSFUL_STEPUP.getName());

		if (value != null) {
			request.getSession().setAttribute(SecuritySessionAttribute.EAI_URL_UPON_SUCCESSFUL_STEPUP.getName(), null);
			return value.toString();
		}

		return null;
	}
}
