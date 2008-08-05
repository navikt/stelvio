package no.nav.datapower.util;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.ExtendedProperties;

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

	public static ExtendedProperties convertToNestedSubsets(ExtendedProperties props, String... subsetKeys) {
		ExtendedProperties nestedProps = new ExtendedProperties();
		for (String subsetName : subsetKeys) {
			nestedProps.put(subsetName, props.subset(subsetName));			
		}
		return nestedProps;
		
	}	
}
