package no.nav.maven.plugins;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.plexus.util.IOUtil;

/**
 * TODO: Rewrite this class using WSDL4J instead of RegEx
 */
public class NamespaceToPackageMappingGenerator {
	private String encoding;
	private boolean removeVersionInfo;
	private boolean useLowerCasePackageName;

	private Pattern namespacePattern = Pattern.compile("\"http://([^\"]+)\"");

	public NamespaceToPackageMappingGenerator() {
		this(System.getProperty("file.encoding"));
	}

	public NamespaceToPackageMappingGenerator(String encoding) {
		this.encoding = encoding;
	}

	public Properties createNamespaceToPackageMappingFromWSDLDirectory(File wsdlDirectory) {
		Properties mapping = new Properties();
		try {
			createNamespaceToPackageMapping(wsdlDirectory, mapping);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return mapping;
	}
	
	public void setRemoveVersionInfo(boolean removeVersionInfo){
		this.removeVersionInfo = removeVersionInfo;
	}
	
	public void setUseLowerCasePackageName(boolean useLowerCasePackageName){
		this.useLowerCasePackageName = useLowerCasePackageName;
	}

	private void createNamespaceToPackageMapping(File file, Properties mapping) throws IOException {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				createNamespaceToPackageMapping(f, mapping);
			}
		} else {
			String fileString = readFileToString(file);
			Matcher m = namespacePattern.matcher(fileString);
			while (m.find()) {
				String namespace = "http://" + m.group(1);
				if (!mapping.containsKey(namespace)) {
					String packageName = generatePackageNameFromNamespace(namespace);
					if (packageName != null) {
						mapping.put(namespace, packageName);
					}
				}
			}
		}
	}

	private String readFileToString(File file) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			return IOUtil.toString(inputStream, encoding);
		} finally {
			IOUtil.close(inputStream);
		}
	}

	private String generatePackageNameFromNamespace(String namespace) {
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

			URI uri = new URI(namespace);

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
				
				//remove version part if removeVersionInfo flag is set
				if(removeVersionInfo){
					if(pathParts.length > 0 && isVersionPart(pathParts[pathParts.length-1])){
						pathParts[pathParts.length-1] = "";
					}
				
					//check for WS port artifact
					if(pathParts.length > 0 && isBindingPart(pathParts[pathParts.length-1])){
						//remove version info
						if(pathParts.length > 1 && isVersionPart(pathParts[pathParts.length-2])){
							pathParts[pathParts.length-2] = "";
						}
					}
				}
				
				//convert Binding part to lower case if useLowerCasePackageName is set
				if(pathParts.length > 0 && isBindingPart(pathParts[pathParts.length-1]) && useLowerCasePackageName){
					pathParts[pathParts.length-1] = pathParts[pathParts.length-1].toLowerCase();
				}
									
				
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
	
	private boolean isVersionPart(String pathPart){
		if("v".equals(pathPart.substring(0,1))){
			try{
				Integer.parseInt(pathPart.substring(1));
				return true;
			} catch (NumberFormatException nfe){
				return false;
			}			
		}
		return false;
	}
	
	private boolean isBindingPart(String pathPart){
		if("Binding".equals(pathPart)){
			return true;
		}
		else{
			return false;
		}

	}
	
}
