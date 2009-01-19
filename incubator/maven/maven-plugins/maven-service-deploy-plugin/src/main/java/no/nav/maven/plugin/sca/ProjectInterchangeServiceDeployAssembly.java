package no.nav.maven.plugin.sca;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

public class ProjectInterchangeServiceDeployAssembly implements ServiceDeployAssembly {
	private static final String PROJECT_INTERCHANGE_ARTIFACT_TYPE = "project-interchange";

	private ArtifactFactory artifactFactory;

	private ArtifactHandlerManager artifactHandlerManager;

	@SuppressWarnings("unchecked")
	public Collection<Artifact> getArtifacts(MavenProject project) {
		String projectInterchangeClassifier = getProjectInterchangeClassifier();

		Collection<Artifact> artifacts = new ArrayList<Artifact>();

		Collection<Artifact> attachedArtifacts = project.getAttachedArtifacts();
		for (Artifact attachedArtifact : attachedArtifacts) {
			if (projectInterchangeClassifier.equals(attachedArtifact.getClassifier())) {
				artifacts.add(attachedArtifact);
				break;
			}
		}

		Collection<Dependency> runtimeDependencies = project.getRuntimeDependencies();
		for (Dependency runtimeDependency : runtimeDependencies) {
			Artifact artifact = artifactFactory.createArtifactWithClassifier(runtimeDependency.getGroupId(), runtimeDependency
					.getArtifactId(), runtimeDependency.getVersion(), PROJECT_INTERCHANGE_ARTIFACT_TYPE,
					projectInterchangeClassifier);
			artifacts.add(artifact);
		}

		return artifacts;
	}

	public void addArtifact(Archiver archiver, Artifact artifact) throws ArchiverException {
		archiver.addArchivedFileSet(artifact.getFile());
	}

	private String getProjectInterchangeClassifier() {
		ArtifactHandler projectInterchangeArtifactHandler = artifactHandlerManager
				.getArtifactHandler(PROJECT_INTERCHANGE_ARTIFACT_TYPE);
		return projectInterchangeArtifactHandler.getClassifier();
	}
}
