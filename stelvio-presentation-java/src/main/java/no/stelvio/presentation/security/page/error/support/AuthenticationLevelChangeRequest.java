package no.stelvio.presentation.security.page.error.support;

import java.io.Serializable;

/**
 * Authentication level change request.
 * 
 * @version $Id$
 */
public class AuthenticationLevelChangeRequest implements Serializable {

	private static final long serialVersionUID = -8942789508761029108L;
	private String requiredAuthenticationLevelName;
	private int requiredAuthenticationLevel;
	private String requestedPageUrl;
	private String initiateChangeUrl;
	private String absoluteRequestedPageUrl;

	/**
	 * Creates a new instance of AuthenticationLevelChangeRequest.
	 * 
	 */
	public AuthenticationLevelChangeRequest() {
	}

	/**
	 * Get initiateChangeUrl.
	 * 
	 * @return initiateChangeUrl
	 */
	public String getInitiateChangeUrl() {
		return initiateChangeUrl;
	}

	/**
	 * Set initiateChangeUrl.
	 * 
	 * @param initiateChangeUrl
	 *            initiateChangeUrl
	 */
	public void setInitiateChangeUrl(String initiateChangeUrl) {
		this.initiateChangeUrl = initiateChangeUrl;
	}

	/**
	 * Get requestedPageUrl.
	 * 
	 * @return requestedPageUrl
	 */
	public String getRequestedPageUrl() {
		return requestedPageUrl;
	}

	/**
	 * Set requestedPageUrl.
	 * 
	 * @param requestedPageUrl
	 *            requestedPageUrl
	 */
	public void setRequestedPageUrl(String requestedPageUrl) {
		this.requestedPageUrl = requestedPageUrl;
	}

	/**
	 * Get requiredAuthenticationLevel.
	 * 
	 * @return requiredAuthenticationLevel
	 */
	public int getRequiredAuthenticationLevel() {
		return requiredAuthenticationLevel;
	}

	/**
	 * Set requiredAuthenticationLevel.
	 * 
	 * @param requiredAuthenticationLevel
	 *            requiredAuthenticationLevel
	 */
	public void setRequiredAuthenticationLevel(int requiredAuthenticationLevel) {
		this.requiredAuthenticationLevel = requiredAuthenticationLevel;
	}

	/**
	 * Get requiredAuthenticationLevelName.
	 * 
	 * @return requiredAuthenticationLevelName
	 */
	public String getRequiredAuthenticationLevelName() {
		return requiredAuthenticationLevelName;
	}

	/**
	 * Set requiredAuthenticationLevelName.
	 * 
	 * @param requiredAuthenticationLevelName
	 *            requiredAuthenticationLevelName
	 */
	public void setRequiredAuthenticationLevelName(String requiredAuthenticationLevelName) {
		this.requiredAuthenticationLevelName = requiredAuthenticationLevelName;
	}

	/**
	 * Get absoluteRequestedPageUrl.
	 * 
	 * @return absoluteRequestedPageUrl
	 */
	public String getAbsoluteRequestedPageUrl() {
		return absoluteRequestedPageUrl;
	}

	/**
	 * Set absoluteRequestedPageUrl.
	 * 
	 * @param absoluteRequestedPageUrl
	 *            absoluteRequestedPageUrl
	 */
	public void setAbsoluteRequestedPageUrl(String absoluteRequestedPageUrl) {
		this.absoluteRequestedPageUrl = absoluteRequestedPageUrl;
	}
}
