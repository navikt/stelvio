package no.nav.maven.plugins;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipException;

import no.nav.maven.plugins.common.utils.EarUtils;
import no.nav.maven.plugins.common.utils.FileUtils;
import no.nav.maven.plugins.fixers.ChangeDisplayName;
import no.nav.maven.plugins.fixers.FixArenaSak;
import no.nav.maven.plugins.fixers.JaxRPCHandlerMojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

/**
 * Goal which that adds the Stelvio jaxrpc handler to webservices.xml
 * 
 * @goal fixConfig
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
	private boolean changeDisplayName;
	
	/**
	 * 
	 * @parameter expression="${fixArenaSak}" default-value="true"
	 * @required
	 */
	private boolean fixArenaSak;
	
	/**
	 * 
	 * @parameter expression="${overriddenEndpointURI}"
	 * @required
	 */
	private String overriddenEndpointURI;
	
	/**
	 * 
	 * @parameter expression="${addAuthentication}" default-value="true"
	 * @required
	 */
	private boolean addAuthentication;
	
	/**
	 * 
	 * @parameter expression="${basicAuthUserid}" default-value=""
	 * @required
	 */
	private String basicAuthUserid;
	
	/**
	 * 
	 * @parameter expression="${basicAuthPassword}" default-value=""
	 * @required
	 */
	private String basicAuthPassword;
	
	/**
	 * 
	 * @parameter expression="${addHandler}" default-value="true"
	 * @required
	 */
	private boolean addHandler;
	
	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${handlerName}" default-value="ArenaResponseHandler"
	 * @required
	 */
	private String handlerName;
	
	/**
	 * This parameter is the directory where the wid ear files are placed.
	 * 
	 * @parameter expression="${handlerClass}" default-value="no.nav.java.ArenaResponseHandler"
	 * @required
	 */
	private String handlerClass;
	
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
				FixArenaSak fax = new FixArenaSak(flattenFolder, handlerName, 
						handlerClass, overriddenEndpointURI, basicAuthUserid, basicAuthPassword);
				fax.execute();
			}
			if(addHandler){
				JaxRPCHandlerMojo jrh = new JaxRPCHandlerMojo(flattenFolder,exceptionModules);
				jrh.execute();
			}
			
			compressEarFiles();
			
			getLog().info("Cleaning up...");
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
		flattenFolder = new File(earDirectory.getAbsolutePath() + "/flatten_temp_" + new Date().getTime());
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
		
		//threading the compressing process to increase performance
		RunnableEarCompressor compressor;
		List<RunnableEarCompressor> compressors = new ArrayList<RunnableEarCompressor>();
		List<Thread> compressorThreads = new ArrayList<Thread>();
		int threadNo = 0;
		getLog().info(extractedModules.length + " threads started on " + Runtime.getRuntime().availableProcessors() + " CPUs");
		for(File extractedModule : extractedModules){
			String moduleName = extractedModule.getName();
			String tagName = EarUtils.getTagLog().get(moduleName + ".ear");
			
			File targetEar = null;
			if(tagName != null){
				targetEar = new File(earDirectory.getAbsolutePath() + "/" + tagName + "/" + moduleName + ".ear");
			}else{
				targetEar = new File(earDirectory.getAbsolutePath() + "/" + moduleName + ".ear");
			}
			
			targetEar.mkdirs();
			targetEar.delete();
			compressor = new RunnableEarCompressor(extractedModule, targetEar, getLog());
			compressors.add(compressor);
			Thread t = new Thread(compressor,"compressor " + threadNo++);
			compressorThreads.add(t);
			t.start();
			//EarUtils.compressEarAndInnerModule(extractedModule, targetEar);
		}

		while(true){
			try {
				Thread.sleep(5000); //polling every 5 seconds

				//checking state of threads
				boolean allDone = true;
				for(Thread t : compressorThreads){
					if(t.getState() != Thread.State.TERMINATED){
						allDone = false;
						break;
					}
				}
				if(allDone){
					//checking for errors
					for(RunnableEarCompressor comp : compressors){
						if(comp.failed()){
							comp.exception.printStackTrace();
							throw new Exception("At least one error occured while compressing ears (showing only first): " + comp.exception.getMessage());
						}
					}
					break;
				}
			} catch (Exception e) {
				throw new IOException("Error occured: " + e.getMessage());
			} 
		}
		
		getLog().info("-------------------- End Compressing ears --------------------");
	}

	private class RunnableEarCompressor implements Runnable{
		private File source;
		private File target;
		private Log logger;
		private Exception exception;
		
		public RunnableEarCompressor(File source, File target, Log logger){
			if(source == null) throw new IllegalArgumentException("Source cannot be null!");
			if(target == null) throw new IllegalArgumentException("Target cannot be null!");
			this.source = source;
			this.target = target;
			this.logger = logger;
		}
		
		public void run(){
			try {
				EarUtils.compressEarAndInnerModule(source, target);
				logger.info(source.getName());
			} catch (IOException e) {
				exception = e;
			}
		}
		
		public boolean failed(){
			return exception != null;
		}

		public Exception getException() {
			return exception;
		}
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
