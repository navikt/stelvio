package no.nav.datapower.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;

public class DPPropertiesUtils {
	
	public static Properties load(String propertiesFileName) {
		return load(new File(propertiesFileName));
	}

	public static Properties load(Class clazz, String propertiesPath) {
		try {
			InputStream stream = clazz.getResourceAsStream(propertiesPath);
			Properties properties = new Properties();
			properties.load(stream);
			return properties;
		} catch (IOException e) {
			throw new IllegalArgumentException("Caught IOException while loading the specified properties file",e);			
		}
	}
	
	public static Properties load(URL propertiesUrl) {
		return load(FileUtils.toFile(propertiesUrl));
	}
	
	public static Properties load(File propertiesFile) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(propertiesFile));
			return properties;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Specified properties file not found",e);
		} catch (IOException e) {
			throw new IllegalArgumentException("Caught IOException while loading the specified properties file",e);
		}
	}
	
	public static Properties interpolateProps(Properties props) {
		for (String key : DPPropertiesUtils.keySet(props)) {
			String value = (String)props.get(key);
			if (value.contains("${") && value.contains("}")) {
				String newValue = new StrSubstitutor(props).replace(value);
				props.put(key, newValue);
			}
		}
		return props;
	}
	
	public static boolean hasUnresolvedProperties(Properties props) {
		return new PropertiesValidator(props).hasUnresolvedProperties();
	}
	
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

	public static ExtendedProperties convertToNestedSubsets(ExtendedProperties props, String... subsetKeys) {
		ExtendedProperties nestedProps = new ExtendedProperties();
		for (String subsetName : subsetKeys) {
			nestedProps.put(subsetName, props.subset(subsetName));			
		}
		return nestedProps;
	}
	
	public static String toString(Properties props) {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.writeLines(list(props), IOUtils.LINE_SEPARATOR, writer);
		} catch (IOException e) {
			throw new IllegalArgumentException("Caught IOException while writing Properties to String", e);
		}
		return writer.getBuffer().toString();
	}
	
	public static List<String> list(Properties props) {
		List<String> list = DPCollectionUtils.newArrayList();
		for (String key : keySet(props)) {
			list.add("Property: " + key + "=" + props.get(key));
		}
		return list;
	}
}
