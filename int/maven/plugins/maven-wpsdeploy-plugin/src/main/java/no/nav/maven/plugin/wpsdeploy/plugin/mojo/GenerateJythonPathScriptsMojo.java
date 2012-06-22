package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that sets environment variables on DMGR
 * 
 * 
 * @goal generate-jython-path-script
 * @requiresDependencyResolution
 */
public class GenerateJythonPathScriptsMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		HashMap<String, StringBuilder> scripts = new HashMap<String, StringBuilder>();
		
		StringBuilder configPahtsScript = new StringBuilder();
		configPahtsScript.append("def getDeployDependenciesPath(): return '"+ deployDependencies +"'\n");
		String configPahtsScriptPath = jythonScriptsPath +"/lib/configurationPaths.py"; //Change the name of the empty placeholder script in the lib folder if you change this name 
		scripts.put(configPahtsScriptPath, configPahtsScript); 

		
		StringBuilder enviromentUtilScript = new StringBuilder();
		enviromentUtilScript.append("def getEnviroment(): return '"+ environment +"'\n");
		enviromentUtilScript.append("def getEnvClass(): return '"+ envClass +"'\n");
		String enviromentUtilScriptPath = jythonScriptsPath +"/lib/deployEnviromentUtil.py"; //Change the name of the empty placeholder script in the lib folder if you change this name
		scripts.put(enviromentUtilScriptPath, enviromentUtilScript);
		
		for (Entry<String, StringBuilder> entry : scripts.entrySet()){
			StringBuilder script = entry.getValue();
			String path = entry.getKey();
			writeScript(script, path);
		}
	}

	private void writeScript(StringBuilder script, String path) throws MojoExecutionException{
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			try{
				bw.write(script.toString());
			} finally {
				bw.close();
			}
		} catch (IOException e) {
			throw new MojoExecutionException("[ERROR generating jython script]: " + e);
		}
		getLog().info("Done generating: " + path);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Generate the dynamic jython scripts?";
	}
}