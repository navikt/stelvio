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

	private FreemarkerConfigBuilder getConfigBuilder() {
		return new FreemarkerConfigBuilder(new File("E:/Develop/wdp/datapower-deployer/src/main/resources"));
	}

//	public void importFilesFromDirectory(String domain, File importDirectory) {
//		if (!importDirectory.isDirectory())
//			throw new IllegalArgumentException("Specified path is not a directory");
//		File[] children = importDirectory.listFiles();
//		for (File child : children) {
//			try {
//				DeviceFileStore childLocation = DeviceFileStore.fromString(child.getName());
//				getXMLMgmtSession(domain).importFiles(child, childLocation != null ? childLocation : DeviceFileStore.LOCAL);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

//	public void importFilesFromDirectory(String domain, File importDirectory, boolean clean) {
//		if (clean) {
//			try {
//				getXMLMgmtSession(domain).removeDirs(DeviceFileStore.LOCAL, "wsdl", "aaa", "ltpa", "xslt");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		importFilesFromDirectory(domain, importDirectory);
//	}

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
		 byte[] base64Config = Base64.encodeBase64(writer.getBuffer().toString().getBytes());
		 getXMLMgmtSession(domain).importConfig(new String(base64Config), ImportFormat.XML);
	}

	private List<File> getWsdlFiles(File directory) {
		List<File> wsdlFiles = DPCollectionUtils.newArrayList();
		for (File wsdlFile : DPFileUtils.findDirectory(directory, "wsdl")
				.listFiles()) {
			if (wsdlFile.isFile() && wsdlFile.getName().endsWith(".wsdl")) {
				wsdlFiles.add(wsdlFile);
			}
		}
		return wsdlFiles;
	}

	public void importFilesAndDeployConfiguration(String configTemplate, Hashtable properties, File wsdlDirectory, boolean clean) {
		String domain = (String)((Hashtable)properties.get("cfg")).get("domain");
		if (clean) {
			try {
				getXMLMgmtSession(domain).deleteDomain(domain);
				getXMLMgmtSession(domain).createDomain(domain);
				getXMLMgmtSession(domain).createDirs(wsdlDirectory, DeviceFileStore.LOCAL);	
				getXMLMgmtSession(domain).importFiles(wsdlDirectory, DeviceFileStore.LOCAL);
				deployConfiguration(domain, configTemplate, properties, wsdlDirectory);
				getXMLMgmtSession(domain).saveConfigAndRestartDomain();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
