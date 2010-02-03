package no.nav.maven.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.IOUtil;

public class NamespaceToPackageMapGenerator {
	private String encoding;

	private Pattern namespacePattern = Pattern.compile("\"http://([^\"]+)\"");

	public NamespaceToPackageMapGenerator() {
		this(System.getProperty("file.encoding"));
	}

	public NamespaceToPackageMapGenerator(String encoding) {
		this.encoding = encoding;
	}

	public Map<String, String> createNameSpaceToPackageMapFromWSDLDirectory(File wsdlDirectory) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			createNameSpaceToPackageMap(wsdlDirectory, map);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	private void createNameSpaceToPackageMap(File file, Map<String, String> nameSpaceMap) throws IOException {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				createNameSpaceToPackageMap(f, nameSpaceMap);
			}
		} else {
			String fileString = readFileToString(file);
			Matcher m = namespacePattern.matcher(fileString);
			while (m.find()) {
				String nameSpace = m.group(1);
				String packageName = generatePackageNameFromNamespace(nameSpace);
				if (packageName != null) {
					String escapedNameSpaceUrl = "http\\://" + nameSpace;
					nameSpaceMap.put(escapedNameSpaceUrl, packageName);
				}
			}
		}
	}

	private String readFileToString(File file) throws IOException {
		StringWriter stringWriter = new StringWriter();
		Reader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
		IOUtil.copy(fileReader, stringWriter);
		fileReader.close();
		return stringWriter.toString();
	}

	private String generatePackageNameFromNamespace(String nameSpace) {
		String[] parts = nameSpace.split("/");
		// Check if this is something else than a namespace
		if (parts[0].startsWith("www") || parts[0].startsWith("localhost") || parts[0].endsWith(".org"))
			return null;

		StringBuilder packageName = new StringBuilder();
		// Skip parts[0], since this is the module name. Add the other parts,
		// dot-separated;
		for (int i = 1; i < parts.length; i++) {
			if (packageName.length() > 0) {
				packageName.append(".");
			}
			packageName.append(parts[i]);
		}
		if (packageName.length() > 0) {
			return packageName.toString().toLowerCase();
		} else {
			return null;
		}
	}
}
