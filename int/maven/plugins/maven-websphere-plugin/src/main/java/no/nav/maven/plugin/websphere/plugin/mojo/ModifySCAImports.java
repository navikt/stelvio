/**
 * 
 */
package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import no.nav.maven.plugin.websphere.plugin.utils.XMLUtils;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;
import org.xml.sax.SAXException;

/**
 * @author test@example.com
 * 
 * Builds up the required string for the Python script by traversing the moduleconfig 
 * filestructure and parsing the relevant xml files, and launches the script.
 * 
 * @goal modify-imports
 * @requiresDependencyResolution
 */
public class ModifySCAImports extends WebsphereUpdaterMojo {

	/**
	 * @parameter expression="${envClass}"
	 * @required
	 */
	protected String envClass;

	/**
	 * @parameter expression="${environment}"
	 * @required
	 */
	protected String environment;

	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {

		try {

			StringBuilder sb = new StringBuilder();
			
			File targetFolder = new File(deployableArtifactsHome);
			
			getLog().info("[INFO] Checking target folder, " + targetFolder + ", to check which modules were installed.");
			
			FilenameFilter fnFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.endsWith(".ear"))
						return true;
					return false;
				}
			};

			String[] deployedModules = targetFolder.list(fnFilter);

			for (int i = 0; i < deployedModules.length; i++) {
				
				String module = deployedModules[i].replace(".ear", "");
				
				Artifact moduleArtifact = null;
				
				for (Artifact a : artifacts){
					if ( module.contains(a.getArtifactId()) ) {
						moduleArtifact = a;
					}
				}
				
				if (moduleArtifact == null) {
					getLog().info("[INFO] Module " + module + " is not deployed, skipping ...");
					continue;
				}
			
				System.out.println("[INFO] Trying to find file with name: " + module);
				File found = getConfigurationFile(environment, envClass, moduleArtifact.getArtifactId(), moduleConfigHome);

				if (found.isFile()) {
					System.out.println("[INFO] Found file: " + found);
					
					String s = XMLUtils.parseWebServiceEndpoints(found);
					
					if (s == null || s.equals("")) {
						getLog().info("[INFO]: No webservice endpoint elements found in " + found + ". Skipping ...");
						continue;
					}
					sb.append(s);
				}
			}

		} catch (SAXException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (IOException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (ParserConfigurationException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (FactoryConfigurationError e) {
			throw new MojoFailureException("[ERROR]: " + e);
		}
	}

	protected String getGoalPrettyPrint() {
		return "Apply webservice endpoints";
	}

}
