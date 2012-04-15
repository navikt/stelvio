package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import no.nav.maven.plugin.wpsdeploy.plugin.utils.XMLUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;
import org.xml.sax.SAXException;

/**
 * @author test@example.com
 * 
 * Goal that finds and parses the ${fileName}.xml to accumulate a String of rolemappings, 
 * and executes the Jython script. 
 * 
 * @goal apply-rolemapping
 * @requiresDependencyResolution
 */
public class ApplyRoleMappingMojo extends WebsphereUpdaterMojo {

	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {

		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		
		try {
			
			String roleMapping;
			String fileName = "cons.xml";

			File file = getConfigurationFile(environment, fileName, moduleConfigHome);
			
			if (file != null){
				getLog().info("Found configuration file, " + file + ".");
			}

			roleMapping = XMLUtils.parseRoleMappings(environment, envClass, fileName, moduleConfigHome, file);
			
			if (roleMapping.equals("")){
				getLog().info("No rolemappings found for the remaining modules in the EARSToDeploy folder.");
				return;
			}
			
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("RoleMapping.py" + " " + "\"" + roleMapping + "\"");
			wsadminCommandLine.addArg(arg);

			executeCommand(wsadminCommandLine);
			
		} catch (SAXException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (IOException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (ParserConfigurationException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		}

	}

	protected String getGoalPrettyPrint() {
		return "Apply user/group mapping and RunAs";
	}

}
