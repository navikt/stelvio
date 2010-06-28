/**
 * 
 */
package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import no.nav.maven.plugin.websphere.plugin.utils.XMLUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;
import org.xml.sax.SAXException;

/**
 * @author utvikler
 */
 
 /**
  * Goal that contacts the deployment manager to deploy resources for artifacts.
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
			
		String roleMapping;
		String fileName = "cons.xml";
				
		try {
			roleMapping = XMLUtils.getRoleMappingString(environment, envClass, fileName, moduleConfigHome);
		} catch (SAXException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (IOException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		} catch (ParserConfigurationException e) {
			throw new MojoFailureException("[ERROR]: " + e);
		}
		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/RoleMapping.py" + " " + scriptsHome + " " + roleMapping);
		commandLine.addArg(arg);
		executeCommand(commandLine);	
	}
	
	protected String getGoalPrettyPrint() {
		return "Map roles to users and groups, and set RunAs users";
	}

}
