package no.stelvio.presentation.security.eai.support;

import no.stelvio.presentation.security.eai.headers.config.EaiHeaderConfig;
import no.stelvio.presentation.security.eai.headers.support.PacHeaders;
import no.stelvio.presentation.security.eai.headers.support.UserIdentityHeaders;

/**
 * A builder that builds PacHeaders and UserIdentityHeaders.
 * 
 * @version $Id$
 * @see EaiHeaderConfig
 * @see PacHeaders
 * @see UserIdentityHeaders
 * 
 */
public class HeaderData {

	private EaiHeaderConfig config;
	private String pac;
	private String pacServiceId;
	private String userIdentity;
	private String authLevel;
	private String xattrOriginalUserId;
	private String urlUponSuccessfulAuth;

	/**
	 * Creates header data for PacHeaders or UserIdentityHeaders.
	 * 
	 * @param config
	 *            the EaiHeaderConfig
	 */
	public HeaderData(EaiHeaderConfig config) {
		this.config = config;
	}

	/**
	 * Builds the pac headers.
	 * 
	 * @return the PacHeaders
	 */
	public PacHeaders buildPacHeaders() {
		PacHeaders headers = new PacHeaders(config);

		if (pac != null) {
			headers.setPac(pac);
		}
		if (pacServiceId != null) {
			headers.setPacServiceId(pacServiceId);
		}
		if (urlUponSuccessfulAuth != null) {
			headers.setEaiRedirUrlHeader(urlUponSuccessfulAuth);
		}

		return headers;
	}

	/**
	 * Builds the user identity headers.
	 * 
	 * @return the UserIdentityHeaders
	 */
	public UserIdentityHeaders buildUserIdentityHeaders() {
		UserIdentityHeaders headers = new UserIdentityHeaders(config);

		if (userIdentity != null) {
			headers.setUserIdentity(userIdentity);
		}
		if (authLevel != null) {
			headers.setAuthenticationLevel(authLevel);
		}
		if (xattrOriginalUserId != null) {
			headers.setOriginalUserId(xattrOriginalUserId);
		}
		if (urlUponSuccessfulAuth != null) {
			headers.setEaiRedirUrlHeader(urlUponSuccessfulAuth);
		}

		return headers;
	}

	/**
	 * Returns the pac.
	 * 
	 * @return the pac
	 */
	public String getPac() {
		return pac;
	}

	/**
	 * Sets the pac.
	 * 
	 * @param pac
	 *            the pac to set
	 */
	public void setPac(String pac) {
		this.pac = pac;
	}

	/**
	 * Returns the pac service id.
	 * 
	 * @return the pacServiceId
	 */
	public String getPacServiceId() {
		return pacServiceId;
	}

	/**
	 * Sets the pac service id.
	 * 
	 * @param pacServiceId
	 *            the pacServiceId to set
	 */
	public void setPacServiceId(String pacServiceId) {
		this.pacServiceId = pacServiceId;
	}

	/**
	 * Returns the url upon succesfull authentication.
	 * 
	 * @return the urlUponSuccessfulAuth
	 */
	public String getUrlUponSuccessfulAuth() {
		return urlUponSuccessfulAuth;
	}

	/**
	 * Sets the url to go to after a successful authentication.
	 * 
	 * @param urlUponSuccessfulAuth
	 *            the urlUponSuccessfulAuth to set
	 */
	public void setUrlUponSuccessfulAuth(String urlUponSuccessfulAuth) {
		this.urlUponSuccessfulAuth = urlUponSuccessfulAuth;
	}

	/**
	 * Returns ther useridentity.
	 * 
	 * @return the userIdentity
	 */
	public String getUserIdentity() {
		return userIdentity;
	}

	/**
	 * Sets the useridentity.
	 * 
	 * @param userIdentity
	 *            the userIdentity to set
	 */
	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}

	/**
	 * Returns the Eai header configuration.
	 * 
	 * @return the config
	 */
	public EaiHeaderConfig getConfig() {
		return config;
	}

	/**
	 * Returns the authentication level.
	 * 
	 * @return the authLevel
	 */
	public String getAuthLevel() {
		return authLevel;
	}

	/**
	 * Sets the authentication level.
	 * 
	 * @param authLevel
	 *            the authLevel to set
	 */
	public void setAuthLevel(String authLevel) {
		this.authLevel = authLevel;
	}

	/**
	 * Returns the extended attribute for the original user id.
	 * 
	 * @return the xattrOriginalUserId
	 */
	public String getXattrOriginalUserId() {
		return xattrOriginalUserId;
	}

	/**
	 * Sets the extended attribute for the original user id.
	 * 
	 * @param xattrOriginalUserId
	 *            the xattrOriginalUserId to set
	 */
	public void setXattrOriginalUserId(String xattrOriginalUserId) {
		this.xattrOriginalUserId = xattrOriginalUserId;
	}
}
