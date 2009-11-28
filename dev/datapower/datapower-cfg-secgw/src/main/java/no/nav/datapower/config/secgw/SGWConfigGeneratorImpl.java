package no.nav.datapower.config.secgw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import no.nav.datapower.config.ConfigPackage;
import no.nav.datapower.config.EnvironmentResources;
import no.nav.datapower.config.WSDLFile;
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


public class SGWConfigGeneratorImpl extends FreemarkerConfigGenerator {

	private static final Logger LOG = Logger.getLogger(SGWConfigGeneratorImpl.class);
	private static final String GENERATOR_NAME = "secgw";
	private static final String TEMPLATE_CFG = "secgw-configuration.ftl";
	private static final String TEMPLATE_AAA = "aaa-mapping-file.ftl";
	private static final String TEMPLATE_AAA_SBLUTB = "aaa-mapping-file-SBLUTB.ftl";
	private static final String REQUIRED_PROPERTIES_NAME = "/cfg-secgw-required.properties";
	private static final Properties REQUIRED_PROPERTIES = DPPropertiesUtils.load(SGWConfigGeneratorImpl.class,REQUIRED_PROPERTIES_NAME);
	private static final TemplateLoader SECGW_TEMPLATE_LOADER = new StreamTemplateLoader(SGWConfigGeneratorImpl.class, "/");
	
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
		
		// Generate AAAInfo files
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
		
		try {
			String SBLUTBaaaFilename = cfg.getProperty("SBLUTBWPSaaaFileName");
			LOG.debug("Generating AAA file " + SBLUTBaaaFilename);
			File SBLUTBaaaMappingFile = DPFileUtils.append(unit.getFilesLocalAaaDir(), SBLUTBaaaFilename);
			FileWriter SBLUTBaaaWriter = new FileWriter(SBLUTBaaaMappingFile);
			processTemplate(TEMPLATE_AAA_SBLUTB, cfg.getProperties(), SBLUTBaaaWriter);
			SBLUTBaaaWriter.close();
			LOG.debug("Done generating AAA file " + SBLUTBaaaFilename);
		} catch (IOException e) {
			throw new IllegalStateException("Caught IOException while building AAAInfo file", e);
		} catch (TemplateException e) {
			throw new IllegalStateException("Template processing failed for AAAInfo file", e);
		}

		// Generate XCFG configuration
		try {			
			File cfgFile = DPFileUtils.append(unit.getImportConfigDir(), cfg.getConfigFilename());
			LOG.debug("Config file " + cfgFile);
			unit.setImportConfigFile(cfgFile);
			FileWriter cfgWriter = new FileWriter(cfgFile);
			List<WSDLFile> wsdlFiles = getWsdlFiles(unit.getFilesLocalWsdlDir());
			
			//Retrieve and load wsdl files and make them available for freemarker.
			cfg.getProperties().put("wsdls", wsdlFiles);
			
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
		// TODO verify that naming standard enforces that WSDL port file location is the root folder
		// If that can't be assumed, search for WSDL ports recursively and filter or exclude WSDL interface files
		for (File wsdlFile : directory.listFiles()) {
			if (wsdlFile.isFile() && wsdlFile.getName().endsWith(".wsdl")) {
				wsdlFiles.add(new WSDLFile(wsdlFile));
				LOG.debug("Found wsdl file: " + wsdlFile.getName());
			}
		}
		return wsdlFiles;
	}
	
}
