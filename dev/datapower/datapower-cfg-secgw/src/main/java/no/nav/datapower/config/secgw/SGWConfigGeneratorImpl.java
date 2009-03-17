package no.nav.datapower.config.secgw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import no.nav.datapower.config.ConfigPackage;
import no.nav.datapower.config.EnvironmentResources;
import no.nav.datapower.config.WSDLFile;
import no.nav.datapower.config.freemarker.FreemarkerConfigGenerator;
import no.nav.datapower.config.freemarker.templates.StreamTemplateLoader;
import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.DPStreamUtils;
import no.nav.datapower.util.DPZipUtils;
import no.nav.datapower.util.PropertiesBuilder;
import no.nav.datapower.util.PropertiesValidator;
import no.nav.datapower.util.WildcardPathFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;

import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateException;


public class SGWConfigGeneratorImpl extends FreemarkerConfigGenerator {

	private static final Logger LOG = Logger.getLogger(SGWConfigGeneratorImpl.class);
	private static final String GENERATOR_NAME = "secgw";
	private static final String TEMPLATE_CFG = "secgw-configuration.ftl";
	private static final String TEMPLATE_AAA = "aaa-mapping-file.ftl";
	private static final String TEMPLATE_AAA_WORKMATE = "aaa-basic-auth-file.ftl";
	private static final String REQUIRED_PROPERTIES_NAME = "/cfg-secgw-required.properties";
	private static final Properties REQUIRED_PROPERTIES = DPPropertiesUtils.load(SGWConfigGeneratorImpl.class,REQUIRED_PROPERTIES_NAME);
	private static final TemplateLoader SECGW_TEMPLATE_LOADER = new StreamTemplateLoader(SGWConfigGeneratorImpl.class, "/");
	private File tmpDir;
	
	public SGWConfigGeneratorImpl() {		
		super(GENERATOR_NAME, REQUIRED_PROPERTIES, SECGW_TEMPLATE_LOADER);
	}
	
	@Override
	public ConfigPackage generate() {
		EnvironmentResources cfg = getEnvironmentResources();
		Properties props = cfg.getProperties();
//		System.out.println("Properties:\r\n" + DPPropertiesUtils.toString(props));
		props = new PropertiesBuilder(props).interpolate().buildProperties();
		LOG.debug("Properties:\r\n" + DPPropertiesUtils.toString(props));
		PropertiesValidator validator = new PropertiesValidator(cfg.getProperties(), getRequiredProperties());
		if (validator.hasInvalidProperties()) {
			throw new IllegalArgumentException("Configuration contains invalid Properties:\r\n" + validator.getErrorMessage());
		}
		LOG.debug("Output directory = " + getOutputDirectory());
		ConfigPackage unit = new ConfigPackage(cfg.getProperty("cfgDomain"), getOutputDirectory());
		LOG.debug("Root directory = " + unit.getRootDir());
		LOG.debug("Files local directory = " + unit.getFilesLocalDir());
		LOG.debug("Files local wsdl directory = " + unit.getFilesLocalWsdlDir());
		
		tmpDir = DPFileUtils.append(unit.getRootDir(), "tmp");
		
		// Extract wsdl from EAR archives
		List<File> earFiles = getEarFiles(cfg.getModuleDirectory());
		try {			
			extractWsdlFiles(earFiles, unit.getFilesLocalDir());
			DPFileUtils.copyFilesToDirectory(cfg.getAaaFiles(), unit.getFilesLocalAaaDir());
			DPFileUtils.copyFilesToDirectory(cfg.getCertFiles(), unit.getFilesCertDir());
			DPFileUtils.copyFilesToDirectory(cfg.getXsltFiles(), unit.getFilesLocalXsltDir());
			DPFileUtils.copyFilesToDirectory(getLocalFiles("xslt"), unit.getFilesLocalXsltDir());
			//FIXME Workmate; comment the below line in again to copy workmate wsdl file to DataPower
			//DPFileUtils.copyFilesToDirectory(getLocalFiles("wsdl"), unit.getFilesLocalWsdlDir());
		} catch (IOException e) {
			throw new IllegalStateException("Caught IOException while extracting WSDL files from EAR archives",e);
		}

		// Generate AAAInfo file
		try {
			String aaaFilename = cfg.getProperty("aaaFileName");
			LOG.debug("Generating AAA file " + aaaFilename);
			File aaaMappingFile = DPFileUtils.append(unit.getFilesLocalAaaDir(), aaaFilename);
			FileWriter aaaWriter = new FileWriter(aaaMappingFile);
			processTemplate(TEMPLATE_AAA, cfg.getProperties(), aaaWriter);
			aaaWriter.close();
			LOG.debug("Done generating AAA file " + aaaFilename);
		} catch (IOException e) {
			throw new IllegalStateException("Caught IOException while building AAAInfo file", e);
		} catch (TemplateException e) {
			throw new IllegalStateException("Template processing failed for AAAInfo file", e);
		}

		//FIXME Workmate; comment in the below section to create aaainfo file used for aaa step		
		// Generate AAAInfo file for Workmate
//		try {
//			String wmateAaaFilename = cfg.getProperty("workmateAaaFilename");
//			LOG.debug("Generating Workmate AAA file " + wmateAaaFilename);
//			File wmateAaaMappingFile = DPFileUtils.append(unit.getFilesLocalAaaDir(), wmateAaaFilename);
//			FileWriter wmateAaaWriter = new FileWriter(wmateAaaMappingFile);
//			processTemplate(TEMPLATE_AAA_WORKMATE, cfg.getProperties(), wmateAaaWriter);
//			wmateAaaWriter.close();
//			LOG.debug("Done generating Workmate AAA file " + wmateAaaFilename);
//		} catch (IOException e) {
//			throw new IllegalStateException("Caught IOException while building Workmate AAAInfo file", e);
//		} catch (TemplateException e) {
//			throw new IllegalStateException("Template processing failed for Workmate AAAInfo file", e);
//		}

		
		// Generate XCFG configuration
		try {			
			File cfgFile = DPFileUtils.append(unit.getImportConfigDir(), cfg.getConfigFilename());
			LOG.debug("Config file " + cfgFile);
			unit.setImportConfigFile(cfgFile);
			FileWriter cfgWriter = new FileWriter(cfgFile);
			List<WSDLFile> wsdlFiles = getWsdlFiles(unit.getFilesLocalWsdlDir());
			
			//Retrieve and load wsdl files and make them available for freemarker.
			cfg.getProperties().put("wsdls", wsdlFiles);
			//"Hack". I was unable to make freemarker do this in the template...
			String[] portListArray = cfg.getProperty("wsdlPortBindingList").split(",");
			cfg.getProperties().put("wsdlPortBindingList", portListArray);			
			
			LOG.debug("Processing template");
			processTemplate(TEMPLATE_CFG, cfg.getProperties(), cfgWriter);
			LOG.debug("Done processing template");
			cfgWriter.flush();
			cfgWriter.close();
			LOG.debug("Done generating config file " + cfg.getConfigFilename());
		} catch (IOException e) {
			throw new IllegalStateException("Caught IOException while generating DataPower configuration", e);
		} catch (TemplateException e) {
			throw new IllegalStateException("Caught IllegalStateException while generating DataPower configuration", e);
		}
		return unit;
	}
	
	private List<WSDLFile> getWsdlFiles(File directory) throws IOException {		
		List<WSDLFile> wsdlFiles = DPCollectionUtils.newArrayList();
		LOG.debug("Gathering wsdl files for automatical generation of WS-proxys from directory: " + directory);				
		for (File wsdlFile : getFileList(directory, "cfgWsdlFileFilter", "wsdl")) {
			if (wsdlFile.isFile() && wsdlFile.getName().endsWith(".wsdl")) {
				wsdlFiles.add(new WSDLFile(wsdlFile));
				LOG.debug("Found wsdl file: " + wsdlFile.getName());
			}
		}
		return wsdlFiles;
	}

	
	private List<File> getLocalFiles(String folderName) {
		URL localDirUrl = getClass().getClassLoader().getResource(String.format("local/%1$s/", folderName));
		List<File> fileList = null;
		if (localDirUrl.getProtocol().equals("file")) {
			fileList = getFileList(FileUtils.toFile(localDirUrl));			
		}
		else if (localDirUrl.getProtocol().equals("jar")) {//hack
			fileList = getFileListFromJarClasspath(localDirUrl);
		}
		return fileList;
	}
	
	private List<File> getFileListFromJarClasspath(URL url) {
		List<File> fileList = DPCollectionUtils.newArrayList();
		JarURLConnection conn;
		try {
			conn = (JarURLConnection)url.openConnection();
			JarEntry directory = conn.getJarEntry();
			DPFileUtils.append(tmpDir, directory.getName()).mkdirs();
			JarFile jarFile = conn.getJarFile();
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.isDirectory() && entry.getName().startsWith(directory.getName())) {
					File file = DPFileUtils.append(tmpDir, entry.getName());
					DPStreamUtils.pumpAndClose(jarFile.getInputStream(entry), new FileOutputStream(file));
					fileList.add(file);
				}
			}			
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return fileList;
	}
	

	private List<File> getFileList(File dir) {
		return (dir == null || dir.listFiles() == null) ? (List<File>)Collections.EMPTY_LIST : Arrays.asList(dir.listFiles());
	}
	
	private List<File> getWsdlArchives(File wsdlDir) {
		return getFileList(wsdlDir, "cfgWsdlArchiveFilter", ".zip");
	}

	private List<File> getEarFiles(File earDir) {
		LOG.trace("getEarFiles(), earDir = " + earDir);
		List<File> earList = DPCollectionUtils.newArrayList();
		for (File dir : earDir.listFiles()) {
			if (dir.isDirectory()) {
				earList.addAll(getFileList(dir, "cfgConsumerModuleFilter", ".ear"));
			}
		}
		return earList;
	}

	
	private List<File> getFileList(File dir, String filterProperty, String fileExt) {
		String filterString = getEnvironmentResources().getProperty(filterProperty);
		LOG.trace("getFileList(), dir = " + dir + ",  filterString = " + filterString + ", fileExt = " + fileExt);
		List<String> filters = DPCollectionUtils.listFromString(filterString);
		OrFileFilter orFilter = new OrFileFilter();
		for (String filter : filters) {
			orFilter.addFileFilter(new WildcardFileFilter(filter + fileExt));
		}
		return DPFileUtils.getFileListFiltered(dir, orFilter);		
	}

	
	private void extractWsdlFiles(List<File> ears, File localFilesWsdlDir) throws IOException {
		LOG.trace("extractWsdlFiles(), localFilesWsdlDir = " + localFilesWsdlDir);
		DPCollectionUtils.printLines(ears, System.out);
		for (File ear : ears) {
			JarFile earArchive = new JarFile(ear);
			Enumeration<JarEntry> entries = earArchive.entries();
			while (entries.hasMoreElements()) {
				JarEntry jarEntry = entries.nextElement();
				LOG.trace("extractWsdlFiles(), jarEntry = " + jarEntry);
				if (jarEntry.getName().endsWith(".war")) {
					File warFile = DPFileUtils.append(localFilesWsdlDir.getParentFile(), jarEntry.getName());
					DPZipUtils.writeZipEntryToOutputStream(earArchive, jarEntry, new FileOutputStream(warFile));
					DPFileUtils.extractArchiveFiltered(warFile, localFilesWsdlDir,
							new OrFileFilter(new WildcardPathFilter("*.wsdl"), new WildcardPathFilter("*.xsd")));
					FileUtils.deleteQuietly(warFile);
				}
			}
		}
	}	
}
