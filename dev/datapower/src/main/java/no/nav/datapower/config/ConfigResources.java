package no.nav.datapower.config;

import java.io.File;
import java.util.List;
import java.util.Properties;

import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.PropertiesBuilder;

public class ConfigResources {

	private Properties properties;
	private List<File> wsdlArchives;
	private List<File> certFiles;
	private List<File> pubCertFiles;
	private List<File> localFiles;
	private File importDirectory;
//	private File outputDirectory;

	public ConfigResources() {}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public Properties getProperties() {
		if (properties == null)
			properties = new Properties();
		return properties;
	}

	public void addProperties(Properties props) {
		this.properties = new PropertiesBuilder(getProperties()).putAll(props).interpolate().buildProperties();
	}

	public List<File> getWsdlArchives() {
		if (wsdlArchives == null)
			wsdlArchives = DPCollectionUtils.newArrayList();
		return wsdlArchives;
	}

	public void setWsdlArchives(List<File> wsdlArchives) {
		this.wsdlArchives = wsdlArchives;
	}

	public void addWsdlArchive(File wsdlArchive) {
		getWsdlArchives().add(wsdlArchive);
	}

	public List<File> getCertFiles() {
		if (certFiles == null)
			certFiles = DPCollectionUtils.newArrayList();
		return certFiles;
	}

	public void setCertFiles(List<File> certFiles) {
		this.certFiles = certFiles;
	}
	
	public void addCertFile(File certFile) {
		getCertFiles().add(certFile);
	}

	public File getImportDirectory() {
		return importDirectory;
	}

	public void setImportDirectory(File importDirectory) {
		this.importDirectory = importDirectory;
	}
	
}
