package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.util.HashMap;

public class FasitUtil {
	public static HashMap<String, String> getDmgrResources(String environment, String username, String password){
		String urlString = "https://envconfig.adeo.no/conf/resources/bestmatch?envName="+ environment +"&domain=test.local&type=DeploymentManager&alias=bpmDmgr&app=bpm";
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
		String urlString = "https://envconfig.adeo.no/conf/resources/bestmatch?envName="+ environment +"&domain=devillo.no&type=Credential&alias=wsadminUser&app=bpm";
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
}
