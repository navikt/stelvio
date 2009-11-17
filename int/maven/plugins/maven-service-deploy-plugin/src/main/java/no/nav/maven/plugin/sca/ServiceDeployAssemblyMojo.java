package no.nav.maven.plugin.sca;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
 * This plugin builds an assembly (zip-file) that can be used as input to
 * serviceDeploy.
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
	 * Defines the assembly type to use. Valid values are zip [default] and pi
	 * (project interchange).
	 * 
	 * @parameter default-value="zip"
	 */
	private String assemblyType;

	private PlexusContainer container;

	private Map<String, MavenProject> projectReferences;

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
					artifact = resolveArtifact(artifact);
				}

				getLog().debug("Adding artifact " + artifact + " to assembly");
				serviceDeployAssembly.addArtifact(project, archiver, artifact);
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
			if (!"zip".equals(assemblyType) && !"pi".equals(assemblyType)) {
				throw new MojoExecutionException("Illegal assembly type (" + assemblyType + "). Must be one of [zip,pi].");
			}
			return (ServiceDeployAssembly) container.lookup(ServiceDeployAssembly.class.getName(), assemblyType);
		} catch (ComponentLookupException e) {
			throw new MojoExecutionException("Error looking up ServiceDeployAssembly component", e);
		}
	}

	private Artifact resolveArtifact(Artifact artifact) throws MojoExecutionException {
		// Trying to resolve artifact from project references first. This will
		// work if the artifact to resolve is built as part of this
		// (multi-module) build.
		Artifact resolvedArtifact = resolveArtifactFromProjectReferences(artifact);
		if (resolvedArtifact != null) {
			return resolvedArtifact;
		}
		return resolveArtifactFromRepositories(artifact);
	}

	@SuppressWarnings("unchecked")
	private Artifact resolveArtifactFromProjectReferences(Artifact artifact) throws MojoExecutionException {
		MavenProject projectReference = getProjectReference(artifact);
		if (projectReference != null) {
			getLog().info("Matching project reference found: " + projectReference.getBasedir());
			Artifact projectReferenceArtifact = projectReference.getArtifact();
			if (projectReferenceArtifact.equals(artifact)) {
				getLog().debug("Matching artifact found in project reference: " + projectReferenceArtifact);
				return projectReferenceArtifact;
			}
			for (Artifact attachedArtifact : (Collection<Artifact>) projectReference.getAttachedArtifacts()) {
				if (attachedArtifact.equals(artifact)) {
					getLog().debug("Matching artifact found in project reference: " + projectReferenceArtifact);
					return attachedArtifact;
				}
			}
			throw new MojoExecutionException(
					"Matching project reference found, but a matching artifact was not found in matching project reference.");
		} else {
			return null;
		}
	}

	private Artifact resolveArtifactFromRepositories(Artifact artifact) throws MojoExecutionException {
		try {
			artifactResolver.resolve(artifact, project.getRemoteArtifactRepositories(), localRepository);
			return artifact;
		} catch (AbstractArtifactResolutionException e) {
			throw new MojoExecutionException("Error resolving artifact", e);
		}
	}

	private MavenProject getProjectReference(Artifact artifact) {
		if (projectReferences == null) {
			projectReferences = new HashMap<String, MavenProject>();
			addProjectReferences(projectReferences, project);
		}
		String projectReferenceId = getProjectReferenceId(artifact.getGroupId(), artifact.getArtifactId(), artifact
				.getVersion());
		return projectReferences.get(projectReferenceId);
	}

	@SuppressWarnings("unchecked")
	private static void addProjectReferences(Map<String, MavenProject> projectReferences, MavenProject project) {
		for (MavenProject projectReference : ((Map<String, MavenProject>) project.getProjectReferences()).values()) {
			String projectReferenceId = getProjectReferenceId(projectReference.getGroupId(), projectReference.getArtifactId(),
					projectReference.getVersion());
			projectReferences.put(projectReferenceId, projectReference);
			addProjectReferences(projectReferences, projectReference);
		}
	}

	private static String getProjectReferenceId(String groupId, String artifactId, String version) {
		return groupId + ":" + artifactId + ":" + version;
	}
}
