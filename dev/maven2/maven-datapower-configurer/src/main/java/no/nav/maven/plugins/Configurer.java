package no.nav.maven.plugins;  

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


/**
 * Goal which touches a timestamp file.
 *
 * @goal configure
 */
public class Configurer
extends AbstractMojo
{
	/**
	 * Location of the file.
	 * @parameter expression="${outDir}"
	 * @required
	 */
	private File outputDirectory = new File("E:\\maven-plugins\\maven-datapower-configurer\\target");
	
	/**
	 * Location of the file.
	 * @parameter expression="${wsdl.zip}"
	 * @required
	 */
	private File wsdlFile = new File("E:/deploy_scripts/kjempen/target/classes/builds/wsdl/wsdl-pselv.zip");
	
	/**
	 * Location of the file.
	 * @parameter expression="${template}"
	 * @required
	 */
	private File template = new File("E:/maven-plugins/maven-datapower-configurer/src/main/resources/template.xcfg");
	
	/**
	 * Location of the file.
	 * @parameter expression="${environment}"
	 * @required
	 */
	private File environment = new File("E:\\maven-plugins\\maven-datapower-configurer\\src\\main\\resources\\environments\\Systemtest2.properties");
	
	/**
	 * Location of the file.
	 * @parameter expression="${host}"
	 * @required
	 */
	private String host = "https://secgw-01.utv.internsone.local:5550";
	
	/**
	 * Location of the file.
	 * @parameter expression="${user}"
	 * @required
	 */
	private String user = "petterasskildt";
	
	/**
	 * Location of the file.
	 * @parameter expression="${password}"
	 * @required
	 */
	private String password = "gy59inku";
	
	/**
	 * Private variables
	 */
	private File tmpFolder, configFile;
	
	private Document doc;
	
	private String templateContent;
	
	private XPath xpath;
	
	private Properties env;
	
	private int retries = 0;
	
	private static char[] map1 = new char[64];
	   static {
	      int i=0;
	      for (char c='A'; c<='Z'; c++) map1[i++] = c;
	      for (char c='a'; c<='z'; c++) map1[i++] = c;
	      for (char c='0'; c<='9'; c++) map1[i++] = c;
	      map1[i++] = '+'; map1[i++] = '/'; }
	
	public void execute() throws MojoExecutionException
	{
		tmpFolder = new File(outputDirectory.getAbsolutePath() + "/" + new Date().getTime());
		
		try {
			
			readTemplate();
			
			readEnvironment();
			
			extract();
			
			generateDataPowerConfig();
		
			writeConfig();
			
			uploadFilesAndFolders(tmpFolder);
			
			cleanUp(tmpFolder);
		} catch (DocumentException e) {
			throw new MojoExecutionException("",e);
		} catch (IOException e) {			
			throw new MojoExecutionException("Error setting up datapower configuration", e);
		}
	}
	
	private void extract() throws IOException
	{
		getLog().info("Extracting wsdl archive...");
		tmpFolder.mkdirs();
		ZipUtils.extract2(wsdlFile,tmpFolder);
		getLog().info("Done!");
	}
	
	private void readTemplate() throws DocumentException, IOException{
		getLog().info("Reading template file...");
		
		BufferedReader reader = new BufferedReader(new FileReader(template));
		templateContent = new String();
		String line = null;
		while((line = reader.readLine()) != null){
			templateContent += line + System.getProperty("line.separator");
		}
		reader.close();
		getLog().info("Done!");
	}

	private void readEnvironment() throws IOException{
		env = new Properties();
		env.load(new FileInputStream(environment));
	}
	
	private void generateDataPowerConfig() throws DocumentException{
		String key = null;
		Enumeration enum = null;
		
		getLog().info("Merging " + environment.getName() + " with template configuration...");
		enum = env.keys();
		getLog().info("Merging " + env.size() + " variables");
		while(enum.hasMoreElements()){
			key = enum.nextElement().toString();
			templateContent = replace(templateContent,"${" + key + "}",env.get(key).toString());
			getLog().info(key + " => " + env.get(key));
		}
		getLog().info("Done!");
	}
	
	private String replace(String source, String pattern, String replace){
		String retval = "";
		int startIndex, endIndex;
		if (source.indexOf(pattern) >= 0){
			startIndex = source.indexOf(pattern);
			endIndex = startIndex + pattern.length();
			retval += source.substring(0,startIndex);
			retval += replace;
			retval += source.substring(endIndex);
			return retval;
		}
		return source;
	}
	
	private String getRequest(Element elem){
		String ret = null;
		ret = 	"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:man=\"http://www.datapower.com/schemas/management\">\n" +
				"<soapenv:Header/>\n" +
				"<soapenv:Body>" + 
				"<man:request domain=\"" + env.get("domain") + "\">";
		ret += elementToString(elem);
		ret += 	"</man:set-config>\n" +
				"</man:request>\n" +
				"</soapenv:Body>\n" +
				"</soapenv:Envelope>\n";
		return ret;
	}
	
	private String elementToString(Element elem){
		Attribute attrib = null;
		Iterator iter = null;
		String retval = null;
		
		retval = "<";
		retval += elem.getName() + " ";
		iter = elem.attributeIterator();
		while(iter.hasNext()){
			attrib = (Attribute)iter.next();
			retval += attrib.getName() + "=\"" + attrib.getValue() + "\" ";
		}
		retval += ">\n";
		iter = elem.elementIterator();
		while(iter.hasNext()){
			retval += elementToString((Element)iter.next());
		}
		retval += "</" + elem.getName() + ">\n";
		return retval;
	}
	
	private void uploadFilesAndFolders(File root) throws IOException{
		List folders = new ArrayList();
		List files = new ArrayList();
		DataPowerRequest request, request2;
		Iterator iter;
		String AuthString = "Basic " + new Base64Codec(null,"").encodeBase64(user + ":" + password);
		scanFilesAndFolders(tmpFolder, folders, files);
		
		//Creating folders
		request = new DataPowerRequest();
		request.setContent(getSOAPEnvelope_mkdir(folders));
		request.getHTTPHeader().put("Authorization",AuthString);
		getLog().info("Creating the following remote folders:");
		iter = folders.iterator();
		while(iter.hasNext()) getLog().info(iter.next().toString());
		sendHTTPSRequest(request);
		
		//Uploading files
		request2 = new DataPowerRequest();
		request2.setContent(getSOAPEnvelope_setfile(files));
		request2.getHTTPHeader().put("Authorization",AuthString);
		getLog().info("Uploading following files to datapower");
		iter = files.iterator();
		while(iter.hasNext()) getLog().info(getSubPath(new File(iter.next().toString())));
		sendHTTPSRequest(request2);
		
		//importing config file
		request = new DataPowerRequest();
		request2.setContent(getSOAPEnvelope_doimport(configFile));
		request2.getHTTPHeader().put("Authorization",AuthString);
		getLog().info("Importing merged configuration file to datapower...");
		sendHTTPSRequest(request2);
		getLog().info("Done!");
		getLog().info("");
		getLog().info("");
		getLog().info("All operations successful!");
	}
	
	private void scanFilesAndFolders(File root, List folders, List files){
		File[] children = root.listFiles();
		File child;
		String path;
		
		for (int i = 0; i < children.length; i++) {
			child = children[i];
			if(child.isDirectory()){
				folders.add("local:///" + getSubPath(child));
				scanFilesAndFolders(child, folders, files);
			}else{
				if(child.getName().toLowerCase().compareTo("manifest.mf") != 0) files.add(child);
			}
		}
	}
	
	private void sendHTTPSRequest(DataPowerRequest request) throws IOException{
		HttpURLConnection conn = null;
		URL gatewayURL = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] buf;
		StringBuffer response = null;
		int character;
		
		 
		try {
			gatewayURL = new URL(host);
			
			getLog().info("Connecting to datapower...");
			conn = (HttpURLConnection)gatewayURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			Enumeration enum = request.getHTTPHeader().keys();
			while(enum.hasMoreElements()){
				String key = enum.nextElement().toString();
				conn.setRequestProperty(key,request.getHTTPHeader().get(key).toString());
			}
			
			conn.connect();
			getLog().info("Connected");
			
			
			getLog().info("Sending request...");
			out = conn.getOutputStream();
			
			out.write(request.getContent().getBytes("UTF-8"));

			in = new BufferedInputStream(conn.getInputStream(),32 * 1024);
			response = new StringBuffer();
			buf = new byte[4096];
			
			int status = 0;
			while((in.read(buf) != -1)){
				response.append(new String(buf));
			}
		} catch (IOException e) {
			if(retries++ < 10){
				getLog().warn(e.getMessage() + ", retrying " + retries + "/10");
				sendHTTPSRequest(request);
			}else {
				getLog().error(e.getMessage());
				throw e;
			}	
		}
		
		
		if(response.indexOf("<dp:result>OK</dp:result>") < 0){ //failure
			getLog().error("Server error message:\n\n" + response);
		}else getLog().info("Request successfull!\n");
		
		in.close();
		out.close();
		conn.disconnect();
	}
	
	private String getSOAPEnvelope_mkdir(List folders){
		String env = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:man=\"http://www.datapower.com/schemas/management\">\n" +
				"<soapenv:Header/>\n" +
				"<soapenv:Body>\n" +
					"<man:request domain=\"test-config\">\n" +
						"<man:do-action>\n";
		
		Iterator iter = folders.iterator();
		while(iter.hasNext()){
			env += 	"<CreateDir>\n" +
			"<Dir>" + iter.next().toString() + "</Dir>\n" +
			"</CreateDir>\n";
		}
		env +=	"</man:do-action>\n" +
					"</man:request>\n" +
				"</soapenv:Body>\n" +
			"</soapenv:Envelope>";
		return env;
	}
	
	private String getSOAPEnvelope_setfile(List files) throws IOException{
		String base64;
		String env = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:man=\"http://www.datapower.com/schemas/management\">\n" +
		   "<soapenv:Header/>\n" +
		   "<soapenv:Body>\n" +
		      "<man:request domain=\"test-config\">\n";
		Iterator iter = files.iterator();
		while(iter.hasNext()){
			String file = iter.next().toString();
			base64 = base64Encode(new File(file));
			env += "<man:set-file name=\"local:///" + getSubPath(new File(file)) + "\">" + base64 + "</man:set-file>\n";
		}
		env += 	"</man:request>\n" +
		   			"</soapenv:Body>\n" +
				"</soapenv:Envelope>";
		return env;
	}
	
	private String getSOAPEnvelope_doimport(File configFile) throws IOException{
		
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:man=\"http://www.datapower.com/schemas/management\">" +
		"<soapenv:Header/>" +
		"<soapenv:Body>" +
		"<man:request domain=\"test-config\">" +
		"<man:do-import source-type=\"xcfg\" dry-run=\"true\" overwrite-files=\"true\" overwrite-objects=\"true\">" +
		"<man:input-file>" + 
		base64Encode(configFile) + 
		"</man:input-file>" +
		"</man:do-import>" +
		"</man:request>" +
		"</soapenv:Body>" +
		"</soapenv:Envelope>";
	}
	
	private String getHash(File file) throws IOException{
		MessageDigest digest;
		Long len;
		InputStream is;
		
		try {
			is = new FileInputStream(file);
			len = new Long(file.length());
			digest = MessageDigest.getInstance("SHA");
			
			byte[] content = new byte[len.intValue()];
			
			//reading bytes from file
			int readBytes = is.read(content,0,len.intValue());
			if(readBytes != len.intValue()) throw new IOException("Unable to read all content from " + file.getAbsolutePath());
			is.close();
			
			digest.update(content);
			byte[] hash = digest.digest();
			
			String test = new Base64Codec(null,"").encodeBase64("petterasskildt:gy59inku");
			return new String(base64Encode(hash,hash.length));
		} catch (Exception e) {
			throw new IOException("Error while creating hash of " + file.getAbsolutePath() + ": " + e.getMessage());
		}
	}
	
	public char[] base64Encode (byte[] in, int iLen) {
		   int oDataLen = (iLen*4+2)/3;       // output length without padding
		   int oLen = ((iLen+2)/3)*4;         // output length including padding
		   char[] out = new char[oLen];
		   int ip = 0;
		   int op = 0;
		   while (ip < iLen) {
		      int i0 = in[ip++] & 0xff;
		      int i1 = ip < iLen ? in[ip++] & 0xff : 0;
		      int i2 = ip < iLen ? in[ip++] & 0xff : 0;
		      int o0 = i0 >>> 2;
		      int o1 = ((i0 &   3) << 4) | (i1 >>> 4);
		      int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
		      int o3 = i2 & 0x3F;
		      out[op++] = map1[o0];
		      out[op++] = map1[o1];
		      out[op] = op < oDataLen ? map1[o2] : '='; op++;
		      out[op] = op < oDataLen ? map1[o3] : '='; op++; }
		   return out; }
	
	private String base64Encode(File file) throws IOException{
		Base64Codec codec = new Base64Codec(null, "");
		Reader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
		int length = new Long(file.length()).intValue();
		char[] buf = new char[length];
		
		int readChars = reader.read(buf, 0, length);
		if(readChars < length) throw new IOException("Unable to read all content from " + file.getAbsolutePath());
		return codec.encodeBase64(new String(buf));
	}
	
	private String base64Encode(byte[] buf) throws IOException{
		String str = new String(buf);
		Base64Codec codec = new Base64Codec(null, "");
		
		return codec.encodeBase64(str);
	}
	
	private String getSubPath(File path){
		String strPath = path.getAbsolutePath();
		String strTmpFolder = tmpFolder.getAbsolutePath();
		strPath = strPath.replaceAll("\\\\","/");
		strTmpFolder = strTmpFolder.replaceAll("\\\\","/");
		
		return strPath.replaceAll(strTmpFolder,"").substring(1);
	}
	
	private void cleanUp(File root){
		File[] children = root.listFiles();
		File child;
		for (int i = 0; i < children.length; i++) {
			child = children[i];
			if(child.isDirectory()) cleanUp(child);
			else{
				child.delete();
			}
		}
	}
	
	private void writeConfig() throws IOException{
		String strDateTime = new SimpleDateFormat("dd.MM.yy").format(new Date());
		configFile = new File(outputDirectory + "/" + environment.getName().split("\\.")[0] + "-" + strDateTime + ".xcfg");
		configFile.delete();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
		writer.write(templateContent,0,templateContent.length());
		writer.flush();
		writer.close();
		
		getLog().info("Written configuration to " + configFile.getAbsolutePath());
	}
	
}
