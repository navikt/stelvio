package no.stelvio.presentation.security.sso.support;

public class PrincipalRepresentation {

    private String accessManagerUser;
    private String originalUserName;
    private String authenticationLevel;
    private String authorizedAs;
    private String authorizationType;

    public PrincipalRepresentation(String accessManagerUser, String originalUserName,
            String authenticationLevel, String authorizedAs,
            String autorizationType) {
        this.accessManagerUser = accessManagerUser;
        this.originalUserName = originalUserName;
        this.authenticationLevel = authenticationLevel;
        this.authorizedAs = authorizedAs;
        this.authorizationType = autorizationType;
    }

    /**
     * @return the accessManagerUser
     */
    public String getAccessManagerUser() {
        return accessManagerUser;
    }

    /**
     * @return the authenticationLevel
     */
    public String getAuthenticationLevel() {
        return authenticationLevel;
    }

    /**
     * @return the authorizationType
     */
    public String getAuthorizationType() {
        return authorizationType;
    }

    /**
     * @return the authorizedAs
     */
    public String getAuthorizedAs() {
        return authorizedAs;
    }

    /**
     * @return the originalUserName
     */
    public String getOriginalUserName() {
        return originalUserName;
    }

}
