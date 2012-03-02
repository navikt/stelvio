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
 * @author test@example.com
 * 
 * Builds up the required string for the Python script by traversing the moduleconfig filestructure 
 * and parsing the relevant xml files, and executes the script.
 * 
 * @goal apply-activationspecs
 * @requiresDependencyResolution
 */
public class ApplyActivationSpecs extends WebsphereUpdaterMojo {

	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {

		if (!isConfigurationLoaded()){
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}
		
		try {

			StringBuilder sb = new StringBuilder();

			File targetFolder = new File(deployableArtifactsHome);
			
			getLog().info("Checking target folder, " + targetFolder + ", to check which modules were installed.");
			
			FilenameFilter fnFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".ear");
				}
			};

			String[] deployedModules = targetFolder.list(fnFilter);

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
					
					String s = XMLUtils.parseActivationSpecs(found);

					if (s == null || s.equals("")) {
						continue;
					}
					
					if (sb.length() != 0){
						sb.append(";");
					}
					
					getLog().info("Found activation specification elements in " + found + ". Adding ...");
					sb.append(s);
				}
			}
			
			String activationSpecs = sb.toString();
			
			if (activationSpecs.equals("")) {
				getLog().info("No activation specifications found for the remaining modules in the EARSToDeploy folder.");
				return;
			}

			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("ModifyMaxConcurrencyAS.py " + "\"" + activationSpecs + "\"");
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

	protected String getGoalPrettyPrint() {
		return "Apply activation specifications";
	}

}
