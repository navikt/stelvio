package no.nav.maven.plugins.descriptor.config;

import java.util.ArrayList;
import java.util.List;

public class EjbJarConfig {
	
	protected List<EjbMethodPermissionConfig> ejbConfigList;
	protected String jarName;
	
	public String getJarName() {
		return jarName;
	}
	public void setJarName(String jarName) {
		this.jarName = jarName;
	}
	public List<EjbMethodPermissionConfig> getEjbConfigList() {
		return ejbConfigList;
	}
	public void addEjbMethodPermissionConfig(EjbMethodPermissionConfig ejbMethodPermissionConfig){
		if(ejbConfigList == null){
			ejbConfigList = new ArrayList<EjbMethodPermissionConfig>();
		}
		ejbConfigList.add(ejbMethodPermissionConfig);
	}
	
	public void setEjbConfigList(List<EjbMethodPermissionConfig> ejbConfigList) {
		this.ejbConfigList = ejbConfigList;
	}
}
