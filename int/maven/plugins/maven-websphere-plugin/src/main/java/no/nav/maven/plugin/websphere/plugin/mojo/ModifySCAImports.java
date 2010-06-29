/**
 * 
 */
package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
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
 * 
 * Builds up the required string for the Python script by traversing the moduleconfig 
 * filestructure and parsing the relevant xml files, and launches the script.
 * 
 * @author test@example.com
 */

/**
 * TODO
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
			
			for (Artifact a : artifacts) {
				System.out.println("Trying to find file with name: " + a.getArtifactId());
				File found = getConfigurationFile(environment, envClass, a.getArtifactId(), moduleConfigHome);

				if (found.isFile()) {
					System.out.println("Found file: " + found);
					
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
		return "Set webservice endpoints";
	}

}
