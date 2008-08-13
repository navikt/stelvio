package no.nav.maven.plugins;

import java.io.File;
import java.util.Properties;

import no.nav.datapower.config.ConfigGenerator;
import no.nav.datapower.config.ConfigResources;
import no.nav.datapower.config.ConfigUnit;
import no.nav.datapower.util.DPPropertiesUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which generates a DataPower configuration.
 *
 * @goal generateConfig
 * 
 * @author Torbjørn Staff, Accenture
 */
public class GenerateConfigMojo extends AbstractDataPowerMojo {
    
	/**
     * Output directory
     * 
     * @parameter expression="${outDir}" alias="outDir"
     * @required
     */	
	private File outputDirectory;
	
    /**
     * ZIP archive containing WSDLs
     * 
     * @parameter expression="${wsdls}" alias="wsdls"
     * @required
     */	
	private File wsdlArchive;
	
    /**
     * Environment specific properties to be used when generating the DataPower configuration
     * 
     * @parameter expression="${props}" alias="props"
     * @required
     */
	private File propertiesFile;
	
	private Properties envProps;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		getLog().info("Executing GenerateConfigMojo, domain = " + getDomain());
		getLog().info("WSDL archive = " + wsdlArchive);
		getLog().info("Properties file = " + propertiesFile);

		envProps = DPPropertiesUtils.load(propertiesFile);
		getLog().debug("Environment properties:\r\n: " + DPPropertiesUtils.toString(envProps));
		String generatorClassName = envProps.getProperty("cfgGeneratorClass");

		ConfigGenerator gen = getConfigGenerator(generatorClassName);
		ConfigResources cfg = new ConfigResources();
		cfg.addProperties(envProps);
		cfg.addWsdlArchive(wsdlArchive);
		gen.setConfigResources(cfg);
//		File domainDir = DPFileUtils.append(new File("/data/datapower"), cfg.getProperty("cfgDomain"));
//		File outputDir = DPFileUtils.append(domainDir, Long.toString(new Date().getTime()));
		getLog().debug("Setting output directory for config generation '" + outputDirectory + "'");				
		gen.setOutputDirectory(outputDirectory);
		getLog().info("START config generation");				
		ConfigUnit unit = gen.generate();
		getLog().info("END config generation");				
		getLog().debug("Directory import-config     = " + unit.getImportConfigDir());
		getLog().debug("Directory files/local       = " + unit.getFilesLocalDir());
		getLog().debug("Directory files/local/aaa   = " + unit.getFilesLocalAaaDir());
		getLog().debug("Directory files/local/wsdl  = " + unit.getFilesLocalWsdlDir());
		getLog().debug("Directory files/local/xslt  = " + unit.getFilesLocalXsltDir());
		getLog().info("START device import");
	}
	
//	public void setWsdlArchive(File archive) {
//		getLog().debug("Adding WSDL archive '" + archive + "'");
//		if (wsdlArchive == null)
//			wsdlArchive = DPCollectionUtils.newArrayList();
//		wsdlArchive.add(archive);
//	}
	
	private ConfigGenerator getConfigGenerator(String generatorClassName) {
//		String generatorClassName = SECGW_CFG_GEN_CLASS;
		try {
			getLog().info("Instantiating ConfigGenerator '" + generatorClassName + "'");
			return (ConfigGenerator) Class.forName(generatorClassName).newInstance();
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Caught '" + e.getClass().getName() + "' when instantiating ConfigGenerator '" + generatorClassName + "'", e);
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Caught '" + e.getClass().getName() + "' when instantiating ConfigGenerator '" + generatorClassName + "'", e);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Caught '" + e.getClass().getName() + "' when instantiating ConfigGenerator '" + generatorClassName + "'", e);
		}
	}

}
