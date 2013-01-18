package no.nav.serviceregistry.mocker;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import no.nav.aura.envconfig.client.ApplicationInfo;

public class MyMocker {

	public File getAppConfigExtractDir() {
		return new File(this.getClass().getResource("/").getFile());
	}

	public File getServiceExtractDir() {
		return new File(this.getClass().getResource("/").getFile());
	}

	public Set<ApplicationInfo> getEnvConfigApplications() {
		
		Set<ApplicationInfo> envConfigApplikasjons = new HashSet<ApplicationInfo>();
		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setName("autodeploy-test");
		applicationInfo.setAppConfigArtifactId("autodeploy-test");
		applicationInfo.setAppConfigGroupId("no.nav.leren");
		applicationInfo.setDomain("devillo.no");
		applicationInfo.setEndpoint("e34apsl00120.devillo.no");
		applicationInfo.setVersion("1.0.1-SNAPSHOT");
		envConfigApplikasjons.add(applicationInfo);
		return envConfigApplikasjons;
	}

}
