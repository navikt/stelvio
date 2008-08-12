package no.nav.datapower.config.partnergw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import no.nav.datapower.config.ConfigResources;
import no.nav.datapower.config.ConfigUnit;
import no.nav.datapower.config.WSDLFile;
import no.nav.datapower.config.freemarker.FreemarkerConfigGenerator;
import no.nav.datapower.config.freemarker.templates.StreamTemplateLoader;
import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.PropertiesBuilder;
import no.nav.datapower.util.PropertiesValidator;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateException;

public class PartnerGWConfigGenerator extends FreemarkerConfigGenerator {

	private static final String TEMPLATE_CFG = "cfg-partnergw-domain.ftl";
	private static final String TEMPLATE_AAA = "aaa-partnergw-dnmapping.ftl";
	private static final String REQUIRED_PROPERTIES_NAME = "/cfg-partnergw-required.properties";
	private static final Properties REQUIRED_PROPERTIES = DPPropertiesUtils.load(PartnerGWConfigGenerator.class,REQUIRED_PROPERTIES_NAME);
	private static final TemplateLoader PARTNERGW_TEMPLATE_LOADER = new StreamTemplateLoader(PartnerGWConfigGenerator.class, "/");

	public PartnerGWConfigGenerator() {		
		super(REQUIRED_PROPERTIES, PARTNERGW_TEMPLATE_LOADER);
	}

	
	@Override
	public ConfigUnit generate() {
		ConfigResources cfg = getConfigResources();
		Properties props = cfg.getProperties();
		System.out.println("Properties:\r\n" + DPPropertiesUtils.toString(props));
		props = new PropertiesBuilder(props).interpolate().buildProperties();
		System.out.println("Properties:\r\n" + DPPropertiesUtils.toString(props));
		PropertiesValidator validator = new PropertiesValidator(cfg.getProperties(), getRequiredProperties());
		if (validator.hasInvalidProperties()) {
			throw new IllegalArgumentException("Configuration contains invalid Properties:\r\n" + validator.getErrorMessage());
		}
		ConfigUnit unit = new ConfigUnit(cfg.getProperty("cfgDomain"), getOutputDirectory());
		
		// Extract wsdl archives
		try {
			for (File wsdlArchive : cfg.getWsdlArchives()) {
				DPFileUtils.extractArchive(wsdlArchive, unit.getFilesLocalDir());
			}
			DPFileUtils.deleteFilesFromDirectory(unit.getFilesLocalDir()); // make sure local directory only contains subsirectories and no immediate files
		} catch (IOException e1) {
			throw new IllegalStateException("Caught IOException while extracting WSDL archive");
		}

		// Generate AAAInfo file
		try {
			File aaaMappingFile = DPFileUtils.append(unit.getFilesLocalAaaDir(), cfg.getProperty("aaaFileName"));
			FileWriter aaaWriter = new FileWriter(aaaMappingFile);
			processTemplate(TEMPLATE_AAA, cfg.getProperties(), aaaWriter);
			aaaWriter.close();
		} catch (IOException e) {
			throw new IllegalStateException("Caught IOException while building AAAInfo file", e);
		} catch (TemplateException e) {
			throw new IllegalStateException("Template processing failed for AAAInfo file", e);
		}

		// Generate XCFG configuration
		try {
			File cfgFile = DPFileUtils.append(unit.getImportConfigDir(), cfg.getProperty("cfgDomain") + "-configuration.xcfg");
			unit.setImportConfigFile(cfgFile);
			FileWriter cfgWriter = new FileWriter(cfgFile);
			File wsdlDirectory = DPFileUtils.findDirectory(unit.getRootDir(), "wsdl");
//			List<WSDLFile> wsdlFiles = WSDLFile.getWsdlFiles(wsdlDirectory);
//			cfg.getProperties().put("wsdls", wsdlFiles);
			cfg.getProperties().put("inboundWsdls", getInboundWsdls(wsdlDirectory));
			cfg.getProperties().put("outboundWsdls", getOutboundWsdls(wsdlDirectory));
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

	
	private List<WSDLFile> getInboundWsdls(File wsdlDirectory) {
		List<WSDLFile> inboundWsdls = DPCollectionUtils.newArrayList();
		File wsdlFile = DPFileUtils.append(wsdlDirectory, "no/nav/tpsamordningregistrering/V0_2/ws/TPSamordningRegistreringWSEXP_TPSamordningRegistreringHttp_Service.wsdl");
		inboundWsdls.add(WSDLFile.createMappedUri(wsdlFile, wsdlDirectory, "/elsam/tptilb/"));
		return inboundWsdls;
	}

	private List<WSDLFile> getOutboundWsdls(File wsdlDirectory) {
		List<WSDLFile> outboundWsdls = DPCollectionUtils.newArrayList();
		File wsdlFile = DPFileUtils.append(wsdlDirectory, "no/nav/tpsamordningvarsling/V0_2/ws/TPSamordningVarslingWSEXP_TPSamordningVarslingHttp_Service.wsdl");
		outboundWsdls.add(new WSDLFile(wsdlFile, wsdlDirectory));
		return outboundWsdls;
	}
}
