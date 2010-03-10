package no.nav.datapower.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesBuilder {
		
	private boolean interpolate;
	private Properties properties;
	
	public PropertiesBuilder() {
		this.interpolate = false;
		this.properties = new Properties();
	}

	public PropertiesBuilder(Properties properties ) {
		this.interpolate = false;
		this.properties = properties;
	}

	public PropertiesBuilder put(Object key, Object value) {
		properties.put(key, value);
		return this;
	}
	
	public PropertiesBuilder putAll(Properties props) {
		properties.putAll(props);
		return this;
	}
	public PropertiesBuilder loadAndPutAll(File propFile) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(propFile));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Specified properties file not found");
		} catch (IOException e) {
			throw new IllegalArgumentException("Caught IOException while loading specified properties file");
		}
		return putAll(props);		
	}
	
	
	public PropertiesBuilder loadAndPutAll(String propFileName) {
		return loadAndPutAll(new File(propFileName));
	}
		
	public PropertiesBuilder interpolate() {
		this.interpolate = true;
		return this;
	}
				
	public Properties buildProperties() {
		return interpolate ? DPPropertiesUtils.interpolateProps(properties) : properties;
	}
}
