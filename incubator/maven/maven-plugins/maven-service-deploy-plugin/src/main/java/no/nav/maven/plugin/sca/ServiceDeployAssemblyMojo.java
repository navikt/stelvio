package no.nav.maven.plugin.sca;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.AbstractArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;

/**
 * This plugin builds a zip-file (containing jar-files) that can be used as
 * input to serviceDeploy.
 * 
 * @author test@example.com
 * 
 * @goal service-deploy-assembly
 * @requiresDependencyResolution
 */
public class ServiceDeployAssemblyMojo extends AbstractMojo implements Contextualizable {
	/**
	 * @component
	 */
	private ArtifactResolver artifactResolver;

	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private Archiver archiver;

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression="${localRepository}"
	 * @required
	 * @readonly
	 */
	private ArtifactRepository localRepository;

	/**
	 * Defines the assembly type to use. Valid values are jar [default]
	 * (jar-files) and pi (project interchange).
	 * 
	 * @parameter default-value="jar"
	 */
	private String assemblyType;

	private PlexusContainer container;

	public void contextualize(Context context) throws ContextException {
		container = (PlexusContainer) context.get(PlexusConstants.PLEXUS_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws MojoExecutionException {
		try {
			ServiceDeployAssembly serviceDeployAssembly = lookupServiceDeployAssembly();

			Build build = project.getBuild();
			File outputFile = new File(build.getDirectory(), build.getFinalName() + "-sd" + ".zip");
			archiver.setDestFile(outputFile);

			Collection<Artifact> artifacts = serviceDeployAssembly.getArtifacts(project);
			for (Artifact artifact : artifacts) {
				File artifactFile = artifact.getFile();
				if (artifactFile == null) {
					resolveArtifact(artifact);
				}

				getLog().debug("Adding artifact " + artifact + " to assembly");
				serviceDeployAssembly.addArtifact(archiver, artifact);
			}

			archiver.createArchive();
		} catch (ArchiverException e) {
			throw new MojoExecutionException("Error creating service deploy assembly", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating service deploy assembly", e);
		}
	}

	private ServiceDeployAssembly lookupServiceDeployAssembly() throws MojoExecutionException {
		try {
			if (!"jar".equals(assemblyType) && !"pi".equals(assemblyType)) {
				throw new MojoExecutionException("Illegal assembly type (" + assemblyType + "). Must be one of [jar,pi].");
			}
			return (ServiceDeployAssembly) container.lookup(ServiceDeployAssembly.class.getName(), assemblyType);
		} catch (ComponentLookupException e) {
			throw new MojoExecutionException("Error looking up ServiceDeployAssembly component", e);
		}
	}

	private void resolveArtifact(Artifact artifact) throws MojoExecutionException {
		try {
			artifactResolver.resolve(artifact, project.getRemoteArtifactRepositories(), localRepository);
		} catch (AbstractArtifactResolutionException e) {
			throw new MojoExecutionException("Error resolving artifact", e);
		}
	}
}
