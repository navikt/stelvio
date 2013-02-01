package no.nav.serviceregistry.test.mocker;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.serviceregistry.exception.MavenArtifactResolevException;
import no.nav.serviceregistry.util.Testdata;

public class MyMocker implements Testdata {
	private File appConfigExtractDir;
	private File serviceExtractDir;
	private Set<ApplicationInfo> envConfigApplications;
	private boolean mockApplicationInfoResolveException;
	private boolean mockServiceResolveException;
	
	public MyMocker() {
		appConfigExtractDir = new File(this.getClass().getResource("/").getFile());
		serviceExtractDir = appConfigExtractDir;
		envConfigApplications = generateMockEnvConfigApplications();
		mockApplicationInfoResolveException = false;
		mockServiceResolveException = false;
	}

	public File getAppConfigExtractDir() {
		if(mockApplicationInfoResolveException){
			throw new MavenArtifactResolevException();
		}
		return appConfigExtractDir;
	}

	public File getServiceExtractDir() {
		if(mockServiceResolveException){
			throw new MavenArtifactResolevException(null, null);
		}
		return serviceExtractDir;
	}

	public void setAppConfigExtractDir(String appConfigExtractDir) {
		this.appConfigExtractDir = new File(appConfigExtractDir);
	}

	public void setServiceExtractDir(String serviceExtractDir) {
		this.serviceExtractDir = new File(serviceExtractDir);
	}

	public Set<ApplicationInfo> getEnvConfigApplications() {
		return this.envConfigApplications;
	}

	public void setMockApplicationInfoResolveException(boolean mockApplicationInfoResolveException) {
		this.mockApplicationInfoResolveException = mockApplicationInfoResolveException;
	}

	public void setMockServiceResolveException(boolean mockServiceResolveException) {
		this.mockServiceResolveException = mockServiceResolveException;
	}


	public void setEnvConfigApplications(Set<ApplicationInfo> envConfigApplications) {
		this.envConfigApplications = envConfigApplications;
	}
	
	
	private Set<ApplicationInfo> generateMockEnvConfigApplications(){
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
