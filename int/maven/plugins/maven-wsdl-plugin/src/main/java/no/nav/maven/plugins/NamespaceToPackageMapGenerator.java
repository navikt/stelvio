package no.nav.maven.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
				String packageName = generatePackageNameFromNamespace("http://" + nameSpace);
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
		try {
			Appendable packageName = new Appendable() {
				StringBuilder adaptee = new StringBuilder();

				public Appendable append(CharSequence csq) throws IOException {
					append(csq, 0, csq.length());
					return this;
				}

				public Appendable append(char c) throws IOException {
					append(new String(new char[] { c }));
					return this;
				}

				public Appendable append(CharSequence csq, int start, int end) throws IOException {
					if (csq != null && csq.length() > 0) {
						if (adaptee.length() > 0) {
							adaptee.append('.');
						}
						adaptee.append(csq, start, end);
					}
					return this;
				}

				@Override
				public String toString() {
					return adaptee.toString();
				}
			};

			URI uri = new URI(nameSpace);

			String host = uri.getHost();
			if (host != null) {
				String[] hostParts = host.split("\\.");
				if (hostParts.length > 1) {
					List<String> hostPartsList = Arrays.asList(hostParts);
					Collections.reverse(hostPartsList);
					for (String hostPart : hostPartsList) {
						packageName.append(hostPart);
					}
				}
			}

			String path = uri.getPath();
			if (path != null) {
				String[] pathParts = path.split("/");
				for (String pathPart : pathParts) {
					packageName.append(pathPart);
				}
			}
			return packageName.toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
