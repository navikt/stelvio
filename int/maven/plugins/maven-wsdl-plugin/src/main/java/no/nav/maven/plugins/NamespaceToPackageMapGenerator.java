package no.nav.maven.plugins;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamespaceToPackageMapGenerator {
	private NamespaceToPackageMapGenerator() {
	}

	public static Map<String, String> createNameSpaceToPackageMapFromWSDLDirectory(File wsdlDirectory) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			createNameSpaceToPackageMap(wsdlDirectory, map);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	private static void createNameSpaceToPackageMap(File file, Map<String, String> nameSpaceMap) throws IOException {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				createNameSpaceToPackageMap(f, nameSpaceMap);
			}
		} else {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

			final byte[] bytes = new byte[(int) file.length()];
			bis.read(bytes);
			bis.close();
			String fileString = new String(bytes);
			Pattern p = Pattern.compile("\"http://([^\"]+)\"");
			Matcher m = p.matcher(fileString);
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

	private static String generatePackageNameFromNamespace(String nameSpace) {
		String[] parts = nameSpace.split("/");
		// Check if this is something else than a namespace
		if (parts[0].startsWith("www") || parts[0].startsWith("localhost") || parts[0].endsWith(".org"))
			return null;

		String packageName = null;
		// Skip parts[0], since this is the module name. Add the other parts,
		// dot-separated;
		for (int i = 1; i < parts.length; i++) {
			if (packageName == null) {
				packageName = parts[i];
			} else {
				packageName = packageName + "." + parts[i];
			}
		}
		return packageName;
	}
}
