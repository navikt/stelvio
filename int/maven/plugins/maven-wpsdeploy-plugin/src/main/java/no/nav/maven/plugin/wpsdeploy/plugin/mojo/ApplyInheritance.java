package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * 
 * @goal apply-inheritance
 * @requiresDependencyResolution
 */
public class ApplyInheritance extends WebsphereUpdaterMojo {

	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {

		getLog().info("Turning the properties tree ("+ propertiesTree +") into the properties file: "+ propertiesPath);
		
		configurationRequierdToProceed();

		Properties prop = new Properties();

		try {
			loadDir(propertiesTree + "/" + envClass.toLowerCase() + "/" + environment.toLowerCase(), prop);
			loadDir(propertiesTree + "/" + envClass.toLowerCase(), prop);
			loadDir(propertiesTree, prop);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Propblemer med å lese properties filer i "+ propertiesTree);
		}
		
		try{
			writeProps(prop, propertiesPath);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Propblemer med å skrive til properties filen "+ propertiesPath);
		}
	}

	private void loadDir(String path, Properties props) throws IOException{
		File dir = new File(path);
		if(dir.exists()){
			boolean found = false;
			for(File propFile : dir.listFiles()){
				if(propFile.getName().toLowerCase().endsWith(".properties")){
					props.load(new FileInputStream(propFile));
					found = true;
				}
			}
			if(found) { getLog().info("Using properties in dir: "+path); }
			else { getLog().info("No properties files found in: "+path); }
		} else {
			getLog().info(path + " doesn't exists, skipping.");
		}
	}

	private void writeProps(Properties props, String filePath) throws IOException{
		File fileObj = new File(filePath);
		if(!fileObj.exists()){
			fileObj.createNewFile();
		}
		props.store(new FileOutputStream(filePath), null);
		getLog().info("Wrote properties into "+filePath);
	}

	protected String getGoalPrettyPrint() {
		return "Apply activation specifications";
	}
}
