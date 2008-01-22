package no.nav.maven.plugins;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import sun.misc.BASE64Encoder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * 
 * @phase process-sources
 */
public class Configurer
extends AbstractMojo
{
	/**
	 * Location of the file.
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory; //= new File("E:/maven-plugins/maven-datapower-configurer/target");
	
	/**
	 * Location of the file.
	 * @parameter expression="${wsdl.zip}"
	 * @required
	 */
	private File wsdlFile; // = new File("E:/maven-plugins/maven-datapower-configurer/wsdl-pselv.zip");
	
	/**
	 * Location of the file.
	 * @parameter expression="${template}"
	 * @required
	 */
	private File template; // = new File("E:/maven-plugins/maven-datapower-configurer/template.xcfg");
	
	/**
	 * Private variables
	 */
	private File tmpFolder;
	
	private Document doc;
	
	private Element files;
	
	private XPath xpath;
	
	public void execute() throws MojoExecutionException
	{
		tmpFolder = new File(outputDirectory.getAbsolutePath() + "/" + new Date().getTime());
		
		try {
			//reading template file
			readTemplate();
			
			//extracting wsdl files
			extract();
			
			//adding files to DataPower config file
			xpath = doc.createXPath("//files");
			files = (Element)xpath.selectSingleNode(doc);
			if(files == null) throw new DocumentException("Unable to find files node in template!");
			getLog().info("Adding wsdl archive content to configuration file...");
			addFilesToTemplate(tmpFolder);
			getLog().info("Done!");
			
			//writing merged template
			writeXML();
			
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
	
	private void addFilesToTemplate(File root) throws DocumentException{
		File[] children = root.listFiles();
		File child;
		Element file;
		
		for (int i = 0; i < children.length; i++) {
			child = children[i];
			if(child.isDirectory()) addFilesToTemplate(child);
			else{
				if(child.getName().toLowerCase().compareTo("manifest.mf") == 0) continue;
				String path = getSubPath(child);
				file = files.addElement("file");
				
				file.addAttribute("name","local:///" + path);
				file.addAttribute("src","local/" + path);
				file.addAttribute("location","local");
				getLog().info("Adding " + path);
				try {
					file.addAttribute("hash",getHash(child));
					file.setText(base64Encode(child));
				} catch (IOException e) {
					throw new DocumentException("Unable to get Base64Encoding/MD5Hash for " + child,e);
				}
			}
		}
	}
	
	private String getHash(File file) throws IOException{
		MessageDigest digest;
		Long len;
		InputStream is;
		
		try {
			is = new FileInputStream(file);
			len = new Long(file.length());
			digest = MessageDigest.getInstance("MD5");
			
			byte[] content = new byte[len.intValue()];
			
			//reading bytes from file
			int readBytes = is.read(content,0,len.intValue());
			if(readBytes != len.intValue()) throw new IOException("Unable to read all content from " + file.getAbsolutePath());
			is.close();
			
			digest.update(content);
			
			return new BASE64Encoder().encode(digest.digest());
		} catch (Exception e) {
			throw new IOException("Error while creating hash of " + file.getAbsolutePath() + ": " + e.getMessage());
		}
	}
	
	private String base64Encode(File file) throws IOException{
		BASE64Encoder encoder = new BASE64Encoder();
		InputStream is = new FileInputStream(file);
		Long len = new Long(file.length());
		
		byte[] content = new byte[len.intValue()];
		
		//reading bytes from file
		int readBytes = is.read(content,0,len.intValue());
		if(readBytes != len.intValue()) throw new IOException("Unable to read all content from " + file.getAbsolutePath());
		is.close();
		
		return encoder.encode(content);
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
		writer.write(doc);
		writer.close();
	}
	
}
