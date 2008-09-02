package no.nav.maven.plugins;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import no.nav.datapower.config.ConfigGenerator;
import no.nav.datapower.config.ConfigResources;
import no.nav.datapower.config.ConfigUnit;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.ServiceLoader;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

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
     * @parameter expression="${outputDir}" alias="outputDir"
     * @required
     */	
	private File outputDirectory;
	
    /**
     * Directory containing EAR files with  WSDL files
     * 
     * @parameter expression="${moduleDir}" alias="moduleDir"
     * @required
     */	
	private File moduleDirectory;
	
    /**
     * Directory containing the environment specific properties to be used when generating the DataPower configuration
     * 
     * @parameter expression="${propertiesDir}" alias="propertiesDir"
     * @required
     */
	private File propertiesDirectory;

    /**
     * Directory containing files and to be overridden locally.
     * 
     * @parameter expression="${overridesDir}" alias="overridesDir"
     * @required
     */
	private File overridesDirectory;

	/**
	 * The maven project.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	private static class Overrides {
		
		private File propertiesFile;
		private File localAaaDir;
		private File certDir;
		
		public Overrides(File overridesDir) {
			this.propertiesFile = DPFileUtils.append(overridesDir, "overrides.properties");
			this.localAaaDir = DPFileUtils.append(overridesDir, "local/aaa");
			this.certDir = DPFileUtils.append(overridesDir, "cert");
		}
		protected File getLocalAaaDir() { return localAaaDir; }
		protected File getCertDir() { return certDir; }
		protected File getPropertiesFile() { return propertiesFile; }
	}
	
	private Overrides overrides = null;
	
	private Overrides getOverrides() {
		if (overrides == null && overridesDirectory != null) {
			overrides = new Overrides(overridesDirectory);
		}
		return overrides;
	}
	
	private Properties getOverriddenProperties() {
		File propsFile = getOverrides().getPropertiesFile();
		if (propsFile.exists())
			return DPPropertiesUtils.load(propsFile);
		return new Properties();
	}
	
	private List<File> getOverriddenLocalAaaFiles() {
//		return Arrays.asList(getOverrides().getLocalAaaDir().listFiles());
		return getFileList(getOverrides().getLocalAaaDir());
	}
	
	private List<File> getOverriddenCertFiles() {
//		return Arrays.asList(getOverrides().getCertDir().listFiles());
		return getFileList(getOverrides().getCertDir());
 	}
	
	private List<File> getFileList(File dir) {
//		return dir == null ? Collections.EMPTY_LIST : Arrays.asList(dir.listFiles());
		return (dir == null || dir.listFiles() == null) ? (List<File>)Collections.EMPTY_LIST : Arrays.asList(dir.listFiles());
	}
	
//	private List<File> getFileList(File[] files) {
//		return files == null ? Collections.EMPTY_LIST : Arrays.asList(files);
//	}

	private void addLocalOverrides(ConfigResources cfg) {
		if (getOverrides() != null) {
			cfg.addProperties(getOverriddenProperties());
			cfg.addAaaFiles(getOverriddenLocalAaaFiles());
			cfg.addCertFiles(getOverriddenCertFiles());		
		}
	}
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("PropertiesDirectory = " + propertiesDirectory);
		getLog().info("OutputDirectory = " + outputDirectory);
		getLog().info("ModuleDirectory = " + moduleDirectory);
		ConfigResources cfg = new ConfigResources();
		cfg.addProperties(DPPropertiesUtils.load(getPropertiesFile(propertiesDirectory)));
		addLocalOverrides(cfg);
		if (project != null) {
			String version = project.getBasedir().getName();
			getLog().info("CONFIG_VERSION = " + version);
			cfg.getProperties().setProperty("cfgVersion", version);
		}
		getLog().info("Environment properties:\r\n: " + DPPropertiesUtils.toString(cfg.getProperties()));
		cfg.setModuleDirectory(moduleDirectory);
		ConfigGenerator gen = getConfigGenerator(cfg);
		getLog().info("START config generation");				
		ConfigUnit unit = gen.generate();
		getLog().info("END config generation");				
	}
	
	
	
	private File getPropertiesFile(File dir) {
		final String propsFilename = "cfg-" + getDomain() + ".properties";
		getLog().debug("Properties filename = " + propsFilename);
		return dir.listFiles((FileFilter) new NameFileFilter(propsFilename))[0];
	}
	private ConfigGenerator getConfigGenerator(ConfigResources cfg) throws MojoExecutionException {
		String generatorName = cfg.getProperty("cfgGeneratorName");
		ConfigGenerator generator = getConfigGeneratorByName(generatorName);
		generator.setConfigResources(cfg);
		getLog().debug("Setting output directory for config generation '" + outputDirectory + "'");				
		generator.setOutputDirectory(outputDirectory);
		return generator;		
	}
	

	private ConfigGenerator getConfigGeneratorByName(String name) throws MojoExecutionException {
		ServiceLoader<ConfigGenerator> generators = ServiceLoader.load(ConfigGenerator.class);
		for (ConfigGenerator generator : generators) {
			getLog().info("Found ConfigGenerator = " + generator.getName());
			if (generator.getName().equals(name))
				return generator;
		}
		throw new MojoExecutionException("Unkown ConfigGenerator '" + name + "'");		
	}
	
	private ConfigGenerator getConfigGeneratorByClassName(String generatorClassName) {
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
