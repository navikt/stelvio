package no.nav.datapower.config.secgw;

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
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.PropertiesBuilder;
import no.nav.datapower.util.PropertiesValidator;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateException;


public class SecGWConfigGenerator extends FreemarkerConfigGenerator {

	private static final String TEMPLATE_CFG = "secgw-configuration.ftl";
	private static final String TEMPLATE_AAA = "aaa-mapping-file.ftl";
	private static final String REQUIRED_PROPERTIES_NAME = "/cfg-secgw-required.properties";
	private static final Properties REQUIRED_PROPERTIES = DPPropertiesUtils.load(SecGWConfigGenerator.class,REQUIRED_PROPERTIES_NAME);
	private static final TemplateLoader SECGW_TEMPLATE_LOADER = new StreamTemplateLoader(SecGWConfigGenerator.class, "/");
//	private static final TemplateLoader SECGW_TEMPLATE_LOADER = new ClassTemplateLoader(SecgwConfigGenerator.class.getClass(), "/");
//	private static final TemplateLoader SECGW_TEMPLATE_LOADER = new TemplateLoader() {
//		
//		TemplateLoader loader = new ClassTemplateLoader(SecgwConfigGenerator.class.getClass(), "/");
//		public void closeTemplateSource(Object arg0) throws IOException {
//			System.out.println("SECGWTemplateLoder.closeTemplateSource()");
//			loader.closeTemplateSource(arg0);
//		}
//
//		public Object findTemplateSource(String arg0) throws IOException {
//			Object returnValue = loader.findTemplateSource(arg0);
//			System.out.println("SECGWTemplateLoader.findTemplateSource(), returned '" + returnValue + "' for template '" + arg0 + "'");
//			return returnValue;
//		}
//
//		public long getLastModified(Object arg0) {
//			long returnValue = loader.getLastModified(arg0);
//			System.out.println("SECGWTemplateLoader.getLastModified(), returned '" + returnValue + "'");
//			return returnValue;
//		}
//
//		public Reader getReader(Object arg0, String arg1) throws IOException {
//			Reader reader = loader.getReader(arg0, arg1);
//			System.out.println("SECGWTemplateLoader.getReader()");
//			return reader;
//		}			
//	};
	
	
	public SecGWConfigGenerator() {		
		super(REQUIRED_PROPERTIES, SECGW_TEMPLATE_LOADER);
	}
	
	@Override
	public ConfigUnit generate() {
		ConfigResources cfg = getConfigResources();
		Properties props = cfg.getProperties();
		System.out.println("Properties:\r\n" + DPPropertiesUtils.toString(props));
		props = new PropertiesBuilder(props).interpolate().buildProperties();
		System.out.println("Properties:\r\n" + DPPropertiesUtils.toString(props));
		PropertiesValidator validator = new PropertiesValidator(getRequiredProperties(), cfg.getProperties());
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
			List<WSDLFile> wsdlFiles = WSDLFile.getWsdlFiles(wsdlDirectory);
			cfg.getProperties().put("wsdls", wsdlFiles);
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
}
