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
		output.put("dmgrSOAPPort", "8879");
		output.put("linuxUser", xpath.evaluate("/resource/property[@name='username']/value"));
		String passwordUrl = xpath.evaluate("/resource/property[@name='password']/ref");
		output.put("linuxPassword", WebUtil.readUrlWithAuth(passwordUrl, username, password));

		return output;
	}

	public static HashMap<String, String> getWsadminUser(String environment, String username, String password){
		String urlString = buildWsadminUserUrl(environment, getDomain(environment));
		HashMap<String, String> output = new HashMap<String, String>();
		String xml = WebUtil.readUrl(urlString);
		XpathDocumentParser xpath = new XpathDocumentParser(xml);

		String wsadminUser = xpath.evaluate("/resource/property[@name='username']/value");
		String passwordUrl = xpath.evaluate("/resource/property[@name='password']/ref");
		String wsadminPassword = WebUtil.readUrlWithAuth(passwordUrl, username, password);

		output.put("dmgrUsername", wsadminUser);
		output.put("dmgrPassword", wsadminPassword);

		return output;
	}

	public static void registerApplication(String application, String applicationVersion, String enviroment, String username, String password) {
		
		String url = buildRegisterApplicationUrl(enviroment, application);
		String content = buildRegisterApplicationContent(application, applicationVersion);
		WebUtil.writeXmlToUrlWithAuth(url, content, username, password);
		
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
		return String.format("https://fasit.adeo.no/conf/environments/%s/applications/bpm/clusters", environment);
	}

	private static String buildDmgrResourcesUrl(String environment, String domain) {
		return String.format("https://fasit.adeo.no/conf/resources/bestmatch?envName=%s&domain=%s&type=DeploymentManager&alias=bpmDmgr&app=bpm", environment, domain);
	}

	private static String buildWsadminUserUrl(String environment, String domain) {
		return String.format("https://fasit.adeo.no/conf/resources/bestmatch?envName=%s&domain=%s&type=Credential&alias=wsadminUser&app=bpm", environment, domain);
	}
	
	private static String buildRegisterApplicationUrl(String environment, String application) {
		return String.format("https://fasit.adeo.no/conf/environments/%s/applications/%s", environment, application);
	}
	
	private static String buildRegisterApplicationContent(String application, String version) {
		return String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><deployedApplication xmlns:ns2=\"http://appconfig.aura.nav.no\"><usedResources/><appconfig><ns2:name>%s</ns2:name><ns2:resources/><ns2:exposed-services/><ns2:artifacts/></appconfig><version>%s</version></deployedApplication>", application,version);
	}
	
}
