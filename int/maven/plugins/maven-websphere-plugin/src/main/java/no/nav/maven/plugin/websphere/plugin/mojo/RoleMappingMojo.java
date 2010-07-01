/**
 * 
 */
package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import no.nav.maven.plugin.websphere.plugin.utils.XMLUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;
import org.xml.sax.SAXException;

/**
 * @author test@example.com
 * 
 * Goal that finds and parses the ${fileName}.xml to accumulate a String of rolemappings, and executes the Jython script. 
 * 
 * @goal role-mapping
 * @requiresDependencyResolution
 */
public class RoleMappingMojo extends WebsphereUpdaterMojo {

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
			
			String roleMapping;
			String fileName = "cons.xml";

			File file = getConfigurationFile(environment, envClass, fileName, moduleConfigHome);
			
			if (file != null){
				getLog().info("[INFO] Found configuration file, " + file + ".");
			}

			roleMapping = XMLUtils.getRoleMappingString(environment, envClass, fileName, moduleConfigHome, file);
			
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("-f " + scriptsHome + "/scripts/RoleMapping.py" + " " + scriptsHome + " " + "\"" + roleMapping + "\"");
			commandLine.addArg(arg);

			executeCommand(commandLine);
			
		} catch (SAXException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (IOException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (ParserConfigurationException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		}

	}

	protected String getGoalPrettyPrint() {
		return "Map roles to users and groups, and set RunAs users";
	}

}
