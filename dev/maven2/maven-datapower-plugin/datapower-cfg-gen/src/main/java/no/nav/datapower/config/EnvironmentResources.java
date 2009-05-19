package no.nav.datapower.config;

import java.io.File;
import java.util.List;
import java.util.Properties;

import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.PropertiesBuilder;

public class EnvironmentResources {

	private Properties properties;
	private List<File> wsdlArchives;
	private List<File> aaaFiles;
	private List<File> xsltFiles;
	private List<File> certFiles;
	private List<File> pubcertFiles;
	private File moduleDirectory;
	private File importDirectory;
//	private File outputDirectory;

	public EnvironmentResources() {}

	public String getProperty(String key) { return properties.getProperty(key); }
	public Properties getProperties() { if (properties == null) properties = new Properties(); return properties; }
	public void addProperties(Properties props) { this.properties = new PropertiesBuilder(getProperties()).putAll(props).interpolate().buildProperties(); }

	public List<File> getWsdlArchives() { if (wsdlArchives == null) wsdlArchives = DPCollectionUtils.newArrayList(); return wsdlArchives; }
	public void setWsdlArchives(List<File> wsdlArchives) { this.wsdlArchives = wsdlArchives; }
	public void addWsdlArchive(File wsdlArchive) { getWsdlArchives().add(wsdlArchive); }
	public void addWsdlFiles(List<File> wsdlFiles) { getWsdlArchives().addAll(wsdlFiles); }
	
	public List<File> getCertFiles() { if (certFiles == null) certFiles = DPCollectionUtils.newArrayList(); return certFiles; }
	public void setCertFiles(List<File> certs) { this.certFiles = certs; }
	public void addCertFile(File cert) { getCertFiles().add(cert); }
	public void addCertFiles(List<File> certs) { getCertFiles().addAll(certs); }

	public List<File> getPubcertFiles() { if (pubcertFiles == null) pubcertFiles = DPCollectionUtils.newArrayList(); return pubcertFiles; }
	public void setPubcertFiles(List<File> pubcerts) { this.pubcertFiles = pubcerts; }
	public void addPubcertFile(File pubcert) { getPubcertFiles().add(pubcert); }
	public void addPubcertFiles(List<File> pubcerts) { getPubcertFiles().addAll(pubcerts); }

	public List<File> getAaaFiles() { if (aaaFiles == null) aaaFiles = DPCollectionUtils.newArrayList(); return aaaFiles; }
	public void setAaaFiles(List<File> aaaFiles) { this.aaaFiles = aaaFiles; }
	public void addAaaFile(File aaaFile) { getAaaFiles().add(aaaFile); }	
	public void addAaaFiles(List<File> aaaFiles) { getAaaFiles().addAll(aaaFiles); }

	public List<File> getXsltFiles() { if (xsltFiles == null) xsltFiles = DPCollectionUtils.newArrayList(); return xsltFiles; }
	public void setXsltFiles(List<File> xsltFiles) { this.xsltFiles = xsltFiles; }
	public void addXsltFile(File xsltFile) { getXsltFiles().add(xsltFile); }	
	public void addXsltFiles(List<File> xsltFiles) { getXsltFiles().addAll(xsltFiles); }

	public File getImportDirectory() { return importDirectory; }
	public void setImportDirectory(File importDirectory) { this.importDirectory = importDirectory; }

	public File getModuleDirectory() { return moduleDirectory; }
	public void setModuleDirectory(File moduleDirectory) { this.moduleDirectory = moduleDirectory; }
	
	public String getDomain() { return getProperty("cfgDomain"); }
	
	public String getConfigFilename() { return getDomain() + ".xcfg"; }
}
