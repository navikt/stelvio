package no.nav.datapower.config;

import java.io.File;
import java.util.Properties;

public abstract class ConfigGenerator {

	private ConfigResources configResources;
	private File outputDirectory;
	private Properties requiredProperties;
	
	public ConfigGenerator() {}
	
	public ConfigGenerator(Properties requiredProperties) {
		this.requiredProperties = requiredProperties;
	}
	
	protected Properties getRequiredProperties() {
		return requiredProperties;
	}

	protected void setRequiredProperties(Properties props) {
		this.requiredProperties = props;
	}
	
	public ConfigResources getConfigResources() {
		return configResources;
	}	
	
	public void setConfigResources(ConfigResources configResources) {
		this.configResources = configResources;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}	

	public abstract ConfigUnit generate();
}
