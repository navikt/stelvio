package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPFileUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.jdom.Element;

import junit.framework.TestCase;

public class XMLMgmtRequestTest extends TestCase {

	
	private class DoImport{
		
		private XMLMgmtRequest request;
		private String xml;
		
		private class Keys{
			private static final String DOMAIN = "/request/@domain";
			private static final String DEPLOYMENT_POLICY =  "/request/do-import/@deployment-policy";
			private static final String SOURCE_TYPE= "/request/do-import/@source-type";
			private static final String INPUT_FILE = "/request/do-import/input-file";
		}
		public DoImport() throws Exception{
			xml = "src/main/resources/xmlmgmt-commands/do-import.xml";
			request = new XMLMgmtRequest(xml);
		}
		
		public Properties getOverrides(){
			Properties overrides = new Properties();;
			overrides.setProperty(Keys.DOMAIN, "testdomain");
			overrides.setProperty(Keys.DEPLOYMENT_POLICY, "policy");
			overrides.setProperty(Keys.SOURCE_TYPE, "xml");
			overrides.setProperty(Keys.INPUT_FILE, "Somebase64encodedContent");
			overrides.setProperty("/request/do-import/no-element/another-element/@name", "hey");
			return overrides;
		}
		
		public void execute() throws Exception {
			Properties p = getOverrides();
			request.updateNodeTree(p);
			System.out.println("--------------------------------------------------------");
			System.out.println(this.getClass());
			System.out.println("--------------------------------------------------------");
			System.out.println(request.toString());
			
		}
		
	}
	
	private class DoActionCreateDir{
		
		//private XMLMgmtRequest request;
		private String xml;
		
		private class Keys{
			private static final String DOMAIN = "/request/@domain";
			private static final String DIR =  "/request/do-action/CreateDir/Dir";
		}
		public DoActionCreateDir() throws Exception{
			xml = "src/main/resources/xmlmgmt-commands/do-action.xml";
			//request = new XMLMgmtRequest(xml);
		}
		
		public Properties getOverrides(){
			Properties overrides = new Properties();;
			overrides.setProperty(Keys.DOMAIN, "testdomain");
			return overrides;
		}
		
		public void execute() throws Exception {
			
			
			
			
			//XMLMgmtRequest request = createDirs(new File("src/main/resources/"), DeviceFileStore.LOCAL);
			
			List<XMLMgmtRequest> list = createDirList(new File("src/main/resources/"), DeviceFileStore.LOCAL);
			//XMLMgmtRequest req1 = list.get(0);
			//XMLMgmtRequest req2 = list.get(1);
			
			
			System.out.println("--------------------------------------------------------");
			System.out.println(this.getClass());
			System.out.println("--------------------------------------------------------");
			//System.out.println("req1 - " + req1.toString());
			//System.out.println("req2- " + req2.toString());
			
			//List<XMLMgmtRequest> list = createDirs(new File("src/main/resources/"), DeviceFileStore.LOCAL);
			
			Iterator<XMLMgmtRequest> requests = list.iterator();
			XMLMgmtRequest merged = requests.next();
			while(requests.hasNext()){
				merged = XMLMgmtRequest.merge(merged, requests.next(), "do-action");
			}
			
			System.out.println("MERGED - " + merged.toString());
			
		}
		
		public XMLMgmtRequest createDirs(File rootDir, DeviceFileStore location) throws Exception {
			//XMLMgmtRequest request = createRequest();
//			System.out.println("Scanning archive for files and folders");
			List<File> directories;
			try {
				directories = DPFileUtils.getFolderListExcludeRoot(rootDir);
			} catch (IOException e) {
				throw new XMLMgmtException("Failed to get folder list from '" + rootDir + "'",e);
			}
			List<File> relativePaths = DPFileUtils.getRelativePathList(directories, rootDir);

			String[] paths = new String[relativePaths.size()];
			ArrayList<XMLMgmtRequest> list = new ArrayList<XMLMgmtRequest>();
			XMLMgmtRequest baseRequest = new XMLMgmtRequest(xml);
			Properties p = getOverrides();
			int i = 1;
			for (File dirPath : relativePaths) {	
				String path = DPFileUtils.replaceSeparator(dirPath,'/');
				System.out.println("Path:" + path);		
				
				p.setProperty("/request/do-action/CreateDir[" + i + "]/Dir" , path);
				i++;
				//p.setProperty(Keys.DIR, path);
				//XMLMgmtRequest request = baseRequest.updateNodeTree(p);
				//list.add(request);	
			}
			
			baseRequest.updateNodeTree(p);
			return baseRequest;
			
		}		
		
		public List<XMLMgmtRequest> createDirList(File rootDir, DeviceFileStore location) throws Exception {
			//XMLMgmtRequest request = createRequest();
//			System.out.println("Scanning archive for files and folders");
			List<File> directories;
			try {
				directories = DPFileUtils.getFolderListExcludeRoot(rootDir);
			} catch (IOException e) {
				throw new XMLMgmtException("Failed to get folder list from '" + rootDir + "'",e);
			}
			List<File> relativePaths = DPFileUtils.getRelativePathList(directories, rootDir);

			String[] paths = new String[relativePaths.size()];
			ArrayList<XMLMgmtRequest> list = new ArrayList<XMLMgmtRequest>();
			XMLMgmtRequest baseRequest = new XMLMgmtRequest(xml);
			for (File dirPath : relativePaths) {	
				
				String path = DPFileUtils.replaceSeparator(dirPath,'/');
				System.out.println("Path:" + path);
				Properties p = getOverrides();		
				p.setProperty(Keys.DIR, path);		
				XMLMgmtRequest request = baseRequest.updateNodeTree(p);
				list.add(request);	
			}
			return list;
		}		
	}
	
	
	
	
	protected void setUp() throws Exception {
		super.setUp();
		
	}
	
	
	public void testCreateXPathFromKey() throws Exception{
		
		/*DoImport doImport = new DoImport();
		doImport.execute();*/
		
		/*DoActionCreateDir dir = new DoActionCreateDir();
		dir.execute();*/
		/*File importDirectory = new File("E:/wsDatapower/nav-mottak-signaturegw-datapower/target/classes/domain-files/");
		String dirExclusions = "";
		String fileExclusions = "";
		XMLMgmtUtil.testImportFiles(importDirectory, dirExclusions, fileExclusions);
		*/
		
		
	}

}
