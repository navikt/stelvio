package no.stelvio.maven.plugins;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import no.stelvio.maven.build.plugin.utils.ApplicationNameResolve;
import no.stelvio.maven.build.plugin.utils.MavenCommandLine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Goal which updates pom files: removes/puts back SNAPSHOT and so on
 *
 * @goal updatePom
 * 
 * @author test@example.com
 */
public class UpdatePoms extends AbstractMojo{

	private String release_version;
	private String snapshot_version;
	private String next_version;
	
	/**
	 * Action to perform: remove, put_back or increase
	 * @parameter action="${action}"
	 * @required
	 */
	private String action;
	
	/**
	 * Project name - BUILD_TEST
	 * 
	 * @parameter expression="${build}"
	 * @required
	 */
	private String build;
	
	/**
	 * Folder where all CC streams are located
	 * 
	 * @parameter expression="${ccProjectDir}"
	 * @required
	 */
	private String ccProjectDir;
	
	/**
	 * Integration stream tag
	 * 
	 * @parameter expression="${intStream}" default-value="_int"
	 */
	private String intStream;
	
	/**
	 * Development stream tag
	 * 
	 * @parameter expression="${devStream}" default-value="_Dev"
	 */
	private String devStream;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_removeSS}" default-value=true
	 */
	private boolean perform_removeSS;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_returnSS}" default-value=true
	 */
	private boolean perform_returnSS;
	
	/**
	 * Whether this goal should be done
	 * @parameter expression="${perform_increaseSS}" default-value=true
	 */
	private boolean perform_increaseSS;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {	
		this.snapshot_version = this.doGetPomVersion();
		this.release_version = this.snapshot_version.substring(0,this.snapshot_version.indexOf("-SNAPSHOT"));
		this.next_version = this.increaseVersion(this.release_version) + "-SNAPSHOT";
		this.getLog().info("Retrived version from dev stream: " + this.snapshot_version);
		this.getLog().info("Next released version: " + this.release_version);
		this.getLog().info("Next SNAPSHOT version: " + this.next_version);
		boolean fail = false;
		if (action.equalsIgnoreCase("remove")){
			if (perform_removeSS) fail = this.removeSnapshot() != 0;
			else this.getLog().warn("Skipping remove SNAPSHOT");
		} else if (action.equalsIgnoreCase("put_back")){
			if (perform_returnSS) fail = this.putbackSnapshot() != 0;
			else this.getLog().warn("Skipping put back SNAPSHOT");
		} else if (action.equalsIgnoreCase("increase")){
			if (perform_increaseSS)	fail = this.doIncreaseVersion() != 0;
			else this.getLog().warn("Skipping increment SNAPSHOT");
		}
		if (fail) throw new MojoExecutionException("Could not update versions in pom files.");
	}
	
	/**
	 * This method increases a version number from f.eks. 1.0.0 to 1.0.1
	 * @param version - version number to increase
	 * @return increased value
	 */
	private String increaseVersion(String version){
		version = version.replace(".", ",");
		String [] parts = version.split(",");
		int last_number = Integer.parseInt(parts[parts.length-1])+1;
		parts[parts.length-1] = ""+last_number;
		StringBuilder result = new StringBuilder();
		for (String part : parts) result.append(part + ".");
		result.setLength(result.length()-1);
		return result.toString();
	}
	
	/**
	 * call to maven versions plugin from int stream
	 * @return
	 */
	private int removeSnapshot() throws MojoFailureException {
		this.getLog().info("-------------------------");
		this.getLog().info("--- Removing SNAPSHOT ---");
		this.getLog().info("-------------------------");
		this.getLog().info("Changing version to "+this.release_version);
		String workDir = this.ccProjectDir+this.build+this.intStream+"/"+ApplicationNameResolve.ApplicationFromProject(build)+"/layers";
		String command = "versions:set -DgenerateBackupPoms=false -DnewVersion=" + this.release_version;
		//return MavenCommandLine.PerformMavenCommand(workDir, command);
		return 0;
	}
	
	/**
	 * call to maven versions plugin from int stream
	 * @return
	 */
	private int putbackSnapshot() throws MojoFailureException {
		this.getLog().info("-----------------------");
		this.getLog().info("--- Adding SNAPSHOT ---");
		this.getLog().info("-----------------------");
		this.getLog().info("Changing version to "+this.snapshot_version);
		String workDir = this.ccProjectDir+this.build+this.intStream+"/"+ApplicationNameResolve.ApplicationFromProject(build)+"/layers";
		String command = "versions:set -DgenerateBackupPoms=false -DnewVersion=" + this.snapshot_version;
		//return MavenCommandLine.PerformMavenCommand(workDir, command);
		return 0;
	}
	
	/**
	 * call to maven versions plugin from dev stream
	 * @return
	 */
	private int doIncreaseVersion() throws MojoFailureException {
		this.getLog().info("--------------------------------------");
		this.getLog().info("--- Updating version in Dev stream ---");
		this.getLog().info("--------------------------------------");
		this.getLog().info("Changing version to "+this.next_version);
		String workDir = this.ccProjectDir+this.build+this.devStream+"/"+ApplicationNameResolve.ApplicationFromProject(build)+"/layers";
		String command = "versions:set -DgenerateBackupPoms=false -DnewVersion=" + this.next_version;
		//return MavenCommandLine.PerformMavenCommand(workDir, command);
		return 0;
	}
	
	private String doGetPomVersion() throws MojoFailureException{
		String version = "";
		try {
			String workDir = this.ccProjectDir+this.build+this.devStream+"/"+ApplicationNameResolve.ApplicationFromProject(build)+"/layers";
			File pomFile = new File(workDir + "/pom.xml");
			Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pomFile);
			xml.getDocumentElement().normalize();
			NodeList versionNode = xml.getElementsByTagName("version");
			version = versionNode.item(0).getChildNodes().item(0).getNodeValue();
		} catch (Exception e) {
			throw new MojoFailureException("Could not retrive version from pom.xml",e);
		}
		return version;
	}
}
