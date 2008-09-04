package no.nav.datapower.config.partnergw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;

import no.nav.datapower.config.ConfigResources;
import no.nav.datapower.config.ConfigUnit;
import no.nav.datapower.config.WSDLFile;
import no.nav.datapower.config.freemarker.FreemarkerConfigGenerator;
import no.nav.datapower.config.freemarker.templates.StreamTemplateLoader;
import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.DPZipUtils;
import no.nav.datapower.util.PropertiesBuilder;
import no.nav.datapower.util.PropertiesValidator;
import no.nav.datapower.util.WildcardPathFilter;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateException;

public class PGWConfigGeneratorImpl extends FreemarkerConfigGenerator {

	private static final Logger LOG = Logger.getLogger(PGWConfigGeneratorImpl.class);
	private static final String GENERATOR_NAME = "partner-gw";
	private static final String TEMPLATE_CFG = "cfg-partnergw-domain.ftl";
	private static final String TEMPLATE_AAA = "aaa-partnergw-dnmapping.ftl";
	private static final String REQUIRED_PROPERTIES_NAME = "/cfg-partnergw-required.properties";
	private static final Properties REQUIRED_PROPERTIES = DPPropertiesUtils.load(PGWConfigGeneratorImpl.class,REQUIRED_PROPERTIES_NAME);
	private static final TemplateLoader PARTNERGW_TEMPLATE_LOADER = new StreamTemplateLoader(PGWConfigGeneratorImpl.class, "/");
	
	public PGWConfigGeneratorImpl() {		
		super(GENERATOR_NAME, REQUIRED_PROPERTIES, PARTNERGW_TEMPLATE_LOADER);
	}

	
	@Override
	public ConfigUnit generate() {
		LOG.info("Executing PGWConfigGeneratorImpl.generate()");
		ConfigResources cfg = getConfigResources();
		Properties props = cfg.getProperties();
//		LOG.trace("Properties:\r\n" + DPPropertiesUtils.toString(props));
		props = new PropertiesBuilder(props).interpolate().buildProperties();
		LOG.info("Properties:\r\n" + DPPropertiesUtils.toString(props));
		LOG.info("Required Properties:\r\n" + DPPropertiesUtils.toString(getRequiredProperties()));
//		PropertiesValidator validator = new PropertiesValidator(cfg.getProperties(), getRequiredProperties());
		PropertiesValidator validator = new PropertiesValidator(props, getRequiredProperties());
		if (validator.hasInvalidProperties()) {
			throw new IllegalArgumentException("Configuration contains invalid Properties:\r\n" + validator.getErrorMessage());
		}
		ConfigUnit unit = new ConfigUnit(cfg.getDomain(), getOutputDirectory());
		
		// Extract wsdl archives
		File tmpDir = DPFileUtils.append(unit.getRootDir(), "tmp");
		File tmpDirInbound = DPFileUtils.append(tmpDir, "inbound");
		tmpDirInbound.mkdirs();
		File tmpDirOutbound = DPFileUtils.append(tmpDir, "outbound");
		tmpDirOutbound.mkdirs();
		try {
			extractWsdlFiles(getConsumerEarFiles(cfg.getModuleDirectory()), tmpDirInbound, "Web.war");
			FileUtils.copyDirectory(tmpDirInbound, unit.getFilesLocalDir());
			extractWsdlFiles(getProducerEarFiles(cfg.getModuleDirectory()), tmpDirOutbound, "EJB.jar");
			FileUtils.copyDirectory(tmpDirOutbound, unit.getFilesLocalDir());
			DPFileUtils.copyFilesToDirectory(cfg.getAaaFiles(), unit.getFilesLocalAaaDir());
			DPFileUtils.copyFilesToDirectory(cfg.getXsltFiles(), unit.getFilesLocalXsltDir());
			DPFileUtils.copyFilesToDirectory(cfg.getCertFiles(), unit.getFilesCertDir());
			DPFileUtils.copyFilesToDirectory(cfg.getPubcertFiles(), unit.getFilesPubcertDir());

		} catch (IOException e1) {
			throw new IllegalStateException("Caught IOException while extracting WSDL archive");
		}
		
		// Generate AAAInfo file
//		try {
//			File aaaMappingFile = DPFileUtils.append(unit.getFilesLocalAaaDir(), cfg.getProperty("aaaFileName"));
//			FileWriter aaaWriter = new FileWriter(aaaMappingFile);
//			processTemplate(TEMPLATE_AAA, cfg.getProperties(), aaaWriter);
//			aaaWriter.close();
//		} catch (IOException e) {
//			throw new IllegalStateException("Caught IOException while building AAAInfo file", e);
//		} catch (TemplateException e) {
//			throw new IllegalStateException("Template processing failed for AAAInfo file", e);
//		}

		// Generate XCFG configuration
		try {
//			File cfgFile = DPFileUtils.append(unit.getImportConfigDir(), cfg.getProperty("cfgDomain") + "-configuration.xcfg");
			File cfgFile = DPFileUtils.append(unit.getImportConfigDir(), cfg.getConfigFilename());
			unit.setImportConfigFile(cfgFile);
			FileWriter cfgWriter = new FileWriter(cfgFile);
//			File wsdlDirectory = DPFileUtils.findDirectory(unit.getRootDir(), "wsdl");
			File wsdlDirectory = unit.getFilesLocalWsdlDir();
			cfg.getProperties().put("inboundWsdls", getInboundWsdls(tmpDirInbound));
			cfg.getProperties().put("outboundWsdls", getOutboundWsdls(tmpDirOutbound));
			FileUtils.deleteDirectory(tmpDir);
			processTemplate(TEMPLATE_CFG, cfg.getProperties(), cfgWriter);
			cfgWriter.flush();
			cfgWriter.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} catch (TemplateException e) {
			throw new IllegalStateException(e);
		}
		return unit;
	}
	
	private List<WSDLFile> getInboundWsdls(File wsdlDirectory) throws IOException {
		List<WSDLFile> inboundWsdls = DPCollectionUtils.newArrayList();
		for (File wsdlFile : DPFileUtils.findFilesRecursively(wsdlDirectory, "*Service.wsdl")) {
			inboundWsdls.add(WSDLFile.createMappedUri(wsdlFile, wsdlDirectory, "/elsam/tptilb/"));			
		}
		return inboundWsdls;
	}

	private List<WSDLFile> getOutboundWsdls(File wsdlDirectory) throws IOException {
		List<WSDLFile> outboundWsdls = DPCollectionUtils.newArrayList();
		for (File wsdlFile : DPFileUtils.findFilesRecursively(wsdlDirectory, "*Service.wsdl")) {
			outboundWsdls.add(new WSDLFile(wsdlFile, wsdlDirectory));
		}
		return outboundWsdls;
	}
	
	private List<File> getConsumerEarFiles(File earDir) {
		LOG.info("getConsumerEarFiles(), earDir = " + earDir);
		return getEarFiles(earDir, "cfgConsumerModuleFilter");
	}

	private List<File> getProducerEarFiles(File earDir) {
		LOG.info("getProducerEarFiles(), earDir = " + earDir);
		return getEarFiles(earDir, "cfgProducerModuleFilter");
	}
	
	private List<File> getEarFiles(File earDir, String filterProperty) {
		LOG.info("getEarFiles(), earDir = " + earDir);
		List<File> earList = DPCollectionUtils.newArrayList();
		for (File dir : earDir.listFiles()) {
			if (dir.isDirectory()) {
				earList.addAll(getFileList(dir, filterProperty, ".ear"));
			}
		}
		return earList;
	}


	
	private List<File> getFileList(File dir, String filterProperty, String fileExt) {
		String filterString = getConfigResources().getProperty(filterProperty);
//		String filterString = getConfigResources().getProperty("cfgConsumerModuleFilter");
		LOG.info("getFileList(), dir = " + dir + ",  filterString = " + filterString + ", fileExt = " + fileExt);
		List<String> filters = DPCollectionUtils.listFromString(filterString);
		OrFileFilter orFilter = new OrFileFilter();
		for (String filter : filters) {
			orFilter.addFileFilter(new WildcardFileFilter(filter + fileExt));
		}
		return DPFileUtils.getFileListFiltered(dir, orFilter);		
	}

	
	private void extractWsdlFiles(List<File> ears, File dir, String wsdlSourceFilter) throws IOException {
		LOG.info("extractWsdlFiles(), dir = " + dir);
		DPCollectionUtils.printLines(ears, System.out);
		for (File ear : ears) {
			JarFile earArchive = new JarFile(ear);
			Enumeration<JarEntry> entries = earArchive.entries();
			while (entries.hasMoreElements()) {
				JarEntry jarEntry = entries.nextElement();
				LOG.info("extractWsdlFiles(), jarEntry = " + jarEntry);
				if (jarEntry.getName().endsWith(wsdlSourceFilter)) {
					File warFile = DPFileUtils.append(dir.getParentFile(), jarEntry.getName());
					DPZipUtils.writeZipEntryToOutputStream(earArchive, jarEntry, new FileOutputStream(warFile));
					DPFileUtils.extractArchiveFiltered(warFile, dir,
							new OrFileFilter(new WildcardPathFilter("*.wsdl"), new WildcardPathFilter("*.xsd")));
					FileUtils.deleteQuietly(warFile);
				}
			}
		}
	}
}
