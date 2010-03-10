package no.nav.datapower.config;

import java.io.File;

import no.nav.datapower.util.DPFileUtils;

public class ConfigPackage {

	private String importDomain;
	private File rootDir;
	private File importConfigDir;
	private File importConfigFile;
	private File filesDir;
	private File filesCertDir;
	private File filesPubcertDir;
	private File filesLocalDir;
	private File filesLocalAaaDir;
	private File filesLocalWsdlDir;
	private File filesLocalXsltDir;
	
	public ConfigPackage(String domain, File rootDirectory) {
		this.importDomain = domain;
		this.rootDir = rootDirectory;
		this.importConfigDir 	= DPFileUtils.mkdirs(rootDirectory, "import-xcfg");
		this.filesDir 			= DPFileUtils.mkdirs(rootDirectory, "import-files");
		this.filesCertDir 		= DPFileUtils.mkdirs(filesDir, "cert");
		this.filesPubcertDir 	= DPFileUtils.mkdirs(filesDir, "pubcert");
		this.filesLocalDir 		= DPFileUtils.mkdirs(filesDir, "local");
		this.filesLocalAaaDir 	= DPFileUtils.mkdirs(filesLocalDir, "aaa");
		this.filesLocalWsdlDir 	= DPFileUtils.mkdirs(filesLocalDir, "wsdl");
		this.filesLocalXsltDir 	= DPFileUtils.mkdirs(filesLocalDir, "xslt");
	}

	public String getImportDomain() {
		return importDomain;
	}
	
	public File getImportConfigDir() {
		return importConfigDir;
	}

	public File getFilesLocalAaaDir() {
		return filesLocalAaaDir;
	}

	public File getFilesLocalDir() {
		return filesLocalDir;
	}

	public File getFilesLocalWsdlDir() {
		return filesLocalWsdlDir;
	}

	public File getFilesLocalXsltDir() {
		return filesLocalXsltDir;
	}

	public File getRootDir() {
		return rootDir;
	}

	public File getImportConfigFile() {
		return importConfigFile;
	}

	public void setImportConfigFile(File configFile) {
		this.importConfigFile = configFile;
	}

	public File getFilesDir() {
		return filesDir;
	}

	public File getFilesCertDir() {
		return filesCertDir;
	}

	public File getFilesPubcertDir() {
		return filesPubcertDir;
	}
}
