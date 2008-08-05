package no.nav.datapower.deployer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import no.nav.datapower.templates.freemarker.FreemarkerConfigBuilder;
import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.xmlmgmt.DeviceFileStore;
import no.nav.datapower.xmlmgmt.ImportFormat;
import no.nav.datapower.xmlmgmt.XMLMgmtSession;

import org.apache.commons.codec.binary.Base64;

import freemarker.template.TemplateException;

public class DeploymentManager {

	private String hostname;
	private String username;
	private String password;
	private Map<String,XMLMgmtSession> mgmtSessions;

	public DeploymentManager(String host, String user, String pwd) {
		this.hostname = host;
		this.username = user;
		this.password = pwd;
	}

	private XMLMgmtSession getXMLMgmtSession(String domain) {
		XMLMgmtSession session = null;
		if (mgmtSessions == null)
			mgmtSessions = DPCollectionUtils.newHashMap();
		if (mgmtSessions.containsKey(domain)) {
			session = mgmtSessions.get(domain);
		} else {
			System.out.println("Opening connection to DataPower device at domain '" + domain + "'");
			session = new XMLMgmtSession.Builder(hostname).user(username).password(password).domain(domain).build();
			mgmtSessions.put(domain, session);
		}
		return session;
	}

	public FreemarkerConfigBuilder getConfigBuilder() {
		return new FreemarkerConfigBuilder(new File("E:/Develop/ws-datapower-tools/datapower-deployer/src/main/resources"));
	}

	public void importFilesFromDirectory(String domain, File importDirectory) {
		if (!importDirectory.isDirectory())
			throw new IllegalArgumentException("Specified path is not a directory");
		File[] children = importDirectory.listFiles();
		for (File child : children) {
			try {
				DeviceFileStore childLocation = DeviceFileStore.fromString(child.getName());
				DeviceFileStore location = childLocation != null ? childLocation : DeviceFileStore.LOCAL;
				if (location == DeviceFileStore.LOCAL)
					getXMLMgmtSession(domain).createDirs(child, DeviceFileStore.LOCAL);
				getXMLMgmtSession(domain).importFiles(child, location);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void importFilesFromDirectory(String domain, File importDirectory, boolean clean) {
		if (clean) {
			try {
				getXMLMgmtSession(domain).removeDirs(DeviceFileStore.LOCAL, "wsdl", "aaa", "ltpa", "xslt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		importFilesFromDirectory(domain, importDirectory);
	}

	public void deployConfiguration(String domain, String configTemplate, Hashtable properties, File wsdlDirectory) {
		StringWriter writer = new StringWriter();
//		OutputStreamWriter writer = new OutputStreamWriter(System.out);
		try {
			getConfigBuilder().buildConfig(configTemplate, properties, getWsdlFiles(wsdlDirectory), writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Configuration = " + writer.getBuffer().toString());
		byte[] base64Config = Base64.encodeBase64(writer.getBuffer().toString().getBytes());
		getXMLMgmtSession(domain).importConfig(new String(base64Config), ImportFormat.XML);
	}

	private List<File> getWsdlFiles(File directory) throws IOException {
		List<File> wsdlFiles = DPCollectionUtils.newArrayList();
		File wsdlDirectory = DPFileUtils.findDirectory(directory, "wsdl");
		System.out.println("DeploymentManager.getWsdlFiles(), wsdlDirectory = " + wsdlDirectory);
		for (File wsdlFile : wsdlDirectory.listFiles()) {
			if (wsdlFile.isFile() && wsdlFile.getName().endsWith(".wsdl")) {
				wsdlFiles.add(wsdlFile);
			}
		}
		return wsdlFiles;
	}

	public void importFilesAndDeployConfiguration(String configTemplate, Hashtable properties, File importDirectory, boolean clean) {
		String domain = (String)properties.get("cfgDomain");
		try {
			if (clean) {
				System.out.println("Deleting domain '" + domain + "'");
				getXMLMgmtSession(domain).deleteDomain(domain);
				System.out.println("Creating domain '" + domain + "'");
				getXMLMgmtSession(domain).createDomain(domain);
			}
			//getXMLMgmtSession(domain).createDirs(wsdlDirectory, DeviceFileStore.LOCAL);	
			//getXMLMgmtSession(domain).importFiles(wsdlDirectory, DeviceFileStore.LOCAL);
			importFilesFromDirectory(domain, importDirectory);
			File wsdlDirectory = DPFileUtils.findDirectory(importDirectory, "wsdl");
			System.out.println("DeploymentManager.importFilesAndDeployCOnfiguration(), wsdlDirectory = " + wsdlDirectory);
			deployConfiguration(domain, configTemplate, properties, wsdlDirectory);
			getXMLMgmtSession(domain).saveConfigAndRestartDomain();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
