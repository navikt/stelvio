package no.nav.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import no.nav.datapower.config.ConfigGenerator;
import no.nav.datapower.config.ConfigResources;
import no.nav.datapower.config.ConfigUnit;
import no.nav.datapower.xmlmgmt.DeviceFileStore;
import no.nav.datapower.xmlmgmt.ImportFormat;
import no.nav.datapower.xmlmgmt.XMLMgmtException;
import no.nav.datapower.xmlmgmt.XMLMgmtSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

public class DeploymentBuilder {

	private static final String SECGW_CFG_GEN_CLASS = "no.nav.datapower.cfg.secgw.SecgwConfigGenerator";
	
	private XMLMgmtSession mgmtSession;

	private ConfigResources cfg;
	private URL deviceUrl;
	private String deviceUsername;
	private String devicePassword;
	private String importDomain;
	private File bustagDirectory;
	private File outputDirectory;
	private boolean cleanDomain;
	private boolean cleanFiles;
	private Log log;

	public DeploymentBuilder() {
		this.cfg = new ConfigResources();
	}
	
	public DeploymentBuilder deviceUrl(URL host) { 					this.deviceUrl = host; return this; }
	public DeploymentBuilder deviceUsername(String user) { 			this.deviceUsername = user; return this; }
	public DeploymentBuilder devicePassword(String pwd) { 			this.devicePassword = pwd; return this; }
	public DeploymentBuilder importDomain(String domain) { 			this.importDomain = domain; return this; }
	public DeploymentBuilder addProperties(Properties props) { 		this.cfg.addProperties(props); return this; }
	public DeploymentBuilder bustagDirectory(File dir) { 			this.bustagDirectory = dir; return this; }
	public DeploymentBuilder outputDirectory(File dir) { 			this.outputDirectory = dir; return this; }
	public DeploymentBuilder cleanDomain(boolean clean) { 			this.cleanDomain = clean; return this; }
	public DeploymentBuilder cleanFiles(boolean clean) { 			this.cleanFiles = clean; return this; }
	public DeploymentBuilder log(Log log) { 						this.log = log; return this; }
	public DeploymentBuilder wsdlArchive(File archive) { 			this.cfg.addWsdlArchive(archive); return this; }

	public void doDeploy() {
		log.info("Selected Kjempen deployment using ConfigGenerator....");
		log.debug("Creating new SecgwConfigGenerator");
		
//		ConfigGenerator gen = new SecgwConfigGenerator();
		ConfigGenerator gen = getConfigGenerator();
		log.debug("Creating new SecgwConfigGenerator");
		gen.setConfigResources(cfg);
//		File domainDir = DPFileUtils.append(new File("/data/datapower"), cfg.getProperty("cfgDomain"));
//		File outputDir = DPFileUtils.append(domainDir, Long.toString(new Date().getTime()));
		log.debug("Setting output directory for config generation '" + outputDirectory + "'");				
		gen.setOutputDirectory(outputDirectory);
		log.info("START config generation");				
		ConfigUnit unit = gen.generate();
		log.info("END config generation");				
		log.debug("Directory import-config     = " + unit.getImportConfigDir());
		log.debug("Directory files/local       = " + unit.getFilesLocalDir());
		log.debug("Directory files/local/aaa   = " + unit.getFilesLocalAaaDir());
		log.debug("Directory files/local/wsdl  = " + unit.getFilesLocalWsdlDir());
		log.debug("Directory files/local/xslt  = " + unit.getFilesLocalXsltDir());
		log.info("START device import");
		doDeployConfigUnit(unit, true);
		log.info("END device import");
	}
	
	private ConfigGenerator getConfigGenerator() {
//		String generatorClassName = SECGW_CFG_GEN_CLASS;
		String generatorClassName = cfg.getProperty("cfgGeneratorClass");
		try {
			log.info("Instantiating ConfigGenerator '" + generatorClassName + "'");
			return (ConfigGenerator) Class.forName(generatorClassName).newInstance();
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Caught '" + e.getClass().getName() + "' when instantiating ConfigGenerator '" + generatorClassName + "'", e);
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Caught '" + e.getClass().getName() + "' when instantiating ConfigGenerator '" + generatorClassName + "'", e);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Caught '" + e.getClass().getName() + "' when instantiating ConfigGenerator '" + generatorClassName + "'", e);
		}
	}
		
	private XMLMgmtSession getXMLMgmtSession(String domain) {
		if (mgmtSession == null) {
			log.debug("Creating new DeploymentManager, host = " + deviceUrl + ", user = " + deviceUsername + ", pwd = " + devicePassword);	
			mgmtSession = new XMLMgmtSession.Builder(deviceUrl.toString()).
										user(deviceUsername).password(devicePassword).domain(domain).build();
		}
		return mgmtSession;
	}
	
	private void doDeployConfigUnit(ConfigUnit unit, boolean clean) {
		if (clean) {
			doCleanDomain(unit.getImportDomain());
		}
		doImportFilesFromDirectory(unit.getImportDomain(), unit.getRootDir());
//		File wsdlDirectory = DPFileUtils.findDirectory(importDirectory, "wsdl");
//		File cfgFile = DPFileUtils.append(unit.getConfigDirectory(), domain + "-configuration.xcfg");
		File cfgFile = unit.getImportConfigFile();
		String config;
		try {
			config = IOUtils.toString(new FileInputStream(cfgFile));
			byte[] base64Config = Base64.encodeBase64(config.getBytes());
			getXMLMgmtSession(unit.getImportDomain()).importConfig(new String(base64Config), ImportFormat.XML);
			getXMLMgmtSession(unit.getImportDomain()).saveConfigAndRestartDomain();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private void doImportFilesFromDirectory(String domain, File importDirectory) {
		if (!importDirectory.isDirectory())
			throw new IllegalArgumentException("Specified path is not a directory");
		File[] children = importDirectory.listFiles();
		for (File child : children) {
			try {
				DeviceFileStore childLocation = DeviceFileStore.fromString(child.getName());
				DeviceFileStore location = childLocation != null ? childLocation : DeviceFileStore.LOCAL;
				if (location == DeviceFileStore.LOCAL) {
					getXMLMgmtSession(domain).createDirs(child, DeviceFileStore.LOCAL);
				}
				getXMLMgmtSession(domain).importFiles(child, location);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	private void doCleanDomain(String domain) {
		try {
			log.info("Deleting domain '" + domain + "'");
			getXMLMgmtSession(domain).deleteDomain(domain);
			log.info("Creating domain '" + domain + "'");
			getXMLMgmtSession(domain).createDomain(domain);
		} catch (Exception e) {
			throw new IllegalStateException("Caught Exception while cleaning domain '" + domain + "'", e);
		}		
	}
}
