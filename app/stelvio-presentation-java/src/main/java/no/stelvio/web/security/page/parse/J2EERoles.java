package no.stelvio.web.security.page.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a list of J2EE roles required for a page authorization.
 * 
 * @author persondab2f89862d3
 * @version $Id$
 */
 public class J2EERoles {
    
    private final String J2EE_ROLE_CONCATENATION_OR = "OR";
    private final String J2EE_ROLE_CONCATENATION_AND = "AND";
    
    private List<J2EERole> roles = null;
    private String roleConcatinationType = this.J2EE_ROLE_CONCATENATION_OR;
    
    public J2EERoles() {
    }
    
    /**
     * Add a role to list.
     * @param role the role to add.
     */
    public void addRole(J2EERole role) {
        if (this.roles == null){
            this.roles = new ArrayList<J2EERole>();
        }
        //add role name to list of roles
        this.roles.add(role);
    }

    /**
     * Set a list of roles.
     * @param roles the list of roles to set.
     */
    public void setRoles(List<J2EERole> roles) {
        this.roles = roles;
    }
    /**
     * Get the list of roles.
     * @return the role list.
     */
    public List<J2EERole> getRoles() {
        return this.roles;
    }

    /**
     * Sets how the roles should be concatenated, i.e. using either AND or OR.
     * @param roleConcatenation 
     * <br>
     * Values are: 
     * 
     * <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_OR</code>, 
     * <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_AND</code>
     */
    public void setRoleConcatenationType(String roleConcatenation) {
        if (roleConcatenation.equalsIgnoreCase(this.J2EE_ROLE_CONCATENATION_AND)){
            this.roleConcatinationType = this.J2EE_ROLE_CONCATENATION_AND;
        }
        else{
            this.roleConcatinationType = this.J2EE_ROLE_CONCATENATION_OR;
        }
    }

    /**
     * Returns OR or AND based on how J2EE roles should be concatinated
     * for security evaluation
     * @return "OR" or "AND"
     */
    public String getRoleConcatenationType() {
        return this.roleConcatinationType;
    }
    
    /**
     * Returns string representation of this class.
     */
    @Override
	public String toString(){
    	StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append(":").append(this.roles);
		return sb.toString();
    }
    
}
