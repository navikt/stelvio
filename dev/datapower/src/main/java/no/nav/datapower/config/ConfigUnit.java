package no.nav.datapower.config;

import java.io.File;

import no.nav.datapower.util.DPFileUtils;

public class ConfigUnit {

	private String importDomain;
	private File rootDir;
	private File importConfigDir;
	private File importConfigFile;
	private File filesDir;
	private File filesLocalDir;
	private File filesLocalAaaDir;
	private File filesLocalWsdlDir;
	private File filesLocalXsltDir;
	
	public ConfigUnit(String domain, File rootDirectory) {
		this.importDomain = domain;
		this.rootDir = rootDirectory;
		this.importConfigDir 	= DPFileUtils.mkdirs(rootDirectory, "import-config");
		this.filesDir 			= DPFileUtils.mkdirs(rootDirectory, "files");
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
}
