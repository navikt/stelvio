package no.nav.maven.plugin.websphere.plugin.mojo;

import java.util.ArrayList;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that removes the modules containing short-running business processes.
 * Note: The modules which are removed are defined statically inside this class.
 * 
 * @author test@example.com
 * 
 * @goal remove-bp
 * @requiresDependencyResolution
 */ 

public class RemoveBPMojo extends WebsphereUpdaterMojo {

	private ArrayList<String> modulesToRemove;
//	private String WSADMIN_HOME;
	private String JACL_LOCATION;
	private String CMDLINE;
	
	@Override
	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
//		
//		// Sets the correct specs for the runtime environment
//		if(Os.isFamily("windows") == true) {
//			WSADMIN_HOME = widRuntime + "/bin/wsadmin.bat";
//		} else {
//			WSADMIN_HOME = widRuntime + "/bin/wsadmin.sh";
//		}	
		
		JACL_LOCATION = scriptsHome + "/scripts/bpcTemplates.jacl";
		
		getLog().info("[### DEBUG ###] inside constructor - JACL_LOCATION = " + JACL_LOCATION );
		
		
		ArrayList<String> modules = getModulesToRemove();
	    
		for (String artifactId : modules) {
			removeBP(commandLine, artifactId);
		}
	} 

	private final ArrayList<String> getModulesToRemove(){
		
		/* 
		 * Sketchy to set these here, should be set automatically 
		 * by naming rules or other logic separate from the plugin. 
		 */ 
	
		modulesToRemove.add("nav-bproc-pen-ppen010");
		modulesToRemove.add("nav-bsrv-frg-hentinstitusjonsoppholdliste");
		
		return modulesToRemove;
	}
	
	private final void removeBP(final Commandline commandLine, final String artifactId){
		
		getLog().info("[INFO] Stopping and uninstalling: " + artifactId);
		
		getLog().info("[### DEBUG ###] inside removeBP - START commandLine.toString() = " + commandLine.toString() );
		
		CMDLINE = commandLine.toString();
		CMDLINE += " -f " + JACL_LOCATION + " -uninstall";
		
		getLog().info("[### DEBUG ###] inside removeBP - CMDLINE = " + CMDLINE );
		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/RemoveOldBPModule.py " + CMDLINE + artifactId);
		commandLine.addArg(arg);
		
		getLog().info("[### DEBUG ###] inside removeBP - EXEC commandLine.toString() = " + commandLine.toString() );
		executeCommand(commandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Remove short-running business processes";
	}
	

}
