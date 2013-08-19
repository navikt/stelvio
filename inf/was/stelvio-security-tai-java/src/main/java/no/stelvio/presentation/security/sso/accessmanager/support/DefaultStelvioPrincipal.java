package no.stelvio.presentation.security.sso.accessmanager.support;

import java.util.List;

import no.stelvio.presentation.security.sso.accessmanager.StelvioPrincipal;

/**
 * An implementation of a StelvioPrincipal containing only userid and authentication level for a userprincipal.
 * 
 * @author persondab2f89862d3
 * @see StelvioPrincipal
 */
public class DefaultStelvioPrincipal implements StelvioPrincipal {

    private String userId;
    private String authorizedAs;
    private List<String> groupIds;

    /**
     * Constructor which creates a principal with the supplied userid and authentication level.
     * 
     * @param userId
     *            the userid of the principal
     * @param authLevel
     *            the authentication level for the principal
     */
    public DefaultStelvioPrincipal(String userId, String authorizedAs, List<String> groupIds) {
        this.userId = userId;
        this.authorizedAs = authorizedAs;
        this.groupIds = groupIds;
    }

    public String getAuthorizedAs() {
        return this.authorizedAs;
    }

    public List<String> getGroupIds() {
        // TODO Auto-generated method stub
        return this.groupIds;
    }

    /**
     * Gets the userid.
     * 
     * @return the userid of the principal.
     */
    public String getUserId() {
        return this.userId;
    }

}
