package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that removes the modules containing short-running business processes.
 * Note: The modules which are removed are defined statically inside this class' constructor.
 * 
 * @author test@example.com
 * 
 * @goal remove-bp
 * @requiresDependencyResolution
 */ 

public class RemoveBPMojo extends WebsphereUpdaterMojo {

	private ArrayList<String> modulesToRemove;

	/*
	 * Inherited method which is the one executed when running the script.
	 * 
	 * Builds up a command line to send as a argument to the RemoveOldBPModule.py script with the appropriate 
	 * environment specific information, customized for the current runtime environment and runs the removeBP method
	 * for each module specified in the constructor.
	 */
	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		
		modulesToRemove = new ArrayList<String>();
		modulesToRemove.add("nav-bsrv-frg-hentinstitusjonsoppholdliste");
		
		for (Artifact a : artifacts){
			if (a.getArtifactId().contains("-microflow-")){
				modulesToRemove.add(a.getArtifactId());
			}
		}
		
		HashMap<String, String> modules = getModuleMap();
	    
		for (String artifact : modules.keySet()) {
			
			String[] orgArgs = commandLine.getArguments();
			Commandline cmdline = new Commandline();
			
			cmdline.setExecutable(commandLine.getExecutable());
			cmdline.addArguments(orgArgs);
			
			removeBP(cmdline, artifact, modules.get(artifact));
		}
	} 
	
	/*
	 * Builds up and executes the commandline from the incoming parameters.
	 */
	private final void removeBP(final Commandline commandLine,final String artifactId, String version) throws MojoExecutionException { 
		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("RemoveOldBPModule.py " + artifactId + " " + version);
		commandLine.addArg(arg);
		executeCommand(commandLine);
	}

	/*
	 * Returns a HashMap<String, String> containing 
	 * <[WHICH APP TO UNINSTALL], [WHICH VERSION OF THIS APP IS IN THIS RELEASE]> 
	 */
	private final HashMap<String, String> getModuleMap() {
		
		HashMap<String, String> modulesMap = new HashMap<String, String>();
		
		Set<Artifact> releaseArtifacts = artifacts;
		
		for (String module : modulesToRemove){
			for (Artifact a : releaseArtifacts) {
				if (a.getArtifactId().contains(module)){
					modulesMap.put(module, a.getVersion());
				}
			}
		}
		
		return modulesMap;
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Remove short-running business processes";
	}
	

}
