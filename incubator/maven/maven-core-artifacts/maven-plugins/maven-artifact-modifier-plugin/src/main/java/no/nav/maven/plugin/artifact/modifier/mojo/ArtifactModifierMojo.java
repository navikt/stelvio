package no.nav.maven.plugin.artifact.modifier.mojo;

import java.io.File;
import java.util.Set;

import no.nav.maven.plugin.artifact.modifier.managers.ArchiveManager;
import no.nav.maven.plugin.artifact.modifier.managers.IArchiveManager;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;;


/**
 * Abstract class using the template pattern for child mojos.
 * 
 * @author test@example.com 
 */
public abstract class ArtifactModifierMojo extends AbstractMojo {

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
	
	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;

	protected IArchiveManager earArchiveManager;
	protected IArchiveManager jarArchiveManager;
	
	public void execute() throws MojoExecutionException, MojoFailureException {

		earArchiveManager = new ArchiveManager(earArchiver, earUnArchiver);
		jarArchiveManager = new ArchiveManager(jarArchiver, jarUnArchiver);
		doExecute();
	}
}
