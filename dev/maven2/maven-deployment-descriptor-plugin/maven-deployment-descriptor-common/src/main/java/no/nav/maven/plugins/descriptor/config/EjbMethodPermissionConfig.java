package no.nav.maven.plugins.descriptor.config;

import java.util.ArrayList;
import java.util.List;

public class EjbMethodPermissionConfig {

	private String methodName;
	private List<String> roleNames;
	private List<SecurityRoleConfig> securityRoles;
	private MethodInterfaceType methodInterfaceType;
	private String ejbName;
	private boolean applyToAllEntepriseBeans = true;
	
	public enum MethodInterfaceType{
		REMOTE,
		LOCAL,
		HOME,
		SERVICE_ENDPOINT,
		UNSPECIFIED;
	};
	
	public EjbMethodPermissionConfig(){
		
	}

	public String getEjbName() {
		return ejbName;
	}

	public void setEjbName(String ejbName) {
		this.ejbName = ejbName;
	}

	public MethodInterfaceType getMethodInterfaceType() {
		return methodInterfaceType;
	}

	public void setMethodInterfaceType(MethodInterfaceType methodInterfaceType) {
		this.methodInterfaceType = methodInterfaceType;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<String> getRoleNames() {
		if(roleNames == null){
			roleNames = new ArrayList<String>();
			for(SecurityRoleConfig role : getSecurityRoles()){
				roleNames.add(role.getRoleName());
			}
		}
		
		return roleNames;
	}

	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}

	public boolean isApplyToAllEntepriseBeans() {
		return applyToAllEntepriseBeans;
	}

	public void setApplyToAllEntepriseBeans(boolean applyToAllEntepriseBeans) {
		this.applyToAllEntepriseBeans = applyToAllEntepriseBeans;
	}

	public List<SecurityRoleConfig> getSecurityRoles() {
		return securityRoles;
	}

	public void setSecurityRoles(List<SecurityRoleConfig> securityRoles) {
		this.securityRoles = securityRoles;
	}
}
