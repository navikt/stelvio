package no.nav.datapower.util;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class DPPropertiesUtils {
	
	public static Set<String> keySet(Properties props) {
		Set<String> keys = DPCollectionUtils.newHashSet();
		for (Object key : props.keySet()) {
			if (!(key instanceof String))
				throw new IllegalArgumentException("Key '" + key + "' is not of type String");
			keys.add((String)key);
		}
		return keys;
	}
	
	public static Properties stripWhiteSpaces(Properties properties) {
		//manually trimming properties for white spaces
		Enumeration keys = properties.keys();
		String key;
		while(keys.hasMoreElements()){
			key = keys.nextElement().toString();
			properties.setProperty(key,properties.getProperty(key).trim());
		}
		return properties;
	}
	
	public static List<String> listSubsetKeys(Properties properties) {
		List<String> subsetList = DPCollectionUtils.newArrayList();
		for(Object k : properties.keySet()) {
			String key = (String)k;
			if (key.contains(".")) {
				String subset = key.substring(0,key.indexOf('.'));
				if (!subsetList.contains(subset))
					subsetList.add(subset);
			}				
		}
		return subsetList;
	}
	
	public static String[] subsetKeys(Properties properties) {
		List<String> subsetList = listSubsetKeys(properties);
		String[] subsetArray = new String[subsetList.size()];
		return subsetList.toArray(subsetArray);		
	}
	
	public static List<Properties> getSubsets(Properties properties) {
		return getSubsets(properties, subsetKeys(properties));
	}
	
	public static List<Properties> getSubsets(Properties properties, String... subsetKeys) {
		List<Properties> propsList = DPCollectionUtils.newArrayList();
		for (String subsetKey : subsetKeys) {
			propsList.add(subset(properties, subsetKey));
		}
		return propsList;
	}

	public static Map<String, Properties> getSubsetsAsMap(Properties properties) {
		Map<String, Properties> propertiesMap = DPCollectionUtils.newHashMap();
		String[] subsetKeys = subsetKeys(properties);
		List<Properties> subsetList = getSubsets(properties);
		for (int i=0; i < subsetKeys.length; i++) {
			propertiesMap.put(subsetKeys[i], subsetList.get(i));
		}
		return propertiesMap;
	}
	
	public static Properties subset(Properties props, String subsetKey) {
		Properties subset = new Properties();
		Set keys = props.keySet();
		for (Object k : keys) {
			String key = (String)k;
			if (key.startsWith(subsetKey)) {
				int subsetKeyLength = subsetKey.length();
				String newKey = key.substring(subsetKeyLength+1);
				subset.put(newKey, props.get(key));
			}
		}
		return subset;
	}
	
	public static List<ExtendedProperties> getSubsets(ExtendedProperties props, String... subsetKeys) {
		List<ExtendedProperties> propsList = DPCollectionUtils.newArrayList();
		for (String subsetKey: subsetKeys) {
			propsList.add(props.subset(subsetKey));
		}
		return propsList;
	}
	
	public static Properties convertToNestesSubsets(Properties props) {
		List<String> subsetList = listSubsetKeys(props);
		String[] subsetArray = new String[subsetList.size()];
		subsetArray = subsetList.toArray(subsetArray);
		return convertToNestedSubsets(props, subsetArray);
	}
	
	public static Properties convertToNestedSubsets(Properties props, String... subsetKeys) {
		Properties nestedProps = new Properties();
		for (String subsetName : subsetKeys) {
			nestedProps.put(subsetName, subset(props, subsetName));			
		}
		return nestedProps;
		
	}
	
	public static Map<String,String> validate(Properties props, Properties required) {
		Map<String, String> invalid = DPCollectionUtils.newHashMap();
		for (String requiredKey : keySet(required)) {
			if (!props.containsKey(requiredKey)) {
				invalid.put(requiredKey, "Key '" + requiredKey + "' is not present if the specified properties collection");
			}
			else {
				String value = (String) props.get(requiredKey);
				if (value.contains("${") && value.contains("}")) {
					invalid.put(requiredKey, "Value of '" + requiredKey + "' contains unresolved variables. Please interpolate properties!");
				}
			}
		}
		return invalid;
	}

	public static ExtendedProperties convertToNestedSubsets(ExtendedProperties props, String... subsetKeys) {
		ExtendedProperties nestedProps = new ExtendedProperties();
		for (String subsetName : subsetKeys) {
			nestedProps.put(subsetName, props.subset(subsetName));			
		}
		return nestedProps;
		
	}
	
	public static class Builder {
		
		protected CompositeConfiguration config;
		protected boolean interpolate;
		
		public Builder() {
			config = new CompositeConfiguration();
			interpolate = false;
		}
		
		public Builder properties(Properties props) {
			config.addConfiguration(ConfigurationConverter.getConfiguration(props)); return this;
		}
		
		public Builder properties(String propFile) {
			Configuration cfg;
			try {
				cfg = new PropertiesConfiguration(propFile);
			} catch (ConfigurationException e) {
				throw new IllegalArgumentException(e);
			}
			config.addConfiguration(cfg);
			return this;
		}
		
		public Builder interpolate() {
			this.interpolate = true;
			return this;
		}
		
		public Builder listDelimiter(char delimiter) {
			config.setListDelimiter(delimiter);
			return this;
		}
		
		private Configuration getConfiguration() {
//			System.out.println("interpolate = " + interpolate);
//			if (interpolate) {
//				System.out.println("Returning interpolated configuration");
//				return config.interpolatedConfiguration();
//			}
//			System.out.println("Returning non-interpolated configuration");
//			return config;
			return interpolate ? config.interpolatedConfiguration() : config;
		}
		
		public Properties buildProperties() {
			return ConfigurationConverter.getProperties(getConfiguration());
		}

		public ExtendedProperties buildExtendedProperties() {
			return ConfigurationConverter.getExtendedProperties(getConfiguration());
		}
	}
}
