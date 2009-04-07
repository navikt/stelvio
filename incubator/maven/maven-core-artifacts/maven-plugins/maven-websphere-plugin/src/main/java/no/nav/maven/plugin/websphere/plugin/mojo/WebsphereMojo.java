package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.util.Set;

import no.nav.maven.commons.managers.ArchiveManager;
import no.nav.maven.commons.managers.IArchiveManager;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;


/**
 * Abstract class using the template pattern for child mojos.
 * 
 * @author test@example.com 
 */
public abstract class WebsphereMojo extends AbstractMojo {

	/** @component */
	private Prompter prompter;
	
	/**
     * The Maven Project Object
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * The Maven Session Object
     *
     * @parameter expression="${session}"
     * @required
     * @readonly
     */
    protected MavenSession session;

    /**
     * The Maven PluginManager Object
     *
     * @component
     * @required
     */
    protected PluginManager pluginManager;
	
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
	 * @parameter expression="${interactiveMode}" default-value="false"
	 * @required
	 */
	protected Boolean interactiveMode;
	
	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;
	protected abstract String getGoalPrettyPrint();

	protected IArchiveManager earArchiveManager;
	protected IArchiveManager jarArchiveManager;
	
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
		
		jarArchiveManager = new ArchiveManager(jarArchiver, jarUnArchiver);
		doExecute();
	}
}
