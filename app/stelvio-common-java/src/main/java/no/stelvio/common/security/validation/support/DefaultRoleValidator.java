package no.stelvio.common.security.validation.support;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.security.definition.Role;
import no.stelvio.common.security.validation.RoleValidator;
import no.stelvio.common.security.validation.ValidRole;

/**
 * 
 */
public class DefaultRoleValidator implements RoleValidator {

	private List<ValidRole> roles;
	
	public DefaultRoleValidator(List<ValidRole> roles){
		this.roles = roles;
	}
	public DefaultRoleValidator(ValidRole... roles){
		setValidRoles(roles);
	}
	public List<ValidRole> getValidRoles() {
		return this.roles;
	}

	public void setValidRoles(List<ValidRole> roles) {
		this.roles = roles;
	}

	public void setValidRoles(ValidRole... roles) {
		this.roles = new ArrayList<ValidRole>();
		for (ValidRole role : roles) {
			this.roles.add(role);
		}
	}
	
	public boolean isValid(Object role) {
		if(role instanceof String){
			return isValid( (String) role );
		} else if( role instanceof Role ) {
			return isValid( (Role) role );
		} else {
			return false;
		}	
	}
	public boolean isValid(String role){
		for (ValidRole vRole : roles) {
			if(vRole.getRoleName().equals(role)){
				return true;
			}
		}
		return false;
	}
	public boolean isValid(Role role){
		return roles.contains(role);
	}
}
