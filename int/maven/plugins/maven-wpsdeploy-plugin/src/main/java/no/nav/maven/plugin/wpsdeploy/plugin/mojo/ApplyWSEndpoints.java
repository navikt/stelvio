package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.XMLUtils;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;
import org.xml.sax.SAXException;

/**
 * @author test@example.com
 * 
 * Builds up the required string for the Python script by traversing the moduleconfig 
 * filestructure and parsing the relevant xml files, and executes the script.
 * 
 * @goal apply-wsendpoints
 * @requiresDependencyResolution
 */
public class ApplyWSEndpoints extends WebsphereUpdaterMojo {

	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {

		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		
		try {

			StringBuilder sb = new StringBuilder();
			
			File earFolder = new File(deployableArtifactsHome);
			
			getLog().info("Checking target folder " + earFolder + " to see which modules were installed.");
			
			FilenameFilter fnFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".ear");
				}
			};

			String[] deployedModules = earFolder.list(fnFilter);
			
			for (int i = 0; i < deployedModules.length; i++) {
				
				String module = deployedModules[i].replace(".ear", "");
				
				Artifact moduleArtifact = null;
				
				for (Artifact a : artifacts){
					if ( module.contains(a.getArtifactId()) ) {
						moduleArtifact = a;
					}
				}
				
				if (moduleArtifact == null) {
					getLog().info("Module " + module + " is not deployed, skipping ...");
					continue;
				}
			
				File found = getConfigurationFile(environment, moduleArtifact.getArtifactId() + ".xml", moduleConfigHome);

				if (found != null) {
					
					String s = XMLUtils.parseWebServiceEndpoints(found);
					
					if (s == null || s.equals("")) {
						continue;
					}

					getLog().info("Found webservice endpoints in " + found + ". Adding ...");
					
					if (sb.length() != 0) {
						sb.append(";" + s);
					} else {
						sb.append(s);
					}
				}
			}
			
			String wsEndpoints = sb.toString();
			
			if (wsEndpoints.equals("")){
				getLog().info("No webservice endpoints found for the remaining modules in the EARSToDeploy folder.");
				return;
			}
			
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("-f " + scriptsHome + "/scripts/ModifySCAImportsBinding.py" + " " + scriptsHome + " " + "\"" + wsEndpoints + "\"");
			commandLine.addArg(arg);
			executeCommand(commandLine);

		} catch (SAXException e) {
			throw new MojoFailureException(e.getMessage());
		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new MojoFailureException(e.getMessage());
		} catch (FactoryConfigurationError e) {
			throw new MojoFailureException(e.getMessage());
		}
	}

	protected String getGoalPrettyPrint() {
		return "Apply webservice endpoints";
	}

}
