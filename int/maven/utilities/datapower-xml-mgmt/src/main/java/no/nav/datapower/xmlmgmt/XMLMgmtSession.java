package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.util.DPHttpUtils;
import no.nav.datapower.xmlmgmt.command.AddHostAliasCommand;
import no.nav.datapower.xmlmgmt.command.AddStaticRouteCommand;
import no.nav.datapower.xmlmgmt.command.CreateDirCommand;
import no.nav.datapower.xmlmgmt.command.CreateDomainCommand;
import no.nav.datapower.xmlmgmt.command.DeleteDomainCommand;
import no.nav.datapower.xmlmgmt.command.DeleteFileCommand;
import no.nav.datapower.xmlmgmt.command.DeleteHSMKeyCommand;
import no.nav.datapower.xmlmgmt.command.DoImportCommand;
import no.nav.datapower.xmlmgmt.command.DoImportWithDeploymentPolicyCommand;
import no.nav.datapower.xmlmgmt.command.FlushDocumentCacheCommand;
import no.nav.datapower.xmlmgmt.command.GetFileCommand;
import no.nav.datapower.xmlmgmt.command.GetStatusCommand;
import no.nav.datapower.xmlmgmt.command.ImportHSMKeyCommand;
import no.nav.datapower.xmlmgmt.command.RemoveDirCommand;
import no.nav.datapower.xmlmgmt.command.RestartThisDomainCommand;
import no.nav.datapower.xmlmgmt.command.SaveConfigCommand;
import no.nav.datapower.xmlmgmt.command.SetFileCommand;
import no.nav.datapower.xmlmgmt.command.SynchronizeWSRRSubscriptionCommand;

public class XMLMgmtSession {
    final Logger log = LoggerFactory.getLogger(XMLMgmtSession.class);

    private final String MGMGT_INTERFACE_PATH_V3 = "service/mgmt/3.0";

    private final String host;
    private final String domain;
    private final String user;
    private final String password;

    public static class Builder {
        private final String host;
        private String domain;
        private String user;
        private String password;

        public Builder(final String host) {
            this.host = host;
        }

        public Builder domain(final String domain) {
            this.domain = domain;
            return this;
        }

        public Builder user(final String user) {
            this.user = user;
            return this;
        }

        public Builder password(final String password) {
            this.password = password;
            return this;
        }

        public XMLMgmtSession build() {
            return new XMLMgmtSession(this);
        }
    }

    private XMLMgmtSession(Builder builder) {
        host = builder.host;
        domain = builder.domain;
        user = builder.user;
        password = builder.password;
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
            if (dirPath.compareTo("") != 0) {
                dirPath += "/";
            }
            dirPath += dirs[i];

            File file = new File(dirPath);
            request.addCommand(new CreateDirCommand(location, Arrays.asList(file)));
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
            throw new XMLMgmtException("Failed to get folder list from '" + rootDir + "'", e);
        }
        List<File> relativePaths = DPFileUtils.getRelativePathList(directories, rootDir);
//		System.out.println("XMLMgmtSession.createDirs(), list of directories:");
//		IOUtils.writeLines(directories, IOUtils.LINE_SEPARATOR, System.out);
//        DPCollectionUtils.printLines(directories, log, Level.DEBUG, "Creating directory: ");

        log.debug("Creating directories: ");
        log.debug(directories.toString());

        request.addCommand(new CreateDirCommand(location, relativePaths));
        log.debug(request.toString());
        return doRequest(request);
    }

    public String removeDir(String path, DeviceFileStore location) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest();
        String[] dirs = path.split("/");
        String dirPath = "";
        for (int i = 0; i < dirs.length; i++) {
            if (dirPath.compareTo("") != 0) {
                dirPath += "/";
            }
            dirPath += dirs[i];
            request.addCommand(new RemoveDirCommand(location, dirPath));
        }
        return doRequest(request);
    }

    public String removeDirs(DeviceFileStore location, String... dirs) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest();
        for (String dir : dirs) {
            request.addCommand(new RemoveDirCommand(location, dir));
        }
        return doRequest(request);
    }

    public String deleteFile(String filename, DeviceFileStore location) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest();
        request.addCommand(new DeleteFileCommand(location, filename));
        return doRequest(request);
    }

    public String importFiles(File source, DeviceFileStore location) throws XMLMgmtException {
        List<File> fileList;
        try {
            fileList = DPFileUtils.getFileList(source);
        } catch (IOException e) {
            throw new XMLMgmtException("Failed to get file list from source '" + source + "'", e);
        }
//		System.out.println("XMLMgmtSession.importFiles(), list of files:");
//		IOUtils.writeLines(fileList, IOUtils.LINE_SEPARATOR, System.out);
//        DPCollectionUtils.printLines(fileList, LOG, Level.DEBUG, "Importing file: ");

        log.debug("Importing files: ");
        log.debug(fileList.toString());

        String response = importFiles(fileList, source, location);
        validateResult(extractResult(response));
        return response;
    }

    private String extractResult(String response) throws XMLMgmtException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new DatapowerManagementNamespaceContext());
        InputSource inputSource = new InputSource(new StringReader(response));
        try {
            String result = (String) xpath.evaluate("//" + DatapowerManagementNamespaceContext.DEFAULT_PREFIX + ":result", inputSource, XPathConstants.STRING);
            log.debug("Result returned from XML management call: " + result);
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

    private void validateCfgResult(String response) throws XMLMgmtException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc;
        InputSource inputSource = new InputSource(new StringReader(response));

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputSource);
        } catch (Exception e) {
            throw new XMLMgmtException("Failed during parsing of response '" + response + "'", e);
        }

        NodeList nList = doc.getElementsByTagName("cfg-result");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String objectStatusLogMessage = "Object of {class=\"" + eElement.getAttribute("class") + "\", name=\""
                        + eElement.getAttribute("name") + "\"} was imported with status: " + eElement.getAttribute("status") + "";

                if (!"SUCCESS".equals(eElement.getAttribute("status")) && !"skipped".equals(eElement.getAttribute("status"))) {
                    log.error(objectStatusLogMessage);
                    log.error("Message returned was: \"" + eElement.getTextContent() + "\"");
                    throw new XMLMgmtException("Object " + eElement.getAttribute("name")
                            + " was imported, but returned with the unexpected status: '"
                            + eElement.getAttribute("status")
                            + "'");
                } else {
                    log.debug(objectStatusLogMessage);
                }
            }
        }
    }

    /**
     * Deprecated; The new mgmt interface does not support importing of multiple files
     * 
     * @param fileList
     * @param root
     * @param location
     * @return
     * @throws XMLMgmtException
     */
    public String importFiles(List<File> fileList, File root, DeviceFileStore location) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest();
        try {
            for (File file : fileList) {
                String relativePath = DPFileUtils.getRelativePath(file, root).getPath();
                request.addCommand(new SetFileCommand(location, relativePath, DPFileUtils.base64EncodeFile(file)));
            }
        } catch (IOException e) {
            throw new XMLMgmtException("Failed to Base64 encode file data", e);
        }
        return doRequest(request);
    }

    /**
     * To support mgmgt interface v3 you need to import files one by one
     * Imports one file
     * 
     * @param file File ref to import
     * @param root Path
     * @param location Location on device
     * @return
     * @throws XMLMgmtException
     */
    public String importFile(File file, File root, DeviceFileStore location) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest();
        try {
            String relativePath = DPFileUtils.getRelativePath(file, root).getPath();
            request.addCommand(new SetFileCommand(location, relativePath, DPFileUtils.base64EncodeFile(file)));
        } catch (IOException e) {
            throw new XMLMgmtException("Failed to Base64 encode file data", e);
        }
        log.trace("ImportFile request: \n" + request);
        return doRequest(request);
    }

    public String importFile(String fileName, String fileContent, DeviceFileStore location) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest();
        try {
            request.addCommand(new SetFileCommand(location, fileName, new String(Base64.encodeBase64(fileContent.getBytes("UTF-8")))));
        } catch (UnsupportedEncodingException e) {
            throw new XMLMgmtException("Failed to Base64 encode file data", e);
        }
        return doRequest(request);
    }

    public String importConfig(String base64Config, ImportFormat format) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest();
        DoImportCommand command = new DoImportCommand.Builder(format, base64Config).build();
        request.addCommand(command);
        String response = doRequest(request);
        validateCfgResult(response);
//		validateResult(extractResult(response));
        return response;
    }

    public String importConfig(String base64Config, ImportFormat format, String deploymentPolicy) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest();
        DoImportWithDeploymentPolicyCommand command = new DoImportWithDeploymentPolicyCommand.Builder(format, base64Config, deploymentPolicy).build();
        request.addCommand(command);
        log.debug(request.toString());
        String response = doRequest(request, host + MGMGT_INTERFACE_PATH_V3);
        log.debug(response);
        validateCfgResult(response);
//      validateResult(extractResult(response));
        return response;
    }

    public String importConfig(File configFile) throws XMLMgmtException {
        String path = configFile.getAbsolutePath();
        if (path.endsWith(".zip")) {
            return importConfig(configFile, ImportFormat.ZIP);
        }
        if (path.endsWith(".xcfg")) {
            return importConfig(configFile, ImportFormat.XML);
        }
        throw new IllegalArgumentException("File is not a valid DataPower configuration file format!");
    }

    public String importConfig(File configFile, ImportFormat format) throws XMLMgmtException {
        try {
            byte[] base64Config = Base64.encodeBase64(FileUtils.readFileToString(configFile).getBytes());
            return importConfig(new String(base64Config), format);
        } catch (IOException e) {
            throw new XMLMgmtException("Failed to import " + format.toString() + " configuration!", e);
        }
    }

    public String importConfig(File configFile, ImportFormat format, String deploymentPolicy) throws XMLMgmtException {
        try {
            byte[] base64Config = Base64.encodeBase64(FileUtils.readFileToString(configFile).getBytes());
            return importConfig(new String(base64Config), format, deploymentPolicy);
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

    private String doRequest(XMLMgmtRequest request, String hostOverride) throws XMLMgmtException {
        String content = request.toString();
        try {
            log.debug("Request to host: {}", hostOverride);
            return DPHttpUtils.doPostRequest(hostOverride, user, password, content, 10);
        } catch (IOException e) {
            throw new XMLMgmtException("Post request failed, content:\n" + content, e);
        }
    }

    private String doRequest(XMLMgmtRequest request) throws XMLMgmtException {
        String content = request.toString();
        try {
            log.debug("Request to host: {}", host);
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

    public String getFileContent(DeviceFileStore location, final String fileName, final String domain) throws XMLMgmtException {
        String response = getFile(location, fileName, domain);
        return extractFileContent(response);
    }

    private String extractFileContent(String response) throws XMLMgmtException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new DatapowerManagementNamespaceContext());
        InputSource inputSource = new InputSource(new StringReader(response));
        try {
            String fileContent = (String) xpath.evaluate("//" + DatapowerManagementNamespaceContext.DEFAULT_PREFIX + ":file", inputSource, XPathConstants.STRING);
            log.debug("B64encoded file content returned from XML management call: " + fileContent);
            try {
                return new String(Base64.decodeBase64(fileContent.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                throw new XMLMgmtException("Failed to Base64 decode file data", e);
            }
        } catch (XPathExpressionException e) {
            throw new XMLMgmtException("Did not find expected element 'file' in response message", e);
        }
    }

    public String flushDocumentCache(final String XMLManager, final String domain) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest(domain);
        request.addCommand(new FlushDocumentCacheCommand(XMLManager));
        return doRequest(request);
    }

    public String deleteHSMKey(String keyHandle, String keyType) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest();
        request.addCommand(new DeleteHSMKeyCommand(keyHandle, keyType));
        return doRequest(request);
    }

    public String importHSMKey(String keyName, String keyFilePath, String keyPassword, boolean kWKExportable) throws XMLMgmtException {

        XMLMgmtRequest request = createRequest();
        request.addCommand(new ImportHSMKeyCommand(keyName, keyFilePath, keyPassword, kWKExportable));
        log.debug(request.toString());

        return doRequest(request);
    }

    public String synchronizeWSRRSubscription(String subscription) throws XMLMgmtException {
        XMLMgmtRequest request = createRequest();
        request.addCommand(new SynchronizeWSRRSubscriptionCommand(subscription));
        String response = doRequest(request);
        validateResult(extractResult(response));
        return response;
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
