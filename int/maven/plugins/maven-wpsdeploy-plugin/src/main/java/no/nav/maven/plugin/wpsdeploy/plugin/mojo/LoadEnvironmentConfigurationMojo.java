package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.PropertyUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that loads the environment configuration
 * 
 * @goal load-configuration
 * @requiresDependencyResolution
 */
public class LoadEnvironmentConfigurationMojo extends WebsphereUpdaterMojo {

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		try {
			exposeEnvironmentProperties();
		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage() + "\n[INFO]\n[INFO] Tip: Make sure you have extracted the bus configuration.");
		}
	}

	// Get the properties from the environment file
	protected void exposeEnvironmentProperties() throws FileNotFoundException, IOException {
		
		String environmentFile = busConfigurationDirectory + "/environments/" + environment + ".properties";

		PropertyUtils pf = new PropertyUtils(environmentFile, project);

		pf.exposeProperty("envClass", false);
		pf.exposeProperty("dmgrUsername", false);
		pf.exposeProperty("dmgrPassword", true);
		pf.exposeProperty("dmgrHostname", false);
		pf.exposeProperty("dmgrSOAPPort", false);
		pf.exposeProperty("linuxUser", false);
		pf.exposeProperty("linuxPassword", true);
		
		createJythonDeployEnviromentUtilScript();
		createJythonModuleConfigPathScript();
	}	
	
	private void createJythonModuleConfigPathScript() throws IOException  {
		String generatedJythonScriptPath = jythonScriptsDirectory +"/lib/moduleConfigPath.py"; //Change the name of the empty placeholder script in the lib folder if you change this name
		
		String script = "def getPath(): return '"+ moduleConfigHome +"'\n";

		BufferedWriter bw = new BufferedWriter(new FileWriter(generatedJythonScriptPath));
		try{
			bw.write(script);
		} finally {
			bw.close();
		}
	}

	private void createJythonDeployEnviromentUtilScript() throws IOException  {
		String generatedJythonScriptPath = jythonScriptsDirectory +"/lib/deployEnviromentUtil.py"; //Change the name of the empty placeholder script in the lib folder if you change this name
		
		String script = "def getEnviroment(): return '"+ environment +"'\n" +
				"def getEnvClass(): return '"+ envClass +"'\n";

		BufferedWriter bw = new BufferedWriter(new FileWriter(generatedJythonScriptPath));
		try{
			bw.write(script);
		} finally {
			bw.close();
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Load environment configuration";
	}

}
