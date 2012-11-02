package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.SamhandlerParser;

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

		getLog().info("Turning the properties tree ("+ environmentPropertiesTree +") into the properties file: "+ mainPropertiesFilepath);
		
		configurationRequierdToProceed();

		Properties prop = new Properties();

		try {
			loadDir(environmentPropertiesTree + "/" + envClass + "/" + envName, prop);
			loadDir(environmentPropertiesTree + "/" + envClass, prop);
			loadDir(environmentPropertiesTree, prop);
			loadDir(nonenvironmentProperties, prop);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Propblemer med å lese properties filer i "+ environmentPropertiesTree);
		}
		
		try{
			writeProps(prop, mainPropertiesFilepath);
		} catch (IOException e) {
			throw new MojoExecutionException("Propblemer med å skrive til properties filen "+ mainPropertiesFilepath, e);
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
		store(props, filePath);
		getLog().info("Wrote properties into "+filePath);
	}
	
	private void store(Properties props, String propertyFilePath) throws FileNotFoundException {

	    PrintWriter pw = new PrintWriter(propertyFilePath);

	    for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
	        String key = (String) e.nextElement();
	        pw.println(key + "=" + props.getProperty(key));
	    }
	    pw.close();
	}

	protected String getGoalPrettyPrint() {
		return "Apply activation specifications";
	}
}
