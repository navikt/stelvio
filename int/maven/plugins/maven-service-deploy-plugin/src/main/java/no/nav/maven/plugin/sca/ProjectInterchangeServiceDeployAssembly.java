package no.nav.maven.plugin.sca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;

public class ProjectInterchangeServiceDeployAssembly implements ServiceDeployAssembly {
	private static final String TYPE_WPS_LIBRARY_JAR = "wps-library-jar";
	private static final String TYPE_PROJECT_INTERCHANGE_ARTIFACT = "project-interchange";

	private ArtifactFactory artifactFactory;

	private ArtifactHandlerManager artifactHandlerManager;

	public ProjectInterchangeServiceDeployAssembly(ArtifactFactory artifactFactory, ArtifactHandlerManager artifactHandlerManager) {
		super();
		this.artifactFactory = artifactFactory;
		this.artifactHandlerManager = artifactHandlerManager;
	}

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

		Collection<Artifact> runtimeArtifacts = project.getRuntimeArtifacts();
		for (Artifact runtimeArtifact : runtimeArtifacts) {
			if (TYPE_WPS_LIBRARY_JAR.equals(runtimeArtifact.getType())) {
				Artifact artifact = artifactFactory.createArtifactWithClassifier(runtimeArtifact.getGroupId(), runtimeArtifact
						.getArtifactId(), runtimeArtifact.getVersion(), TYPE_PROJECT_INTERCHANGE_ARTIFACT,
						projectInterchangeClassifier);
				artifacts.add(artifact);
			} else {
				artifacts.add(runtimeArtifact);
			}

		}

		return artifacts;
	}

	public void addArtifacts(MavenProject project, Archiver archiver, Collection<Artifact> artifacts) throws ArchiverException {
		for (Artifact artifact : artifacts) {
			addArtifact(project, archiver, artifact);
		}
	}

	private void addArtifact(MavenProject project, Archiver archiver, Artifact artifact) throws ArchiverException {
		File artifactFile = artifact.getFile();
		if (TYPE_PROJECT_INTERCHANGE_ARTIFACT.equals(artifact.getType())) {
			archiver.addArchivedFileSet(artifactFile);
		} else {
			archiver.addFile(artifactFile, project.getArtifactId() + "/" + artifactFile.getName());
		}
	}

	private String getProjectInterchangeClassifier() {
		ArtifactHandler projectInterchangeArtifactHandler = artifactHandlerManager
				.getArtifactHandler(TYPE_PROJECT_INTERCHANGE_ARTIFACT);
		return projectInterchangeArtifactHandler.getClassifier();
	}
}
