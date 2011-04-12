package no.nav.maven.plugins.mojos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import no.nav.maven.plugins.utils.Archiver;
import no.nav.maven.plugins.utils.NativeOps;
import no.nav.maven.plugins.utils.XMLOps;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.xml.sax.SAXException;

/**
 * Goal that handles the file operations with coherent checks, as well as custom file fixes for spesific scenarios
 * 
 * @goal handle-files
 * 
 * @author test@example.com
 */
public class FileHandlerMojo extends AbstractMojo {

	/**
	 * @parameter expression="${app}"
	 * @required
	 */
	private String application;

	/**
	 * @parameter expression="${project}"
	 * @readonly
	 * @required
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${stagingArea}"
	 * @required
	 */
	private String stagingArea;

	/**
	 * @parameter expression="${earDir}"
	 * @required
	 */
	private String earDir;

	/**
	 * @parameter expression="${configDir}"
	 * @required
	 */
	private String configDir;

	/**
	 * @parameter expression="${penConfigDir}"
	 */
	private String penConfigDir;

	/**
	 * @parameter expression="${pselvWARDir}"
	 */
	private String pselvWARDir;

	/**
	 * @parameter expression="${batchDir}"
	 */
	private String batchDir;

	/**
	 * @parameter expression="${log4jDir}"
	 * @required
	 */
	private String log4jDir;

	/**
	 * @parameter expression="${environment/log4j-file}"
	 * @required
	 */
	private String log4jFile;

	/**
	 * @parameter expression="${environment/log4j-batch-file}"
	 */
	private String log4jBatchFile;
	
	/**
	 * @parameter expression="${environment/logback-file}"
	 * @required
	 */
	private String logbackFile;

	/**
	 * @parameter expression="${environment/logback-batch-file}"
	 */
	private String logbackBatchFile;

	/**
	 * @parameter expression="${zone}"
	 * @required
	 */
	private String zone;
	
	/**
	 * @parameter expression="${environment/domain}"
	 * @required
	 */
	private String domain;

	/**
	 * @parameter expression="${pselvISSessionTimeout}"
	 * @required
	 */
	private String pselvISSessionTimeout;

	/**
	 * @parameter expression="${pselvSSAccessDeniedURL}"
	 * @required
	 */
	private String pselvSSAccessDeniedURL;

	/**
	 * @parameter expression="${roleMappingDir}"
	 * @required
	 */
	private String roleMappingDir;

	/**
	 * @parameter expression="${applicationConfig}"
	 * @required
	 */
	private String applicationConfig;
	
	/**
	 * List of files to delete
	 */
	private ArrayList<String> filesToDelete;

	public void execute() throws MojoExecutionException, MojoFailureException {

		try {

			// Initialize the array list, will be populated along the execution
			filesToDelete = new ArrayList<String>();

			// Checks whether the application your deploying contains batch
			boolean isBatch = ProcessDependenciesMojo.isBatch(applicationConfig, application);

			// Copies the appropriate log4j settings for the application
			copyLog4j(application, isBatch);
			
//			// For PEN, replace cfg-pen-batch-environment.properties
//			if (application.equals("pen")) {
//				getLog().info("##################################################################");
//				getLog().info("### PEN - Overwriting cfg-pen-batch-environment.properties ... ###");
//				getLog().info("##################################################################");
//				FileUtils.copyFile(new File(configDir + "/cfg-pen-provider-environment.properties"), new File(configDir + "/cfg-pen-batch-environment.properties"));
//			}

			if (application.equals("psak") || application.equals("pselv")) {

				// For PSAK and PSELV, copy PEN file dependencies
				copyDependencies();

				// For PSAK and PSELV, create a new role mapping file
				XMLOps.fixRoleMapping(new File(roleMappingDir + "/template.xml"), new File(earDir + "/META-INF/ibm-application-bnd.xmi"), new File(roleMappingDir + "/" + domain + "/" + zone + "/" + application + ".xml"));

				// For PSELV, do domain spesific file modifications
				if (application.equals("pselv")) {

					String webXMLPath = pselvWARDir + "/WEB-INF/web.xml";

					if (zone.equalsIgnoreCase("intern")) {
						
						XMLOps.fixContext(configDir + "/cfg-pen-context.xml");
						XMLOps.fixContext(configDir + "/cfg-pselv-context.xml");
						
						XMLOps.fixPSELVSessionTimeout(webXMLPath, pselvISSessionTimeout);
					}
					// Sensitiv sone
					else {
						
						XMLOps.fixPSELVLoginConfig(webXMLPath, pselvSSAccessDeniedURL);

						// We delete these files to make sure your unable to access the login page for pselv in sensitiv sone
						filesToDelete.add(pselvWARDir + "/tilleggsfunksjonalitet/innlogging-layout.xhtml");
						filesToDelete.add(pselvWARDir + "/tilleggsfunksjonalitet/innlogging.xhtml");
						filesToDelete.add(pselvWARDir + "/WEB-INF/flows/tilleggsfunksjonalitet/innlogging/innlogging-context.xml");
						filesToDelete.add(pselvWARDir + "/WEB-INF/flows/tilleggsfunksjonalitet/innlogging/innlogging-flow.xml");
					}
				}
			}

			// Configuration file exlusions

			if (application.equals("inst")) {
				File komptest65 = new File(configDir + "/cfg-environment.komptest65.properties");
				
				if (komptest65.exists())
					filesToDelete.add(komptest65.getAbsolutePath());
			}

			if (filesToDelete.size() > 0)
				deleteFiles(filesToDelete);

			// Reassemble archive files
			reassembleArchives();

		} catch (ParserConfigurationException e) {
			throw new MojoFailureException(e.getMessage());
		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage());
		} catch (TransformerException e) {
			throw new MojoFailureException(e.getMessage());
		} catch (SAXException e) {
			throw new MojoFailureException(e.getMessage());
		}

	}
	
	/**
	 * Only pen, psak, pselv or popp has logback.xml. 
	 * Other applications don't.
	 * @param application
	 * @return true if application has logback.xml
	 */
	private boolean hasLogback(String application){
		return application.equalsIgnoreCase("pen") ||
			   application.equalsIgnoreCase("psak") ||
			   application.equalsIgnoreCase("pselv") ||
			   application.equalsIgnoreCase("popp");
	}

	// Copy the appropriate log4j.properties files to the correct directories
	private void copyLog4j(String application, boolean isBatch) throws MojoFailureException {
		try {

			getLog().info("#########################################################");
			getLog().info("### Copying log4j and logback configuration files ... ###");
			getLog().info("#########################################################");

			// Application does not contain batch
			if (!isBatch) {

				// log4j.properties => <config folder>
				File l4jSource = new File(log4jDir + "/" + log4jFile);
				File l4jDestination = new File(configDir + "/" + "log4j.properties");
				NativeOps.copy(l4jSource, l4jDestination);
				
				if (hasLogback(application)){
					//logback.xml => <config folder>
					File logbackSource = new File(log4jDir + "/" + logbackFile);
					File logbackDestination = new File(configDir + "/" + "logback.xml");
					try{
						NativeOps.copy(logbackSource, logbackDestination);
					}catch (IOException e) {
						getLog().warn("Logback.xml file was not found. Check module config for " + this.application);
					}
				}
			}

			// Application contains batch
			else {

				// log4j.properties => <config folder>
				File l4jSource = new File(log4jDir + "/" + log4jFile);
				File l4jDestination = new File(configDir + "/" + "log4j.properties");
				NativeOps.copy(l4jSource, l4jDestination);

				// log4j.properties => <batch folder>
				File l4jBatchSource = new File(log4jDir + "/" + log4jBatchFile);
				File l4jBatchDestination = new File(batchDir + "/config/" + "log4j.properties");
				NativeOps.copy(l4jBatchSource, l4jBatchDestination);
				
				if (hasLogback(application)){
					//logback.xml => <config folder>
					File logbackSource = new File(log4jDir + "/" + logbackFile);
					File logbackDestination = new File(configDir + "/" + "logback.xml");
					try{
						NativeOps.copy(logbackSource, logbackDestination);
					}catch (IOException e) {
						getLog().warn("Logback.xml file was not found. Check module config for " + this.application);
					}
					
					if (!application.equalsIgnoreCase("popp")){ // popp doesnot have logback_batch.xml
						//logback_batch.xml => <config folder>
						File logbackBatchSource = new File(log4jDir + "/" + logbackBatchFile);
						File logbackBatchDestination = new File(batchDir + "/config/" + "logback.xml");
						try{
							NativeOps.copy(logbackBatchSource, logbackBatchDestination);
						}catch (IOException e) {
							getLog().warn("Logback_batch.xml file was not found. Check module config for " + this.application);
						}
					}
				}
			}

		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage());
		}

	}

	// Copies the PSAK/PSELV PEN-dependencies to the config directory
	private void copyDependencies() throws MojoFailureException {
		try {

			getLog().info("###################################################");
			getLog().info("### Copying PSAK/PSELV PEN-dependency files ... ###");
			getLog().info("###################################################");

			// Copy cfg-pen-ehcache.xml => config directory
			File penEHCacheSource = new File(penConfigDir + "/" + "cfg-pen-ehcache.xml");
			File penEHCacheDestination = new File(configDir + "/" + "cfg-pen-ehcache.xml");
			NativeOps.copy(penEHCacheSource, penEHCacheDestination);

			// Copy pen-constants.properties => config directory
			File penConstantsSource = new File(penConfigDir + "/" + "pen-constants.properties");
			File penConstantsDestination = new File(configDir + "/" + "pen-constants.properties");
			NativeOps.copy(penConstantsSource, penConstantsDestination);

			// Copy no-nav-pensjon-pen-resources.properties => config directory
			File penResourcesSource = new File(penConfigDir + "/" + "no-nav-pensjon-pen-resources.properties");
			File penResourcesDestination = new File(configDir + "/" + "no-nav-pensjon-pen-resources.properties");
			NativeOps.copy(penResourcesSource, penResourcesDestination);

			// Copy pen-loglevels.properties => config directory
			File penLogLevelSource = new File(penConfigDir + "/" + "pen-loglevels.properties");
			File penLogLevelDestination = new File(configDir + "/" + "pen-loglevels.properties");
			NativeOps.copy(penLogLevelSource, penLogLevelDestination);

			// Copy commons-logging.properties => config directory
			File commonsLoggingSource = new File(penConfigDir + "/" + "commons-logging.properties");
			File commonsLoggingDestination = new File(configDir + "/" + "commons-logging.properties");
			NativeOps.copy(commonsLoggingSource, commonsLoggingDestination);

			// Copy cfg-pen-context.xml => config directory
			File penContextSource = new File(penConfigDir + "/" + "cfg-pen-context.xml");
			File penContextDestination = new File(configDir + "/" + "cfg-pen-context.xml");
			NativeOps.copy(penContextSource, penContextDestination);

			// Delete the PEN config directory from the staging area
			getLog().info("##############################################");
			getLog().info("### Deleting the PEN config directory ...  ###");
			getLog().info("##############################################");
			FileUtils.deleteDirectory(penConfigDir);

		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage());
		}

	}

	
	// Removes the files marked for exclusion
	private void deleteFiles(ArrayList<String> filesToDelete) throws MojoFailureException {
		getLog().info("#######################################");
		getLog().info("### Deleting the excluded files ... ###");
		getLog().info("#######################################");

		for (String s : filesToDelete) {
			File toDelete = new File(s);
			if (toDelete.delete()) {
				getLog().info("Deleted the file: " + s);
			} else {
				throw new MojoFailureException("Unable to delete the file: " + s);
			}
		}
	}

	//Reassembles the archives, making them ready for deploy/bundle
	private void reassembleArchives() throws MojoFailureException {

		getLog().info("########################################################");
		getLog().info("### Reassembling archive files for bundle/deploy ... ###");
		getLog().info("########################################################");

		// Reassemble PSELV WAR
		if (application.equals("pselv")) {
			Archiver.createArchive(new File(pselvWARDir), new File(earDir), "war");
		}

		// Reassemble application EAR and expose properties
		File appEAR = Archiver.createArchive(new File(earDir), new File(stagingArea), "ear");

		String earName = appEAR.getName();
		String earPath = appEAR.getAbsolutePath();

		project.getProperties().put("earName", earName);
		getLog().info("[INFO] EXPOSED MAVEN PROPERTY: " + "earName" + " = " + earName);
		project.getProperties().put("earPath", earPath);
		getLog().info("[INFO] EXPOSED MAVEN PROPERTY: " + "earPath" + " = " + earPath);
	}
}
