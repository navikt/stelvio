package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.maven.plugin.MojoExecutionException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;

import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;
import no.nav.datapower.xmlmgmt.net.DPHttpClient;

public class XMLMgmtUtil {
	
	private static final Logger LOG = Logger.getLogger(XMLMgmtUtil.class);
	
	public static List<String> getCreateDirectoryList(File rootDir, DeviceFileStore location) throws IOException {
		List<File> directories = DPFileUtils.getFolderListExcludeRoot(rootDir);
		List<File> relativePaths = DPFileUtils.getRelativePathList(directories, rootDir);
		ArrayList<String> list = new ArrayList<String>();		
		for (File dirPath : relativePaths) {	
			String path = DPFileUtils.replaceSeparator(dirPath,'/');
			list.add(location.getDevicePath(path));	
			//list.add(path);	
		}
		return list;
	}		
	
	public static String getBase64EncodedFile(File file) {
		try {
			return getBase64EncodedString(FileUtils.readFileToString(file));
		} catch (IOException e) {
			return null;
		}
	}
	
	public static String getBase64EncodedString(String contentToEncode) {
		byte[] base64Config = Base64.encodeBase64(contentToEncode.getBytes());
		return new String(base64Config);
	}
	
	public static Map<String,String> getBase64EncodedFileSet(
												File source, DeviceFileStore location, String... excludes)
												throws IOException {
		
		List<File> fileList = DPFileUtils.getFileList(source);
		DPCollectionUtils.printLines(fileList, LOG, Level.DEBUG, "Found files: ");
		Map<String,String> map = DPCollectionUtils.newHashMap();
		for (File file : fileList) {
			if(!DPFileUtils.fileMatch(file, excludes)){
				String relativePath = DPFileUtils.getRelativePath(file, source).getPath();
				String dpPath = location.getDevicePath(relativePath);
				String base64EncodedFile = DPFileUtils.base64EncodeFile(file);
				map.put(dpPath, base64EncodedFile);			
			} 
		}
		return map; 
	}
	
	
	public static List<File> getDirectoriesToTraverse(File rootDirectory, String[] dirExcludes) throws IOException {	
		if (!rootDirectory.isDirectory())
			throw new IllegalArgumentException("Specified path '" + rootDirectory + "'is not a directory");
		File[] children = rootDirectory.listFiles((FileFilter)FileFilterUtils.directoryFileFilter());
		ArrayList<File> traverse = new ArrayList<File>();
		for (File child : children) {	
			if(!DPFileUtils.fileMatch(child, dirExcludes)){	
				traverse.add(child);
			} 
		}
		return traverse;		
	}
	
	
	public static XMLMgmtRequest mergeXMLElements(List<XMLMgmtRequest> list, String parentElementName) throws JDOMException{
		Iterator<XMLMgmtRequest> requests = list.iterator();
		XMLMgmtRequest merged = requests.next();
		while(requests.hasNext()){
			merged = XMLMgmtRequest.merge(merged, requests.next(), parentElementName);
		}
		return merged;
	}
	
	
	
	public static boolean allowCreateDirectory(File directory){
		DeviceFileStore childLocation = DeviceFileStore.fromString(directory.getName());
		DeviceFileStore location = childLocation != null ? childLocation : DeviceFileStore.LOCAL;
		if (location == DeviceFileStore.LOCAL) {
			//List<String> dirList = getCreateDirectoryList(directory, DeviceFileStore.LOCAL);
			return true;
		}
		return false;
	}
	
	
	
	public static void testImportFiles(File importDirectory, String dirExclusions, String fileExclusions) throws Exception{
		String[] dirExcludes = dirExclusions.split(",");
		String[] fileExcludes = fileExclusions.split(",");
		List<File> children = getDirectoriesToTraverse(importDirectory, dirExcludes);
		
		for (File child : children) {
			
			DeviceFileStore childLocation = DeviceFileStore.fromString(child.getName());
			DeviceFileStore location = childLocation != null ? childLocation : DeviceFileStore.LOCAL;
			if (location == DeviceFileStore.LOCAL) {
				List<String> dirList = getCreateDirectoryList(child, DeviceFileStore.LOCAL);
				
				DPCollectionUtils.printLines(dirList, System.out);
				
			}
			Map<String,String> map = getBase64EncodedFileSet(child,location, fileExcludes);
			
			DPCollectionUtils.printLines(map.keySet(), System.out);
			
			LOG.info("Importing files to device location '" + location + "'");	
			
		}
		
	}
	
	public static String wrapInSoapEnvelope(XMLMgmtRequest request){
		String ns ="http://schemas.xmlsoap.org/soap/envelope/";
		Element env = new Element("Envelope", Namespace.getNamespace("soapenv", ns));
		Element header = new Element("Header", env.getNamespace());
		Element body = new Element("Body", env.getNamespace());
		env.addContent(header);
		env.addContent(body);
		body.addContent(request.getNodeTree());	
		return XMLMgmtRequest.getXML(env);
		
	}
	
	public static void logToFileWithDate(File directory, String prefix,Date date, String extension , String content ) throws IOException  {			
		if(directory.isDirectory()){
			DateFormat format = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss-S");
			String dateTime = format.format(date);
			String filename = prefix + "-" + dateTime + extension;
			File file = new File(directory.getAbsolutePath() + "/" + filename);
			FileUtils.writeStringToFile(file, content);
			//LOG.info("Writing to log directory: [" + file.getName() + "]");
		} else {
			throw new IOException("Failed to log to file. " + directory + " is not a directory." );
		}
	}
	
	public static DPHttpClient createHttpClient(String clientClassName) {
		try {		
				Class clazz = Class.forName(clientClassName,
						true, Thread.currentThread().getContextClassLoader());

				if (DPHttpClient.class.isAssignableFrom(clazz)) {
					DPHttpClient client = (DPHttpClient) clazz
							.newInstance();
					return client;
				}
				return null;
			
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Could not create Http client. Class '" + clientClassName + "' not found", ex);
			
		} catch (InstantiationException ine) {
			throw new RuntimeException("Could not create Http client. Class '" + clientClassName + "' cannot be instantiated. It" +
							" is either an interface or abstract class.", ine);
			
		} catch (IllegalAccessException ile) {
			throw new RuntimeException("Could not create Http client. Could not create an instance of class '" + clientClassName + "'", ile);		
		}
	}
	
	
}
