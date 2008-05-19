package no.nav.maven.plugins;  

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
	 * Helper class to simplify testing and programmatic configuration of the plugin.
	 * 
	 * @author Torbj�rn Staff, Accenture
	 */
	public static class Builder {
		private File fileArchive; 
		private File configTemplate; 
		private File mapTemplate; 
		private File outputDirectory;
		private File environment; 
		private String host; 
		private String user; 
		private String password; 
		private String ltpaPwdWAS; 
		private String ltpaPwdWPS; 
		private String bustag; 
		private boolean importConfig; 
		private boolean importFiles; 
		
		public Builder() {}
		public Builder fileArchive(File fileArchive) { this.fileArchive = fileArchive; return this;}
		public Builder configTemplate(File configTemplate) { this.configTemplate = configTemplate; return this; }
		public Builder mapTemplate(File mapTemplate) { this.mapTemplate = mapTemplate; return this; }
		public Builder outputDirectory(File outputDirectory) { this.outputDirectory = outputDirectory; return this; }
		public Builder environment(File environment) { this.environment = environment; return this; }
		public Builder host(String host) { this.host = host; return this; }
		public Builder user(String user) { this.user = user; return this; }
		public Builder password(String password) { this.password = password; return this; }
		public Builder ltpaPwdWAS(String ltpaPwdWAS) { this.ltpaPwdWAS = ltpaPwdWAS; return this; }
		public Builder ltpaPwdWPS(String ltpaPwdWPS) { this.ltpaPwdWPS = ltpaPwdWPS; return this; }
		public Builder bustag(String bustag) { this.bustag = bustag; return this; }
		public Builder importConfig(boolean importConfig) { this.importConfig = importConfig; return this; }
		public Builder importFiles(boolean importFiles) { this.importFiles = importFiles; return this; }
		public Configurer build() { return new Configurer(this); }
	}
	
	private Configurer(Builder builder) {
		this.bustag = builder.bustag;
		this.configTemplate = builder.configTemplate;
		this.environment = builder.environment;
		this.fileArchive = builder.fileArchive;
		this.host = builder.host;
		this.importConfig = builder.importConfig;
		this.importFiles = builder.importFiles;
		this.ltpaPwdWAS = builder.ltpaPwdWAS;
		this.ltpaPwdWPS = builder.ltpaPwdWPS;
		this.mapTemplate = builder.mapTemplate;
		this.outputDirectory = builder.outputDirectory;
		this.password = builder.password;
		this.user = builder.user;
	}
	
	public Configurer(){
	
	}
	/**
	 * Location of the file.
	 * @parameter expression="${outDir}"
	 * @required
	 */
	private File outputDirectory;
	
	/**
	 * Location of the file.
	 * @parameter expression="${archive}"
	 */
	private File fileArchive; 
	
	/**
	 * Location of the file.
	 * @parameter expression="${configTemplate}"
	 * @required
	 */
	private File configTemplate; 
	
	/**
	 * Location of the file.
	 * @parameter expression="${mapTemplate}"
	 * @required
	 */ 
	private File mapTemplate; 
	
	/**
	 * Location of the file.
	 * @parameter expression="${importConfig}"
	 */
	private boolean importConfig; 
	
	/**
	 * Location of the file.
	 * @parameter expression="${importFiles}"
	 */
	private boolean importFiles; 
	
	/**
	 * Location of the file.
	 * @parameter expression="${environment}"
	 */
	private File environment; 
	
	/**
	 * Location of the file.
	 * @parameter expression="${host}"
	 * @required
	 */
	private String host; 
	
	/**
	 * Location of the file.
	 * @parameter expression="${user}"
	 * @required
	 */
	private String user; 
	
	/**
	 * Location of the file.
	 * @parameter expression="${password}"
	 * @required
	 */
	private String password; 

	/**
	 * Location of the file.
	 * @parameter expression="${ltpaPwdWAS}"
	 */
	private String ltpaPwdWAS; 

	/**
	 * Location of the file.
	 * @parameter expression="${ltpaPwdWPS}"
	 */
	private String ltpaPwdWPS; 

	/**
	 * Location of the file.
	 * @parameter expression="${bustag}"
	 * @required
	 */
	private String bustag; 
	
	/**
	 * Private variables
	 */
	private File tmpFolder/*, configFile*/;
	
	private String mergedContent;
	
	private Properties env;
	
	public void execute() throws MojoExecutionException
	{
		tmpFolder = new File(outputDirectory.getAbsolutePath() + "/" + new Date().getTime());
		XMLMgmtInterface dp = null;
		
		getLog().info("Reading environment file...");
		try {
			readEnvironment();
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new MojoExecutionException("Error reading environment file!");
		}
		getLog().info("Done!\n");
		
		dp = new XMLMgmtInterface.Builder(host)
								  .domain(env.getProperty("domain"))
								  .user(user)
								  .password(password)
								  .build();
		
		//uploading files to datapower
		if (importFiles) {
			try {
				doImportFiles(dp);
			} catch (IOException e) {			
				throw new MojoExecutionException("Error importing files to datapower!", e);
			}
		}
		
		//importing config to datapower
		if (importConfig) {
			try {
				doImportConfig(dp);
			} catch (IOException e) {
				throw new MojoExecutionException("Error importing config to datapower!", e);
			}
		}
	}
	
	private void doImportFiles(XMLMgmtInterface dp) throws IOException {
		if(!fileArchive.exists())
			throw new IOException("Specified file archive invalid: File not found!");		
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
	}
	
	private void doImportConfig(XMLMgmtInterface dp) throws IOException {
		getLog().info("------------- Config Import -------------");
		//reading environment and template file
		
		getLog().info("Loading template file...");
		
		
		getLog().info("Merging configuration with templates ...");
		
		//creating map formatter
		TemplateFormatter mapFormatter = new TemplateFormatter(readTemplate(mapTemplate));
		mergedContent = mapFormatter.format(env);
		String filename = env.getProperty("mapping.filename").toString();
		filename = filename.substring(filename.lastIndexOf("/") + 1);
		getLog().info("Opening connection to DataPower device...");
		getLog().info("Importing " + filename + "...");
		dp.importFile(filename,mergedContent,DeviceFileStore.LOCAL);
		
		//writing merged config to outDir
		try {
			File localmap = new File(outputDirectory.getAbsolutePath() + "/" + mapTemplate.getName());
			localmap.delete();
			FileWriter writer = new FileWriter(localmap);
			writer.write(mergedContent);
			writer.close();
		} catch (IOException e) {
			getLog().warn("Unable to write local copy of imported map file!");
		}
		
		
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
		
		//writing merged config to outDir
		try {
			File localConfig = new File(outputDirectory.getAbsolutePath() + "/dp-plugin-config.xcfg");
			localConfig.delete();
			FileWriter writer = new FileWriter(localConfig);
			writer.write(mergedContent);
			writer.close();
		} catch (IOException e) {
			getLog().warn("Unable to write local copy of imported config file!");
		}
		
		
		//importing LTPA keys to DataPower device
		doImportLTPAKeys(dp,"ltpa");
		
		//Saving imported configuration and restarting dp
		getLog().info("Saving configuration and restarting domain...");
		dp.saveConfigAndRestartDomain();
		
		getLog().info("Done!");
		getLog().info("-----------------------------------------");
	}
	
	private void doImportLTPAKeys(XMLMgmtInterface dp, String path) throws IOException{
		File LTPAFolder = new File(environment.getAbsolutePath().replaceAll(environment.getName(), "") + "/ltpa-keys");
		File[] keys = LTPAFolder.listFiles();
		getLog().info("------------- LTPA Keys Import -------------");
		getLog().info("Searching for keys in " + LTPAFolder.getAbsolutePath());
		if(keys == null) getLog().info("No keys to import...");
		else{
			dp.createDir(path,DeviceFileStore.LOCAL);
			for (int i = 0; i < keys.length; i++) {
				if(keys[i].isFile()){
					getLog().info("Importing " + keys[i].getName());
					dp.importFile(path + "/" + keys[i].getName(),StreamUtils.getInputStreamAsString(new FileInputStream(keys[i]),true),DeviceFileStore.LOCAL);
				}
			}
		}
		getLog().info("-----------------------------------------");
	}
	
	private void readEnvironment() throws IOException {
		env = new Properties();
		
		if (environment == null)
			throw new IOException("Environment must be set when importing config!");
		env.load(new FileInputStream(environment));
		
		// Check for properties overridden by command line arguments
		if (checkCommandlinePropertyOverride(ltpaPwdWAS)) {
			getLog().info("Command line override property ltpaPwdWAS = " + ltpaPwdWAS);
			env.put("AULTPAKeyFilePassword.WAS", ltpaPwdWAS);
		}
		if (checkCommandlinePropertyOverride(ltpaPwdWPS)) {
			getLog().info("Command line override property ltpaPwdWPS = " + ltpaPwdWPS);
			env.put("PPLTPAKeyFilePassword.WPS", ltpaPwdWPS);
		}
		
		//manually trimming properties
		Enumeration envProps = env.keys();
		String key;
		while(envProps.hasMoreElements()){
			key = envProps.nextElement().toString();
			env.setProperty(key,env.getProperty(key).trim());
		}
		
		//adding bus_tag to environment
		env.setProperty("BUSTAG",bustag.trim());
	}	
	
	private boolean checkCommandlinePropertyOverride(String property) {
		return (property != null && !property.equals("")) ? true : false;
	}

	private String readTemplate(File template) throws IOException{
		if (template == null)
			throw new IOException("Template must be set when importing config!");
		return StreamUtils.getInputStreamAsString(new FileInputStream(template), true);
	}
}
