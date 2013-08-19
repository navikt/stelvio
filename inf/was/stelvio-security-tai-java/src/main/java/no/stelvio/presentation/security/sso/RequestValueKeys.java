package no.stelvio.presentation.security.sso;

public class RequestValueKeys {

    private String accessManagerUserKey;
    private String originalUserNameKey;
    private String authenticationLevelKey;
    private String authorizedAsKey;
    private String authorizationTypeKey;
    private String cookieKey;

    /**
     * @return the authorizedAsKey
     */
    public String getAuthorizedAsKey() {
        return authorizedAsKey;
    }

    /**
     * @param authorizedAsKey
     *            the authorizedAsKey to set
     */
    public void setAuthorizedAsKey(String authorizedAsKey) {
        this.authorizedAsKey = authorizedAsKey;
    }

    /**
     * @return the accessManagerUserKey
     */
    public String getAccessManagerUserKey() {
        return accessManagerUserKey;
    }

    /**
     * @param accessManagerUserKey
     *            the accessManagerUserKey to set
     */
    public void setAccessManagerUserKey(String accessManagerUserKey) {
        this.accessManagerUserKey = accessManagerUserKey;
    }

    /**
     * @return the authenticationLevelKey
     */
    public String getAuthenticationLevelKey() {
        return authenticationLevelKey;
    }

    /**
     * @param authenticationLevelKey
     *            the authenticationLevelKey to set
     */
    public void setAuthenticationLevelKey(String authenticationLevelKey) {
        this.authenticationLevelKey = authenticationLevelKey;
    }

    /**
     * @return the autorizationTypeKey
     */
    public String getAuthorizationTypeKey() {
        return authorizationTypeKey;
    }

    /**
     * @param autorizationTypeKey
     *            the autorizationTypeKey to set
     */
    public void setAuthorizationTypeKey(String authorizationTypeKey) {
        this.authorizationTypeKey = authorizationTypeKey;
    }

    /**
     * @return the originalUserNameKey
     */
    public String getOriginalUserNameKey() {
        return originalUserNameKey;
    }

    /**
     * @param originalUserNameKey
     *            the originalUserNameKey to set
     */
    public void setOriginalUserNameKey(String originalUserNameKey) {
        this.originalUserNameKey = originalUserNameKey;
    }

    /**
     * @return the cookieKey
     */
    public String getCookieKey() {
        return cookieKey;
    }

    /**
     * @param cookieKey
     *            the cookieKey to set
     */
    public void setCookieKey(String cookieKey) {
        this.cookieKey = cookieKey;
    }

}
