package no.stelvio.web.security.page.parse;


import no.stelvio.web.security.page.constants.Constants;

/**
 * 
 * Object representing one JSF page. Holds the parsed page security definitions 
 * obtained from <code>SecurityConfig</code>.
 *  
 * @author persondab2f89862d3
 * @version $Id$
 */
public class JSFPage {
    
    private String pageName = null;
    private boolean requiresAuthentication = false;
    private boolean requiresAuthorization = false;
    private boolean requiresSSL = false;
    private String concatinationRule = Constants.J2EE_ROLE_CONCATINATION_OR;
    private J2EERoles roles;
    
    public JSFPage() {
    }
    
    /**
     * Sets the page to require authentication with boolean parameter.
     * @param requiresAuthentication wheter or not the page requires authentication.
     */
    public void setRequiresAuthentication(boolean requiresAuthentication) {
        this.requiresAuthentication = requiresAuthentication;
    }
    /**
     * Sets the page to require authentication with string parameter.
     * @param requiresAuthentication wheter or not the page requires authentication. 
     * Should be "true" or "false".
     */
    public void setRequiresAuthentication(String requiresAuthentication) {
        this.requiresAuthentication = new Boolean(requiresAuthentication).booleanValue();
    }
    /**
     * Checks if page requires authentication.
     * @return <code>true</code> if page requires authentication, <code>false</code> otherwise.
     */
    public boolean requiresAuthentication() {
        return this.requiresAuthentication;
    }
    /**
     * Sets the page to require authorization with boolean parameter. If set to <code>true</code>, 
     * the page will also be set to require authentication.
     * @param requiresAuthorization wheter or not the page requires authorization.
     */
    public void setRequiresAuthorization(boolean requiresAuthorization) {
        this.requiresAuthorization = requiresAuthorization;
        //authorization is based on authentication
        if(this.requiresAuthorization){
        	setRequiresAuthentication(this.requiresAuthorization);
        }
    }
    /**
     * Sets the page to require authorization with string parameter. If set to "true", the page
     * will also be set to require authentication.
     * @param requiresAuthorization wheter or not the page requires authorization. 
     * Should be "true" or "false";
     */
    public void setRequiresAuthorization(String requiresAuthorization) {
        this.requiresAuthorization = new Boolean(requiresAuthorization).booleanValue();
        //authorization is based on authentication
        if(this.requiresAuthorization){
        	setRequiresAuthentication(this.requiresAuthorization);
        }
    }
    /**
     * Checks if page requires authorization.
     * @return <code>true</code> if page requires authorization, <code>false</code> otherwise.
     */
    public boolean requiresAuthorization() {
        return this.requiresAuthorization;
    }
    /**
     * Sets the page to require SSL with string parameter.
     * @param requiresSSL wheter or not the page requires SSL. Should be "true" or "false".
     */
    public void setRequiresSSL(String requiresSSL) {
        this.requiresSSL = new Boolean(requiresSSL).booleanValue();
    }
    /**
     * Checks if page requires SSL.
     * @return <code>true</code> if page requires SSL, <code>false</code> otherwise.
     */
    public boolean requiresSSL() {
        return this.requiresSSL;
    }
    /**
     * Adds a list of J2EE roles required by this page.
     * @param roles - List of J2EE roles required by the page authorization
     */
    public void addRoles(J2EERoles roles) {
        this.roles = roles;
    }

    /**
     * Gets the list of J2EE roles associated with this page.
     * @return List of J2EE roles required by the page authorization
     */
    public J2EERoles getRoles() {
        if (this.roles == null){
            this.roles = new J2EERoles();
        }
        return this.roles;
    }

    /**
     * Sets how the J2EE roles should be treated when evaluating this page, i.e. if it is required
     * to have all roles or just one to access the page.
     * @param concatinationRule Determines how J2EE roles are treated. 
     * <br>
     * Values are: 
     * 
     * <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_OR</code>, 
     * <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_AND</code>
     */
    public void setConcatinationRule(String concatinationRule) {
        this.concatinationRule = concatinationRule;
    }
    /**
     * Gets how the J2EE roles should be treated when evaluating this page, i.e. if it is required
     * to have all roles or just one to access the page.
     * @return Determines how J2EE roles are treated. Values are: 
     * <br>
     * Values are: 
     * 
     * <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_OR</code>, 
     * <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_AND</code>
     */
    public String getConcatinationRule() {
        return this.concatinationRule;
    }
    /**
     * Sets the name of the page.
     * @param page the page name.
     */
    public void setPageName(String page) {
        this.pageName=page;
    }
    /**
     * Gets the name of the page.
     * @return the page name.
     */
    public String getPageName() {
        return this.pageName;
    }
    /**
     * Returns all security constraints of this page as a string.
     */
    @Override
	public String toString(){
    	StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append(": Pagename=").append(this.pageName);
		sb.append(",Requires authentication=").append(this.requiresAuthentication);
		sb.append(",Requires authorization=").append(this.requiresAuthorization);
		sb.append(",Requires SSL=").append(this.requiresSSL);
		sb.append(",Role concatination rule=").append(this.concatinationRule);
		sb.append(",Roles=").append(this.roles);

        return sb.toString();
    }
    
}
