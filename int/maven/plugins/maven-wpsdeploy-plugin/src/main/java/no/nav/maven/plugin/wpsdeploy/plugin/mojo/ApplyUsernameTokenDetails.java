/**
 * 
 */
package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.XMLUtils;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;
import org.xml.sax.SAXException;

/**
 * @author utvikler
 *
 * @goal apply-usernametokendetails
 * @requiresDependencyResolution
 */
public class ApplyUsernameTokenDetails extends WebsphereUpdaterMojo {

	/* (non-Javadoc)
	 * @see no.nav.maven.plugin.wpsdeploy.plugin.mojo.WebsphereUpdaterMojo#applyToWebSphere(org.codehaus.plexus.util.cli.Commandline)
	 */
	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
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
					
					String matchString = a.getArtifactId() + "-\\d+.\\d+.\\d+";
					Pattern p = Pattern.compile(matchString);

					if(p.matcher(module).find()) {
						moduleArtifact = a;
						break;
					}
				}
				
				
				
				if (moduleArtifact == null) {
					getLog().info("Module " + module + " is not deployed, skipping ...");
					continue;
				}
			
				File found = getConfigurationFile(environment, moduleArtifact.getArtifactId() + ".xml", moduleConfigHome);

				if (found != null) {
					
					String s = XMLUtils.parseUsernameTokenDetails(found);
					
					if (s == null || s.equals("")) {
						continue;
					}

					getLog().info("Found username token details in " + found + ". Adding ...");
										
					sb.append(s+"#");
				}
			}
			
			String usernametokenDetails = "";
			
			if (sb.length() > 0) {
				usernametokenDetails = sb.toString().substring(0, sb.toString().length()-1);	
			}
				
			
			if (usernametokenDetails.equals("")){
				getLog().info("No policy set bindings found for the remaining modules in the EARSToDeploy folder.");
				return;
			}
			
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("ModifyUserNameTokenGenerator.py " + "\"" + usernametokenDetails + "\"");
			wsadminCommandLine.addArg(arg);
			executeCommand(wsadminCommandLine);

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
		

	/* (non-Javadoc)
	 * @see no.nav.maven.plugin.wpsdeploy.plugin.mojo.WebsphereMojo#getGoalPrettyPrint()
	 */
	@Override
	protected String getGoalPrettyPrint() {
		return "Apply username token details";
	}

}
