package no.nav.maven.plugin.sca;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

public class ProjectInterchangeServiceDeployAssembly implements ServiceDeployAssembly {
	private static final String PROJECT_INTERCHANGE_ARTIFACT_TYPE = "project-interchange";
	private static final String PROJECT_INTERCHANGE_CLASSIFIER = "pi";

	private ArtifactFactory artifactFactory;

	@SuppressWarnings("unchecked")
	public Collection<Artifact> getArtifacts(MavenProject project) {
		Collection<Artifact> artifacts = new ArrayList<Artifact>();

		Collection<Artifact> attachedArtifacts = project.getAttachedArtifacts();
		for (Artifact attachedArtifact : attachedArtifacts) {
			if (PROJECT_INTERCHANGE_CLASSIFIER.equals(attachedArtifact.getClassifier())) {
				artifacts.add(attachedArtifact);
				break;
			}
		}

		Collection<Dependency> runtimeDependencies = project.getRuntimeDependencies();
		for (Dependency runtimeDependency : runtimeDependencies) {
			Artifact artifact = artifactFactory.createArtifactWithClassifier(runtimeDependency.getGroupId(), runtimeDependency
					.getArtifactId(), runtimeDependency.getVersion(), PROJECT_INTERCHANGE_ARTIFACT_TYPE,
					PROJECT_INTERCHANGE_CLASSIFIER);

			artifacts.add(artifact);
		}

		return artifacts;
	}

	public void addArtifact(Archiver archiver, Artifact artifact) throws ArchiverException {
		archiver.addArchivedFileSet(artifact.getFile());
	}
}
