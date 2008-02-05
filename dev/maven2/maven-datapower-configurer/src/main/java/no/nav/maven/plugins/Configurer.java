package no.nav.maven.plugins;  

import no.nav.maven.plugins.datapower.XMLMgmtInterface;
import no.nav.maven.plugins.datapower.config.ImportFormat;
import no.nav.maven.plugins.datapower.config.XCFGFormatter;
import no.nav.maven.plugins.datapower.util.FileUtils;

import org.apache.commons.codec.binary.Base64;
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
	private File outputDirectory; // = new File("E:\\maven-plugins\\maven-datapower-configurer\\target");
	
	/**
	 * Location of the file.
	 * @parameter expression="${archive}"
	 */
	private File fileArchive; // = new File("E:/deploy_scripts/kjempen/target/classes/builds/wsdl/wsdl-pselv.zip");
	
	/**
	 * Location of the file.
	 * @parameter expression="${importConfig}"
	 */
	private boolean importConfig; // = true;
	
	/**
	 * Location of the file.
	 * @parameter expression="${template}"
	 */
	private File template; // = new File("E:/maven-plugins/maven-datapower-configurer/src/main/resources/template.xcfg");
	
	/**
	 * Location of the file.
	 * @parameter expression="${environment}"
	 */
	private File environment; // = new File("E:\\maven-plugins\\maven-datapower-configurer\\src\\main\\resources\\environments\\Systemtest2.properties");
	
	/**
	 * Location of the file.
	 * @parameter expression="${host}"
	 * @required
	 */
	private String host; // = "https://secgw-01.utv.internsone.local:5550";
	
	/**
	 * Location of the file.
	 * @parameter expression="${domain}"
	 * @required
	 */
	private String domain; // = "test-config";
	
	/**
	 * Location of the file.
	 * @parameter expression="${user}"
	 * @required
	 */
	private String user; // = "petterasskildt";
	
	/**
	 * Location of the file.
	 * @parameter expression="${password}"
	 * @required
	 */
	private String password; // = "gy59inku";
	
	/**
	 * Private variables
	 */
	private File tmpFolder, configFile;
	
	private Document doc;
	
	private String templateContent;
	
	private XPath xpath;
	
	private Properties env;
	
	public void execute() throws MojoExecutionException
	{
		tmpFolder = new File(outputDirectory.getAbsolutePath() + "/" + new Date().getTime());
		
		//uploading files to datapower
		if(fileArchive != null){
			try {
				getLog().info("------------- File Import -------------");
				getLog().info("Opening connection to DataPower device...");
				XMLMgmtInterface dp = new XMLMgmtInterface.Builder(host)
														  .domain(domain)
														  .user(user)
														  .password(password)
														  .build();
				getLog().info("Extracting ZIP archive...");
				FileUtils.extractArchive(fileArchive, tmpFolder);
				getLog().info("Creating request...");
				dp.importFiles(tmpFolder);
				getLog().info("Files successfully imported...");
				getLog().info("-----------------------------------------");
			} catch (IOException e) {			
				throw new MojoExecutionException("Error importing files to datapower!", e);
			}
		}
		
		//importing config to datapower
		if(importConfig){
			try {
				getLog().info("------------- Config Import -------------");
				//reading environment and template file
				getLog().info("Reading environment file...");
				readEnvironment();
				getLog().info("Loading template file...");
				readTemplate();
				
				//Create config formatter
				getLog().info("Reformatting config...");
				XCFGFormatter cfgFormatter = new XCFGFormatter(templateContent);
				String newConfig = cfgFormatter.format(env);

				//ZipUtils.compress(source, destination
				getLog().info("Adding config to zip file...");
				byte[] zipBytes = FileUtils.createZipArchive(newConfig.getBytes("UTF-8"), "export.xml");
				
				// Open connection to DataPower device
				getLog().info("Opening connection to DataPower device...");
				XMLMgmtInterface dp = new XMLMgmtInterface.Builder(host).domain(domain).user(user).password(password).build();
				getLog().info("Importing configuration...");
				String compressedData = new String(Base64.encodeBase64(zipBytes));
				dp.importConfig(compressedData, ImportFormat.ZIP);
				
				getLog().info("Done!");
				getLog().info("-----------------------------------------");
			} catch (IOException e) {
				throw new MojoExecutionException("Error importing config to datapower!", e);
			}
		}
	}	
	
	private void readEnvironment() throws IOException{
		env = new Properties();
		
		if(environment == null) throw new IOException("Environment must be set when importing config!");
		env.load(new FileInputStream(environment));
		
		//manually trimming properties
		Enumeration enum = env.keys();
		String key;
		while(enum.hasMoreElements()){
			key = enum.nextElement().toString();
			env.setProperty(key,env.getProperty(key).trim());
		}
	}

	private void readTemplate() throws IOException{
		if(template == null) throw new IOException("Template must be set when importing config!");
		BufferedReader reader = new BufferedReader(new FileReader(template));
		StringBuffer buffer = new StringBuffer();
		String line;
		while((line = reader.readLine()) != null){
			buffer.append(line + System.getProperty("line.separator"));
		}
		templateContent = buffer.toString();
	}
}
