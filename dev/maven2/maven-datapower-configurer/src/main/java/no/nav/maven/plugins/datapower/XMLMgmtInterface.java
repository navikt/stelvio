package no.nav.maven.plugins.datapower;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import no.nav.maven.plugins.datapower.command.DoImportCommand;
import no.nav.maven.plugins.datapower.config.ImportFormat;
import no.nav.maven.plugins.datapower.util.FileUtils;
import no.nav.maven.plugins.datapower.util.HttpUtils;
import no.nav.maven.plugins.datapower.util.StreamUtils;

public class XMLMgmtInterface {

	private final String host;
	private final String domain;
	private final String user;
	private final String password;
	
	public static class Builder {
		private final String host;
		private String domain;
		private String user;
		private String password;
		
		public Builder(final String host) { this.host = host; }		
		public Builder domain(final String domain) { this.domain = domain; return this; }		
		public Builder user(final String user) { this.user = user; return this; }
		public Builder password(final String password) { this.password = password; return this; }
		public XMLMgmtInterface build() { return new XMLMgmtInterface(this); }
	}
	
	private XMLMgmtInterface(Builder builder) {
		this.host = builder.host;
		this.domain = builder.domain;
		this.user = builder.user;
		this.password = builder.password;
	}
	
	public XMLMgmtRequest createRequest() {
		return new XMLMgmtRequest(domain);
	}
	
	public String importFiles(File source) {
		XMLMgmtRequest request = createRequest();
		System.out.println("Scanning archive for files and folders");
		try {
			FileUtils.scanFilesAndFolders(source, source, request);
			System.out.println("Connecting to datapower...");
			return doRequest(request);
		} catch (Exception e) {
			System.out.println("Error occured when importing files");
		}
		return null;
	}

	public String importConfig(String base64Config, ImportFormat format) {
		XMLMgmtRequest request = createRequest();
		DoImportCommand command = new DoImportCommand.Builder(format, base64Config).build();	
		request.addCommand(command);
		try {
			return doRequest(request);
		} catch (IOException e) {
			return "Failed to import " + format.toString() + " configuration!";
		}		
		//return format.toString() + "configuration imported successfully!";
	}
	
	public String importConfig(File configFile) {
		String path = configFile.getAbsolutePath();
		if (path.endsWith(".zip"))
			return importConfig(configFile, ImportFormat.ZIP);
		if (path.endsWith(".xcfg"))
			return importConfig(configFile, ImportFormat.XML);
		throw new IllegalArgumentException("File is not a valid DataPower configuration file format!");
	}
	
	public String importConfig(File configFile, ImportFormat format) {
		try {
			return importConfig(StreamUtils.getInputStreamAsString(new FileInputStream(configFile),true),format);
		} catch (IOException e) {
			return "Failed to import " + format.toString() + " configuration!";
		}		
	}
	
	public String importZIPConfig(File configFile) {
		return importConfig(configFile, ImportFormat.ZIP);
	}

	public String importXMLConfig(File configFile) {
		return importConfig(configFile, ImportFormat.XML);
	}

	private String doRequest(XMLMgmtRequest request) throws IOException {
		try {
			String content = request.toString();
			return HttpUtils.doPostRequest(host, user, password, content);
		} catch (IOException e) {
			System.out.println("XMLManagementConnection.doRequest(), caught IOException...");
			return "IOException occurred during HTTP request";
		}
	}	
}
