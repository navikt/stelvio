package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.IOException;
import java.util.List;

import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPHttpUtils;
import no.nav.datapower.xmlmgmt.command.AddHostAliasCommand;
import no.nav.datapower.xmlmgmt.command.AddStaticRouteCommand;
import no.nav.datapower.xmlmgmt.command.CreateDirCommand;
import no.nav.datapower.xmlmgmt.command.CreateDomainCommand;
import no.nav.datapower.xmlmgmt.command.DeleteDomainCommand;
import no.nav.datapower.xmlmgmt.command.DoImportCommand;
import no.nav.datapower.xmlmgmt.command.RemoveDirCommand;
import no.nav.datapower.xmlmgmt.command.RestartThisDomainCommand;
import no.nav.datapower.xmlmgmt.command.SaveConfigCommand;
import no.nav.datapower.xmlmgmt.command.SetFileCommand;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class XMLMgmtSession {

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
		public XMLMgmtSession build() { return new XMLMgmtSession(this); }
	}
	
	private XMLMgmtSession(Builder builder) {
		this.host = builder.host;
		this.domain = builder.domain;
		this.user = builder.user;
		this.password = builder.password;
	}
	
	public XMLMgmtRequest createRequest() {
		return createRequest(domain);		
	}

	public XMLMgmtRequest createRequest(String domain) {
		return new XMLMgmtRequest(domain);		
	}
	
	private XMLMgmtRequest createRequestToDefaultDomain() {
		return createRequest("default");		
	}
	
	public String createDomain(final String domainToCreate) throws IOException {
		XMLMgmtRequest request = createRequestToDefaultDomain();
		request.addCommand(new CreateDomainCommand(domainToCreate));
		return doRequest(request);
	}
	
	public String deleteDomain(final String domainToDelete) throws IOException {
		XMLMgmtRequest request = createRequestToDefaultDomain();
		request.addCommand(new DeleteDomainCommand(domainToDelete));
		return doRequest(request);
	}
	
	public String addHostAlias(final String alias, final String ipAddress) throws IOException {
		XMLMgmtRequest request = createRequestToDefaultDomain();
		request.addCommand(new AddHostAliasCommand(alias, ipAddress));
		return doRequest(request);		
	}

	public String addStaticRoute(final String ethInterface, final String destination, final String gateway) throws IOException {
		XMLMgmtRequest request = createRequestToDefaultDomain();
		request.addCommand(new AddStaticRouteCommand(ethInterface, destination, gateway));
		return doRequest(request);		
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
	
	public String createDirs(File rootDir, DeviceFileStore location) throws IOException {
		XMLMgmtRequest request = createRequest();
		System.out.println("Scanning archive for files and folders");
		List<File> directories = DPFileUtils.getFolderListExcludeRoot(rootDir);
		List<File> relativePaths = DPFileUtils.getRelativePathList(directories, rootDir);
		IOUtils.writeLines(directories, IOUtils.LINE_SEPARATOR, System.out);
		for (File dirPath : relativePaths) {
			request.addCommand(new CreateDirCommand(location,DPFileUtils.replaceSeparator(dirPath,'/')));			
		}
		return doRequest(request);
	}
	
	public String removeDir(String path, DeviceFileStore location) throws IOException {
		XMLMgmtRequest request = createRequest();
		String[] dirs = path.split("/");
		String dirPath = ""; 
		for (int i = 0; i < dirs.length; i++) {
			if (dirPath.compareTo("") != 0)
				dirPath += "/";
			dirPath += dirs[i];
			request.addCommand(new RemoveDirCommand(location,dirPath));
		}
		return doRequest(request);
	}

	public String removeDirs(DeviceFileStore location, String... dirs) throws IOException {
		XMLMgmtRequest request = createRequest();
		for (String dir : dirs) {
			request.addCommand(new RemoveDirCommand(location,dir));
		}
		return doRequest(request);
	}
	
	public String importFiles(File source, DeviceFileStore location) throws IOException {
		List<File> fileList = DPFileUtils.getFileList(source);
		IOUtils.writeLines(fileList, IOUtils.LINE_SEPARATOR, System.out);
		return importFiles(fileList, source, location);
	}
	 
	public String importFiles(List<File> fileList, File root, DeviceFileStore location) throws IOException {
		XMLMgmtRequest request = createRequest();
		for (File file : fileList) {
			String relativePath = DPFileUtils.getRelativePath(file, root).getPath();
			request.addCommand(new SetFileCommand(location, relativePath, DPFileUtils.base64EncodeFile(file)));			
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
			//return importConfig(DPStreamUtils.getInputStreamAsString(new FileInputStream(configFile),true),format);
			return importConfig(FileUtils.readFileToString(configFile),format);
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
			return DPHttpUtils.doPostRequest(host, user, password, content, 10);
		} catch (IOException e) {
			System.out.println("Request:\n" + content);
			throw e;
		}
	}	
//	private void scanFilesAndFolders(File root, File parent, XMLMgmtRequest request, DeviceFileStore location) throws IOException {
//		File[] children = parent.listFiles();
//		if (children != null) {
//			for (File child : children) {
//				if (child.isDirectory()) {
//					String folderPath = DPFileUtils.getRelativePath(child, root);
//					System.out.println("Folder path = " + folderPath);
//					request.addCommand(new CreateDirCommand(location, folderPath));
//					scanFilesAndFolders(root, child, request, location);
//				} else if (child.getName().toLowerCase().compareTo("manifest.mf") != 0) {
//					String filePath = DPFileUtils.getRelativePath(child, root);
//					System.out.println("File path = " + filePath);
//					request.addCommand(new SetFileCommand(location, filePath, DPFileUtils.base64EncodeFile(child)));
//				}
//			}
//		}
//	}
		
//	private void scanFilesAndFolders(File root, File parent, XMLMgmtRequest request, DeviceFileStore location) throws IOException {
//		File[] children = parent.listFiles();
//
//		if (children != null) {
//			for (File child : children) {
//				if (child.isDirectory()) {
//					String folderPath = FileUtils.getRelativePath(child, root);
//					System.out.println("Folder path = " + folderPath);
//					request.addCommand(new CreateDirCommand(location, folderPath));
//					scanFilesAndFolders(root, child, request, location);
//				} else if (child.getName().toLowerCase().compareTo("manifest.mf") != 0) {
//					String filePath = FileUtils.getRelativePath(child, root);
//					System.out.println("File path = " + filePath);
//					request.addCommand(new SetFileCommand(location, filePath, FileUtils.base64EncodeFile(child)));
//				}
//			}
//		}
//	}
}
