package no.nav.datapower.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class PropertiesValidator {

	Properties required;
	Properties toValidate;
	List<String> missingProperties;
	List<String> unresolvedProperties;
	Map<String, String> unresolvedPropertiesMap;
	List<String> unknownProperties;
	
	public PropertiesValidator(Properties required, Properties toValidate) {
		this.required = required;
		this.toValidate = toValidate;
		validate(toValidate, required);
	}

	public boolean hasInvalidProperties() {
		return hasMissingProperties() | hasUnknownProperties() | hasUnresolvedProperties();
	}
	
	public List<String> getMissingProperties() {
		if (missingProperties == null)
			missingProperties = DPCollectionUtils.newArrayList();
		return missingProperties;
	}

	public boolean hasMissingProperties() {
		return !getMissingProperties().isEmpty();
	}

	public List<String> getUnknownProperties() {
		if (unknownProperties == null)
			unknownProperties = DPCollectionUtils.newArrayList();
		return unknownProperties;
	}

	public boolean hasUnknownProperties() {
		return !getUnknownProperties().isEmpty();
	}

	public List<String> getUnresolvedProperties() {
		if (unresolvedProperties == null)
			unresolvedProperties = DPCollectionUtils.newArrayList();
		return unresolvedProperties;
	}

	public boolean hasUnresolvedProperties() {
		return !getUnresolvedProperties().isEmpty();
	}

	public Map<String, String> getUnresolvedPropertiesMap() {
		if (unresolvedPropertiesMap == null)
			unresolvedPropertiesMap = DPCollectionUtils.newHashMap();
		return unresolvedPropertiesMap;
	}
	
	private void validate(Properties props, Properties required) {
		for (String requiredKey : DPPropertiesUtils.keySet(required)) {
			if (!props.containsKey(requiredKey)) {
				getMissingProperties().add(requiredKey);
			}
			else {
				String value = (String) props.get(requiredKey);
				if (value.contains("${") && value.contains("}")) {
					getUnresolvedProperties().add(requiredKey);
					getUnresolvedPropertiesMap().put(requiredKey, (String)props.get(requiredKey));
				}
			}
		}
		for (String key : DPPropertiesUtils.keySet(props)) {
			if (!required.containsKey(key)) {
				getUnknownProperties().add(key);
			}
		}		
	}
	
	public String getErrorMessage() {
		StringWriter writer = new StringWriter();
		try {
			if (hasMissingProperties()) {
				IOUtils.write("Missing Properties:", writer);
				IOUtils.writeLines(getMissingProperties(), IOUtils.LINE_SEPARATOR, writer);
			}
			if (hasUnknownProperties()) {
				IOUtils.write("Unkown Properties:", writer);
				IOUtils.writeLines(getUnknownProperties(), IOUtils.LINE_SEPARATOR, writer);
			}
			if (hasUnresolvedProperties()) {
				IOUtils.write("Unresolved Properties values:", writer);
				IOUtils.writeLines(getUnresolvedProperties(), IOUtils.LINE_SEPARATOR, writer);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error writing validation messages", e);
		}
		return writer.getBuffer().toString();
	}
}
