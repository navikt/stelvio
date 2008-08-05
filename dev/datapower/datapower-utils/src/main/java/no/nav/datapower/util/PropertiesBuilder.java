package no.nav.datapower.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class PropertiesBuilder {
		
//	protected CompositeConfiguration config;
	protected boolean interpolate;
	private List<Properties> propsList;
	
	public PropertiesBuilder() {
//		config = new CompositeConfiguration();
		interpolate = false;
		propsList = DPCollectionUtils.newLinkedList();
	}
		
	public PropertiesBuilder properties(Properties props) {
//		config.addConfiguration(ConfigurationConverter.getConfiguration(props));
		propsList.add(props);
		return this;
	}
		
	public PropertiesBuilder properties(String propFile) {
//		Configuration cfg;
//		try {
//			cfg = new PropertiesConfiguration(propFile);
//		} catch (ConfigurationException e) {
//			throw new IllegalArgumentException(e);
//		}
//		config.addConfiguration(cfg);
//		return this;
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(propFile));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Specified properties file not found");
		} catch (IOException e) {
			throw new IllegalArgumentException("Caught IOException while loading specified properties file");
		}
		return properties(props);
	}
		
	public PropertiesBuilder interpolate() {
		this.interpolate = true;
		return this;
	}
		
//	public PropertiesBuilder listDelimiter(char delimiter) {
//		config.setListDelimiter(delimiter);
//		return this;
//	}
		
//	private Configuration getConfiguration() {
//		return interpolate ? config.interpolatedConfiguration() : config;
//	}
		
	public Properties buildProperties() {
//		return ConfigurationConverter.getProperties(getConfiguration());
		Properties props = mergeProps(propsList);
		return interpolate ? interpolateProps(props) : props;
	}

//	public ExtendedProperties buildExtendedProperties() {
//		return ConfigurationConverter.getExtendedProperties(getConfiguration());
//	}
	
	private Properties mergeProps(List<Properties> propsList) {
		Properties merged = new Properties();
		for (Properties props : propsList) {
			merged.putAll(props);
		}
		return merged;
	}
	
	private Properties interpolateProps(Properties props) {
		for (String key : DPPropertiesUtils.keySet(props)) {
			String value = (String)props.get(key);
			if (hasUnresolvedKey(value)) {
				for (String unresolvedKey : getUnresolvedKeys(value)) {
					String valueOfUnresolvedKey = (String) props.get(unresolvedKey);
					String newValue = value.replace("${" + unresolvedKey + "}", valueOfUnresolvedKey);
					props.put(key, newValue);
				}
			}
		}
		return props;
	}

	private boolean hasUnresolvedKey(String value) {
		return value.contains("${") && value.contains("}");
	}

	private String[] getUnresolvedKeys(String value) {
		return StringUtils.substringsBetween(value, "${", "}");
	}	
}
