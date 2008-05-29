package no.nav.maven.plugins;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import no.nav.maven.plugins.fixers.BasicAuthMojo;
import no.nav.maven.plugins.fixers.ChangeDisplayName;
import no.nav.maven.plugins.fixers.FixArenaSak;
import no.nav.maven.plugins.fixers.JaxRPCHandlerMojo;
import no.nav.maven.utils.EarUtils;
import no.nav.maven.utils.FileUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which that adds the Stelvio jaxrpc handler to webservices.xml
 * 
 * @goal fixAllConfig
 * 
 *  
 */
public class ConfigFixer extends AbstractMojo {

	/**
	 * This is the directory of the flatten ear directory.
	 * 
	 * @parameter
	 * @required
	 */
	protected File earDirectory;
	
	/**
	 * 
	 * @parameter expression="${changeDisplayName}" default-value="true"
	 * @required
	 */
	private boolean changeDisplayName = true; 
	
	/**
	 * 
	 * @parameter expression="${fixArenaSak}" default-value="true"
	 * @required
	 */
	private boolean fixArenaSak = true; 
	
	/**
	 * 
	 * @parameter expression="${addAuthentication}" default-value="true"
	 * @required
	 */
	private boolean addAuthentication = true; 
	
	/**
	 * 
	 * @parameter expression="${addHandler}" default-value="true"
	 * @required
	 */
	private boolean addHandler = true; 
	
	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${handlerName}" default-value="ArenaResponseHandler"
	 * @required
	 */
	private String handlerName = "ArenaResponseHandler";
	
	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${handlerClass}" default-value="no.nav.java.ArenaResponseHandler"
	 * @required
	 */
	private String handlerClass = "no.nav.java.ArenaResponseHandler";
	
	/**
     * List of modules that needs this authentication-info added
     *
     * @parameter expression=${modules} default-value="null"
     * @required
     */
    private Set modules;
    
    /**
     * List of modules that needs this authentication-info added
     *
     * @parameter expression=${exceptionModules} default-value="null"
     * @required
     */
    private Set exceptionModules;
	
	/**
	 * NON MAVEN VARIABLES
	 */
	private File flattenFolder = null;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			getLog().info("######################### START CONFIG EDITING #########################");
			getLog().info("########################################################################");
			extractEarFiles();
			
			//running sub goals
			if(changeDisplayName){
				ChangeDisplayName cdn = new ChangeDisplayName();
				cdn.setTargetDir(flattenFolder);
				cdn.execute();
			}
			if(fixArenaSak){
				FixArenaSak fax = new FixArenaSak(flattenFolder, handlerName, handlerClass);
				fax.execute();
			}
			if(addAuthentication){
				BasicAuthMojo bam = new BasicAuthMojo(flattenFolder,modules);
				bam.execute();
			}
			if(addHandler){
				JaxRPCHandlerMojo jrh = new JaxRPCHandlerMojo(flattenFolder,exceptionModules);
				jrh.execute();
			}
			
			compressEarFiles();
			
			FileUtils.recursiveDelete(flattenFolder);
			
			getLog().info("######################### CONFIG EDITING ALL DONE #########################");
			getLog().info("###########################################################################");
		} catch (ZipException e) {
			throw new MojoExecutionException("Archive error!", e);
		} catch (IOException e) {
			throw new MojoExecutionException("An IO error occured!",e);
		}
	}
	
	private void extractEarFiles() throws ZipException, IOException{
		flattenFolder = new File(earDirectory.getAbsolutePath() + "/flatten_temp");
		flattenFolder.delete();
		flattenFolder.mkdirs();
		getLog().info("-------------------- Start EAR flattening --------------------");
		EarUtils.flattenEarStructure(earDirectory, flattenFolder);
		
		File[] earFiles = flattenFolder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".ear");
			}
		
		});
		
		for(File f : earFiles){
			getLog().info(f.getName());
			EarUtils.extractEarAndInnerModule(f, flattenFolder);
		}
		getLog().info("-------------------- End EAR flattening --------------------");
	}
	
	private void compressEarFiles() throws IOException{
		File[] extractedModules = flattenFolder.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				return (pathname.isDirectory() && pathname.getName().startsWith("nav-"));
			}
		});
		getLog().info("-------------------- Start Compressing ears --------------------");
		for(File extractedModule : extractedModules){
			String moduleName = extractedModule.getName();
			String tagName = EarUtils.getTagLog().get(moduleName + ".ear");
			
			File targetEar = null;
			if(tagName != null){
				targetEar = new File(earDirectory.getAbsolutePath() + "/" + tagName + "/" + moduleName + ".ear");
			}else{
				targetEar = new File(earDirectory.getAbsolutePath() + "/" + moduleName + ".ear");
			}
			
			getLog().info(moduleName);
			targetEar.mkdirs();
			targetEar.delete();
			EarUtils.compressEarAndInnerModule(extractedModule, targetEar);
		}
		
		getLog().info("-------------------- End Compressing ears --------------------");
	}

	
	public File getEarDirectory() {
		return earDirectory;
	}
	

	public void setEarDirectory(File earDirectory) {
		this.earDirectory = earDirectory;
	}

	public Set getModules() {
		return modules;
	}

	public void setModules(Set modules) {
		this.modules = modules;
	}

	public Set getExceptionModules() {
		return exceptionModules;
	}

	public void setExceptionModules(Set exceptionModules) {
		this.exceptionModules = exceptionModules;
	}

}
