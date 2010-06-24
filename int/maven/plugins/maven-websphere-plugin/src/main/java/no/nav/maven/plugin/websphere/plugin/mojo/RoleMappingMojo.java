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
 * @author utvikler
 *
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
	
	/* (non-Javadoc)
	 * @see no.nav.maven.plugin.websphere.plugin.mojo.WebsphereUpdaterMojo#applyToWebSphere(org.codehaus.plexus.util.cli.Commandline)
	 */
	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		
		String separator = System.getProperty("file.separator");
		String moduleconfigPath = "target" + separator + "bus-config" + separator + "moduleconfig";
		String env = "";
		String fileName = "cons.xml";
		String roleMapping = "";
		try {
			roleMapping = XMLUtils.getRoleMappingString(env, envClass, fileName, moduleconfigPath);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + separator + "scripts" + separator + "RoleMapping.py" + " " + scriptsHome + " " + roleMapping);
		commandLine.addArg(arg);
		executeCommand(commandLine);	
	}
	
	

	/* (non-Javadoc)
	 * @see no.nav.maven.plugin.websphere.plugin.mojo.WebsphereMojo#getGoalPrettyPrint()
	 */
	@Override
	protected String getGoalPrettyPrint() {
		// TODO Auto-generated method stub
		return "Map roles to users and groups, and set RunAs users";
	}

}
