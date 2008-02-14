package no.nav.maven.plugins.datapower;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;

import no.nav.maven.plugins.datapower.command.CreateDirCommand;
import no.nav.maven.plugins.datapower.command.DoImportCommand;
import no.nav.maven.plugins.datapower.command.RestartThisDomainCommand;
import no.nav.maven.plugins.datapower.command.SaveConfigCommand;
import no.nav.maven.plugins.datapower.command.SetFileCommand;
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
	
	public String createDir(String path, DeviceFileStore location) throws IOException {
		XMLMgmtRequest request = createRequest();
		String[] dirs = path.split("/");
		String dirPath = ""; 
		for (int i = 0; i < dirs.length; i++) {
			if (dirPath.compareTo("") != 0)
				dirPath += "/";
			dirPath += dirs[i];
			request.addCommand(new CreateDirCommand(location,dirPath));
		}
		return doRequest(request);
	}
	
	public String importFiles(File source, DeviceFileStore location) throws IOException {
		XMLMgmtRequest request = createRequest();
		System.out.println("Scanning archive for files and folders");
		FileUtils.scanFilesAndFolders(source, source, request, location);
		System.out.println("Connecting to datapower...");
		return doRequest(request);
	}
	 
	public String importFiles(List fileList, DeviceFileStore location) throws IOException {
		XMLMgmtRequest request = createRequest();
		Iterator fileItr = fileList.iterator();
		while (fileItr.hasNext()) {
			File file = (File)fileItr.next();
			request.addCommand(new SetFileCommand(location, file.getName(), FileUtils.base64EncodeFile(file)));
		}
		return doRequest(request);
	}
	
	public String importFile(String fileName, String fileContent, DeviceFileStore location) throws IOException {
		XMLMgmtRequest request = createRequest();
		request.addCommand(new SetFileCommand(location, fileName, new String(Base64.encodeBase64(fileContent.getBytes("UTF-8")))));
		return doRequest(request);
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
	
	public String saveConfigAndRestartDomain() throws IOException {
		XMLMgmtRequest request = createRequest();
		request.addCommand( new SaveConfigCommand());
		request.addCommand(new RestartThisDomainCommand());
		return doRequest(request);
	}
	
	public String importZIPConfig(File configFile) {
		return importConfig(configFile, ImportFormat.ZIP);
	}

	public String importXMLConfig(File configFile) {
		return importConfig(configFile, ImportFormat.XML);
	}

	private String doRequest(XMLMgmtRequest request) throws IOException {
		String content = request.toString();
		try {
			return HttpUtils.doPostRequest(host, user, password, content, 10);
		} catch (IOException e) {
			System.out.println("Request:\n" + content);
			throw e;
		}
	}	
}
