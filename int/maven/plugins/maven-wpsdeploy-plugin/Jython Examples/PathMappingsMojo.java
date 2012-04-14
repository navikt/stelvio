package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.KeyNotFoundException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that sets environment variables on DMGR
 * 
 * 
 * @goal path-mappings
 * @requiresDependencyResolution
 */
public class PathMappingsMojo extends WebsphereUpdaterMojo {
	String delimiter = ",";
	BufferedWriter pathMappings;
	String generatedJythonScriptName = "fileMapPath.py";

	public final void applyToWebSphere(final Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		if (!isConfigurationLoaded()) {
			getLog().info("You can't run this step without having loaded the environment configuration. Skipping ...");
			return;
		}

		try{
			pathMappings = new BufferedWriter(new FileWriter(pathMappingFilePath));
			try{
				addMap("environmentFile", environmentFile);
				addMap("pathMappings", pathMappingFilePath);
				addMap("deployDependencies", targetDirectory +"/EarFilesToDeploy.csv");
				createJythonPathMappingsFilePathScript(pathMappingFilePath);
			} finally {
				pathMappings.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new MojoExecutionException(String.format("[ERROR] generating \"%s\": %s", pathMappingFilePath, e));
		}
	}

	private void addMap(String key, String value) throws IOException{
		if(key.contains(delimiter)){
			throw new IllegalArgumentException("A key cannot contain a \""+ delimiter +"\"!");
		}
		pathMappings.write(key + delimiter + value + '\n');
	}

	private void createJythonPathMappingsFilePathScript(String pathMappingsFilePath) throws MojoExecutionException {
		String generatedJythonScriptPath = jythonScriptsDirectory +"/lib/"+ generatedJythonScriptName;
		String script = "def getPath(): return '"+ pathMappingsFilePath +"'";

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(generatedJythonScriptPath));
			try{
				bw.write(script);
			} finally {
				bw.close();
			}
		} catch (IOException e) {
			throw new MojoExecutionException("[ERROR generating jython script]: " + e);
		}
	}

	public String getMapValue(String key) throws MojoExecutionException{
		try {			
			BufferedReader br = new BufferedReader(new FileReader(pathMappingFilePath));
			try {
				String line;
				while((line = br.readLine()) != null){
					String[] tmp = line.split(delimiter);
					String k = tmp[0];
					String v = tmp[1];
					if(key.equals(k)){
						return v;
					}
				}
				throw new KeyNotFoundException(String.format("The key %s doesn't exist in %s!", key, pathMappingFilePath));
			} finally {
				br.close();
			}

		} catch (IOException e) {
			throw new MojoExecutionException(String.format("[ERROR getting map value \"%s\"]: %s", key, e));
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Write all path mappings to map file";
	}
}