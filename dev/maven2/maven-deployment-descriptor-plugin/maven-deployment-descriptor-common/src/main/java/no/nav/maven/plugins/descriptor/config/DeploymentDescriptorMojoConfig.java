package no.nav.maven.plugins.descriptor.config;

import java.util.List;

public class DeploymentDescriptorMojoConfig {

	private List<EarConfig> earConfigList;

	public List<EarConfig> getEarConfigList() {
		return earConfigList;
	}

	public void setEarConfigList(List<EarConfig> earConfigList) {
		this.earConfigList = earConfigList;
	}	
	
	public EarConfig getEarDescriptorConfig(String applicationName){
		if(getEarConfigList() != null){
			for (EarConfig earConfig : getEarConfigList()) {
				if(applicationName.startsWith(earConfig.getApplicationName())){
					return earConfig;
				}
			}
		}
		return null;
	}
}
