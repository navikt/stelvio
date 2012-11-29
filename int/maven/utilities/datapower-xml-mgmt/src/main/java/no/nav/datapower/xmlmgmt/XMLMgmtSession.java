package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPHttpUtils;
import no.nav.datapower.xmlmgmt.command.AddHostAliasCommand;
import no.nav.datapower.xmlmgmt.command.AddStaticRouteCommand;
import no.nav.datapower.xmlmgmt.command.CreateDirCommand;
import no.nav.datapower.xmlmgmt.command.CreateDomainCommand;
import no.nav.datapower.xmlmgmt.command.DeleteDomainCommand;
import no.nav.datapower.xmlmgmt.command.DeleteFileCommand;
import no.nav.datapower.xmlmgmt.command.DoImportCommand;
import no.nav.datapower.xmlmgmt.command.GetStatusCommand;
import no.nav.datapower.xmlmgmt.command.RemoveDirCommand;
import no.nav.datapower.xmlmgmt.command.RestartThisDomainCommand;
import no.nav.datapower.xmlmgmt.command.SaveConfigCommand;
import no.nav.datapower.xmlmgmt.command.SetFileCommand;
import no.nav.datapower.xmlmgmt.command.GetFileCommand;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

public class XMLMgmtSession {

	private static final Logger LOG = Logger.getLogger(XMLMgmtSession.class);
	
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
	
	public String createDomain(final String domainToCreate) throws XMLMgmtException {
		XMLMgmtRequest request = createRequestToDefaultDomain();
		request.addCommand(new CreateDomainCommand(domainToCreate));
		return doRequest(request);
	}
	
	public String deleteDomain(final String domainToDelete) throws XMLMgmtException {
		XMLMgmtRequest request = createRequestToDefaultDomain();
		request.addCommand(new DeleteDomainCommand(domainToDelete));
		return doRequest(request);
	}
	
	public String addHostAlias(final String alias, final String ipAddress) throws XMLMgmtException {
		XMLMgmtRequest request = createRequestToDefaultDomain();
		request.addCommand(new AddHostAliasCommand(alias, ipAddress));
		return doRequest(request);		
	}

	public String addStaticRoute(final String ethInterface, final String destination, final String gateway) throws XMLMgmtException {
		XMLMgmtRequest request = createRequestToDefaultDomain();
		request.addCommand(new AddStaticRouteCommand(ethInterface, destination, gateway));
		return doRequest(request);		
	}

	public String createDir(String path, DeviceFileStore location) throws XMLMgmtException {
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
	
	public String createDirs(File rootDir, DeviceFileStore location) throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
//		System.out.println("Scanning archive for files and folders");
		List<File> directories;
		try {
			directories = DPFileUtils.getFolderListExcludeRoot(rootDir);
		} catch (IOException e) {
			throw new XMLMgmtException("Failed to get folder list from '" + rootDir + "'",e);
		}
		List<File> relativePaths = DPFileUtils.getRelativePathList(directories, rootDir);
//		System.out.println("XMLMgmtSession.createDirs(), list of directories:");
//		IOUtils.writeLines(directories, IOUtils.LINE_SEPARATOR, System.out);
		DPCollectionUtils.printLines(directories, LOG, Level.DEBUG, "Creating directory: ");
		for (File dirPath : relativePaths) {
			request.addCommand(new CreateDirCommand(location,DPFileUtils.replaceSeparator(dirPath,'/')));			
		}
		return doRequest(request);
	}
	
	public String removeDir(String path, DeviceFileStore location) throws XMLMgmtException {
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

	public String removeDirs(DeviceFileStore location, String... dirs) throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
		for (String dir : dirs) {
			request.addCommand(new RemoveDirCommand(location,dir));
		}
		return doRequest(request);
	}
	
	public String deleteFile(String filename, DeviceFileStore location) throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
		request.addCommand(new DeleteFileCommand(location,filename));
		return doRequest(request);
	}
	
	public String importFiles(File source, DeviceFileStore location) throws XMLMgmtException {
		List<File> fileList;
		try {
			fileList = DPFileUtils.getFileList(source);
		} catch (IOException e) {
			throw new XMLMgmtException("Failed to get file list from source '" + source + "'",e);
		}
//		System.out.println("XMLMgmtSession.importFiles(), list of files:");
//		IOUtils.writeLines(fileList, IOUtils.LINE_SEPARATOR, System.out);
		DPCollectionUtils.printLines(fileList, LOG, Level.DEBUG, "Importing file: ");
		String response = importFiles(fileList, source, location);
		validateResult(extractResult(response));
		return response;
	}

	private String extractResult(String response) throws XMLMgmtException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new DatapowerManagementNamespaceContext());
		InputSource inputSource = new InputSource(new StringReader(response));
		try {
			String result = (String) xpath.evaluate("//"+DatapowerManagementNamespaceContext.DEFAULT_PREFIX+":result", inputSource, XPathConstants.STRING);
			LOG.debug("Result returned from XML management call: " + result);
			return result;
		} catch (XPathExpressionException e) {
			throw new XMLMgmtException("Did not find expected element 'result' in response message", e);
		}
	}
	 
	private void validateResult(String result) throws XMLMgmtException {
		if (!"OK".equals(result.trim())) {
			throw new XMLMgmtException("Expected result 'OK', but received: '" + result + "'");
		}
	}

	public String importFiles(List<File> fileList, File root, DeviceFileStore location) throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
		try {
			for (File file : fileList) {
				String relativePath = DPFileUtils.getRelativePath(file, root).getPath();
				request.addCommand(new SetFileCommand(location, relativePath, DPFileUtils.base64EncodeFile(file)));			
			}
		} catch (IOException e) {
			throw new XMLMgmtException("Failed to Base64 encode file data",e);
		}
		return doRequest(request);
	}
	
	public String importFile(String fileName, String fileContent, DeviceFileStore location) throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
		try {
			request.addCommand(new SetFileCommand(location, fileName, new String(Base64.encodeBase64(fileContent.getBytes("UTF-8")))));
		} catch (UnsupportedEncodingException e) {
			throw new XMLMgmtException("Failed to Base64 encode file data",e);
		}
		return doRequest(request);
	}

	public String importConfig(String base64Config, ImportFormat format) throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
		DoImportCommand command = new DoImportCommand.Builder(format, base64Config).build();	
		request.addCommand(command);
		String response = doRequest(request);
//		validateResult(extractResult(response));
		return response;
	}
	
	public String importConfig(File configFile) throws XMLMgmtException {
		String path = configFile.getAbsolutePath();
		if (path.endsWith(".zip"))
			return importConfig(configFile, ImportFormat.ZIP);
		if (path.endsWith(".xcfg"))
			return importConfig(configFile, ImportFormat.XML);
		throw new IllegalArgumentException("File is not a valid DataPower configuration file format!");
	}
	
	public String importConfig(File configFile, ImportFormat format) throws XMLMgmtException {
		try {
			byte[] base64Config = Base64.encodeBase64(FileUtils.readFileToString(configFile).getBytes());
			return importConfig(new String(base64Config),format);
		} catch (IOException e) {
			throw new XMLMgmtException("Failed to import " + format.toString() + " configuration!", e);
		}
	}
	
	public String saveConfigAndRestartDomain() throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
		request.addCommand(new SaveConfigCommand());
		request.addCommand(new RestartThisDomainCommand());
		return doRequest(request);
	}

	public String saveConfig() throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
		request.addCommand(new SaveConfigCommand());
		String response = doRequest(request);
		validateResult(extractResult(response));
		return response;
	}

	public String restartDomain() throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
		request.addCommand(new RestartThisDomainCommand());
		String response = doRequest(request);
		validateResult(extractResult(response));
		return response;
	}

	
	public String importZIPConfig(File configFile) throws XMLMgmtException {
		return importConfig(configFile, ImportFormat.ZIP);
	}

	public String importXMLConfig(File configFile) throws XMLMgmtException {
		return importConfig(configFile, ImportFormat.XML);
	}
	
	public String getStatus() throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
		request.addCommand(new GetStatusCommand());
		String response = doRequest(request);
		return response;
	}
	
	public String getStatus(String objectClass) throws XMLMgmtException {
		XMLMgmtRequest request = createRequest();
		request.addCommand(new GetStatusCommand(objectClass));
		String response = doRequest(request);
		return response;
	}

	private String doRequest(XMLMgmtRequest request) throws XMLMgmtException {
		String content = request.toString();
		try {
			return DPHttpUtils.doPostRequest(host, user, password, content, 10);
		} catch (IOException e) {
			throw new XMLMgmtException("Post request failed, content:\n" + content, e);
		}
	}
	
	public String getFile(DeviceFileStore location, final String fileName, final String domain) throws XMLMgmtException {
		XMLMgmtRequest request = createRequest(domain);
		request.addCommand(new GetFileCommand(location, fileName));
		return doRequest(request);
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
