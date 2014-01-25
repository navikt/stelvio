package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.util.HashMap;

public class FasitUtil {
	private static String domain;
	public static HashMap<String, String> getDmgrResources(String environment, String username, String password){
		String urlString = buildDmgrResourcesUrl(environment, getDomain(environment));
		HashMap<String, String> output = new HashMap<String, String>();
		String xml = WebUtil.readUrl(urlString);
		XpathDocumentParser xpath = new XpathDocumentParser(xml);

		output.put("envName", xpath.evaluate("/resource/environmentName"));
		output.put("dmgrHostname", xpath.evaluate("/resource/property[@name='hostname']/value"));
		output.put("dmgrUsername", xpath.evaluate("/resource/property[@name='username']/value"));
		String passwordUrl = xpath.evaluate("/resource/property[@name='password']/ref");
		output.put("dmgrPassword", WebUtil.readUrlWithAuth(passwordUrl, username, password));
		output.put("dmgrSOAPPort", "8879");

		return output;
	}

	public static HashMap<String, String> getLinuxUser(String environment, String username, String password){
		String urlString = buildLinuxUserUrl(environment, getDomain(environment));
		HashMap<String, String> output = new HashMap<String, String>();
		String xml = WebUtil.readUrl(urlString);
		XpathDocumentParser xpath = new XpathDocumentParser(xml);

		String linuxUser = xpath.evaluate("/resource/property[@name='username']/value");
		String passwordUrl = xpath.evaluate("/resource/property[@name='password']/ref");
		String linuxPassword = WebUtil.readUrlWithAuth(passwordUrl, username, password);

		output.put("linuxUser", linuxUser);
		output.put("linuxPassword", linuxPassword);

		return output;
	}

	private static String getDomain(String environment) {
		if (domain == null){
			String url = buildDomainUrl(environment);
			String xml = WebUtil.readUrl(url);
			XpathDocumentParser xpath = new XpathDocumentParser(xml);
			domain = xpath.evaluate("/collection/cluster/domain");
		}
		return domain;
	}

	private static String buildDomainUrl(String environment) {
		return String.format("http://fasit.adeo.no/conf/environments/%s/applications/bpm/clusters", environment);
	}

	private static String buildDmgrResourcesUrl(String environment, String domain) {
		return String.format("https://envconfig.adeo.no/conf/resources/bestmatch?envName=%s&domain=%s&type=DeploymentManager&alias=bpmDmgr&app=bpm", environment, domain);
	}

	private static String buildLinuxUserUrl(String environment, String domain) {
		return String.format("https://envconfig.adeo.no/conf/resources/bestmatch?envName=%s&domain=%s&type=Credential&alias=wsadminUser&app=bpm", environment, domain);
	}
}
