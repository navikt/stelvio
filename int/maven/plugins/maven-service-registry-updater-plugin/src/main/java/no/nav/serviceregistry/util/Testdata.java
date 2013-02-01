package no.nav.serviceregistry.util;

import java.io.File;
import java.util.Set;

import no.nav.aura.envconfig.client.ApplicationInfo;

public interface Testdata {

	public abstract File getAppConfigExtractDir();

	public abstract File getServiceExtractDir();

	public abstract void setAppConfigExtractDir(String appConfigExtractDir);

	public abstract void setServiceExtractDir(String serviceExtractDir);

	public abstract Set<ApplicationInfo> getEnvConfigApplications();

	public abstract void setMockApplicationInfoResolveException(
			boolean mockApplicationInfoResolveException);

	public abstract void setMockServiceResolveException(
			boolean mockServiceResolveException);

	public abstract void setEnvConfigApplications(
			Set<ApplicationInfo> envConfigApplications);

}