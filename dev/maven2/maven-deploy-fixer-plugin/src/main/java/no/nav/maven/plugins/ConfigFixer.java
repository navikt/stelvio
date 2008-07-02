package no.nav.maven.plugins;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipException;

import no.nav.maven.plugins.common.utils.EarUtils;
import no.nav.maven.plugins.common.utils.FileUtils;
import no.nav.maven.plugins.fixers.AddUsernameToken;
import no.nav.maven.plugins.fixers.FixRoleBinding;

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
	 * 
	 * @parameter expression="${earFolder}"
	 * @required
	 */
	private File earFolder;
	
	/**
	 * 
	 * @parameter expression="${flattenedFolder}"
	 * @required
	 */
	private File flattenedFolder;
	
	/**
	 * 
	 * @parameter expression="${envFile}"
	 * @required
	 */
	private File envFile;
	
	/**
	 * 
	 * @parameter expression="${resume}" default-value="false"
	 * @required
	 */
	private boolean resume;
	
	/**
	 * 
	 * @parameter expression="${doRoleBinding}" default-value="false"
	 * @required
	 */
	private boolean doRoleBinding;
	
	//NON MAVEN PROPERTIES
	private File workingFolder;
	private Properties props;
	private boolean addUsernameToken;
	
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("######################### START CONFIG EDITING #########################");
		getLog().info("########################################################################");
		
		if(!earFolder.exists()) throw new MojoExecutionException("earFolder: '" + earFolder.getAbsolutePath() + "' does not exist!");
		
		workingFolder = new File(flattenedFolder.getAbsolutePath() + "/temp_" + new Date().getTime());
		workingFolder.mkdirs();
		
		try {
			readEnvFile();
			
			if(!resume){
				flattenEars();
				
				if(doRoleBinding || addUsernameToken){
					extractEarFiles();
					
					if(doRoleBinding){
						FixRoleBinding frb = new FixRoleBinding();
						frb.setProperties(props);
						File[] consModules = workingFolder.listFiles(new FilenameFilter() {
												public boolean accept(File dir, String name) {
													return name.contains("nav-cons");
												}
											});
						if(consModules != null){
							for(File f : consModules){
								frb.setConsModuleFolder(f);
								frb.execute();
							}
						}
						
					}
					if(addUsernameToken){
						AddUsernameToken aut = new AddUsernameToken();
						
						File[] consModules = workingFolder.listFiles(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return name.contains("nav-cons");
							}
						});
						
						if(consModules != null){
							for(File f : consModules){
								aut.setConsModuleFolder(f);
								aut.execute();
							}
						}
					}
					
					compressEarFiles();
				}
			}else{
				getLog().info("Resume enabled, using present eardist folder");
			}
			
		} catch (Exception e) {
			throw new MojoExecutionException("Error during config setup",e);
		}
		
		getLog().info("######################### CONFIG EDITING ALL DONE #########################");
		getLog().info("###########################################################################");
	}
	
	private void flattenEars() throws IOException{
		getLog().info("-------------------- Start flattening EAR --------------------");
		
		getLog().info("Cleaning up present eardist folder...");
		if(!FileUtils.recursiveDelete(flattenedFolder)) throw new IOException("Unable to delete eardist folder!");
		flattenedFolder.mkdirs();
		getLog().info("Done!");
		
		List<File> ears = FileUtils.listFiles(earFolder, true, ".ear");
		for(File ear : ears){
			FileUtils.copyFile(ear, new File(flattenedFolder.getAbsolutePath() + "/" + ear.getName()));
		}
		getLog().info("-------------------- End flattening EAR --------------------");
	}
	
	private void extractEarFiles() throws ZipException, IOException{
		getLog().info("-------------------- Start EAR extraction --------------------");

		
		File[] ears = flattenedFolder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.contains("nav-cons");
			}
		});
		
		for(File ear : ears){
			getLog().info(ear.getName());
			EarUtils.extractEarAndInnerModule(ear, workingFolder);
		}
		
		getLog().info("-------------------- End EAR extraction --------------------");
	}
	
	private void readEnvFile() throws IOException {
		getLog().info("-------------------- Start Environment Load --------------------");
		props = new Properties();
		props.load(new FileInputStream(envFile));

		// Properties class doesn't trim property values, need to do manual
		// trimming
		Enumeration enumeration = props.keys();
		String key = null;
		while (enumeration.hasMoreElements()) {
			key = enumeration.nextElement().toString();
			props.setProperty(key, props.getProperty(key).trim());
		}
		getLog().info("Loaded " + props.size() + " property values.");
		
		//setting switches according to environment
		if(props.getProperty("addUsernameToken") != null && props.getProperty("addUsernameToken").compareToIgnoreCase("true") == 0)
			addUsernameToken = true;
		if(props.getProperty("doRoleBinding") != null && props.getProperty("doRoleBinding").compareToIgnoreCase("true") == 0)
			doRoleBinding = true;
		
		getLog().info("-------------------- End Environment Load --------------------");
	}
	
	private void compressEarFiles() throws IOException{
		File[] extractedModules = workingFolder.listFiles(new FileFilter(){
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
			
			File targetEar = new File(flattenedFolder.getAbsolutePath() + "/" + moduleName + ".ear");
			
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
		/*
		File[] extractedModules = workingFolder.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				return (pathname.isDirectory() && pathname.getName().startsWith("nav-"));
			}
		});
		getLog().info("-------------------- Start Compressing ears --------------------");
		for(File extractedModule : extractedModules){
			String moduleName = extractedModule.getName();
			
			File targetEar = new File(flattenedFolder.getAbsolutePath() + "/" + moduleName + ".ear");
			
			getLog().info(moduleName);
			targetEar.mkdirs();
			targetEar.delete();
			EarUtils.compressEarAndInnerModule(extractedModule, targetEar);
		}
		FileUtils.recursiveDelete(workingFolder);
		getLog().info("-------------------- End Compressing ears --------------------");*/
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


	public boolean isAddUsernameToken() {
		return addUsernameToken;
	}


	public void setAddUsernameToken(boolean addUsernameToken) {
		this.addUsernameToken = addUsernameToken;
	}


	public boolean isDoRoleBinding() {
		return doRoleBinding;
	}


	public void setDoRoleBinding(boolean doRoleBinding) {
		this.doRoleBinding = doRoleBinding;
	}


	public File getEarFolder() {
		return earFolder;
	}


	public void setEarFolder(File earDistFolder) {
		this.earFolder = earDistFolder;
	}


	public File getEnvFile() {
		return envFile;
	}


	public void setEnvFile(File envFile) {
		this.envFile = envFile;
	}

	public File getFlattenedFolder() {
		return flattenedFolder;
	}

	public void setFlattenedFolder(File flattenedFolder) {
		this.flattenedFolder = flattenedFolder;
	}

	public boolean isResume() {
		return resume;
	}

	public void setResume(boolean resume) {
		this.resume = resume;
	}
	
}
