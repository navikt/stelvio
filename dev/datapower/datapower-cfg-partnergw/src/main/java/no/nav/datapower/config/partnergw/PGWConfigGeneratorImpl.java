package no.nav.datapower.config.partnergw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import no.nav.datapower.config.ConfigPackage;
import no.nav.datapower.config.EnvironmentResources;
import no.nav.datapower.config.WSDLFile;
import no.nav.datapower.config.WSProxy;
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
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
	private static final OrFileFilter WSDL_OR_XSD_FILENAME_FILTER = new OrFileFilter(new WildcardPathFilter("*.wsdl"), new WildcardPathFilter("*.xsd"));
	
	private File tmpDir;
	
	public PGWConfigGeneratorImpl() {		
		super(GENERATOR_NAME, REQUIRED_PROPERTIES, PARTNERGW_TEMPLATE_LOADER);
	}

	
	@Override
	public ConfigPackage generate() {
		LOG.info("Executing PGWConfigGeneratorImpl.generate()");
		EnvironmentResources cfg = getEnvironmentResources();
		Properties props = cfg.getProperties();
		props = new PropertiesBuilder(props).interpolate().buildProperties();
		LOG.info("Properties:\r\n" + DPPropertiesUtils.toString(props));
		LOG.info("Required Properties:\r\n" + DPPropertiesUtils.toString(getRequiredProperties()));
		PropertiesValidator validator = new PropertiesValidator(props, getRequiredProperties());
		if (validator.hasInvalidProperties()) {
			throw new IllegalArgumentException("Configuration contains invalid Properties:\r\n" + validator.getErrorMessage());
		}
		ConfigPackage cfgPackage = new ConfigPackage(cfg.getDomain(), getOutputDirectory());
		
		// Extract wsdl archives
		tmpDir = DPFileUtils.append(cfgPackage.getRootDir(), "tmp");
		File tmpDirInbound = DPFileUtils.append(tmpDir, "inbound");
		tmpDirInbound.mkdirs();
		File tmpDirOutbound = DPFileUtils.append(tmpDir, "outbound");
		tmpDirOutbound.mkdirs();
		try {
			extractWsdlsFromConsumerModules(cfg, tmpDirInbound);
			extractWsdlsFromProducerModules(cfg, tmpDirOutbound);
			FileUtils.copyDirectory(tmpDirInbound, cfgPackage.getFilesLocalDir());
			FileUtils.copyDirectory(tmpDirOutbound, cfgPackage.getFilesLocalDir());
			DPFileUtils.copyFilesToDirectory(cfg.getAaaFiles(), cfgPackage.getFilesLocalAaaDir());
			DPFileUtils.copyFilesToDirectory(getLocalXslt(), cfgPackage.getFilesLocalXsltDir());
			DPFileUtils.copyFilesToDirectory(cfg.getXsltFiles(), cfgPackage.getFilesLocalXsltDir());
			DPFileUtils.copyFilesToDirectory(cfg.getCertFiles(), cfgPackage.getFilesCertDir());
			DPFileUtils.copyFilesToDirectory(cfg.getPubcertFiles(), cfgPackage.getFilesPubcertDir());

		} catch (IOException e1) {
			throw new IllegalStateException("Caught IOException while extracting WSDL archive");
		}
		
		// Generate XCFG configuration
		try {
			File cfgFile = DPFileUtils.append(cfgPackage.getImportConfigDir(), cfg.getConfigFilename());
			cfgPackage.setImportConfigFile(cfgFile);
			FileWriter cfgWriter = new FileWriter(cfgFile);
			setEnvironmentProperty("inboundWsdls", getInboundWsdls(tmpDirInbound));
			setEnvironmentProperty("outboundWsdls", getOutboundWsdls(tmpDirOutbound));
			setEnvironmentProperty("inboundProxies", getInboundProxies(tmpDirInbound));
			setEnvironmentProperty("outboundProxies", getOutboundProxies(tmpDirOutbound));
			FileUtils.deleteDirectory(tmpDir);
			addTrustCerts(cfg.getProperties());
			processTemplate(TEMPLATE_CFG, cfg.getProperties(), cfgWriter);
			cfgWriter.flush();
			cfgWriter.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} catch (TemplateException e) {
			throw new IllegalStateException(e);
		}
		return cfgPackage;
	}
	
	private void addTrustCerts(Properties props) {
		List<String> trustCertList = DPCollectionUtils.listFromString(props.getProperty("partnerTrustCerts"));
		List<Map<String, String>> trustCertMapList = DPCollectionUtils.newArrayList();
		for (String trustCert : trustCertList) {
			Map<String,String> cert = DPCollectionUtils.newHashMap();
			cert.put("name", trustCert.substring(trustCert.lastIndexOf("/")+1));
//			cert.put("name", StringUtils.substringBetween(trustCert, ":///", "."));
			cert.put("file", trustCert);
			trustCertMapList.add(cert);
		}
		setEnvironmentProperty("partnerTrustedCerts", trustCertMapList);
	}

	private List<File> getLocalXslt() {
		URL localDirUrl = getClass().getClassLoader().getResource("local/xslt/");
		List<File> fileList = null;
		if (localDirUrl.getProtocol().equals("file")) {
			fileList = getFileList(FileUtils.toFile(localDirUrl));			
		}
		else if (localDirUrl.getProtocol().equals("jar")) {
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

	private void extractWsdlsFromProducerModules(EnvironmentResources cfg, File outputDir) throws IOException {
		extractWsdlFiles(getProducerEarFiles(cfg.getModuleDirectory()), outputDir, "EJB.jar");
	}


	private void extractWsdlsFromConsumerModules(EnvironmentResources cfg, File outputDir) throws IOException {
		extractWsdlFiles(getConsumerEarFiles(cfg.getModuleDirectory()), outputDir, "Web.war");
	}

	
	private List<WSDLFile> getInboundWsdls(File wsdlDirectory) throws IOException {
		List<WSDLFile> inboundWsdls = DPCollectionUtils.newArrayList();
		String wsdlFilter = getEnvironmentProperty("cfgInboundWsdlFilter");
		String uriMapping = getEnvironmentProperty("cfgInboundUriMapping");
		for (File wsdlFile : DPFileUtils.findFilesRecursively(wsdlDirectory, wsdlFilter)) {
			inboundWsdls.add(WSDLFile.createMappedUri(wsdlFile, wsdlDirectory, uriMapping));			
		}
		return inboundWsdls;
	}

	private List<WSProxy> getInboundProxies(File wsdlDirectory) throws IOException {
		List<WSProxy> proxies = DPCollectionUtils.newArrayList();
		List<WSDLFile> wsdls = getInboundWsdls(wsdlDirectory);
		for (WSDLFile wsdl : wsdls) {
			System.out.println("getInboundWsdl(), proxyName = " + wsdl.getProxyName());
			getProxyByName(proxies, wsdl.getProxyName()).addWsdl(wsdl);
		}
		return proxies;
	}

	
	private List<WSProxy> getOutboundProxies(File wsdlDirectory) throws IOException {
		List<WSProxy> proxies = DPCollectionUtils.newArrayList();
		List<WSDLFile> wsdls = getOutboundWsdls(wsdlDirectory);
		for (WSDLFile wsdl : wsdls) {
			getProxyByName(proxies, wsdl.getProxyName()).addWsdl(wsdl);
		}
		return proxies;
	}
	
	private WSProxy getProxyByName(List<WSProxy> proxies, String name) {
		WSProxy proxy = null;
		for (WSProxy p : proxies) {
			if (p.getName().equals(name)) {
				proxy = p;
			}
		}
		if (proxy == null) {
			proxy = new WSProxy(name);
			proxies.add(proxy);
		}
		return proxy;
	}

	private List<WSDLFile> getOutboundWsdls(File wsdlDirectory) throws IOException {
		List<WSDLFile> outboundWsdls = DPCollectionUtils.newArrayList();
		String wsdlFilter = getEnvironmentResources().getProperty("cfgOutboundWsdlFilter");
		for (File wsdlFile : DPFileUtils.findFilesRecursively(wsdlDirectory, wsdlFilter)) {
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
		String filterString = getEnvironmentResources().getProperty(filterProperty);
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
					DPFileUtils.extractArchiveFiltered(warFile, dir, WSDL_OR_XSD_FILENAME_FILTER);
					FileUtils.deleteQuietly(warFile);
				}
			}
		}
	}
}
