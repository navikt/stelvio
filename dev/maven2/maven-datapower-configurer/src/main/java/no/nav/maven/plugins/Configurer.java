package no.nav.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.XPath;
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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;


/**
 * Goal which touches a timestamp file.
 *
 * @goal configure
 * @phase install
 */
public class Configurer
extends AbstractMojo
{
	/**
	 * Location of the file.
	 * @parameter expression="${outDir}"
	 * @required
	 */
	private File outputDirectory = new File("E:/maven-plugins/maven-datapower-configurer/target");
	
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
	private File tmpFolder;
	
	private Document doc;
	
	private XPath xpath;
	
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
			//reading template file
			readTemplate();
			
			//extracting wsdl files
			extract();
		
			//String test = new Base64Codec(null,"").encodeBase64(user + ":" + password);
			uploadFilesAndFolders(tmpFolder);
			
			//writing merged template
			//writeXML();
			
			//cleaning up temporary files
			cleanUp(tmpFolder);
		} catch (DocumentException e) {
			throw new MojoExecutionException("",e);
		} catch (IOException e) {			
			throw new MojoExecutionException("Error extracting wsdl files!",e);
		}
	}
	
	private void extract() throws IOException
	{
		getLog().info("Extracting wsdl archive...");
		tmpFolder.mkdirs();
		ZipUtils.extract2(wsdlFile,tmpFolder);
		getLog().info("Done!");
	}
	
	private void readTemplate() throws DocumentException{
		getLog().info("Reading template file...");
		SAXReader reader = new SAXReader();
		doc = reader.read(template);    	
		getLog().info("Done!");
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
			//write headr info as well??????
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
	
	private void writeXML() throws IOException{
		String strDateTime = new SimpleDateFormat("dd-MM-yy").format(new Date());
		File configFile = new File(outputDirectory + "/" + strDateTime + "." + template.getName());
		configFile.delete();
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile),"UTF-8"));
		XMLWriter writer = new XMLWriter(bw,OutputFormat.createPrettyPrint());
		writer.write(doc.getRootElement());
		writer.close();
		
		getLog().info("\n\n\t\tWritten configuration to " + configFile.getAbsolutePath());
	}
	
}
