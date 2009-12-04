package no.nav.datapower.config.partnergw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import no.nav.datapower.config.ConfigPackage;
import no.nav.datapower.config.EnvironmentResources;
import no.nav.datapower.config.WSDLFile;
import no.nav.datapower.config.WSProxy;
import no.nav.datapower.config.freemarker.FreemarkerConfigGenerator;
import no.nav.datapower.config.freemarker.templates.StreamTemplateLoader;
import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.PropertiesBuilder;
import no.nav.datapower.util.PropertiesValidator;

import org.apache.log4j.Logger;

import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateException;

public class PGWConfigGeneratorImpl extends FreemarkerConfigGenerator {

	private static final Logger LOG = Logger.getLogger(PGWConfigGeneratorImpl.class);
	private static final String GENERATOR_NAME = "partner-gw";
	private static final String TEMPLATE_CFG = "cfg-partnergw-domain.ftl";
	private static final String REQUIRED_PROPERTIES_NAME = "/cfg-partnergw-required.properties";
	private static final Properties REQUIRED_PROPERTIES = DPPropertiesUtils.load(PGWConfigGeneratorImpl.class,REQUIRED_PROPERTIES_NAME);
	private static final TemplateLoader PARTNERGW_TEMPLATE_LOADER = new StreamTemplateLoader(PGWConfigGeneratorImpl.class, "/");
	
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
		
		// Generate XCFG configuration
		try {
			File cfgFile = DPFileUtils.append(cfgPackage.getImportConfigDir(), cfg.getConfigFilename());
			cfgPackage.setImportConfigFile(cfgFile);
			FileWriter cfgWriter = new FileWriter(cfgFile);
			setEnvironmentProperty("inboundWsdls", getInboundWsdls(cfgPackage.getFilesLocalWsdlDir()));
			setEnvironmentProperty("outboundWsdls", getOutboundWsdls(cfgPackage.getFilesLocalWsdlDir()));
			setEnvironmentProperty("inboundProxies", getInboundProxies(cfgPackage.getFilesLocalWsdlDir()));
			setEnvironmentProperty("outboundProxies", getOutboundProxies(cfgPackage.getFilesLocalWsdlDir()));
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

	private List<WSDLFile> getInboundWsdls(File directory) throws IOException {
		List<WSDLFile> inboundWsdls = DPCollectionUtils.newArrayList();
		String uriMapping = getEnvironmentProperty("cfgInboundUriMapping");
		for (File wsdlFile : directory.listFiles()) {
			inboundWsdls.add(WSDLFile.createMappedUri(wsdlFile, new File("wsdlDirectory"), uriMapping));			
		}
		return inboundWsdls;
	}

	private List<WSProxy> getInboundProxies(File directory) throws IOException {
		List<WSProxy> proxies = DPCollectionUtils.newArrayList();
		List<WSDLFile> wsdls = getInboundWsdls(directory);
		for (WSDLFile wsdl : wsdls) {
			System.out.println("getInboundWsdl(), proxyName = " + wsdl.getProxyName());
			getProxyByName(proxies, wsdl.getProxyName()).addWsdl(wsdl);
		}
		return proxies;
	}

	
	private List<WSProxy> getOutboundProxies(File directory) throws IOException {
		List<WSProxy> proxies = DPCollectionUtils.newArrayList();
		List<WSDLFile> wsdls = getOutboundWsdls(directory);
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

	private List<WSDLFile> getOutboundWsdls(File directory) throws IOException {
		List<WSDLFile> outboundWsdls = DPCollectionUtils.newArrayList();
		for (File wsdlFile : directory.listFiles()) {
			outboundWsdls.add(new WSDLFile(wsdlFile, new File("wsdlDirectory")));
		}
		return outboundWsdls;
	}
	
}
