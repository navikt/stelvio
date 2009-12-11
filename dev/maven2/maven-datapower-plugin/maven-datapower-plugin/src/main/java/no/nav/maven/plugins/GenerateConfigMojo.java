package no.nav.maven.plugins;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import no.nav.datapower.config.ConfigGenerator;
import no.nav.datapower.config.ConfigPackage;
import no.nav.datapower.config.EnvironmentResources;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.ServiceLoader;

import org.apache.commons.io.FileUtils;
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

	private Overrides overrides = null;

	
	private static class Overrides {
		
		private File propertiesFile;
		private File localAaaDir;
		private File localXsltDir;
		private File localWsdlDir;
		private File certDir;
		private File pubcertDir;		
		
		public Overrides(File overridesDir) {
			this.propertiesFile = DPFileUtils.append(overridesDir, "overrides.properties");
			this.localAaaDir = DPFileUtils.append(overridesDir, "local/aaa");
			this.localXsltDir = DPFileUtils.append(overridesDir, "local/xslt");
			this.localWsdlDir = DPFileUtils.append(overridesDir, "local/wsdl");
			this.certDir = DPFileUtils.append(overridesDir, "cert");
			this.pubcertDir = DPFileUtils.append(overridesDir, "pubcert");
		}
		protected File getLocalAaaDir() { return localAaaDir; }
		protected File getLocalXsltDir() { return localXsltDir; }
		protected File getLocalWsdlDir() { return localWsdlDir; }
		protected File getCertDir() { return certDir; }
		protected File getPubcertDir() { return pubcertDir; }
		protected File getPropertiesFile() { return propertiesFile; }
	}
		
	
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("PropertiesDirectory = " + propertiesDirectory);
		getLog().info("OutputDirectory = " + outputDirectory);
		getLog().info("ModuleDirectory = " + moduleDirectory);
		try {
			cleanDirectory(outputDirectory);
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to clean outputDirectory", e);
		}
		EnvironmentResources cfg = new EnvironmentResources();
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
		ConfigPackage unit = gen.generate();
		getLog().info("END config generation");				
	}
	
	private void cleanDirectory(File directory) throws IOException {
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
//				if (file.isDirectory())
				System.out.println("Deleting: " + file);
				FileUtils.deleteDirectory(file);
			}
		}
	}
	
	private File getPropertiesFile(File dir) {
		getLog().info("getPropertiesFile(), dir = " + dir);
		final String propsFilename = "cfg-" + getDomain() + ".properties";
		getLog().debug("Properties filename = " + propsFilename);
		return dir.listFiles((FileFilter) new NameFileFilter(propsFilename))[0];
	}
	
	private ConfigGenerator getConfigGenerator(EnvironmentResources cfg) throws MojoExecutionException {
		String generatorName = cfg.getProperty("cfgGeneratorName");
		ConfigGenerator generator = getConfigGeneratorByName(generatorName);
		generator.setEnvironmentResources(cfg);
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
		return getFileList(getOverrides().getLocalAaaDir());
	}
	private List<File> getOverriddenLocalXsltFiles() {
		return getFileList(getOverrides().getLocalXsltDir());
	}
	private List<File> getOverriddenLocalWsdlFiles() {
		return getFileList(getOverrides().getLocalWsdlDir());
	}
	
	private List<File> getOverriddenCertFiles() {
		return getFileList(getOverrides().getCertDir());
 	}
	private List<File> getOverriddenPubcertFiles() {
		return getFileList(getOverrides().getPubcertDir());
 	}
	
	private List<File> getFileList(File dir) {
		return (dir == null || dir.listFiles() == null) ? (List<File>)Collections.EMPTY_LIST : Arrays.asList(dir.listFiles());
	}

	private void addLocalOverrides(EnvironmentResources cfg) {
		if (getOverrides() != null) {
			cfg.addProperties(getOverriddenProperties());
			cfg.addAaaFiles(getOverriddenLocalAaaFiles());
			cfg.addXsltFiles(getOverriddenLocalXsltFiles());
			cfg.addWsdlFiles(getOverriddenLocalWsdlFiles());
			cfg.addCertFiles(getOverriddenCertFiles());		
			cfg.addPubcertFiles(getOverriddenPubcertFiles());		
		}
	}

}
