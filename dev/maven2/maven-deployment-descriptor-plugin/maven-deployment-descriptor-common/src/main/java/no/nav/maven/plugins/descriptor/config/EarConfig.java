package no.nav.maven.plugins.descriptor.config;

import java.util.List;

public class EarConfig {

	private List<EjbJarConfig> ejbJarFileConfigList;
	private List<SecurityRoleConfig> securityRoles;
	private String applicationName;
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public List<EjbJarConfig> getEjbJarFileConfigList() {
		return ejbJarFileConfigList;
	}
	public EjbJarConfig getEjbJarConfig(String jarName){
		for(EjbJarConfig config : getEjbJarFileConfigList()){
			if(config.getJarName().equals(jarName)){
				return config;
			}
		}
		return null;
	}
	public void setEjbJarFileConfigList(List<EjbJarConfig> ejbJarFileConfigList) {
		this.ejbJarFileConfigList = ejbJarFileConfigList;
	}
	public List<SecurityRoleConfig> getSecurityRoles() {
		return securityRoles;
	}
	public void setSecurityRoles(List<SecurityRoleConfig> securityRoles) {
		this.securityRoles = securityRoles;
	}
}
