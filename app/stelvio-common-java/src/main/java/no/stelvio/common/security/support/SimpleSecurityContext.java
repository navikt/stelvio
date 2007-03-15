package no.stelvio.common.security.support;

import java.util.List;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.definition.Role;
import no.stelvio.common.security.validation.RoleNotValidException;
import no.stelvio.common.security.validation.RoleValidator;


/**
 * <code>SecurityContext</code> implementation that simply takes the userId id and roles directly as constructor
 * parameters. An optional constructor also offers the possibility to validate the roles which are sent in as 
 * input parameters to the class' methods.
 *
 * @author personbf936f5cae20, Accenture
 * @author persondab2f89862d3, Accenture
 * @author personf8e9850ed756, Accenture
 * @version $Id: SimpleSecurityContext.java $ 
 * @see SecurityContext
 * @see RoleValidator
 */
public final class SimpleSecurityContext implements SecurityContext {
	
	private String userId;
	private List<String> roles;
	private RoleValidator roleValidator;
	/**
	 * Constructs an instance with the given user id and list of roles.
	 *
	 * @param userId the logged in user's id.
	 * @param roles the logged in user's roles.
	 */
	public SimpleSecurityContext(String userId, List<String> roles) {
		this.userId = userId;
		this.roles = roles;
	}
	/**
	 * Constructs an instance with the given user id, list of roles and roleValidator.
	 *
	 * @param userId the logged in user's id.
	 * @param roles the logged in user's roles.
	 * @param roleValidator the validator which will be used to check if a given rolename is valid.
	 */
	public SimpleSecurityContext(String userId, List<String> roles, RoleValidator roleValidator) {
		this.userId = userId;
		this.roles = roles;
		this.roleValidator = roleValidator;
	}
	/**
	 * Gets the RoleValidator which is used to validate the roles 
	 * that are sent in to the userInRole methods.
	 * @return the roleValidator
	 */
	public RoleValidator getRoleValidator(){
		return roleValidator;
	}
	
	/**
	 * Private helper method which uses the role validator to validate the supplied roleName parameter.
	 * 
	 * @param roleName the rolename to check.
	 * @throws RoleNotValidException if the role is found to be invalid.
	 */
	private void validateRole(String roleName){
		if(roleValidator != null ){
			if(!roleValidator.isValid(roleName)){
            	throw new RoleNotValidException(roleName, roleValidator.getValidRoles());
            }
    	}
	}
	/**
	 * {@inheritDoc}
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInRole(String role) {
		validateRole(role);
		List<String> roleList = getRoles();
		return (null != roleList) && roleList.contains(role);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInRole(Role role) {
		return isUserInRole(role.getRoleName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInAllRoles(List<String> roleList) {
		for (String roleparam : roleList) {
			if (!isUserInRole(roleparam)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInAllRoles(String... roleparams) {
		for (String roleparam : roleparams) {
			if (!isUserInRole(roleparam)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInAllRoles(Role... roleparams) {
		for (Role roleparam : roleparams) {
			if (!isUserInRole(roleparam)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInRoles(List<String> roleList) {
		for (String userRole : roleList) {
			if(isUserInRole(userRole)){
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInRoles(String... roleparams) {
			for (String roleparam : roleparams) {
				if (isUserInRole(roleparam)) {
					return true;
				}
			}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInRoles(Role... roleparams) {
			for (Role roleparam : roleparams) {
				if (isUserInRole(roleparam)) {
					return true;
				}
			}
		return false;
	}
	
	
}
