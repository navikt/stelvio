package no.nav.datapower.config;

import java.io.File;
import java.util.Properties;

public abstract class ConfigGenerator {

	private EnvironmentResources environmentResources;
	private File outputDirectory;
	private Properties requiredProperties;
	private String name;
	
	public ConfigGenerator() {}
	
	public ConfigGenerator(String name, Properties requiredProperties) {
		this.name = name;
		this.requiredProperties = requiredProperties;
	}
	
	public String getName() {
		return name;
	}
	
	public Properties getRequiredProperties() {
		return requiredProperties;
	}

	protected void setRequiredProperties(Properties props) {
		this.requiredProperties = props;
	}
	
	public EnvironmentResources getEnvironmentResources() {
		return environmentResources;
	}	
	
	public void setEnvironmentResources(EnvironmentResources resources) {
		this.environmentResources = resources;
	}
	
	public String getEnvironmentProperty(String property) {
		return getEnvironmentResources().getProperty(property);
	}

	public void setEnvironmentProperty(String property, Object value) {
		getEnvironmentResources().getProperties().put(property, value);
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}	

	public abstract ConfigPackage generate();
}
