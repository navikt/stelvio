package no.nav.maven.plugins;  

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import no.nav.maven.plugins.datapower.DeviceFileStore;
import no.nav.maven.plugins.datapower.XMLMgmtInterface;
import no.nav.maven.plugins.datapower.config.ImportFormat;
import no.nav.maven.plugins.datapower.config.TemplateFormatter;
import no.nav.maven.plugins.datapower.util.FileUtils;
import no.nav.maven.plugins.datapower.util.StreamUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;


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
	 * @parameter expression="${configTemplate}"
	 * @required
	 */
	private File configTemplate; // = new File("E:\\maven-plugins\\maven-datapower-configurer\\src\\main\\resources\\templates\\template-config.xcfg");
	
	/**
	 * Location of the file.
	 * @parameter expression="${mapTemplate}"
	 * @required
	 */ 
	private File mapTemplate; // = new File("E:\\maven-plugins\\maven-datapower-configurer\\src\\main\\resources\\templates\\template-mapper.xml");
	
	/**
	 * Location of the file.
	 * @parameter expression="${importConfig}"
	 */
	private boolean importConfig; // = true;
	
	/**
	 * Location of the file.
	 * @parameter expression="${importFiles}"
	 */
	private boolean importFiles; // = true;
	
	/**
	 * Location of the file.
	 * @parameter expression="${environment}"
	 */
	private File environment; // = new File("E:\\maven-plugins\\maven-datapower-configurer\\src\\main\\resources\\environments\\Systemtest2\\Systemtest2.properties");
	
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
	private String user; // = "mavendeployer";
	
	/**
	 * Location of the file.
	 * @parameter expression="${password}"
	 * @required
	 */
	private String password; // = "Test1234";
	
	/**
	 * Private variables
	 */
	private File tmpFolder, configFile;
	
	private String mergedContent;
	
	private Properties env;
	
	public void execute() throws MojoExecutionException
	{
		tmpFolder = new File(outputDirectory.getAbsolutePath() + "/" + new Date().getTime());
		XMLMgmtInterface dp = new XMLMgmtInterface.Builder(host)
												  .domain(domain)
												  .user(user)
												  .password(password)
												  .build();
		//uploading files to datapower
		//getLog().info("name: " + fileArchive.getName() + " tostring: " + fileArchive.toString());
		if(importFiles){
			try {
				if(!fileArchive.exists()) throw new MojoExecutionException("Specified file archive invalid: File not found!");
				
				getLog().info("------------- File Import -------------");
				getLog().info("Opening connection to DataPower device...");
				
				getLog().info("Extracting ZIP archive...");
				FileUtils.extractArchive(fileArchive, tmpFolder);
				getLog().info("Creating request...");
				dp.importFiles(tmpFolder, DeviceFileStore.LOCAL);
				getLog().info("Files successfully imported...");
				getLog().info("Cleaning up temporary files...");
				tmpFolder.delete();
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
				
				
				getLog().info("Merging configuration with templates ...");
				
				//creating map formatter
				TemplateFormatter mapFormatter = new TemplateFormatter(readTemplate(mapTemplate));
				mergedContent = mapFormatter.format(env);
				String filename = env.getProperty("mapping.filename").toString();
				filename = filename.substring(filename.lastIndexOf("/") + 1);
				dp.importFile(filename,mergedContent,DeviceFileStore.LOCAL);
				
				//creating config formatter
				TemplateFormatter cfgFormatter = new TemplateFormatter(readTemplate(configTemplate));
				mergedContent = cfgFormatter.format(env);
				getLog().info("Adding config to zip file...");
				byte[] zipBytes = FileUtils.createZipArchive(mergedContent.getBytes("UTF-8"), "export.xml");
				
				// Open connection to DataPower device
				getLog().info("Opening connection to DataPower device...");
				getLog().info("Importing configuration...");
				String compressedData = new String(Base64.encodeBase64(zipBytes));
				dp.importConfig(compressedData, ImportFormat.ZIP);
				
				//importing LTPA keys to DataPower device
				importLTPAKeys(dp);
				
				getLog().info("Done!");
				getLog().info("-----------------------------------------");
			} catch (IOException e) {
				throw new MojoExecutionException("Error importing config to datapower!", e);
			}
		}
	}	
	
	private void importLTPAKeys(XMLMgmtInterface dp) throws IOException{
		File LTPAFolder = new File(environment.getAbsolutePath().replaceAll(environment.getName(), "") + "/ltpa-keys");
		File[] keys = LTPAFolder.listFiles();
		getLog().info("Importing " + keys.length + " LTPA keys to DataPower...");
		for (int i = 0; i < keys.length; i++) {
			dp.importFile(keys[i].getName(),StreamUtils.getInputStreamAsString(new FileInputStream(keys[i]),true),DeviceFileStore.CERT);
		}
	}
	
	private void readEnvironment() throws IOException{
		env = new Properties();
		
		if(environment == null) throw new IOException("Environment must be set when importing config!");
		env.load(new FileInputStream(environment));
		
		//manually trimming properties
		Enumeration envProps = env.keys();
		String key;
		while(envProps.hasMoreElements()){
			key = envProps.nextElement().toString();
			env.setProperty(key,env.getProperty(key).trim());
		}
	}

	private String readTemplate(File template) throws IOException{
		if(template == null) throw new IOException("Template must be set when importing config!");
		return StreamUtils.getInputStreamAsString(new FileInputStream(template), true);
	}
}
