package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;
import java.util.Set;

import no.nav.maven.commons.managers.ArchiveManager;
import no.nav.maven.commons.managers.IArchiveManager;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;


/**
 * Abstract class for all mojos which contains the top level executeand some global
 * maven annotation based variables used throughout the class hierarchy.
 * 
 * @author test@example.com 
 */
public abstract class ArtifactModifierMojo extends AbstractMojo {
	
	/** @component */
	private Prompter prompter;

	/**
	 * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#ear}"
	 * @required
	 */
	protected Archiver earArchiver;

	/**
	 * @parameter expression="${component.org.codehaus.plexus.archiver.UnArchiver#ear}"
	 * @required
	 */
	protected UnArchiver earUnArchiver;

	/**
	 * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#jar}"
	 * @required
	 */
	protected Archiver jarArchiver;

	/**
	 * @parameter expression="${component.org.codehaus.plexus.archiver.UnArchiver#jar}"
	 * @required
	 */
	protected UnArchiver jarUnArchiver;	

	/**
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	protected String targetDirectory;

	/**
	 * @parameter expression="${project.basedir}"
	 * @required
	 */
	protected File baseDirectory;
	
	/**
	 * @parameter expression="${project.build.scriptSourceDirectory}"
	 * @required
	 */
	protected String scriptDirectory;

	/**
	 * @parameter expression="${project.dependencyArtifacts}"
	 * @required
	 */
	protected Set<Artifact> dependencyArtifacts;	
	
	/**
	 * @parameter expression="${project.artifacts}"
	 * @required
	 */
	protected Set<Artifact> artifacts;	
	
	/**
	 * @parameter expression="${environment}"
	 * @required
	 */
	protected String environment;
	
	/**
	 * @parameter expression="${interactiveMode}" default-value="false"
	 * @required
	 */
	protected Boolean interactiveMode;
	
	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;
	protected abstract String getGoalPrettyPrint();

	protected IArchiveManager earArchiveManager;
	protected IArchiveManager jarArchiveManager;
	
    /**
     * Creates the ear and jar archive managers and calls execute.
     *
     * @throws MojoExecutionException if the plugin failes to run. Causes an "BUILD ERROR" message
     * @throws MojoFailureException if the plugin failes to run. Causes an "BUILD FAILURE" message
     */
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		if(interactiveMode == true) {
			String answer=null;
			try {
				answer = prompter.prompt("Do you want to perform step \"" + getGoalPrettyPrint() + "\" (y/n)? ", "n");
			} catch (PrompterException e) {
				throw new MojoFailureException(e, "An error occured during prompt input","An error occured during prompt input");
			}
			if("n".equalsIgnoreCase(answer)) {
				getLog().info("Skipping step: " + getGoalPrettyPrint());
				return;
			}
		}
		
		earArchiveManager = new ArchiveManager(earArchiver, earUnArchiver);
		jarArchiveManager = new ArchiveManager(jarArchiver, jarUnArchiver);
		doExecute();
	}
}
