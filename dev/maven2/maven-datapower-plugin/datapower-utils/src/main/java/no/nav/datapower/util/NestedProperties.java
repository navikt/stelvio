package no.nav.datapower.util;

import java.util.Map;
import java.util.Properties;

public class NestedProperties extends Properties {

	private static final long serialVersionUID = 5539299149474276208L;

	private Map<String, Properties> nestedProps;

	public NestedProperties(Properties properties, int numberOfLevels) {
		nestedProps = DPPropertiesUtils.getSubsetsAsMap(properties);
	}

	public Properties getSubset(String subsetKey) {
		return nestedProps.get(subsetKey);
	}
	
	@Override
	public String getProperty(String key) {
		throw new IllegalStateException("Method not yet implemented");
	}
}
