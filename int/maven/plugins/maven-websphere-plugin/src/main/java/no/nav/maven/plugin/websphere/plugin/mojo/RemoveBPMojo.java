package no.nav.maven.plugin.websphere.plugin.mojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
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


	private String WSADMIN_HOME;
	private String EXEC;
	private String JACL_LOCATION;
	private ArrayList<String> modulesToRemove;

	
	public RemoveBPMojo(){
		
		/* 
		 * Sketchy to set this here, should be set automatically 
		 * by naming rules or other logic separate from the plugin. 
		 */ 
		
		modulesToRemove = new ArrayList<String>();
		modulesToRemove.add("nav-bsrv-frg-hentinstitusjonsoppholdliste");
		
	}
	
	/*
	 * Inherited method which is the one executed when running the script.
	 * 
	 * Builds up a commandline to send as a argument to the RemoveOldBPModule.py script with the appropriate 
	 * environment spesific information, customized for the current runtime environment and runs the removeBP method
	 * for each module specified in the constructor.
	 */
	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		
		// Sets the correct specs for the runtime environment
		if(Os.isFamily("windows") == true) {
			WSADMIN_HOME = widRuntime + "/bin/wsadmin.bat";
			EXEC = "cmd.exe /X /C";
		} else {
			WSADMIN_HOME = widRuntime + "/bin/wsadmin.sh";
			EXEC = "sh";
		}	
		
		JACL_LOCATION = scriptsHome + "/scripts/bpcTemplates.jacl";
		
		StringBuilder cmdString = new StringBuilder();
		
		cmdString.append("\"");
		cmdString.append(EXEC + " ");
		cmdString.append(WSADMIN_HOME + " ");
		cmdString.append("-host " + deploymentManagerHost + " ");
		cmdString.append("-port " + deploymentManagerPort + " ");
		cmdString.append("-user " + deploymentManagerUser + " ");
		cmdString.append("-password " + deploymentManagerPassword + " ");
		cmdString.append("-f " + JACL_LOCATION + " -uninstall");
		cmdString.append("\"");
		
		HashMap<String, String> modules = getModuleMap();
	    
		for (String artifact : modules.keySet()) {
			
			String[] orgArgs = commandLine.getArguments();
			Commandline cmdline = new Commandline();
			
			cmdline.setExecutable(commandLine.getExecutable());
			cmdline.addArguments(orgArgs);
			
			removeBP(cmdline, artifact, modules.get(artifact) ,cmdString.toString());
		}
	} 
	
	/*
	 * Builds up and executes the commandline from the incoming parameters.
	 */
	private final void removeBP(final Commandline commandLine, final String artifactId, String version, String cmdLine){
		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/RemoveOldBPModule.py " + cmdLine + " " + artifactId + " " + version);
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
